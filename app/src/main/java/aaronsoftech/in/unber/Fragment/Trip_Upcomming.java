package aaronsoftech.in.unber.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import aaronsoftech.in.unber.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Trip_Upcomming extends Fragment {


    public Trip_Upcomming() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trip__upcomming, container, false);
    }

}
