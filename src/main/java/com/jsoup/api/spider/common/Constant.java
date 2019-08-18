package com.jsoup.api.spider.common;

/**
 * 静态常量
 *
 * Created by Happy王子乐 on 2019/8/17.
 */
public class Constant {

    // 输出路径
    public static final String PATH_PREFIX = "/Users/userName/Desktop/jsoup";

    // html文件模版
    public static final String OUTPUT_TEMPLATE =
        "<html>\n" +
            "    <head>\n" +
            "        <style type=\"text/css\">\n" +
            "            h1 {\n" +
            "                text-align: center;\n" +
            "            }\n" +
            "            .layout {\n" +
            "                margin: auto;\n" +
            "                width: 1000px;\n" +
            "            }\n" +
            "        </style>\n" +
            "    </head>\n" +
            "    <body>\n" +
            "        <h1>%s</h1><br><br>\n" +
            "        <div class=\"layout\">\n" +
            "            <span>作者：%s&nbsp; 来源：%s</span><br>\n" +
            "            <span>原文地址：%s</span><br><br>\n" +
            "            <div class=\"content\" >\n" +
            "                %s\n" +
            "            </div>\n" +
            "        </div>\n" +
            "    </body>\n" +
            "</html>";

}
