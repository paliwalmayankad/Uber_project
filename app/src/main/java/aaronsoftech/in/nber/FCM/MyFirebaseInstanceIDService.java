package aaronsoftech.in.nber.FCM;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import aaronsoftech.in.nber.Utils.App_Utils;


/**
 * Created by Android on 08-12-2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService
{
    private static final String TAG = "MyFirebaseIIDService";
    public static    String refreshedToken;

    @Override
    public void onTokenRefresh()
    {
        //Getting registration token
        refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        //calling the method store token and passing token
        storeToken(refreshedToken);
    }

    private void storeToken(String token)
    {
        //we will save the token in sharedpreferences later
        App_Utils.saveAppVersionAndDeviceId(token);

    }
}