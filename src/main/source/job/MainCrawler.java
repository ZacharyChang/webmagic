package job;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZacharyChang.
 */
public class MainCrawler {
    private static int beginId = 0;
    private static int endId = 700;
    private static int inc = 100;
    private static Logger logger = Logger.getLogger("MainCrawlerLog");

    public static void main(String[] args) throws UnknownHostException, UnsupportedEncodingException {
        Long startTime = System.currentTimeMillis();

        // 爬虫种子urls集合
        List<String> urls = new ArrayList<>();

        // 按照规则生成种子url
        String url;
        try {
            FileInputStream fileInputStream = new FileInputStream("c:\\Dev\\district.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                JobCrawler crawler = new JobCrawler();
                for (int i = 0; i < CrawlerInfo.education.length; i++) {
                    for (int j = 0; j < CrawlerInfo.experience.length; j++) {
                        for (int id = beginId; id <= endId; id += inc) {
                            url = str + "&education=" + CrawlerInfo.education[i] + "&experience=" + CrawlerInfo.experience[j] + "&employertype=" + "&rn=" + inc + "&pn=" + id;
                            urls.add(url);
                        }
                    }
                }
                crawler.crawl(urls);
                logger.error("spider crawled path : [" + str + "]");
                urls.clear();
            }
            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Long endTime = System.currentTimeMillis();
        logger.info((endTime - startTime) / 1000.0 + " seconds spent");
    }
}
