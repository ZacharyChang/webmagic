import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by ZacharyChang.
 */
@TargetUrl("http://dmxz.zerodm.com/xiazai/\\w+/")
@HelpUrl("http://dmxz.zerodm.com/list/1/\\d+")
public class ZeroListProcessor implements AfterExtractor {
    @ExtractBy("//div[@class='title']/h1/text()")
    private String name;

    @ExtractBy("//div[@class='detail']/p[3]/allText()")
    private String type;

    @ExtractBy("//div[@class='detail']/p/font/text()")
    private String status;

    @ExtractBy("//div[@class='detail']/p[5]/text()")
    private String count;

    @ExtractBy("//div[@class='detail']/p[7]/text()")
    private String date;

//    @ExtractBy("//div[@class='c-video']/div[@class='info item']/div[@class='detail2']/p[last()]/text()")
//    private String characters;

    @ExtractBy("//div[@id='c-detail']/p[2]/allText()")
    private String summary;

//    @ExtractBy("//div[@class='c-rbox']/div[@class='c-share']/div[@class='ui-rate']/dl/dt/strong/em/text()")
//    private String score;

    private String regex(String str, String reg) {
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group();
        }
        return str;
    }

    public void afterProcess(Page page) {
        count = regex(count, "(\\d+)");
        date = regex(date, "[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}");
        type = type.replace("\u7c7b\u522b\uff1a", "");
        status = status.replace("\u81f3", "");
    }

    public static void main(String[] args) {
        OOSpider.create(Site.me().setSleepTime(1000)
                , new ConsolePageModelPipeline(), ZeroListProcessor.class)
                .addUrl("http://dmxz.zerodm.com/list/1").thread(4).run();
    }
}
