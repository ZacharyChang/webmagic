package job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import util.CrawlerUtil;

import java.io.*;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZacharyChang.
 */
public class DistrictCrawler {
    // 并发线程数，建议不要过高
    private int threadNum = 2;
    // 线程休眠时间，单位毫秒
    private int threadSleepTime = 5000;
    private static Log logger = LogFactory.getLog("DistrictCrawlerLog");

    public void crawl(List<String> urls) throws UnknownHostException {
        final Site site = Site.me().setSleepTime(this.threadSleepTime).setTimeOut(5000).setRetryTimes(3)
                .setCycleRetryTimes(1).setDomain("zhaopin.baidu.com");
//                .setUserAgent("Mozilla/5.0 (compatible; " +
//                        "Baiduspider/2.0; +http://www.baidu.com/search/spider.html)");

        Spider spider = Spider.create(new PageProcessor() {
            @Override
            public void process(Page page) {
                JSONObject data = (JSONObject) ((JSONObject) JSON.parse(page.getRawText())).get("data");
                JSONArray jobArray = data.getJSONArray("dist_info");
                String city = (String) ((JSONObject) data.get("uri_info")).get("city");
                String districtUrl;
                if (jobArray.size() > 0) {
                    for (Object obj : jobArray) {
                        districtUrl = "http://zhaopin.baidu.com/api/async?city=" + city + "&district=" + obj;
                        CrawlerUtil.appendContent("c:\\Dev\\district.txt", districtUrl);
                        logger.info("add district[" + obj.toString() + "] to city [" + city + "]");
                    }
                } else {
                    districtUrl = "http://zhaopin.baidu.com/api/async?city=" + city;
                    CrawlerUtil.appendContent("c:\\Dev\\district.txt", districtUrl);
                    logger.info("just add city [" + city + "]");
                }
            }

            @Override
            public Site getSite() {
                return site;
            }
        });
        spider.startUrls(urls).thread(this.threadNum).run();
    }

    public static void main(String[] args) {
        Long startTime = System.currentTimeMillis();
        String url;
        // 爬虫种子urls集合
        List<String> urls = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader("c:\\Dev\\city.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                url = "http://zhaopin.baidu.com/api/async?city=" + str + "&rn=1&pn=1";
                urls.add(url);
                logger.debug("url list add : " + url);
            }
            bufferedReader.close();
            fileReader.close();
            // 爬取
            new DistrictCrawler().crawl(urls);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Long endTime = System.currentTimeMillis();
        logger.info((endTime - startTime) / 1000.0);
    }
}
