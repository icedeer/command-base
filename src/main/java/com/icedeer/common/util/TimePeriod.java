package com.icedeer.common.util;

import java.util.Calendar;
import java.util.Date;

/**
 * <B>Description</B>: a utility class for calculating the start and end time base on a given period setup and a reference time point.
 * <br/> if the value is 0, the time period will treat it as "this"; for example, LAST_0_DAY will get "this day" value which is today;
 * LAST_0_MONTH will get "this month".
 * <P>
 * <B>Revision</B>:
 * <UL>
 * <LI> Peter W -- Jul 15, 2014 -- Initial Draft</LI>
 * <LI> </LI>
 * </UL>
 * 
 */
public class TimePeriod {
    public static final String PERIOD_PATTERN = "LAST_\\d+_(HOUR|DAY|MONTH|YEAR)";

    private long referenceTime;

    private String periodStr;

    private boolean valid;

    private long startTime;

    private long endTime;

    public TimePeriod(String periodStr, long referenceTime) {
        this.referenceTime = referenceTime;
        if (periodStr != null) {
            this.periodStr = periodStr.toUpperCase();
            valid = this.periodStr.matches(PERIOD_PATTERN);
        }
        if (valid) {
            calculate();
        }
    }

    private void calculate() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(referenceTime);
        String[] fields = periodStr.split("_", 3);
        if (fields == null || fields.length != 3) {
            valid = false;
            return;
        }
        try {
            int value = Integer.parseInt(fields[1]);
            if (value == 0) {
                endTime = cal.getTimeInMillis();
            }
            String unit = fields[2];
            if (unit.equalsIgnoreCase("HOUR")) {
                cal.set(Calendar.MILLISECOND, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.add(Calendar.HOUR, -value);
                startTime = cal.getTimeInMillis();
                if (value > 0) {
                    cal.add(Calendar.HOUR, value);
                    cal.add(Calendar.MILLISECOND, -1);
                    endTime = cal.getTimeInMillis();
                }

            } else if (unit.equalsIgnoreCase("DAY")) {
                cal.set(Calendar.MILLISECOND, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.add(Calendar.DAY_OF_YEAR, -value);
                startTime = cal.getTimeInMillis();
                if (value > 0) {
                    cal.add(Calendar.DAY_OF_YEAR, value);
                    cal.add(Calendar.MILLISECOND, -1);
                    endTime = cal.getTimeInMillis();
                }

            } else if (unit.equalsIgnoreCase("MONTH")) {
                cal.set(Calendar.MILLISECOND, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.add(Calendar.MONTH, -value);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                startTime = cal.getTimeInMillis();
                if (value > 0) {
                    cal.add(Calendar.MONTH, value);
                    cal.add(Calendar.MILLISECOND, -1);
                    endTime = cal.getTimeInMillis();
                }

            } else if (unit.equalsIgnoreCase("YEAR")) {
                cal.set(Calendar.MILLISECOND, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.add(Calendar.YEAR, -value);
                cal.set(Calendar.DAY_OF_YEAR, 1);
                startTime = cal.getTimeInMillis();
                if (value > 0) {
                    cal.add(Calendar.YEAR, value);
                    cal.add(Calendar.MILLISECOND, -1);
                    endTime = cal.getTimeInMillis();
                }

            }
        } catch (Throwable e) {
            valid = false;
        }
    }

    public boolean isValid() {
        return valid;
    }

    public Long calculateStartTime() {
        if (!isValid()) {
            return null;
        }
        return Long.valueOf(startTime);
    }

    public Date calculateStartDate() {
        if (!isValid()) {
            return null;
        }
        return new Date(startTime);
    }

    public Date calculateEndDate() {
        if (!isValid()) {
            return null;
        }
        return new Date(endTime);
    }

    public Long calculateEndTime() {
        if (!isValid()) {
            return null;
        }
        return Long.valueOf(endTime);
    }

    public boolean isInPeriod(long checkTime) {
        if (checkTime <= this.endTime && checkTime >= this.startTime) {
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<TimePeriod>");
        sb.append("<PeriodDef>" + periodStr + "</PeriodDef>");
        sb.append("<RefTime>" + new Date(referenceTime) + "</RefTime>");
        sb.append("<IsValid>" + isValid() + "</IsValid>");
        if (isValid()) {
            sb.append("<StartTime>" + new Date(startTime) + "</StartTime>");
            sb.append("<EndTime>" + new Date(endTime) + "</EndTime>");
        }
        sb.append("</TimePeriod>");
        return sb.toString();
    }

}
