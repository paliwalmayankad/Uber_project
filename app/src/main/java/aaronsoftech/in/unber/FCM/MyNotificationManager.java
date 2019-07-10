package aaronsoftech.in.unber.FCM;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;



import java.io.InputStream;
import java.net.URL;

import aaronsoftech.in.unber.R;

/**
 * Created by Android on 08-12-2017.
 */

public class MyNotificationManager {
    private Context mCtx;
    private NotificationManager mNotificationManager;

    public MyNotificationManager(Context mCtx)
    {
        this.mCtx = mCtx;
    }

    public void showBigImageNotification(String title, String message, String url, Intent intent)
    {
        try
        {
            Log.v("NOTIFY", "showBigImageNotification called...");

            PendingIntent pendingIntent = PendingIntent.getActivity(mCtx, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

            Bitmap bigImageBitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());

            NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(mCtx)
                    .setAutoCancel(true)
                    .setContentTitle(mCtx.getResources().getString(R.string.app_name))
                    .setSubText(message)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentText(title)
                    .setSmallIcon(R.drawable.nber_logo)
                    .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.nber_logo));


            NotificationCompat.BigPictureStyle bigPicStyle = new NotificationCompat.BigPictureStyle();
            bigPicStyle.bigPicture(bigImageBitmap);

            mBuilder.setStyle(bigPicStyle);
            mBuilder.setContentIntent(pendingIntent);

            generateNotification(mBuilder);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void showSmallNotification(String title, String message, Intent intent)
    {
        try
        {
            Log.v("NOTIFY", "showNormalNotification called...");
            PendingIntent pendingIntent = PendingIntent.getActivity(mCtx, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(mCtx)
                    .setAutoCancel(true)
                    .setContentTitle(mCtx.getResources().getString(R.string.app_name))
                    .setSubText(message)
                    .setContentText(title)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setSmallIcon(R.drawable.nber_logo)
                    .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.nber_logo))
                    .setContentIntent(pendingIntent);

            generateNotification(mBuilder);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void generateNotification(NotificationCompat.Builder mBuilder)
    {
        try
        {
            Log.v("NOTIFY", "generateNotification called...");

            mNotificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);

            Notification notification = mBuilder.build();

            notification.flags    |= Notification.FLAG_AUTO_CANCEL;
            notification.defaults |= Notification.DEFAULT_LIGHTS;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notification.defaults |= Notification.DEFAULT_SOUND;

            mNotificationManager.notify(1, notification);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
