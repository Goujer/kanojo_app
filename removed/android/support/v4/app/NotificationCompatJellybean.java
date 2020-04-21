package android.support.v4.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.Iterator;

class NotificationCompatJellybean {
    private Notification.Builder b;

    public NotificationCompatJellybean(Context context, Notification n, CharSequence contentTitle, CharSequence contentText, CharSequence contentInfo, RemoteViews tickerView, int number, PendingIntent contentIntent, PendingIntent fullScreenIntent, Bitmap largeIcon, int mProgressMax, int mProgress, boolean mProgressIndeterminate, boolean useChronometer, int priority, CharSequence subText) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        Notification.Builder lights = new Notification.Builder(context).setWhen(n.when).setSmallIcon(n.icon, n.iconLevel).setContent(n.contentView).setTicker(n.tickerText, tickerView).setSound(n.sound, n.audioStreamType).setVibrate(n.vibrate).setLights(n.ledARGB, n.ledOnMS, n.ledOffMS);
        if ((n.flags & 2) != 0) {
            z = true;
        } else {
            z = false;
        }
        Notification.Builder ongoing = lights.setOngoing(z);
        if ((n.flags & 8) != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        Notification.Builder onlyAlertOnce = ongoing.setOnlyAlertOnce(z2);
        if ((n.flags & 16) != 0) {
            z3 = true;
        } else {
            z3 = false;
        }
        Notification.Builder deleteIntent = onlyAlertOnce.setAutoCancel(z3).setDefaults(n.defaults).setContentTitle(contentTitle).setContentText(contentText).setSubText(subText).setContentInfo(contentInfo).setContentIntent(contentIntent).setDeleteIntent(n.deleteIntent);
        if ((n.flags & 128) != 0) {
            z4 = true;
        } else {
            z4 = false;
        }
        this.b = deleteIntent.setFullScreenIntent(fullScreenIntent, z4).setLargeIcon(largeIcon).setNumber(number).setUsesChronometer(useChronometer).setPriority(priority).setProgress(mProgressMax, mProgress, mProgressIndeterminate);
    }

    public void addAction(int icon, CharSequence title, PendingIntent intent) {
        this.b.addAction(icon, title, intent);
    }

    public void addBigTextStyle(CharSequence bigContentTitle, boolean useSummary, CharSequence summaryText, CharSequence bigText) {
        Notification.BigTextStyle style = new Notification.BigTextStyle(this.b).setBigContentTitle(bigContentTitle).bigText(bigText);
        if (useSummary) {
            style.setSummaryText(summaryText);
        }
    }

    public void addBigPictureStyle(CharSequence bigContentTitle, boolean useSummary, CharSequence summaryText, Bitmap bigPicture) {
        Notification.BigPictureStyle style = new Notification.BigPictureStyle(this.b).setBigContentTitle(bigContentTitle).bigPicture(bigPicture);
        if (useSummary) {
            style.setSummaryText(summaryText);
        }
    }

    public void addInboxStyle(CharSequence bigContentTitle, boolean useSummary, CharSequence summaryText, ArrayList<CharSequence> texts) {
        Notification.InboxStyle style = new Notification.InboxStyle(this.b).setBigContentTitle(bigContentTitle);
        if (useSummary) {
            style.setSummaryText(summaryText);
        }
        Iterator i$ = texts.iterator();
        while (i$.hasNext()) {
            style.addLine(i$.next());
        }
    }

    public Notification build() {
        return this.b.build();
    }
}
