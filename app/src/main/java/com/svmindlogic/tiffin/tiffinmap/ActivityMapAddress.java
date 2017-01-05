package com.svmindlogic.tiffin.tiffinmap;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.svmindlogic.tiffin.tiffinmap.adapter.PlacesAutoCompleteAdapter;
import com.svmindlogic.tiffin.tiffinmap.listeners.RecyclerItemClickListener;
import com.svmindlogic.tiffin.tiffinmap.model.M_MapAddress;
import com.svmindlogic.tiffin.tiffinmap.utils.CommonMethods;
import com.svmindlogic.tiffin.tiffinmap.utils.Constants;
import com.svmindlogic.tiffin.tiffinmap.utils.NetWorkConnection;
import com.svmindlogic.tiffinmap.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by softbunch on 14/12/15.
 */
public class ActivityMapAddress extends AppCompatActivity implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "ActivityMapAddress";
    // private static final String MAP_SEARCH_LOCATION_URL="http://tiffinmap.com/tmrpg/android/index.php?task=getsearchlist&longitude=74.23&latitude=16.69";
    private static final String MAP_SEARCH_LOCATION_URL = "http://tiffinmap.com/tmrpg/android/index.php";

    String MAP_SEARCH_LOCATION_LATITUDE;
    String MAP_SEARCH_LOCATION_LONGITUDE;
    int i;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mDisplayAddress;
    String searchAddress;
    ArrayList<M_MapAddress> addressMapList;
    ProgressDialog progressDialog;
    M_MapAddress m_mapAddress;
    EditText edtSearch;
    double enter_map_latitude, enter_map_logitude;
    ImageView imgsearch;
    String enter_address_Latitude, enter_address_Logitudel;
    Map<String, M_MapAddress> locationList1 = new HashMap<>();
    private GoogleMap mMap;


    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(
            new LatLng(-0, 0), new LatLng(0, 0));

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Check google play service **/
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        /** Show current Location **/
        createLocationRequest();

        /** Add google api for client **/
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
        /** Set .xml **/
        setContentView(R.layout.activity_map_list);

        mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(this, R.layout.searchview_adapter,
                mGoogleApiClient, BOUNDS_INDIA, null);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ActivityMapAddress.this));

        mRecyclerView.setAdapter(mAutoCompleteAdapter);

        getSupportActionBar().hide();
        /** Add Action Bar **/
        // getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        // getSupportActionBar().setCustomView(R.layout.et_search_bar);
        /** Action Bar view Ids **/
        //  ImageView imgBackArrow = (ImageView) findViewById(R.id.img_arrow_back);
        imgsearch = (ImageView) findViewById(R.id.img_search_address_map);
        ImageView imgAddressList = (ImageView) findViewById(R.id.img_address_list);
        ImageView imgarrowBack = (ImageView) findViewById(R.id.img_arrow_back);
        edtSearch = (EditText) findViewById(R.id.et_input_search);
        imgAddressList.setVisibility(View.VISIBLE);
        imgAddressList.setBackgroundResource(R.drawable.ic_mess_list);

        /** Check gps on or off **/
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //  Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
        } else {
            showGPSDisabledAlertToUser();
        }

        /** Map Fragment **/

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_address_list);
        mMap = mapFragment.getMap();
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        /** Show My Current Location **/
        mMap.getUiSettings().setZoomControlsEnabled(true);

        addressMapList = new ArrayList<M_MapAddress>();

//        NetWorkConnection netWorkConnection = new NetWorkConnection();
//        boolean isNetConnected = netWorkConnection.isNetAvailable(getSystemService(Context.CONNECTIVITY_SERVICE));
//        if (isNetConnected) {
//            /** Call AsynTask **/
//            new AsynMessMapList().execute();
//        } else {
//            Toast.makeText(ActivityMapAddress.this, getResources().getString(R.string.check_connection_message), Toast.LENGTH_SHORT).show();
//        }


        edtSearch.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRecyclerView.setVisibility(View.VISIBLE);
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                    mAutoCompleteAdapter.getFilter().filter(s.toString());
                }
//                else if (!mGoogleApiClient.isConnected()) {
//
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
                        final String placeName = String.valueOf(item.description);
                        Log.i("TAG", "Autocomplete item selected: " + item.description);
                        /*
                             Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.
                         */

                        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
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

        /** Action Bar click Listener **/
        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchAddress = edtSearch.getText().toString().trim();
                if (edtSearch.getText().toString().trim() != null || !(edtSearch.getText().toString().trim().equals(""))) {
                    addressMapList.clear();
                    getLatLogEnterAddress(searchAddress);
                }
                if (edtSearch.getText().toString().trim() == null || edtSearch.getText().toString().trim().equals("")) {
                    Toast.makeText(ActivityMapAddress.this, "Enter Location", Toast.LENGTH_SHORT).show();
                } else {
                    addressMapList.clear();

                    /** Check Network connection **/
                    NetWorkConnection netWorkConnection = new NetWorkConnection();
                    boolean isNetConnected = netWorkConnection.isNetAvailable(getSystemService(Context.CONNECTIVITY_SERVICE));
                    if (isNetConnected) {
                        /** Call AsynTask **/
                        new AsynMessMapList().execute();
                    } else {
                        Toast.makeText(ActivityMapAddress.this, getResources().getString(R.string.check_connection_message), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        imgarrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(ActivityMapAddress.this, ActivityMain.class);
                startActivity(intentBack);
            }
        });

        imgAddressList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentList = new Intent(ActivityMapAddress.this, Activity_MessList.class);
                String sd = edtSearch.getText().toString().trim();
                intentList.putExtra("SEARCH", edtSearch.getText().toString().trim());
                startActivity(intentList);
            }
        });

    }

    /**
     * Show current location
     **/
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Check google play service in device
     **/
    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    /**
     * Default method of GoogleApiClient.ConnectionCallback
     **/
    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent(ActivityMapAddress.this, ActivityMain.class);
                        startActivity(intent);
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
     //   mMap.clear();
        /** show current location and update location and time **/
        mCurrentLocation = location;
        mDisplayAddress = "Me";

        /** Display marker to current location **/
        addMarker();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    public LatLng getLatLogEnterAddress(String enterAddress) {

        addressMapList.clear();
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

    /**
     * Display marker to current location
     **/
    private void addMarker() {

        MarkerOptions options = new MarkerOptions();
        IconGenerator iconFactory = new IconGenerator(this);
        iconFactory.setStyle(IconGenerator.STYLE_PURPLE);

        options.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(mDisplayAddress)));
        options.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        /** Display current location with current LatLng **/
        double currentLatitude = mCurrentLocation.getLatitude();
        double currentLongitude = mCurrentLocation.getLongitude();


        enter_address_Latitude =Double.toString(currentLatitude);
        enter_address_Logitudel = Double.toString(currentLatitude);;

        LatLng currentLatLng = new LatLng(currentLatitude, currentLongitude);
        options.position(currentLatLng);

        /** Change the marker color **/
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        Marker mapMarker = mMap.addMarker(options.icon(bitmapDescriptor).title(mDisplayAddress));

        /** Display the perfect location place **/
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 13));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(currentLatitude, currentLongitude)).zoom(15).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public class AsynMessMapList extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(ActivityMapAddress.this);
            progressDialog.setIndeterminate(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String respons = "";
            ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
            MAP_SEARCH_LOCATION_LATITUDE = enter_address_Latitude;
            MAP_SEARCH_LOCATION_LONGITUDE = enter_address_Logitudel;

            StringBuilder addressmaplist_url = new StringBuilder(MAP_SEARCH_LOCATION_URL + "?task=getsearchlist" +
                    "&longitude=" + MAP_SEARCH_LOCATION_LONGITUDE + "&latitude=" + MAP_SEARCH_LOCATION_LATITUDE);

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(addressmaplist_url.toString());
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                respons = EntityUtils.toString(httpEntity);

                JSONObject jsonObject = new JSONObject(respons);
                JSONObject jsonObject1 = jsonObject.getJSONObject("allsearchlist");
                String status = jsonObject1.getString("status");
                String message = jsonObject1.getString("message");
                String count = jsonObject1.getString("count");

                JSONArray jsonArray = jsonObject1.getJSONArray("searchresult");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                    String name = jsonObject2.getString("name");
                    String business_name = jsonObject2.getString("business_name");
                    String food_type = jsonObject2.getString("food_type");
                    String kitchen_type = jsonObject2.getString("kitchen_type");
                    String delivery_option = jsonObject2.getString("delivery_option");
                    String delivery_radius = jsonObject2.getString("delivery_radius");
                    String full_tiffin_prize = jsonObject2.getString("full_tiffin_prize");
                    String half_tiffin_prize = jsonObject2.getString("half_tiffin_prize");
                    String opens_at = jsonObject2.getString("opens_at");
                    String closed_at = jsonObject2.getString("closed_at");
                    String address = jsonObject2.getString("address");
                    String sunday_close = jsonObject2.getString("sunday_close");
                    String mobile = jsonObject2.getString("mobile");
                    String created_date = jsonObject2.getString("created_date");
                    String published = jsonObject2.getString("published");
                    String latitude = jsonObject2.getString("latitude");
                    String longitude = jsonObject2.getString("longitude");
                    String distance = jsonObject2.getString("distance");
                    String oneTime = jsonObject2.getString("onetime_monthly");
                    String twoTime = jsonObject2.getString("twotime_monthly");

                    addressMapList.add(new M_MapAddress(name, business_name, food_type, kitchen_type, delivery_option,
                            delivery_radius, full_tiffin_prize, half_tiffin_prize, opens_at, closed_at, address,
                            sunday_close, mobile, created_date, published, latitude, longitude, distance,oneTime,twoTime));

                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return respons;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            mMap.clear();

            String userBusinessAddress;
            MarkerOptions markerOptions = new MarkerOptions();
            IconGenerator iconFactory = new IconGenerator(ActivityMapAddress.this);
            iconFactory.setStyle(IconGenerator.STYLE_PURPLE);

            M_MapAddress mFriend;

            double latitude, longitude;
            String id;

            for (int i = 0; i < addressMapList.size(); i++) {
                mFriend = addressMapList.get(i);

                // double latitude = Double.parseDouble(addressMapList.get(i).getLatitude());
                //  double longitude = Double.parseDouble(addressMapList.get(i).getLongitude());

                userBusinessAddress = addressMapList.get(i).getAddress();

                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(userBusinessAddress)));
                markerOptions.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

                String lat = addressMapList.get(i).getLatitude().toString();
                String log = addressMapList.get(i).getLongitude().toString();


                if (lat != null && !lat.equals("") && log != null && !log.equals("")) {
                    latitude = Double.parseDouble(lat);
                    longitude = Double.parseDouble(log);
                } else {
                    latitude = 0;
                    longitude = 0;
                }

                LatLng addressLatLng = new LatLng(latitude, longitude);
                markerOptions.position(addressLatLng);

                /** Change the marker color **/
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);

                Marker mapMarker = mMap.addMarker(markerOptions.icon(bitmapDescriptor));
                locationList1.put(mapMarker.getId(), mFriend);

                // Showing InfoWindow on the GoogleMap
                mapMarker.showInfoWindow();

                /** Display the perfect location place **/
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(addressLatLng, 13));

            }

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {

                    M_MapAddress marker_data = locationList1.get(marker.getId());

                    Intent intent = new Intent(ActivityMapAddress.this, ActivityMapDetails.class);

                    intent.putExtra("Mess_name", marker_data.getBusiness_name());
                    intent.putExtra("Mess_address", marker_data.getAddress());
                    intent.putExtra("Mess_phone", marker_data.getMobile());
                    intent.putExtra("Mess_openat", marker_data.getOpens_at());
                    intent.putExtra("Mess_closeat", marker_data.getClosed_at());
                    intent.putExtra("Mess_sunday", marker_data.getSunday_close());
                    intent.putExtra("Mess_foodtype", marker_data.getFood_type());
                    intent.putExtra("Mess_kitchen", marker_data.getKitchen_type());
                    intent.putExtra("Mess_delevery", marker_data.getDelivery_option());
                    intent.putExtra("Mess_full", marker_data.getFull_tiffin_prize());
                    intent.putExtra("Mess_dhalf", marker_data.getHalf_tiffin_prize());
                    intent.putExtra("Mess_onetime", marker_data.getOneTime());
                    intent.putExtra("Mess_twotime", marker_data.getTwoTime());

                    startActivity(intent);
                }
            });


            // Setting a custom info window adapter for the google map
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                // Use default InfoWindow frame
                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                // Defines the contents of the InfoWindow
                @Override
                public View getInfoContents(Marker marker) {
                    View v = null;

                    // Getting view from the layout file info_window_layout
                    if (v == null) {
                        v = getLayoutInflater().inflate(R.layout.infowindow_layout, null);

                    }

                    M_MapAddress marker_data = locationList1.get(marker.getId());

                    TextView txt_mess_name = (TextView) v.findViewById(R.id.txt_map_mess_name);
                    TextView txt_mess_address = (TextView) v.findViewById(R.id.txt_map_mess_address);
                    TextView txt_mess_distance = (TextView) v.findViewById(R.id.txt_map_distance_km);
                    TextView txt_mess_full = (TextView) v.findViewById(R.id.txt_map_full_tiffin_prize);
                    TextView txt_mess_half = (TextView) v.findViewById(R.id.txt_map_half_tiffin_prize);

                    ImageView img_mess_delivery = (ImageView) v.findViewById(R.id.img_map_deliver_options);
                    ImageView img_mess_food = (ImageView) v.findViewById(R.id.img_map_veg_nonveg);

                    String messName = marker_data.getBusiness_name();
                    String messAddress = marker_data.getAddress();
                    String messMobile = marker_data.getMobile();
                    String messOpenAt = marker_data.getOpens_at();
                    String messCloseAt = marker_data.getClosed_at();
                    String messSunday = marker_data.getSunday_close();
                    String messFoodType = marker_data.getFood_type();
                    String messKichanTpye = marker_data.getKitchen_type();
                    String messDelivery = marker_data.getDelivery_option();
                    String messFullTifffin = marker_data.getFull_tiffin_prize();
                    String messHalfTiffin = marker_data.getHalf_tiffin_prize();
                    String messDistance = marker_data.getDistance();

                    txt_mess_name.setText(messName);
                    txt_mess_address.setText(messAddress);
                    txt_mess_distance.setText(messDistance + "/Km");
                    txt_mess_full.setText(messFullTifffin + "/Rs");
                    txt_mess_half.setText(messHalfTiffin + "/Rs");

                    if (messDelivery.equals("Yes")) {
                        img_mess_delivery.setVisibility(View.VISIBLE);
                        img_mess_delivery.setImageResource(R.drawable.ic_delivary);
                    } else {
                        img_mess_delivery.setVisibility(View.VISIBLE);
                        img_mess_delivery.setImageResource(R.drawable.ic_notdelevery);
                    }

                    if (messFoodType.equals("Veg")) {
                        img_mess_food.setVisibility(View.VISIBLE);
                        img_mess_food.setImageResource(R.drawable.ic_veg);
                    } else {
                        img_mess_food.setVisibility(View.VISIBLE);
                        img_mess_food.setImageResource(R.drawable.ic_non_veg);
                    }


                    CommonMethods.setCustomFontCANADARA(ActivityMapAddress.this, txt_mess_name);
                    CommonMethods.setCustomFontCANADARA(ActivityMapAddress.this, txt_mess_address);
                    //  CommonMethods.setCustomFontCANADARA(ActivityMapAddress.this, txt_mess_distance);
                    // CommonMethods.setCustomFontCANADARA(ActivityMapAddress.this, txt_mess_full);
                    // CommonMethods.setCustomFontCANADARA(ActivityMapAddress.this, txt_mess_half);

                    return v;

                }

            });

        }
    }


}
