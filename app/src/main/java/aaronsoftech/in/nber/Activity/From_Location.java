package aaronsoftech.in.nber.Activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import aaronsoftech.in.nber.App_Conteroller;
import aaronsoftech.in.nber.R;
import aaronsoftech.in.nber.Utils.App_Utils;

public class From_Location extends AppCompatActivity implements LocationListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    String TAG="From_Location";
    LinearLayout coordinatorLayout;

    GoogleMap googleMap=null;
    LatLng FROM_latLng,TO_latlng;
    public boolean isLocationReceiverRegistered;
    String[] locationPermissions = {"android.permission.ACCESS_COARSE_LOCATION","android.permission.ACCESS_FINE_LOCATION"};

    private static final int REQUEST_CODE_LOCATION = 101;
    private static final int REQUEST_CODE_GPSON = 102;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 103;
    private static final int RESULT_CODE_MAPLOCATION = 104;
    EditText et_location,et_location2;
    TextView btn_done;

    Dialog dialog;
    String isFrom="";
    String focus_type="FROM";

    String FROM_LAT="";
    String FROM_LNG="";
    String TO_LAT="";
    String TO_LNG="";
    ArrayList<String> google_map_list = new ArrayList<String>();
    private static final int KEY_LATLNG=0xffffffff;
    private static final int KEY_ADDRESS=0xfffffffd;
    private static final int KEY_LACALITY=0xfffffffc;
    private static final int KEY_CITY=0xfffffffe;
    private static final int KEY_STATE=0xfffffffa;
    private static final int KEY_COUNTRY=0xffffffae;
    private static final int KEY_POSTCODE=0xffffffde;
    ProgressDialog progressDialog;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    List<Address> addressList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from__location);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ImageView get_from_Address_btn=findViewById(R.id.find_location);
        ImageView get_to_Address_btn=findViewById(R.id.find_location2);
        get_from_Address_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focus_type="FROM";
                et_location2.selectAll();
                et_location2.setTextIsSelectable(true);
                String location = et_location.getText().toString();
                set_location_list(location);
            }
        });
        get_to_Address_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focus_type="TO";
                et_location2.selectAll();
                et_location2.setTextIsSelectable(true);
                String location = et_location2.getText().toString();
                set_location_list(location);
            }
        });

        setToolbar();

            btn_done =(TextView)findViewById(R.id.txt_done);
            btn_done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Show_polyline_map();
                    Toast.makeText(From_Location.this, "Book your ride", Toast.LENGTH_SHORT).show();
                }
            });

            et_location2 = (EditText) findViewById(R.id.et_location2);
            et_location = (EditText) findViewById(R.id.et_location);


            et_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    focus_type="FROM";
                    et_location.selectAll();
                    et_location.setTextIsSelectable(true);
                    et_location.setBackground(getResources().getDrawable(R.drawable.login_et_rectangle2));
                    et_location2.setBackground(getResources().getDrawable(R.drawable.login_et_rectangle));
                }
            });
            et_location2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    focus_type="TO";
                    et_location2.selectAll();
                    et_location2.setTextIsSelectable(true);
                    et_location2.setBackground(getResources().getDrawable(R.drawable.login_et_rectangle2));
                    et_location.setBackground(getResources().getDrawable(R.drawable.login_et_rectangle));
                }
            });

            if(getIntent().getExtras()!=null)
            {
                isFrom=getIntent().getExtras().getString("isFrom");
            }

            Set_location_on_map();


    }

    private void Set_location_on_map() {
        if (App_Utils.isNetworkAvailable(From_Location.this))
        {
            if (App_Utils.checkPlayServices(From_Location.this))
            {
                // If this check succeeds, proceed with normal processing.
                // Otherwise, prompt user to get valid Play Services APK.
                if (!App_Utils.isLocationEnabled(From_Location.this))
                {
                    /** to call for GPS Enabling  */
                   /* Todo-----set call intent for open map permission*/
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), REQUEST_CODE_GPSON);
                }
                else
                {
                    if(App_Utils.checkAppVersion())
                    {
                        if (ActivityCompat.checkSelfPermission(From_Location.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(From_Location.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                        {
                            ActivityCompat.requestPermissions(From_Location.this, locationPermissions, REQUEST_CODE_LOCATION);
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
                Toast.makeText(From_Location.this, "Location not supported in this device", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "onCreate internet not found else called", Toast.LENGTH_SHORT).show();
        }
    }

    private void setToolbar() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolBarTitle = (TextView) toolBar.findViewById(R.id.toolbar_title);
        toolBarTitle.setText("Book your ride");
        toolBarTitle.setTextColor(Color.BLACK);
        setSupportActionBar(toolBar);

    }

    private void Show_polyline_map()
    {
        if (FROM_LAT!="" && FROM_LNG!="" && TO_LAT!="" && TO_LNG!="")
        {



            GoogleDirection.withServerKey(getResources().getString(R.string.google_maps_key))
                    .from(FROM_latLng)
                    .to(TO_latlng)
                    .transportMode(TransportMode.DRIVING).execute(new DirectionCallback() {
                @Override
                public void onDirectionSuccess(Direction direction, String rawBody) {
                    if (direction.isOK()) {
                        try {
                            Route route = direction.getRouteList().get(0);
                            google_map_list.add(String.valueOf(direction.getRouteList().get(0)));
                            try {
                                ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
                                googleMap.addPolyline(DirectionConverter.createPolyline(From_Location.this, directionPositionList, 4, getResources().getColor(R.color.light_green_700)));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                setCameraWithCoordinationBounds(route);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //  progressDialogmap.dismiss();
                        } catch (Exception e) {
                            //progressDialogmap.dismiss();
                            e.printStackTrace();
                        }
                        //  btnRequestDirection.setVisibility(View.GONE);
                    } else {
                        //  progressDialogmap.dismiss();
                        Snackbar.make(coordinatorLayout, direction.getStatus(), Snackbar.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onDirectionFailure(Throwable t) {
                    Snackbar.make(coordinatorLayout, t.getMessage(), Snackbar.LENGTH_SHORT).show();
                }
            });
            btn_done.setText("Distance in km: "+String.valueOf(distance(Double.valueOf(FROM_LAT),Double.valueOf(FROM_LNG),Double.valueOf(TO_LAT),Double.valueOf(TO_LNG))));


        }
        else {
            Toast.makeText(this, "Please select Start point and end point", Toast.LENGTH_SHORT).show();
        }

    }

    private double distance(double lat1, double lng1, double lat2, double lng2)
    {

        // double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output
        double earthRadius = 6371; // in km, change to 3958.75 for miles output
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double dist = earthRadius * c;// output distance, in Km

        Log.i(TAG,"Distance in km: "+dist);

        return dist; // output distance, in Km
    }

    private void setCameraWithCoordinationBounds(Route route)
    {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        //  mapFragment.getMapAsync(this);
    }

    private void showMap()
    {
        try
        {
            coordinatorLayout = (LinearLayout) findViewById(R.id.coordinatorLayout);

            if(App_Conteroller.latitute==0 || App_Conteroller.latitute==0.0 || App_Conteroller.longitude==0 || App_Conteroller.longitude==0.0)
            {
                App_Utils.checkGpsAndsaveLocationAddress(From_Location.this);
            }

            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap)).getMapAsync(new OnMapReadyCallback()
            {
                @Override
                public void onMapReady(GoogleMap gMap)
                {
                    if (gMap!=null)
                    {
                        if (ActivityCompat.checkSelfPermission(From_Location.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(From_Location.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                    if (focus_type.equalsIgnoreCase("FROM"))
                    {
                        FROM_latLng=latLng;
                        et_location.setTag(KEY_LATLNG,latLng);
                        et_location.setTag(KEY_ADDRESS,mAddress);
                        et_location.setTag(KEY_LACALITY,subLocality);
                        et_location.setTag(KEY_CITY,city);
                        et_location.setTag(KEY_STATE,state);
                        et_location.setTag(KEY_COUNTRY,country);
                        et_location.setTag(KEY_POSTCODE,postCode);
                        et_location.setText(""+fullAddress);
                        FROM_LAT= String.valueOf(latLng.latitude);
                        FROM_LNG=String.valueOf(latLng.longitude);
                    }else{
                        TO_latlng=latLng;
                        et_location2.setTag(KEY_LATLNG,latLng);
                        et_location2.setTag(KEY_ADDRESS,mAddress);
                        et_location2.setTag(KEY_LACALITY,subLocality);
                        et_location2.setTag(KEY_CITY,city);
                        et_location2.setTag(KEY_STATE,state);
                        et_location2.setTag(KEY_COUNTRY,country);
                        et_location2.setTag(KEY_POSTCODE,postCode);
                        et_location2.setText(""+fullAddress);
                        TO_LAT= String.valueOf(latLng.latitude);
                        TO_LNG=String.valueOf(latLng.longitude);
                    }

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
                                        App_Utils.getSelectedAddressFromLocation(googleMap.getCameraPosition().target.latitude, googleMap.getCameraPosition().target.longitude, From_Location.this, new App_Utils.AddressListner() {
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
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        /*mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }*/

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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


    private void set_location_list(final String location) {
        progressDialog=new ProgressDialog(From_Location.this);
        progressDialog.setMessage("Finding: "+location);
        progressDialog.setCancelable(false);
        progressDialog.show();

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                    addressList = geocoder.getFromLocationName(location, 3);

            } catch (IOException e) {
                e.printStackTrace();
            }
            try{
                double Add_lat= addressList.get(0).getLatitude();
                double Add_long= addressList.get(0).getLongitude();

                if (focus_type.equalsIgnoreCase("FROM"))
                {
                    FROM_LAT=String.valueOf(Add_lat);
                    FROM_LNG=String.valueOf(Add_long);
                    progressDialog.dismiss();
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }else{
                    TO_LAT=String.valueOf(Add_lat);
                    TO_LNG=String.valueOf(Add_long);
                    progressDialog.dismiss();
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            }catch (Exception e){
                progressDialog.dismiss();
                e.printStackTrace();}

        }
    }


}
