package com.svmindlogic.tiffin.tiffinmap.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.svmindlogic.tiffin.tiffinmap.ListAddressDetails;
import com.svmindlogic.tiffin.tiffinmap.model.Listmap_model;
import com.svmindlogic.tiffin.tiffinmap.utils.CommonMethods;
import com.svmindlogic.tiffinmap.R;

import java.util.ArrayList;

/**
 * Created by pappu on 20/12/15.
 */
public class Listmap_Adapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    Listmap_model mFriend;
    ArrayList<Listmap_model> listOfFreinds;

    public Listmap_Adapter(Context context, ArrayList<Listmap_model> arraylist) {
        this.context = context;
        this.listOfFreinds = arraylist;
    }

    @Override
    public int getCount() {
        return listOfFreinds.size();
    }

    @Override
    public Object getItem(int position) {
        return listOfFreinds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_address_list_row, parent, false);
            holder = new ViewHolder();

            holder.txtMessName = (TextView) convertView.findViewById(R.id.txt_list_mess_name);
            holder.txtDistance = (TextView) convertView.findViewById(R.id.txt_list_distance);
            holder.txtDistanceKm = (TextView) convertView.findViewById(R.id.txt_list_distance_km);
            holder.txtFullTiffin = (TextView) convertView.findViewById(R.id.txt_list_full_tiffin);
            holder.txtFullTiffinPrize = (TextView) convertView.findViewById(R.id.txt_list_full_tiffin_prize);
            holder.txtMiniTiffin = (TextView) convertView.findViewById(R.id.txt_list_half_tiffin);
            holder.txtMiniTiffinPrize = (TextView) convertView.findViewById(R.id.txt_list_half_tiffin_prize);

            holder.imgDeleveryOption = (ImageView) convertView.findViewById(R.id.img_list_deliver_options);
            holder.imgMessType = (ImageView) convertView.findViewById(R.id.img_list_veg_nonveg);
            holder.imgMessCall = (ImageView) convertView.findViewById(R.id.imd_list_mess_call);

            holder.llMessData = (LinearLayout) convertView.findViewById(R.id.ll_mess_data);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        mFriend = listOfFreinds.get(position);
        /** set data to list item **/
        holder.txtMessName.setText(Html.fromHtml(mFriend.getBusiness_name()));
        holder.txtDistanceKm.setText(Html.fromHtml(mFriend.getDistance() + " " + "Km"));
        holder.txtFullTiffinPrize.setText(Html.fromHtml(mFriend.getFull_tiffin_prize() + " " + "Rs"));
        holder.txtMiniTiffinPrize.setText(Html.fromHtml(mFriend.getHalf_tiffin_prize() + " " + "Rs"));
        String f = mFriend.getMobile();
        String Delevery_Option = mFriend.getDelivery_option();
        String Mess_Type = mFriend.getFood_type();

        String delivary = mFriend.getDelivery_option();
        String messtype = mFriend.getFood_type();
        String phone = mFriend.getMobile();


        if (delivary.equals("Yes")) {
            holder.imgDeleveryOption.setVisibility(View.VISIBLE);
            holder.imgDeleveryOption.setImageResource(R.drawable.ic_delivary);
        } else {
            holder.imgDeleveryOption.setVisibility(View.VISIBLE);
            holder.imgDeleveryOption.setImageResource(R.drawable.ic_notdelevery);
        }

        if (messtype.equals("Veg")) {
            holder.imgMessType.setVisibility(View.VISIBLE);
            holder.imgMessType.setImageResource(R.drawable.ic_veg);
        } else {
            holder.imgMessType.setVisibility(View.VISIBLE);
            holder.imgMessType.setImageResource(R.drawable.ic_non_veg);
        }

        /** font apply **/
        CommonMethods.setCustomFontCANADARA(context, holder.txtMessName);
        //CommonMethods.setCustomFontCANADARA(context, holder.txtDistanceKm);
        // CommonMethods.setCustomFontCANADARA(context, holder.txtFullTiffinPrize);
        // CommonMethods.setCustomFontCANADARA(context, holder.txtMiniTiffinPrize);
        CommonMethods.setCustomFontCANADARA(context, holder.txtDistance);
        CommonMethods.setCustomFontCANADARA(context, holder.txtFullTiffin);
        CommonMethods.setCustomFontCANADARA(context, holder.txtMiniTiffin);

        /** Click on call row button  **/
        holder.imgMessCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFriend = listOfFreinds.get(position);
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mFriend.getMobile()));
                context.startActivity(intent);
            }
        });

        /** Click on listview row **/
        holder.llMessData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                /** Get the position to pass the data **/
                mFriend = listOfFreinds.get(position);
                Intent intent = new Intent(context, ListAddressDetails.class);
                intent.putExtra("Mess_name", mFriend.getBusiness_name());
                intent.putExtra("Mess_address", mFriend.getAddress());
                intent.putExtra("Mess_phone", mFriend.getMobile());
                intent.putExtra("Mess_openat", mFriend.getOpens_at());
                intent.putExtra("Mess_closeat", mFriend.getClosed_at());
                intent.putExtra("Mess_sunday", mFriend.getSunday_close());
                intent.putExtra("Mess_foodtype", mFriend.getFood_type());
                intent.putExtra("Mess_kitchen", mFriend.getKitchen_type());
                intent.putExtra("Mess_delevery", mFriend.getDelivery_option());
                intent.putExtra("Mess_full", mFriend.getFull_tiffin_prize());
                intent.putExtra("Mess_dhalf", mFriend.getHalf_tiffin_prize());
                intent.putExtra("Mess_onetime", mFriend.getOneTime());
                intent.putExtra("Mess_twotime", mFriend.getTwoTime());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    /**
     * Holder class
     **/
    private class ViewHolder {
        TextView txtMessName, txtDistance, txtDistanceKm, txtFullTiffin, txtFullTiffinPrize, txtMiniTiffin, txtMiniTiffinPrize;
        ImageView imgDeleveryOption, imgMessType, imgMessCall;
        LinearLayout llMessData;

    }
}

