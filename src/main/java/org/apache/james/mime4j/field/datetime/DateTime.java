package org.apache.james.mime4j.field.datetime;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateTime {
    private final Date date;
    private final int day;
    private final int hour;
    private final int minute;
    private final int month;
    private final int second;
    private final int timeZone;
    private final int year;

    public DateTime(String yearString, int month2, int day2, int hour2, int minute2, int second2, int timeZone2) {
        this.year = convertToYear(yearString);
        this.date = convertToDate(this.year, month2, day2, hour2, minute2, second2, timeZone2);
        this.month = month2;
        this.day = day2;
        this.hour = hour2;
        this.minute = minute2;
        this.second = second2;
        this.timeZone = timeZone2;
    }

    private int convertToYear(String yearString) {
        int year2 = Integer.parseInt(yearString);
        switch (yearString.length()) {
            case 1:
            case 2:
                if (year2 < 0 || year2 >= 50) {
                    return year2 + 1900;
                }
                return year2 + 2000;
            case 3:
                return year2 + 1900;
            default:
                return year2;
        }
    }

    public static Date convertToDate(int year2, int month2, int day2, int hour2, int minute2, int second2, int timeZone2) {
        Calendar c = new GregorianCalendar(TimeZone.getTimeZone("GMT+0"));
        c.set(year2, month2 - 1, day2, hour2, minute2, second2);
        c.set(14, 0);
        if (timeZone2 != Integer.MIN_VALUE) {
            c.add(12, (((timeZone2 / 100) * 60) + (timeZone2 % 100)) * -1);
        }
        return c.getTime();
    }

    public Date getDate() {
        return this.date;
    }

    public int getYear() {
        return this.year;
    }

    public int getMonth() {
        return this.month;
    }

    public int getDay() {
        return this.day;
    }

    public int getHour() {
        return this.hour;
    }

    public int getMinute() {
        return this.minute;
    }

    public int getSecond() {
        return this.second;
    }

    public int getTimeZone() {
        return this.timeZone;
    }

    public void print() {
        System.out.println(toString());
    }

    public String toString() {
        return getYear() + " " + getMonth() + " " + getDay() + "; " + getHour() + " " + getMinute() + " " + getSecond() + " " + getTimeZone();
    }

    public int hashCode() {
        return (((((((((((((((this.date == null ? 0 : this.date.hashCode()) + 31) * 31) + this.day) * 31) + this.hour) * 31) + this.minute) * 31) + this.month) * 31) + this.second) * 31) + this.timeZone) * 31) + this.year;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DateTime other = (DateTime) obj;
        if (this.date == null) {
            if (other.date != null) {
                return false;
            }
        } else if (!this.date.equals(other.date)) {
            return false;
        }
        if (this.day != other.day) {
            return false;
        }
        if (this.hour != other.hour) {
            return false;
        }
        if (this.minute != other.minute) {
            return false;
        }
        if (this.month != other.month) {
            return false;
        }
        if (this.second != other.second) {
            return false;
        }
        if (this.timeZone != other.timeZone) {
            return false;
        }
        if (this.year != other.year) {
            return false;
        }
        return true;
    }
}
