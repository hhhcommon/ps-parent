package com.adpanshi.cashloan.common.utils;
import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.adpanshi.cashloan.common.exception.PSException;
import org.apache.commons.lang3.time.DateUtils;
/**
 * Created by zsw on 2018/6/23 0023.
 */
public class DateUtil
{
    public static final DateFormat ymdhmsSSSFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");

    public static final DateFormat ymdhmsFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final DateFormat ymdhmsNoPFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    public static final DateFormat ymdhmFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static final DateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static final DateFormat ymdNoPFormat = new SimpleDateFormat("yyyyMMdd");

    public static final DateFormat ymFormat = new SimpleDateFormat("yyyy-MM");

    public static final DateFormat ymNoPFormat = new SimpleDateFormat("yyyyMM");

    public static final DateFormat yFormat = new SimpleDateFormat("yyyy");

    public static final DateFormat mFormat = new SimpleDateFormat("MM");

    public static final DateFormat hFormat = new SimpleDateFormat("HH");

    public static final DateFormat mmFormat = new SimpleDateFormat("mm");

    public static final DateFormat hmsFormat = new SimpleDateFormat("HH:mm:ss");

    public static final DateFormat ymdhmsFormatWithCn = new SimpleDateFormat("yyyy年MM月dd日");

    public static final DateFormat hmsNoPFormat = new SimpleDateFormat("HHmmss");
    public static final long coefficient_D = 86400000L;
    public static final long coefficient_H = 3600000L;
    public static final long coefficient_m = 60000L;
    public static final long coefficient_s = 1000L;
    public static final int cycle_y = 6;
    public static final int cycle_m = 5;
    public static final int cycle_w = 7;
    private static final long MILLIS_IN_A_SECOND = 1000L;
    private static final long SECONDS_IN_A_MINUTE = 60L;
    private static final long MINUTES_IN_AN_HOUR = 60L;
    private static final long HOURS_IN_A_DAY = 24L;

    public static Date buildDate(int year, int month, int date, int hourOfDay, int minute, int second)
    {
        Calendar gg = new GregorianCalendar(year, month - 1, date, hourOfDay, minute, second);

        return gg.getTime();
    }

    public static Date string2Date(String stringDate, DateFormat format)
    {
        if ((stringDate == null) || (format == null))
            throw new PSException("日期或格式不能为空！");
        try
        {
            return format.parse(stringDate);
        } catch (ParseException e) {
            throw new PSException(e);
        }
    }

    public static String dateToString(Date date, DateFormat format)
    {
        if ((date == null) || (format == null)) {
            throw new PSException("日期或格式不能为空！");
        }
        return format.format(date);
    }

    public static String formatString(Date date) {
        if (date == null)
            return null;
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static long compare(Date startDate, Date endDate, long coefficient)
    {
        Long num1 = Long.valueOf(startDate.getTime());
        Long num2 = Long.valueOf(endDate.getTime());
        return (num2.longValue() - num1.longValue()) / coefficient;
    }

    public static long compare(String str1, DateFormat str1Format, String str2, DateFormat str2Format, long coefficient)
    {
        Date one = null;
        Date two = null;
        one = string2Date(str1, str1Format);
        two = string2Date(str2, str2Format);
        return compare(one, two, coefficient);
    }

    public static long compare(String str1, String str2, DateFormat format, long coefficient)
    {
        return compare(str1, format, str2, format, coefficient);
    }

    public static boolean isPeriod(Date start, Date end)
    {
        return (start.getTime() <= System.currentTimeMillis()) && (end.getTime() >= System.currentTimeMillis());
    }

    public static Date getDate(long time)
    {
        Date d = new Date();
        d.setTime(time);
        return d;
    }

    public static Date sumDate(Date date, long coefficient, int num)
    {
        return getDate(date.getTime() + coefficient * num);
    }

    public static Date getCycleData(Date date, int cycleType, int days)
    {
        if (days <= 0) {
            throw new PSException("天数必须大于0");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(cycleType, days);
        return calendar.getTime();
    }

    public static Date dateAfter(Date date, int cycleType, int amount)
    {
        if (amount < 0) {
            throw new PSException("时间周期数量必须大于0");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(cycleType, amount);
        return calendar.getTime();
    }

    public static Date dateBefore(Date date, int cycleType, int amount)
    {
        if (amount <= 0) {
            throw new PSException("时间周期数量必须大于0");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(cycleType, -amount);
        return calendar.getTime();
    }

    public static int getCycleDays(Date date, int cycleType)
    {
        Map m = new HashMap();
        m.put(Integer.valueOf(6), Integer.valueOf(6));
        m.put(Integer.valueOf(5), Integer.valueOf(5));
        m.put(Integer.valueOf(7), Integer.valueOf(7));
        if (m.get(Integer.valueOf(cycleType)) == null) {
            throw new PSException("周期设置错误！");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(((Integer)m.get(Integer.valueOf(cycleType))).intValue());
    }

    public static Date zeroConvertTime(Date fullDate)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fullDate);
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        return cal.getTime();
    }

    public static Date totalConvertTime(Date fullDate)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fullDate);
        cal.set(11, 23);
        cal.set(12, 59);
        cal.set(13, 59);
        cal.set(14, 999);
        return cal.getTime();
    }

    public static Date totalConvertTimeWithoutMillisecond(Date fullDate)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fullDate);
        cal.set(11, 23);
        cal.set(12, 59);
        cal.set(13, 59);
        cal.set(14, 0);
        return cal.getTime();
    }

    public static String dateToWeek(Date date)
    {
        String[] weekarr = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return weekarr[(cal.get(7) - 1)];
    }

    public static String[] getDateArray(Date beginDate, Date endDate)
    {
        if ((beginDate == null) || (endDate == null)) {
            return null;
        }
        if (beginDate.getTime() == endDate.getTime()) {
            return new String[] { dateToString(getDate(beginDate.getTime()), ymdFormat) };
        }

        if (beginDate.getTime() > endDate.getTime()) {
            Date temp = beginDate;
            beginDate = endDate;
            endDate = temp;
        }
        List list = new ArrayList();
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(beginDate);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(endDate);

        while (calBegin.getTimeInMillis() <= calEnd.getTimeInMillis()) {
            list.add(dateToString(calBegin.getTime(), ymdFormat));
            calBegin.add(6, 1);
        }

        return (String[])list.toArray(new String[list.size()]);
    }

    public static Date getSystemDate()
    {
        return new Date(System.currentTimeMillis() / 86400000L * 86400000L - 28800000L);
    }

    public static boolean isSameDay(Date date1, Date date2)
    {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        boolean isSameYear = cal1.get(1) == cal2
                .get(1);

        boolean isSameMonth = (isSameYear) &&
                (cal1
                        .get(2) ==
                        cal2.get(2));
        if (isSameMonth);
        boolean isSameDate = cal1
                .get(5) ==
                cal2
                        .get(5);

        return isSameDate;
    }

    public static long getTimeDifference(Date date1, Date date2)
    {
        long diff = date2.getTime() - date1.getTime();
        long mins = diff / 60000L;
        return mins;
    }

    public static boolean isDateAfter(Date date1, Date date2)
    {
        Date theDate1 = DateUtils.truncate(date1, 5);
        Date theDate2 = DateUtils.truncate(date2, 5);
        return theDate1.after(theDate2);
    }

    public static boolean isAfter(Date date1, Date date2)
    {
        Calendar calendar1 = new GregorianCalendar();
        calendar1.setTime(date1);
        Calendar calendar2 = new GregorianCalendar();
        calendar2.setTime(date2);
        return calendar1.after(calendar2);
    }

    public static int getYearDiff(Date date1, Date date2)
    {
        if ((date1 == null) || (date2 == null)) {
            throw new InvalidParameterException("date1 and date2 cannot be null!");
        }
        if (date1.after(date2)) {
            throw new InvalidParameterException("date1 cannot be after date2!");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        int year1 = calendar.get(1);
        int month1 = calendar.get(2);
        int day1 = calendar.get(5);
        calendar.setTime(date2);
        int year2 = calendar.get(1);
        int month2 = calendar.get(2);
        int day2 = calendar.get(5);
        int result = year2 - year1;
        if (month2 >= month1) {
            result++;
        }
        return result;
    }

    public static int getDayDiff(Date date1, Date date2)
    {
        if ((date1 == null) || (date2 == null)) {
            throw new InvalidParameterException("date1 and date2 cannot be null!");
        }

        Date startDate = DateUtils.truncate(date1, 5);
        Date endDate = DateUtils.truncate(date2, 5);
        if (startDate.after(endDate)) {
            throw new InvalidParameterException("date1 cannot be after date2!");
        }
        long millSecondsInOneDay = 86400000L;
        return (int)((endDate.getTime() - startDate.getTime()) / millSecondsInOneDay);
    }

    public static long daysBetween(Date startTime, Date endTime)
    {
        if ((startTime == null) || (endTime == null)) {
            throw new RuntimeException("日期或格式不能为空！");
        }
        Date startDate = zeroConvertTime(startTime);
        Date endDate = zeroConvertTime(endTime);
        return compare(startDate, endDate, 86400000L) + 1L;
    }

    public static String getTimeDiff(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int Cmonth = 1 + cal.get(2);
        int Cday = cal.get(5);
        Date start = date;
        Date end = new Date();
        long between = (end.getTime() - start.getTime()) / 1000L;
        long day = between / 86400L;
        long hour = between % 86400L / 3600L;
        long minute = between % 3600L / 60L;
        long second = between;

        String timediff = "";
        if (day > 0L) {
            if (day > 5L) {
                String datstr = "0" + Cday;
                String monthstr = "0" + Cmonth;
                timediff = monthstr + "-" + datstr;
            } else {
                timediff = day + "天前";
            }
        } else if (hour > 0L)
            timediff = hour + "小时前";
        else if (minute > 0L)
            timediff = minute + "分钟前";
        else if (second > 0L)
            timediff = second + "秒前";
        else {
            timediff = "1秒前";
        }
        return timediff;
    }

    public static Date getEarlyMorning()
    {
        Calendar c = Calendar.getInstance();
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        return c.getTime();
    }

    public static Date getAfterDays(Date date, int dayCnt)
    {
        return DateUtils.addDays(date, dayCnt);
    }

    public static String getDateAndTime(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int Cmonth = 1 + cal.get(2);
        int Cday = cal.get(5);
        int Chour = cal.get(11);
        int Cminute = cal.get(12);
        String timeStr = "";
        String datStr = "0" + Cday;
        String monthStr = "0" + Cmonth;
        String hourStr = "0" + Chour;
        String minuteStr = "0" + Cminute;
        timeStr = monthStr + "-" + datStr + " " + hourStr + ":" + minuteStr;
        return timeStr;
    }
}
