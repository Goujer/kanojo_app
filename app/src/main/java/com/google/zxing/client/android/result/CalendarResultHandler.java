package com.google.zxing.client.android.result;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.util.Log;
import com.google.zxing.client.result.CalendarParsedResult;
import com.google.zxing.client.result.ParsedResult;
import java.text.DateFormat;
import java.util.Date;
import jp.co.cybird.barcodekanojoForGAM.R;

public final class CalendarResultHandler extends ResultHandler {
    private static final String TAG = CalendarResultHandler.class.getSimpleName();
    private static final int[] buttons = {R.string.button_add_calendar};

    public CalendarResultHandler(Activity activity, ParsedResult result) {
        super(activity, result);
    }

    public int getButtonCount() {
        return buttons.length;
    }

    public int getButtonText(int index) {
        return buttons[index];
    }

    public void handleButtonPress(int index) {
        if (index == 0) {
            CalendarParsedResult calendarResult = (CalendarParsedResult) getResult();
            String description = calendarResult.getDescription();
            String organizer = calendarResult.getOrganizer();
            if (organizer != null) {
                if (description == null) {
                    description = organizer;
                } else {
                    description = String.valueOf(description) + 10 + organizer;
                }
            }
            addCalendarEvent(calendarResult.getSummary(), calendarResult.getStart(), calendarResult.isStartAllDay(), calendarResult.getEnd(), calendarResult.getLocation(), description, calendarResult.getAttendees());
        }
    }

    private void addCalendarEvent(String summary, Date start, boolean allDay, Date end, String location, String description, String[] attendees) {
        long endMilliseconds;
        Intent intent = new Intent("android.intent.action.INSERT");
        intent.setType("vnd.android.cursor.item/event");
        long startMilliseconds = start.getTime();
        intent.putExtra("beginTime", startMilliseconds);
        if (allDay) {
            intent.putExtra("allDay", true);
        }
        if (end != null) {
            endMilliseconds = end.getTime();
        } else if (allDay) {
            endMilliseconds = startMilliseconds + 86400000;
        } else {
            endMilliseconds = startMilliseconds;
        }
        intent.putExtra("endTime", endMilliseconds);
        intent.putExtra("title", summary);
        intent.putExtra("eventLocation", location);
        intent.putExtra("description", description);
        if (attendees != null) {
            intent.putExtra("android.intent.extra.EMAIL", attendees);
        }
        try {
            rawLaunchIntent(intent);
        } catch (ActivityNotFoundException e) {
            Log.w(TAG, "No calendar app available that responds to android.intent.action.INSERT");
            intent.setAction("android.intent.action.EDIT");
            launchIntent(intent);
        }
    }

    public CharSequence getDisplayContents() {
        CalendarParsedResult calResult = (CalendarParsedResult) getResult();
        StringBuilder result = new StringBuilder(100);
        ParsedResult.maybeAppend(calResult.getSummary(), result);
        Date start = calResult.getStart();
        ParsedResult.maybeAppend(format(calResult.isStartAllDay(), start), result);
        Date end = calResult.getEnd();
        if (end != null) {
            if (calResult.isEndAllDay() && !start.equals(end)) {
                end = new Date(end.getTime() - 86400000);
            }
            ParsedResult.maybeAppend(format(calResult.isEndAllDay(), end), result);
        }
        ParsedResult.maybeAppend(calResult.getLocation(), result);
        ParsedResult.maybeAppend(calResult.getOrganizer(), result);
        ParsedResult.maybeAppend(calResult.getAttendees(), result);
        ParsedResult.maybeAppend(calResult.getDescription(), result);
        return result.toString();
    }

    private static String format(boolean allDay, Date date) {
        DateFormat format;
        if (date == null) {
            return null;
        }
        if (allDay) {
            format = DateFormat.getDateInstance(2);
        } else {
            format = DateFormat.getDateTimeInstance(2, 2);
        }
        return format.format(date);
    }

    public int getDisplayTitle() {
        return R.string.result_calendar;
    }
}
