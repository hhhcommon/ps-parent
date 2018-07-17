package com.adpanshi.cashloan.common.utils;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.adpanshi.cashloan.common.page.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.util.CollectionUtils;
/**
 * Created by zsw on 2018/6/23 0023.
 */
public abstract class BeanUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(BeanUtil.class);

    public static <T> Page<T> convertPage(Page<?> sourcePage, Class<T> clazz) {
        if (sourcePage == null) {
            return null;
        }
        List targetList = convertList(sourcePage.getRows(), clazz);
        Page targetPage = new Page(sourcePage.getRp(), sourcePage.getPage(), targetList, sourcePage.getTotal());
        return targetPage;
    }

    public static <T> List<T> convertList(List<?> list, Class<T> targetClass)
    {
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList();
        }
        List listDest = new ArrayList();
        for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) { Object source = localIterator.next();
            Object target = convert(source, targetClass);
            listDest.add(target);
        }
        return listDest;
    }

    public static <T> T convert(Object source, Class<T> targetClass)
    {
        if (source == null)
            return null;
        try
        {
            Object result = targetClass.newInstance();
            copyProperties(source, result);
            return (T) result;
        } catch (Exception e) {
            LOGGER.warn(source.getClass().getSimpleName() + " convert to " + targetClass.getSimpleName() + " fail");
            throw new RuntimeException(e);
        }
    }

    public static void copyProperties(Object source, Object target)
            throws BeansException
    {
        BeanUtils.copyProperties(source, target);
        if ((target instanceof ConversionCustomizble))
            ((ConversionCustomizble)target).convertOthers(source);
    }

    public static Object getPropValue(Object obj, String prop)
    {
        if ((obj == null) || (prop == null)) {
            return null;
        }
        PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(obj.getClass(), prop);
        try {
            Method readMethod = propertyDescriptor.getReadMethod();
            return propertyDescriptor.getReadMethod().invoke(obj, new Object[0]); } catch (Exception e) {
        }
        return null;
    }

    public interface ConversionCustomizble
    {
        void convertOthers(Object paramObject);
    }
}