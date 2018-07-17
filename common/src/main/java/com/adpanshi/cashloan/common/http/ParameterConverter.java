package com.adpanshi.cashloan.common.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.message.BasicNameValuePair;

/**
 * Created by zsw on 2018/6/23 0023.
 */
public class ParameterConverter {
    public static String convert2URL(Map<String, String> parameters)
    {
        return convert2URL(parameters, null, true);
    }

    public static String convert2URL(Map<String, String> parameters, boolean isNeedURLEncode) {
        return convert2URL(parameters, null, isNeedURLEncode);
    }

    public static String convert2URL(Map<String, String> parameters, String decodeCharset, boolean isNeedURLEncode) {
        if ((parameters != null) && (parameters.size() > 0)) {
            StringBuilder query = new StringBuilder();
            for (Iterator itr = parameters.entrySet().iterator(); itr.hasNext(); ) {
                Map.Entry param = (Map.Entry)itr.next();
                try {
                    String paramKey = isNeedURLEncode ? URLEncoder.encode((String)param.getKey(), StringUtils.isBlank(decodeCharset) ? "UTF-8" : decodeCharset) : (String)param.getKey();
                    String paramValue = isNeedURLEncode ? URLEncoder.encode((String)param.getValue(), StringUtils.isBlank(decodeCharset) ? "UTF-8" : decodeCharset) : (String)param.getValue();
                    query.append(paramKey)
                            .append("=")
                            .append(paramValue);
                }
                catch (UnsupportedEncodingException e) {
                    new RuntimeException(e.getMessage(), e);
                }
                if (itr.hasNext()) {
                    query.append("&");
                }
            }
            return query.toString();
        }
        return null;
    }

    public static List<BasicNameValuePair> convert2ValuePair(Map<String, String> parameters) {
        if ((parameters != null) && (parameters.size() > 0)) {
            List valuePairs = new ArrayList();
            for (Map.Entry entry : parameters.entrySet()) {
                valuePairs.add(new BasicNameValuePair((String)entry.getKey(), (String)entry.getValue()));
            }
            return valuePairs;
        }
        return null;
    }
}
