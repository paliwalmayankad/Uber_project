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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aaronsoftech.in.nber.Adapter.Adapter_user_list;
import aaronsoftech.in.nber.Adapter.CustomInfoWindowAdapter;
import aaronsoftech.in.nber.App_Conteroller;
import aaronsoftech.in.nber.BuildConfig;
import aaronsoftech.in.nber.Model.FB_Driver_res;
import aaronsoftech.in.nber.POJO.Customwindow_const;
import aaronsoftech.in.nber.POJO.Response_Booking;
import aaronsoftech.in.nber.POJO.Response_Booking_List;
import aaronsoftech.in.nber.POJO.Response_Driver_vehicle;
import aaronsoftech.in.nber.POJO.Response_register;
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
    int update_marker2=0;
    String[] locationPermissionsl = {"android.permission.ACCESS_COARSE_LOCATION","android.permission.ACCESS_FINE_LOCATION"};
    private static int REQUEST_CODE_LOCATIONl = 102;
    private DatabaseReference mDatabase;
    double lat= 0.0;
    double lng= 0.0;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Fabric.with(this, new Crashlytics());

        Init();
        Checkout.preload(getApplicationContext());

      /*  GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);*/

        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        try{
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            try{
                booked_id= String.valueOf(getIntent().getExtras().get("book_id"));
            }catch (Exception e){e.printStackTrace();}


            /*try{
                Toast.makeText(this, "User ID: "+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_ID,"")+"\n\n"+"Driver ID: "+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,""), Toast.LENGTH_SHORT).show();
            }catch (Exception e){e.printStackTrace();}*/

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
                        btn_driver_status.setSelected(false);
                        btn_driver_status.setBackground(getResources().getDrawable(R.drawable.border_line_grey));
                    }
                    else
                        {
                            btn_driver_status.setText("Active");
                            btn_driver_status.setSelected(true);
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
            btn_driver_status.setSelected(true);
            btn_driver_status.setBackground(getResources().getDrawable(R.drawable.border_line_yellow));

        }else{
            btn_driver_status.setText("Deactive");
            btn_driver_status.setSelected(false);
            btn_driver_status.setBackground(getResources().getDrawable(R.drawable.border_line_grey));
        }


            if (App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase("null")
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
                    //    mGoogleSignInClient.signOut();
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
       /* TextView title = (TextView) dialog.findViewById(R.id.txt_DialogHeadingTitle);
        title.setText("Driver Profile");*/
                TextView txt_DialogTitle = (TextView) dialog.findViewById(R.id.txt_DialogTitle);
                txt_DialogTitle.setText("Are you sure customer give cash on delivery");
                TextView txt_submit = (TextView) dialog.findViewById(R.id.txt_submit);
                txt_submit.setText("Yes");
                txt_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        try{
                            //add_payment_gatway(list);
                            Change_ride_status(list.get(0).getId(),list.get(0).getVehicle_id(),bookid);
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("Driver_ID").child(driverid).child("status").child("Deactive");
                            mDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            progressDialog=new ProgressDialog(Home.this);
                            progressDialog.setCancelable(false);
                            progressDialog.setMessage("Loading...");
                            progressDialog.show();
                            get_Booking_List.clear();

                            Change_ride_status(get_BookID_Status,get_vehicle_id_status,"COD");

                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("Driver_ID").child(driverid).removeValue();

                            try {
                                Toast.makeText(Home.this, "Payment Successful: ", Toast.LENGTH_SHORT).show();
                                btn_finish_ride_user.setVisibility(View.GONE);
                                btn_finish_ride_driver.setVisibility(View.GONE);
                            } catch (Exception e) {
                                Log.e(TAG, "Exception in onPaymentSuccess", e);
                            }
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

        Change_ride_status(get_BookID_Status,get_vehicle_id_status,razorpayPaymentID);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Driver_ID").removeValue();


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

    private void Change_ride_status(final String bookID, final String vehicleid, final String get_payment_id) {
        if (isNetworkAvailable(Home.this))
        {
            Map<String,String> map=new HashMap<>();
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
                            Show_driver_rating_box();

                            if (App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase("null")
                                    || App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase(null)
                                    || App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase(""))
                            {
                                btn_from_address.setText("Where to ?");
                            }else{
                                btn_from_address.setText("Show your Vehicle");
                            }
                            get_loaction.setClickable(true);
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

        return dist; // output distance, in KM
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
        mMap.clear();
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

    private void Call_driver_book_Api(final String driver_ID, final String contact, final String img, final String name, final String bookid) {
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

                            ShowBottomSheet(get_list,bookid,driver_ID);
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
                    mMap.clear();
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
        handler.postDelayed(runnable, 2000);
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
                btn_from_address.setText("Where to ?");

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

          //              Toast.makeText(Home.this, "name "+name+"\n"+"get_driverid "+driver_ID, Toast.LENGTH_SHORT).show();
                        mMap.clear();
                        double get_lat= Double.valueOf(d_lat);
                        double get_lng=Double.valueOf(d_lng);

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

    public void Show_driver_rating_box(){
        // Create custom dialog object
        final Dialog dialog = new Dialog(Home.this);
        // Include dialog.xml file
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_driver_rating, null);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        final AppCompatRatingBar ratingBar=v.findViewById(R.id.rating_bar);
        final EditText ed_review=v.findViewById(R.id.txt_review);
        TextView btn_submit=v.findViewById(R.id.txt_submit_btn);
        dialog.setContentView(v);

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
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void Api_rating(String driver_id,String rating,String review,String remark) {
        if (isNetworkAvailable(Home.this))
        {
            Map<String,String> map=new HashMap<>();
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
                        Log.i(TAG,"Home || Api_rating || "+response);
                        Toast.makeText(Home.this, "Server error", Toast.LENGTH_SHORT).show();
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

         if (id == R.id.nav_help) {
            startActivity(new Intent(Home.this,Trip_free.class));
        } else if (id == R.id.nav_setting) {
            startActivity(new Intent(Home.this,Acc_setting.class));
        } else if (id == R.id.nav_your_trips) {
            startActivity(new Intent(Home.this,Trip.class));
        }else if (id == R.id.nav_logout) {
             creatrdialogbox_for_logout();
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
}
