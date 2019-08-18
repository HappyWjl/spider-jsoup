package com.jsoup.api.spider;

import com.jsoup.api.spider.common.Constant;
import com.jsoup.api.spider.crawler.GswAuthorCrawler;
import com.jsoup.api.spider.crawler.GswContentCrawler;
import com.jsoup.api.spider.model.PageSnapshot;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Random;

/**
 * 爬虫
 *
 * @author kunge
 */
@Slf4j
@Service
public class SpiderService {

    public static void main(String... args) {
        new Thread(
                () -> spider(new GswAuthorCrawler())
        ).start();
        new Thread(
                () -> spider(new GswContentCrawler())
        ).start();
    }

    /**
     * 开始抓取方法
     *
     * @param crawler 抓取对象
     */
    private static void spider(Crawler crawler) {
        log.info("{} start.", crawler.name());
        int count = 0;
        Crawler.Spider spider = crawler.newSpider();
        Crawler.Analyzer analyzer = crawler.analyzer();

        final Random random = new Random();
        do {
            String link = spider.nextLink();
            if (StringUtils.isBlank(link)) {
                break;
            }
            try{
                for (PageSnapshot pageSnapshot : analyzer.parsePageSnapshot(link , Crawler.Analyzer.REPLACE_IMG_SRC_TO_BASE64)) {
                    output(pageSnapshot);
                    log.info("{} {} {} {}.", pageSnapshot.getSource(), pageSnapshot.getTitle(), ++count, link);
                }
            } catch (Exception e){
                log.error("output fail." , e);
            }
            // 限制数量
            if (count > 100000) {
                break;
            }
            // 限制请求频率，避免被检测到
            try{
                Thread.sleep(100 + random.nextInt(400));
            } catch (Exception e){
                log.error("Thread.sleep fail." , e);
            }
        } while (true);
        log.info("{} over.", crawler.name());
    }

    /**
     * 将数据对象输出为html文件
     *
     * @param pageSnapshot 数据对象
     */
    private static void output(PageSnapshot pageSnapshot) {
        // 文件名
        String fileName = pageSnapshot.getTitle().replace('/', ' ') + ".html";
        // 输出路径
        String path = Constant.PATH_PREFIX + "/" + pageSnapshot.getSource().getName() + "/" + fileName;
        // 创建文件对象
        File file = new File(path);
        // 拼接文件内容
        String text = String.format(Constant.OUTPUT_TEMPLATE,
                pageSnapshot.getTitle() ,
                pageSnapshot.getAuthor() ,
                pageSnapshot.getSource().getName() + "&nbsp;" + pageSnapshot.getSource().getSite(),
                pageSnapshot.getSourceLink(),
                pageSnapshot.getContent()
        );
        try {
            // 生成文件
            FileUtils.writeStringToFile(file, text, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
