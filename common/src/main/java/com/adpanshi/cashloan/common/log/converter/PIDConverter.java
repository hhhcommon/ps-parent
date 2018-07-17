package com.adpanshi.cashloan.common.log.converter;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Minutes;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Date;

/**
 * Created by zsw on 2018/6/22 0022.
 */
public class PIDConverter extends ClassicConverter
{
    private static String PID;
    private static Date SETUP_TIME;

    public String convert(ILoggingEvent event)
    {
        return getPID();
    }

    private String getPID() {
        if ((PID == null) || (minutesBetween() >= 15)) {
            synchronized (PIDConverter.class) {
                if (PID == null) {
                    PID = detectPID();
                    SETUP_TIME = new Date();
                }
            }
        }
        return PID;
    }

    private int minutesBetween() {
        DateTime setupTime = new DateTime(SETUP_TIME);
        DateTime now = new DateTime();
        return Minutes.minutesBetween(setupTime, now).getMinutes();
    }

    private String detectPID() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        String name = runtimeMXBean.getName();
        if (StringUtils.isNoneEmpty(new CharSequence[] { name })) {
            int index = name.indexOf("@");
            if (index != -1) {
                return name.substring(0, index);
            }
            return name;
        }

        return "NoPID";
    }
}
