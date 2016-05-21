package util;

import org.apache.log4j.Logger;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by ZacharyChang.
 */
public class IndexUtil {
    private static Logger logger = Logger.getLogger("IndexUtilLog");

    public static void createIndex(String cityName) {
        try {
            Client client = TransportClient.builder().build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
            XContentBuilder builder = XContentFactory.jsonBuilder().startObject()
                    .startObject(cityName).startObject("_all")
                    .field("analyzer", "ik_max_word").field("search_analyzer", "ik_max_word").field("term_vector", "no").field("store", "false").endObject()
                    .startObject("properties")
                    .startObject("name").field("type", "string").field("store", "no").field("index", "analyzed").field("boost", 4).endObject()
                    .startObject("description").field("type", "string").field("store", "no").field("index", "analyzed").field("boost", 1.5).endObject()
                    .startObject("city").field("type", "string").field("store", "no").field("index", "not_analyzed").field("include_in_all", "false").endObject()
                    .startObject("ori_salary").field("type", "string").field("store", "no").field("index", "not_analyzed").field("include_in_all", "false").endObject()
                    .startObject("employertype").field("type", "string").field("store", "no").field("index", "not_analyzed").field("include_in_all", "false").endObject()
                    .startObject("district").field("type", "string").field("store", "no").field("index", "not_analyzed").field("include_in_all", "false").endObject()
                    .startObject("education").field("type", "string").field("store", "no").field("index", "not_analyzed").field("include_in_all", "false").endObject()
                    .startObject("experience").field("type", "string").field("store", "no").field("index", "not_analyzed").field("include_in_all", "false").endObject()
                    .endObject()
                    .endObject()
                    .endObject();
            PutMappingRequest mapping = Requests.putMappingRequest("newsearch").type(cityName).source(builder);
            client.admin().indices().putMapping(mapping).actionGet();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
