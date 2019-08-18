package com.jsoup.api.test.service;

import com.jsoup.api.test.model.AncientPoetry;

import java.util.List;

/**
 * StartService
 *
 * Created by Happy王子乐 on 2019/8/17.
 */
public interface StartService {

    /**
     * 根据url获取古诗词对象
     *
     * @param url 网页url
     * @return 古诗词对象
     */
    AncientPoetry getOne(String url);

    /**
     * 根据url获取古诗词列表
     *
     * @param url       网页url
     * @param pageMaxNo 抓取最大页数
     * @return 古诗词列表
     */
    List<AncientPoetry> getList(String url, int pageMaxNo);
}
