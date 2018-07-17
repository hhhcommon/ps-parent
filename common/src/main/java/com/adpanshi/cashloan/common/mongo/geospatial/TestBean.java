package com.adpanshi.cashloan.common.mongo.geospatial;

import com.adpanshi.cashloan.common.mongo.beanUtil.MongoBean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by zsw on 2018/6/23 0023.
 */
public class TestBean  extends MongoBean
        implements Serializable
{
    private static final long serialVersionUID = 3050902737964210560L;
    private int mem;
    private Date date;
    private List<String> values;

    public TestBean(int i, Date date, List<String> values)
    {
        this.mem = i;
        this.date = date;
        this.values = values;
    }

    public TestBean()
    {
    }

    public List<String> getValues() {
        return this.values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public int getMem() {
        return this.mem;
    }

    public void setMem(int mem) {
        this.mem = mem;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}