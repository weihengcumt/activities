package com.WebMagic.Demo;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;


public class Lianjia implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(300);
    static ArrayList<ResultItems> list = new ArrayList<>();
    @Override
    public void process(Page page) {
        List<String> all = page.getHtml().css(".position_link").links().all();
        page.addTargetRequests(all);
        //将内容存入field中,键值对
       // page.putField("author", page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString());
        page.putField("jobName", page.getHtml().xpath("//div[@class='job-name']//span[@class='name']/text()").toString());
//        page.putField("salary", page.getHtml().xpath("//span[@class='salary']/text()").toString());
//        page.putField("expression", page.getHtml().xpath("//dd[@class='job_request']/span[3]/text()").toString());
//        page.putField("job-advantage", page.getHtml().xpath("//dd[@class='job-advantage']/p[1]/text()").toString());
//        page.putField("job_bt", page.getHtml().xpath("//dd[@class='job_bt']/p/text()").toString());

        list.add(page.getResultItems());
        if (page.getResultItems().get("jobName")==null){
            //skip this page
           // page.setSkip(true);
            //分页
            for (int i = 2; i <= 30; i++) {
                page.addTargetRequest("https://www.lagou.com/zhaopin/Java/"+i+"/?filterOption=3");
            }
        }

    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new Lianjia()).addUrl("https://www.lagou.com/zhaopin/Java/1/?filterOption=3").thread(5).run();
        System.out.println(list.size());
    }
}