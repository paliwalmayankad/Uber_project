package aaronsoftech.in.nber.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import aaronsoftech.in.nber.App_Conteroller;
import aaronsoftech.in.nber.R;
import de.hdodenhof.circleimageview.CircleImageView;

import static aaronsoftech.in.nber.Activity.Driver_doc_Image.IMAGE_DIRECTORY_NAME;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

/**
 * Created by Chouhan on 06/25/2019.
 */

public class App_Utils {

    private static App_Utils instance = new App_Utils();

    public static ProgressDialog progressDialog;
    public static Uri getOutputMediaFileUri(int type,String PageName){
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists())
        {
            if (!mediaStorageDir.mkdirs())
            {
                //    if(AppUtills.showLogs)Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "+ IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + time + ".jpg");
        }
        else
        {
            return null;
        }
        return  Uri.fromFile(mediaFile);
    }

    public static Dialog createDialog(Context context, boolean single)
    {
        final Dialog dialog = new Dialog(context, R.style.ThemeDialogCustom);

        dialog.getWindow().getAttributes().windowAnimations=R.style.dialog_animation;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        if(single)
            dialog.setContentView(R.layout.custom_dialog_one);
        else
            dialog.setContentView(R.layout.custom_dialog_two);

        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        dialog.show();

        return dialog;
    }

    public static void saveAppVersionAndDeviceId(String gcm_deviceId)
    {
        try
        {

            //if token is not null
            if (gcm_deviceId == null)
            {
                gcm_deviceId="";
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public static boolean checkAppVersion(){
        Boolean isM=false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            isM=true;
        }
        else
        {
            isM=false;
        }
        return isM;
    }


    public static void loadProfileImage_c(Context conn, String imageURL, ImageView imageView)
    {
        try
        {
            if(imageURL.equals("")  &&  imageURL.isEmpty())
            {
                Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +"://" + conn.getResources().getDrawable(R.drawable.ic_user));
                Glide.with(conn)
                        .load(imageUri)
                        .into(imageView);
            }
            else
            {
                Glide.with(conn)
                        .load(imageURL)
                        .into(imageView);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void loadProfileImage(Context conn, String imageURL, CircleImageView imageView)
    {
        try
        {
            if(imageURL.equals("")  &&  imageURL.isEmpty())
            {
                Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +"://" + conn.getResources().getDrawable(R.drawable.ic_user));
                Glide.with(conn)
                        .load(imageUri)
                        .into(imageView);
            }
            else
            {
                Glide.with(conn)
                        .load(imageURL)
                        .into(imageView);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void showAlertSnakeMessage(LinearLayout coordinatorLayout, String message)
    {
        try
        {
            Snackbar.make(coordinatorLayout, ""+message, Snackbar.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public static boolean isLocationEnabled(Context context)
    {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            try
            {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        }
        else
        {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }
    public static Location getCurrentLocation(Context connnnn)
    {
        try
        {
            LocationManager locationManager = (LocationManager)connnnn.getSystemService(Context.LOCATION_SERVICE);
            Location location;
            if (locationManager != null)
            {
                boolean networkIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                boolean gpsIsEnabled 	 = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                boolean passiveIsEnabled 	 = locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);

                if (ActivityCompat.checkSelfPermission(connnnn, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(connnnn, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    return null;
                }

                if(networkIsEnabled)
                {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    if (location == null)
                    {
                        if(gpsIsEnabled)
                        {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location == null)
                            {
                                return null;
                            }
                            else
                            {
                                return location;
                            }
                        }
                        else if(passiveIsEnabled)
                        {
                            location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                            if (location == null)
                            {
                                return null;
                            }
                            else
                            {
                                return location;
                            }
                        }
                        else
                        {
                            return null;
                        }
                    }
                    else
                    {
                        return location;
                    }

                }
                else if(gpsIsEnabled)
                {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if (location == null)
                    {
                        if(passiveIsEnabled)
                        {
                            location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                            if (location == null)
                            {
                                return null;
                            }
                            else
                            {
                                return location;
                            }
                        }
                        else
                        {
                            return null;
                        }
                    }
                    else
                    {
                        return location;
                    }
                }
                else if(passiveIsEnabled)
                {
                    location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                    if (location == null)
                    {
                        return null;
                    }
                    else
                    {
                        return location;
                    }
                }
                else
                {
                    return null;
                }
            }
            else
            {
             //   if(AppUtills.showLogs)Log.v(TAG,"getCurrentLocation Location Manager is null");
                return null;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public static void getAddressFromLocation(final double latitude, final double longitude, final Context context)
    {
        
        try
        {
            Thread thread = new Thread()
            {
                @Override
                public void run()
                {
                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                    try
                    {
                        ArrayList<Address> addressList = (ArrayList<Address>)geocoder.getFromLocation(latitude, longitude, 1);
                        if (addressList != null && addressList.size() > 0)
                        {
                            Address address = addressList.get(0);
                            
                            //Start Fetching postCode
                            if (address.getAddressLine(0) != null)
                            {
                                App_Conteroller.mAddress = address.getAddressLine(0);

                                App_Conteroller.mAddress = App_Conteroller.mAddress.replace(address.getCountryName(),"");
                                App_Conteroller.mAddress = App_Conteroller.mAddress.replace("Unnamed Rd", "");
                                App_Conteroller.mAddress = App_Conteroller.mAddress.replace("Unnamed Road", "");
                                App_Conteroller.mAddress = App_Conteroller.mAddress.replace("null","");

                                App_Conteroller.mAddress = App_Conteroller.mAddress.replace("Unnamed Rd,", "");
                                App_Conteroller.mAddress = App_Conteroller.mAddress.replace("Unnamed Road,", "");
                                App_Conteroller.mAddress = App_Conteroller.mAddress.replace("null,","");
                                App_Conteroller.mAddress = App_Conteroller.mAddress.replace(",","");
                                App_Conteroller.mAddress = App_Conteroller.mAddress.replace(" ,","");
                                App_Conteroller.mAddress = App_Conteroller.mAddress.replace(", ","");
                                App_Conteroller.mAddress = App_Conteroller.mAddress.replace(" , ","");
                                App_Conteroller.mAddress=App_Conteroller.mAddress.trim();

                            }
                            //End Fetching postCode

                            //Start Fetch Locality....
                            if (address.getSubLocality() != null)
                            {
                                App_Conteroller.subLocality = address.getSubLocality();

                                App_Conteroller.subLocality = App_Conteroller.subLocality.replace("Unnamed Rd", "");
                                App_Conteroller.subLocality = App_Conteroller.subLocality.replace("Unnamed Road", "");
                                App_Conteroller.subLocality = App_Conteroller.subLocality.replace("null","");

                                App_Conteroller.subLocality = App_Conteroller.subLocality.replace("Unnamed Rd,", "");
                                App_Conteroller.subLocality = App_Conteroller.subLocality.replace("Unnamed Road,", "");
                                App_Conteroller.subLocality = App_Conteroller.subLocality.replace("null,","");

                                App_Conteroller.subLocality = App_Conteroller.subLocality.replace(",","");
                                App_Conteroller.subLocality = App_Conteroller.subLocality.replace(" ,","");
                                App_Conteroller.subLocality = App_Conteroller.subLocality.replace(", ","");
                                App_Conteroller.subLocality = App_Conteroller.subLocality.replace(" , ","");
                                App_Conteroller.subLocality=App_Conteroller.subLocality.trim();
                            }
                            //End Fetching Locality

                            //Start Fetching City...
                            if (address.getSubAdminArea() == null)
                            {
                                App_Conteroller.city = address.getLocality();
                            }
                            else
                            {
                                App_Conteroller.city = address.getSubAdminArea();
                            }
                            App_Conteroller.city = App_Conteroller.city.replace("Unnamed Rd", "");
                            App_Conteroller.city = App_Conteroller.city.replace("Unnamed Road", "");
                            App_Conteroller.city = App_Conteroller.city.replace("null","");

                            App_Conteroller.city = App_Conteroller.city.replace("Unnamed Rd,", "");
                            App_Conteroller.city = App_Conteroller.city.replace("Unnamed Road,", "");
                            App_Conteroller.city = App_Conteroller.city.replace("null,","");

                            App_Conteroller.city = App_Conteroller.city.replace(",","");
                            App_Conteroller.city = App_Conteroller.city.replace(" ,","");
                            App_Conteroller.city = App_Conteroller.city.replace(", ","");
                            App_Conteroller.city = App_Conteroller.city.replace(" , ","");
                            App_Conteroller.city=App_Conteroller.city.trim();
                            //End Fetching City


                            //Start Fetching state
                            if (address.getAdminArea() != null)
                            {
                                App_Conteroller.state = address.getAdminArea();
                                App_Conteroller.state = App_Conteroller.state.replace("Unnamed Rd", "");
                                App_Conteroller.state = App_Conteroller.state.replace("Unnamed Road", "");
                                App_Conteroller.state = App_Conteroller.state.replace("null","");

                                App_Conteroller.state = App_Conteroller.state.replace("Unnamed Rd,", "");
                                App_Conteroller.state = App_Conteroller.state.replace("Unnamed Road,", "");
                                App_Conteroller.state = App_Conteroller.state.replace("null,","");

                                App_Conteroller.state = App_Conteroller.state.replace(",","");
                                App_Conteroller.state = App_Conteroller.state.replace(" ,","");
                                App_Conteroller.state = App_Conteroller.state.replace(", ","");
                                App_Conteroller.state = App_Conteroller.state.replace(" , ","");
                                App_Conteroller.state=App_Conteroller.state.trim();
                            }
                            //End Fetching state


                            //Start Fetching country
                            if (address.getCountryName() != null)
                            {
                                App_Conteroller.country = address.getCountryName();
                                App_Conteroller.country = App_Conteroller.country.replace("Unnamed Rd", "");
                                App_Conteroller.country = App_Conteroller.country.replace("Unnamed Road", "");
                                App_Conteroller.country = App_Conteroller.country.replace("null","");

                                App_Conteroller.country = App_Conteroller.country.replace("Unnamed Rd,", "");
                                App_Conteroller.country = App_Conteroller.country.replace("Unnamed Road,", "");
                                App_Conteroller.country = App_Conteroller.country.replace("null,","");

                                App_Conteroller.country = App_Conteroller.country.replace(",","");
                                App_Conteroller.country = App_Conteroller.country.replace(" ,","");
                                App_Conteroller.country = App_Conteroller.country.replace(", ","");
                                App_Conteroller.country = App_Conteroller.country.replace(" , ","");
                                App_Conteroller.country=App_Conteroller.country.trim();
                            }
                            //End Fetching country

                            //Start Fetching postCode
                            if (address.getPostalCode() != null)
                            {
                                App_Conteroller.postCode = address.getPostalCode();
                                App_Conteroller.postCode = App_Conteroller.postCode.replace("Unnamed Rd", "");
                                App_Conteroller.postCode = App_Conteroller.postCode.replace("Unnamed Road", "");
                                App_Conteroller.postCode = App_Conteroller.postCode.replace("null","");

                                App_Conteroller.postCode = App_Conteroller.postCode.replace("Unnamed Rd,", "");
                                App_Conteroller.postCode = App_Conteroller.postCode.replace("Unnamed Road,", "");
                                App_Conteroller.postCode = App_Conteroller.postCode.replace("null,","");

                                App_Conteroller.postCode = App_Conteroller.postCode.replace(",","");
                                App_Conteroller.postCode = App_Conteroller.postCode.replace(" ,","");
                                App_Conteroller.postCode = App_Conteroller.postCode.replace(", ","");
                                App_Conteroller.postCode = App_Conteroller.postCode.replace(" , ","");
                                App_Conteroller.postCode=App_Conteroller.postCode.trim();
                            }
                            //End Fetching postCode

                            if(App_Conteroller.mAddress.contains(App_Conteroller.subLocality))
                            {
                                App_Conteroller.subLocality=App_Conteroller.subLocality.replace(App_Conteroller.mAddress,"");
                            }

                            if(App_Conteroller.subLocality.equals(App_Conteroller.city))
                            {
                                App_Conteroller.city=App_Conteroller.city.replace(App_Conteroller.subLocality,"");
                            }

                            //Start Fetching full address
                            App_Conteroller.full_address="";
                            if(App_Conteroller.mAddress.equals(""))
                            {
                                if(App_Conteroller.subLocality.equals(""))
                                {
                                    if(App_Conteroller.city.equals(""))
                                    {
                                        if(App_Conteroller.state.equals(""))
                                        {
                                            if(App_Conteroller.country.equals(""))
                                            {
                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(App_Conteroller.full_address.equals(""))
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.country;
                                                }
                                                else
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.country;
                                                }

                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                        }
                                        else
                                        {
                                            if(App_Conteroller.full_address.equals(""))
                                            {
                                                App_Conteroller.full_address=App_Conteroller.state;
                                            }
                                            else
                                            {
                                                App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.state;
                                            }

                                            if(App_Conteroller.country.equals(""))
                                            {
                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(App_Conteroller.full_address.equals(""))
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.country;
                                                }
                                                else
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.country;
                                                }

                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    else
                                    {
                                        if(App_Conteroller.full_address.equals(""))
                                        {
                                            App_Conteroller.full_address=App_Conteroller.city;
                                        }
                                        else
                                        {
                                            App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.city;
                                        }

                                        if(App_Conteroller.state.equals(""))
                                        {
                                            if(App_Conteroller.country.equals(""))
                                            {
                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(App_Conteroller.full_address.equals(""))
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.country;
                                                }
                                                else
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.country;
                                                }

                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                        }
                                        else
                                        {
                                            if(App_Conteroller.full_address.equals(""))
                                            {
                                                App_Conteroller.full_address=App_Conteroller.state;
                                            }
                                            else
                                            {
                                                App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.state;
                                            }

                                            if(App_Conteroller.country.equals(""))
                                            {
                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(App_Conteroller.full_address.equals(""))
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.country;
                                                }
                                                else
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.country;
                                                }

                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                else
                                {
                                    if(App_Conteroller.full_address.equals(""))
                                    {
                                        App_Conteroller.full_address=App_Conteroller.subLocality;
                                    }
                                    else
                                    {
                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.subLocality;
                                    }

                                    if(App_Conteroller.city.equals(""))
                                    {
                                        if(App_Conteroller.state.equals(""))
                                        {
                                            if(App_Conteroller.country.equals(""))
                                            {
                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(App_Conteroller.full_address.equals(""))
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.country;
                                                }
                                                else
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.country;
                                                }

                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                        }
                                        else
                                        {
                                            if(App_Conteroller.full_address.equals(""))
                                            {
                                                App_Conteroller.full_address=App_Conteroller.state;
                                            }
                                            else
                                            {
                                                App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.state;
                                            }

                                            if(App_Conteroller.country.equals(""))
                                            {
                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(App_Conteroller.full_address.equals(""))
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.country;
                                                }
                                                else
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.country;
                                                }

                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    else
                                    {
                                        if(App_Conteroller.full_address.equals(""))
                                        {
                                            App_Conteroller.full_address=App_Conteroller.city;
                                        }
                                        else
                                        {
                                            App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.city;
                                        }

                                        if(App_Conteroller.state.equals(""))
                                        {
                                            if(App_Conteroller.country.equals(""))
                                            {
                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(App_Conteroller.full_address.equals(""))
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.country;
                                                }
                                                else
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.country;
                                                }

                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                        }
                                        else
                                        {
                                            if(App_Conteroller.full_address.equals(""))
                                            {
                                                App_Conteroller.full_address=App_Conteroller.state;
                                            }
                                            else
                                            {
                                                App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.state;
                                            }

                                            if(App_Conteroller.country.equals(""))
                                            {
                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(App_Conteroller.full_address.equals(""))
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.country;
                                                }
                                                else
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.country;
                                                }

                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            else
                            {
                                if(App_Conteroller.full_address.equals(""))
                                {
                                    App_Conteroller.full_address=App_Conteroller.mAddress;
                                }
                                else
                                {
                                    App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.mAddress;
                                }

                                if(App_Conteroller.subLocality.equals(""))
                                {
                                    if(App_Conteroller.city.equals(""))
                                    {
                                        if(App_Conteroller.state.equals(""))
                                        {
                                            if(App_Conteroller.country.equals(""))
                                            {
                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(App_Conteroller.full_address.equals(""))
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.country;
                                                }
                                                else
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.country;
                                                }

                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                        }
                                        else
                                        {
                                            if(App_Conteroller.full_address.equals(""))
                                            {
                                                App_Conteroller.full_address=App_Conteroller.state;
                                            }
                                            else
                                            {
                                                App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.state;
                                            }

                                            if(App_Conteroller.country.equals(""))
                                            {
                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(App_Conteroller.full_address.equals(""))
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.country;
                                                }
                                                else
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.country;
                                                }

                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    else
                                    {
                                        if(App_Conteroller.full_address.equals(""))
                                        {
                                            App_Conteroller.full_address=App_Conteroller.city;
                                        }
                                        else
                                        {
                                            App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.city;
                                        }

                                        if(App_Conteroller.state.equals(""))
                                        {
                                            if(App_Conteroller.country.equals(""))
                                            {
                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(App_Conteroller.full_address.equals(""))
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.country;
                                                }
                                                else
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.country;
                                                }

                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                        }
                                        else
                                        {
                                            if(App_Conteroller.full_address.equals(""))
                                            {
                                                App_Conteroller.full_address=App_Conteroller.state;
                                            }
                                            else
                                            {
                                                App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.state;
                                            }

                                            if(App_Conteroller.country.equals(""))
                                            {
                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(App_Conteroller.full_address.equals(""))
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.country;
                                                }
                                                else
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.country;
                                                }

                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                else
                                {
                                    if(App_Conteroller.full_address.equals(""))
                                    {
                                        App_Conteroller.full_address=App_Conteroller.subLocality;
                                    }
                                    else
                                    {
                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.subLocality;
                                    }

                                    if(App_Conteroller.city.equals(""))
                                    {
                                        if(App_Conteroller.state.equals(""))
                                        {
                                            if(App_Conteroller.country.equals(""))
                                            {
                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(App_Conteroller.full_address.equals(""))
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.country;
                                                }
                                                else
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.country;
                                                }

                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                        }
                                        else
                                        {
                                            if(App_Conteroller.full_address.equals(""))
                                            {
                                                App_Conteroller.full_address=App_Conteroller.state;
                                            }
                                            else
                                            {
                                                App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.state;
                                            }

                                            if(App_Conteroller.country.equals(""))
                                            {
                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(App_Conteroller.full_address.equals(""))
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.country;
                                                }
                                                else
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.country;
                                                }

                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    else
                                    {
                                        if(App_Conteroller.full_address.equals(""))
                                        {
                                            App_Conteroller.full_address=App_Conteroller.city;
                                        }
                                        else
                                        {
                                            App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.city;
                                        }

                                        if(App_Conteroller.state.equals(""))
                                        {
                                            if(App_Conteroller.country.equals(""))
                                            {
                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(App_Conteroller.full_address.equals(""))
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.country;
                                                }
                                                else
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.country;
                                                }

                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                        }
                                        else
                                        {
                                            if(App_Conteroller.full_address.equals(""))
                                            {
                                                App_Conteroller.full_address=App_Conteroller.state;
                                            }
                                            else
                                            {
                                                App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.state;
                                            }

                                            if(App_Conteroller.country.equals(""))
                                            {
                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(App_Conteroller.full_address.equals(""))
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.country;
                                                }
                                                else
                                                {
                                                    App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.country;
                                                }

                                                if(!App_Conteroller.postCode.equals(""))
                                                {
                                                    if(App_Conteroller.full_address.equals(""))
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.postCode;
                                                    }
                                                    else
                                                    {
                                                        App_Conteroller.full_address=App_Conteroller.full_address+", "+App_Conteroller.postCode;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            App_Conteroller.full_address = App_Conteroller.full_address.replace("Unnamed Rd", "");
                            App_Conteroller.full_address = App_Conteroller.full_address.replace("Unnamed Road", "");
                            App_Conteroller.full_address = App_Conteroller.full_address.replace("null","");

                            App_Conteroller.full_address = App_Conteroller.full_address.replace("Unnamed Rd,", "");
                            App_Conteroller.full_address = App_Conteroller.full_address.replace("Unnamed Road,", "");
                            App_Conteroller.full_address = App_Conteroller.full_address.replace("null,","");
                            App_Conteroller.full_address = App_Conteroller.full_address.replace(" ,","");
                            App_Conteroller.full_address = App_Conteroller.full_address.trim();
                            //End Fetching full address


                            App_Conteroller.latitute=latitude;
                            App_Conteroller.longitude=longitude;
                           }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void getSelectedAddressFromLocation(final double latitude, final double longitude, final Context connnnnn,final AddressListner addressListener)
    {

        try
        {
            Thread thread = new Thread()
            {
                @Override
                public void run()
                {
                    try
                    {
                        Geocoder geocoder = new Geocoder(connnnnn, Locale.getDefault());
                        ArrayList<Address> addressList = (ArrayList<Address>)geocoder.getFromLocation(latitude, longitude, 1);
                        if (addressList != null && addressList.size() > 0)
                        {
                            String mAddress="",subLocality="",city="",state="",country="",postCode="",full_address="";

                            Address address = addressList.get(0);

                            //Start Fetching postCode
                            if (address.getAddressLine(0) != null)
                            {
                                mAddress = address.getAddressLine(0);

                                mAddress = mAddress.replace(address.getCountryName(),"");
                                mAddress = mAddress.replace("Unnamed Rd", "");
                                mAddress = mAddress.replace("Unnamed Road", "");
                                mAddress = mAddress.replace("null","");

                                mAddress = mAddress.replace("Unnamed Rd,", "");
                                mAddress = mAddress.replace("Unnamed Road,", "");
                                mAddress = mAddress.replace("null,","");
                                mAddress = mAddress.replace(",","");
                                mAddress = mAddress.replace(" ,","");
                                mAddress = mAddress.replace(", ","");
                                mAddress = mAddress.replace(" , ","");
                                mAddress = mAddress.trim();

                            }
                            //End Fetching postCode

                            //Start Fetch Locality....
                            if (address.getSubLocality() != null)
                            {
                                subLocality = address.getSubLocality();

                                subLocality = subLocality.replace("Unnamed Rd", "");
                                subLocality = subLocality.replace("Unnamed Road", "");
                                subLocality = subLocality.replace("null","");

                                subLocality = subLocality.replace("Unnamed Rd,", "");
                                subLocality = subLocality.replace("Unnamed Road,", "");
                                subLocality = subLocality.replace("null,","");

                                subLocality = subLocality.replace(",","");
                                subLocality = subLocality.replace(" ,","");
                                subLocality = subLocality.replace(", ","");
                                subLocality = subLocality.replace(" , ","");
                                subLocality=subLocality.trim();
                            }
                            //End Fetching Locality

                            //Start Fetching City...
                            if (address.getSubAdminArea() == null)
                            {
                                city = address.getLocality();
                            }
                            else
                            {
                                city = address.getSubAdminArea();
                            }
                            city = city.replace("Unnamed Rd", "");
                            city = city.replace("Unnamed Road", "");
                            city = city.replace("null","");

                            city = city.replace("Unnamed Rd,", "");
                            city = city.replace("Unnamed Road,", "");
                            city = city.replace("null,","");

                            city = city.replace(",","");
                            city = city.replace(" ,","");
                            city = city.replace(", ","");
                            city = city.replace(" , ","");
                            city=city.trim();
                            //End Fetching City


                            //Start Fetching state
                            if (address.getAdminArea() != null)
                            {
                                state = address.getAdminArea();
                                state = state.replace("Unnamed Rd", "");
                                state = state.replace("Unnamed Road", "");
                                state = state.replace("null","");

                                state = state.replace("Unnamed Rd,", "");
                                state = state.replace("Unnamed Road,", "");
                                state = state.replace("null,","");

                                state = state.replace(",","");
                                state = state.replace(" ,","");
                                state = state.replace(", ","");
                                state = state.replace(" , ","");
                                state=state.trim();
                            }
                            //End Fetching state


                            //Start Fetching country
                            if (address.getCountryName() != null)
                            {
                                country = address.getCountryName();
                                country = country.replace("Unnamed Rd", "");
                                country = country.replace("Unnamed Road", "");
                                country = country.replace("null","");

                                country = country.replace("Unnamed Rd,", "");
                                country = country.replace("Unnamed Road,", "");
                                country = country.replace("null,","");

                                country = country.replace(",","");
                                country = country.replace(" ,","");
                                country = country.replace(", ","");
                                country = country.replace(" , ","");
                                country=country.trim();
                            }
                            //End Fetching country

                            //Start Fetching postCode
                            if (address.getPostalCode() != null)
                            {
                                postCode = address.getPostalCode();
                                postCode = postCode.replace("Unnamed Rd", "");
                                postCode = postCode.replace("Unnamed Road", "");
                                postCode = postCode.replace("null","");

                                postCode = postCode.replace("Unnamed Rd,", "");
                                postCode = postCode.replace("Unnamed Road,", "");
                                postCode = postCode.replace("null,","");

                                postCode = postCode.replace(",","");
                                postCode = postCode.replace(" ,","");
                                postCode = postCode.replace(", ","");
                                postCode = postCode.replace(" , ","");
                                postCode=postCode.trim();
                            }
                            //End Fetching postCode

                            if(mAddress.contains(subLocality))
                            {
                                subLocality=subLocality.replace(mAddress,"");
                            }

                            if(subLocality.equals(city))
                            {
                                city=city.replace(subLocality,"");
                            }

                            //Start Fetching full address
                            full_address="";
                            if(mAddress.equals(""))
                            {
                                if(subLocality.equals(""))
                                {
                                    if(city.equals(""))
                                    {
                                        if(state.equals(""))
                                        {
                                            if(country.equals(""))
                                            {
                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(full_address.equals(""))
                                                {
                                                    full_address=country;
                                                }
                                                else
                                                {
                                                    full_address=full_address+", "+country;
                                                }

                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                        }
                                        else
                                        {
                                            if(full_address.equals(""))
                                            {
                                                full_address=state;
                                            }
                                            else
                                            {
                                                full_address=full_address+", "+state;
                                            }

                                            if(country.equals(""))
                                            {
                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(full_address.equals(""))
                                                {
                                                    full_address=country;
                                                }
                                                else
                                                {
                                                    full_address=full_address+", "+country;
                                                }

                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    else
                                    {
                                        if(full_address.equals(""))
                                        {
                                            full_address=city;
                                        }
                                        else
                                        {
                                            full_address=full_address+", "+city;
                                        }

                                        if(state.equals(""))
                                        {
                                            if(country.equals(""))
                                            {
                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(full_address.equals(""))
                                                {
                                                    full_address=country;
                                                }
                                                else
                                                {
                                                    full_address=full_address+", "+country;
                                                }

                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                        }
                                        else
                                        {
                                            if(full_address.equals(""))
                                            {
                                                full_address=state;
                                            }
                                            else
                                            {
                                                full_address=full_address+", "+state;
                                            }

                                            if(country.equals(""))
                                            {
                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(full_address.equals(""))
                                                {
                                                    full_address=country;
                                                }
                                                else
                                                {
                                                    full_address=full_address+", "+country;
                                                }

                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                else
                                {
                                    if(full_address.equals(""))
                                    {
                                        full_address=subLocality;
                                    }
                                    else
                                    {
                                        full_address=full_address+", "+subLocality;
                                    }

                                    if(city.equals(""))
                                    {
                                        if(state.equals(""))
                                        {
                                            if(country.equals(""))
                                            {
                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(full_address.equals(""))
                                                {
                                                    full_address=country;
                                                }
                                                else
                                                {
                                                    full_address=full_address+", "+country;
                                                }

                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                        }
                                        else
                                        {
                                            if(full_address.equals(""))
                                            {
                                                full_address=state;
                                            }
                                            else
                                            {
                                                full_address=full_address+", "+state;
                                            }

                                            if(country.equals(""))
                                            {
                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(full_address.equals(""))
                                                {
                                                    full_address=country;
                                                }
                                                else
                                                {
                                                    full_address=full_address+", "+country;
                                                }

                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    else
                                    {
                                        if(full_address.equals(""))
                                        {
                                            full_address=city;
                                        }
                                        else
                                        {
                                            full_address=full_address+", "+city;
                                        }

                                        if(state.equals(""))
                                        {
                                            if(country.equals(""))
                                            {
                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(full_address.equals(""))
                                                {
                                                    full_address=country;
                                                }
                                                else
                                                {
                                                    full_address=full_address+", "+country;
                                                }

                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                        }
                                        else
                                        {
                                            if(full_address.equals(""))
                                            {
                                                full_address=state;
                                            }
                                            else
                                            {
                                                full_address=full_address+", "+state;
                                            }

                                            if(country.equals(""))
                                            {
                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(full_address.equals(""))
                                                {
                                                    full_address=country;
                                                }
                                                else
                                                {
                                                    full_address=full_address+", "+country;
                                                }

                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            else
                            {
                                if(full_address.equals(""))
                                {
                                    full_address=mAddress;
                                }
                                else
                                {
                                    full_address=full_address+", "+mAddress;
                                }

                                if(subLocality.equals(""))
                                {
                                    if(city.equals(""))
                                    {
                                        if(state.equals(""))
                                        {
                                            if(country.equals(""))
                                            {
                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(full_address.equals(""))
                                                {
                                                    full_address=country;
                                                }
                                                else
                                                {
                                                    full_address=full_address+", "+country;
                                                }

                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                        }
                                        else
                                        {
                                            if(full_address.equals(""))
                                            {
                                                full_address=state;
                                            }
                                            else
                                            {
                                                full_address=full_address+", "+state;
                                            }

                                            if(country.equals(""))
                                            {
                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(full_address.equals(""))
                                                {
                                                    full_address=country;
                                                }
                                                else
                                                {
                                                    full_address=full_address+", "+country;
                                                }

                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    else
                                    {
                                        if(full_address.equals(""))
                                        {
                                            full_address=city;
                                        }
                                        else
                                        {
                                            full_address=full_address+", "+city;
                                        }

                                        if(state.equals(""))
                                        {
                                            if(country.equals(""))
                                            {
                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(full_address.equals(""))
                                                {
                                                    full_address=country;
                                                }
                                                else
                                                {
                                                    full_address=full_address+", "+country;
                                                }

                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                        }
                                        else
                                        {
                                            if(full_address.equals(""))
                                            {
                                                full_address=state;
                                            }
                                            else
                                            {
                                                full_address=full_address+", "+state;
                                            }

                                            if(country.equals(""))
                                            {
                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(full_address.equals(""))
                                                {
                                                    full_address=country;
                                                }
                                                else
                                                {
                                                    full_address=full_address+", "+country;
                                                }

                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                else
                                {
                                    if(full_address.equals(""))
                                    {
                                        full_address=subLocality;
                                    }
                                    else
                                    {
                                        full_address=full_address+", "+subLocality;
                                    }

                                    if(city.equals(""))
                                    {
                                        if(state.equals(""))
                                        {
                                            if(country.equals(""))
                                            {
                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(full_address.equals(""))
                                                {
                                                    full_address=country;
                                                }
                                                else
                                                {
                                                    full_address=full_address+", "+country;
                                                }

                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                        }
                                        else
                                        {
                                            if(full_address.equals(""))
                                            {
                                                full_address=state;
                                            }
                                            else
                                            {
                                                full_address=full_address+", "+state;
                                            }

                                            if(country.equals(""))
                                            {
                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(full_address.equals(""))
                                                {
                                                    full_address=country;
                                                }
                                                else
                                                {
                                                    full_address=full_address+", "+country;
                                                }

                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    else
                                    {
                                        if(full_address.equals(""))
                                        {
                                            full_address=city;
                                        }
                                        else
                                        {
                                            full_address=full_address+", "+city;
                                        }

                                        if(state.equals(""))
                                        {
                                            if(country.equals(""))
                                            {
                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(full_address.equals(""))
                                                {
                                                    full_address=country;
                                                }
                                                else
                                                {
                                                    full_address=full_address+", "+country;
                                                }

                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                        }
                                        else
                                        {
                                            if(full_address.equals(""))
                                            {
                                                full_address=state;
                                            }
                                            else
                                            {
                                                full_address=full_address+", "+state;
                                            }

                                            if(country.equals(""))
                                            {
                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                if(full_address.equals(""))
                                                {
                                                    full_address=country;
                                                }
                                                else
                                                {
                                                    full_address=full_address+", "+country;
                                                }

                                                if(!postCode.equals(""))
                                                {
                                                    if(full_address.equals(""))
                                                    {
                                                        full_address=postCode;
                                                    }
                                                    else
                                                    {
                                                        full_address=full_address+", "+postCode;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            full_address = full_address.replace("Unnamed Rd", "");
                            full_address = full_address.replace("Unnamed Road", "");
                            full_address = full_address.replace("null","");

                            full_address = full_address.replace("Unnamed Rd,", "");
                            full_address = full_address.replace("Unnamed Road,", "");
                            full_address = full_address.replace("null,","");
                            full_address = full_address.replace(" ,","");
                            full_address = full_address.trim();

                            final String mAddress2=mAddress,subLocality2=subLocality,city2=city,state2=state,country2=country,postCode2=postCode,full_address2=full_address;
                            ((Activity)connnnnn).runOnUiThread(new Runnable()
                            {
                                public void run()
                                {
                                    addressListener.setGetAddress(mAddress2,subLocality2,city2,state2,country2,postCode2,full_address2);
                                }
                            });
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public interface AddressListner
    {
        void setGetAddress(String mAddress,String subLocality,String city,String state,String country,String postCode,String full_address);
    }

    public static void checkGpsAndsaveLocationAddress(final Context context)
    {
        try
        {

            Thread thread=new Thread()
            {
                public void run()
                {
                    try
                    {
                        synchronized(this)
                        {
                            wait(4000);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    ((Activity)context).runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Location location = App_Utils.getCurrentLocation(context);
                            if (location != null)
                            {
                                double latitude=0.0d,longitude=0.0d;

                                latitude=location.getLatitude();
                                longitude=location.getLongitude();

                                if(latitude!=0.0 &&longitude!=0.0 && latitude!=0 &&longitude!=0)
                                {
                                    App_Utils.getAddressFromLocation(latitude, longitude, context);
                                }
                                else
                                {
                                }
                            }
                            else
                            {
                                checkGpsAndsaveLocationAddress(context);
                            }
                        }
                    });
                }
            };
            thread.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public static boolean checkPlayServices(Context con)
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(con);
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode,((Activity)con),9000).show();
            }
            else
            {
                //finish();
            }
            return false;
        }
        return true;
    }


    public static String getCurrentdate()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String getdate=dateFormat.format(date);
        //2016/11/16 12:08:43
        return getdate;
    }

    public static boolean isNetworkAvailable(Context context)
    {
        boolean connected = false;
        try
        {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
            return connected;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return connected;
    }


    public static void alertDialog(Context context, String message)
    {
        try
        {
            AlertDialog.Builder dialog=new AlertDialog.Builder(context);
            dialog.setTitle("Nber");
            dialog.setMessage(message);
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            dialog.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }





}
