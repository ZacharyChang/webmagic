package job;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.TargetUrl;
import util.CrawlerUtil;


/**
 * Created by ZacharyChang.
 */

@TargetUrl("http://zhaopin.baidu.com/")
@ExtractBy(value = "//div[@class='tabs-ctn-item hot-cities']/dl/dd", multi = true)
public class CityCrawler implements AfterExtractor {
    @ExtractBy(value = "//dd/allText()")
    private String cityName;    // 城市名称

    public static void main(String[] args) {
        OOSpider.create(Site.me(), new ConsolePageModelPipeline(), CityCrawler.class).addUrl("http://zhaopin.baidu.com/").thread(3).run();
    }

    @Override
    public void afterProcess(Page page) {
        CrawlerUtil.appendContent("c:\\Dev\\city.txt", cityName);
    }
}
