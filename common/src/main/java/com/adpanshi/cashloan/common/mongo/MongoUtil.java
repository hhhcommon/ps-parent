package com.adpanshi.cashloan.common.mongo;

import com.adpanshi.cashloan.common.mongo.beanUtil.MongoDocumentKit;
import com.adpanshi.cashloan.common.mongo.beanUtil.MongoKit;
import com.adpanshi.cashloan.common.mongo.geospatial.TestBean;
import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.client.model.InsertManyOptions;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zsw on 2018/6/23 0023.
 */
public class MongoUtil {
    private static Logger logger = LoggerFactory.getLogger(MongoUtil.class);

    private static MongoClient mongoClient = null;
    private String domains;
    private String user;
    private String password;
    private String database;
    private int connectionsPerHost;
    private int connectTimeout;
    private int maxWaitTime;
    private int socketTimeout;
    private int threadsAllowedToBlockForConnectionMultiplier;

    public String getDomains()
    {
        return this.domains;
    }

    public void setDomains(String domains) {
        this.domains = domains;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return this.database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public int getConnectionsPerHost() {
        return this.connectionsPerHost;
    }

    public void setConnectionsPerHost(int connectionsPerHost) {
        this.connectionsPerHost = connectionsPerHost;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getMaxWaitTime() {
        return this.maxWaitTime;
    }

    public void setMaxWaitTime(int maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

    public int getSocketTimeout() {
        return this.socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public int getThreadsAllowedToBlockForConnectionMultiplier() {
        return this.threadsAllowedToBlockForConnectionMultiplier;
    }

    public void setThreadsAllowedToBlockForConnectionMultiplier(int threadsAllowedToBlockForConnectionMultiplier) {
        this.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
    }

    private MongoClient getClient()
    {
        try {
            if (null != mongoClient) {
                return mongoClient;
            }

            List addressLists = new ArrayList();
            for (String domain : this.domains.split(";")) {
                String[] hostAndPort = domain.split(":");
                String host = hostAndPort[0];
                String port = hostAndPort[1];
                ServerAddress serverAddress = new ServerAddress(host, Integer.parseInt(port));
                addressLists.add(serverAddress);
            }

            MongoClientOptions.Builder options = new MongoClientOptions.Builder();

            options.connectionsPerHost(this.connectionsPerHost);
            options.connectTimeout(this.connectTimeout);
            options.maxWaitTime(this.maxWaitTime);
            options.socketTimeout(this.socketTimeout);
            options.threadsAllowedToBlockForConnectionMultiplier(this.threadsAllowedToBlockForConnectionMultiplier);
            options.writeConcern(WriteConcern.SAFE);

            MongoClientOptions op = options.build();

            Object credentialsLists = new ArrayList();
            MongoCredential credential = MongoCredential.createCredential(this.user, this.database, this.password.toCharArray());
            ((List)credentialsLists).add(credential);

            mongoClient = new MongoClient(addressLists, (List)credentialsLists, op);
        } catch (NumberFormatException e) {
            logger.error("MongoDB init error", e);
        }
        return mongoClient;
    }

    public void init() {
        MongoKit.INSTANCE.init(getClient(), this.database);
    }

    public MongoClient getMongoClient()
    {
        return mongoClient;
    }

    public void insert(String collName, Object bean)
    {
        MongoKit.INSTANCE.insert(collName, MongoDocumentKit.toDocument(bean));
    }

    public void insert(String collName, List<Object> list)
    {
        if ((CollectionUtils.isNotEmpty(list)) && (StringUtils.isNotEmpty(collName))) {
            List docList = new ArrayList();
            for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) { Object bean = localIterator.next();
                docList.add(MongoDocumentKit.toDocument(bean));
            }
            MongoKit.INSTANCE.insert(collName, docList, new InsertManyOptions());
        }
    }

    public <T> List<T> findAll(String collName, Class<T> clazz)
    {
        return MongoKit.INSTANCE.find(collName, 0, new BsonDocument(), new BsonDocument(), clazz);
    }

    public <T> List<T> find(String collName, Bson filter, Class<T> clazz)
    {
        Bson query = filter;
        if (filter == null) {
            query = new BsonDocument();
        }
        return MongoKit.INSTANCE.find(collName, query, new BsonDocument(), new BsonDocument(), clazz);
    }

    public List<JSONObject> find(String collName, Bson filter)
    {
        Bson query = filter;
        if (filter == null) {
            query = new BsonDocument();
        }
        return MongoKit.INSTANCE.find(collName, query, new BsonDocument());
    }

    public List<JSONObject> find(String collName, Bson filter, String sortColumn, boolean isDesc)
    {
        Bson query = filter;
        int desc = isDesc ? -1 : 1;
        if (filter == null) {
            query = new BsonDocument();
        }
        return MongoKit.INSTANCE.find(collName, query, new BasicDBObject(sortColumn, Integer.valueOf(desc)), new BsonDocument());
    }

    public <T> List<T> find(String collName, Bson filter, String sortColumn, boolean isDesc, Class<T> clazz)
    {
        Bson query = filter;
        int desc = isDesc ? -1 : 1;
        if (filter == null) {
            query = new BsonDocument();
        }
        return MongoKit.INSTANCE.find(collName, query, new BasicDBObject(sortColumn, Integer.valueOf(desc)), new BsonDocument(), clazz);
    }

    public <T> List<T> find(String collName, Bson filter, String sortColumn, boolean isDesc, Integer limit, Class<T> clazz)
    {
        Bson query = filter;
        int desc = isDesc ? -1 : 1;
        if (filter == null) {
            query = new BsonDocument();
        }
        int limitNum = limit == null ? 0 : limit.intValue();
        return MongoKit.INSTANCE.find(collName, query, new BasicDBObject(sortColumn, Integer.valueOf(desc)), new BsonDocument(), limitNum, clazz);
    }

    public <T> List<T> findByPage(String collName, Bson filter, int pageNo, int pageSize, Class<T> clazz)
    {
        Bson orderBy = new BasicDBObject("_id", Integer.valueOf(1));
        int skip = (pageNo - 1) * pageSize;
        int limit = pageSize;
        Bson query = filter;
        if (filter == null) {
            query = new BsonDocument();
        }
        return MongoKit.INSTANCE.find(collName, query, orderBy, limit, skip, new BsonDocument(), clazz);
    }

    public <T> List<T> findByPage(String collName, Bson filter, String sortColumn, boolean isDesc, int pageNo, int pageSize, Class<T> clazz)
    {
        int desc = isDesc ? -1 : 1;
        int skip = (pageNo - 1) * pageSize;
        int limit = pageSize;
        Bson query = filter;
        if (filter == null) {
            query = new BsonDocument();
        }
        return MongoKit.INSTANCE.find(collName, query, new BasicDBObject(sortColumn, Integer.valueOf(desc)), limit, skip, new BsonDocument(), clazz);
    }

    public <T> List<T> find(String collName, Bson filter, Bson project, String sortColumn, boolean isDesc, int pageNo, int pageSize, String join, Class<T> clazz)
    {
        int desc = isDesc ? -1 : 1;
        int skip = (pageNo - 1) * pageSize;
        int limit = pageSize;
        Bson query = filter;
        if (filter == null) {
            query = new BsonDocument();
        }
        return MongoKit.INSTANCE.find(collName, query, new BasicDBObject(sortColumn, Integer.valueOf(desc)), project, limit, skip, join, clazz);
    }

    public Long count(String collName, Bson filter)
    {
        return Long.valueOf(MongoKit.INSTANCE.count(collName, filter));
    }

    public long updateData(String collName, Bson bson, Document doc)
    {
        return MongoKit.INSTANCE.replaceOne(collName, bson, doc);
    }

    public long updateField(String collName, Bson bson, Document doc)
    {
        return MongoKit.INSTANCE.update(collName, bson, new Document("$set", doc));
    }

    public long updateFieldMutil(String collName, Bson bson, Document doc)
    {
        return MongoKit.INSTANCE.update(collName, bson, new Document("$set", doc), true);
    }

    public long deleteOne(String collName, Bson bson)
    {
        return MongoKit.INSTANCE.deleteOne(collName, bson);
    }

    public long deleteMany(String collName, Bson bson)
    {
        return MongoKit.INSTANCE.delete(collName, bson);
    }

    public static void main(String[] args) {
        MongoUtil db = new MongoUtil();
        db.setDomains("192.168.100.179:27017");
        db.setUser("root");
        db.setPassword("root");
        db.setDatabase("BtKjBuriedPoint");
        db.setConnectionsPerHost(100);
        db.setConnectTimeout(15000);
        db.setMaxWaitTime(10000);
        db.setSocketTimeout(0);
        db.setThreadsAllowedToBlockForConnectionMultiplier(5);
        try {
            db.init();

            List valueList = new ArrayList();
            valueList.add("asdf1");
            valueList.add("asdf2");
            valueList.add("asdf3");
            valueList.add("asdf4");
            valueList.add("asdf5");
            valueList.add("asdf6");
            valueList.add("asdf7");
            db.insert("testDB", new TestBean(23131, new Date(), valueList));

            List objects = db.find("testDB", null, TestBean.class);
            System.out.println(JSON.toJSONString(objects));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            mongoClient.close();
        }
    }
}
