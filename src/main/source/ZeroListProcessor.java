import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;
import us.codecraft.webmagic.pipeline.FilePageModelPipeline;

/**
 * Created by ZacharyChang.
 */
@TargetUrl("http://dmxz.zerodm.com/xiazai/\\w+/")
@HelpUrl("http://dmxz.zerodm.com/list/1/\\d+")
public class ZeroListProcessor {
    @ExtractBy("//div[@class='title']/h1/text()")
    private String name;

    //    private String status;
//    private String count;
//    private String date;
    public static void main(String[] args) {
        OOSpider.create(Site.me().setSleepTime(1000)
                , new FilePageModelPipeline("C:\\Dev\\zerodm"), ZeroListProcessor.class)
                .addUrl("http://dmxz.zerodm.com/list/1").thread(4).run();
    }
}
