import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.selector.Selector;

import java.util.List;

/**
 * Created by ZacharyChang.
 */
public class ZeroPageProcesser implements PageProcessor {
    private Site site = Site.me().setDomain("dmxz.zerodm.com").setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36");

    public void process(Page page) {
        List<String> links = page.getHtml().links().regex("http://dmxz.zerodm.com/list/1/\\d+").all();
        page.addTargetRequests(links);
        List<String> contents = page.getHtml().xpath("//dl[@class=\"l-list mt10 oh\"]/dd/ul/li/a[@class=\"zerowzlh\"]/@title").all();
        for (String str : contents) {
            System.out.println(str);
            System.out.println("---------------------");
            page.putField("name", str);

//            page.putField("name", selector.xpath("a[@class=\"zerowzlh\"]/@title").toString());
        }
//        page.putField("name", page.getHtml().xpath("//dl[@class=\"l-list mt10 oh\"]/dd/ul/li/a[@class=\"zerowzlh\"]/@title").toString());
//        page.putField("content", page.getHtml().$("div.content").toString());
//        page.putField("tags",page.getHtml().xpath("//div[@class='BlogTags']/a/text()").all());
    }

    public Site getSite() {
        return site;

    }

    public static void main(String[] args) {
        Spider.create(new ZeroPageProcesser()).addUrl("http://dmxz.zerodm.com/list/1/")
                .addPipeline(new JsonFilePipeline("C:\\webmagic")).addPipeline(new ConsolePipeline()).thread(4).run();

    }
}
