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

import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import aaronsoftech.in.unber.Activity.Splash;

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
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try
            {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                sendPushNotification(json);
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
    }

    //this method will display the notification
    //We are passing the JSONObject that is received from
    //firebase cloud messaging
    private void sendPushNotification(JSONObject json)
    {
        //optionally we can display the json into log
        Log.e(TAG, "Notification JSON " + json.toString());
        try
        {
            //getting the json data
            JSONObject data = json.getJSONObject("data");

            //parsing json data
            String title = data.getString("title");
            String message = data.getString("message");
            String imageUrl = data.getString("image");

         /*   if(title!=null && !title.equalsIgnoreCase("null") && title.equals("Logout"))
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
                
                    Log.v(TAG, "onMessage Current package is not app package");
                    moveIntent = new Intent(getApplicationContext(),Splash.class);
                    moveIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    
                //if there is no image
                if(imageUrl.equals("null"))
                {
                    //displaying small notification
                    mNotificationManager.showSmallNotification(title, message, moveIntent);
                }
                else
                {
                    //if there is an image
                    //displaying a big notification
                    mNotificationManager.showBigImageNotification(title, message, imageUrl, moveIntent);
                }
            }*/
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
