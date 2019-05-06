package aaronsoftech.in.uber.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import aaronsoftech.in.uber.Fragment.Trip_Upcomming;
import aaronsoftech.in.uber.Fragment.Trip_past;

/**
 * Created by Chouhan on 05/06/2019.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        String fragment_type;
        switch (position) {
            case 0:
                /*set In_Trail fragment in viewpager*/
                Trip_past tab1 = new Trip_past();
                return tab1;
            case 1:
                /*set Trail_pending fragment in viewpager*/
                Trip_Upcomming tab2 = new Trip_Upcomming();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
