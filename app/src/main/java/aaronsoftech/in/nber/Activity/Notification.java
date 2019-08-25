package aaronsoftech.in.nber.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import aaronsoftech.in.nber.Adapter.Adapter_notification;
import aaronsoftech.in.nber.Adapter.Adapter_user_list;
import aaronsoftech.in.nber.App_Conteroller;
import aaronsoftech.in.nber.POJO.Response_Booking_List;
import aaronsoftech.in.nber.R;
import aaronsoftech.in.nber.Service.APIClient;
import aaronsoftech.in.nber.Utils.SP_Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static aaronsoftech.in.nber.Fragment.Trip_Upcomming.set_data_in_alist;
import static aaronsoftech.in.nber.Utils.App_Utils.isNetworkAvailable;

public class Notification extends AppCompatActivity {

    static RecyclerView recyclerView;
    String TAG="Trip_past";
    ProgressDialog progressDialog;
    public static List<aaronsoftech.in.nber.POJO.Notification.send_data> get_Notification_List=new ArrayList<>();
    public static List<aaronsoftech.in.nber.POJO.Notification.send_data> get_Notification_List_User=new ArrayList<>();
    public static List<aaronsoftech.in.nber.POJO.Notification.send_data> get_Notification_List_Driver=new ArrayList<>();

    String language,intent_noti;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        ImageView btn_back=findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.recycle_past_trips);
        // Adapter_past adapter_past=new Adapter_past(getActivity(),TAG);
        // set a StaggeredGridLayoutManager with 1 number of columns and vertical orientation
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager); // set LayoutManager to RecyclerView

        get_Notification_List.clear();
        if (App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase("null")
                || App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase(null)
                || App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"").equalsIgnoreCase(""))
        {
            String userid= App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_ID,"");
            Call_user_book_Api(userid);
        }else{
            String driverid= App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"");
            Call_driver_book_Api(driverid);
        }
    }



    private void Call_user_book_Api(String driver_ID) {
        progressDialog=new ProgressDialog(Notification.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        HashMap map= new HashMap<>();
        map.put("user_id",driver_ID);
        if (isNetworkAvailable(Notification.this))
        {
            Call<aaronsoftech.in.nber.POJO.Notification> call= APIClient.getWebServiceMethod().notification_get_user(map);
            call.enqueue(new Callback<aaronsoftech.in.nber.POJO.Notification>() {
                @Override
                public void onResponse(Call<aaronsoftech.in.nber.POJO.Notification> call, Response<aaronsoftech.in.nber.POJO.Notification> response)
                {
                    progressDialog.dismiss();
                    try{
                        String status=response.body().getApi_status();
                        String msg=response.body().getApi_message();
                        if (status.equalsIgnoreCase("1") && msg.equalsIgnoreCase("success") )
                        {

                            get_Notification_List=response.body().getData();
                            Adapter_notification aa=new Adapter_notification(Notification.this,get_Notification_List);
                            recyclerView.setAdapter(aa);

                        }else{
                            //                     Toast.makeText(getContext(), "status "+status+"\n"+"msg "+msg, Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();}
                }

                @Override
                public void onFailure(Call<aaronsoftech.in.nber.POJO.Notification> call, Throwable t) {
                    Toast.makeText(Notification.this, "Error : "+t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(Notification.this, "No Internet", Toast.LENGTH_SHORT).show();
        }

    }


    private void Call_driver_book_Api(String driver_ID) {
        progressDialog=new ProgressDialog(Notification.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        HashMap map= new HashMap<>();
        map.put("driver_id",driver_ID);
        if (isNetworkAvailable(Notification.this))
        {
            Call<aaronsoftech.in.nber.POJO.Notification> call= APIClient.getWebServiceMethod().notification_get_driver(map);
            call.enqueue(new Callback<aaronsoftech.in.nber.POJO.Notification>() {
                @Override
                public void onResponse(Call<aaronsoftech.in.nber.POJO.Notification> call, Response<aaronsoftech.in.nber.POJO.Notification> response) {
                    progressDialog.dismiss();
                    try{
                        String status=response.body().getApi_status();
                        String msg=response.body().getApi_message();
                        if (status.equalsIgnoreCase("1") && msg.equalsIgnoreCase("success") )
                        {
                            get_Notification_List=response.body().getData();
                            Adapter_notification aa=new Adapter_notification(Notification.this,get_Notification_List);
                            recyclerView.setAdapter(aa);
                        }else{

                            Toast.makeText(Notification.this, "status "+status+"\n"+"msg "+msg, Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                         Toast.makeText(Notification.this, "Server error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();}
                }

                @Override
                public void onFailure(Call<aaronsoftech.in.nber.POJO.Notification> call, Throwable t) {
                    Toast.makeText(Notification.this, "Error : "+t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(Notification.this, "No Internet", Toast.LENGTH_SHORT).show();
        }

    }


}
