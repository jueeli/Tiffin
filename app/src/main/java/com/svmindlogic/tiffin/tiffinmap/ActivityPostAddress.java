package com.svmindlogic.tiffin.tiffinmap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.svmindlogic.tiffin.tiffinmap.utils.CommonMethods;
import com.svmindlogic.tiffin.tiffinmap.utils.NetWorkConnection;
import com.svmindlogic.tiffinmap.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by softbunch on 8/12/15.
 */
public class ActivityPostAddress extends AppCompatActivity {

    private static final String TAG = "ActivityPostAddress";

    protected ArrayList<CharSequence> selectedDays = new ArrayList<CharSequence>();
    EditText edtMessUserName, edtMessName, edtMessAddress, edtMessPhone, edtMessArea;
    Button btnPostMyAddress, btnOpenAt, btnCloseAt, btnLocation;
    String id, message;
    TextView txtKm, txtFullPrize, txtHalfPrize, txtOneTimePrice, txtTwoTimePrice;
    SeekBar seekbar_Km, seekbar_FullPrize, seekbar_HalfPrize, seekbar_OneTime, seekbar_TwoTime;

    TextView txtDetailsMapSearch, txtDetailsFoodType, txtDetailsBusiness, txtDetailsKm,
            txtDetailsKichanType, txtDetailsDeliveryOptions, txtDetailsSundayClose,
            txtDetailsHalf, txtDetailsFull, txtDetailsOneTime, txtDetailsOneTimePrice, txtDetailsTwoTime, txtDetailsTwoTimePrice;

    RadioGroup radioGrp_FoodType, radioGrp_KichanType, radioGrp_DelivariOptions, radioGrp_SundayOptions;
    RadioButton radioBtn_FoodType, radioBtn_KichanType, radioBtn_DelivariOptions, radioBtn_SundayOptions;

    /**
     * Pass Value to json service
     **/
    String messUserName, messName, messUserMobile, messAddress, openTime, closeTime;
    String food_type, kichan_type, delivari_options, sunday_options, full_prize, half_prize, distance_km, distance_km1;

    String pass_Address_Latitude, pass_Address_Logitude,onePrice,onePrice1,twoPrice,twoPrice1;

    int full_prize_rs, hale_prize_rs;

    /**
     * get selected radio button from radioGroup
     **/
    int selectFoodType;
    int selectKichanType;
    int selectDelivaryOption;
    int selectSunday;

    // int idx = radioGrp_FoodType.getChildAt().getId();
    int foodtype_idx;
    int kichantype_idx;
    int delivari_idx;
    int sunday_idx;

    private Calendar cal;
    private int hour;
    private int min;
    private TimePickerDialog timePickerDialog;
    private int mYear, mMonth, mDay, mHour, mMinute;

    String full_tiffin;
    String half_tiffin;
    String one_tiffin;
    String two_tiffin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_post_address);

        /**Add Custom Action Bar **/
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        /**Add Custom Action Bar .xml **/
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);

        /** Add Views of Custom Action Bar **/
        TextView txtHeaderName = (TextView) findViewById(R.id.txt_headers_name);
        ImageView imgHeaderImage = (ImageView) findViewById(R.id.img_headers_icon);
        txtHeaderName.setText("Add Details");

        // imgHeaderImage.setBackgroundDrawable(R.drawable.ic_arrow);
        imgHeaderImage.setImageResource(R.drawable.ic_arrow);
        imgHeaderImage.setVisibility(View.VISIBLE);

        imgHeaderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityPostAddress.this, ActivityMain.class);
                startActivity(intent);
            }
        });

        /** Id of .xml view **/
        edtMessUserName = (EditText) findViewById(R.id.edt_mess_user_name);
        edtMessName = (EditText) findViewById(R.id.edt_mess_name);
        edtMessAddress = (EditText) findViewById(R.id.edt_mess_address);
        edtMessPhone = (EditText) findViewById(R.id.edt_mess_phone_number);
        //    edtMessArea = (EditText) findViewById(R.id.edt_radius_area);

        /** Button ids **/
        btnPostMyAddress = (Button) findViewById(R.id.btn_post_my_address);
        btnOpenAt = (Button) findViewById(R.id.btn_open_at);
        btnCloseAt = (Button) findViewById(R.id.btn_close_at);
        btnLocation = (Button) findViewById(R.id.btn_location);

        /** SeekBar ids **/
        seekbar_Km = (SeekBar) findViewById(R.id.seekbar_distance);
        seekbar_FullPrize = (SeekBar) findViewById(R.id.seekbar_full_prize);
        seekbar_HalfPrize = (SeekBar) findViewById(R.id.seekbar_half_prize);
        seekbar_OneTime = (SeekBar) findViewById(R.id.seekbar_one_time_prize);
        seekbar_TwoTime = (SeekBar) findViewById(R.id.seekbar_two_time_prize);

        /** RadioGroup ids **/
        radioGrp_FoodType = (RadioGroup) findViewById(R.id.radio_grp_food_type);
        radioGrp_KichanType = (RadioGroup) findViewById(R.id.radio_grp_kichan_type);
        radioGrp_DelivariOptions = (RadioGroup) findViewById(R.id.radio_grp_deliviry_options);
        radioGrp_SundayOptions = (RadioGroup) findViewById(R.id.radio_grp_sunday_close);

        /** TextView ids **/
        txtKm = (TextView) findViewById(R.id.txt_distance_set);
        txtFullPrize = (TextView) findViewById(R.id.txt_full_prize);
        txtHalfPrize = (TextView) findViewById(R.id.txt_half_prize);
       // txtDetailsMapSearch = (TextView) findViewById(R.id.txt_details_map_search);
        txtDetailsFoodType = (TextView) findViewById(R.id.txt_details_food_type);
        txtDetailsKichanType = (TextView) findViewById(R.id.txt_details_kichan_type);
        txtDetailsDeliveryOptions = (TextView) findViewById(R.id.txt_details_delivery_options);
        txtDetailsSundayClose = (TextView) findViewById(R.id.txt_details_sunday_close);
        txtDetailsBusiness = (TextView) findViewById(R.id.txt_details_business);
        txtDetailsHalf = (TextView) findViewById(R.id.txt_details_half);
        txtDetailsFull = (TextView) findViewById(R.id.txt_details_full);
        txtDetailsKm = (TextView) findViewById(R.id.txt_details_km);
        txtDetailsOneTime = (TextView) findViewById(R.id.txt_details_one_time);
        txtDetailsTwoTime = (TextView) findViewById(R.id.txt_details_two_time);
        txtDetailsOneTimePrice = (TextView) findViewById(R.id.txt_one_time_prize);
        txtDetailsTwoTimePrice = (TextView) findViewById(R.id.txt_two_time_prize);

        /** Set Text **/
        txtKm.setText(seekbar_Km.getProgress() + "/" + "Km");
        txtFullPrize.setText(seekbar_FullPrize.getProgress() + "/" + "Rs");
        txtHalfPrize.setText(seekbar_HalfPrize.getProgress() + "/" + "Rs");
        txtDetailsOneTimePrice.setText(seekbar_OneTime.getProgress() + "/" + "Rs");
        txtDetailsTwoTimePrice.setText(seekbar_TwoTime.getProgress() + "/" + "Rs");


        //   txtKm.setText(seekbar_Km.getProgress() + "/" + seekbar_Km.getMax());

        /** Apply font **/
        CommonMethods.setCustomFontCANADARA(ActivityPostAddress.this, txtHeaderName);
        CommonMethods.setCustomFontCANADARA(ActivityPostAddress.this, txtDetailsBusiness);
        CommonMethods.setCustomFontCANADARA(ActivityPostAddress.this, txtDetailsMapSearch);
        CommonMethods.setCustomFontCANADARA(ActivityPostAddress.this, txtDetailsFoodType);
        CommonMethods.setCustomFontCANADARA(ActivityPostAddress.this, txtDetailsKichanType);
        CommonMethods.setCustomFontCANADARA(ActivityPostAddress.this, txtDetailsDeliveryOptions);
        CommonMethods.setCustomFontCANADARA(ActivityPostAddress.this, txtDetailsSundayClose);
        CommonMethods.setCustomFontCANADARA(ActivityPostAddress.this, txtDetailsHalf);
        CommonMethods.setCustomFontCANADARA(ActivityPostAddress.this, txtDetailsFull);
        CommonMethods.setCustomFontCANADARA(ActivityPostAddress.this, txtDetailsKm);
        // CommonMethods.setCustomFontCANADARA(ActivityPostAddress.this, txtKm);
        //CommonMethods.setCustomFontCANADARA(ActivityPostAddress.this, txtFullPrize);
        // CommonMethods.setCustomFontCANADARA(ActivityPostAddress.this, txtHalfPrize);
        CommonMethods.setCustomFontCANADARA(ActivityPostAddress.this, radioGrp_FoodType);
        CommonMethods.setCustomFontCANADARA(ActivityPostAddress.this, radioGrp_KichanType);
        CommonMethods.setCustomFontCANADARA(ActivityPostAddress.this, radioGrp_DelivariOptions);
        CommonMethods.setCustomFontCANADARA(ActivityPostAddress.this, radioGrp_SundayOptions);
        CommonMethods.setCustomFontCANADARA(ActivityPostAddress.this, btnPostMyAddress);
        CommonMethods.setCustomFontCANADARA(ActivityPostAddress.this, btnOpenAt);
        CommonMethods.setCustomFontCANADARA(ActivityPostAddress.this, btnCloseAt);
        CommonMethods.setCustomFontCANADARA(ActivityPostAddress.this, btnLocation);
        CommonMethods.setCustomFontCANADARA(ActivityPostAddress.this, edtMessUserName);
        CommonMethods.setCustomFontCANADARA(ActivityPostAddress.this, edtMessName);
        CommonMethods.setCustomFontCANADARA(ActivityPostAddress.this, edtMessAddress);
        //CommonMethods.setCustomFontCANADARA(ActivityPostAddress.this, edtMessPhone);
        CommonMethods.setCustomFontCANADARA(ActivityPostAddress.this, txtDetailsOneTime);
        CommonMethods.setCustomFontCANADARA(ActivityPostAddress.this, txtDetailsTwoTime);
        //   CommonMethods.setCustomFontCANADARA(ActivityPostAddress.this, txtDetailsOneTimePrice);
        //  CommonMethods.setCustomFontCANADARA(ActivityPostAddress.this, txtDetailsTwoTimePrice);


        seekbar_OneTime.setProgress(0);
        seekbar_OneTime.incrementProgressBy(50);
        seekbar_OneTime.setMax(2000);

        seekbar_TwoTime.setProgress(0);
        seekbar_TwoTime.incrementProgressBy(50);
        seekbar_TwoTime.setMax(4000);
        /** click on seekbar**/
        seekbar_OneTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           int seekProgressOne = 0;


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               // seekProgressOne = progress;
                //one_tiffin = seekProgressOne + "/" + "Rs";
                progress = progress / 50;
                progress = progress * 50;

                one_tiffin = progress + "/" + "Rs";
                txtDetailsOneTimePrice.setText(one_tiffin);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // textView.setText(progress + "/" + seekBar.getMax());

               // one_tiffin = seekProgressOne + "/" + "Rs";
               // one_tiffin = progress + "/" + "Rs";
              // txtDetailsOneTimePrice.setText(one_tiffin);
            }
        });

        /** click on seekbar**/
        seekbar_TwoTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
          //  int seekProgressTwo = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //seekProgressTwo = progress;

                progress = progress / 50;
                progress = progress * 50;
                two_tiffin = progress + "/" + "Rs";
                txtDetailsTwoTimePrice.setText(two_tiffin);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // textView.setText(progress + "/" + seekBar.getMax());
//                two_tiffin = seekProgressTwo + "/" + "Rs";
//                txtDetailsTwoTimePrice.setText(two_tiffin);
            }
        });

        /** click on seekbar**/
        seekbar_Km.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int seekProgressKm = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekProgressKm = progress;
                txtKm.setText(seekProgressKm + "/" + "Km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // textView.setText(progress + "/" + seekBar.getMax());
                txtKm.setText(seekProgressKm + "/" + "Km");
            }
        });


        /** click on seekbar**/
        seekbar_FullPrize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int seekProgressFull = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekProgressFull = progress;
                full_tiffin = seekProgressFull + "/" + "Rs";
                txtFullPrize.setText(full_tiffin);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // textView.setText(progress + "/" + seekBar.getMax());

                full_tiffin = seekProgressFull + "/" + "Rs";
                txtFullPrize.setText(full_tiffin);

            }
        });


        /** click on seekbar**/
        seekbar_HalfPrize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int seekProgressHalf = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekProgressHalf = progress;
                half_tiffin = seekProgressHalf + "/" + "Rs";
                txtHalfPrize.setText(half_tiffin);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // textView.setText(progress + "/" + seekBar.getMax());
                half_tiffin = seekProgressHalf + "/" + "Rs";
                txtHalfPrize.setText(half_tiffin);
            }
        });

        /** Date Picker **/
        btnOpenAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog_open();
            }
        });
        /** Date Picker **/
        btnCloseAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog_close();
            }
        });

        /** Click on button **/
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtMessAddress.getText().toString().trim() == null || edtMessAddress.getText().toString().trim().equals("")) {
                    Toast.makeText(ActivityPostAddress.this, "Enter Address ", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intentMap = new Intent(ActivityPostAddress.this, ActivityCurrentMap.class);
                    intentMap.putExtra("ADDRESS", edtMessAddress.getText().toString().trim());
                    // startActivity(intentMap);
                    startActivityForResult(intentMap, 1);
                }
            }
        });


        /** get selected radio button from radioGroup **/


        radioGrp_DelivariOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectDelivaryOption = radioGrp_DelivariOptions.getCheckedRadioButtonId();
                delivari_idx = radioGrp_DelivariOptions.indexOfChild(findViewById(selectDelivaryOption));
                Log.e("id", "" + delivari_idx);

                if (delivari_idx == 0) {
                    seekbar_Km.setVisibility(View.GONE);
                    txtKm.setVisibility(View.GONE);
                    txtDetailsKm.setVisibility(View.GONE);
                } else {
                    seekbar_Km.setVisibility(View.VISIBLE);
                    txtKm.setVisibility(View.VISIBLE);
                    txtDetailsKm.setVisibility(View.VISIBLE);
                }
            }
        });


        btnPostMyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messUserName = edtMessUserName.getText().toString().trim();
                messName = edtMessName.getText().toString().trim();
                messUserMobile = edtMessPhone.getText().toString().trim();
                messAddress = edtMessAddress.getText().toString().trim();
                openTime = btnOpenAt.getText().toString().trim();
                closeTime = btnCloseAt.getText().toString().trim();

                distance_km = txtKm.getText().toString().trim();
                distance_km1 = distance_km.replace("/Km", " ");

                onePrice = txtDetailsTwoTimePrice.getText().toString().trim();
                onePrice1 = onePrice.replace("/Rs", " ");
                twoPrice = txtDetailsOneTimePrice.getText().toString().trim();
                twoPrice1 = twoPrice.replace("/Rs", " ");

                full_prize = txtFullPrize.getText().toString().trim();
                half_prize = txtHalfPrize.getText().toString().trim();

                /** get selected radio button from radioGroup **/
                selectFoodType = radioGrp_FoodType.getCheckedRadioButtonId();
                selectKichanType = radioGrp_KichanType.getCheckedRadioButtonId();
                selectSunday = radioGrp_SundayOptions.getCheckedRadioButtonId();

                foodtype_idx = radioGrp_FoodType.indexOfChild(findViewById(selectFoodType));
                kichantype_idx = radioGrp_KichanType.indexOfChild(findViewById(selectKichanType));
                sunday_idx = radioGrp_SundayOptions.indexOfChild(findViewById(selectSunday));

                Log.e("id", "" + foodtype_idx + kichantype_idx + delivari_idx + sunday_idx);


                /** Convert int ot Sting **/
                food_type = Integer.toString(foodtype_idx);
                kichan_type = Integer.toString(kichantype_idx);
                delivari_options = Integer.toString(delivari_idx);
                sunday_options = Integer.toString(sunday_idx);
                Log.e("id4", "" + food_type + kichan_type + delivari_options + sunday_options);


                /** check validation **/
                boolean isValidate = Validation();
                /** Check Network connection **/
                NetWorkConnection netWorkConnection = new NetWorkConnection();
                boolean isNetConnected = netWorkConnection.isNetAvailable(getSystemService(Context.CONNECTIVITY_SERVICE));

                if (isValidate) {
                    String gg = full_tiffin;

                    //   "add".equalsIgnoreCase(called_from)
                    if ((pass_Address_Latitude == null || pass_Address_Latitude.equalsIgnoreCase("")) && (pass_Address_Logitude == null || pass_Address_Logitude.equalsIgnoreCase(""))) {
                        Toast.makeText(ActivityPostAddress.this, " Make your location on Map", Toast.LENGTH_SHORT).show();
                    } else if (full_tiffin == null) {
                        Toast.makeText(ActivityPostAddress.this, " Enter Full Tiffin Prize", Toast.LENGTH_SHORT).show();
                    } else if (half_tiffin == null) {
                        Toast.makeText(ActivityPostAddress.this, "Enter Half Tiffin Prize", Toast.LENGTH_SHORT).show();
                    } else if (one_tiffin == null) {
                        Toast.makeText(ActivityPostAddress.this, "Enter One time Tiffin Prize", Toast.LENGTH_SHORT).show();
                    } else if (two_tiffin == null) {
                        Toast.makeText(ActivityPostAddress.this, "Enter Two time Tiffin Prize", Toast.LENGTH_SHORT).show();
                    } else if (isNetConnected) {
                        /** Call AsynTask **/
                        //     Toast.makeText(ActivityPostAddress.this, "is Validate", Toast.LENGTH_SHORT).show();
                        new AsynPostAddress().execute();
                    } else {
                        Toast.makeText(ActivityPostAddress.this,
                                getResources().getString(R.string.check_connection_message),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * Validation
     **/
    private boolean Validation() {

        if (edtMessUserName.getText().toString().trim() == null || edtMessUserName.getText().toString().trim().equals("")) {
            Toast.makeText(ActivityPostAddress.this, "Enter Your Name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (edtMessName.getText().toString().trim() == null || edtMessName.getText().toString().trim().equals("")) {
            Toast.makeText(ActivityPostAddress.this, "Enter Mess Name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (edtMessAddress.getText().toString().trim() == null || edtMessAddress.getText().toString().trim().equals("")) {
            Toast.makeText(ActivityPostAddress.this, "Enter Your Address", Toast.LENGTH_SHORT).show();
            return false;
        } else if (radioGrp_FoodType.getCheckedRadioButtonId() <= 0) {
            Toast.makeText(ActivityPostAddress.this, "Select Food Type", Toast.LENGTH_SHORT).show();
            return false;
        } else if (radioGrp_KichanType.getCheckedRadioButtonId() <= 0) {
            Toast.makeText(ActivityPostAddress.this, "Select Kichan Type", Toast.LENGTH_SHORT).show();
            return false;
        } else if (radioGrp_DelivariOptions.getCheckedRadioButtonId() <= 0) {
            Toast.makeText(ActivityPostAddress.this, "Select Delivary Options", Toast.LENGTH_SHORT).show();
            return false;
        } else if (btnOpenAt.getText().toString().trim() == null || btnOpenAt.getText().toString().trim().equals("Open At")) {
            Toast.makeText(ActivityPostAddress.this, "Enter Your Mess Open Time", Toast.LENGTH_SHORT).show();
            return false;
        } else if (btnCloseAt.getText().toString().trim() == null || btnCloseAt.getText().toString().trim().equals("Close At")) {
            Toast.makeText(ActivityPostAddress.this, "Enter Your Mess Close Time", Toast.LENGTH_SHORT).show();
            return false;

        } else if (radioGrp_SundayOptions.getCheckedRadioButtonId() <= 0) {
            Toast.makeText(ActivityPostAddress.this, "Select Sunday Mess OPen or Not ", Toast.LENGTH_SHORT).show();
            return false;

        } else if (edtMessPhone.getText().toString().trim() == null || edtMessPhone.getText().toString().trim().equals("")) {
            Toast.makeText(ActivityPostAddress.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    /**
     * Date Picker
     **/
    private void timePickerDialog_open() {
        final int hourselected = 0;
        Calendar newCalendarTime = Calendar.getInstance();
        /** Launch Time Picker Dialog **/
        timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

//                        hour = hourOfDay;
//                        Calendar currentDate = Calendar.getInstance();
//                        String selectedDate = btnCloseAt.getText().toString();
//                        btnOpenAt.setText(hour + " : " + minute);

                        String timeSet = "";
                        if (hourOfDay > 12) {
                            hourOfDay -= 12;
                            timeSet = "PM";
                        } else if (hourOfDay == 0) {
                            hourOfDay += 12;
                            timeSet = "AM";
                        } else if (hourOfDay == 12)
                            timeSet = "PM";
                        else
                            timeSet = "AM";

                        String minutes = "";
                        if (minute < 10)
                            minutes = "0" + minute;
                        else
                            minutes = String.valueOf(minute);
                        btnOpenAt.setText(hourOfDay + ":" + minutes + " " + timeSet);

                    }
                }, newCalendarTime.get(Calendar.HOUR), newCalendarTime.get(Calendar.MINUTE), true);
        newCalendarTime.getTimeInMillis();
        timePickerDialog.show();
    }

    /**
     * Date Picker
     **/
    private void timePickerDialog_close() {
        final int hourselected = 0;
        Calendar newCalendarTime = Calendar.getInstance();
        /** Launch Time Picker Dialog **/
        timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

//                        hour = hourOfDay;
//                        Calendar currentDate = Calendar.getInstance();
//                        String selectedDate = btnCloseAt.getText().toString();
//                        btnCloseAt.setText(hour + " : " + minute);

                        String timeSet = "";
                        if (hourOfDay > 12) {
                            hourOfDay -= 12;
                            timeSet = "PM";
                        } else if (hourOfDay == 0) {
                            hourOfDay += 12;
                            timeSet = "AM";
                        } else if (hourOfDay == 12)
                            timeSet = "PM";
                        else
                            timeSet = "AM";

                        String minutes = "";
                        if (minute < 10)
                            minutes = "0" + minute;
                        else
                            minutes = String.valueOf(minute);
                        btnCloseAt.setText(hourOfDay + ":" + minutes + " " + timeSet);

                    }
                }, newCalendarTime.get(Calendar.HOUR), newCalendarTime.get(Calendar.MINUTE), true);
        newCalendarTime.getTimeInMillis();
        timePickerDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                pass_Address_Latitude = data.getStringExtra("LOCATION_LAT");
                pass_Address_Logitude = data.getStringExtra("LOCATION_LOG");

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(ActivityPostAddress.this, "Address Not Found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class AsynPostAddress extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityPostAddress.this);
            progressDialog.setMessage("Sending Data");
            progressDialog.setIndeterminate(false);
            // progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("name", messUserName));
                nameValuePairs.add(new BasicNameValuePair("business_name", messName));
                nameValuePairs.add(new BasicNameValuePair("address", messAddress));
                nameValuePairs.add(new BasicNameValuePair("mobile", messUserMobile));
//                nameValuePairs.add(new BasicNameValuePair("latitude", "-19.0518937"));
//                nameValuePairs.add(new BasicNameValuePair("longitude", "-72.9159729"));

                nameValuePairs.add(new BasicNameValuePair("latitude", pass_Address_Latitude));
                nameValuePairs.add(new BasicNameValuePair("longitude", pass_Address_Logitude));
                nameValuePairs.add(new BasicNameValuePair("food_type", food_type));
                nameValuePairs.add(new BasicNameValuePair("kitchen_type", kichan_type));
                nameValuePairs.add(new BasicNameValuePair("delivery_option", delivari_options));
                nameValuePairs.add(new BasicNameValuePair("delivery_radius", distance_km1));
                nameValuePairs.add(new BasicNameValuePair("full_tiffin_prize", full_prize));
                nameValuePairs.add(new BasicNameValuePair("half_tiffin_prize", half_prize));
                nameValuePairs.add(new BasicNameValuePair("onetime_monthly", onePrice1));
                nameValuePairs.add(new BasicNameValuePair("twotime_monthly", twoPrice1));
                nameValuePairs.add(new BasicNameValuePair("opens_at", openTime));
                nameValuePairs.add(new BasicNameValuePair("closed_at", closeTime));
                nameValuePairs.add(new BasicNameValuePair("sunday_close", sunday_options));
                nameValuePairs.add(new BasicNameValuePair("published", "0"));
                nameValuePairs.add(new BasicNameValuePair("task", "addbusiness"));


                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://www.tiffinmap.com/tmrpg/android/index.php");
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);

                // JSONObject jsonObject = new JSONObject(response);
                // JSONObject jsonObject1 = jsonObject.getJSONObject("");
                // String status = jsonObject.getString("status");
                //  String message = jsonObject.getString("msg");
                //  String id = jsonObject.getString("ID");


            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);

                // JSONObject jsonObject1 = jsonObject.getJSONObject("");
                String status = jsonObject.getString("status");
                message = jsonObject.getString("msg");
                id = jsonObject.getString("ID");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Toast.makeText(ActivityPostAddress.this, "Save Details Successfuliy", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ActivityPostAddress.this, ActivityOtp.class);
            intent.putExtra("RegisterId", id);
            startActivity(intent);
            finish();
        }
    }
}
