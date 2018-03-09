package com.qchery.city.crawler;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

public class StatsGovPageProcessor implements PageProcessor {

    private static final String PREFIX_URL = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2016/";

    private static final String INDEX_URL = PREFIX_URL + "index.html";
    private static final String CITY_REGEX_URL = PREFIX_URL + "\\d+.html";
    private static final String DISTRACT_REGEX_URL = PREFIX_URL + "\\d+/\\d+.html";
    private Site site = Site.me().setRetryTimes(3).setDomain("www.stats.gov.cn");

    @Override
    public void process(Page page) {
        Selectable pageUrl = page.getUrl();

        if (INDEX_URL.equals(pageUrl.toString())) {
            processProvince(page);

        } else if (pageUrl.regex(CITY_REGEX_URL).match()) {
            processCity(page);

        } else if (pageUrl.regex(DISTRACT_REGEX_URL).match()) {
            processCounty(page);
        }
    }

    private void processProvince(Page page) {

        Selectable provinceTable = page.getHtml().xpath("//table[@class=provincetable]");
        List<Selectable> provinceTds = provinceTable.xpath("//tr[@class='provincetr']/td").nodes();
        for (Selectable provinceTd : provinceTds) {
            String url = provinceTd.xpath("//a/@href").toString();
            String name = provinceTd.xpath("//a").regex(">(.+)<br>", 1).toString();
            Area province = new Area(name, ExecutiveLevel.PROVINCE);
            page.addTargetRequest(new Request().setUrl(PREFIX_URL + url).putExtra("province", province));
            // 将省份添加到返回值中
            page.getResultItems().put(province.getName(), province);
        }
    }

    private void processCity(Page page) {
        Selectable cityTable = page.getHtml().xpath("//table[@class=citytable]");
        List<Selectable> nodes = cityTable.xpath("//tr[@class=citytr]").nodes();
        for (Selectable node : nodes) {
            Selectable codeTd = node.xpath("//td[1]");
            String url = codeTd.xpath("//a/@href").toString();
            String code = codeTd.xpath("//a/text()").toString();
            String name = node.xpath("//td[2]/a/text()").toString();
            Area city = new Area(name, ExecutiveLevel.CITY);
            city.setCode(code);
            city.setParentArea(Area.class.cast(page.getRequest().getExtra("province")));
            page.addTargetRequest(new Request().setUrl(PREFIX_URL + url).putExtra("city", city));
            // 将城市添加到返回值中
            page.getResultItems().put(city.getName(), city);
        }
    }

    private void processCounty(Page page) {
        Selectable countyTable = page.getHtml().xpath("//table[@class=countytable]");
        List<Selectable> nodes = countyTable.xpath("//tr[@class=countytr]").nodes();
        for (Selectable node : nodes) {
            String code = node.xpath("//td[1]/a/text()").toString();
            String name = node.xpath("//td[2]/a/text()").toString();
            if (name != null) {
                Area county = new Area(name, ExecutiveLevel.COUNTY);
                county.setCode(code);
                county.setParentArea(Area.class.cast(page.getRequest().getExtra("city")));
                // 将区添加到返回值中
                page.getResultItems().put(county.getName(), county);
            }
        }
    }

    @Override
    public Site getSite() {
        return site;

    }

    public static void main(String[] args) {
        Spider.create(new StatsGovPageProcessor())
                .addUrl(INDEX_URL)
                .thread(5)
                .addPipeline(new ConsolePipeline()).run();
    }
}