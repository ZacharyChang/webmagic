package util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.*;
import org.apache.log4j.Logger;

/**
 * Created by ZacharyChang.
 */
public class MongoDBUtil {
    private static Logger logger = Logger.getLogger("DBUtilLog");

    public static void mongo2elastic() {
        Mongo mongo = new Mongo("localhost", 27017);
        DB db = mongo.getDB("search");
        DBCollection collection = db.getCollection("job");
        DBCursor cursor = collection.find();
        while (cursor.hasNext()) {
            JSONObject obj = (JSONObject) JSON.toJSON(cursor.next());
            obj.remove("_id");
            CrawlerUtil.sendPost("http://localhost:9200/search/job", obj.toJSONString().replace("_version", "version"));
            logger.info(cursor.count());
        }
    }

    public static void main(String[] args) {
        mongo2elastic();
    }
}
