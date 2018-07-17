package com.adpanshi.cashloan.common.mongo.beanUtil;

import ch.qos.logback.classic.Level;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.Block;
import com.mongodb.DBRef;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexModel;
import com.mongodb.client.model.InsertManyOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.LoggerFactory;
import tool.util.StringUtil;


/**
 * Created by zsw on 2018/6/23 0023.
 */

public enum MongoKit
{
    INSTANCE;

    private static MongoClient client;
    private static MongoDatabase defaultDb;
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(MongoKit.class.getName());

    public MongoClient getClient() {
        return client;
    }

    public void init(MongoClient client, String database) {
        this.client = client;
        defaultDb = client.getDatabase(database);
    }

    public MongoCollection<Document> getCollection(String collectionName) {
        return defaultDb.getCollection(collectionName);
    }

    public void insert(String collectionName, List<Document> docs, InsertManyOptions ops)
    {
        getCollection(collectionName).insertMany(uniding(docs), ops);
    }

    public void insert(String collectionName, Document doc)
    {
        getCollection(collectionName).insertOne(uniding(doc));
    }

    public List<JSONObject> aggregate(String collectionName, List<Bson> query, boolean allowDiskUse)
    {
        final List list = new ArrayList();

        Block block = new Block()
        {
            public void apply(Object o) {
                Document document = (Document)o;
                document = MongoKit.this.iding(document);
                list.add(MongoKit.this.parseObject(document.toJson()));
            }
        };
        getCollection(collectionName).aggregate(query).allowDiskUse(Boolean.valueOf(allowDiskUse)).forEach(block);

        return list;
    }

    public <T> List<T> aggregate(String collectionName, List<Bson> query, boolean allowDiskUse, final Class<T> clazz)
    {
        final List list = new ArrayList();

        Block block = new Block()
        {
            public void apply(Object o) {
                Document document = (Document)o;
                document = MongoKit.this.iding(document);
                list.add(MongoKit.this.parseObject(document, clazz));
            }
        };
        getCollection(collectionName).aggregate(query).allowDiskUse(Boolean.valueOf(allowDiskUse)).forEach(block);

        return list;
    }

    public List<JSONObject> find(String collectionName, Bson projection)
    {
        return find(collectionName, new BsonDocument(), new BsonDocument(), projection, 0, 0, "");
    }

    public List<JSONObject> find(String collectionName, int limit, Bson sort, Bson projection)
    {
        return find(collectionName, new BsonDocument(), sort, projection, limit, 0, "");
    }

    public <T> List<T> find(String collectionName, int limit, Bson sort, Bson projection, Class<T> clazz)
    {
        return find(collectionName, new BsonDocument(), sort, projection, limit, 0, "", clazz);
    }

    public List<JSONObject> find(String collectionName, Bson query, Bson sort, Bson projection)
    {
        return find(collectionName, query, sort, projection, 0, 0, "");
    }

    public List<JSONObject> find(String collectionName, Bson query, Bson sort, int limit, int skip, Bson projection)
    {
        return find(collectionName, query, sort, projection, limit, skip, "");
    }

    public <T> List<T> find(String collectionName, Bson query, Bson sort, int limit, int skip, Bson projection, Class<T> clazz)
    {
        return find(collectionName, query, sort, projection, limit, skip, "", clazz);
    }

    public List<JSONObject> find(String collectionName, int limit, int skip, Bson sort, Bson projection, String join)
    {
        return find(collectionName, new BsonDocument(), sort, projection, limit, skip, join);
    }

    public <T> List<T> find(String collectionName, int limit, int skip, Bson sort, Bson projection, String join, Class<T> clazz)
    {
        return find(collectionName, new BsonDocument(), sort, projection, limit, skip, join, clazz);
    }

    public <T> List<T> find(String collectionName, Bson query, Bson sort, Bson projection, Class<T> clazz)
    {
        return find(collectionName, query, sort, projection, 0, 0, "", clazz);
    }

    public <T> List<T> find(String collectionName, Bson query, Bson sort, Bson projection, int limit, Class<T> clazz)
    {
        return find(collectionName, query, sort, projection, limit, 0, "", clazz);
    }

    public List<JSONObject> find(String collectionName, Bson query, Bson projection)
    {
        return find(collectionName, query, new BsonDocument(), projection, 0, 0, "");
    }

    public long count(String collectionName, Bson query)
    {
        return getCollection(collectionName).count(query);
    }

    public long count(String collectionName)
    {
        return getCollection(collectionName).count();
    }

    public JSONObject findOne(String collectionName, Bson query, Bson sort, String join)
    {
        return toJSON(
                iding(jointing((Document)getCollection(collectionName)
                        .find(query).sort(sort).first(), join)));
    }

    public <T> T findOne(String collectionName, Bson query, Bson sort, String join, Class<T> clazz)
    {
        return parseObject(
                iding(jointing((Document)getCollection(collectionName)
                        .find(query).sort(sort).first(), join)), clazz);
    }

    public List<JSONObject> find(String collectionName, Bson query, Bson sort, Bson projection, int limit, int skip, final String join)
    {
        final List list = new ArrayList();

        Block block = new Block()
        {
            public void apply(Object o) {
                Document document = (Document)o;
                document = MongoKit.this.iding(document);
                document = MongoKit.this.jointing(document, join);
                list.add(MongoKit.this.toJSON(document));
            }
        };
        getCollection(collectionName).find(query).projection(projection).sort(sort).limit(limit).skip(skip).forEach(block);

        return list;
    }

    public <T> List<T> find(String collectionName, Bson query, Bson sort, Bson projection, int limit, int skip, final String join, final Class<T> clazz)
    {
        final List list = new ArrayList();

        Block block = new Block()
        {
            public void apply(Object o) {
                Document document = (Document)o;
                document = MongoKit.this.iding(document);
                document = MongoKit.this.jointing(document, join);
                list.add(MongoKit.this.parseObject(document, clazz));
            }
        };
        getCollection(collectionName).find(query).projection(projection).sort(sort).limit(limit).skip(skip).forEach(block);
        return list;
    }

    public long update(String collectionName, Bson queue, Bson data)
    {
        UpdateResult updateResult = getCollection(collectionName).updateMany(queue, data, new UpdateOptions());
        return updateResult.getModifiedCount();
    }

    public long update(String collectionName, Bson queue, Bson data, boolean isMutil)
    {
        if (isMutil) {
            UpdateResult updateResult = getCollection(collectionName).updateMany(queue, data, new UpdateOptions());
            return updateResult.getModifiedCount();
        }
        UpdateResult updateResult = getCollection(collectionName).updateMany(queue, data, new UpdateOptions());
        return updateResult.getModifiedCount();
    }

    public long updateOne(String collectionName, Bson queue, Bson data)
    {
        UpdateResult updateResult = getCollection(collectionName).updateOne(queue, data);
        return updateResult.getModifiedCount();
    }

    public long replaceOne(String collectionName, Bson queue, Document document)
    {
        UpdateResult updateResult = getCollection(collectionName).replaceOne(queue, document);
        return updateResult.getModifiedCount();
    }

    public long delete(String collectionName, Bson queue)
    {
        DeleteResult deleteResult = getCollection(collectionName).deleteMany(queue);
        return deleteResult.getDeletedCount();
    }

    public long deleteOne(String collectionName, Bson queue)
    {
        MongoCollection collections = getCollection(collectionName);
        if (collections != null) {
            DeleteResult deleteResult = collections.deleteOne(queue);
            return deleteResult.getDeletedCount();
        }
        return 0L;
    }

    public String validation(Object obj)
    {
        StringBuffer buffer = new StringBuffer(64);

        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();

        Set constraintViolations = validator
                .validate(obj, new Class[0]);

        Iterator iter = constraintViolations.iterator();
        while (iter.hasNext()) {
            ConstraintViolation c = (ConstraintViolation)iter.next();
            buffer.append(c.getMessage());
        }

        return buffer.toString();
    }

    public String validation(Object obj, String[] keys)
    {
        StringBuffer buffer = new StringBuffer(64);

        Validator validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
        Set constraintViolations = new HashSet();

        for (String key : keys) {
            Iterator it = validator.validateProperty(obj, key, new Class[0]).iterator();
            if (it.hasNext()) {
                constraintViolations.add(it.next());
            }
        }

        Iterator iter = constraintViolations.iterator();
        while (iter.hasNext()) {
            ConstraintViolation c = (ConstraintViolation)iter.next();
            buffer.append(c.getMessage());
        }

        return buffer.toString();
    }

    public String setIndex(String collectionName, Bson bson)
    {
        return getCollection(collectionName).createIndex(bson);
    }

    public List<String> setIndex(String collectionName, List<IndexModel> list)
    {
        return getCollection(collectionName).createIndexes(list);
    }

    public List<JSONObject> getIndex(String collectionName)
    {
        final List list = new ArrayList();

        Block block = new Block()
        {
            public void apply(Object o) {
                Document document = (Document)o;
                list.add(MongoKit.this.parseObject(document.toJson()));
            }
        };
        getCollection(collectionName).listIndexes().forEach(block);

        return list;
    }

    public void deleteIndex(String collectionName, Bson bson)
    {
        getCollection(collectionName).dropIndex(bson);
    }

    public void deleteIndex(String collectionName)
    {
        getCollection(collectionName).dropIndexes();
    }

    private Document iding(Document document)
    {
        try
        {
            if ((document == null) || (document.get("_id") == null)) {
                return document;
            }
            document.put("_id", document.get("_id").toString());
        }
        catch (ClassCastException localClassCastException)
        {
        }

        return document;
    }

    private List<Document> uniding(List<Document> list)
    {
        List newList = new ArrayList();
        for (Document doc : list) {
            newList.add(uniding(doc));
        }
        return newList;
    }

    private Document uniding(Document document)
    {
        try
        {
            if ((document == null) || (document.get("_id") == null)) {
                return document;
            }
            document.remove("_id");
        }
        catch (ClassCastException localClassCastException)
        {
        }

        return document;
    }

    private Document jointing(Document document, String join)
    {
        if (!StringUtil.isBlank(join))
            try {
                DBRef dbRef = (DBRef)document.get(join, DBRef.class);

                Document joinDoc = (Document)getCollection(dbRef.getCollectionName())
                        .find(new Document("_id", dbRef
                                .getId())).first();

                joinDoc = iding(joinDoc);
                joinDoc.put("id", joinDoc.getString("_id"));
                joinDoc.remove("_id");
                document.put(join, joinDoc);
            }
            catch (ClassCastException localClassCastException) {
            }
        return document;
    }

    private JSONObject parseObject(String json)
    {
        try
        {
            if (!StringUtil.isBlank(json)) {
                return JSON.parseObject(json);
            }
            return new JSONObject();
        } catch (NullPointerException e) {
            error("parseObject", json);
        }return new JSONObject();
    }

    private <T> T parseObject(Document doc, Class<T> clazz)
    {
        try
        {
            if (doc == null) {
                return JSON.parseObject(new JSONObject().toJSONString(), clazz);
            }
            return JSON.parseObject(JSON.toJSONString(doc), clazz);
        } catch (NullPointerException e) {
            error("parseObject", clazz.getName());
        }return JSON.parseObject(new JSONObject().toJSONString(), clazz);
    }

    private JSONObject toJSON(Object obj)
    {
        try {
            return (JSONObject)JSON.toJSON(obj);
        } catch (NullPointerException e) {
            error("toJSON", obj.getClass().getName());
        }return new JSONObject();
    }

    protected void error(String funName, String text)
    {
        logger.error("MongKit tips: funName " + funName + " is error ! " + text);
    }

    public Map<String, Object> toMap(Object obj) {
        if (obj == null) {
            return null;
        }
        Map map = new HashMap();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                if (!key.equals("class"))
                {
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj, new Object[0]);

                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            INSTANCE.error("MongKit.class", "toMap is error " + e.getMessage());
        }
        return map;
    }

    public void setDebug(boolean debug)
    {
        Level level;
        if (debug)
            level = Level.DEBUG;
        else {
            level = Level.WARN;
        }
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger("ROOT");
        root.setLevel(level);
    }
}
