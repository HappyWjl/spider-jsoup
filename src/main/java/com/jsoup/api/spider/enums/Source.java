package com.jsoup.api.spider.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 自定义网站枚举
 *
 * Created by Happy王子乐 on 2019/8/17.
 */
@Getter
@AllArgsConstructor
public enum Source {

    Gsc("古诗文", "https://so.gushiwen.org/", "古诗文");

    // 名称
    public String name;
    // 网站地址
    public String site;
    // 默认作者
    public String defaultAuthor;
}
