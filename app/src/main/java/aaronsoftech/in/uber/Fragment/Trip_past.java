package aaronsoftech.in.nber.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import aaronsoftech.in.nber.Adapter.Adapter_past;
import aaronsoftech.in.nber.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Trip_past extends Fragment {
    static RecyclerView recyclerView;
    String TAG="Trip_past";
    public Trip_past() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_trip_past, container, false);
        recyclerView = (RecyclerView)v. findViewById(R.id.recycle_past_trips);
        Adapter_past adapter_past=new Adapter_past(getActivity(),TAG);
        // set a StaggeredGridLayoutManager with 1 number of columns and vertical orientation
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager); // set LayoutManager to RecyclerView
        //call the constructor of CustomAdapter to send the reference and data to Adapter; // set the Adapter to RecyclerView
        /* Todo----Call api and get kiranas data*/
        recyclerView.setAdapter(adapter_past);
        return v;
    }

}
