package com.jsoup.api.spider;

import com.jsoup.api.spider.enums.Source;
import com.jsoup.api.spider.model.PageSnapshot;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 爬虫
 *
 * @author kunge
 */
public interface Crawler {

    String name();

    Spider newSpider();

    Analyzer analyzer();

    // 爬虫接口
    interface Spider {

        String nextLink();
    }

    // 来源接口
    interface Analyzer {

        /**
         * 组装页面快照信息
         *
         * @param link          网页地址
         * @param replaceImgSrc 需替换图片对象
         * @return 页面快照集合
         */
        default List<PageSnapshot> parsePageSnapshot(String link, Function<String, String> replaceImgSrc) {
            return document(link).stream().map(document -> parsePageSnapshot(document, replaceImgSrc)).collect(Collectors.toList());
        }

        /**
         * 组装页面快照信息
         *
         * @param document      文档对象
         * @param replaceImgSrc 需替换图片对象
         * @return 页面快照
         */
        default PageSnapshot parsePageSnapshot(Document document, Function<String, String> replaceImgSrc) {
            PageSnapshot pageSnapshot = new PageSnapshot();
            pageSnapshot.setSource(source(document));
            pageSnapshot.setTitle(title(document));
            pageSnapshot.setAuthor(author(document));
            pageSnapshot.setContent(content(document, replaceImgSrc));
            pageSnapshot.setSourceLink(document.location());
            return pageSnapshot;
        }

        /**
         * 根据url地址，获取文档集合
         *
         * @param link url地址
         * @return 文档集合
         */
        default List<Document> document(String link) {
            try {
                return List.of(Jsoup.connect(link).get());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * 获取文档名称
         *
         * @param document
         * @return
         */
        default String title(Document document) {
            return document.title();
        }

        /**
         * 获取默认作者
         *
         * @param document 文档对象
         * @return 作者名称
         */
        default String author(Document document) {
            Source source = source(document);
            return Objects.isNull(source) ? "" : source.defaultAuthor;
        }

        /**
         * 根据文档地址信息，获取枚举对象
         *
         * @param document 文档
         * @return 枚举对象
         */
        default Source source(Document document) {
            for (Source source : Source.values()) {
                if (document.baseUri().contains(source.site)) {
                    return source;
                }
            }
            return null;
        }

        /**
         * 封装内容
         *
         * @param document 文档对象
         * @return 内容
         */
        String content(Document document);

        /**
         * 将图片替换到内容中
         *
         * @param document      文档对象
         * @param replaceImgSrc 需替换图片对象
         * @return 内容
         */
        default String content(Document document, Function<String, String> replaceImgSrc) {
            if (replaceImgSrc == null) {
                return content(document);
            }
            document.select("img[src]")
                    .stream()
                    .filter(element -> !element.attr("src").startsWith("data"))
                    .forEach(element -> element.attr("src", replaceImgSrc.apply(element.attr("abs:src"))));
            return content(document);
        }

        /**
         * 模拟请求，获取请求结果
         *
         * @param url         网页地址
         * @param contentType 内容类型
         * @return 字节数组
         */
        static byte[] open(String url, String... contentType) {
            try {
                HttpClient httpClient = HttpClient.newBuilder()
                        .build();
                HttpRequest request = HttpRequest.newBuilder()
                        .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36")
                        .uri(new URI(url))
                        .GET()
                        .build();

                HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
                if (contentType != null && contentType.length > 0) {
                    boolean match = false;
                    for (String c : contentType) {
                        Optional<String> contentTypeHeader = response.headers().firstValue("Content-Type");
                        if (match = contentTypeHeader.isPresent()
                                && StringUtils.isNotBlank(contentTypeHeader.get())
                                && contentTypeHeader.get().contains(c)) {
                            break;
                        }
                    }
                    if (!match) {
                        return null;
                    }
                }
                return response.body();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * 图片转成base64
         */
        Function<String, String> REPLACE_IMG_SRC_TO_BASE64 = (src) -> {
            final String base64Prefix = "data:img/%s;base64,";
            try {
                byte[] data = open(src, "image", "img");
                if (data == null) {
                    return src;
                }
                String fileType;
                if (src.endsWith("gif")) {
                    fileType = "gif";
                } else if (src.endsWith("png")) {
                    fileType = "png";
                } else {
                    fileType = "jpg";
                }
                return String.format(base64Prefix, fileType) + Base64.getEncoder().encodeToString(data);
            } catch (Exception e) {
                e.getStackTrace();
            }
            return "";
        };
    }
}
