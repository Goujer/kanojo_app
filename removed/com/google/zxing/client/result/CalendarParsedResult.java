package com.google.zxing.client.result;

import com.google.android.gcm.GCMRegistrar;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CalendarParsedResult extends ParsedResult {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
    private static final Pattern DATE_TIME = Pattern.compile("[0-9]{8}(T[0-9]{6}Z?)?");
    private static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.ENGLISH);
    private static final Pattern RFC2445_DURATION = Pattern.compile("P(?:(\\d+)W)?(?:(\\d+)D)?(?:T(?:(\\d+)H)?(?:(\\d+)M)?(?:(\\d+)S)?)?");
    private static final long[] RFC2445_DURATION_FIELD_UNITS = {GCMRegistrar.DEFAULT_ON_SERVER_LIFESPAN_MS, 86400000, 3600000, 60000, 1000};
    private final String[] attendees;
    private final String description;
    private final Date end;
    private final boolean endAllDay;
    private final double latitude;
    private final String location;
    private final double longitude;
    private final String organizer;
    private final Date start;
    private final boolean startAllDay;
    private final String summary;

    static {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public CalendarParsedResult(String summary2, String startString, String endString, String durationString, String location2, String organizer2, String[] attendees2, String description2, double latitude2, double longitude2) {
        super(ParsedResultType.CALENDAR);
        this.summary = summary2;
        try {
            this.start = parseDate(startString);
            if (endString == null) {
                long durationMS = parseDurationMS(durationString);
                this.end = durationMS < 0 ? null : new Date(this.start.getTime() + durationMS);
            } else {
                try {
                    this.end = parseDate(endString);
                } catch (ParseException pe) {
                    throw new IllegalArgumentException(pe.toString());
                }
            }
            this.startAllDay = startString.length() == 8;
            this.endAllDay = endString != null && endString.length() == 8;
            this.location = location2;
            this.organizer = organizer2;
            this.attendees = attendees2;
            this.description = description2;
            this.latitude = latitude2;
            this.longitude = longitude2;
        } catch (ParseException pe2) {
            throw new IllegalArgumentException(pe2.toString());
        }
    }

    public String getSummary() {
        return this.summary;
    }

    public Date getStart() {
        return this.start;
    }

    public boolean isStartAllDay() {
        return this.startAllDay;
    }

    public Date getEnd() {
        return this.end;
    }

    public boolean isEndAllDay() {
        return this.endAllDay;
    }

    public String getLocation() {
        return this.location;
    }

    public String getOrganizer() {
        return this.organizer;
    }

    public String[] getAttendees() {
        return this.attendees;
    }

    public String getDescription() {
        return this.description;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public String getDisplayResult() {
        StringBuilder result = new StringBuilder(100);
        maybeAppend(this.summary, result);
        maybeAppend(format(this.startAllDay, this.start), result);
        maybeAppend(format(this.endAllDay, this.end), result);
        maybeAppend(this.location, result);
        maybeAppend(this.organizer, result);
        maybeAppend(this.attendees, result);
        maybeAppend(this.description, result);
        return result.toString();
    }

    private static Date parseDate(String when) throws ParseException {
        if (!DATE_TIME.matcher(when).matches()) {
            throw new ParseException(when, 0);
        } else if (when.length() == 8) {
            return DATE_FORMAT.parse(when);
        } else {
            if (when.length() != 16 || when.charAt(15) != 'Z') {
                return DATE_TIME_FORMAT.parse(when);
            }
            Date date = DATE_TIME_FORMAT.parse(when.substring(0, 15));
            Calendar calendar = new GregorianCalendar();
            long milliseconds = date.getTime() + ((long) calendar.get(15));
            calendar.setTime(new Date(milliseconds));
            return new Date(milliseconds + ((long) calendar.get(16)));
        }
    }

    private static String format(boolean allDay, Date date) {
        if (date == null) {
            return null;
        }
        return (allDay ? DateFormat.getDateInstance(2) : DateFormat.getDateTimeInstance(2, 2)).format(date);
    }

    private static long parseDurationMS(CharSequence durationString) {
        long durationMS = -1;
        if (durationString != null) {
            Matcher m = RFC2445_DURATION.matcher(durationString);
            if (m.matches()) {
                durationMS = 0;
                for (int i = 0; i < RFC2445_DURATION_FIELD_UNITS.length; i++) {
                    String fieldValue = m.group(i + 1);
                    if (fieldValue != null) {
                        durationMS += RFC2445_DURATION_FIELD_UNITS[i] * ((long) Integer.parseInt(fieldValue));
                    }
                }
            }
        }
        return durationMS;
    }
}
