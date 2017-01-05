package com.svmindlogic.tiffin.tiffinmap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.svmindlogic.tiffin.tiffinmap.utils.CommonMethods;
import com.svmindlogic.tiffin.tiffinmap.utils.NetWorkConnection;
import com.svmindlogic.tiffinmap.R;

/**
 * Created by pappu on 13/12/15.
 */
public class ActivityMain extends Activity {
    TextView edtInputSerch;
    ImageView imgSerach;
    ImageView btnAddDetails;
    LinearLayout llSearch;
    String search;
    TextView txtMainTitel, txtMainSubTitle, txtMainProvider, txtMainAdvertise,txtMainAdvertise1, txtMainMessage, txtMainPolicy,txtMainUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtInputSerch = (TextView) findViewById(R.id.edt_search_input);
        imgSerach = (ImageView) findViewById(R.id.img_search);
        btnAddDetails = (ImageView) findViewById(R.id.btn_add_details);
        llSearch = (LinearLayout) findViewById(R.id.ll_search);

        txtMainTitel = (TextView) findViewById(R.id.txt_main_title);
        txtMainSubTitle = (TextView) findViewById(R.id.txt_main_sub_title);
        txtMainProvider = (TextView) findViewById(R.id.txt_main_provider);
        txtMainAdvertise = (TextView) findViewById(R.id.txt_main_advertise);
        txtMainAdvertise1 = (TextView) findViewById(R.id.txt_main_advertise1);
        txtMainMessage = (TextView) findViewById(R.id.txt_main_message);
        txtMainPolicy = (TextView) findViewById(R.id.txt_main_policy);
        txtMainUrl = (TextView) findViewById(R.id.txt_main_visit);

        CommonMethods.setCustomFontCANADARA(ActivityMain.this, txtMainTitel);
        CommonMethods.setCustomFontCANADARA(ActivityMain.this, txtMainSubTitle);
        CommonMethods.setCustomFontCANADARA(ActivityMain.this, txtMainProvider);
        CommonMethods.setCustomFontCANADARA(ActivityMain.this, txtMainAdvertise);
        CommonMethods.setCustomFontCANADARA(ActivityMain.this, txtMainAdvertise1);
        CommonMethods.setCustomFontCANADARA(ActivityMain.this, txtMainMessage);
        CommonMethods.setCustomFontCANADARA(ActivityMain.this, txtMainPolicy);
        CommonMethods.setCustomFontCANADARA(ActivityMain.this, txtMainUrl);


        btnAddDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /** Check Network connection **/
                NetWorkConnection netWorkConnection = new NetWorkConnection();
                boolean isNetConnected = netWorkConnection.isNetAvailable(getSystemService(Context.CONNECTIVITY_SERVICE));

                if (isNetConnected) {
                    Intent intent = new Intent(ActivityMain.this, ActivityPostAddress.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ActivityMain.this,
                            getResources().getString(R.string.check_connection_message),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                /** Check Network connection **/
                NetWorkConnection netWorkConnection = new NetWorkConnection();
                boolean isNetConnected = netWorkConnection.isNetAvailable(getSystemService(Context.CONNECTIVITY_SERVICE));

                if (isNetConnected) {
                    Intent intent = new Intent(ActivityMain.this, ActivityMapAddress.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ActivityMain.this,
                            getResources().getString(R.string.check_connection_message),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


        txtMainUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "http://www.tiffinmap.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        txtMainPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.tiffinmap.com/terms.php";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }
}
