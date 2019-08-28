package com.example.hp.chatapplication.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.hp.chatapplication.R;
public class SearchFragments extends Fragment {
    public SearchFragments() {
        // Required empty public constructor
    }
    Button sign_in_page_button,sign_up_button;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_search_fragments, container, false);

       sign_in_page_button=(Button)view.findViewById(R.id.sign_in_page_button);
       sign_in_page_button.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              sign_up_button.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.signin_button_voilet));
              sign_up_button.setTextColor((Color.parseColor("#FFFFFF")));
              sign_in_page_button.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.signin_button));
              sign_in_page_button.setTextColor((Color.parseColor("#000000")));

              NormalSearchFragment advanceSearchFragment = new NormalSearchFragment();
              fragmentManager = getFragmentManager();
              fragmentTransaction = fragmentManager.beginTransaction();
              fragmentTransaction.replace(R.id.container_farme, advanceSearchFragment);
              fragmentTransaction.commit();

          }
        });

        sign_up_button=(Button)view.findViewById(R.id.sign_up_button);
        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                sign_up_button.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.signin_button));
                sign_in_page_button.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.signin_button_voilet));
                sign_up_button.setTextColor((Color.parseColor("#000000")));
                sign_in_page_button.setTextColor((Color.parseColor("#FFFFFF")));
               // Sign_in_SignUp_text.setText("Sign In");

                AdvanceSearchFragment advanceSearchFragment = new AdvanceSearchFragment();
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_farme, advanceSearchFragment);
                fragmentTransaction.commit();


            }
        });


        NormalSearchFragment advanceSearchFragment = new NormalSearchFragment();
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_farme, advanceSearchFragment);
        fragmentTransaction.commit();
       return view;

    }
}