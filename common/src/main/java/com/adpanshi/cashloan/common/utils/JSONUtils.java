package com.adpanshi.cashloan.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by zsw on 2018/6/23 0023.
 */
public class JSONUtils
{
    private static final Log log = LogFactory.getLog(JSONUtils.class);

    public static String toJSON(Object object) {
        return JSONObject.toJSONString(object);
    }

    public static String toJSONWithArray(Object object) {
        return JSONArray.toJSONString(object);
    }

    public static Map<String, Object> toMap(String json)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        try {
            return (Map)objectMapper.readValue(json, Map.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Map<String, Object>> toList(String json)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        try {
            return (List)objectMapper.readValue(json, List.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toJSONByJackson(Object object)
    {
        StringWriter writer = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.getSerializationConfig().setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            writer = new StringWriter();
            try {
                mapper.writeValue(writer, object);
            } catch (JsonGenerationException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return writer.toString();
        } finally {
            if (writer != null)
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public static <T> T toBean(String content, Class<T> targetClass)
    {
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(content, targetClass);
        } catch (JsonParseException e) {
            log.info("JSON parse to " + targetClass.getSimpleName() + " occur JsonParseExcetion. content:" + content, e);
        } catch (JsonMappingException e) {
            log.info("JSON parse to " + targetClass.getSimpleName() + " occur JsonMappingException. content:" + content, e);
        } catch (IOException e) {
            log.info("JSON parse to " + targetClass.getSimpleName() + " occur IOException. content:" + content, e);
        }
        return null;
    }
}
