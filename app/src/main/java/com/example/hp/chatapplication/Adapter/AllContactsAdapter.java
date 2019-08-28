package com.example.hp.chatapplication.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hp.chatapplication.ModelClasses.ContactVO;
import com.example.hp.chatapplication.R;

import java.util.List;

public class AllContactsAdapter extends RecyclerView.Adapter<AllContactsAdapter.ContactViewHolder> {

    private List<ContactVO> contactVOList;
    private Context mContext;

    public AllContactsAdapter(List<ContactVO> contactVOList, Context mContext) {
        this.contactVOList = contactVOList;
        this.mContext = mContext;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_contact_view, null);
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        ContactVO contactVO = contactVOList.get(position);
        holder.tvContactName.setText(contactVO.getContactName());
        holder.tvPhoneNumber.setText(contactVO.getContactNumber());

        if (contactVO.getContactImage() == null) {
            holder.ivContactImage.setImageResource(R.drawable.ic_person_black_24dp);

        } else {
            Glide.with(mContext).load(contactVO.getContactImage()).into(holder.ivContactImage);

        }

        //  Glide.with(mContext).load(contactVO.getContactImage()).into(holder.ivContactImage);
    }

    @Override
    public int getItemCount() {
        return contactVOList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        ImageView ivContactImage;
        TextView tvContactName;
        TextView tvPhoneNumber;

        public ContactViewHolder(View itemView) {
            super(itemView);
            ivContactImage = (ImageView) itemView.findViewById(R.id.ivContactImage);
            tvContactName = (TextView) itemView.findViewById(R.id.tvContactName);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.tvPhoneNumber);
        }
    }
}