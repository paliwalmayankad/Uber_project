package aaronsoftech.in.nber.Activity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Location;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.philliphsu.bottomsheetpickers.BottomSheetPickerDialog;
import com.philliphsu.bottomsheetpickers.date.DatePickerDialog;
import com.philliphsu.bottomsheetpickers.time.BottomSheetTimePickerDialog;
import com.philliphsu.bottomsheetpickers.time.grid.GridTimePickerDialog;
import com.philliphsu.bottomsheetpickers.time.numberpad.NumberPadTimePickerDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import aaronsoftech.in.nber.Adapter.Adapter_Vehicle;
import aaronsoftech.in.nber.Adapter.Adapter_Vehicle_gallery;
import aaronsoftech.in.nber.App_Conteroller;
import aaronsoftech.in.nber.BeginJourneyEvent;
import aaronsoftech.in.nber.CurrentJourneyEvent;
import aaronsoftech.in.nber.Model.Near_Driver;
import aaronsoftech.in.nber.POJO.Response_All_Vehicle;
import aaronsoftech.in.nber.POJO.Response_Vehicle_type;
import aaronsoftech.in.nber.POJO.Response_register;
import aaronsoftech.in.nber.R;
import aaronsoftech.in.nber.Service.APIClient;
import aaronsoftech.in.nber.Utils.App_Utils;
import aaronsoftech.in.nber.Utils.SP_Utils;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static aaronsoftech.in.nber.Utils.App_Utils.isNetworkAvailable;
import static com.google.android.gms.maps.model.JointType.ROUND;

public class From_Location extends AppCompatActivity implements LocationListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,Adapter_Vehicle.Vehicle_Item_listner,
        BottomSheetTimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, NavigationView.OnNavigationItemSelectedListener {

    String TAG="From_Location";
    public   int index = -1;
    public   LatLng startPosition, endPosition;
    public int next = 1;
    DrawerLayout coordinatorLayout;
    TextView show_dialog_btn_submit,txt_timer_value;
    GoogleMap googleMap=null;
    LatLng FROM_latLng,TO_latlng;
    Polyline polyline;
    MarkerOptions marker_rotate = null;
    LatLng yelloflagLatlaong,redflaglatlong;
    NavigationView nav_view;
    public boolean isLocationReceiverRegistered;
    String[] locationPermissions = {"android.permission.ACCESS_COARSE_LOCATION","android.permission.ACCESS_FINE_LOCATION"};
    boolean location_startpoint=false;
    boolean locationendpoint=false;
    private static final int REQUEST_CODE_LOCATION = 101;
    private static final int REQUEST_CODE_GPSON = 102;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 103;
    private static final int RESULT_CODE_MAPLOCATION = 104;
    TextView et_location,et_location2;
    TextView btn_done;
    public DialogFragment calendardialogfragment;
    String date,time;
    Dialog dialog;
    boolean bootam_vehicletype_listshowing=false;
    public BottomSheetDialog bottm_dialogforconfirmbooking;
    String isFrom="";
    String focus_type="FROM";
    private static final int AUTOCOMPLETE_FROM = 123;
    private static final int AUTOCOMPLETE_TO = 765;
    String FROM_LAT="";
    String FROM_LNG="";
    String TO_LAT="";
    String TO_LNG="";
    double Total_distanse=0;
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
    List<Response_Vehicle_type.Data_List> get_vehicle_type_list=new ArrayList<>();
    List<Response_All_Vehicle.Data_Vehicle_List> get_vehicle_select_list=new ArrayList<>();
    RecyclerView recy_vehicle_list;
    private DatabaseReference mDatabase;
    boolean Call_driver_book_api=false;
    private static final boolean USE_BUILDERS = false;
    TextView btn_book_later,btn_book_now;
    RadioGroup group;
    RadioButton rb_time,rb_date;
    LinearLayout btn_order_layout;
    String book_vehicleid,book_amount,book_Driver_ID,book_vehicle_no,book_vehicle_image,book_refreshtoken,book_vehicle_type_id;
    String Book_status;
    boolean Check_booking_status=true;
    Gallery galleryview;
    String get_vehicle_type,get_Vehicle_icon;
    boolean check_get_location=true;
    LinearLayout layout_loc_one;
    boolean chk_timer_booking=false;
    double current_lat= 0.0;
    double current_lng= 0.0;
    ArrayList<Near_Driver> Near_driver_list=new ArrayList<>();
    ImageView btn_pin;
    ImageView image_header;
    TextView header_name;
    int Select_vehicle_position=0;
    boolean btn_confirmbookingnow=false;
    boolean btn_confirmbookinglater=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from__location);

        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme_down_up;
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        nav_view=findViewById(R.id.nav_view);
        btn_pin=findViewById(R.id.pin);
        layout_loc_one=findViewById(R.id.layout_one);
        galleryview=(Gallery)findViewById(R.id.gallery);
        ImageView btnback=findViewById(R.id.back_btn_location);
        coordinatorLayout=findViewById(R.id.coordinatorLayout);
        coordinatorLayout.requestDisallowInterceptTouchEvent(true);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*finish();
                Home home=new Home();
                 home.drawer.openDrawer(Gravity.START);*/
                try {

                    if(!coordinatorLayout.isDrawerOpen(GravityCompat.START)) {
                        coordinatorLayout.openDrawer(Gravity.START);
                    }
                    else  {
                        coordinatorLayout.closeDrawer(Gravity.START);
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    String s=e.toString();
                }



            }
        });

        View headerView = nav_view.getHeaderView(0);

        if (App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase("null")
                || App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase(null)
                || App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase(""))
        {
            nav_view.inflateMenu(R.menu.activity_home_drawer);
        }else{
            nav_view.inflateMenu(R.menu.activity_driver_menu);
            mSocket.connect();
        }

        TextView btn_driver_login=headerView.findViewById(R.id.textView_driver);

        btn_driver_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_CONTACT_NUMBER,"").equalsIgnoreCase("")
                        || App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_EMAIL,"").equalsIgnoreCase(""))
                {
                    Toast.makeText(From_Location.this, "Please complete your profile", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(From_Location.this,Acc_edit.class));
                }else
                {
                    if (App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase("null")
                            || App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase(null)
                            || App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase(""))
                    {
                        startActivity(new Intent(From_Location.this,Driver_document.class));
                    }else{
                        startActivity(new Intent(From_Location.this,Vehicle_reg.class));
                    }
                }
            }
        });


        nav_view.setNavigationItemSelectedListener(this);




        btn_order_layout=findViewById(R.id.layout_btn_order);
        rb_time=findViewById(R.id.choice_grid_picker);
        rb_date=findViewById(R.id.choice_date_picker);

        group = (RadioGroup) findViewById(R.id.radioGroup);
        btn_book_later=findViewById(R.id.txt_book_later);
        btn_book_now=findViewById(R.id.txt_book_now);
        image_header = (ImageView) headerView.findViewById(R.id.imageView);
        header_name = (TextView) headerView.findViewById(R.id.textView_name);
        set_Header_value();
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(From_Location.this,Acc_edit.class));
            }
        });
        image_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(From_Location.this,Acc_edit.class));
            }
        });


        btn_book_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (App_Utils.isNetworkAvailable(From_Location.this))
                {
                    if (Check_booking_status){
                        Book_status="Now";
                        String datenew=App_Utils.getCurrentdate();
                        btn_confirmbookingnow=true;
                        Show_Dialog_booking(datenew,book_vehicleid,book_amount,book_Driver_ID,book_vehicle_no,book_vehicle_image,book_refreshtoken,book_vehicle_type_id);
                        //get_driver_token(datenew,book_vehicleid,book_amount,book_Driver_ID,book_vehicle_no,book_vehicle_image,book_refreshtoken,book_vehicle_type_id);
                    }else{
                        Toast.makeText(From_Location.this, "Already ride pending", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(From_Location.this, "No internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_book_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (App_Utils.isNetworkAvailable(From_Location.this))
                {      if (Check_booking_status){
                    Book_status="Later";
                    rb_time.setChecked(true);
                    Show_calander();
                    check_get_location=false;
                    btn_confirmbookinglater=true;
                }else{
                    Toast.makeText(From_Location.this, "Already ride pending", Toast.LENGTH_SHORT).show();
                }
                }else{
                    Toast.makeText(From_Location.this, "No internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ImageView get_from_Address_btn=findViewById(R.id.find_location);
        ImageView get_to_Address_btn=findViewById(R.id.find_location2);

        // Initialize Places.
        Places.initialize(getApplicationContext(), getString(R.string.google_api_key));
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

       /* // Initialize the AutocompleteSupportFragment.
        final AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                    et_location.setText(place.getName());
                    LatLng get_latlong=place.getLatLng();
                    set_location_list(get_latlong);
                     startMapAnimation(get_latlong);
                    autocompleteFragment.onDestroy();  }
            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);}
        });

        // Initialize the AutocompleteSupportFragment.
        final AutocompleteSupportFragment autocompleteFragment2 = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment2);
        // Specify the types of place data to return.
        autocompleteFragment2.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME));

        autocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                et_location2.setText(place.getName());
                LatLng get_latlong=place.getLatLng();
                startMapAnimation(get_latlong);
                set_location_list(get_latlong);
                autocompleteFragment2.onDestroy();  }
            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });*/

        get_from_Address_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focus_type="FROM";

            }
        });


        get_to_Address_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focus_type="TO";
                et_location2.setTextIsSelectable(true);
                String location = et_location2.getText().toString();

            }
        });

        btn_done =(TextView)findViewById(R.id.txt_done);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_location.setBackground(getResources().getDrawable(R.drawable.login_et_rectangle));
                et_location2.setBackground(getResources().getDrawable(R.drawable.login_et_rectangle));
                check_get_location=false;



                if ((FROM_LAT!="") && FROM_latLng!=null && TO_latlng!=null && (FROM_LNG!="") && (TO_LAT!="") && (TO_LNG!="") && (FROM_LAT!=null) && (FROM_LNG!=null) && (TO_LAT!=null) && (TO_LNG!=null) ) {
                    Double distance_betweenstarttoandpoint=distance(Double.valueOf(FROM_LAT),Double.valueOf(FROM_LNG),Double.valueOf(TO_LAT),Double.valueOf(TO_LNG));

                    if (distance_betweenstarttoandpoint > 60.00) {
                        Toast.makeText(From_Location.this, "Sorry, you cannot book your ride more then  60 Kms.", Toast.LENGTH_SHORT).show();
                    } else {
                        Show_polyline_map();
                    }
                }else {
                    Toast.makeText(From_Location.this, "Please select your Destination", Toast.LENGTH_SHORT).show();
                }




            }
        });

        et_location2 =  findViewById(R.id.et_location2);
        et_location =  findViewById(R.id.et_location);

        et_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .setCountry("IN")
                        .build(From_Location.this);
                startActivityForResult(intent, AUTOCOMPLETE_FROM);
                // autocompleteFragment2.setAllowReturnTransitionOverlap(false);
                btn_pin.setVisibility(View.VISIBLE);
                check_get_location=true;
                try{
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }catch (Exception e){e.printStackTrace();}
                focus_type="FROM";
                check_get_location=true;
                et_location.setBackground(getResources().getDrawable(R.drawable.login_et_rectangle2));
                et_location2.setBackground(getResources().getDrawable(R.drawable.login_et_rectangle));
            }
        });
        et_location2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .setCountry("IN")
                        .build(From_Location.this);
                startActivityForResult(intent, AUTOCOMPLETE_TO);

                btn_pin.setVisibility(View.VISIBLE);
                check_get_location=true;
                try{
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }catch (Exception e){e.printStackTrace();}

                focus_type="TO";
                et_location2.setBackground(getResources().getDrawable(R.drawable.login_et_rectangle2));
                et_location.setBackground(getResources().getDrawable(R.drawable.login_et_rectangle));

            }
        });

        if(getIntent().getExtras()!=null)
        {
            isFrom=getIntent().getExtras().getString("isFrom");
        }

        recy_vehicle_list = (RecyclerView)findViewById(R.id.recycle_vehicle_Select_list);
        StaggeredGridLayoutManager staggeredGridLayoutManager2 = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        recy_vehicle_list.setLayoutManager(staggeredGridLayoutManager2); // set LayoutManager to RecyclerView

        Call_Vihicle_Api();

        Set_location_on_map();

    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();




        if (btn_confirmbookingnow==true)
        {
            if (bottm_dialogforconfirmbooking!=null)
                bottm_dialogforconfirmbooking.dismiss();
            bottm_dialogforconfirmbooking.cancel();
            btn_confirmbookingnow=false;
        }
        else  if (bootam_vehicletype_listshowing==true){
            bootam_vehicletype_listshowing=false;
            galleryview.setVisibility(View.GONE);
            et_location2.setClickable(true);
            et_location.setClickable(true);
            btn_pin.setVisibility(View.VISIBLE);
            btn_order_layout.setVisibility(View.GONE);
            if (bottm_dialogforconfirmbooking!=null) {
                bottm_dialogforconfirmbooking.dismiss();

                bottm_dialogforconfirmbooking.cancel();
            }
            btn_done.setText("Search");
            googleMap.clear();
        }
        else if (btn_confirmbookinglater==true)
        {
            // bottm_dialogforconfirmbooking.dismiss();
            calendardialogfragment.dismiss();
            btn_confirmbookinglater=false;
        }
        else if(coordinatorLayout.isDrawerOpen(GravityCompat.START)) {
            coordinatorLayout.closeDrawer(Gravity.START);
        }
        else {

            final Dialog dialog = App_Utils.createDialog(From_Location.this, false);
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
                    Intent i = new Intent();
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(i);

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
    private void set_Header_value() {
        try{

            if (App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase("null")
                    || App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase(null)
                    || App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase(""))
            {
                //btn_from_address.setText("Where to ?");
            }else{
                //btn_from_address.setText("Show your Vehicle");
            }

            String name= App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_NAME,"");
            String photo= App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_PHOTO,"");
            header_name.setText(name);
            Picasso.with(From_Location.this).load(photo)
                    .placeholder(R.drawable.ic_user)
                    .error(R.drawable.ic_user)
                    .into(image_header);
        }catch (Exception e){e.printStackTrace();}
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
             `1       });
                }
            });*/
            mSocket.connect();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void Show_calander() {

        calendardialogfragment = createDialog(group.getCheckedRadioButtonId());
        calendardialogfragment.show(getSupportFragmentManager(), TAG);

    }

    private DialogFragment createDialog(int checkedId) {
        if (USE_BUILDERS) {
            return createDialogWithBuilders(checkedId);
        } else {
            return createDialogWithSetters(checkedId);
        }
    }

    private DialogFragment createDialogWithBuilders(int checkedId) {
        BottomSheetPickerDialog.Builder builder = null;
        boolean custom = false;
        boolean customDark = false;
        boolean themeDark = false;

        switch (checkedId) {
            case R.id.choice_number_pad:
            case R.id.choice_number_pad_dark:
            case R.id.choice_number_pad_custom:
            case R.id.choice_number_pad_custom_dark: {
                custom = checkedId == R.id.choice_number_pad_custom;
                customDark = checkedId == R.id.choice_number_pad_custom_dark;
                themeDark = checkedId == R.id.choice_number_pad_dark || customDark;
                builder = new NumberPadTimePickerDialog.Builder(From_Location.this);
                if (custom || customDark) {
                    ((NumberPadTimePickerDialog.Builder) builder).setHeaderTextColor(0xFFFF4081);
                }
                break;
            }
            case R.id.choice_grid_picker:
            case R.id.choice_grid_picker_dark:
            case R.id.choice_grid_picker_custom:
            case R.id.choice_grid_picker_custom_dark: {
                custom = checkedId == R.id.choice_grid_picker_custom;
                customDark = checkedId == R.id.choice_grid_picker_custom_dark;
                themeDark = checkedId == R.id.choice_grid_picker_dark || customDark;

                Calendar now = Calendar.getInstance();
                builder = new GridTimePickerDialog.Builder(
                        From_Location.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        DateFormat.is24HourFormat(From_Location.this));
                GridTimePickerDialog.Builder gridDialogBuilder =
                        (GridTimePickerDialog.Builder) builder;
                if (custom || customDark) {
                    gridDialogBuilder.setHeaderTextColorSelected(0xFFFF4081)
                            .setHeaderTextColorUnselected(0x4AFF4081)
                            .setTimeSeparatorColor(0xFF000000)
                            .setHalfDayButtonColorSelected(0xFFFF4081)
                            .setHalfDayButtonColorUnselected(0x4AFF4081);
                }
                break;
            }
            case R.id.choice_date_picker:
            case R.id.choice_date_picker_dark:
            case R.id.choice_date_picker_custom:
            case R.id.choice_date_picker_custom_dark: {
                custom = checkedId == R.id.choice_date_picker_custom;
                customDark = checkedId == R.id.choice_date_picker_custom_dark;
                themeDark = checkedId == R.id.choice_date_picker_dark || customDark;

                Calendar now = Calendar.getInstance();
                Calendar max = Calendar.getInstance();
                max.add(Calendar.YEAR, 10);
                builder = new DatePickerDialog.Builder(
                        From_Location.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
                DatePickerDialog.Builder dateDialogBuilder = (DatePickerDialog.Builder) builder;
                dateDialogBuilder.setMaxDate(max)
                        .setMinDate(now)
                        .setYearRange(1970, 2032);
                if (custom || customDark) {
                    dateDialogBuilder.setHeaderTextColorSelected(0xFFFF4081)
                            .setHeaderTextColorUnselected(0x4AFF4081)
                            .setDayOfWeekHeaderTextColorSelected(0xFFFF4081)
                            .setDayOfWeekHeaderTextColorUnselected(0x4AFF4081);
                }
                break;
            }
        }

        builder.setThemeDark(themeDark);
        if (custom || customDark) {
            builder.setAccentColor(0xFFFF4081)
                    .setBackgroundColor(custom? 0xFF90CAF9 : 0xFF2196F3)
                    .setHeaderColor(custom? 0xFF90CAF9 : 0xFF2196F3)
                    .setHeaderTextDark(custom);
        }
        return builder.build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_FROM) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("tag", "Place: " + place.getName() + ", " + place.getLatLng());
                //       tvLocationName.setText(place.getName());
                //       tvPlaceId.setText(place.getId());
                //      tvLatLon.setText(String.valueOf(place.getLatLng()));

                et_location.setText(place.getName());
                location_startpoint=true;
                yelloflagLatlaong=place.getLatLng();
                startMapAnimation(yelloflagLatlaong);
                set_location_list(yelloflagLatlaong);


            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("tag", status.getStatusMessage());
            }
            else if (resultCode == RESULT_CANCELED)
            {
                // The user canceled the operation.
            }
        }
        else if (requestCode == AUTOCOMPLETE_TO)
        {
            if (resultCode == RESULT_OK)
            {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("tag", "Place: " + place.getName() + ", " + place.getLatLng());
                et_location2.setText(place.getName());
                redflaglatlong=place.getLatLng();
                startMapAnimation(redflaglatlong);
                set_location_list(redflaglatlong);
                locationendpoint=true;
            }
            else if (resultCode == AutocompleteActivity.RESULT_ERROR)
            {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("tag", status.getStatusMessage());
            }
            else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
    private DialogFragment createDialogWithSetters(int checkedId) {
        BottomSheetPickerDialog dialog = null;
        boolean custom = false;
        boolean customDark = false;
        boolean themeDark = false;

        switch (checkedId) {
            case R.id.choice_number_pad:
            case R.id.choice_number_pad_dark:
            case R.id.choice_number_pad_custom:
            case R.id.choice_number_pad_custom_dark: {
                dialog = NumberPadTimePickerDialog.newInstance(From_Location.this);
                custom = checkedId == R.id.choice_number_pad_custom;
                customDark = checkedId == R.id.choice_number_pad_custom_dark;
                themeDark = checkedId == R.id.choice_number_pad_dark || customDark;
                if (custom || customDark) {
                    ((NumberPadTimePickerDialog) dialog).setHeaderTextColor(0xFFFF4081);
                }
                break;
            }
            case R.id.choice_grid_picker:
            case R.id.choice_grid_picker_dark:
            case R.id.choice_grid_picker_custom:
            case R.id.choice_grid_picker_custom_dark: {
                Calendar now = Calendar.getInstance();
                dialog = GridTimePickerDialog.newInstance(
                        From_Location.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        DateFormat.is24HourFormat(From_Location.this));
                custom = checkedId == R.id.choice_grid_picker_custom;
                customDark = checkedId == R.id.choice_grid_picker_custom_dark;
                themeDark = checkedId == R.id.choice_grid_picker_dark || customDark;
                GridTimePickerDialog gridDialog = (GridTimePickerDialog) dialog;
                if (custom || customDark) {
                    gridDialog.setHeaderTextColorSelected(0xFFFF4081);
                    gridDialog.setHeaderTextColorUnselected(0x4AFF4081);
                    gridDialog.setTimeSeparatorColor(0xFF000000);
                    gridDialog.setHalfDayButtonColorSelected(0xFFFF4081);
                    gridDialog.setHalfDayButtonColorUnselected(0x4AFF4081);
                }
                break;
            }
            case R.id.choice_date_picker:
            case R.id.choice_date_picker_dark:
            case R.id.choice_date_picker_custom:
            case R.id.choice_date_picker_custom_dark: {
                Calendar now = Calendar.getInstance();
                dialog = DatePickerDialog.newInstance(
                        From_Location.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
                custom = checkedId == R.id.choice_date_picker_custom;
                customDark = checkedId == R.id.choice_date_picker_custom_dark;
                themeDark = checkedId == R.id.choice_date_picker_dark || customDark;
                DatePickerDialog dateDialog = (DatePickerDialog) dialog;
                dateDialog.setMinDate(now);
                Calendar max = Calendar.getInstance();
                max.add(Calendar.YEAR, 10);
                dateDialog.setMaxDate(max);
                dateDialog.setYearRange(1970, 2032);
                if (custom || customDark) {
                    dateDialog.setHeaderTextColorSelected(0xFFFF4081);
                    dateDialog.setHeaderTextColorUnselected(0x4AFF4081);
                    dateDialog.setDayOfWeekHeaderTextColorSelected(0xFFFF4081);
                    dateDialog.setDayOfWeekHeaderTextColorUnselected(0x4AFF4081);
                }
                break;
            }
        }

        dialog.setThemeDark(themeDark);
        if (custom || customDark) {
            dialog.setAccentColor(0xFFFF4081);
            dialog.setBackgroundColor(custom? 0xFF90CAF9 : 0xFF2196F3);
            dialog.setHeaderColor(custom? 0xFF90CAF9 : 0xFF2196F3);
            dialog.setHeaderTextDark(custom);
        }

        return dialog;
    }

    private void Save_data_on_firebase(DatabaseReference mDatabase) {
        // Read from the database
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //  String value = dataSnapshot.getValue(String.class);
                //  Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    private void Call_Vihicle_Api() {

        //     recyclerView_vehicle_type.setVisibility(View.VISIBLE);
        HashMap map= new HashMap<>();

        if (isNetworkAvailable(From_Location.this))
        {
            Call<Response_Vehicle_type> call= APIClient.getWebServiceMethod().get_All_vehicle_type(map);
            call.enqueue(new Callback<Response_Vehicle_type>() {
                @SuppressLint("Range")
                @RequiresApi(api = Build.VERSION_CODES.P)
                @Override
                public void onResponse(Call<Response_Vehicle_type> call, Response<Response_Vehicle_type> response) {

                    String status=response.body().getApi_status();
                    String msg=response.body().getApi_message();
                    String gender_value=App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_GENDER,"");

                    if (status.equalsIgnoreCase("1") && msg.equalsIgnoreCase("success") )
                    {
                        for (int i=0;i<response.body().getData().size();i++)
                        {
                            if (response.body().getData().get(i).getVehicle_type().trim().equalsIgnoreCase("Scooty"))
                            {
                                if (gender_value.equalsIgnoreCase("Female")){
                                    get_vehicle_type_list.add(response.body().getData().get(i));
                                }
                            }else{
                                get_vehicle_type_list.add(response.body().getData().get(i));
                            }
                        }

                        //  final Adapter_Vehicle_gallery galleryImageAdapter= new Adapter_Vehicle_gallery(this);

                    }else{

                        Toast.makeText(From_Location.this, "status "+status+"\n"+"msg "+msg, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Response_Vehicle_type> call, Throwable t) {

                    Toast.makeText(From_Location.this, "Error : "+t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(From_Location.this, "No Internet", Toast.LENGTH_SHORT).show();
        }

    }

    private void Call_Select_Vihicle_Api(String vehicle_id, final String vehicle_price) {
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        get_vehicle_select_list.clear();
        recy_vehicle_list.setVisibility(View.VISIBLE);
        recy_vehicle_list.clearFocus();
        HashMap map= new HashMap<>();
        map.put("vehicle_type_id",vehicle_id);
        map.put("lat",String.valueOf(App_Conteroller.latitute));
        map.put("lng",String.valueOf(App_Conteroller.longitude));

        if (isNetworkAvailable(From_Location.this))
        {
            Call<Response_All_Vehicle> call= APIClient.getWebServiceMethod().get_All_select_vehicle(map);
            call.enqueue(new Callback<Response_All_Vehicle>() {
                @Override
                public void onResponse(Call<Response_All_Vehicle> call, Response<Response_All_Vehicle> response) {
                    progressDialog.dismiss();
                    String status=response.body().getResponseCode();
                    String msg=response.body().getResponseMessage();
                    if (status.equalsIgnoreCase("200") )
                    {
                        get_vehicle_select_list.clear();
                        Log.i(TAG,"Log driver price :"+vehicle_price);
                        final double price_pkm= Double.valueOf(vehicle_price);

                        List<Response_All_Vehicle.Data_Vehicle_List> get_select_list=new ArrayList<>();

                        get_select_list=response.body().getResult();
                        for (int m = 0; m<get_select_list.size(); m++)
                        {

                            ///// CHECK FROM FIREBASEDATABSE FOR VEHICLE ACTIVE STATUS OR NOT
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            final Query myTopPostsQuery = mDatabase.child("Driver_Current_latlng_ID").child(get_select_list.get(m).getDriver_id());

                            // My top posts by number of stars
                            final List<Response_All_Vehicle.Data_Vehicle_List> finalGet_select_list = get_select_list;
                            final int finalM = m;
                            myTopPostsQuery.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.d(TAG, "Number of messages: " + dataSnapshot.getChildrenCount());
                                    String token_no = String.valueOf(dataSnapshot.child("token_no").getValue());
                                    String driver_id = String.valueOf(dataSnapshot.child("driver_id").getValue());
                                    String driver_lat = String.valueOf(dataSnapshot.child("driver_lat").getValue());
                                    String driver_lng = String.valueOf(dataSnapshot.child("driver_lng").getValue());
                                    String driver_vehicle_no = String.valueOf(dataSnapshot.child("driver_vehicle_no").getValue());
                                    String driver_vehicle_type = String.valueOf(dataSnapshot.child("driver_vehicle_type").getValue());
                                    String driver_status = String.valueOf(dataSnapshot.child("driver_status").getValue());
//distance(Double.valueOf(FROM_LAT),Double.valueOf(FROM_LNG),Double.valueOf(TO_LAT),Double.valueOf(TO_LNG)
                                    Double user_startpoint_lat,user_startpoint_long;
                                    if (yelloflagLatlaong!=null){
                                        user_startpoint_lat=Double.valueOf(yelloflagLatlaong.latitude);
                                        user_startpoint_long=Double.valueOf(yelloflagLatlaong.longitude);

                                    }
                                    else
                                    {
                                        user_startpoint_lat= App_Conteroller.latitute;
                                        user_startpoint_long= App_Conteroller.longitude;
                                    }
                                    Double driver_lat_double=Double.valueOf(driver_lat);
                                    Double driver_long_douvle=Double.valueOf(driver_lng);
                                    Double distances= distance(user_startpoint_lat,user_startpoint_long,driver_lat_double,driver_long_douvle);
                                    if (driver_status.equalsIgnoreCase("Active")&&distances<=10)
                                    {
                                        get_vehicle_select_list.add(finalGet_select_list.get(finalM));
                                        if (finalM==(finalGet_select_list.size()-1))
                                        {
                                            Collections.sort(get_vehicle_select_list, new Comparator<Response_All_Vehicle.Data_Vehicle_List>() {
                                                @Override
                                                public int compare(Response_All_Vehicle.Data_Vehicle_List u1, Response_All_Vehicle.Data_Vehicle_List u2) {
                                                    return u1.getDistance().compareTo(u2.getDistance());
                                                }
                                            });




                                            if (get_vehicle_select_list.size()==0)
                                            {
                                                Toast.makeText(From_Location.this, "Sorry, there is no any vehicle availabe, please try with other", Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                for (int i=0;i<get_vehicle_select_list.size();i++)
                                                {
                                                    double price=distance(user_startpoint_lat,user_startpoint_long,redflaglatlong.latitude,redflaglatlong.longitude)*price_pkm;
                                                    get_vehicle_select_list.get(i).setVehicle_price(String.valueOf(price));
                                                    get_driver_location(get_vehicle_select_list.get(i).getDriver_id(),i);
                                                }
                                                get_select_vehicle_api(Select_vehicle_position);

                                            }
                                        }
                                    }
                                    else {
                                        if (finalM==(finalGet_select_list.size()-1)){





                                            if (get_vehicle_select_list.size()==0)
                                            {

                                                Toast.makeText(From_Location.this, "Sorry, there is no any vehicle availabe, please try with other", Toast.LENGTH_SHORT).show();
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


                        Adapter_Vehicle adapter_past=new Adapter_Vehicle(From_Location.this,get_vehicle_select_list,From_Location.this);
                        //           recy_vehicle_list.setAdapter(adapter_past);

                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(From_Location.this, "status "+status+"\n"+"msg "+msg, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Response_All_Vehicle> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(From_Location.this, "Error : "+t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(From_Location.this, "No Internet", Toast.LENGTH_SHORT).show();
        }

    }

    private void get_select_vehicle_api(int vehicle_position) {

        book_vehicleid=get_vehicle_select_list.get(vehicle_position).getId();
        book_amount=get_vehicle_select_list.get(vehicle_position).getVehicle_price();
        book_Driver_ID=get_vehicle_select_list.get(vehicle_position).getDriver_id();
        book_vehicle_no=get_vehicle_select_list.get(vehicle_position).getVehicle_number();
        book_vehicle_image=get_vehicle_select_list.get(vehicle_position).getVehicle_photo();
        book_refreshtoken=get_vehicle_select_list.get(vehicle_position).getToken_no();
        book_vehicle_type_id=get_vehicle_select_list.get(vehicle_position).getVehicle_type_id();
        btn_order_layout.setVisibility(View.VISIBLE);

    }

    private void get_driver_location(String driver_id, final int position) {

       /* mDatabase = FirebaseDatabase.getInstance().getReference();
        Query myTopPostsQuery = mDatabase.child("Driver_Current_latlng_ID").child(driver_id);

        // My top posts by number of stars
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "Number of messages: " + dataSnapshot.getChildrenCount());
                String token_no = String.valueOf(dataSnapshot.child("token_no").getValue());
                String driver_id = String.valueOf(dataSnapshot.child("driver_id").getValue());
                String driver_lat = String.valueOf(dataSnapshot.child("driver_lat").getValue());
                String driver_lng = String.valueOf(dataSnapshot.child("driver_lng").getValue());
                String driver_vehicle_no = String.valueOf(dataSnapshot.child("driver_vehicle_no").getValue());
                String driver_vehicle_type = String.valueOf(dataSnapshot.child("driver_vehicle_type").getValue());
                String driver_status = String.valueOf(dataSnapshot.child("driver_status").getValue());

                if (driver_status.equalsIgnoreCase("Active"))
                {
                    LatLng latlng=new LatLng(Double.valueOf(driver_lat),Double.valueOf(driver_lng));
                    show_vehicle_location_list(latlng,driver_id,driver_vehicle_type,position,driver_vehicle_no);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });*/






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
                    /** to call for GPS Enabling
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



    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void Show_polyline_map()
    {
        if ((FROM_LAT!="") && FROM_latLng!=null && TO_latlng!=null && (FROM_LNG!="") && (TO_LAT!="") && (TO_LNG!="") && (FROM_LAT!=null) && (FROM_LNG!=null) && (TO_LAT!=null) && (TO_LNG!=null) )
        {
            ///// START POINT MARKER
            //  LatLng startlatlong = new LatLng(FROM_latLng.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(FROM_latLng);
            //markerOptions.title("Start");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            Marker   startpoint = googleMap.addMarker(markerOptions);
            //// END POINT MARKER
            MarkerOptions markerOptionsendpoint = new MarkerOptions();
            markerOptionsendpoint.position(TO_latlng);
            //markerOptions.title("Start");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            Marker   endpoint = googleMap.addMarker(markerOptionsendpoint);

            set_line_on_map(FROM_latLng,TO_latlng);
            DecimalFormat df2=new DecimalFormat("#.##");
            btn_pin.setVisibility(View.GONE);
            btn_done.setText("Distance in km: "+String.valueOf(df2.format(distance(Double.valueOf(FROM_LAT),Double.valueOf(FROM_LNG),Double.valueOf(TO_LAT),Double.valueOf(TO_LNG)))));

            Adapter_Vehicle_gallery adapter_past=new Adapter_Vehicle_gallery(From_Location.this,get_vehicle_type_list);
            galleryview.setAdapter(adapter_past);
            galleryview.setSpacing(33);
            galleryview.setUnselectedAlpha(11);
            galleryview.setHorizontalScrollBarEnabled(true);

            et_location2.setClickable(false);
            et_location.setClickable(false);
            bootam_vehicletype_listshowing=true;
            galleryview.setVisibility(View.VISIBLE);
            galleryview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    int select_item=galleryview.getSelectedItemPosition();
                    btn_order_layout.setVisibility(View.GONE);
                    btn_pin.setVisibility(View.GONE);
                    googleMap.clear();
                    Show_polyline_map();
                    if (get_vehicle_type_list.get(i).getKm_price()==null)
                    {
                        Call_Select_Vihicle_Api(get_vehicle_type_list.get(i).getId(),"1");
                    }else{
                        Call_Select_Vihicle_Api(get_vehicle_type_list.get(i).getId(),get_vehicle_type_list.get(i).getKm_price());
                    }
                    get_vehicle_type=get_vehicle_type_list.get(i).getVehicle_type();
                    get_Vehicle_icon=get_vehicle_type_list.get(i).getVehicle_icon();
                    galleryview.setSelection(select_item,true);
                }
            });
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                galleryview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                        btn_order_layout.setVisibility(View.GONE);
                    }
                });
            }

        }
        else {
            Toast.makeText(this, "Please select Start point and end point", Toast.LENGTH_SHORT).show();
        }

    }



    private void set_line_on_map(LatLng from_latLng, LatLng to_latlng) {
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
                            // googleMap.addPolyline(DirectionConverter.createPolyline(From_Location.this, directionPositionList, 4, getResources().getColor(R.color.light_green_700)));
                            //// ADD POLYLINE
                            PolylineOptions polylineOptions = new PolylineOptions();

                            for (int i = 0; i < directionPositionList.size(); i++)
                            {

                                String latitude = String.valueOf(directionPositionList.get(i).latitude);
                                String longitude = String.valueOf(directionPositionList.get(i).longitude);

                                polylineOptions.color(Color.BLACK);

                                polylineOptions.width(3);

                                Double lat = Double.parseDouble(latitude);
                                Double Longitude = Double.parseDouble(longitude);

                                polylineOptions.add(new LatLng(lat, Longitude));
                            }
                            polyline=googleMap.addPolyline(polylineOptions);





                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try
                        {
                            setCameraWithCoordinationBounds(route);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        //  progressDialogmap.dismiss();
                    } catch (Exception e)
                    {
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
        Total_distanse=dist;
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
            coordinatorLayout = (DrawerLayout) findViewById(R.id.coordinatorLayout);

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
                        //////

                        /*View locationButton = ((View) ((SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.googleMap)).getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
// position on right bottom
                        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                        rlp.setMargins(0, 140, 140, 0);*/
                        View locationButton = ((View) ((SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.googleMap)).getView().findViewById(Integer.parseInt("4")).getParent()).findViewById(Integer.parseInt("2"));
                        // and next place it, on bottom right (as Google Maps app)
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                                locationButton.getLayoutParams();
                        // position on right bottom
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                        layoutParams.setMargins(0, 0, 30, 30);




                        View compass = ((View) ((SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.googleMap)).getView().findViewById(Integer.parseInt("5")).getParent()).findViewById(Integer.parseInt("5"));
                        // and next place it, on bottom right (as Google Maps app)
                        RelativeLayout.LayoutParams layoutParamss = (RelativeLayout.LayoutParams)
                                compass.getLayoutParams();
                        // position on right bottom
                        layoutParamss.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                        layoutParamss.addRule(RelativeLayout.ABOVE,locationButton.getId());
                        layoutParamss.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                        layoutParamss.setMargins(0, 0, 30, 30);

                    }
                    else
                    {
                        App_Utils.showAlertSnakeMessageForDrawerLAyout(coordinatorLayout,"Sorry! Location will not found.");
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void startMapAnimation(final LatLng latlng)
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
                        .target(latlng)
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
                    if(check_get_location)
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
                            if (location_startpoint==false) {
                                et_location.setText("" + fullAddress);
                            }
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
                            if (locationendpoint==false) {
                                et_location2.setText("" + fullAddress);
                            }
                            TO_LAT= String.valueOf(latLng.latitude);
                            TO_LNG=String.valueOf(latLng.longitude);
                        }
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
                LatLng lt= new LatLng(App_Conteroller.latitute,App_Conteroller.longitude);
                startMapAnimation(lt);

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
                                LatLng lt= new LatLng(App_Conteroller.latitute,App_Conteroller.longitude);
                                startMapAnimation(lt);
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        return false;
                    }
                });
                progressDialog.dismiss();
                //  et_location2.performClick();
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


                googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {
                        layout_loc_one.setVisibility(View.GONE);
                        recy_vehicle_list.setVisibility(View.GONE);
                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {

                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                        layout_loc_one.setVisibility(View.VISIBLE);
                        recy_vehicle_list.setVisibility(View.VISIBLE);
                    }
                });

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        String Snippet =  marker.getSnippet();
                        int position=Integer.valueOf(Snippet);

                        book_vehicleid=get_vehicle_select_list.get(position).getId();
                        book_amount=get_vehicle_select_list.get(position).getVehicle_price();
                        book_Driver_ID=get_vehicle_select_list.get(position).getDriver_id();
                        book_vehicle_no=get_vehicle_select_list.get(position).getVehicle_number();
                        book_vehicle_image=get_vehicle_select_list.get(position).getVehicle_photo();
                        book_refreshtoken=get_vehicle_select_list.get(position).getToken_no();
                        book_vehicle_type_id=get_vehicle_select_list.get(position).getVehicle_type_id();
                        btn_order_layout.setVisibility(View.VISIBLE);

                        /*if (get_vehicle_type_list.get(position).getKm_price()==null)
                        {
                            Call_Select_Vihicle_Api(get_vehicle_type_list.get(position).getId(),"1");
                        }else{
                            Call_Select_Vihicle_Api(get_vehicle_type_list.get(position).getId(),get_vehicle_type_list.get(position).getKm_price());
                        }
                        get_vehicle_type=get_vehicle_type_list.get(position).getVehicle_type();
                        get_Vehicle_icon=get_vehicle_type_list.get(position).getVehicle_icon();
*/
                        return true;
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

        current_lat=location.getLatitude();
        current_lng=location.getLongitude();
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

    private void show_vehicle_location_list(final LatLng location,String driver_id,String veh_type_id,int position,String vehicle_no) {
        int update_marker2=0;
        try{
            double Add_lat= location.latitude;
            double Add_long= location.longitude;

            FROM_LAT=String.valueOf(Add_lat);
            FROM_LNG=String.valueOf(Add_long);
            LatLng latLng = new LatLng(Add_lat, Add_long);
            MarkerOptions marker3=null;

            if (update_marker2==0){
                marker3 = new MarkerOptions().position(location);
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
                marker3. snippet(String.valueOf(position));

                googleMap.addMarker(marker3).setTitle("Vehicle No :"+vehicle_no);

            }

        }catch (Exception e){
            e.printStackTrace();}
    }

    private void set_location_list(final LatLng location) {

        try{
            double Add_lat= location.latitude;
            double Add_long= location.longitude;

            if (focus_type.equalsIgnoreCase("FROM"))
            {
                FROM_LAT=String.valueOf(Add_lat);
                FROM_LNG=String.valueOf(Add_long);
                LatLng latLng = new LatLng(Add_lat, Add_long);
                //       googleMap.addMarker(new MarkerOptions().position(latLng));
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }else{
                TO_LAT=String.valueOf(Add_lat);
                TO_LNG=String.valueOf(Add_long);
                LatLng latLng = new LatLng(Add_lat, Add_long);
                //    googleMap.addMarker(new MarkerOptions().position(latLng));
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(location)      // Sets the center of the map to Mountain View
                    .zoom(10)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(90)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }catch (Exception e){
            e.printStackTrace();}
    }

    @Override
    public void OnClick_item(Response_All_Vehicle.Data_Vehicle_List vehicle_id) {
        //for vehicle book call api
        book_vehicleid=vehicle_id.getId();
        book_amount=vehicle_id.getVehicle_price();
        book_Driver_ID=vehicle_id.getDriver_id();
        book_vehicle_no=vehicle_id.getVehicle_number();
        book_vehicle_image=vehicle_id.getVehicle_photo();
        book_refreshtoken=vehicle_id.getToken_no();
        book_vehicle_type_id=vehicle_id.getVehicle_type_id();
        btn_order_layout.setVisibility(View.VISIBLE);

    }

    public void Call_Api_book_ride(String date,final String vehicleid, String pricc, final String driver_ID, final String vehicle_no, final String vehicle_image, String refreshtoken,final String vehicla_type_id){

        final HashMap<String,String> map=new HashMap<>();
        String userid=App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_ID,"");

        map.put("user_id",""+userid);
        map.put("vehicle_id",""+vehicleid);
        map.put("booked_date_time",""+date);
        map.put("from_lat",""+FROM_LAT);
        map.put("from_lng",""+FROM_LNG);
        map.put("from_address",""+et_location.getText().toString().trim());
        map.put("to_address",""+et_location2.getText().toString().trim());
        map.put("to_lat",""+TO_LAT);
        map.put("to_lng",""+TO_LNG);
        map.put("stoppage_date_time",""+date);
        map.put("payment_status","pending");
        map.put("payment_id","00000000");
        map.put("amount",""+pricc);
        map.put("pickup",""+Book_status);
        map.put("uname",""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_NAME,""));
        map.put("ucontact",""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_CONTACT_NUMBER,""));
        map.put("uimage",""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_PHOTO,""));
        map.put("status","Active");
        map.put("mac_id","121212");
        map.put("remark","yes");
        map.put("ip","959595");
        map.put("token_no",""+refreshtoken);
        map.put("driver_id",""+driver_ID);

        if (isNetworkAvailable(From_Location.this))
        {
            Call<Response_register> call= APIClient.getWebServiceMethod().getBooked_ride(map);
            call.enqueue(new Callback<Response_register>() {
                @Override
                public void onResponse(Call<Response_register> call, final Response<Response_register> response) {

                    try{
                        String status=response.body().getApi_status();
                        String msg=response.body().getApi_message();

                        if (status.equalsIgnoreCase("1") && msg.equalsIgnoreCase("success") )
                        {

                            progressDialog.dismiss();

                            createlivetrack_fordrivertocustomer(driver_ID);










/*

                            final String id=response.body().getId();
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            map.put("book_id",""+id);
                            map.put("user_name",""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_NAME,""));
                            map.put("user_image",""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_PHOTO,""));
                            map.put("user_contact",""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_CONTACT_NUMBER,""));
                            map.put("vehicle_no",vehicle_no);
                            map.put("Driver_id",driver_ID);
                            map.put("vehicle_type_id",vehicla_type_id);
                            map.put("vehicle_image",vehicle_image);
                            mDatabase.child("Booking_ID").child(id).setValue(map);
                            Save_data_on_firebase(mDatabase);
                            Query myTopPostsQuery = mDatabase.child("Driver_ID").child(driver_ID);
                            myTopPostsQuery.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    try{
                                        String name = String.valueOf(dataSnapshot.child("name").getValue());
                                        String state = String.valueOf(dataSnapshot.child("state").getValue());
                                        MarkerOptions marker2 = null;

                                        if (name.toString().equalsIgnoreCase("null") || (name.toString().equalsIgnoreCase(null) || name.toString().equalsIgnoreCase("")) )
                                        {
                                            if (chk_timer_booking)
                                            {
                                                chk_timer_booking=false;
                                                Log.i(TAG,"Remaining time :start");
                                                new CountDownTimer(50000, 1000) {

                                                    public void onTick(long millisUntilFinished) {
                                                        //       txt_booking_timer.setText("Remaining: " + millisUntilFinished / 1000);
                                                        Log.i(TAG,"Remaining time :"+millisUntilFinished / 1000);
                                                    }
                                                    public void onFinish() {
                                                        Log.i(TAG,"Remaining time :done");
                                                        progressDialog.dismiss();
                                                        HashMap<String,String>map=new HashMap<>();
                                                        map.put("id",id);
                                                        Call<Response_register> call= APIClient.getWebServiceMethod().delete_Booked_ride(map);
                                                        call.enqueue(new Callback<Response_register>() {
                                                            @Override
                                                            public void onResponse(Call<Response_register> call, Response<Response_register> response) {
                                                                bottm_dialogforconfirmbooking.dismiss();
                                                                Toast.makeText(From_Location.this, response.message(), Toast.LENGTH_SHORT).show();
                                                            }

                                                            @Override
                                                            public void onFailure(Call<Response_register> call, Throwable t) {
                                                                progressDialog.dismiss();
                                                                bottm_dialogforconfirmbooking.dismiss();
                                                                Toast.makeText(From_Location.this, response.message(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                        mDatabase.child("Booking_ID").child(id).removeValue();
                                                        chk_timer_booking=true;
                                                        Select_vehicle_position= Select_vehicle_position+1;
                                                        Set_value_in_list();
                                                    }
                                                }.start();




                                            }

                                        }
                                        else{

                                            Change_vehicle_status(vehicleid);
                                            progressDialog.dismiss();
                                            googleMap.clear();
                                            ///// check here for live TRACK LOCATION OF DRIVER
                                            createlivetrack_fordrivertocustomer(driver_ID);


                                            // finish();
                                            Toast.makeText(From_Location.this, "Book your ride", Toast.LENGTH_SHORT).show();
                                        }

                                    }catch (Exception e){
                                        String error=e.toString();
                                        Log.i(TAG,"Call_Api_book_ride || error : "+error);
                                        e.printStackTrace();}

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    progressDialog.dismiss();
                                }
                            });
*/

                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(From_Location.this, "status "+status+"\n"+" msg "+msg, Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e){
                        progressDialog.dismiss();
                        Toast.makeText(From_Location.this, "Server error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();}

                }

                @Override
                public void onFailure(Call<Response_register> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(From_Location.this, "Error : "+t.toString(), Toast.LENGTH_SHORT).show();

                }
            });
        }else{
            progressDialog.dismiss();
            Toast.makeText(From_Location.this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void createlivetrack_fordrivertocustomer(String driver_ID) {
        progressDialog.dismiss();
        ///// LIVE TERACKING FOR DRIVER TO CUSTOMER
        polyline.remove();
        googleMap.clear();
        galleryview.setVisibility(View.GONE);
        layout_loc_one.setVisibility(View.GONE);
        btn_order_layout.setVisibility(View.GONE);
        btn_done.setVisibility(View.GONE);
        if (btn_confirmbookingnow == true) {
            if (bottm_dialogforconfirmbooking != null)
                bottm_dialogforconfirmbooking.dismiss();
            bottm_dialogforconfirmbooking.cancel();
            btn_confirmbookingnow = false;
        } else if (bootam_vehicletype_listshowing == true) {
            bootam_vehicletype_listshowing = false;
            galleryview.setVisibility(View.GONE);

            btn_order_layout.setVisibility(View.GONE);
            if (bottm_dialogforconfirmbooking != null) {
                bottm_dialogforconfirmbooking.dismiss();

                bottm_dialogforconfirmbooking.cancel();
            }
            // btn_done.setText("Search");
            //  googleMap.clear();
        } else if (btn_confirmbookinglater == true) {
            // bottm_dialogforconfirmbooking.dismiss();
            calendardialogfragment.dismiss();
            btn_confirmbookinglater = false;
        }

        googleMap.setMyLocationEnabled(true);
        Socket socket=null;
        try
        {
            IO.Options opts = new IO.Options();
            opts.forceNew = true;
            socket = IO.socket("http://thenber.com:4006");
        }
        catch (URISyntaxException e)
        {

        }

        socket.connect();
        socket.on("driverid", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject position = (JSONObject) args[0];
                try {
                    Toast.makeText(From_Location.this, "LOCATION"+position.getString("latitude")+position.getString("latitude"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        //// GET USER LIVE LAT LONG FROM FIREBASE
       /* mDatabase = FirebaseDatabase.getInstance().getReference();
        Query myTopPostsQuery = mDatabase.child("Driver_Current_latlng_ID").child(driver_ID);


        // My top posts by number of stars
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "Number of messages: " + dataSnapshot.getChildrenCount());
                String driver_lat = String.valueOf(dataSnapshot.child("driver_lat").getValue());
                String driver_lng = String.valueOf(dataSnapshot.child("driver_lng").getValue());
                String vehicletype = String.valueOf(dataSnapshot.child("driver_vehicle_type").getValue());
                final LatLng driver_cur_latlong = new LatLng(Double.parseDouble(driver_lat), Double.parseDouble(driver_lng));


//googleMap.clear();
                galleryview.setVisibility(View.GONE);
                layout_loc_one.setVisibility(View.GONE);
                btn_order_layout.setVisibility(View.GONE);
                btn_done.setVisibility(View.GONE);
                marker_rotate = new MarkerOptions().position(driver_cur_latlong);
                if (vehicletype.toString().equalsIgnoreCase("8")) {
                    marker_rotate.icon(BitmapDescriptorFactory.fromResource(R.drawable.auto_icon));
                } else if (vehicletype.toString().equalsIgnoreCase("7")) {
                    marker_rotate.icon(BitmapDescriptorFactory.fromResource(R.drawable.bike));
                } else if (vehicletype.toString().equalsIgnoreCase("6")) {
                    marker_rotate.icon(BitmapDescriptorFactory.fromResource(R.drawable.ok_car_icon));
                } else if (vehicletype.toString().equalsIgnoreCase("5")) {
                    marker_rotate.icon(BitmapDescriptorFactory.fromResource(R.drawable.e_rickshaw));
                } else if (vehicletype.toString().equalsIgnoreCase("4")) {
                    marker_rotate.icon(BitmapDescriptorFactory.fromResource(R.drawable.scooty));
                }
                marker_rotate.snippet(String.valueOf(driver_cur_latlong));

                final Marker marker = googleMap.addMarker(marker_rotate);
                final Double user_cur_lat, user_cur_long;
                if (yelloflagLatlaong == null) {

                 *//*  MarkerOptions usermarkeroptions=new MarkerOptions().position(new LatLng(App_Conteroller.latitute,App_Conteroller.longitude));
                   usermarkeroptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
                   usermarkeroptions. snippet(String.valueOf(new LatLng(App_Conteroller.latitute,App_Conteroller.longitude)));
                   Marker mm=googleMap.addMarker(usermarkeroptions);*//*
                    user_cur_lat = App_Conteroller.latitute;
                    user_cur_long = App_Conteroller.longitude;
                } else {
                   *//*MarkerOptions usermarkeroptions=new MarkerOptions().position(yelloflagLatlaong);
                   usermarkeroptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
                   usermarkeroptions. snippet(String.valueOf(yelloflagLatlaong));
                   Marker mm=googleMap.addMarker(usermarkeroptions);*//*
                    user_cur_lat = yelloflagLatlaong.latitude;
                    user_cur_long = yelloflagLatlaong.longitude;
                }


                ///// SET BEARING BETWEEN DRIVER AND CUSTOMER
                double PI = 3.14159;
                double lat1 = redflaglatlong.latitude * PI / 180;
                double long1 = redflaglatlong.longitude * PI / 180;
                double lat2 = driver_cur_latlong.latitude * PI / 180;
                double long2 = driver_cur_latlong.longitude * PI / 180;

                double dLon = (long2 - long1);

                double y = Math.sin(dLon) * Math.cos(lat2);
                double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                        * Math.cos(lat2) * Math.cos(dLon);

                double brng = Math.atan2(y, x);

                brng = Math.toDegrees(brng);
                brng = (brng + 360) % 360;


                ////// MARKER ROTATING
                final Handler handler = new Handler();
                final long start = SystemClock.uptimeMillis();
                final float startRotation = marker.getRotation();
                final long duration = 1000;

                final Interpolator interpolator = new LinearInterpolator();

                final double finalBrng = brng;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //isMarkerRotating = true;

                        long elapsed = SystemClock.uptimeMillis() - start;
                        float t = interpolator.getInterpolation((float) elapsed / duration);

                        float rot = (float) (t * finalBrng + (1 - t) * startRotation);

                        marker.setRotation(-rot > 180 ? rot / 2 : rot);
                        marker.setAnchor(0.5f,0.5f);
                        if (t < 1.0) {
                            // Post again 16ms later.
                            handler.postDelayed(this, 16);
                        } else {
                            // isMarkerRotating = false;
                        }
                    }
                });


                if (distance(Double.parseDouble(String.valueOf(driver_cur_latlong.latitude)), Double.parseDouble(String.valueOf(driver_cur_latlong.longitude)), Double.parseDouble(String.valueOf(user_cur_lat)), Double.parseDouble(String.valueOf(user_cur_long))) <= 0.100) {
                    Toast.makeText(From_Location.this, "Your Drive is Redy . Start your ride", Toast.LENGTH_SHORT).show();
                }
*/
/*
                GoogleDirection.withServerKey(getResources().getString(R.string.google_maps_key))
                        .from(new LatLng(Double.parseDouble(driver_lat), Double.parseDouble(driver_lng)))
                        .to(new LatLng(user_cur_lat, user_cur_long))
                        .transportMode(TransportMode.DRIVING).execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (direction.isOK()) {
                            try {
                                Route route = direction.getRouteList().get(0);
                                google_map_list.add(String.valueOf(direction.getRouteList().get(0)));
                                try {
                                    final ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
                                    PolylineOptions polylineOptions = new PolylineOptions();

                                    for (int i = 0; i < directionPositionList.size(); i++) {

                                        String latitude = String.valueOf(directionPositionList.get(i).latitude);
                                        String longitude = String.valueOf(directionPositionList.get(i).longitude);

                                        polylineOptions.color(Color.BLACK);

                                        polylineOptions.width(3);

                                        Double lat = Double.parseDouble(latitude);
                                        Double Longitude = Double.parseDouble(longitude);

                                        polylineOptions.add(new LatLng(lat, Longitude));
                                    }
                                    polyline = googleMap.addPolyline(polylineOptions);


                                    callmethodforsetdragcarfromstarttoendpoint(directionPositionList,polylineOptions,new LatLng(user_cur_lat, user_cur_long));








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
                            }*/


        btn_order_layout.setVisibility(View.GONE);
    }


            /*@Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
*/


























    private void Set_value_in_list() {
        Select_vehicle_position= Select_vehicle_position+1;
        if (Select_vehicle_position<get_vehicle_select_list.size())
        {
            get_select_vehicle_api(Select_vehicle_position);
            Book_status="Now";
            String datenew=App_Utils.getCurrentdate();
            Show_Dialog_booking(datenew,book_vehicleid,book_amount,book_Driver_ID,book_vehicle_no,book_vehicle_image,book_refreshtoken,book_vehicle_type_id);

        }else{
            final Dialog dialog = App_Utils.createDialog(From_Location.this, true);
            dialog.setCancelable(false);
            TextView txt_DialogTitle = (TextView) dialog.findViewById(R.id.txt_DialogTitle);
            txt_DialogTitle.setText("This driver is offline. please select other vehicle");
            TextView txt_submit = (TextView) dialog.findViewById(R.id.txt_submit);
            txt_submit.setText("OK");
            txt_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Check_booking_status=true;
                    bottm_dialogforconfirmbooking.dismiss();
                }
            });
            dialog.show();
        }
    }
    private void startTimer(int noOfMinutes, final String booking_id) {
        CountDownTimer countDownTimer = new CountDownTimer(noOfMinutes, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                //Convert milliseconds into hour,minute and seconds
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                txt_timer_value.setText(hms);
                progressDialog.setMessage(hms);
            }
            public void onFinish() {
                HashMap<String,String>map=new HashMap<>();
                map.put("id",booking_id);
                Call<Response_register> call= APIClient.getWebServiceMethod().delete_Booked_ride(map);
                call.enqueue(new Callback<Response_register>() {
                    @Override
                    public void onResponse(Call<Response_register> call, Response<Response_register> response) {
                        mDatabase.child("Booking_ID").child(booking_id).removeValue();
                        chk_timer_booking=true;
                        Select_vehicle_position= Select_vehicle_position+1;
                        show_dialog_btn_submit.performClick();
                    }

                    @Override
                    public void onFailure(Call<Response_register> call, Throwable t) {

                    }
                });
            }
        }.start();

    }

    private void Show_Driver_Location(final String datetime,final String vehicleid,final String amount, final String driver_ID, final String vehicle_no, final String vehicle_image,final String token_no,final String vehicle_type_id) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Call_Api_book_ride(datetime,vehicleid,amount,driver_ID,vehicle_no,vehicle_image,token_no,vehicle_type_id);
        showallnearbydriver();
    }

    private void showallnearbydriver() {



    }


    private void Change_vehicle_status(String vehicleid) {
        if (isNetworkAvailable(From_Location.this))
        {
            Map<String,String> map=new HashMap<>();
            map.put("id",vehicleid);
            map.put("status","Deactive");
            Call<Response_register> call= APIClient.getWebServiceMethod().update_change_vehicle_status(map);
            call.enqueue(new Callback<Response_register>() {
                @Override
                public void onResponse(Call<Response_register> call, Response<Response_register> response) {

                    try{
                        String status=response.body().getApi_status();
                        String msg=response.body().getApi_message();
                        progressDialog.dismiss();
                        if (status.equalsIgnoreCase("1") && msg.equalsIgnoreCase("success") )
                        {

                        }
                        else{

                            Toast.makeText(From_Location.this, "status vehicle "+status+"\n"+" msg vehicle "+msg, Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e){
                        Toast.makeText(From_Location.this, "Server error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();}

                }

                @Override
                public void onFailure(Call<Response_register> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(From_Location.this, "Error : "+t.toString(), Toast.LENGTH_SHORT).show();

                }
            });
        }else{
            Toast.makeText(From_Location.this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void get_driver_token(final String datetime,final String vehicleid, final String amount, final String driver_ID, final String vehicle_no, final String vehicle_image, final String refreshtoken,final String vehicle_type_id,final String book_status) {
        Call_driver_book_api=true;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query myTopPostsQuery = mDatabase.child("Driver_Token_ID").child(driver_ID);

        // My top posts by number of stars
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "Number of messages: " + dataSnapshot.getChildrenCount());
                String token_no = String.valueOf(dataSnapshot.child("token_id").getValue());
                String driver_status = String.valueOf(dataSnapshot.child("driver_status").getValue());
                if (driver_status.equalsIgnoreCase("Active"))
                {
                    if   (Call_driver_book_api)
                    {
                        Call_driver_book_api=false;
                        Show_Driver_Location(datetime,vehicleid,amount,driver_ID,vehicle_no,vehicle_image,token_no,vehicle_type_id);
                    }
                }
                else
                {

                    Select_vehicle_position= Select_vehicle_position+1;
                    if (Select_vehicle_position<get_vehicle_select_list.size())
                    {
                        get_select_vehicle_api(Select_vehicle_position);
                        Book_status="Now";
                        String datenew=App_Utils.getCurrentdate();
                        Show_Dialog_booking(datenew,book_vehicleid,book_amount,book_Driver_ID,book_vehicle_no,book_vehicle_image,book_refreshtoken,book_vehicle_type_id);

                    }else{
                        final Dialog dialog = App_Utils.createDialog(From_Location.this, true);
                        dialog.setCancelable(false);
                        TextView txt_DialogTitle = (TextView) dialog.findViewById(R.id.txt_DialogTitle);
                        txt_DialogTitle.setText("This driver is offline. please select other vehicle");
                        TextView txt_submit = (TextView) dialog.findViewById(R.id.txt_submit);
                        txt_submit.setText("OK");
                        txt_submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                Check_booking_status=true;
                            }
                        });
                        dialog.show();
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


    @Override
    public void onTimeSet(ViewGroup viewGroup, int hourOfDay, int minute) {
        Calendar cal = new java.util.GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        rb_date.setChecked(true);
        java.text.DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        time=dateFormat.format(cal.getTime());
        btn_done.setText("Time set: " + dateFormat.format(cal.getTime()));
        Show_calander();
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = new java.util.GregorianCalendar();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        java.text.DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        date=dateFormat.format(cal.getTime());
        String datenew=date+" "+time;
        btn_done.setText("Time set: " + datenew);
        Show_Dialog_booking(datenew,book_vehicleid,book_amount,book_Driver_ID,book_vehicle_no,book_vehicle_image,book_refreshtoken,book_vehicle_type_id);

    }

    private void Show_Dialog_booking(final String datenew,final  String book_vehicleid,final  String book_amount,final  String book_Driver_ID,
                                     final String book_vehicle_no,final  String book_vehicle_image,final  String book_refreshtoken,
                                     final String book_vehicle_type_id) {

        bottm_dialogforconfirmbooking = new BottomSheetDialog(From_Location.this);
        //final Dialog dialog = new Dialog(new ContextThemeWrapper(this, R.style.DialogSlideAnim));
        LayoutInflater inflater=this.getLayoutInflater();
        View v=inflater.inflate(R.layout.dialog_ride_book,null);
        bottm_dialogforconfirmbooking.setCancelable(true);
        // getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        CircleImageView vehi_typ_img=v.findViewById(R.id.vehicle_type_img);
        CircleImageView vehi_img=v.findViewById(R.id.vehicle_img);

        TextView txt_amount=v.findViewById(R.id.txt_amount);
        txt_timer_value=v.findViewById(R.id.txt_timer);
        TextView txt_book_type=v.findViewById(R.id.txt_book_type);
        TextView txt_vehicle_no=v.findViewById(R.id.txt_vehicle_no);
        TextView txt_vehicle_type=v.findViewById(R.id.txt_vehicle_type);
        TextView txt_from=v.findViewById(R.id.txt_from_add);
        TextView txt_to=v.findViewById(R.id.txt_to_add);
        TextView txt_date=v.findViewById(R.id.txt_date);
        show_dialog_btn_submit=v.findViewById(R.id.txt_book_done);
        txt_from.setText(et_location.getText().toString().trim());
        txt_to.setText(et_location2.getText().toString().trim());
        txt_date.setText(datenew);
        txt_vehicle_no.setText(book_vehicle_no);
        txt_book_type.setText(Book_status);
        txt_vehicle_type.setText(get_vehicle_type);

        DecimalFormat df2 = new DecimalFormat("#.##");
        if (book_amount != null) {
            txt_amount.setText(df2.format(Double.valueOf(book_amount)));
        } else {
            txt_amount.setText("0.00");
        }
        Picasso.with(From_Location.this).load(get_Vehicle_icon).into(vehi_typ_img);
        Picasso.with(From_Location.this).load(book_vehicle_image).into(vehi_img);
        bottm_dialogforconfirmbooking.setContentView(v);
        show_dialog_btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (App_Utils.isNetworkAvailable(From_Location.this)){
                    Check_booking_status=false;

                    get_driver_token(datenew,book_vehicleid,book_amount,book_Driver_ID,book_vehicle_no,book_vehicle_image,book_refreshtoken,book_vehicle_type_id,Book_status);

                }else{
                    Toast.makeText(From_Location.this, "No Internet", Toast.LENGTH_SHORT).show();
                }

            }
        });
        bottm_dialogforconfirmbooking.show();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                findViewById(R.id.fab).requestFocus();
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                findViewById(R.id.radioGroup).requestFocus();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_help) {
            startActivity(new Intent(From_Location.this,Trip_free.class));
        }else if (id == R.id.nav_notification) {
            startActivity(new Intent(From_Location.this,Notification.class));
        } else if (id == R.id.nav_setting) {
            startActivity(new Intent(From_Location.this,Acc_setting.class));
        } else if (id == R.id.nav_your_trips) {
            startActivity(new Intent(From_Location.this,Trip.class));
        }else if (id == R.id.nav_logout) {
            creatrdialogbox_for_logout();
        }else if (id ==R.id.nav_wallet)
        {
            startActivity(new Intent(From_Location.this,Wallet_page.class));
        }
        // coordinatorLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        coordinatorLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void creatrdialogbox_for_logout()
    {
        final Dialog dialog = App_Utils.createDialog(From_Location.this, false);
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
                    startActivity(new Intent(From_Location.this,login_mobile.class).
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
}
