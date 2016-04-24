import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;
import us.codecraft.webmagic.model.annotation.ExtractBy;

/**
 * Created by ZacharyChang.
 */

@TargetUrl("http://www.lagou.com/gongsi/*.html")
@HelpUrl("http://www.lagou.com/gongsi/")
public class LagouProcessor {
    @ExtractBy("//div[@class='company_main']/h1/a/text()")
    private String name;    // 公司名称

    @ExtractBy("//div[@class='company_word']/text()")
    private String word;    // 公司口号

    @ExtractBy("//div[@id='container_right']/div[@class='item_container']/div[@class='item_content']/ul/li[1]/allText()")
    private String type;    // 类型

    public static void main(String[] args) {
        OOSpider.create(Site.me(), new ConsolePageModelPipeline(), LagouProcessor.class).addUrl("http://www.lagou.com/gongsi/").thread(4).run();
    }
}
