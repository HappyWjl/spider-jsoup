package com.jsoup.api.test.job;

import com.jsoup.api.test.service.StartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 定时任务job
 *
 * Created by Happy王子乐 on 2019/8/17.
 */
@Service
public class Job {

    @Value("${url}")
    private String url;

    @Autowired
    StartService startService;

//    @Scheduled(fixedRate = 5000) // 5秒抓取1次
    @Scheduled(cron = "0 0 2 * * ? ") // 每天凌晨2点抓取1次
    public void startByJob() {
        startService.getOne(url);
    }
}
