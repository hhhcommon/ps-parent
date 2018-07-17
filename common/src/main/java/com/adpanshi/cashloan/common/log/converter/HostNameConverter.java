package com.adpanshi.cashloan.common.log.converter;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.joda.time.DateTime;
import org.joda.time.Minutes;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * Created by zsw on 2018/6/22 0022.
 */
public class HostNameConverter extends ClassicConverter
{
    private static String HOST_NAME;
    private static Date SETUP_TIME;

    public String convert(ILoggingEvent event)
    {
        return getHostName();
    }

    private String getHostName() {
        if ((HOST_NAME == null) || (minutesBetween() >= 15)) {
            synchronized (HostNameConverter.class) {
                if (HOST_NAME == null) {
                    HOST_NAME = detectHostName();
                    SETUP_TIME = new Date();
                }
            }
        }
        return HOST_NAME;
    }

    private int minutesBetween() {
        DateTime setupTime = new DateTime(SETUP_TIME);
        DateTime now = new DateTime();
        return Minutes.minutesBetween(setupTime, now).getMinutes();
    }

    private String detectHostName() {
        try {
            InetAddress host = InetAddress.getLocalHost();
            return host.getHostName(); } catch (UnknownHostException e) {
        }
        return "UnknownHost";
    }
}