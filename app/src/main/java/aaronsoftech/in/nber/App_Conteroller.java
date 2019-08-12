package aaronsoftech.in.nber;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by Chouhan on 06/21/2019.
 */

public class App_Conteroller extends Application{

    public static SharedPreferences.Editor editor;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static SharedPreferences sharedpreferences;

    public static String mAddress="";
    public static String postCode="";
    public static String subLocality="";
    public static String city="";
    public static String state="";
    public static String country="";
    public static String full_address="";
    public static double latitute=0.0;
    public static double longitude=0.0;

}
