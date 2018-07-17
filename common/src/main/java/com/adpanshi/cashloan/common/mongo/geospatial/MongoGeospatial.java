package com.adpanshi.cashloan.common.mongo.geospatial;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import java.util.ArrayList;
import java.util.List;
import org.bson.conversions.Bson;


/**
 * Created by zsw on 2018/6/23 0023.
 */
public class MongoGeospatial {
    private Point point;
    private Bson query;
    private List<List<Double>> list = new ArrayList();

    public MongoGeospatial()
    {
    }

    public MongoGeospatial(Double x, Double y) {
        set(x, y);
    }

    public MongoGeospatial set(Double x, Double y) {
        this.point = new Point(new Position(x.doubleValue(), y.doubleValue(), new double[0]));
        return this;
    }

    public MongoGeospatial add(Double x, Double y) {
        List temp = new ArrayList();
        temp.add(x);
        temp.add(y);
        this.list.add(temp);
        return this;
    }

    public Point getPoint() {
        return this.point;
    }

    public Bson getQuery() {
        return this.query;
    }

    public List<List<Double>> getList() {
        return this.list;
    }

    public MongoGeospatial near(String filedName, Double maxDistance, Double minDistance) {
        this.query = Filters.near(filedName, this.point, maxDistance, minDistance);
        return this;
    }

    public MongoGeospatial nearSphere(String filedName, Double maxDistance, Double minDistance) {
        this.query = Filters.nearSphere(filedName, this.point, maxDistance, minDistance);
        return this;
    }

    public MongoGeospatial circle(String filedName, Double radius) {
        this.query = Filters.geoWithinCenter(filedName, ((Double)this.point.getPosition().getValues().get(0)).doubleValue(), ((Double)this.point.getPosition().getValues().get(1)).doubleValue(), radius.doubleValue());
        return this;
    }

    public MongoGeospatial circleSphere(String filedName, Double radius) {
        this.query = Filters.geoWithinCenterSphere(filedName, ((Double)this.point.getPosition().getValues().get(0)).doubleValue(), ((Double)this.point.getPosition().getValues().get(1)).doubleValue(), radius.doubleValue());
        return this;
    }

    public MongoGeospatial withinPolygon(String filedName) {
        this.query = Filters.geoWithinPolygon(filedName, this.list);
        return this;
    }
}
