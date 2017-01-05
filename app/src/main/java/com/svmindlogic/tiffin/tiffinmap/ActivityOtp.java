package com.svmindlogic.tiffin.tiffinmap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.svmindlogic.tiffin.tiffinmap.utils.CommonMethods;
import com.svmindlogic.tiffinmap.R;


public class ActivityOtp extends AppCompatActivity {
    EditText OTP;
    Button Btn_submit;
    String Register_Id, Task, Get_otp, status, msg;
    TextView txtTerms,txtTerms1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        Btn_submit = (Button) findViewById(R.id.btb_submit);
        OTP = (EditText) findViewById(R.id.edt_otp);

        Intent intent = getIntent();
        Register_Id = intent.getStringExtra("RegisterId");
        Task = "checkotp";

        txtTerms = (TextView) findViewById(R.id.txt_terms);
        txtTerms1 = (TextView) findViewById(R.id.txt_terms1);
        CommonMethods.setCustomFontCANADARA(this, txtTerms);
        CommonMethods.setCustomFontCANADARA(this, txtTerms1);

        Btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Get_otp = OTP.getText().toString();
                if (OTP.getText().toString().trim() == null || OTP.getText().toString().trim().equals("")) {
                    Toast.makeText(ActivityOtp.this, "Enter OTP Number", Toast.LENGTH_SHORT).show();
                } else {
                    new AsynOTP().execute();
                }
            }
        });

        txtTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "http://www.tiffinmap.com/terms.php";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });

    }

    private class AsynOTP extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityOtp.this);
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

                nameValuePairs.add(new BasicNameValuePair("task", Task));
                nameValuePairs.add(new BasicNameValuePair("id", Register_Id));
                nameValuePairs.add(new BasicNameValuePair("otp", Get_otp));

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://tiffinmap.com/tmrpg/android/index.php");
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);


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

                status = jsonObject.getString("status");
                msg = jsonObject.getString("msg");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (status.equals("success")) {
               // Toast.makeText(ActivityOtp.this, msg, Toast.LENGTH_SHORT).show();
                if(msg.equals("Please Enter Valid OTP.")){
                    Toast.makeText(ActivityOtp.this, "ReEnter OTP Number", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(ActivityOtp.this, ActivityMain.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                Toast.makeText(ActivityOtp.this, "Ad Not Post Successfully,Try Again", Toast.LENGTH_SHORT).show();

            }
        }
    }

}
