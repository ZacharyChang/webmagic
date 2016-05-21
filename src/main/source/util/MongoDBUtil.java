package util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.*;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by ZacharyChang.
 */
public class MongoDBUtil {
//    private static Logger logger = Logger.getLogger("DBUtilLog");

    public static void mongo2elastic() {
        Mongo mongo = new Mongo("127.0.0.1", 10000);
        DB db = mongo.getDB("search");
        DBCollection collection = db.getCollection("job");
        DBCursor cursor = collection.find();
        while (cursor.hasNext()) {
            JSONObject obj = (JSONObject) JSON.toJSON(cursor.next());
//            logger.info(obj.toJSONString());
            obj.remove("_id");
//            System.out.println(obj.get("city"));
            try {
                CrawlerUtil.sendPost("http://127.0.0.1:9200/newsearch/" + URLEncoder.encode((String) obj.get("city"), "utf-8"), obj.toJSONString().replace("_version", "version"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
//            logger.info(cursor.numSeen()+"/"+cursor.size());
//            System.out.println(cursor.numSeen()+"/"+cursor.size());
        }
    }

    public static void main(String[] args) {
        mongo2elastic();
    }
}
