package com.jsoup.api.test.model;

import lombok.Data;

/**
 * 古诗词model对象
 *
 * Created by Happy王子乐 on 2019/8/17.
 */
@Data
public class AncientPoetry {

    /**
     * 标题
     */
    private String title;

    /**
     * 朝代
     */
    private String dynasty;

    /**
     * 作者
     */
    private String author;

    /**
     * 内容
     */
    private String content;
}
