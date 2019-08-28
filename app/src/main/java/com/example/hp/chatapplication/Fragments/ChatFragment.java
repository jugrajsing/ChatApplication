package com.example.hp.chatapplication.Fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hp.chatapplication.Adapter.GroupChannelListFragment;
import com.example.hp.chatapplication.R;
import com.example.hp.chatapplication.openchannel.OpenChannelListFragment;

/**
 * A simple {@link Fragment} subclass.
 */

public class ChatFragment extends Fragment {

    View view;
    TabLayout tabLayout;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.chat_fragment, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        // init();
        includetabs();
        return view;
    }

    private void includetabs() {
        tabLayout.addTab(tabLayout.newTab().setText("Private Chat"));
        tabLayout.setTabTextColors(ContextCompat.getColorStateList(getActivity(), R.color.tab_black));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.color_white));
        tabLayout.addTab(tabLayout.newTab().setText("Public Chat"));
        Fragment fragment = new GroupChannelListFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_group_channel, fragment).addToBackStack(null).commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() == 0) {
                    //  Toast.makeText(MainActivity.this, "Tab " + tabLayout.getSelectedTabPosition(), Toast.LENGTH_LONG).show();
                    Fragment fragment = new GroupChannelListFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_group_channel, fragment).addToBackStack(null).commit();

                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    // Toast.makeText(MainActivity.this, "Tab " + tabLayout.getSelectedTabPosition(), Toast.LENGTH_LONG).show();
                    Fragment fragment = new OpenChannelListFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_group_channel, fragment).addToBackStack(null).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}
