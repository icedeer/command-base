package com.icedeer.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <B>Description</B>: 
 * <P>
 * <B>Revision</B>:
 * <UL>
 * <LI> Peter W -- Aug 27, 2010 -- Initial Draft</LI>
 * <LI> </LI>
 * </UL>
 * 
 */
public class DateTool {
    public static final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_CMDLINE_TIME_FORMAT = "yyyy-MM-dd-HH-mm-ss";
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    public static Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().length() == 0) {
            return null;
        }

        // using different format to parse the string
        Date result = parseDate(dateStr, DEFAULT_TIME_FORMAT);
        if (result != null) {
            return result;
        }

        result = parseDate(dateStr, DEFAULT_CMDLINE_TIME_FORMAT);
        if (result != null) {
            return result;
        }

        result = parseDate(dateStr, DEFAULT_DATE_FORMAT);
        return result;
    }

    public static Date parseDate(String dateStr, String format) {
        if (dateStr == null || dateStr.trim().length() == 0) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(format);
        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getDateString(Date date, String format) {
        if (date != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            return simpleDateFormat.format(date);
        } else {
            return null;
        }
    }

    public static Date getYesterday() {
        return getDate(-1);
    }

    public static Date getDate(int val) {
        return getDate(new Date(), val);
    }

    public static Date getDate(Date refDate, int val) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(refDate);
        cal.add(Calendar.DAY_OF_YEAR, val);
        return cal.getTime();
    }

    public static Date getEndOfDay(Date refDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(refDate);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.add(Calendar.MILLISECOND, -1);
        return cal.getTime();
    }

    public static Date getBeginOfDay(Date refDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(refDate);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);

        return cal.getTime();
    }

    public static int getDayDifference(Date endDate, Date startDate) {
        long diffMillies = endDate.getTime() - startDate.getTime();
        TimeUnit unit = TimeUnit.DAYS;
        long result = unit.convert(diffMillies, TimeUnit.MILLISECONDS);

        return (int) result;

    }

}
