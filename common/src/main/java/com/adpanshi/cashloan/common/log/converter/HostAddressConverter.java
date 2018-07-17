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
public class HostAddressConverter extends ClassicConverter
{
    private static String HOST_ADDRESS;
    private static Date SETUP_TIME;

    public String convert(ILoggingEvent event)
    {
        return getHostAddress();
    }

    private String getHostAddress() {
        if ((HOST_ADDRESS == null) || (minutesBetween() >= 15)) {
            synchronized (HostAddressConverter.class) {
                if (HOST_ADDRESS == null) {
                    HOST_ADDRESS = detectHostAddress();
                    SETUP_TIME = new Date();
                }
            }
        }
        return HOST_ADDRESS;
    }

    private int minutesBetween() {
        DateTime setupTime = new DateTime(SETUP_TIME);
        DateTime now = new DateTime();
        return Minutes.minutesBetween(setupTime, now).getMinutes();
    }

    private String detectHostAddress() {
        try {
            InetAddress host = InetAddress.getLocalHost();
            return host.getHostAddress(); } catch (UnknownHostException e) {
        }
        return "UnknownHost";
    }
}