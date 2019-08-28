package com.example.hp.chatapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.hp.chatapplication.R;
import com.quickblox.chat.model.QBChatDialog;

import java.util.ArrayList;

public class ChatDialogsAdapter extends BaseAdapter {

    TextView text_title, text_message;
    ImageView imageView;
    private Context context;
    private ArrayList<QBChatDialog> qbChatDialogs;


    public ChatDialogsAdapter(Context context, ArrayList<QBChatDialog> qbChatDialogs) {
        this.context = context;
        this.qbChatDialogs = qbChatDialogs;
    }

    @Override
    public int getCount() {
        return qbChatDialogs.size();
    }

    @Override
    public Object getItem(int position) {
        return qbChatDialogs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_chat_dialog, null);


            text_message = (TextView) view.findViewById(R.id.list_chat_dailog_message);
            text_title = (TextView) view.findViewById(R.id.list_chat_dailog_title);
            imageView = (ImageView) view.findViewById(R.id.image_chat_dialog);

            text_message.setText(qbChatDialogs.get(position).getLastMessage());
            text_title.setText(qbChatDialogs.get(position).getName());

            ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
            int randamColor = colorGenerator.getRandomColor();

            TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig().withBorder(4).endConfig().round();

            //get first character from chat dialog title for create chat dialog image
            TextDrawable drawable = builder.build(text_title.getText().toString().substring(0, 1).toUpperCase(), randamColor);
            imageView.setImageDrawable(drawable);

        }

        return view;
    }
}
