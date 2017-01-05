package com.svmindlogic.tiffin.tiffinmap;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.svmindlogic.tiffin.tiffinmap.utils.CommonMethods;
import com.svmindlogic.tiffinmap.R;

/**
 * Created by pappu on 20/12/15.
 */
public class ListAddressDetails extends AppCompatActivity {
    TextView txtPhone, txtAddress, txtOpenAt, txtOpenAtTime,
            txtCloseAt, txtCloseAtTime, txtSundayOpen, txtSundayOpenOption,
            txtFood, txtFoodType, txtKitchen, txtKitchenType, txtDelevery,
            txtDeleveryOption, txtFullTiffin, txtFullTiffinPrize, txtHalfTiffin, txtHalfTiffinPrize,
            txtOneTimeMess, txtoneTime, txtTwoTimeMess, txtTwotime;

    LinearLayout llCall;
    String Phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list_details);

        /**Add Custom Action Bar **/
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        /**Add Custom Action Bar .xml **/
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);

        /** Add Views of Custom Action Bar **/
        TextView txtHeaderName = (TextView) findViewById(R.id.txt_headers_name);
        ImageView imgHeaderImage = (ImageView) findViewById(R.id.img_headers_icon);
        imgHeaderImage.setVisibility(View.VISIBLE);


        txtPhone = (TextView) findViewById(R.id.txt_details_view_number);
        txtAddress = (TextView) findViewById(R.id.txt_details_view_address);
        txtOpenAt = (TextView) findViewById(R.id.txt_details_view_open);
        txtOpenAtTime = (TextView) findViewById(R.id.txt_details_view_open_time);
        txtCloseAt = (TextView) findViewById(R.id.txt_details_view_close);
        txtCloseAtTime = (TextView) findViewById(R.id.txt_details_view_close_time);
        txtSundayOpen = (TextView) findViewById(R.id.txt_details_view_sunday);
        txtSundayOpenOption = (TextView) findViewById(R.id.txt_details_view_sunday_close);
        txtFood = (TextView) findViewById(R.id.txt_details_view_food);
        txtFoodType = (TextView) findViewById(R.id.txt_details_view_food_type);
        txtKitchen = (TextView) findViewById(R.id.txt_details_view_kichan);
        txtKitchenType = (TextView) findViewById(R.id.txt_details_view_kichan_type);
        txtDelevery = (TextView) findViewById(R.id.txt_details_view_delivary);
        txtDeleveryOption = (TextView) findViewById(R.id.txt_details_view_delivary_option);
        txtFullTiffin = (TextView) findViewById(R.id.txt_details_view_full);
        txtFullTiffinPrize = (TextView) findViewById(R.id.txt_details_view_full_prize);
        txtHalfTiffin = (TextView) findViewById(R.id.txt_details_view_half);
        txtHalfTiffinPrize = (TextView) findViewById(R.id.txt_details_view_half_prize);

        txtOneTimeMess = (TextView) findViewById(R.id.txt_details_view_one);
        txtoneTime = (TextView) findViewById(R.id.txt_details_view_one_price);
        txtTwoTimeMess = (TextView) findViewById(R.id.txt_details_view_two);
        txtTwotime = (TextView) findViewById(R.id.txt_details_view_two_price);
        llCall = (LinearLayout) findViewById(R.id.ll_call);


        Intent i = getIntent();
        String Mess_Name = i.getStringExtra("Mess_name");
        String Mess_Add = i.getStringExtra("Mess_address");
        Phone = i.getStringExtra("Mess_phone");
        String Open = i.getStringExtra("Mess_openat");
        String close = i.getStringExtra("Mess_closeat");
        String sunday = i.getStringExtra("Mess_sunday");
        String food = i.getStringExtra("Mess_foodtype");
        String kitchen = i.getStringExtra("Mess_kitchen");
        String delevery = i.getStringExtra("Mess_delevery");
        String full = i.getStringExtra("Mess_full");
        String half = i.getStringExtra("Mess_dhalf");
        String one = i.getStringExtra("Mess_onetime");
        String two = i.getStringExtra("Mess_twotime");

        /** Set text **/
        txtHeaderName.setText(Mess_Name);
        txtPhone.setText(Phone);
        txtAddress.setText(Mess_Add);
        txtOpenAtTime.setText(Open);
        txtCloseAtTime.setText(close);
        txtSundayOpenOption.setText(sunday);
        txtFoodType.setText(food);
        txtKitchenType.setText(kitchen);
        txtDeleveryOption.setText(delevery);
        txtFullTiffinPrize.setText(full + "  " + "Rs");
        txtHalfTiffinPrize.setText(half + " " + "Rs");

        txtoneTime.setText(one + "  " + "Rs");
        txtTwotime.setText(two + " " + "Rs");


        /** Font apply**/
        CommonMethods.setCustomFontCANADARA(ListAddressDetails.this, txtHeaderName);
        //CommonMethods.setCustomFontCANADARA(ListAddressDetails.this, txtPhone);
        CommonMethods.setCustomFontCANADARA(ListAddressDetails.this, txtAddress);
        CommonMethods.setCustomFontCANADARA(ListAddressDetails.this, txtOpenAt);
        CommonMethods.setCustomFontCANADARA(ListAddressDetails.this, txtOpenAtTime);
        CommonMethods.setCustomFontCANADARA(ListAddressDetails.this, txtCloseAt);
        CommonMethods.setCustomFontCANADARA(ListAddressDetails.this, txtCloseAtTime);
        CommonMethods.setCustomFontCANADARA(ListAddressDetails.this, txtSundayOpen);
        CommonMethods.setCustomFontCANADARA(ListAddressDetails.this, txtSundayOpenOption);
        CommonMethods.setCustomFontCANADARA(ListAddressDetails.this, txtFood);
        CommonMethods.setCustomFontCANADARA(ListAddressDetails.this, txtFoodType);
        CommonMethods.setCustomFontCANADARA(ListAddressDetails.this, txtKitchen);
        CommonMethods.setCustomFontCANADARA(ListAddressDetails.this, txtKitchenType);
        CommonMethods.setCustomFontCANADARA(ListAddressDetails.this, txtDelevery);
        CommonMethods.setCustomFontCANADARA(ListAddressDetails.this, txtDeleveryOption);
        CommonMethods.setCustomFontCANADARA(ListAddressDetails.this, txtFullTiffin);
        CommonMethods.setCustomFontCANADARA(ListAddressDetails.this, txtOneTimeMess);
        CommonMethods.setCustomFontCANADARA(ListAddressDetails.this, txtHalfTiffin);
        CommonMethods.setCustomFontCANADARA(ListAddressDetails.this, txtTwoTimeMess);

        llCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Phone));
                startActivity(intent);


            }
        });
//        imgHeaderImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intentList = new Intent(ListAddressDetails.this, Activity_MessList.class);
//                startActivity(intentList);
//            }
//        });

    }
}
