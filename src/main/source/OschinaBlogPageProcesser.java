import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.processor.example.OschinaBlogPageProcessor;

import java.util.List;

/**
 * Created by ZacharyChang.
 */
public class OschinaBlogPageProcesser implements PageProcessor {
    private Site site = Site.me().setDomain("http://dmxz.zerodm.com/list/wanjie/").setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36");

    public void process(Page page) {
        List<String> links = page.getHtml().links().regex("http://dmxz\\.zerodm\\.com/xiazai/\\S+").all();
        page.addTargetRequests(links);
        page.putField("name", page.getHtml().xpath("//div[@class='title']/h1/text()").toString());
//        page.putField("content", page.getHtml().$("div.content").toString());
//        page.putField("tags",page.getHtml().xpath("//div[@class='BlogTags']/a/text()").all());
    }

    public Site getSite() {
        return site;

    }

    public static void main(String[] args) {
        Spider.create(new OschinaBlogPageProcesser()).addUrl("http://dmxz.zerodm.com/list/wanjie/")
                .addPipeline(new ConsolePipeline()).run();

    }
}
