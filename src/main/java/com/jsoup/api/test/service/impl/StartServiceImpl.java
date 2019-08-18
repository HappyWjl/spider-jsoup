package com.jsoup.api.test.service.impl;

import com.jsoup.api.test.model.AncientPoetry;
import com.jsoup.api.test.service.StartService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * StartServiceImpl
 *
 * Created by Happy王子乐 on 2019/8/17.
 */
@Slf4j
@Service("startService")
public class StartServiceImpl implements StartService {

    @Override
    public AncientPoetry getOne(String url) {
        AncientPoetry ancientPoetry = new AncientPoetry();
        if (StringUtils.isEmpty(url)) {
            return ancientPoetry;
        }
        try {
            Document doc = Jsoup.connect(url).get();
            getAncientPoetry(ancientPoetry, doc);
        } catch (Exception e) {
            log.error("getOne is error: ", e);
        }
        log.info("getOne result:{}", ancientPoetry);
        return ancientPoetry;
    }

    /**
     * 封装单个古诗词对象
     *
     * @param ancientPoetry 古诗词对象
     * @param doc           文档数据
     */
    private void getAncientPoetry(AncientPoetry ancientPoetry, Document doc) {
        // 获取标题
        Elements pEl = doc.select("H1");
        String p0Text = pEl.text();
        ancientPoetry.setTitle(p0Text);
        // 获取朝代、作者
        Elements pSourceEl = doc.select("p.source a");
        ancientPoetry.setDynasty(pSourceEl.first().text());
        ancientPoetry.setAuthor(pSourceEl.eq(1).text());
        // 获取内容
        Elements divContsonEl = doc.select("div.contson");
        String divContsonText = divContsonEl.text();
        ancientPoetry.setContent(divContsonText);
    }

    @Override
    public List<AncientPoetry> getList(String url, int pageMaxNo) {
        List<AncientPoetry> ancientPoetryList = new ArrayList<>();
        if (StringUtils.isEmpty(url)) {
            return ancientPoetryList;
        }
        try {
            for (int i=1; i<=pageMaxNo; i++) {
                String requestUrl = String.format(url, i);
                Document doc = Jsoup.connect(requestUrl).get();
                getAncientPoetry(ancientPoetryList, doc);
            }
        } catch (Exception e) {
            log.error("getList is error: ", e);
        }
        log.info("getList result:{}", ancientPoetryList);
        return ancientPoetryList;
    }

    /**
     * 封装多个古诗词对象
     *
     * @param ancientPoetryList 古诗词对象集合
     * @param doc               文档数据
     */
    private void getAncientPoetry(List<AncientPoetry> ancientPoetryList, Document doc) {
        AncientPoetry ancientPoetry;
        Elements elements = doc.body().select(".left .sons");
        for (Element element : elements) {
            ancientPoetry = new AncientPoetry();
            // 获取标题
            Elements pEl = element.select("b");
            String p0Text = pEl.first().text();
            ancientPoetry.setTitle(p0Text);
            // 获取朝代、作者
            Elements pSourceEl = element.select("p.source a");
            ancientPoetry.setDynasty(pSourceEl.first().text());
            ancientPoetry.setAuthor(pSourceEl.eq(1).text());
            // 获取内容
            Elements divContsonEl = element.select("div.contson");
            String divContsonText = divContsonEl.text();
            ancientPoetry.setContent(divContsonText);
            ancientPoetryList.add(ancientPoetry);
        }
    }
}
