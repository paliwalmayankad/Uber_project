package aaronsoftech.in.unber.FCM;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;

import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import aaronsoftech.in.unber.Activity.Home;
import aaronsoftech.in.unber.Activity.Splash;
import aaronsoftech.in.unber.R;

/**
 * Created by Android on 08-12-2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    Intent moveIntent;
    Context con;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        String from=remoteMessage.getNotification().getBody();
        String title=remoteMessage.getNotification().getTitle();

        if (remoteMessage.getData().size() > 0)
        {


          //  Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try
            {
               // JSONObject json = new JSONObject(remoteMessage.getData().toString());
                sendPushNotification();
            }
            catch (Exception e)
            {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }else{
            sendNotification(from,title);

       }
    }

    private void sendNotification(String from, String title) {
        try {

            Intent intent = new Intent(getApplication(),Home.class);
            intent.putExtra("intentnotif","1");

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */,intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)

                    .setSmallIcon(R.drawable.nber_logo)
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setContentText(from)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.nber_logo))
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //this method will display the notification
    //We are passing the JSONObject that is received from
    //firebase cloud messaging
    private void sendPushNotification()
    {
        //optionally we can display the json into log
       // Log.e(TAG, "Notification JSON " + json.toString());
        try
        {
            //getting the json data
         //   JSONObject data = json.getJSONObject("data");

            //parsing json data
            String title = "You have new booking";
            String message = "Accept this booking";


            if(title!=null && !title.equalsIgnoreCase("null") && title.equals("Logout"))
            {
                //   AppController.getSpUserInfo().edit().clear().commit();
                //  AppController.getSpIsLogin().edit().putBoolean(SPUtills.IS_LOGIN, false).commit();

                //         AppController.countryList.clear();

                if(getCurrentPackage().equals(this.getPackageName().toString()))
                {
                    Intent intent = new Intent(getApplicationContext(), Splash.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    ((Activity) getApplicationContext()).startActivity(intent);
                    ((Activity) getApplicationContext()).finish();
                }
            }
            else
            {
                //creating MyNotificationManager object
                MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());
                if(getCurrentPackage().equals(this.getPackageName().toString()))
                {

                    if(getCurrentPackageCurrentActivity().equals("aaronsoftech.in.unber.Activity.Home"))
                    {
                        Log.v(TAG, "onMessage Current package is app package");
                        moveIntent = new Intent();
                    }
                    else
                    {
                        Log.v(TAG, "onMessage Current package NotificationsActivity called...");
                        moveIntent = new Intent(getApplicationContext(),Home.class);
                   //     moveIntent = new Intent(getApplication().getApplicationContext(),d_notification_new_fragment.class);
                    }

                }
                else
                {
                    Log.v(TAG, "onMessage Current package is not app package");
                    moveIntent = new Intent(getApplicationContext(),Splash.class);
                    moveIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                }

                    mNotificationManager.showSmallNotification(title, message, moveIntent);

            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private String getCurrentPackage()
    {
        String packageName="";
        try
        {
            ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            packageName = am.getRunningTasks(1).get(0).topActivity.getPackageName();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return packageName;
    }

    private String getCurrentPackageCurrentActivity()
    {
        String packageActivityName="";
        try
        {
            ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            packageActivityName = am.getRunningTasks(1).get(0).topActivity.getClassName();

            Log.d("MyFMS", "CURRENT Activity ::" + am.getRunningTasks(1).get(0).topActivity.getClassName()+"   Package Name :  "+am.getRunningTasks(1).get(0).topActivity.getPackageName());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return packageActivityName;
    }
}
