package com.example.hp.chatapplication.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.hp.chatapplication.Fragments.AnniversaryFragment;
import com.example.hp.chatapplication.Fragments.BirthdayFragment;
import com.example.hp.chatapplication.Fragments.HolidaysFragment;

public class EventPagerAdapter1 extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public EventPagerAdapter1(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                BirthdayFragment tab1 = new BirthdayFragment();
                return tab1;
            case 1:
                AnniversaryFragment tab2 = new AnniversaryFragment();
                return tab2;
            case 2:
                HolidaysFragment tab3 = new HolidaysFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
