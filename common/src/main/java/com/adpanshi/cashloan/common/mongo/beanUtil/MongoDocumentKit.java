package com.adpanshi.cashloan.common.mongo.beanUtil;

import com.adpanshi.cashloan.common.mongo.geospatial.MongoGeospatial;
import org.bson.Document;
import com.mongodb.client.model.geojson.Point;
import java.util.*;

/**
 * Created by zsw on 2018/6/23 0023.
 */
public class MongoDocumentKit {
    public static Document toDocument(Object obj)
    {
        if (Document.class.isInstance(obj))
            return (Document)obj;
        Map map;
        if (!Map.class.isInstance(obj))
            map = MongoKit.INSTANCE.toMap(obj);
        else {
            map = (Map)obj;
        }
        transferDocument(map);

        String id = String.valueOf(map.get("id"));
        if ((id == null) || (id.equals("")) || ("null".equalsIgnoreCase(id))) {
            map.remove("id");
        }

        return new Document(map);
    }

    private static void transferDocument(Map<String, Object> map) {
        for (Map.Entry entry : map.entrySet())
        {
            if ((entry.getValue() instanceof MongoGeospatial)) {
                map.put((String)entry.getKey(), ((MongoGeospatial) entry.getValue()).getPoint());
            }

            if ((entry.getValue() instanceof MongoBean)) {
                Document doc = toDocument((MongoBean)entry.getValue());
                map.put((String)entry.getKey(), doc);
            }

            if (((entry.getValue() instanceof List)) || ((entry.getValue() instanceof Set)))
                try {
                    List list = new ArrayList((Collection)entry.getValue());
                    List docList = new ArrayList();
                    for (int i = 0; i < list.size(); i++) {
                        Document doc = toDocument(list.get(i));
                        docList.add(doc);
                    }
                    map.put((String)entry.getKey(), docList);
                } catch (RuntimeException e) {
                    MongoKit.INSTANCE.error("MongoDocumentKit.class", "The list must be List<MongoBean> inserted into the database.");
                }
        }
    }

    public static Document toDocument(MongoBean bean)
    {
        return new Document(bean.toMap());
    }

    public static boolean conversionValidation(Object obj)
    {
        if ((String.class.isInstance(obj)) || (Integer.class.isInstance(obj)) ||
                (Double.class
                        .isInstance(obj)) ||
                (Boolean.class.isInstance(obj)) ||
                (Float.class
                        .isInstance(obj)) ||
                (Character.class.isInstance(obj)) ||
                (Long.class
                        .isInstance(obj)) ||
                (Byte.class.isInstance(obj)) ||
                (Short.class
                        .isInstance(obj)) ||
                (Date.class.isInstance(obj)) ||
                (Map.class
                        .isInstance(obj)))
        {
            return false;
        }

        if ((obj instanceof Object)) {
            return true;
        }

        return false;
    }
}
