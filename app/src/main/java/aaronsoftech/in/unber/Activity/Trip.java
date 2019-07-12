package aaronsoftech.in.unber.Activity;

import android.app.ProgressDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import aaronsoftech.in.unber.Adapter.PagerAdapter;
import aaronsoftech.in.unber.App_Conteroller;
import aaronsoftech.in.unber.POJO.Response_Booking_List;
import aaronsoftech.in.unber.R;
import aaronsoftech.in.unber.Service.APIClient;
import aaronsoftech.in.unber.Utils.SP_Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static aaronsoftech.in.unber.Utils.App_Utils.isNetworkAvailable;


public class Trip extends AppCompatActivity {

    static ViewPager simpleViewPager;
    static TabLayout tabLayout;
    static PagerAdapter adapter;
    public static Trip trip_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        trip_activity=this;
        ImageView back_btn=(ImageView)findViewById(R.id.btn_back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        simpleViewPager = (ViewPager) findViewById(R.id.simpleViewPager);
        tabLayout = (TabLayout) findViewById(R.id.simpleTabLayout);
        // Todo: Create a new Tab named "Past Trips"
        TabLayout.Tab firstTab = tabLayout.newTab();
        firstTab.setText("Past");
        // set the Text for the first Tab
        // first tab
        tabLayout.addTab(firstTab); // add  the tab at in the TabLayout
        // Todo:  Create a new Tab named "Upcomming Trips"
        TabLayout.Tab secondTab = tabLayout.newTab();
        secondTab.setText("Upcomming");
        // set the Text for the second Tab
        tabLayout.addTab(secondTab); // add  the tab  in the TabLayout

        // addOnPageChangeListener event change the tab on slide
        simpleViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                simpleViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //todo --set all fragment  in view pager
        adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        simpleViewPager.setAdapter(adapter);



    }




}
