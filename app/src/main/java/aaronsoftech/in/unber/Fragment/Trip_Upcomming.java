package aaronsoftech.in.unber.Fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import aaronsoftech.in.unber.Adapter.Adapter_user_list;
import aaronsoftech.in.unber.POJO.Response_Booking_List;
import aaronsoftech.in.unber.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Trip_Upcomming extends Fragment implements Adapter_user_list.Vehicle_Item_listner{
    static Context conu;
    static RecyclerView recyclerView_upcomming;
    public Trip_Upcomming() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_trip__upcomming, container, false);

        try{
            recyclerView_upcomming = (RecyclerView)v. findViewById(R.id.recycle_upcomming);
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
            recyclerView_upcomming.setLayoutManager(staggeredGridLayoutManager);

        }catch (Exception e){e.printStackTrace();}

        return v;
    }

    public static void set_data_in_alist(List<Response_Booking_List.User_List> get_Booking_List_upcomming)
    {
        Adapter_user_list aa=new Adapter_user_list(((Activity)conu),get_Booking_List_upcomming, (Adapter_user_list.Vehicle_Item_listner) conu);
        recyclerView_upcomming.setAdapter(aa);
    }

    @Override
    public void OnClick_item(Response_Booking_List.User_List user_list) {

    }
}
