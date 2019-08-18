package com.jsoup.api.test.controller;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * 测试接口Controller
 *
 * Created by Happy王子乐 on 2019/8/17.
 */
@Slf4j
public class TestController {

    /**
     * 手动获取常用标签信息
     *
     * @param args
     */
    public static void main(String args[]){

        // 需要爬取的网页地址
        final String url = "https://so.gushiwen.org/shiwenv_632c5beb84eb.aspx";

        try {
            // 首先我们先把所有的标签找出来，放到doc文件中
            Document doc = Jsoup.connect(url).get();

            // H1标签，一级标题
            Elements h1El = doc.select("H1");
            String h1Text = h1El.text();
            log.info("H1 一级标题：{}", h1Text);
            // H2标签，二级标题
            Elements h2El = doc.select("H2");
            String h2Text = h2El.text();
            log.info("H2 二级标题：{}", h2Text);
            // H3标签，三级标题
            Elements h3El = doc.select("H3");
            String h3Text = h3El.text();
            log.info("H3 三级标题：{}", h3Text);
            // H4标签，四级标题
            Elements h4El = doc.select("H4");
            String h4Text = h4El.text();
            log.info("H4 四级标题：{}", h4Text);
            // H5标签，五级标题
            Elements h5El = doc.select("H5");
            String h5Text = h5El.text();
            log.info("H5 五级标题：{}", h5Text);

            // p标签
            Elements pEls = doc.select("p");
            log.info("p标签------------：{}", getText(pEls));

            // b标签
            Elements bEls = doc.select("b");
            log.info("b标签------------：{}", getText(bEls));

            // a标签
            Elements aEls = doc.select("a");
            log.info("a标签------------：{}", getText(aEls));

            //获取div标签
            Elements divEls = doc.select("div");
            log.info("div标签------------：{}", getText(divEls));

            //获取span标签
            Elements spanEls = doc.select("span");
            log.info("span标签------------：{}", getText(spanEls));
        } catch (IOException e) {
            log.error("get text is error: ", e);
        }
    }

    private static String getText(Elements els) {
        StringBuilder sb = new StringBuilder();
        for(Element el : els) {
            sb.append("\n");
            if (el.text().equals("")) {
                break;
            }
            sb.append(el.text());
            sb.append("\n");
        }
        return sb.toString();
    }
}
