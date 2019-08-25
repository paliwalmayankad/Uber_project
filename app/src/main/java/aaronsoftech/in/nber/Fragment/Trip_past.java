package aaronsoftech.in.nber.Fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class Trip_past extends Fragment{

    static RecyclerView recyclerView;
    String TAG="Trip_past";
    ProgressDialog progressDialog;
    public static List<Response_Booking_List.User_List> get_Booking_List=new ArrayList<>();
    public static List<Response_Booking_List.User_List> get_Booking_List_past=new ArrayList<>();
    public static List<Response_Booking_List.User_List> get_Booking_List_upcomming=new ArrayList<>();

    Context con;
    public Trip_past() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_trip_past, container, false);
        recyclerView = (RecyclerView)v. findViewById(R.id.recycle_past_trips);
       // Adapter_past adapter_past=new Adapter_past(getActivity(),TAG);
        // set a StaggeredGridLayoutManager with 1 number of columns and vertical orientation
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager); // set LayoutManager to RecyclerView
        //call the constructor of CustomAdapter to send the reference and data to Adapter; // set the Adapter to RecyclerView
       // recyclerView.setAdapter(adapter_past);
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


        return v;
    }

    private void Call_user_book_Api(String driver_ID) {
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        get_Booking_List.clear();
        get_Booking_List_past.clear();
        get_Booking_List_upcomming.clear();

        HashMap map= new HashMap<>();
        map.put("user_id",driver_ID);
        if (isNetworkAvailable(getContext()))
        {
            Call<Response_Booking_List> call= APIClient.getWebServiceMethod().get_User_Booking(map);
            call.enqueue(new Callback<Response_Booking_List>() {
              @Override
                public void onResponse(Call<Response_Booking_List> call, Response<Response_Booking_List> response)
                {
                    progressDialog.dismiss();
                    try{
                        String status=response.body().getApi_status();
                        String msg=response.body().getApi_message();
                        if (status.equalsIgnoreCase("1") && msg.equalsIgnoreCase("success") )
                        {
                            if (response.body().getData()!=null)
                             {
                                 get_Booking_List=response.body().getData();
                                 for (int i=0;i<get_Booking_List.size();i++)
                                 {
                                     if (get_Booking_List.get(i).getPickup().toString().equalsIgnoreCase("Now"))
                                     {
                                         get_Booking_List_past.add(get_Booking_List.get(i));
                                     }else{
                                         get_Booking_List_upcomming.add(get_Booking_List.get(i));
                                     }
                                 }
                                 set_data_in_alist(get_Booking_List_upcomming);

                                 Adapter_user_list aa=new Adapter_user_list(((Activity)con),get_Booking_List_past, (Adapter_user_list.Vehicle_Item_listner) con);
                                 recyclerView.setAdapter(aa);
                             }

                        }else{
       //                     Toast.makeText(getContext(), "status "+status+"\n"+"msg "+msg, Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();}
                }

                @Override
                public void onFailure(Call<Response_Booking_List> call, Throwable t) {
                    Toast.makeText(getContext(), "Error : "+t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(getContext(), "No Internet", Toast.LENGTH_SHORT).show();
        }

    }


    private void Call_driver_book_Api(String driver_ID) {
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        get_Booking_List.clear();
        get_Booking_List_past.clear();
        get_Booking_List_upcomming.clear();
        HashMap map= new HashMap<>();
        map.put("driver_id",driver_ID);
        if (isNetworkAvailable(getContext()))
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

                            if (response.body().getData()!=null)
                            {
                                get_Booking_List=response.body().getData();
                                for (int i=0;i<get_Booking_List.size();i++)
                                {
                                    if (get_Booking_List.get(i).getPickup().toString().equalsIgnoreCase("Now"))
                                    {
                                        get_Booking_List_past.add(get_Booking_List.get(i));
                                    }else{
                                        get_Booking_List_upcomming.add(get_Booking_List.get(i));
                                    }
                                }
                                set_data_in_alist(get_Booking_List_upcomming);

                                Adapter_user_list aa=new Adapter_user_list(((Activity)con),get_Booking_List_past, (Adapter_user_list.Vehicle_Item_listner) con);
                                recyclerView.setAdapter(aa);
                            }


                        }else{

                            Toast.makeText(getContext(), "status "+status+"\n"+"msg "+msg, Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
     //                   Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();}
                }

                @Override
                public void onFailure(Call<Response_Booking_List> call, Throwable t) {
                    Toast.makeText(getContext(), "Error : "+t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(getContext(), "No Internet", Toast.LENGTH_SHORT).show();
        }

    }



}
