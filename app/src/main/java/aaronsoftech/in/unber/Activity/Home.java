package aaronsoftech.in.unber.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aaronsoftech.in.unber.Adapter.Adapter_user_list;
import aaronsoftech.in.unber.Adapter.CustomInfoWindowAdapter;
import aaronsoftech.in.unber.App_Conteroller;
import aaronsoftech.in.unber.Model.FB_Driver_res;
import aaronsoftech.in.unber.POJO.Customwindow_const;
import aaronsoftech.in.unber.POJO.Response_Booking;
import aaronsoftech.in.unber.POJO.Response_Booking_List;
import aaronsoftech.in.unber.POJO.Response_register;
import aaronsoftech.in.unber.R;
import aaronsoftech.in.unber.Service.APIClient;
import aaronsoftech.in.unber.Utils.SP_Utils;
import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static aaronsoftech.in.unber.Utils.App_Utils.isNetworkAvailable;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,Adapter_user_list.Vehicle_Item_listner, PaymentResultListener {
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
    List<FB_Driver_res >get_driver_vehicle=new ArrayList<>();
    String booked_id="";
    RelativeLayout coordinatorLayout;
    double oldlat,oldlong;
    int Accept_this_booking=0;
    String refreshedToken;
    LinearLayout get_loaction;
    RecyclerView user_list_recycle;
    List<Response_Booking> get_Booking_list=new ArrayList<>();
    BottomSheetDialog bottomSheetDialog;
    BottomNavigationView bottomNavigationView;
    ProgressDialog progressDialog;
    List<Response_Booking_List.User_List> get_Booking_List=new ArrayList<>();
    LinearLayout layout_user_info,layout_user_profile_list;
    TextView btn_finish_ride_driver,btn_finish_ride_user;

    public void Init()
    {
        btn_finish_ride_driver =findViewById(R.id.txt_finish_ride);
        btn_finish_ride_user=findViewById(R.id.txt_finish_ride2);
        layout_user_profile_list=findViewById(R.id.layout_bottomsheet_list);
        layout_user_info=findViewById(R.id.layout_bottomsheet_user_info);
        coordinatorLayout=findViewById(R.id.layout_linear);
        get_loaction=findViewById(R.id.location_layout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void ShowBottomSheet(final List<Response_Booking_List.User_List> list, final String bookid) {
        layout_user_profile_list.setVisibility(View.VISIBLE);
        user_list_recycle=findViewById(R.id.user_list_view);
        StaggeredGridLayoutManager staggeredGridLayoutManager2 = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        user_list_recycle.setLayoutManager(staggeredGridLayoutManager2); // set LayoutManager to RecyclerView
        Adapter_user_list aa=new Adapter_user_list(Home.this,list,Home.this);
        btn_finish_ride_driver.setVisibility(View.GONE);
        btn_finish_ride_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_payment_gatway(list);
                Change_ride_status(list.get(0).getId(),list.get(0).getVehicle_id(),bookid);
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("Driver_ID").child(list.get(0).getDriver_id()).child("status").child("Deactive");
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        user_list_recycle.setAdapter(aa);

    }

    private void add_payment_gatway(List<Response_Booking_List.User_List> list) {
        final Activity activity = this;
        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", list.get(0).getUname());
            options.put("description", "Ride booking charges"+"\n"+"From :"+list.get(0).getFrom_address()+"\n"+"To :"+list.get(0).getTo_address());
            //You can omit the image option to fetch the image from dashboard
            options.put("image", list.get(0).getUimage());
            options.put("currency", "INR");
            DecimalFormat df2=new DecimalFormat("#.##");

            double price= (Double.parseDouble(list.get(0).getAmount()));

            String priceee=df2.format(price);
            String   pricee = priceee.substring(0, priceee.length() - 3);
            options.put("amount", String.valueOf(pricee+"00"));

            JSONObject preFill = new JSONObject();
       //     preFill.put("email", App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_EMAIL,""));
            preFill.put("contact", list.get(0).getUcontact());

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            btn_finish_ride_user.setVisibility(View.GONE);
            btn_finish_ride_driver.setVisibility(View.GONE);
            Log.i(TAG, "Error in payment: " +e.getMessage());
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_LONG
            )
                    .show();
            e.printStackTrace();
        }
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            btn_finish_ride_user.setVisibility(View.GONE);
            btn_finish_ride_driver.setVisibility(View.GONE);
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */

    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            btn_finish_ride_user.setVisibility(View.GONE);
            btn_finish_ride_driver.setVisibility(View.GONE);
            Log.i(TAG, "Exception in onPaymentError  Payment failed: " + code + " " + response);
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

    private void Change_ride_status(final String bookID, final String vehicleid, final String getbookid) {
        if (isNetworkAvailable(Home.this))
        {
            Map<String,String> map=new HashMap<>();
            map.put("id",bookID);
            map.put("status","Deactive");
            Call<Response_register> call= APIClient.getWebServiceMethod().get_booking_status_change(map);
            call.enqueue(new Callback<Response_register>() {
                @Override
                public void onResponse(Call<Response_register> call, Response<Response_register> response) {
                    progressDialog.dismiss();
                    try{
                        String status=response.body().getApi_status();
                        String msg=response.body().getApi_message();

                        if (status.equalsIgnoreCase("1") && msg.equalsIgnoreCase("success") )
                        {
                            Toast.makeText(Home.this, "Complite your ride", Toast.LENGTH_SHORT).show();
                            Change_vehicle_status(vehicleid,bookID);
                        }else{

                            Toast.makeText(Home.this, "status vehicle "+status+"\n"+" msg vehicle "+msg, Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e){
                        Log.i(TAG,"Exception : || Home || Change_ride_status "+e.toString());
      //                  Toast.makeText(Home.this, "Server error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();}

                }

                @Override
                public void onFailure(Call<Response_register> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(Home.this, "Error : "+t.toString(), Toast.LENGTH_SHORT).show();

                }
            });
        }else{
            Toast.makeText(Home.this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void Change_vehicle_status(String vehicleid, final String getbook_id) {
        if (isNetworkAvailable(Home.this))
        {
            Map<String,String> map=new HashMap<>();
            map.put("vehicle_type_id",vehicleid);
            map.put("status","Active");
            Call<Response_register> call= APIClient.getWebServiceMethod().update_change_vehicle_status(map);
            call.enqueue(new Callback<Response_register>() {
                @Override
                public void onResponse(Call<Response_register> call, Response<Response_register> response) {
                    progressDialog.dismiss();
                    try{
                        String status=response.body().getApi_status();
                        String msg=response.body().getApi_message();

                        if (status.equalsIgnoreCase("1") && msg.equalsIgnoreCase("success") )
                        {
                            //Toast.makeText(From_Location.this, "msg "+msg+"\n"+"id"+id, Toast.LENGTH_SHORT).show();
                            Toast.makeText(Home.this, "status change", Toast.LENGTH_SHORT).show();
                            String id=response.body().getId();


                        }else{

                            Toast.makeText(Home.this, "status vehicle "+status+"\n"+" msg vehicle "+msg, Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e){
   //                     Toast.makeText(Home.this, "Server error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();}

                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    try {
                        mDatabase.child("Booking_ID").child(getbook_id).child("status").setValue("Deactive");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Save_data_on_firebase(mDatabase);

                }

                @Override
                public void onFailure(Call<Response_register> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(Home.this, "Error : "+t.toString(), Toast.LENGTH_SHORT).show();

                }
            });
        }else{
            Toast.makeText(Home.this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Fabric.with(this, new Crashlytics());

        Init();
        Checkout.preload(getApplicationContext());

        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        try{
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            try{
                booked_id= String.valueOf(getIntent().getExtras().get("book_id"));
            }catch (Exception e){e.printStackTrace();}

            try{
                Toast.makeText(this, "User ID: "+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_ID,"")+"\n\n"+"Driver ID: "+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,""), Toast.LENGTH_SHORT).show();
            }catch (Exception e){e.printStackTrace();}

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

       final List<Integer> get_User_ID=new ArrayList<>();
        get_User_ID.clear();
        get_Booking_list.clear();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query myTopPostsQuery = mDatabase.child("Booking_ID");
        // My top posts by number of stars

        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Log.d(TAG, "Number of messages: " + dataSnapshot.getChildrenCount());
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    // Extract a Message object from the DataSnapshot
                    Response_Booking message = child.getValue(Response_Booking.class);
                    if (message.getStatus().toString().equalsIgnoreCase("Active") || message.getStatus().toString().equalsIgnoreCase("Running"))
                    {
                      //  String Driver_ID=App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"");
                        if (message.getDriver_id().equalsIgnoreCase(App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"")) && (Accept_this_booking==0) )
                        {
                                 Accept_this_booking=11;

                                if (message.getStatus().toString().equalsIgnoreCase("Running"))
                                {
                                    Set_running_value(message.getBook_id(), location,message.getVehicle_image(),message.getVehicle_type_id(),
                                            message.getVehicle_no(),message.getAmount(),message.getUser_contact(),message.getUser_image(),message.getUser_name());
                                }else{

                                    Show_dialog_box(message.getBook_id(), location,message.getVehicle_image(),message.getVehicle_type_id(),
                                            message.getVehicle_no(),message.getAmount(),message.getUser_contact(),message.getUser_image(),message.getUser_name());
                                    addNotification();
                                }

                                LatLng from_latlng=new LatLng(Double.valueOf(message.getFrom_lat()),Double.valueOf(message.getFrom_lng()));
                                LatLng to_latlng=new LatLng(Double.valueOf(message.getTo_lat()),Double.valueOf(message.getTo_lng()));
                                addstart_end_icontrip(message.getFrom_address(),message.getTo_address(),Double.valueOf(message.getFrom_lat()),Double.valueOf(message.getFrom_lng()),Double.valueOf(message.getTo_lat()),Double.valueOf(message.getTo_lng()));
                                set_line_on_map(from_latlng,to_latlng);


                        }else if (message.getDriver_id().equalsIgnoreCase(App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"")) && (Accept_this_booking==22))
                        {
                            lat=location.getLatitude();
                            lng=location.getLongitude();
                            double speed=location.getSpeed();
                            Toast.makeText(Home.this, "lat------ "+lat, Toast.LENGTH_SHORT).show();

                            String driver_id=App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"");
                            HashMap<String,String> map=new HashMap<>();
                            map.put("driver_ID",""+driver_id);
                            map.put("book_ID",""+message.getBook_id());
                            map.put("vehical_ID",""+message.getVehicle_id());
                            map.put("name", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_NAME,""));
                            map.put("photo", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_PHOTO,""));
                            map.put("contact_number", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_CONTACT_NUMBER,""));
                            map.put("lat",""+lat);
                            map.put("vehicle_no",message.getVehicle_no());
                            map.put("vehicle_type_id",message.getVehicle_type_id());
                            map.put("vehicle_image",message.getVehicle_image());
                            map.put("amount",message.getAmount());
                            map.put("speed",""+speed);
                            map.put("email", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_EMAIL,""));
                            map.put("address", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_ADDRESS,""));
                            map.put("city", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_CITY,""));
                            map.put("state", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_STATUS,""));
                            map.put("country", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_COUNTER,""));
                            map.put("status","Active");
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
                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                return false;
                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

    }

    private void Set_running_value(final String book_id, final Location location, final String veh_img, final String veh_type_id, final String veh_no, final String amount, final String contact, final String img, final String name) {
        String driverid=App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"");
        Call_driver_book_Api(driverid,contact,img,name,book_id);

        lat=location.getLatitude();
        lng=location.getLongitude();
        double speed=location.getSpeed();
        Toast.makeText(Home.this, "lat------ "+lat, Toast.LENGTH_SHORT).show();
        String driver_id=App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"");
        HashMap<String,String> mapw=new HashMap<>();
        mapw.put("driver_ID",""+driver_id);
        mapw.put("name", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_NAME,""));
        mapw.put("photo", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_PHOTO,""));
        mapw.put("contact_number", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_CONTACT_NUMBER,""));
        mapw.put("lat",""+lat);
        mapw.put("lng",""+lng);
        mapw.put("vehicle_no",veh_no);
        mapw.put("vehicle_type_id",veh_type_id);
        mapw.put("vehicle_image",veh_img);
        mapw.put("speed",""+speed);
        mapw.put("email", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_EMAIL,""));
        mapw.put("address", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_ADDRESS,""));
        mapw.put("city", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_CITY,""));
        mapw.put("state", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_STATUS,""));
        mapw.put("country", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_COUNTER,""));
        mapw.put("status","Active");
        Call_firebase_service(mapw);

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



    private void Call_driver_book_Api(String driver_ID, final String contact, final String img, final String name, final String bookid) {
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        get_Booking_List.clear();
        HashMap map= new HashMap<>();
        map.put("driver_id",driver_ID);
        if (isNetworkAvailable(Home.this))
        {
            Call<Response_Booking_List> call= APIClient.getWebServiceMethod().get_Driver_Booking(map);
            call.enqueue(new Callback<Response_Booking_List>() {
                @Override
                public void onResponse(Call<Response_Booking_List> call, Response<Response_Booking_List> response) {
                    progressDialog.dismiss();
                    try{
                        String status=response.body().getApi_status();
                        String msg=response.body().getApi_message();
                        if (status.equalsIgnoreCase("1") && msg.equalsIgnoreCase("success") )
                        {
                            List<Response_Booking_List.User_List> list=new ArrayList<>();
                            List<Response_Booking_List.User_List> get_list=new ArrayList<>();
                            list=response.body().getData();
                            for (int i=0;i<list.size();i++)
                            {
                                if (list.get(i).getStatus().toString().equalsIgnoreCase("Active"))
                                {
                                    get_list.add(list.get(i));
                                }
                            }

                            ShowBottomSheet(get_list,bookid);
                            get_Booking_List=get_list;

                        }else{

                            Toast.makeText(Home.this, "status "+status+"\n"+"msg "+msg, Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        Log.i(TAG,"Exception : || Home || Call_driver_book_Api "+e.toString());
//                        Toast.makeText(Home.this, "Server error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();}
                }

                @Override
                public void onFailure(Call<Response_Booking_List> call, Throwable t) {
                    Toast.makeText(Home.this, "Error : "+t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(Home.this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }



    private void Show_dialog_box(final String book_id, final Location location, final String veh_img, final String veh_type_id, final String veh_no, final String amount, final String contact, final String img, final String name) {
        try{
            AlertDialog.Builder dialog=new AlertDialog.Builder(Home.this);
            dialog.setTitle(getResources().getString(R.string.app_name));
            dialog.setMessage("Accept this booking");
            dialog.setCancelable(true);

            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Accept_this_booking=0;
                }
            });
            dialog.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    try {
                        mDatabase.child("Booking_ID").child(book_id).child("status").setValue("Running");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Save_data_on_firebase(mDatabase);

                    String driverid=App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"");
                    Call_driver_book_Api(driverid,contact,img,name,book_id);

                /*Map<String,String> map=new HashMap<>();
                //  map.put("token_id",refreshedToken);
                map.put("driver_id",location.get);
                mDatabase = FirebaseDatabase.getInstance().getReference();
                Query myTopPostsQuery = mDatabase.child("Driver_Token_ID");*/

                    lat=location.getLatitude();
                    lng=location.getLongitude();
                    double speed=location.getSpeed();
                    Toast.makeText(Home.this, "lat------ "+lat, Toast.LENGTH_SHORT).show();
                    String driver_id=App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"");
                    HashMap<String,String> mapw=new HashMap<>();
                    mapw.put("driver_ID",""+driver_id);
                    mapw.put("name", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_NAME,""));
                    mapw.put("photo", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_PHOTO,""));
                    mapw.put("contact_number", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_CONTACT_NUMBER,""));
                    mapw.put("lat",""+lat);
                    mapw.put("lng",""+lng);
                    mapw.put("vehicle_no",veh_no);
                    mapw.put("vehicle_type_id",veh_type_id);
                    mapw.put("vehicle_image",veh_img);
                    mapw.put("speed",""+speed);
                    mapw.put("email", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_EMAIL,""));
                    mapw.put("address", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_ADDRESS,""));
                    mapw.put("city", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_CITY,""));
                    mapw.put("state", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_STATUS,""));
                    mapw.put("country", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_COUNTER,""));
                    mapw.put("status","Active");
                    Call_firebase_service(mapw);

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
        }catch (Exception e){
            Log.i(TAG,"Exception : || Home || Show_dialog_box "+e.toString());
            e.printStackTrace();}



    }

    private void Check_User_Id_on_firebase() {
        get_Booking_list.clear();
        Show_all_driver_vehicle();
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

                        if (message.getUser_id().equalsIgnoreCase(UserID) && (!(message.getStatus().equalsIgnoreCase("Deactive"))))
                        {
                            String driver_id=message.getDriver_id();
                            LatLng from_latlng=new LatLng(Double.valueOf(message.getFrom_lat()),Double.valueOf(message.getFrom_lng()));
                            LatLng to_latlng=new LatLng(Double.valueOf(message.getTo_lat()),Double.valueOf(message.getTo_lng()));
                            addstart_end_icontrip(message.getFrom_address(),message.getTo_address(),Double.valueOf(message.getFrom_lat()),Double.valueOf(message.getFrom_lng()),Double.valueOf(message.getTo_lat()),Double.valueOf(message.getTo_lng()));
                            set_line_on_map(from_latlng,to_latlng);
                            Show_Driver_Location(driver_id,message);

                        }else{
                            layout_user_info.setVisibility(View.GONE);
                        }
                    }

                }catch (Exception e){e.printStackTrace();}

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    private void Show_all_driver_vehicle() {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query myTopPostsQuery = mDatabase.child("Driver_ID");

        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                get_driver_vehicle.clear();
                String lat = String.valueOf(dataSnapshot.child("lat").getValue());
                String lng = String.valueOf(dataSnapshot.child("lng").getValue());
                String veh_type_id = String.valueOf(dataSnapshot.child("vehicle_type_id").getValue());
                String state = String.valueOf(dataSnapshot.child("state").getValue());
                get_driver_vehicle.add(new FB_Driver_res(lat,lng,veh_type_id));
                MarkerOptions marker2 = null;
                try{
                    if (get_driver_vehicle.size()!=0)
                    {
                        for (int i=0;i<get_driver_vehicle.size();i++)
                        {
                            double get_lat= Double.valueOf(get_driver_vehicle.get(i).getLat());
                            double get_lng=Double.valueOf(get_driver_vehicle.get(i).getLng());
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
                                if (get_driver_vehicle.get(i).getVeh_type_id().toString().equalsIgnoreCase("8"))
                                {
                                    marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.auto_icon));
                                }else if (get_driver_vehicle.get(i).getVeh_type_id().toString().equalsIgnoreCase("7"))
                                {
                                    marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.bike));
                                }else if (get_driver_vehicle.get(i).getVeh_type_id().toString().equalsIgnoreCase("6"))
                                {
                                    marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.ok_car_icon));
                                }else if (get_driver_vehicle.get(i).getVeh_type_id().toString().equalsIgnoreCase("5"))
                                {
                                    marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.e_rickshaw));
                                }else if (get_driver_vehicle.get(i).getVeh_type_id().toString().equalsIgnoreCase("4"))
                                {
                                    marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.scooty));
                                }
                                marker2.anchor(0.5f, 0.5f);
                                marker2.rotation(bearing);
                                marker2.flat(true);
                                mMap.addMarker(marker2);
                                update_marker = 1;
                            }
                            else
                            {
                                if (get_driver_vehicle.get(i).getVeh_type_id().toString().equalsIgnoreCase("8"))
                                {
                                    marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.auto_icon));
                                }else if (get_driver_vehicle.get(i).getVeh_type_id().toString().equalsIgnoreCase("7"))
                                {
                                    marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.bike));
                                }else if (get_driver_vehicle.get(i).getVeh_type_id().toString().equalsIgnoreCase("6"))
                                {
                                    marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.ok_car_icon));
                                }else if (get_driver_vehicle.get(i).getVeh_type_id().toString().equalsIgnoreCase("5"))
                                {
                                    marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.e_rickshaw));
                                }else if (get_driver_vehicle.get(i).getVeh_type_id().toString().equalsIgnoreCase("4"))
                                {
                                    marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.scooty));
                                }
                                marker2.anchor(0.5f, 0.5f);
                                marker2.rotation(bearing);
                                marker2.flat(true);
                                mMap.addMarker(marker2);
                            }

                            Response_Booking message=new Response_Booking();
                            show_driver_profile(get_driver_vehicle, message);
                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    return false;
                                }
                            });
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

            LatLng sydney = new LatLng(from_lat, from_lng);
            //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Udaipur"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(sydney)      // Sets the center of the map to Mountain View
                    .zoom(12)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(90)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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
                            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
                            rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                            rlp.rightMargin = 25;
                            rlp.topMargin = 200;
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
            try{
                if (setCurrentLocation){

                    lat=location.getLatitude();
                    lng=location.getLongitude();                    // Add a marker in Sydney and move the camera
                    LatLng sydney = new LatLng(lat, lng);
                    //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Udaipur"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(sydney)      // Sets the center of the map to Mountain View
                            .zoom(15)                   // Sets the zoom
                            .bearing(90)                // Sets the orientation of the camera to east
                            .tilt(90)                   // Sets the tilt of the camera to 30 degrees
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

            }catch (Exception e){e.printStackTrace();}


            }
        });
    }


    private void Save_data_on_firebase(DatabaseReference mDatabase) {
        // Read from the database
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                layout_user_profile_list.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void Show_Driver_Location(String driver_id, final Response_Booking message) {
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
                    String address = String.valueOf(dataSnapshot.child("address").getValue());
                    String city = String.valueOf(dataSnapshot.child("city").getValue());
                    String email = String.valueOf(dataSnapshot.child("email").getValue());
                    String veh_type_id = String.valueOf(message.getVehicle_type_id());
                    String veh_no =  String.valueOf(message.getVehicle_no());
                    String amount =  String.valueOf(message.getAmount());
                    String veh_img =  String.valueOf(message.getVehicle_image());
                    String vehicle_id=String.valueOf(message.getVehicle_id());
                    String book_id=String.valueOf(message.getBook_id());

                    String state = String.valueOf(dataSnapshot.child("state").getValue());

                    get_driver_loc.add(new FB_Driver_res(driver_ID,name,photo,contactno,lat,lng,address,city,email,email,state,veh_type_id,veh_no,veh_img,amount,vehicle_id,book_id));

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
                            if (get_driver_loc.get(i).getVeh_type_id().toString().equalsIgnoreCase("8"))
                            {
                                marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.auto_icon));
                            }else if (get_driver_loc.get(i).getVeh_type_id().toString().equalsIgnoreCase("7"))
                            {
                                marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.bike));
                            }else if (get_driver_loc.get(i).getVeh_type_id().toString().equalsIgnoreCase("6"))
                            {
                                marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.ok_car_icon));
                            }else if (get_driver_loc.get(i).getVeh_type_id().toString().equalsIgnoreCase("5"))
                            {
                                marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.e_rickshaw));
                            }else if (get_driver_loc.get(i).getVeh_type_id().toString().equalsIgnoreCase("4"))
                            {
                                marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.scooty));
                            }

                            marker2.anchor(0.5f, 0.5f);
                            marker2.rotation(bearing);
                            marker2.flat(true);
                            mMap.addMarker(marker2);
                            update_marker = 1;
                        }
                        else
                        {
                            if (get_driver_loc.get(i).getVeh_type_id().toString().equalsIgnoreCase("8"))
                            {
                                marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.auto_icon));
                            }else if (get_driver_loc.get(i).getVeh_type_id().toString().equalsIgnoreCase("7"))
                            {
                                marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.bike));
                            }else if (get_driver_loc.get(i).getVeh_type_id().toString().equalsIgnoreCase("6"))
                            {
                                marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.ok_car_icon));
                            }else if (get_driver_loc.get(i).getVeh_type_id().toString().equalsIgnoreCase("5"))
                            {
                                marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.e_rickshaw));
                            }else if (get_driver_loc.get(i).getVeh_type_id().toString().equalsIgnoreCase("4"))
                            {
                                marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.scooty));
                            }
                            marker2.anchor(0.5f, 0.5f);
                            marker2.rotation(bearing);
                            marker2.flat(true);
                            mMap.addMarker(marker2);
                        }

                        LatLng sydney = new LatLng(get_lat, get_lng);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(sydney)      // Sets the center of the map to Mountain View
                                .zoom(29)                   // Sets the zoom
                                .bearing(90)                // Sets the orientation of the camera to east
                                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                                .build();                   // Creates a CameraPosition from the builder
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                        show_driver_profile(get_driver_loc,message);
                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {

                                return false;
                            }
                        });
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

    private void show_driver_profile(final List<FB_Driver_res> get_driver_loc_2, final Response_Booking message) {
      //  View view = getLayoutInflater().inflate(R.layout.layout_bottomsheet_driver_profile, null);
        layout_user_info.setVisibility(View.VISIBLE);
        CircleImageView driver_image=findViewById(R.id.driver_img);
        TextView driver_name=findViewById(R.id.txt_name);
        TextView driver_contect=findViewById(R.id.txt_mobile);
        TextView driver_veh_no=findViewById(R.id.txt_veh_no);
        TextView driver_amount=findViewById(R.id.txt_amount);
        driver_amount.setText("Amount :"+ get_driver_loc_2.get(0).getAmount());
        driver_veh_no.setText("Vehicle no :"+ get_driver_loc_2.get(0).getVeh_no());
        driver_name.setText("Driver name :"+ get_driver_loc_2.get(0).getName());
        driver_contect.setText("Driver mobile no. :"+ get_driver_loc_2.get(0).getContact_number());
        final String txt_contect= get_driver_loc_2.get(0).getContact_number();
        driver_contect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+txt_contect));
                startActivity(intent);
            }
        });



        String imguri= get_driver_loc_2.get(0).getPhoto().toString();
        try{
            Picasso.with(Home.this).load(imguri).error(R.drawable.ic_user).into(driver_image);
        }catch (Exception e){e.printStackTrace();}

        btn_finish_ride_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog=new ProgressDialog(Home.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Loading...");
                progressDialog.show();

                get_Booking_List.clear();
                add_payment_gatway_user(message);
                Change_ride_status(message.getBook_id(),message.getVehicle_id(),get_driver_loc_2.get(0).getBook_ID());

                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("Driver_ID").child(get_driver_loc_2.get(0).getDriver_ID()).child("status").child("Deactive");
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void add_payment_gatway_user(Response_Booking get_booking) {
        final Activity activity = this;
        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_NAME,""));
            options.put("description", "Ride booking charges"+"\n"+"From :"+get_booking.getFrom_address()+"\n"+"To :"+get_booking.getTo_address());
            //You can omit the image option to fetch the image from dashboard
            options.put("image", App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_PHOTO,""));
            options.put("currency", "INR");
            DecimalFormat df2=new DecimalFormat("#.##");

            double price= (Double.parseDouble(get_booking.getAmount()));

            String priceee=df2.format(price);
            String   pricee = priceee.substring(0, priceee.length() - 3);
            options.put("amount", String.valueOf(pricee+"00"));

            JSONObject preFill = new JSONObject();
            //     preFill.put("email", App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_EMAIL,""));
            preFill.put("contact", App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_CONTACT_NUMBER,""));

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Log.i(TAG, "Error in payment: " +e.getMessage());
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_LONG
            )
                    .show();
            e.printStackTrace();
        }
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


    @Override
    public void OnClick_item(Response_Booking_List.User_List user_list) {

    }
}
