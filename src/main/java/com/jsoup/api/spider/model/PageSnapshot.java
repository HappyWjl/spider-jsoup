package com.jsoup.api.spider.model;

import com.jsoup.api.spider.enums.Source;
import lombok.Data;

/**
 * 自定义网页快照对象
 *
 * Created by Happy王子乐 on 2019/8/17.
 */
@Data
public class PageSnapshot {

    // 标题
    private String title;
    // 来源网站名称
    private Source source;
    // 来源网站地址
    private String sourceLink;
    // 作者
    private String author;
    // 内容
    private String content;
}
