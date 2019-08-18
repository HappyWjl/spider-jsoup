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
        import java.util.List;
        import java.util.stream.Collectors;

/**
 * 自定义网站抓取爬虫（古诗文网内容）
 *
 * Created by Happy王子乐 on 2019/8/17.
 */
@Service
public class GswContentCrawler implements Crawler {

    @Override
    public String name() {
        return "古诗文内容";
    }

    @Override
    public Spider newSpider() {
        return new GscSpider();
    }

    @Override
    public Analyzer analyzer() {
        return new GscAnalyzer();
    }

    @Slf4j
    static class GscSpider implements Spider {

        static final String LIST_LINK_FORMAT = "https://so.gushiwen.org/shiwen/default.aspx?page=%s&type=4&id=1";

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
                Elements elements = conn.get().body().select(".sons .cont p");
                List<Element> elementList = elements.stream().filter(element -> element.select(".source").size() == 0).collect(Collectors.toList());
                if (last = CollectionUtils.isEmpty(elementList)) {
                    return null;
                }
                for (Element element : elementList) {
                    linkList.add(element.select("a").attr("abs:href"));
                }
            } catch (Exception e) {
                e.getStackTrace();
                return null;
            }
            return linkList.removeFirst();
        }
    }

    static class GscAnalyzer implements Analyzer {

        @Override
        public String title(Document document) {
            Element headline = document.select("h1").first();
            return headline != null ? headline.text() : null;
        }

        @Override
        public String content(Document document) {
            Element content = document.select(".contson").first();
            if (content == null) {
                return null;
            }
            content.removeClass("*");
            return content.html();
        }
    }
}
