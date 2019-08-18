package com.jsoup.api.test.controller;

import com.jsoup.api.test.model.AncientPoetry;
import com.jsoup.api.test.service.StartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 手动抓取接口Controller
 *
 * Created by Happy王子乐 on 2019/8/17.
 */
@Controller
@RequestMapping("/api/jsoup")
public class ManualController {

    @Autowired
    StartService startService;

    /**
     * 根据url抓取数据
     * @param url 网站url
     * @return 抓取对象
     */
    @PostMapping("/getOne")
    @ResponseBody
    public AncientPoetry getOne(String url) {
        return startService.getOne(url);
    }

    /**
     * 根据传入url，以及分页抓取页数，抓取对象列表
     * @param url       传入url
     * @param pageMaxNo 分页抓取对象
     * @return 对象列表
     */
    @PostMapping("/getList")
    @ResponseBody
    public List<AncientPoetry> getList(String url, int pageMaxNo) {
        return startService.getList(url, pageMaxNo);
    }
}
