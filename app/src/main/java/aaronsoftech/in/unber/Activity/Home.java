package aaronsoftech.in.unber.Activity;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aaronsoftech.in.unber.Adapter.CustomInfoWindowAdapter;
import aaronsoftech.in.unber.App_Conteroller;
import aaronsoftech.in.unber.Model.FB_Driver_res;
import aaronsoftech.in.unber.POJO.Customwindow_const;
import aaronsoftech.in.unber.POJO.Response_Booking;
import aaronsoftech.in.unber.R;
import aaronsoftech.in.unber.Utils.SP_Utils;
import io.fabric.sdk.android.Fabric;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback{
    private GoogleMap mMap;
    CustomInfoWindowAdapter wind_adaptet;
    ArrayList<String> google_map_list = new ArrayList<String>();
    ImageView image_header;
    TextView header_name;
    String TAG="Home";
    int update_marker = 0;
    int update_marker2=0;
    String[] locationPermissionsl = {"android.permission.ACCESS_COARSE_LOCATION","android.permission.ACCESS_FINE_LOCATION"};
    private static int REQUEST_CODE_LOCATIONl = 102;
    private DatabaseReference mDatabase;
    double lat= 0.0;
    double lng= 0.0;
    boolean setCurrentLocation=true;
    List<FB_Driver_res >get_driver_loc=new ArrayList<>();
    String booked_id="";
    RelativeLayout coordinatorLayout;
    double oldlat,oldlong;
    int Accept_this_booking=0;
    String refreshedToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Fabric.with(this, new Crashlytics());

        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        try{
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            try{
                booked_id= String.valueOf(getIntent().getExtras().get("book_id"));
            }catch (Exception e){e.printStackTrace();}



            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            try{
                Toast.makeText(this, "User ID: "+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_ID,"")+"\n\n"+"Driver ID: "+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,""), Toast.LENGTH_SHORT).show();
            }catch (Exception e){e.printStackTrace();}

            coordinatorLayout=findViewById(R.id.layout_linear);
            LinearLayout get_loaction=findViewById(R.id.location_layout);
            get_loaction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_CONTACT_NUMBER,"").equalsIgnoreCase("")
                            || App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_EMAIL,"").equalsIgnoreCase(""))
                    {
                        Toast.makeText(Home.this, "Please Complite your profile", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Home.this,Acc_edit.class));
                    }else if (App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase("null")
                            || App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase(null)
                            || App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase(""))
                    {
                        startActivity(new Intent(Home.this,From_Location.class));
                    }else{
                        startActivity(new Intent(Home.this,Show_Vehicle.class));
                    }

                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);

            TextView btn_driver_login=headerView.findViewById(R.id.textView_driver);
            btn_driver_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_CONTACT_NUMBER,"").equalsIgnoreCase("")
                            || App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_EMAIL,"").equalsIgnoreCase(""))
                    {
                        Toast.makeText(Home.this, "Please Complite your profile", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Home.this,Acc_edit.class));
                    }else
                    {
                        if (App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase("null")
                                || App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase(null)
                                || App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase(""))
                        {
                            startActivity(new Intent(Home.this,Driver_document.class));
                        }else{
                            startActivity(new Intent(Home.this,Vehicle_reg.class));
                        }
                    }

                }
            });

            image_header = (ImageView) headerView.findViewById(R.id.imageView);
            header_name = (TextView) headerView.findViewById(R.id.textView_name);
            set_Header_value();
            image_header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Home.this,Acc_edit.class));
                }
            });
            navigationView.setNavigationItemSelectedListener(this);

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            Give_Permission();

            Check_User_Id_on_firebase();

        }catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);}

    }

    private void Save_Token_on_firebase() {
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (refreshedToken.equals(null) || refreshedToken=="")
        {
            Save_Token_on_firebase();
        }else{
            String id=App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"");
            mDatabase = FirebaseDatabase.getInstance().getReference();
            Map<String,String> map=new HashMap<>();
            map.put("token_id",refreshedToken);
            map.put("driver_id",id);
            mDatabase.child("Driver_Token_ID").child(id).setValue(map);
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

    }

    private void Check_driver_booking(final Location location) {
        Save_Token_on_firebase();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query myTopPostsQuery = mDatabase.child("Booking_ID");
        // My top posts by number of stars
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String Driver_ID=App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"");

                Log.d(TAG, "Number of messages: " + dataSnapshot.getChildrenCount());
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    // Extract a Message object from the DataSnapshot
                    Response_Booking message = child.getValue(Response_Booking.class);

                    if (message.getDriver_id().equalsIgnoreCase(Driver_ID) && (Accept_this_booking==0) )
                    {
                        Accept_this_booking=11;
                        addNotification();
                        Show_dialog_box(location);

                    }else if (message.getDriver_id().equalsIgnoreCase(Driver_ID) && (Accept_this_booking==22))
                    {
                        lat=location.getLatitude();
                        lng=location.getLongitude();
                        double speed=location.getSpeed();
                        Toast.makeText(Home.this, "lat------ "+lat, Toast.LENGTH_SHORT).show();
                        String driver_id=App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"");
                        HashMap<String,String> map=new HashMap<>();
                        map.put("driver_ID",""+driver_id);
                        map.put("name", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_NAME,""));
                        map.put("photo", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_PHOTO,""));
                        map.put("contact_number", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_CONTACT_NUMBER,""));
                        map.put("lat",""+lat);
                        map.put("lng",""+lng);
                        map.put("speed",""+speed);
                        Call_firebase_service(map);
                        Accept_this_booking=22;
                        MarkerOptions marker3 = null;

                        if (update_marker2==0){
                            marker3 = new MarkerOptions().position(new LatLng(lat, lng));
                            marker3.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                            mMap.addMarker(marker3);
                            update_marker2 = 1;
                            mMap.addCircle(new CircleOptions()
                                    .center(new LatLng(lat,lng))
                                    .radius(10)
                                    .strokeColor(Color.YELLOW)
                                    .fillColor(Color.TRANSPARENT));
                        }

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    private void Show_dialog_box(final Location location) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(Home.this);
        dialog.setTitle(getResources().getString(R.string.app_name));
        dialog.setMessage("Accept this booking");
        dialog.setCancelable(false);
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Accept_this_booking=0;
            }
        });
        dialog.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                lat=location.getLatitude();
                lng=location.getLongitude();
                double speed=location.getSpeed();
                Toast.makeText(Home.this, "lat------ "+lat, Toast.LENGTH_SHORT).show();
                String driver_id=App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"");
                HashMap<String,String> map=new HashMap<>();
                map.put("driver_ID",""+driver_id);
                map.put("name", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_NAME,""));
                map.put("photo", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_PHOTO,""));
                map.put("contact_number", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_CONTACT_NUMBER,""));
                map.put("lat",""+lat);
                map.put("lng",""+lng);
                map.put("speed",""+speed);
                Call_firebase_service(map);

                MarkerOptions marker3 = null;

                if (update_marker2==0){
                    marker3 = new MarkerOptions().position(new LatLng(lat, lng));
                    marker3.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                    mMap.addMarker(marker3);
                    update_marker2 = 1;
                    mMap.addCircle(new CircleOptions()
                            .center(new LatLng(lat,lng))
                            .radius(500)
                            .strokeColor(Color.YELLOW)
                            .fillColor(Color.TRANSPARENT));
                }
            }
        });
        dialog.show();

    }

    private void Check_User_Id_on_firebase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query myTopPostsQuery = mDatabase.child("Booking_ID");
        // My top posts by number of stars
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try{
                    String UserID=App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_ID,"");
                    // Get the data as Message objects
                    Log.d(TAG, "Number of messages: " + dataSnapshot.getChildrenCount());
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        // Extract a Message object from the DataSnapshot
                        Response_Booking message = child.getValue(Response_Booking.class);

                        if (message.getUser_id().equalsIgnoreCase(UserID))
                        {
                            String driver_id=message.getDriver_id();
                            LatLng from_latlng=new LatLng(Double.valueOf(message.getFrom_lat()),Double.valueOf(message.getFrom_lng()));
                            LatLng to_latlng=new LatLng(Double.valueOf(message.getTo_lat()),Double.valueOf(message.getTo_lng()));
                            addstart_end_icontrip(message.getFrom_address(),message.getTo_address(),Double.valueOf(message.getFrom_lat()),Double.valueOf(message.getFrom_lng()),Double.valueOf(message.getTo_lat()),Double.valueOf(message.getTo_lng()));
                            set_line_on_map(from_latlng,to_latlng);
                            Show_Driver_Location(driver_id);
                        }

                    }
                }catch (Exception e){e.printStackTrace();}

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    private void addstart_end_icontrip(String from_add,String to_address,double from_lat,double from_lng,double to_lat,double to_lng)
    {

        try {

            final MarkerOptions marker2e = new MarkerOptions().position(
                    new LatLng(from_lat, from_lng)).title("Pick up at:" + from_add);
            marker2e.icon(BitmapDescriptorFactory.fromResource(R.drawable.greenpin));
            final Customwindow_const infoew = new Customwindow_const();
            infoew.setSnippet(from_add);
            infoew.setTitle("Pick up at:");

            wind_adaptet = new CustomInfoWindowAdapter(this);
            mMap.setInfoWindowAdapter(wind_adaptet);

            Marker m = mMap.addMarker(marker2e);
            m.setTag(infoew);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            try {
                final MarkerOptions marker1e = new MarkerOptions().position(
                        //"Drop off at:"+"\n"+
                        new LatLng(to_lat, to_lng)).title("Drop off at: " + to_address);
                marker1e.icon(BitmapDescriptorFactory.fromResource(R.drawable.redpin));
                final Customwindow_const infoe = new Customwindow_const();
                infoe.setSnippet(to_address);
                infoe.setTitle("Drop off at:");
                wind_adaptet = new CustomInfoWindowAdapter(this);
                mMap.setInfoWindowAdapter(wind_adaptet);
                Marker m = mMap.addMarker(marker1e);
                m.setTag(infoe);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void set_line_on_map(LatLng from_latLng, LatLng to_latlng) {
        google_map_list.clear();
        GoogleDirection.withServerKey(getResources().getString(R.string.google_maps_key))
                .from(from_latLng)
                .to(to_latlng)
                .transportMode(TransportMode.DRIVING).execute(new DirectionCallback() {
            @Override
            public void onDirectionSuccess(Direction direction, String rawBody) {
                if (direction.isOK()) {
                    try {
                        Route route = direction.getRouteList().get(0);
                        google_map_list.add(String.valueOf(direction.getRouteList().get(0)));
                        try {
                            ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
                            mMap.addPolyline(DirectionConverter.createPolyline(Home.this, directionPositionList, 4, getResources().getColor(R.color.blue_700)));

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
    }

    private void setCameraWithCoordinationBounds(Route route)
    {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        //  mapFragment.getMapAsync(this);
    }

    private void set_Header_value() {
        try{
            String name= App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_NAME,"");
            String photo= App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_PHOTO,"");
            header_name.setText(name);
            Picasso.with(Home.this).load(photo)
                    .placeholder(R.drawable.ic_user)
                    .error(R.drawable.ic_user)
                    .into(image_header);
        }catch (Exception e){e.printStackTrace();}
    }


    @Override
    protected void onResume() {
        set_Header_value();
        super.onResume();
    }

    private void Give_Permission() {
        Handler handler  = new Handler();
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {

                if (ActivityCompat.checkSelfPermission(Home.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Home.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                       )                    {
                    ActivityCompat.requestPermissions(Home.this, locationPermissionsl, REQUEST_CODE_LOCATIONl);
                }
            }
        };
        handler.postDelayed(runnable, 2000);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //      int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        try
        {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().
                findFragmentById(R.id.map);
            assert mapFragment.getView() != null;
            final ViewGroup parent = (ViewGroup) mapFragment.getView().findViewWithTag("GoogleMapMyLocationButton").getParent();
            parent.post(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        for (int i = 0, n = parent.getChildCount(); i < n; i++) {
                            View view = parent.getChildAt(i);
                            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) view.getLayoutParams();
                            // position on right bottom
                            rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
                            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                            rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                            rlp.rightMargin = 25;
                            rlp.bottomMargin = 25;
                            view.requestLayout();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                if (setCurrentLocation){

                    lat=location.getLatitude();
                    lng=location.getLongitude();                    // Add a marker in Sydney and move the camera
                    LatLng sydney = new LatLng(lat, lng);
                    //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Udaipur"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(sydney)      // Sets the center of the map to Mountain View
                            .zoom(17)                   // Sets the zoom
                            .bearing(90)                // Sets the orientation of the camera to east
                            .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                            .build();                   // Creates a CameraPosition from the builder
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    setCurrentLocation=false;
                }

                try{
                    if (  App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase("null")
                            || App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase(null)
                            || App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase(""))
                    {

                    }else{
                        Check_driver_booking(location);
                    }
                }catch (Exception e){e.printStackTrace();}


            }
        });
    }

    private void Show_Driver_Location(String driver_id) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query myTopPostsQuery = mDatabase.child("Driver_ID").child(driver_id);

        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    get_driver_loc.clear();

                    String id=dataSnapshot.getKey();
                    String contactno = String.valueOf(dataSnapshot.child("contact_number").getValue());
                    String driver_ID = String.valueOf(dataSnapshot.child("driver_ID").getValue());
                    String name = String.valueOf(dataSnapshot.child("name").getValue());
                    String photo = String.valueOf(dataSnapshot.child("photo").getValue());
                    String lat = String.valueOf(dataSnapshot.child("lat").getValue());
                    String lng = String.valueOf(dataSnapshot.child("lng").getValue());
                    String speed = String.valueOf(dataSnapshot.child("speed").getValue());
                    get_driver_loc.add(new FB_Driver_res(driver_ID,name,photo,contactno,lat,lng,speed));
                MarkerOptions marker2 = null;

            try{
                if (get_driver_loc.size()!=0)
                {
                    for (int i=0;i<get_driver_loc.size();i++)
                    {

                    String  get_driverid= get_driver_loc.get(i).getDriver_ID();
                    String nameq=get_driver_loc.get(i).getName();
                    Toast.makeText(Home.this, "name "+nameq+"\n"+"get_driverid "+get_driverid, Toast.LENGTH_SHORT).show();
                    double get_lat= Double.valueOf(get_driver_loc.get(i).getLat());
                    double get_lng=Double.valueOf(get_driver_loc.get(i).getLng());

                        Location prevLoc = new Location("service Provider");
                        prevLoc.setLatitude(oldlat);
                        prevLoc.setLongitude(oldlong);
                        Location newLoc = new Location("service Provider");
                        newLoc.setLatitude(get_lat);
                        newLoc.setLongitude(get_lng);
                        float bearing = prevLoc.bearingTo(newLoc);

                        if (update_marker == 0)
                        {
                            marker2 = new MarkerOptions().position(new LatLng(get_lat, get_lng));
                            marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.car));
                            marker2.anchor(0.5f, 0.5f);
                            marker2.rotation(bearing);
                            marker2.flat(true);
                            mMap.addMarker(marker2);
                            update_marker = 1;
                        }
                        else
                        {
                            //   marker2 = new MarkerOptions().position(new LatLng(sokit_long, sokit_lan));
                            marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.car));
                            marker2.anchor(0.5f, 0.5f);
                            marker2.rotation(bearing);
                            marker2.flat(true);
                            mMap.addMarker(marker2);
                        }
                        oldlat=get_lat;
                        oldlong=get_lng;
                    }
                }
            }catch (Exception e){e.printStackTrace();}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_payment) {
            startActivity(new Intent(Home.this,Payment_add.class));
        } else if (id == R.id.nav_help) {
            startActivity(new Intent(Home.this,Trip_free.class));
        } else if (id == R.id.nav_free_trips) {
            startActivity(new Intent(Home.this,Acc_edit.class));
        } else if (id == R.id.nav_setting) {
            startActivity(new Intent(Home.this,Acc_setting.class));
        } else if (id == R.id.nav_your_trips) {
            startActivity(new Intent(Home.this,Trip.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void Call_firebase_service(HashMap<String, String> map) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String Driver_ID=App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Driver_ID").child(Driver_ID).setValue(map);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Log.i(TAG,"Firebase data : "+postSnapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.nber_logo);
        Intent intent = new Intent(this, Home.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.nber_logo));
        builder.setContentTitle("You have new booking");
        builder.setContentText("Accept this book ride");
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Will display the notification in the notification bar
        notificationManager.notify(1, builder.build());
    }
    /*private void addNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.nber_logo) //set icon for notification
                        .setContentTitle("You have new booking") //set title of notification
                        .setContentText("Accept this book ride")//this is notification message
                        .setAutoCancel(true) // makes auto cancel of notification
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT); //set priority of notification

        Intent notificationIntent = new Intent(this, Home.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //notification message will get at NotificationView
        notificationIntent.putExtra("message", "This is a notification message");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }*/

}
