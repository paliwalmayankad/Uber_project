package aaronsoftech.in.nber.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
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
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import aaronsoftech.in.nber.Adapter.Adapter_Driver_vehicle;
import aaronsoftech.in.nber.Adapter.Adapter_user_list;
import aaronsoftech.in.nber.Adapter.CustomInfoWindowAdapter;
import aaronsoftech.in.nber.App_Conteroller;
import aaronsoftech.in.nber.BuildConfig;
import aaronsoftech.in.nber.Model.FB_Driver_res;
import aaronsoftech.in.nber.POJO.Customwindow_const;
import aaronsoftech.in.nber.POJO.Resp_Common;
import aaronsoftech.in.nber.POJO.Response_Booking;
import aaronsoftech.in.nber.POJO.Response_Booking_List;
import aaronsoftech.in.nber.POJO.Response_Driver_vehicle;
import aaronsoftech.in.nber.POJO.Response_register;
import aaronsoftech.in.nber.POJO.Wallet;
import aaronsoftech.in.nber.R;
import aaronsoftech.in.nber.Service.APIClient;
import aaronsoftech.in.nber.Utils.App_Utils;
import aaronsoftech.in.nber.Utils.SP_Utils;
import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static aaronsoftech.in.nber.Utils.App_Utils.isNetworkAvailable;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback,Adapter_user_list.Vehicle_Item_listner, PaymentResultListener , LocationListener {
    private GoogleMap mMap;
    CustomInfoWindowAdapter wind_adaptet;
    ArrayList<String> google_map_list = new ArrayList<String>();
    ImageView image_header;
    TextView header_name;
    String TAG="Home";
    int update_marker = 0;
    int update_markerc = 0;
    int update_marker2=0;
    String[] locationPermissionsl = {"android.permission.ACCESS_COARSE_LOCATION","android.permission.ACCESS_FINE_LOCATION"};
    private static int REQUEST_CODE_LOCATIONl = 102;
    private DatabaseReference mDatabase;
    double lat= 0.0;
    double lng= 0.0;
    static DrawerLayout drawer;
    double current_lat= 0.0;
    double current_lng= 0.0;
    boolean setCurrentLocation=true;

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
    TextView btn_finish_ride_driver,btn_finish_ride_user,btn_from_address;
    String get_Selected_Driver_Id;
    String get_BookID_Status,get_vehicle_id_status,get_book_id_2;
    RadioButton btn_driver_status;
    boolean check_user_from_to_location=false;
    HashMap payment_history_map=new HashMap();
    TextView txt_reamin_time;

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
                        btn_from_address.setText("Where to ?");

                    }else{
                        startActivity(new Intent(Home.this,Vehicle_reg.class));
                        btn_from_address.setText("Show your Vehicle");

                    }

                }
            });


            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);

            if (App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase("null")
                        || App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase(null)
                        || App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase(""))
                {
                    navigationView.inflateMenu(R.menu.activity_home_drawer);
                }else{
                navigationView.inflateMenu(R.menu.activity_driver_menu);
                mSocket.connect();
                }


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
            headerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Home.this,Acc_edit.class));
                }
            });
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

            btn_driver_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (btn_driver_status.getText().toString().equalsIgnoreCase("Active"))
                    {
                        btn_driver_status.setText("Deactive");
                        btn_driver_status.setChecked(false);
                        btn_driver_status.setBackground(getResources().getDrawable(R.drawable.border_line_grey));
                    }
                    else
                        {
                            btn_driver_status.setText("Active");
                            btn_driver_status.setChecked(true);
                            btn_driver_status.setBackground(getResources().getDrawable(R.drawable.border_line_yellow));
                        }
                    Save_Token_on_firebase(btn_driver_status.getText().toString().trim());
                    App_Conteroller.sharedpreferences = getSharedPreferences(App_Conteroller.MyPREFERENCES, Context.MODE_PRIVATE);
                    App_Conteroller.editor = App_Conteroller.sharedpreferences.edit();
                    App_Conteroller. editor.putString(SP_Utils.LOGIN_DRIVER_STATUS,""+btn_driver_status.getText().toString().trim());
                    App_Conteroller. editor.commit();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);}
    }



    public void Init()
    {
        txt_reamin_time=findViewById(R.id.txt_remain_time);
        btn_from_address=findViewById(R.id.set_loaction);
        btn_driver_status=findViewById(R.id.set_driver_status);
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

        try{
            GetVersionCode versionCode=new GetVersionCode();
            versionCode.execute();
           }catch (Exception e){e.printStackTrace();}

        if (App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_STATUS,"").equalsIgnoreCase("Active"))
        {
            btn_driver_status.setText("Active");
            btn_driver_status.setChecked(true);
            btn_driver_status.setBackground(getResources().getDrawable(R.drawable.border_line_yellow));

        }else{
            btn_driver_status.setText("Deactive");
            btn_driver_status.setChecked(false);
            btn_driver_status.setBackground(getResources().getDrawable(R.drawable.border_line_grey));
        }

        Check_booking();


    }

    private void Check_booking() {

                if (App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_CONTACT_NUMBER,"").equalsIgnoreCase("")
                        || App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_EMAIL,"").equalsIgnoreCase(""))
                {
                    Toast.makeText(Home.this, "Please Complite your profile", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Home.this,Acc_edit.class));
                }else if (App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase("null")
                        || App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase(null)
                        || App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase(""))
                {
                    String status=App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_USR_BOOKING_STATUS,"").trim();
                    if (!(status.equalsIgnoreCase("yes")))
                    {
                        startActivity(new Intent(Home.this,From_Location.class));
                    }
                }else{
                    get_loaction.setVisibility(View.VISIBLE);
                    get_driver_vehicle_Api();

                }

    }

    private void creatrdialogbox_for_logout()
    {
        final Dialog dialog = App_Utils.createDialog(Home.this, false);
        dialog.setCancelable(false);
       /* TextView title = (TextView) dialog.findViewById(R.id.txt_DialogHeadingTitle);
        title.setText("Driver Profile");*/
        TextView txt_DialogTitle = (TextView) dialog.findViewById(R.id.txt_DialogTitle);
        txt_DialogTitle.setText("Are you sure you want to logout ?");
        TextView txt_submit = (TextView) dialog.findViewById(R.id.txt_submit);
        txt_submit.setText("Yes");
        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    App_Conteroller.sharedpreferences = getSharedPreferences(App_Conteroller.MyPREFERENCES, Context.MODE_PRIVATE);
                    App_Conteroller.editor = App_Conteroller.sharedpreferences.edit();
                    App_Conteroller.editor.clear();
                    App_Conteroller.editor.commit();
                    startActivity(new Intent(Home.this,login_mobile.class).
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        TextView txt_cancel = (TextView) dialog.findViewById(R.id.txt_cancel);
        txt_cancel.setText("No");
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }


    private void get_driver_vehicle_Api() {

        try{
            HashMap map= new HashMap<>();
            map.put("driver_id",App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,""));
            if (isNetworkAvailable(Home.this))
            {
                Call<Response_Driver_vehicle> call= APIClient.getWebServiceMethod().get_Driver_Vehicle(map);
                call.enqueue(new Callback<Response_Driver_vehicle>() {
                    @Override
                    public void onResponse(Call<Response_Driver_vehicle> call, Response<Response_Driver_vehicle> response) {

                        try{
                            String status=response.body().getApi_status();
                            String msg=response.body().getApi_message();
                            if (status.equalsIgnoreCase("1") && msg.equalsIgnoreCase("success") )
                            {
                                List< Response_Driver_vehicle.Vehicle_Info> getlist=new ArrayList<>();
                                getlist=response.body().data;

                                App_Conteroller.sharedpreferences = getSharedPreferences(App_Conteroller.MyPREFERENCES, Context.MODE_PRIVATE);
                                App_Conteroller.editor = App_Conteroller.sharedpreferences.edit();
                                App_Conteroller. editor.putString(SP_Utils.DRIVER_VEHICLE_TYPE_ID,""+getlist.get(0).getVehicle_type_id());
                                App_Conteroller. editor.putString(SP_Utils.DRIVER_VEHICLE_NO,""+getlist.get(0).getVehicle_number());
                                App_Conteroller. editor.putString(SP_Utils.DRIVER_VEHICLE_PIC_RC,""+getlist.get(0).getVehicle_rc());
                                App_Conteroller. editor.putString(SP_Utils.DRIVER_VEHICLE_PIC_DOC,""+getlist.get(0).getVehicle_other_doc());
                                App_Conteroller. editor.putString(SP_Utils.DRIVER_VEHICLE_PIC_INSURENCE,""+getlist.get(0).getVehicle_insurance_id());
                                App_Conteroller. editor.putString(SP_Utils.DRIVER_VEHICLE_PIC_PERMIT,""+getlist.get(0).getPermit());
                                App_Conteroller. editor.putString(SP_Utils.DRIVER_VEHICLE_PIC,""+getlist.get(0).getVehicle_photo());
                                App_Conteroller. editor.putString(SP_Utils.DRIVER_VEHICLE_STATUS,""+btn_driver_status.getText().toString().trim());
                                App_Conteroller. editor.commit();

                            }
                        }catch (Exception e){
                            e.printStackTrace();}

                    }

                    @Override
                    public void onFailure(Call<Response_Driver_vehicle> call, Throwable t) {

                    }
                });
            }

        }catch (Exception e){
            e.printStackTrace();}

    }

    private class GetVersionCode extends AsyncTask<Void, String, String> {

        @Override

        protected String doInBackground(Void... voids) {

            String newVersion = null;

            try {
                Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID  + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();
                if (document != null) {
                    Elements element = document.getElementsContainingOwnText("Current Version");
                    for (Element ele : element) {
                        if (ele.siblingElements() != null) {
                            Elements sibElemets = ele.siblingElements();
                            for (Element sibElemet : sibElemets) {
                                newVersion = sibElemet.text();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newVersion;

        }

        @Override
        protected void onPostExecute(String onlineVersion) {

            super.onPostExecute(onlineVersion);

            if (onlineVersion != null && !onlineVersion.isEmpty()) {

                if (!String.valueOf(BuildConfig.VERSION_NAME).equalsIgnoreCase(String.valueOf(onlineVersion))) {

                    final Dialog dialog = App_Utils.createDialog(Home.this, true);
                    dialog.setCancelable(false);
                    TextView txt_DialogTitle = (TextView) dialog.findViewById(R.id.txt_DialogTitle);
                    txt_DialogTitle.setText("Please update this app on playstore");
                    TextView txt_submit = (TextView) dialog.findViewById(R.id.txt_submit);
                    txt_submit.setText("UPDATE NOW");
                    txt_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)));
                        }
                    });
                    dialog.show();

                }

            }
        }

    }

    private void creatrdialogbox_for_exit()
    {
        final Dialog dialog = App_Utils.createDialog(Home.this, false);
        dialog.setCancelable(false);
       /* TextView title = (TextView) dialog.findViewById(R.id.txt_DialogHeadingTitle);
        title.setText("Driver Profile");*/
        TextView txt_DialogTitle = (TextView) dialog.findViewById(R.id.txt_DialogTitle);
        txt_DialogTitle.setText("Are you sure you want to Exit ?");

        TextView txt_submit = (TextView) dialog.findViewById(R.id.txt_submit);
        txt_submit.setText("Yes");
        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView txt_cancel = (TextView) dialog.findViewById(R.id.txt_cancel);
        txt_cancel.setText("NO");
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void ShowBottomSheet(final List<Response_Booking_List.User_List> list, final String bookid, final String driverid) {
        layout_user_profile_list.setVisibility(View.VISIBLE);
        user_list_recycle=findViewById(R.id.user_list_view);
        StaggeredGridLayoutManager staggeredGridLayoutManager2 = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        user_list_recycle.setLayoutManager(staggeredGridLayoutManager2); // set LayoutManager to RecyclerView
        Adapter_user_list aa=new Adapter_user_list(Home.this,list,Home.this);

        btn_finish_ride_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = App_Utils.createDialog(Home.this, false);
                dialog.setCancelable(false);

                TextView txt_DialogTitle = (TextView) dialog.findViewById(R.id.txt_DialogTitle);
                txt_DialogTitle.setText("Are you sure customer give cash on delivery");
                TextView txt_submit = (TextView) dialog.findViewById(R.id.txt_submit);
                txt_submit.setText("Yes");
                txt_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        try{




                            payment_history_map.clear();
                            double price= (Double.parseDouble(list.get(0).getAmount()));
                            payment_history_map.put("ride_amount",""+price);
                            double amount_driver=0.7*price;
                            double amount_comp=0.3*price;
                            Log.i(TAG,"Price dirver :"+amount_driver);
                            Log.i(TAG,"Price Comp :"+amount_comp);


                            DecimalFormat df2=new DecimalFormat("#.##");

                            price= Double.parseDouble(df2.format(price));
                            amount_driver= Double.parseDouble(df2.format(amount_driver));
                            amount_comp= Double.parseDouble(df2.format(amount_comp));

                            get_Selected_Driver_Id=App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"");
                            payment_history_map.put("driver_earning",""+amount_driver);
                            payment_history_map.put("comp_commission",""+amount_comp);
                            //add_payment_gatway(list);
                            Change_ride_status(list.get(0).getUser_id(),list.get(0).getId(),list.get(0).getVehicle_id(),"COD");
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("Driver_ID").child(driverid).removeValue();

                            final HashMap<String,String> hashMap=new HashMap<>();
                            hashMap.put("driver_id",""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,""));
                            hashMap.put("amt_type","COD");
                            hashMap.put("amount",""+amount_driver);
                            hashMap.put("remark","Added to wallet");
                            hashMap.put("timestamp",""+App_Utils.getCurrentdate());
                            hashMap.put("status","success");
                            wallet_save(hashMap);

                            progressDialog=new ProgressDialog(Home.this);
                            progressDialog.setCancelable(false);
                            progressDialog.setMessage("Loading...");
                            progressDialog.show();
                            get_Booking_List.clear();

                            Change_ride_status(list.get(0).getUser_id(),get_BookID_Status,get_vehicle_id_status,"COD");

                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("Driver_ID").child(driverid).removeValue();

                            try {
                                Toast.makeText(Home.this, "Payment Successful: ", Toast.LENGTH_SHORT).show();
                                btn_finish_ride_user.setVisibility(View.GONE);
                                btn_finish_ride_driver.setVisibility(View.GONE);
                            } catch (Exception e) {
                                Log.e(TAG, "Exception in onPaymentSuccess", e);
                            }


                            mMap.clear();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                TextView txt_cancel = (TextView) dialog.findViewById(R.id.txt_cancel);
                txt_cancel.setText("No");
                txt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();


            }
        });
        user_list_recycle.setAdapter(aa);

    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        Log.e(TAG, "Home ||  in onPaymentSuccess------- "+razorpayPaymentID);

        progressDialog=new ProgressDialog(Home.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        get_Booking_List.clear();
        String Userid=App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_ID,"");
        Change_ride_status(Userid,get_BookID_Status,get_vehicle_id_status,razorpayPaymentID);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Driver_ID").child(get_Selected_Driver_Id).removeValue();

        try {
            Toast.makeText(Home.this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
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
        Toast.makeText(Home.this, "Error: "+response, Toast.LENGTH_SHORT).show();
        Log.e(TAG, "Home ||  in onPaymentError------- "+response);

    }

    private void Change_ride_status(final String User_id,final String bookID, final String vehicleid, final String get_payment_id) {

        payment_history_map.put("user_id",""+User_id);
        payment_history_map.put("ride_id",""+bookID);
        if (get_payment_id.equalsIgnoreCase("COD"))
        {
            payment_history_map.put("payment_method","COD");
        }else
            {
                payment_history_map.put("payment_method","Online");
            }

        payment_history_map.put("transcation_id",""+get_payment_id);
        payment_history_map.put("payment_status","Complite");
        payment_history_map.put("status","done");
        payment_history_map.put("remark","no");
        Save_payment_user(payment_history_map);

        if (isNetworkAvailable(Home.this))
        {
          final  HashMap<String,String> map=new HashMap<>();
            map.put("id",bookID);
            map.put("status","Deactive");
            map.put("payment_id",""+get_payment_id);
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
                            mMap.clear();
                            btn_finish_ride_user.setVisibility(View.GONE);
                            btn_finish_ride_driver.setVisibility(View.GONE);
                            if (App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase("null")
                                    || App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase(null)
                                    || App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase(""))
                            {
                                btn_from_address.setText("Where to ?");
                                btn_from_address.setVisibility(View.VISIBLE);
                            }else{
                                btn_from_address.setText("Show your Vehicle");
                                btn_from_address.setVisibility(View.VISIBLE);
                            }

                            Show_driver_rating_box();



                            get_loaction.setClickable(true);
                        }else{

                            Toast.makeText(Home.this, "status vehicle "+status+"\n"+" msg vehicle "+msg, Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e){
                        Log.i(TAG,"Home || Change_ride_status "+e.toString());
                        Log.i(TAG,"Home || Change_ride_status || response "+response);
                        Log.i(TAG,"Home || Change_ride_status || send data "+map);
                        Toast.makeText(Home.this, "Server error Change_ride_status", Toast.LENGTH_SHORT).show();
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
            final  Map<String,String> map=new HashMap<>();
            map.put("id",vehicleid);
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
                            Toast.makeText(Home.this, "status change", Toast.LENGTH_SHORT).show();
                            String id=response.body().getId();

                        }else{

                            Toast.makeText(Home.this, "status vehicle "+status+"\n"+" msg vehicle "+msg, Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e){

                        Log.i(TAG,"Home || Change_vehicle_status || response "+response);
                        Log.i(TAG,"Home || Change_vehicle_status || send data "+map);
                        Toast.makeText(Home.this, "Server error Change_vehicle_status", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();}

                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    try {
                        mDatabase.child("Booking_ID").child(getbook_id).removeValue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            //      Save_data_on_firebase(mDatabase);

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

    private void Save_Token_on_firebase(String status) {
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (refreshedToken.equals(null) || refreshedToken=="")
        {
            Save_Token_on_firebase(status);
        }else{
            String id=App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"");
            mDatabase = FirebaseDatabase.getInstance().getReference();
            Map<String,String> map=new HashMap<>();
            map.put("token_id",refreshedToken);
            map.put("driver_id",id);
            map.put("driver_status",status);

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

    private void send_driver_current_latlng_on_firebase(String lat,String lng) {
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (refreshedToken.equals(null) || refreshedToken=="")
        {
            send_driver_current_latlng_on_firebase(lat,lng);
        }else{
            String id=App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"");
            String vehicle_type_id=App_Conteroller.sharedpreferences.getString(SP_Utils.DRIVER_VEHICLE_TYPE_ID,"");
            String vehicle_no=App_Conteroller.sharedpreferences.getString(SP_Utils.DRIVER_VEHICLE_NO,"");
            mDatabase = FirebaseDatabase.getInstance().getReference();
            Map<String,String> map=new HashMap<>();
            map.put("token_id",refreshedToken);
            map.put("driver_id",id);
            map.put("driver_lat",lat);
            map.put("driver_lng",lng);
            map.put("driver_vehicle_no",vehicle_no);
            map.put("driver_vehicle_type",""+vehicle_type_id);
            map.put("driver_status",""+btn_driver_status.getText().toString().trim());
            mDatabase.child("Driver_Current_latlng_ID").child(id).setValue(map);

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

    private double distance(double lat1, double lng1, double lat2, double lng2)
    {

        double earthRadius = 6371;  // in miles, change to 6371 for kilometer output
                                    // in km, change to 3958.75 for miles output
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double dist = earthRadius * c;

        return dist; // output distance, in km
    }

    private void Check_driver_booking(final Location location) {
        if (App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_STATUS,"").equalsIgnoreCase("Active"))
        {
            Save_Token_on_firebase("Active");

        }else{
            Save_Token_on_firebase("Deactive");
        }

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
                    mMap.clear();
                    if (message.getStatus().toString().equalsIgnoreCase("Active") || message.getStatus().toString().equalsIgnoreCase("Running"))
                     {
                        String Driver_ID=App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"");
                        String get_Driver_ID=message.getDriver_id();
                        if (get_Driver_ID.equalsIgnoreCase(Driver_ID) && (Accept_this_booking==0) )
                        {
                                 Accept_this_booking=11;

                                if (message.getStatus().toString().equalsIgnoreCase("Running"))
                                {
                                    Set_running_value(message.getBook_id(), location,message.getVehicle_image(),message.getVehicle_type_id(),
                                            message.getVehicle_no(),message.getAmount(),message.getUser_contact(),message.getUser_image(),message.getUser_name());
                                }else{

                                    Show_dialog_box(message.getUser_id(), message.getBook_id(), location,message.getVehicle_image(),message.getVehicle_type_id(),
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


                    }else{

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
  //      Toast.makeText(Home.this, "lat------ "+lat, Toast.LENGTH_SHORT).show();
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
        double oldlat = 0.0, oldlong = 0.0;
        double newlat = 0.0, newlong = 0.0;
        newlat = lat;
        newlong = lng;
        Location prevLoc = new Location("service Provider");
        prevLoc.setLatitude(oldlat);
        prevLoc.setLongitude(oldlong);
        Location newLoc = new Location("service Provider");
        newLoc.setLatitude(newlat);
        newLoc.setLongitude(newlong);
        float bearing = prevLoc.bearingTo(newLoc);
        MarkerOptions marker3 = null;

            marker3 = new MarkerOptions().position(new LatLng(lat, lng));
            if (veh_type_id.toString().equalsIgnoreCase("8"))
            {
                marker3.icon(BitmapDescriptorFactory.fromResource(R.drawable.auto_icon));
            }else if (veh_type_id.toString().equalsIgnoreCase("7"))
            {
                marker3.icon(BitmapDescriptorFactory.fromResource(R.drawable.bike));
            }else if (veh_type_id.toString().equalsIgnoreCase("6"))
            {
                marker3.icon(BitmapDescriptorFactory.fromResource(R.drawable.ok_car_icon));
            }else if (veh_type_id.toString().equalsIgnoreCase("5"))
            {
                marker3.icon(BitmapDescriptorFactory.fromResource(R.drawable.e_rickshaw));
            }else if (veh_type_id.toString().equalsIgnoreCase("4"))
            {
                marker3.icon(BitmapDescriptorFactory.fromResource(R.drawable.scooty));
            }

        if (update_markerc == 0) {
            marker3 = new MarkerOptions().position(new LatLng(newlat, newlong));
            marker3.anchor(0.5f, 0.5f);
            marker3.rotation(bearing);
            marker3.flat(true);
            mMap.addCircle(new CircleOptions()
                    .center(new LatLng(lat,lng))
                    .radius(500)
                    .strokeColor(Color.YELLOW)
                    .fillColor(Color.TRANSPARENT));
            mMap.addMarker(marker3);
            update_markerc = 1;
        } else {

            marker3.anchor(0.5f, 0.5f);
            marker3.rotation(bearing);
            marker3.flat(true);
            mMap.addCircle(new CircleOptions()
                    .center(new LatLng(lat,lng))
                    .radius(500)
                    .strokeColor(Color.YELLOW)
                    .fillColor(Color.TRANSPARENT));
            mMap.addMarker(marker3);
        }
        oldlong = newlong;


    }

    // Marker mk = null;
    public Emitter.Listener onNewMessage = new Emitter.Listener()
    {
        @Override
        public void call(final Object... args)
        {
            JSONObject data = (JSONObject)args[0];
            try
            {
                for (int i = 0; i < data.length(); i++)
                {
                    Log.i(TAG,"Sokit value position: "+i+"     value: "+data);
                    Log.i(TAG,"-----------------------------------------------------------");

                            /*if (jd.getString("event_id").equalsIgnoreCase(value_event_id.toString()))
                            {
                                sokti_on_lat = jd.getString("latitude");
                                sokti_on_long = jd.getString("longitude");
                                sokit_driver_details_id = jd.getString("driver_details_id");
                                sokit_driver_event_id = jd.getString("driver_event_id");
                                sokit_event_id = jd.getString("event_id");
                                sokit_id = jd.getString("id");
                                sokit_status = jd.getString("status");
                                sokit_tracking = jd.getString("tracking");
                                sokit_vehicle_detail_id = jd.getString("vehicle_detail_id");
                            }*/
                }
            }
            catch (Exception e)
            {
                return;
            }
                  /* try
                    {

                             MarkerOptions marker2 = null;


                            sokit_lan = Double.valueOf(sokti_on_lat);
                            sokit_long = Double.valueOf(sokti_on_long);


                            newlat = sokit_lan;
                            newlong = sokit_long;
                            Location prevLoc = new Location("service Provider");
                            prevLoc.setLatitude(oldlat);
                            prevLoc.setLongitude(oldlong);
                            Location newLoc = new Location("service Provider");
                            newLoc.setLatitude(newlat);
                            newLoc.setLongitude(newlong);
                            float bearing = prevLoc.bearingTo(newLoc);

                            if (update_marker == 0)
                            {
                                marker2 = new MarkerOptions().position(new LatLng(newlat, newlong));
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
                            oldlong = newlong;
                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
                            {
                                @Override
                                public boolean onMarkerClick(Marker marker)
                                {
                                    //                             Toast.makeText(Home.this, "111", Toast.LENGTH_SHORT).show();
                                    //                         //    driver_details.setVisibility(View.VISIBLE);
                                    //    book_now_event2.setVisibility(View.GONE);
                                    return true;
                                }
                            });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
        }
    };



    private void Call_driver_book_Api(final String driver_ID, final String contact, final String img, final String name, final String bookid) {
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        get_Booking_List.clear();
        final HashMap map= new HashMap<>();
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

                            ShowBottomSheet(get_list,bookid,driver_ID);
                            get_Booking_List=get_list;

                        }else{

                            Toast.makeText(Home.this, "status "+status+"\n"+"msg "+msg, Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        Log.i(TAG,"Home || Call_driver_book_Api "+e.toString());
                        Log.i(TAG,"Home || Call_driver_book_Api || response "+response);
                        Log.i(TAG,"Home || Call_driver_book_Api || send data "+map);
                      Toast.makeText(Home.this, "Server error Call_driver_book_Api", Toast.LENGTH_SHORT).show();
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

    private void Show_dialog_box(final String user_id,final String book_id, final Location location, final String veh_img, final String veh_type_id, final String veh_no, final String amount, final String contact, final String img, final String name) {
        try{
            AlertDialog.Builder dialog=new AlertDialog.Builder(Home.this);
       //     dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme_down_up;
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

                    lat=location.getLatitude();
                    lng=location.getLongitude();
                    double speed=location.getSpeed();
                    Toast.makeText(Home.this, "lat------ "+lat, Toast.LENGTH_SHORT).show();
                    String driver_id=App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"");
                    String driver_name=App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_NAME,"");
                    HashMap<String,String> mapw=new HashMap<>();
                    mapw.put("driver_ID",""+driver_id);
                    mapw.put("name", ""+driver_name);
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

                    DecimalFormat df2=new DecimalFormat("#.##");

                    String dist_value=df2.format((distance(current_lat,current_lng,Double.valueOf(lat),Double.valueOf(lng))));

                    HashMap<String,String> map= new HashMap<>();
                    map.put("user_id",user_id);

                    map.put("timestamp",App_Utils.getCurrentdate());
                    map.put("sent_by",veh_type_id);

                    map.put("notification_text",driver_name+" accept your request");
                    send_noticication_user(map);

                    map.remove("user_id");
                    map.put("driver_id",driver_id);
                    map.put("notification_text","NBER "+driver_name+" booked a ride for approx "+dist_value+"km to accept check and go for ride");
                    send_noticication_driver(map);

                    Call_firebase_service(mapw);

                    MarkerOptions marker3 = null;

                        marker3 = new MarkerOptions().position(new LatLng(lat, lng));
                        if (veh_type_id.toString().equalsIgnoreCase("8"))
                        {
                            marker3.icon(BitmapDescriptorFactory.fromResource(R.drawable.auto_icon));
                        }else if (veh_type_id.toString().equalsIgnoreCase("7"))
                        {
                            marker3.icon(BitmapDescriptorFactory.fromResource(R.drawable.bike));
                        }else if (veh_type_id.toString().equalsIgnoreCase("6"))
                        {
                            marker3.icon(BitmapDescriptorFactory.fromResource(R.drawable.ok_car_icon));
                        }else if (veh_type_id.toString().equalsIgnoreCase("5"))
                        {
                            marker3.icon(BitmapDescriptorFactory.fromResource(R.drawable.e_rickshaw));
                        }else if (veh_type_id.toString().equalsIgnoreCase("4"))
                        {
                            marker3.icon(BitmapDescriptorFactory.fromResource(R.drawable.scooty));
                        }
                        mMap.addMarker(marker3);
                        update_marker2 = 1;
                        mMap.addCircle(new CircleOptions()
                                .center(new LatLng(lat,lng))
                                .radius(500)
                                .strokeColor(Color.YELLOW)
                                .fillColor(Color.TRANSPARENT));


                }
            });
            dialog.show();
        }catch (Exception e){
            Log.i(TAG,"Exception : || Home || Show_dialog_box "+e.toString());
            e.printStackTrace();}
    }


    private void send_noticication_user(final Map map) {

        if (isNetworkAvailable(Home.this))
        {
            Call<Response_Driver_vehicle> call= APIClient.getWebServiceMethod().notification_send_user(map);
            call.enqueue(new Callback<Response_Driver_vehicle>() {
                @Override
                public void onResponse(Call<Response_Driver_vehicle> call, Response<Response_Driver_vehicle> response) {
                    try{
                        String status=response.body().getApi_status();
                        String msg=response.body().getApi_message();

                        Toast.makeText(Home.this, "status uu "+status+"\n"+"msg "+msg, Toast.LENGTH_SHORT).show();

                    }catch (Exception e){

                        Log.i(TAG,"Home || send_noticication_user || response "+response);
                        Log.i(TAG,"Home || send_noticication_user || send data "+map);
    //                    Toast.makeText(Home.this, "Server error send_noticication_user", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();}
                }

                @Override
                public void onFailure(Call<Response_Driver_vehicle> call, Throwable t) {
                    Toast.makeText(Home.this, "Error : "+t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(Home.this, "No Internet", Toast.LENGTH_SHORT).show();
        }

    }


    private void Save_payment_user(final HashMap map) {

        if (isNetworkAvailable(Home.this))
        {
            Call<Response_Driver_vehicle> call= APIClient.getWebServiceMethod().payment_save(map);
            call.enqueue(new Callback<Response_Driver_vehicle>() {
                @Override
                public void onResponse(Call<Response_Driver_vehicle> call, Response<Response_Driver_vehicle> response) {
                    try{
                        String status=response.body().getApi_status().toString();
                        String msg=response.body().getApi_message().toString();

                        Toast.makeText(Home.this, "status uu "+status+"\n"+"msg "+msg, Toast.LENGTH_SHORT).show();

                    }catch (Exception e){
                        Log.i(TAG,"Home || Save_payment_user || response "+response);
                        Log.i(TAG,"Home || Save_payment_user || send data "+map);
                       Toast.makeText(Home.this, "Server error Save_payment_user", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();}


                }

                @Override
                public void onFailure(Call<Response_Driver_vehicle> call, Throwable t) {
                    Toast.makeText(Home.this, "Error : "+t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(Home.this, "No Internet", Toast.LENGTH_SHORT).show();
        }

    }


    private void send_noticication_driver(final HashMap map) {

        if (isNetworkAvailable(Home.this))
        {
            Call<Response_Driver_vehicle> call= APIClient.getWebServiceMethod().notification_send_driver(map);
            call.enqueue(new Callback<Response_Driver_vehicle>() {
                @Override
                public void onResponse(Call<Response_Driver_vehicle> call, Response<Response_Driver_vehicle> response) {
                    try{
                        String status=response.body().getApi_status().toString();
                        String msg=response.body().getApi_message().toString();
                        Toast.makeText(Home.this, "status dd "+status+"\n"+"msg "+msg, Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Log.i(TAG,"Home || send_noticication_driver || response "+response);
                        Log.i(TAG,"Home || send_noticication_driver || send data "+map);
       //                 Toast.makeText(Home.this, "error driver send_noticication_driver", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();}
                }

                @Override
                public void onFailure(Call<Response_Driver_vehicle> call, Throwable t) {
                    Toast.makeText(Home.this, "Error : "+t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(Home.this, "No Internet", Toast.LENGTH_SHORT).show();
        }

    }

    private void Check_User_Id_on_firebase() {
        get_Booking_list.clear();
        final ProgressDialog dialog=new ProgressDialog(Home.this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");
        dialog.show();
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
                            get_loaction.setVisibility(View.GONE);
                            get_loaction.setClickable(false);

                            btn_driver_status.setClickable(false);
                            App_Conteroller.sharedpreferences = getSharedPreferences(App_Conteroller.MyPREFERENCES, Context.MODE_PRIVATE);
                            App_Conteroller.editor = App_Conteroller.sharedpreferences.edit();
                            App_Conteroller. editor.putString(SP_Utils.LOGIN_USR_BOOKING_STATUS,"yes");
                            App_Conteroller. editor.commit();

                        }else{

                            get_loaction.setVisibility(View.VISIBLE);
                            layout_user_info.setVisibility(View.GONE);
                            get_loaction.setClickable(true);
                            layout_user_info.setVisibility(View.GONE);
                            get_loaction.setVisibility(View.VISIBLE);

                            btn_driver_status.setClickable(true);
                            App_Conteroller.sharedpreferences = getSharedPreferences(App_Conteroller.MyPREFERENCES, Context.MODE_PRIVATE);
                            App_Conteroller.editor = App_Conteroller.sharedpreferences.edit();
                            App_Conteroller. editor.putString(SP_Utils.LOGIN_USR_BOOKING_STATUS,"no");
                            App_Conteroller. editor.commit();
                        }
                        dialog.dismiss();
                    }
                }catch (Exception e){e.printStackTrace();}
                dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.dismiss();
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
                MarkerOptions marker2 = null;
                try{
                    double get_lat= Double.valueOf(lat);
                    double get_lng=Double.valueOf(lng);
                    Location prevLoc = new Location("service Provider");
                    prevLoc.setLatitude(oldlat);
                    prevLoc.setLongitude(oldlong);
                    Location newLoc = new Location("service Provider");
                    newLoc.setLatitude(get_lat);
                    newLoc.setLongitude(get_lng);
                    float bearing = prevLoc.bearingTo(newLoc);

                        marker2 = new MarkerOptions().position(new LatLng(get_lat, get_lng));
                        if (veh_type_id.toString().equalsIgnoreCase("8")) {
                            marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.auto_icon));
                        } else if (veh_type_id.toString().equalsIgnoreCase("7")) {
                            marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.bike));
                        } else if (veh_type_id.toString().equalsIgnoreCase("6")) {
                            marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.ok_car_icon));
                        } else if (veh_type_id.toString().equalsIgnoreCase("5")) {
                            marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.e_rickshaw));
                        } else if (veh_type_id.toString().equalsIgnoreCase("4")) {
                            marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.scooty));
                        }

                        mMap.addMarker(marker2);


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
                    .zoom(2.0f)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(90)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } catch (Exception e) {
            e.printStackTrace();
        }
        btn_finish_ride_user.setVisibility(View.VISIBLE);
        btn_finish_ride_driver.setVisibility(View.VISIBLE);
      //  double dist_value=distance(current_lat,current_lng,to_lat,to_lng);

        /*Log.i(TAG,"Home || addstart_end_icontrip || dist_value in km :"+dist_value);
        if (dist_value<5){
            btn_finish_ride_user.setVisibility(View.VISIBLE);
            btn_finish_ride_driver.setVisibility(View.VISIBLE);
        }else{
            btn_finish_ride_user.setVisibility(View.GONE);
            btn_finish_ride_driver.setVisibility(View.GONE);
        }*/

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

            if (App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase("null")
                    || App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase(null)
                    || App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase(""))
            {
                btn_from_address.setText("Where to ?");
            }else{
                btn_from_address.setText("Show your Vehicle");
            }

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
        update_marker = 0;
        Check_User_Id_on_firebase();
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
        handler.postDelayed(runnable, 1000);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            creatrdialogbox_for_exit();
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
                lat=location.getLatitude();
                lng=location.getLongitude();
                if (setCurrentLocation){

                                      // Add a marker in Sydney and move the camera
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
                        btn_driver_status.setVisibility(View.GONE);
                    }else{

                        btn_driver_status.setVisibility(View.VISIBLE);
                        Check_driver_booking(location);
                        send_driver_current_latlng_on_firebase(String.valueOf(lat),String.valueOf(lng));
                        start_soket(lat,lng);
                    }
                }catch (Exception e){e.printStackTrace();}

            }catch (Exception e){e.printStackTrace();}
            }
        });
    }

    private void start_soket(double lat, double lng) {
        mSocket.connect();
        JSONObject obj_get = new JSONObject();
        try {
            obj_get.putOpt("driverid", App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,""));
            obj_get.putOpt("latitude", String.valueOf(lat));
            obj_get.putOpt("longitude", String.valueOf(lng));
            mSocket.emit("status added", obj_get);

           /* JSONObject obj_set = new JSONObject();
            obj_set.putOpt("driverid", App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,""));
          //  obj_set.putOpt("latitude", String.valueOf(lat));
          //  obj_set.putOpt("longitude", String.valueOf(lng));
        //    mSocket. emit("status geted", obj_set);
         //   mSocket.on("refresh feed",onNewMessage);
            mSocket.on("status geted",obj_set).on("refresh feed", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    final JSONObject data = (JSONObject)args[0];
//data is in JSOn format
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG,"getsokit data "+data);
                            //Toast.makeText(MainActivity.this, "Haha !! All rats are   killed !", Toast.LENGTH_SHORT).show();

                            //whatever your UI logic
                        }
                    });
                }
            });*/
            mSocket.connect();

            } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Save_data_on_firebase(DatabaseReference mDatabase) {
        // Read from the database
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


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
                try{
                    String id=dataSnapshot.getKey();
                    String contactno = String.valueOf(dataSnapshot.child("contact_number").getValue());
                    String driver_ID = String.valueOf(dataSnapshot.child("driver_ID").getValue());
                    String name = String.valueOf(dataSnapshot.child("name").getValue());
                    String photo = String.valueOf(dataSnapshot.child("photo").getValue());
                    String d_lat = String.valueOf(dataSnapshot.child("lat").getValue());
                    String d_lng = String.valueOf(dataSnapshot.child("lng").getValue());
                    String address = String.valueOf(dataSnapshot.child("address").getValue());
                    String city = String.valueOf(dataSnapshot.child("city").getValue());
                    String email = String.valueOf(dataSnapshot.child("email").getValue());
                    String veh_type_id = String.valueOf(message.getVehicle_type_id());
                    String veh_no =  String.valueOf(message.getVehicle_no());
                    String amount =  String.valueOf(message.getAmount());
                    String veh_img =  String.valueOf(message.getVehicle_image());
                    String vehicle_id=String.valueOf(message.getVehicle_id());
                    String book_id=String.valueOf(message.getBook_id());
                    get_Selected_Driver_Id=driver_ID;
                    if (!name.equalsIgnoreCase("null"))
                    {
                       String state = String.valueOf(dataSnapshot.child("state").getValue());
                       MarkerOptions marker2 = null;



                       show_driver_profile(message,contactno,driver_ID,name,photo,address,city,email,veh_type_id,veh_no,amount,veh_img,vehicle_id,book_id);

                       Toast.makeText(Home.this, "name "+name+"\n"+"get_driverid "+driver_ID, Toast.LENGTH_SHORT).show();
                        mMap.clear();
                        double get_lat= Double.valueOf(d_lat);
                        double get_lng=Double.valueOf(d_lng);

                        set_timer_cust(get_lat,get_lng);

                        Location prevLoc = new Location("service Provider");
                        prevLoc.setLatitude(oldlat);
                        prevLoc.setLongitude(oldlong);
                        Location newLoc = new Location("service Provider");
                        newLoc.setLatitude(get_lat);
                        newLoc.setLongitude(get_lng);
                        float bearing = prevLoc.bearingTo(newLoc);
                        marker2 = new MarkerOptions().position(new LatLng(get_lat, get_lng));

                            if (veh_type_id.toString().equalsIgnoreCase("8"))
                            {
                                marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.auto_icon));
                            }else if (veh_type_id.toString().equalsIgnoreCase("7"))
                            {
                                marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.bike));
                            }else if (veh_type_id.toString().equalsIgnoreCase("6"))
                            {
                                marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.ok_car_icon));
                            }else if (veh_type_id.toString().equalsIgnoreCase("5"))
                            {
                                marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.e_rickshaw));
                            }else if (veh_type_id.toString().equalsIgnoreCase("4"))
                            {
                                marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.scooty));
                            }

                            marker2.anchor(0.5f, 0.5f);
                            marker2.rotation(bearing);
                            marker2.flat(true);
                            mMap.addMarker(marker2);
                            update_marker = 1;


                        LatLng sydney = new LatLng(get_lat, get_lng);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(sydney)      // Sets the center of the map to Mountain View
                                .zoom(29)                   // Sets the zoom
                                .bearing(90)                // Sets the orientation of the camera to east
                                .tilt(90)                   // Sets the tilt of the camera to 30 degrees
                                .build();                   // Creates a CameraPosition from the builder
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        oldlat=get_lat;
                        oldlong=get_lng;
                    }else {
                        get_loaction.setVisibility(View.VISIBLE);
                        layout_user_info.setVisibility(View.GONE);
                        get_loaction.setVisibility(View.VISIBLE);
                        btn_from_address.setText("Pending booking");
                        get_loaction.setClickable(false);
                    }

                }catch (Exception e){e.printStackTrace();}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void set_timer_cust(double get_lat, double get_lng) {

        double get_driver_dist = distance(get_lat,get_lng,current_lat,current_lng);
        int get_time= (int) get_driver_dist;
        startTimer(get_time);

    }
    private void startTimer(int noOfMinutes) {
        CountDownTimer countDownTimer = new CountDownTimer(noOfMinutes, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                //Convert milliseconds into hour,minute and seconds
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                txt_reamin_time.setText(hms);//set text
            }
            public void onFinish() {
                txt_reamin_time.setText("TIME'S UP!!"); //On finish change timer text
            }
        }.start();

    }


    public void Show_driver_rating_box(){

        final BottomSheetDialog dialog = new BottomSheetDialog(Home.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_driver_rating, null);
        final AppCompatRatingBar ratingBar=v.findViewById(R.id.rating_bar);
        final EditText ed_review=v.findViewById(R.id.txt_review);
        TextView btn_submit=v.findViewById(R.id.txt_submit_btn);
        TextView btn_cancel=v.findViewById(R.id.txt_cancel_btn);
        dialog.setContentView(v);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_loaction.setVisibility(View.VISIBLE);
                layout_user_info.setVisibility(View.GONE);
                btn_from_address.setText("Where go ?");
                get_loaction.setClickable(true);
                dialog.dismiss();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_review.getText().toString().isEmpty())
                {
                    ed_review.setError("Enter review");
                    ed_review.requestFocus();
                }else{

                    progressDialog=new ProgressDialog(Home.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();

                    String rating_value= String.valueOf(ratingBar.getRating());
                    String remark="no";
                    Api_rating(get_Selected_Driver_Id,rating_value,ed_review.getText().toString(),remark);
                    dialog.dismiss();
                }

            }
        });

        dialog.show();
    }


    private Socket mSocket;
    {
        try
        {
            IO.Options opts = new IO.Options();
            opts.forceNew = true;
            mSocket = IO.socket("http://thenber.com:4006");
        }
        catch (URISyntaxException e)
        {

        }
    }

    private void Api_rating(String driver_id,String rating,String review,String remark) {
        if (isNetworkAvailable(Home.this))
        {
            final Map<String,String> map=new HashMap<>();
            map.put("driver_id",driver_id);
            map.put("user_id",App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_ID,""));
            map.put("rating",rating);
            map.put("review",review);
            map.put("remark",remark);
            map.put("status","Active");
            String date= App_Utils.getCurrentdate();
            map.put("timestamp",""+date);
            Call<Response_register> call= APIClient.getWebServiceMethod().give_ratiing(map);
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
                            Toast.makeText(Home.this, "Rating success", Toast.LENGTH_SHORT).show();
                            get_loaction.setVisibility(View.VISIBLE);
                            layout_user_info.setVisibility(View.GONE);
                            btn_from_address.setText("Where go ?");
                            get_loaction.setClickable(true);

                        }else{

                            Toast.makeText(Home.this, "status vehicle "+status+"\n"+" msg vehicle "+msg, Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e){
                        Log.i(TAG,"Home || Api_rating || response "+response);
                        Log.i(TAG,"Home || Api_rating || send data "+map);
                        Toast.makeText(Home.this, "error  Api_rating", Toast.LENGTH_SHORT).show();
                //        Toast.makeText(Home.this, "Server error", Toast.LENGTH_SHORT).show();
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

    private void show_driver_profile(final Response_Booking message, final String contactno, final String driver_id,
                                     String name, String photo, String address, String city, String email,
                                     String veh_type_id, String veh_no, String amount, String veh_img, String vehicle_id, final String book_id) {
        get_loaction.setVisibility(View.GONE);
        layout_user_info.setVisibility(View.VISIBLE);
        CircleImageView driver_image=findViewById(R.id.driver_img);
        CircleImageView driver_vehicle=findViewById(R.id.driver_veh);
        TextView driver_name=findViewById(R.id.txt_name);
        TextView driver_contect=findViewById(R.id.txt_mobile);
        TextView driver_veh_no=findViewById(R.id.txt_veh_no);
        TextView driver_amount=findViewById(R.id.txt_amount);
        get_Selected_Driver_Id=driver_id;
        payment_history_map.clear();
        DecimalFormat df2=new DecimalFormat("#.##");
        if (amount!=null)
        {
            driver_amount.setText(getResources().getString(R.string.rupee_sign)+" "+df2.format(Double.valueOf(amount)));
        }else{
            driver_amount.setText(getResources().getString(R.string.rupee_sign)+ " 0.00");
        }


        TextView btn_call=findViewById(R.id.driver_veh2);
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+contactno));
                startActivity(intent);
            }
        });

        driver_veh_no.setText("Vehicle no:"+ veh_no);
        driver_name.setText("Name:"+ name);
        driver_contect.setText("Mobile no. :"+ contactno);
        driver_contect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+contactno));
                startActivity(intent);
            }
        });

        driver_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+contactno));
                startActivity(intent);
            }
        });

        try{
            Picasso.with(Home.this).load(veh_img).error(R.drawable.ic_user).into(driver_vehicle);
        }catch (Exception e){e.printStackTrace();}

        try{
            Picasso.with(Home.this).load(photo).error(R.drawable.ic_user).into(driver_image);
        }catch (Exception e){e.printStackTrace();}

        btn_finish_ride_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*get_Selected_Driver_Id=driver_id;
                get_BookID_Status=message.getBook_id();
                get_vehicle_id_status=message.getVehicle_id();
                get_book_id_2=book_id;

                Change_ride_status(get_BookID_Status,get_vehicle_id_status,"kjhasdkjfsa");

                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("Driver_ID").removeValue();

                try {

                    btn_finish_ride_user.setVisibility(View.GONE);
                    btn_finish_ride_driver.setVisibility(View.GONE);
                } catch (Exception e) {
                    Log.e(TAG, "Exception in onPaymentSuccess", e);
                }*/


               add_payment_gatway_user(message);

            }
        });
    }

    private void add_payment_gatway_user(Response_Booking get_booking) {

        double price= (Double.parseDouble(get_booking.getAmount()));
        payment_history_map.put("ride_amount",""+price);
        double amount_driver=0.7*price;
        double amount_comp=0.3*price;
        Log.i(TAG,"Price dirver :"+amount_driver);
        Log.i(TAG,"Price Comp :"+amount_comp);

        payment_history_map.put("driver_earning",""+amount_driver);
        payment_history_map.put("comp_commission",""+amount_comp);
        get_Selected_Driver_Id=get_booking.getDriver_id();

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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

         if (id == R.id.nav_help) {
            startActivity(new Intent(Home.this,Trip_free.class));
        }else if (id == R.id.nav_notification) {
             startActivity(new Intent(Home.this,Notification.class));
        } else if (id == R.id.nav_setting) {
            startActivity(new Intent(Home.this,Acc_setting.class));
        } else if (id == R.id.nav_your_trips) {
            startActivity(new Intent(Home.this,Trip.class));
        }else if (id == R.id.nav_logout) {
             creatrdialogbox_for_logout();
        }else if (id ==R.id.nav_wallet)
         {
             startActivity(new Intent(Home.this,Wallet_page.class));
         }
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.yellow_logo));
        builder.setContentTitle("You have new booking");
        builder.setContentText("Accept this book ride");
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Will display the notification in the notification bar
        notificationManager.notify(1, builder.build());
    }


    @Override
    public void OnClick_item(Response_Booking_List.User_List user_list) {

    }

    @Override
    public void onLocationChanged(Location location) {
        current_lat=location.getLatitude();
        current_lng=location.getLongitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    private void wallet_save(final HashMap<String, String> hashMap) {

        if (isNetworkAvailable(Home.this))
        {
            Call<Resp_Common> call= APIClient.getWebServiceMethod().wallet_save(hashMap);
            call.enqueue(new Callback<Resp_Common>() {
                @Override
                public void onResponse(Call<Resp_Common> call, Response<Resp_Common> response) {
                    try{
                           String status = response.body().getApi_status().toString();
                           String msg = response.body().getApi_message().toString();
                           Toast.makeText(Home.this, "Wallet add", Toast.LENGTH_SHORT).show();
                         }catch (Exception e){
                           Log.i(TAG,"Home || wallet_save || response "+response);
                           Log.i(TAG,"Home || wallet_save || send data "+hashMap);
                           Toast.makeText(Home.this, "error wallet_save", Toast.LENGTH_SHORT).show();
                       e.printStackTrace();}
                }

                @Override
                public void onFailure(Call<Resp_Common> call, Throwable t) {
                    Toast.makeText(Home.this, "Error : "+t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(Home.this, "No Internet", Toast.LENGTH_SHORT).show();
        }

    }

}
