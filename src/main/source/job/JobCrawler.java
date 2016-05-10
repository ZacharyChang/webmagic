package job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import org.apache.log4j.Logger;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by ZacharyChang.
 */
public class JobCrawler {

    private final static int inc = 100000;
    // 并发线程数，建议不要过高
    private int threadNum = 4;
    // 线程休眠时间，单位毫秒
    private int threadSleepTime = 3000;
    private static Logger logger = Logger.getLogger("JobCrawlerLog");

    public void crawl(List urls) throws UnknownHostException {
        final Site site = Site.me().setSleepTime(this.threadSleepTime).setTimeOut(5000).setRetryTimes(3)
                .setCycleRetryTimes(1).setDomain("zhaopin.baidu.com");
//                .setUserAgent("Mozilla/5.0 (compatible; " +
//                        "Baiduspider/2.0; +http://www.baidu.com/search/spider.html)");

        Spider spider = Spider.create(new PageProcessor() {
            @Override
            public void process(Page page) {
                JSONObject data = (JSONObject) ((JSONObject) ((JSONObject) JSON.parse(page.getRawText())).get("data")).get("data");
                if (data.get("resNum") != null) {
                    // 驱动连接mongodb数据库
                    Mongo mongo = new Mongo("localhost", 27017);
                    DB db = mongo.getDB("search");
                    DBCollection collection = db.getCollection("job");
                    DBObject query;
                    for (Object obj : data.getJSONArray("disp_data")) {
                        query = (DBObject) com.mongodb.util.JSON.parse(obj.toString());
                        collection.save(query);
                    }
                    logger.error("*******************  post data size: " + data.get("resNum") + "  *******************");
                } else {
                    logger.error("no data post!");
                }
            }

            @Override
            public Site getSite() {
                return site;
            }
        });
        spider.startUrls(urls).thread(this.threadNum).run();
    }
/*
    public static void main(String[] args) throws UnknownHostException, UnsupportedEncodingException {
        Long startTime = System.currentTimeMillis();

        // 爬虫种子urls集合
        List<String> urls = new ArrayList<>();

        // 按照规则生成种子url
        String url;
        FileReader fileReader;
        try {
            fileReader = new FileReader("c:\\Dev\\district.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                for (int i = 0; i < CrawlerInfo.education.length; i++) {
                    for (int j = 0; j < CrawlerInfo.experience.length; j++) {
                        for (int k = 0; k < CrawlerInfo.employertype.length; k++) {
                            for (int id = beginId; id <= endid; id += 30) {
                                url = str + "&education=" + CrawlerInfo.education[i] + "&experience=" + CrawlerInfo.experience[j] + "&employertype=" + CrawlerInfo.employertype[k] + "&rn=30&pn=" + id;
                                urls.add(url);
                            }
                        }
                    }
                }
                logger.info("spider list add : ["+str+"]");
            }
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 爬取
        new JobCrawler().crawl(urls);

        Long endTime = System.currentTimeMillis();
        logger.info((endTime - startTime) / 1000.0+" seconds spent");
    }
 */
}
