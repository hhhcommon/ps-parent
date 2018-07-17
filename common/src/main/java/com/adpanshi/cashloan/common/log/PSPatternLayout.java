package com.adpanshi.cashloan.common.log;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.adpanshi.cashloan.common.log.converter.HostAddressConverter;
import com.adpanshi.cashloan.common.log.converter.HostNameConverter;
import com.adpanshi.cashloan.common.log.converter.PIDConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zsw on 2018/6/22 0022.
 */
public class PSPatternLayout extends PatternLayout
{
    public static Map<String, String> DEFAULT_CONVERTER_MAP = new HashMap();

    public Map<String, String> getDefaultConverterMap()
    {
        return DEFAULT_CONVERTER_MAP;
    }

    public String doLayout(ILoggingEvent event) {
        if (!isStarted()) {
            return "";
        }
        return writeLoopOnConverters(event);
    }

    protected String getPresentationHeaderPrefix()
    {
        return "#logback.classic pattern: ";
    }

    static
    {
        DEFAULT_CONVERTER_MAP.putAll(PatternLayout.defaultConverterMap);
        DEFAULT_CONVERTER_MAP.put("hostName", HostNameConverter.class.getName());
        DEFAULT_CONVERTER_MAP.put("hostAddress", HostAddressConverter.class.getName());
        DEFAULT_CONVERTER_MAP.put("pid", PIDConverter.class.getName());
    }
}
