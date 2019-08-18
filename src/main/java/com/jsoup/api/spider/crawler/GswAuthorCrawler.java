package com.jsoup.api.spider.crawler;

import com.jsoup.api.spider.Crawler;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;

/**
 * 自定义网站抓取爬虫（古诗文作者）
 *
 * Created by Happy王子乐 on 2019/8/17.
 */
@Service
public class GswAuthorCrawler implements Crawler {

    @Override
    public String name() {
        return "古诗文作者";
    }

    @Override
    public Spider newSpider() {
        return new BaiDuImgSpider();
    }

    @Override
    public Analyzer analyzer() {
        return new BaiDuImgAnalyzer();
    }

    @Slf4j
    static class BaiDuImgSpider implements Spider {

        static final String LIST_LINK_FORMAT = "https://so.gushiwen.org/authors/";

        private int page = 3;

        private boolean last = false;

        private LinkedList<String> linkList = new LinkedList<>();

        @Override
        public String nextLink() {
            if (last) {
                return null;
            }
            if (!linkList.isEmpty()) {
                return linkList.removeFirst();
            }
            try {
                String url = String.format(LIST_LINK_FORMAT, page++);
                Connection conn = Jsoup.connect(url);
                Elements elements = conn.get().body().select(".sonspic .cont p");
                if (last = CollectionUtils.isEmpty(elements)) {
                    return null;
                }
                for (Element element : elements) {
                    String jumpUrl = element.select("a").attr("abs:href");
                    if (jumpUrl.contains("authors")) {
                        continue;
                    }
                    linkList.add(jumpUrl);
                }
            } catch (Exception e) {
                e.getStackTrace();
                return null;
            }
            return linkList.removeFirst();
        }
    }

    static class BaiDuImgAnalyzer implements Analyzer {

        @Override
        public String title(Document document) {
            Element headline = document.select("h1").first();
            return headline != null ? headline.text() : null;
        }

        @Override
        public String content(Document document) {
            Element content = document.select(".sonspic .cont").first();
            if (content == null) {
                return null;
            }
            content.removeClass("*");
            return content.html();
        }
    }
}
