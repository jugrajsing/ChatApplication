package com.example.hp.chatapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

public class ListUsersAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<QBUser> qbUserArrayList;

    public ListUsersAdapter(Context context, ArrayList<QBUser> qbUserArrayList) {
        this.context = context;
        this.qbUserArrayList = qbUserArrayList;
    }
    @Override
    public int getCount() {
        return qbUserArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return qbUserArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        if (view==null){

            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(android.R.layout.simple_list_item_multiple_choice,null);
            TextView textView=(TextView)view.findViewById(android.R.id.text1);
            textView.setText(qbUserArrayList.get(position).getLogin());

         /*   view=inflater.inflate(R.layout.user_list_custom_layout,null);
            TextView textView=(TextView)view.findViewById(R.id.user_list_my_custom);
            textView.setText(qbUserArrayList.get(position).getLogin());*/

        }
        return view;
    }

}
