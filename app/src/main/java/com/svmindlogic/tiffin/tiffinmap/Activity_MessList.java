package com.svmindlogic.tiffin.tiffinmap;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.svmindlogic.tiffin.tiffinmap.adapter.Listmap_Adapter;
import com.svmindlogic.tiffin.tiffinmap.adapter.PlacesAutoCompleteAdapter;
import com.svmindlogic.tiffin.tiffinmap.listeners.RecyclerItemClickListener;
import com.svmindlogic.tiffin.tiffinmap.model.Listmap_model;
import com.svmindlogic.tiffin.tiffinmap.utils.Constants;
import com.svmindlogic.tiffin.tiffinmap.utils.NetWorkConnection;
import com.svmindlogic.tiffinmap.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Activity_MessList extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    ListView list;
    JSONObject jsonobject;
    JSONArray jsonarray;
    Listmap_Adapter adapter;
    ProgressDialog mProgressDialog;
    String searchAddress;
    ImageView imgsearch;
    String placeName;
    EditText edtSearch;
    private ArrayList<Listmap_model> addressMessList;
    String enter_address_Latitude, enter_address_Logitudel;
    double enter_map_latitude, enter_map_logitude;


    private static final String MAP_SEARCH_LOCATION_URL = "http://tiffinmap.com/tmrpg/android/index.php";

    String MAP_SEARCH_LOCATION_LATITUDE;
    String MAP_SEARCH_LOCATION_LONGITUDE;

    String passAddress;

    protected GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(
            new LatLng(-0, 0), new LatLng(0, 0));

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();
        setContentView(R.layout.activity_addess_list);

        getSupportActionBar().hide();
        /** Add Action Bar **/
        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setCustomView(R.layout.et_search_bar);

        /** Action Bar view Ids **/
        // ImageView imgBackArrow = (ImageView) findViewById(R.id.img_arrow_back);
        imgsearch = (ImageView) findViewById(R.id.img_search_address_map);
        ImageView imgAddressList = (ImageView) findViewById(R.id.img_address_list);
        edtSearch = (EditText) findViewById(R.id.et_input_search);
        ImageView imgarrowBack = (ImageView) findViewById(R.id.img_arrow_back);
        imgAddressList.setVisibility(View.VISIBLE);
        imgAddressList.setBackgroundResource(R.drawable.ic_mess_location);

        addressMessList = new ArrayList<Listmap_model>();
        passAddress = getIntent().getExtras().getString("SEARCH");

        if(passAddress!=null){
            edtSearch.setText(passAddress);
            getLatLogEnterAddress(passAddress);
            new AsynMessAddressList().execute();
        }

        mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(this, R.layout.searchview_adapter,
                mGoogleApiClient, BOUNDS_INDIA, null);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAutoCompleteAdapter);


        edtSearch.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                mRecyclerView.setVisibility(View.VISIBLE);
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                    mAutoCompleteAdapter.getFilter().filter(s.toString());
                }
//                else if (!mGoogleApiClient.isConnected()) {
//                    Toast.makeText(getApplicationContext(), Constants.API_NOT_CONNECTED, Toast.LENGTH_SHORT).show();
//                    Log.e(Constants.PlacesTag, Constants.API_NOT_CONNECTED);
//                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {


            }

            public void afterTextChanged(Editable s) {
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        });
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mAutoCompleteAdapter.getItem(position);
                        final String placeId = String.valueOf(item.placeId);
                        placeName = String.valueOf(item.description);
                        Log.i("TAG", "Autocomplete item selected: " + item.description);
                        /*
                             Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.
                         */

                        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                                .getPlaceById(mGoogleApiClient, placeId);
                        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(PlaceBuffer places) {
                                if (places.getCount() == 1) {
                                    //Do the things here on Click.....
                                    // Toast.makeText(getApplicationContext(), String.valueOf(places.get(0).getLatLng()), Toast.LENGTH_SHORT).show();
                                    edtSearch.setText(placeName);

                                    mRecyclerView.setVisibility(View.GONE);

                                } else {
                                    Toast.makeText(getApplicationContext(), Constants.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        Log.i("TAG", "Clicked: " + item.description);
                        Log.i("TAG", "Called getPlaceById to get Place details for " + item.placeId);
                    }
                })
        );

        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchAddress = edtSearch.getText().toString().trim();
                if (edtSearch.getText().toString().trim() != null || !(edtSearch.getText().toString().trim().equals(""))) {
                    addressMessList.clear();
                    getLatLogEnterAddress(searchAddress);

                }
                if (edtSearch.getText().toString().trim() == null || edtSearch.getText().toString().trim().equals("")) {
                    Toast.makeText(Activity_MessList.this, "Enter Location", Toast.LENGTH_SHORT).show();
                } else {
                    addressMessList.clear();
                    /** Check Network connection **/
                    NetWorkConnection netWorkConnection = new NetWorkConnection();
                    boolean isNetConnected = netWorkConnection.isNetAvailable(getSystemService(Context.CONNECTIVITY_SERVICE));
                    if (isNetConnected) {
                        /** Call AsynTask **/
                        new AsynMessAddressList().execute();
                    } else {
                        Toast.makeText(Activity_MessList.this, getResources().getString(R.string.check_connection_message), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        imgarrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(Activity_MessList.this, ActivityMapAddress.class);
                startActivity(intentBack);
            }
        });

        imgAddressList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentList = new Intent(Activity_MessList.this, ActivityMapAddress.class);
                startActivity(intentList);
            }
        });
    }


    /**
     * AsyncTask
     **/
    public class AsynMessAddressList extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(Activity_MessList.this);
            mProgressDialog.setMessage("Loading…");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }


        @Override
        protected String doInBackground(String... strings) {
            String responseMessage = "";

            try {

                MAP_SEARCH_LOCATION_LATITUDE = enter_address_Latitude;
                MAP_SEARCH_LOCATION_LONGITUDE = enter_address_Logitudel;

                StringBuilder addresslist_url = new StringBuilder(MAP_SEARCH_LOCATION_URL + "?task=getsearchlist" +
                        "&longitude=" + MAP_SEARCH_LOCATION_LONGITUDE + "&latitude=" + MAP_SEARCH_LOCATION_LATITUDE);

                HttpClient httpClient = new DefaultHttpClient();
                //  HttpPost httpPost = new HttpPost("http://tiffinmap.com/tmrpg/android/index.php?task=getsearchlist&longitude=74.2261197265625&latitude=16.689089771367563");

                HttpPost httpPost = new HttpPost(addresslist_url.toString());

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                responseMessage = EntityUtils.toString(httpEntity);

                JSONObject obj = new JSONObject(responseMessage);
                JSONObject emp = obj.getJSONObject("allsearchlist");
                String status = emp.getString("status");
                String message = emp.getString("message");
                String count = emp.getString("count");

                jsonarray = emp.getJSONArray("searchresult");

                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);
                    String owner_name = jsonobject.getString("name");
                    String Business_name = jsonobject.getString("business_name");
                    String Food_type = jsonobject.getString("food_type");
                    String Kitchen_type = jsonobject.getString("kitchen_type");
                    String Delivery_option = jsonobject.getString("delivery_option");
                    String Delivery_radius = jsonobject.getString("delivery_radius");
                    String Full_tiffin_prize = jsonobject.getString("full_tiffin_prize");
                    String Half_tiffin_prize = jsonobject.getString("half_tiffin_prize");
                    String Opens_at = jsonobject.getString("opens_at");
                    String Closed_at = jsonobject.getString("closed_at");
                    String Distance = jsonobject.getString("distance");
                    String Address = jsonobject.getString("address");
                    String Sunday_close = jsonobject.getString("sunday_close");
                    String Mobile = jsonobject.getString("mobile");
                    String oneTime = jsonobject.getString("onetime_monthly");
                    String twoTime = jsonobject.getString("twotime_monthly");


                    addressMessList.add(new Listmap_model(owner_name, Business_name, Food_type, Kitchen_type, Delivery_option, Delivery_radius, Full_tiffin_prize
                            , Half_tiffin_prize, Opens_at, Closed_at, Distance, Address, Sunday_close, Mobile,oneTime,twoTime));
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return responseMessage;
        }

        protected void onPostExecute(String args) {
            list = (ListView) findViewById(R.id.map_listView);
            adapter = new Listmap_Adapter(Activity_MessList.this, addressMessList);
            list.setAdapter(adapter);
            mProgressDialog.dismiss();
        }
    }

    /**
     * LatLog from enter address
     **/
    public LatLng getLatLogEnterAddress(String enterAddress) {

        addressMessList.clear();
        Geocoder coder = new Geocoder(this, Locale.getDefault());
        List<Address> address;
        LatLng addressLatLng = null;

        try {
            address = coder.getFromLocationName(enterAddress, 5);
            if (address == null) {
                return null;
            }
            enter_map_latitude = address.get(0).getLatitude();
            enter_map_logitude = address.get(0).getLongitude();

            enter_address_Latitude = Double.toString(enter_map_latitude);
            enter_address_Logitudel = Double.toString(enter_map_logitude);

            addressLatLng = new LatLng(enter_map_latitude, enter_map_logitude);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return addressLatLng;
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.v("Google API Callback", "Connection Done");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v("Google API Callback", "Connection Suspended");
        Log.v("Code", String.valueOf(i));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v("Google API Callback", "Connection Failed");
        Log.v("Error Code", String.valueOf(connectionResult.getErrorCode()));
        Toast.makeText(this, Constants.API_NOT_CONNECTED, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
            Log.v("Google API", "Connecting");
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            Log.v("Google API", "Dis-Connecting");
            mGoogleApiClient.disconnect();
        }
    }
}



