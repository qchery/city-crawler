package com.qchery.city.crawler;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

public class StatsGovPageProcessor implements PageProcessor {

    private Site site = Site.me().setDomain("www.stats.gov.cn");

    @Override
    public void process(Page page) {
//        List<String> links = page.getHtml().links().regex("http://www\\.stats\\.gov\\.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2016/\\d+.html").all();
//        page.addTargetRequests(links);
        Selectable provinceTable = page.getHtml().xpath("//table[@class=provincetable]");
        List<Selectable> provinceTrs = provinceTable.xpath("//tr[@class='provincetr']/td/a").nodes();
        List<Area> provinces = new ArrayList<>();
        for (Selectable provinceTr : provinceTrs) {
            String cityName = provinceTr.regex(">(.+)<br>", 1).toString();
            provinces.add(new Area(cityName, ExecutiveLevel.PROVINCE));
        }
        page.putField("provinces", provinces);
    }

    @Override
    public Site getSite() {
        return site;

    }

    public static void main(String[] args) {
        Spider.create(new StatsGovPageProcessor())
                .addUrl("http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2016/index.html")
                .addPipeline(new ConsolePipeline()).run();
    }
}