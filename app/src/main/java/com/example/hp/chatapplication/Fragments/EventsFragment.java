package com.example.hp.chatapplication.Fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hp.chatapplication.Adapter.EventPagerAdapter1;
import com.example.hp.chatapplication.R;


public class EventsFragment extends Fragment {

    ViewPager simpleViewPager;
    TabLayout tabLayout;

    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        // get the reference of ViewPager and TabLayout
        simpleViewPager = (ViewPager) view.findViewById(R.id.eventViewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.eventTabLayout);
// Create a new Tab named "First"
        TabLayout.Tab firstTab = tabLayout.newTab();

        firstTab.setText("Birthday"); // set the Text for the first Tab
// first tab
        tabLayout.addTab(firstTab); // add  the tab at in the TabLayout
// Create a new Tab named "Second"
        TabLayout.Tab secondTab = tabLayout.newTab();
        secondTab.setText("Anniversary"); // set the Text for the second Tab
        tabLayout.addTab(secondTab); // add  the tab  in the TabLayout
// Create a new Tab named "Third"
        TabLayout.Tab thirdTab = tabLayout.newTab();
        thirdTab.setText("Holidays"); // set the Text for the first Tab
        tabLayout.addTab(thirdTab); // add  the tab at in the TabLayout

        EventPagerAdapter1 adapter = new EventPagerAdapter1(getFragmentManager(), tabLayout.getTabCount());
        simpleViewPager.setAdapter(adapter);
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


        return view;/*  tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tabLayout.getSelectedTabPosition() == 0){
                    //  Toast.makeText(MainActivity.this, "Tab " + tabLayout.getSelectedTabPosition(), Toast.LENGTH_LONG).show();

                }
                else if(tabLayout.getSelectedTabPosition() == 1){
                    // Toast.makeText(MainActivity.this, "Tab " + tabLayout.getSelectedTabPosition(), Toast.LENGTH_LONG).show();
                    Fragment fragment=new AnniversaryFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.eventViewPager,fragment).addToBackStack(null).commit();

                }

                else if(tabLayout.getSelectedTabPosition() == 2){
                    // Toast.makeText(MainActivity.this, "Tab " + tabLayout.getSelectedTabPosition(), Toast.LENGTH_LONG).show();
                    Fragment fragment=new HolidaysFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.eventViewPager,fragment).addToBackStack(null).commit();

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/

    }

}
