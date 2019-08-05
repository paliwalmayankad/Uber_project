package aaronsoftech.in.unber.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import aaronsoftech.in.unber.App_Conteroller;
import aaronsoftech.in.unber.R;
import aaronsoftech.in.unber.Utils.App_Utils;
import aaronsoftech.in.unber.Utils.SP_Utils;

public class MapsActivity extends FragmentActivity implements LocationListener,GoogleApiClient.ConnectionCallbacks{
    Context con;
    private GoogleMap mMap;
    TextView et_location;
    String[] locationPermissions = {"android.permission.ACCESS_COARSE_LOCATION","android.permission.ACCESS_FINE_LOCATION"};
    private static final int REQUEST_CODE_LOCATION = 101;
    private static final int REQUEST_CODE_GPSON = 102;
    LinearLayout coordinatorLayout;
    GoogleMap googleMap=null;

    private static final int KEY_LATLNG=0xffffffff;
    private static final int KEY_ADDRESS=0xfffffffd;
    private static final int KEY_LACALITY=0xfffffffc;
    private static final int KEY_CITY=0xfffffffe;
    private static final int KEY_STATE=0xfffffffa;
    private static final int KEY_COUNTRY=0xffffffae;
    private static final int KEY_POSTCODE=0xffffffde;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    String get_ADDRESS;
    String get_LAT;
    String get_LNG;
    String TYPE="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        et_location =  findViewById(R.id.et_location);
        TYPE=getIntent().getExtras().getString("type","");

        ImageView back_btn=findViewById(R.id.btn_back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView btn_done=findViewById(R.id.txt_done);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                App_Conteroller.sharedpreferences = getSharedPreferences(App_Conteroller.MyPREFERENCES, Context.MODE_PRIVATE);
                App_Conteroller.editor = App_Conteroller.sharedpreferences.edit();
                if (TYPE.equalsIgnoreCase("1"))
                {
                    App_Conteroller. editor.putString(SP_Utils.LOGIN_USR_HOME_ADDRESS,""+get_ADDRESS);
                    App_Conteroller. editor.putString(SP_Utils.LOGIN_USR_HOME_LAT,""+get_LAT);
                    App_Conteroller. editor.putString(SP_Utils.LOGIN_USR_HOME_LNG,""+get_LNG);
                }else if (TYPE.equalsIgnoreCase("2"))
                {
                    App_Conteroller. editor.putString(SP_Utils.LOGIN_USR_WORK_ADDRESS,""+get_ADDRESS);
                    App_Conteroller. editor.putString(SP_Utils.LOGIN_USR_WORK_LAT,""+get_LAT);
                    App_Conteroller. editor.putString(SP_Utils.LOGIN_USR_WORK_LNG,""+get_LNG);
                }else if (TYPE.equalsIgnoreCase("3"))
                {
                    App_Conteroller. editor.putString(SP_Utils.LOGIN_USR_PLACE_ADDRESS,""+get_ADDRESS);
                    App_Conteroller. editor.putString(SP_Utils.LOGIN_USR_PLACE_LAT,""+get_LAT);
                    App_Conteroller. editor.putString(SP_Utils.LOGIN_USR_PLACE_LNG,""+get_LNG);
                }

                App_Conteroller. editor.commit();
                finish();
            }
        });

        TextView toolBarTitle = (TextView)findViewById(R.id.toolbar_title);
        if (TYPE.equalsIgnoreCase("1"))
        {
            toolBarTitle.setText("Save Work Address");
        }else if (TYPE.equalsIgnoreCase("2"))
        {
            toolBarTitle.setText("Save Home Address");
        }else if (TYPE.equalsIgnoreCase("3"))
        {
            toolBarTitle.setText("Save Place Address");
        }
        Set_location_on_map();


    }

    private void Set_location_on_map() {
        if (App_Utils.isNetworkAvailable(MapsActivity.this))
        {
            if (App_Utils.checkPlayServices(MapsActivity.this))
            {
                // If this check succeeds, proceed with normal processing.
                // Otherwise, prompt user to get valid Play Services APK.
                if (!App_Utils.isLocationEnabled(MapsActivity.this))
                {
                    /** to call for GPS Enabling  */
                    /* Todo-----set call intent for open map permission*/
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), REQUEST_CODE_GPSON);
                }
                else
                {
                    if(App_Utils.checkAppVersion())
                    {
                        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                        {
                            ActivityCompat.requestPermissions(MapsActivity.this, locationPermissions, REQUEST_CODE_LOCATION);
                        }
                        else
                        {
                            showMap();
                        }
                    }
                    else
                    {
                        showMap();
                    }
                }
            }
            else
            {
                Toast.makeText(MapsActivity.this, "Location not supported in this device", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "onCreate internet not found else called", Toast.LENGTH_SHORT).show();
        }


    }
    private void showMap()
    {
        try
        {
            coordinatorLayout = (LinearLayout) findViewById(R.id.coordinatorLayout);

            if(App_Conteroller.latitute==0 || App_Conteroller.latitute==0.0 || App_Conteroller.longitude==0 || App_Conteroller.longitude==0.0)
            {
                App_Utils.checkGpsAndsaveLocationAddress(MapsActivity.this);
            }

            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap)).getMapAsync(new OnMapReadyCallback()
            {
                @Override
                public void onMapReady(GoogleMap gMap)
                {
                    if (gMap!=null)
                    {
                        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        googleMap = gMap;
                        if (googleMap != null)
                        {
                            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            googleMap.setMyLocationEnabled(true);
                            googleMap.getUiSettings().setZoomControlsEnabled(false);
                            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                            googleMap.getUiSettings().setCompassEnabled(true);
                            googleMap.getUiSettings().setRotateGesturesEnabled(true);
                            googleMap.getUiSettings().setZoomGesturesEnabled(true);
                        }

                        if(App_Conteroller.latitute==0 || App_Conteroller.latitute==0.0 || App_Conteroller.longitude==0 || App_Conteroller.longitude==0.0)
                        {
                            new Handler().postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    displayMap();
                                    buildGoogleApiClient();
                                }
                            },1000);
                        }
                        else
                        {
                            displayMap();
                            buildGoogleApiClient();

                        }
                    }
                    else
                    {
                        App_Utils.showAlertSnakeMessage(coordinatorLayout,"Sorry! Location will not found.");
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void startMapAnimation()
    {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(App_Conteroller.latitute,App_Conteroller.longitude), 17), 2200,new GoogleMap.CancelableCallback()
        {
            @Override
            public void onCancel()
            {

            }

            @Override
            public void onFinish()
            {
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                        .target(new LatLng(App_Conteroller.latitute,App_Conteroller.longitude))
                        .zoom(googleMap.getCameraPosition().zoom)
                        .bearing(300F)
                        .tilt(50F)
                        .build()),1500,this);
            }
        });
    }
    private void setEditTextAddress(final String mAddress, final String subLocality, final String city, final String state, final String country, final String postCode, final String fullAddress,final LatLng latLng)
    {
        try
        {
            runOnUiThread(new Runnable()
            {
                public void run()
                {

                        et_location.setTag(KEY_LATLNG,latLng);
                        et_location.setTag(KEY_ADDRESS,mAddress);
                        et_location.setTag(KEY_LACALITY,subLocality);
                        et_location.setTag(KEY_CITY,city);
                        et_location.setTag(KEY_STATE,state);
                        et_location.setTag(KEY_COUNTRY,country);
                        et_location.setTag(KEY_POSTCODE,postCode);
                        et_location.setText(""+fullAddress);

                        get_LAT= String.valueOf(latLng.latitude);
                        get_LNG=String.valueOf(latLng.longitude);
                        get_ADDRESS=String.valueOf(fullAddress);

                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void displayMap()
    {
        try
        {
            if(googleMap!=null && App_Conteroller.latitute!=0 && App_Conteroller.latitute!=0.0 && App_Conteroller.longitude!=0 && App_Conteroller.longitude!=0.0)
            {

                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                startMapAnimation();

                setEditTextAddress(App_Conteroller.mAddress,App_Conteroller.subLocality,App_Conteroller.city,App_Conteroller.state,App_Conteroller.country,App_Conteroller.postCode,App_Conteroller.full_address,new LatLng(App_Conteroller.latitute,App_Conteroller.longitude));

                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()
                {
                    @Override
                    public void onInfoWindowClick(Marker arg0)
                    {
                        try
                        {
                            setEditTextAddress(App_Conteroller.mAddress,App_Conteroller.subLocality,App_Conteroller.city,App_Conteroller.state,App_Conteroller.country,App_Conteroller.postCode,App_Conteroller.full_address,new LatLng(App_Conteroller.latitute,App_Conteroller.longitude));
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });

                googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener()
                {
                    @Override
                    public boolean onMyLocationButtonClick()
                    {
                        try
                        {
                            setEditTextAddress(App_Conteroller.mAddress,App_Conteroller.subLocality,App_Conteroller.city,App_Conteroller.state,App_Conteroller.country,App_Conteroller.postCode,App_Conteroller.full_address,new LatLng(App_Conteroller.latitute,App_Conteroller.longitude));


                            if(googleMap!=null && App_Conteroller.latitute!=0 && App_Conteroller.latitute!=0.0 && App_Conteroller.longitude!=0 && App_Conteroller.longitude!=0.0)
                            {
                                startMapAnimation();
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        return false;
                    }
                });

                googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener()
                {
                    @Override
                    public void onCameraIdle()
                    {

                        runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                if(googleMap.getCameraPosition()!=null)
                                {
                                    if(googleMap.getCameraPosition().target.latitude!=0 && googleMap.getCameraPosition().target.latitude!=0.0 && googleMap.getCameraPosition().target.longitude!=0 && googleMap.getCameraPosition().target.longitude!=0.0)
                                    {
                                        App_Utils.getSelectedAddressFromLocation(googleMap.getCameraPosition().target.latitude, googleMap.getCameraPosition().target.longitude, MapsActivity.this, new App_Utils.AddressListner() {
                                            @Override
                                            public void setGetAddress(String mAddress, String subLocality, String city, String state, String country, String postCode, String full_address) {
                                                setEditTextAddress(mAddress,subLocality,city,state,country,postCode,full_address,new LatLng(googleMap.getCameraPosition().target.latitude,googleMap.getCameraPosition().target.longitude));

                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                });
            }
            else
            {
                showMap();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        mCurrLocationMarker = googleMap.addMarker(markerOptions);
        //move map camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
