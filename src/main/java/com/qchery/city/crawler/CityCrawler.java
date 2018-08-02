package com.qchery.city.crawler;

import com.qchery.city.crawler.entity.Area;
import com.qchery.city.crawler.enums.ExecutiveLevel;
import com.qchery.city.crawler.pipeline.AreaMapperPipeline;
import org.apache.http.HttpHeaders;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CityCrawler {

    private static final String PREFIX_URL = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2017/";

    private static final String INDEX_URL = PREFIX_URL + "index.html";
    private static final String CITY_REGEX_URL = PREFIX_URL + "\\d+.html";
    private static final String DISTRACT_REGEX_URL = PREFIX_URL + "\\d+/\\d+.html";

    public static void main(String[] args) throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        Spider.create(new StatsGovPageProcessor())
                .addRequest(createRequest(INDEX_URL))
                .thread(8)
                .addPipeline(new AreaMapperPipeline(sqlSessionFactory)).run();
    }

    private static class StatsGovPageProcessor implements PageProcessor {

        private Site site = Site.me().setRetryTimes(3)
                .setCycleRetryTimes(3)
                .setSleepTime(50)
                .setTimeOut(10000).setDomain("www.stats.gov.cn");

        private AtomicInteger idGen = new AtomicInteger();

        @Override
        public void process(Page page) {
            Selectable pageUrl = page.getUrl();

            if (pageUrl.toString().contains("index.html")) {
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
                if (name != null) {
                    Area province = new Area(idGen.incrementAndGet(), name, ExecutiveLevel.PROVINCE);
                    page.addTargetRequest(createRequest(PREFIX_URL + url).putExtra("province", province));
                    // 将省份添加到返回值中
                    page.getResultItems().put(province.getName(), province);
                }
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
                Area city = new Area(idGen.incrementAndGet(), name, code, ExecutiveLevel.CITY);
                city.setParentArea(Area.class.cast(page.getRequest().getExtra("province")));
                page.addTargetRequest(createRequest(PREFIX_URL + url).putExtra("city", city));
                // 将城市添加到返回值中
                page.getResultItems().put(city.getName(), city);
            }
        }

        private void processCounty(Page page) {
            List<Selectable> nodes = page.getHtml().xpath("//table[@class=countytable]//tr[@class=countytr]").nodes();
            for (Selectable node : nodes) {
                String code = node.xpath("//td[1]/a/text()|//td[1]/text()").toString();
                String name = node.xpath("//td[2]/a/text()|//td[2]/text()").toString();
                if (name != null) {
                    Area county = new Area(idGen.incrementAndGet(), name, code, ExecutiveLevel.COUNTY);
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

    }

    private static Request createRequest(String url) {
        Request request = new Request(url);
        request.addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36");
        return request;
    }
}
