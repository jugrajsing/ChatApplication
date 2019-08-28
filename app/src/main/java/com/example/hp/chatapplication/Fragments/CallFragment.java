package com.example.hp.chatapplication.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.hp.chatapplication.Adapter.CallingAdapter;
import com.example.hp.chatapplication.ModelClasses.Callhistory_Model;
import com.example.hp.chatapplication.R;
import com.example.hp.chatapplication.VideoCall.adapters.OpponentsAdapter;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

public class CallFragment extends Fragment {

    private static final String TAG = "Call Fragment";
    private OpponentsAdapter opponentsAdapter;
    private ListView opponentsListView;
    private QBUser currentUser;
    private ArrayList<Callhistory_Model> calllist;

    private RecyclerView call_history_recycler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_call, container, false);
        call_history_recycler = rootView.findViewById(R.id.call_history_recycler);

        calllist = new ArrayList<>();
        //  calllist.add(new Callhistory_Model())
        calllist.add(new Callhistory_Model("Vivek Singh", "Just Now, 13:24 pm", "Just Now, 13:24 pm"));
        calllist.add(new Callhistory_Model("Prashant", "14 Decemember, 14:19 pm", "15 December, 14:57 pm"));
        calllist.add(new Callhistory_Model("Zakir", "14 Decemember, 18:19 pm", "15 November, 18:57 pm"));
        calllist.add(new Callhistory_Model("Kamal", "16 Decemember, 19:17 pm", "15 January, 19:57 pm"));
        CallingAdapter callingAdapter = new CallingAdapter(getContext(), calllist);
        //  call_history_recycler.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        call_history_recycler.setLayoutManager(layoutManager);
        call_history_recycler.setAdapter(callingAdapter);

        return rootView;
    }


}
