package com.svmindlogic.tiffin.tiffinmap;


import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.svmindlogic.tiffin.tiffinmap.adapter.PlacesAutoCompleteAdapter;
import com.svmindlogic.tiffin.tiffinmap.listeners.RecyclerItemClickListener;
import com.svmindlogic.tiffin.tiffinmap.utils.Constants;
import com.svmindlogic.tiffinmap.R;

import java.util.List;
import java.util.Locale;

//import com.google.android.gms.wallet.Address;

/**
 * Created by softbunch on 15/12/15.
 */
public class ActivityCurrentMap extends AppCompatActivity implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapLongClickListener, OnMapClickListener, OnMarkerDragListener {

    private static final String TAG = "ActivityMapAddress";
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mDisplayAddress, changeAddress;
    Button btnContinue;
    double map_latitude, map_longitude;
    String address_Latitude, address_Logitudel;
    LatLng addressLatLng = null;
    //  EditText edtMapAddressChange;
    private GoogleMap mMap;
    EditText edtMapAddressChange;


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
                .addConnectionCallbacks(ActivityCurrentMap.this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
        /** Set .xml **/
        setContentView(R.layout.activity_map_current);


        getSupportActionBar().hide();
        /** Add Action Bar **/
       // getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
      //  getSupportActionBar().setCustomView(R.layout.et_search_bar);
        /** Action Bar view Ids **/
        //  ImageView imgBackArrow = (ImageView) findViewById(R.id.img_arrow_back);
        ImageView imgsearch = (ImageView) findViewById(R.id.img_search_address_map);
        ImageView imgAddressList = (ImageView) findViewById(R.id.img_address_list);
        ImageView imgArrowBack = (ImageView) findViewById(R.id.img_arrow_back);
        edtMapAddressChange = (EditText) findViewById(R.id.et_input_search);
        imgAddressList.setVisibility(View.GONE);
        // imgAddressList.setBackgroundResource(R.drawable.ic_mess_list);

       // Toast.makeText(ActivityCurrentMap.this, "Long press on Marker to set accurrate Address", Toast.LENGTH_SHORT).show();


        AlertDialog.Builder alert = new AlertDialog.Builder(ActivityCurrentMap.this);
       // alert.setTitle("Doctor");
        alert.setMessage("Long press on RED Marker and  move to adjust");
        alert.setPositiveButton("OK", null);
        alert.show();


        mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(this, R.layout.searchview_adapter,
                mGoogleApiClient, BOUNDS_INDIA, null);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        mLinearLayoutManager=new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ActivityCurrentMap.this));

        mRecyclerView.setAdapter(mAutoCompleteAdapter);

        btnContinue = (Button) findViewById(R.id.btn_continue);
        //  edtMapAddressChange = (EditText) findViewById(R.id.edt_map_address_change);


        /** Map Fragment **/
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mMap = mapFragment.getMap();
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMapClickListener(this);

        /** Check gps on or off **/
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
        } else {
            showGPSDisabledAlertToUser();
        }

        mDisplayAddress = getIntent().getExtras().getString("ADDRESS");

        //edtMapAddressChange.setText(mDisplayAddress);
        changeAddress = edtMapAddressChange.getText().toString().trim();

        getLocationFromAddress(mDisplayAddress);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(ActivityCurrentMap.this,ActivityPostAddress.class);
                MarkerOptions marker = new MarkerOptions().position(new LatLng(map_latitude, map_longitude));
                LatLng latLng = marker.getPosition();
                double latitude1 = latLng.latitude;
                double longitude1 = latLng.longitude;
                // Toast.makeText(ActivityCurrentMap.this, "" + latitude1 + longitude1, Toast.LENGTH_SHORT).show();
                address_Latitude = Double.toString(latitude1);
                address_Logitudel = Double.toString(longitude1);

                Intent intent = new Intent();
                intent.putExtra("LOCATION_LAT", address_Latitude);
                intent.putExtra("LOCATION_LOG", address_Logitudel);
                setResult(RESULT_OK, intent);
                finish();

            }
        });

        edtMapAddressChange.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mMap.clear();
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
                                    edtMapAddressChange.setText(placeName);
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

                if (edtMapAddressChange.getText().toString().trim() == null || edtMapAddressChange.getText().toString().trim().equals("")) {
                    Toast.makeText(ActivityCurrentMap.this, "Enter Location", Toast.LENGTH_SHORT).show();
                } else {
                    //if(mDisplayAddress == edtMapAddressChange.getText().toString().trim()) {
                    mDisplayAddress=edtMapAddressChange.getText().toString().trim();
                        getLocationFromAddress(mDisplayAddress);
                  //  }

                }
            }
        });

        imgArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityCurrentMap.this, ActivityPostAddress.class);
                startActivity(intent);
                finish();
            }
        });

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
                        Intent intent = new Intent(ActivityCurrentMap.this, ActivityPostAddress.class);
                        startActivity(intent);
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    /**
     * get latlng from address
     **/
    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this, Locale.getDefault());
        List<Address> address;


        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            map_latitude = address.get(0).getLatitude();
            map_longitude = address.get(0).getLongitude();

            address_Latitude = Double.toString(map_latitude);
            address_Logitudel = Double.toString(map_longitude);

            MarkerOptions markerOptions = new MarkerOptions();

            IconGenerator iconFactory = new IconGenerator(this);
            iconFactory.setStyle(IconGenerator.STYLE_PURPLE);

            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(strAddress)));
            markerOptions.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

            addressLatLng = new LatLng(map_latitude, map_longitude);

            markerOptions.position(addressLatLng);

            /** Change the marker color **/
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
            //Marker mapMarker = mMap.addMarker(options)
            Marker mapMarker = mMap.addMarker(markerOptions.icon(bitmapDescriptor).title(strAddress));
            mapMarker.setDraggable(true);

            mapMarker.setTitle(strAddress);
            /** Display the perfect location place **/
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(addressLatLng, 13));


        } catch (Exception e) {
            e.printStackTrace();
        }
        return addressLatLng;
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


    /**
     * Default method of GoogleApiClient.ConnectionCallback
     **/
    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.
                requestLocationUpdates(mGoogleApiClient, mLocationRequest, ActivityCurrentMap.this);
        Log.d(TAG, "Location update started ..............: ");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mMap.clear();
        /** Display marker to current location **/
        // addMarker();
        if (mDisplayAddress == changeAddress) {
            getLocationFromAddress(mDisplayAddress);
        } else {
            getLocationFromAddress(changeAddress);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
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
        mGoogleApiClient.disconnect();
    }


    // Add below all method to drag and drop
    @Override
    public void onMarkerDrag(Marker arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMarkerDragEnd(Marker arg0) {
        // TODO Auto-generated method stub
        LatLng dragPosition = arg0.getPosition();
        double dragLat = dragPosition.latitude;
        double dragLong = dragPosition.longitude;
        map_latitude = dragLat;
        map_longitude = dragLong;
        //   Toast.makeText(getApplicationContext(), "Marker Dragged..!" + map_latitude + " " + map_longitude, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMarkerDragStart(Marker arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMapClick(LatLng arg0) {
        // TODO Auto-generated method stub
        mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0));
    }


    @Override
    public void onMapLongClick(LatLng arg0) {
        // TODO Auto-generated method stub

       /* //create new marker when user long clicks
        map.addMarker(new MarkerOptions()
                .position(arg0)
                .draggable(true));*/


    }

    @Override
    public void onBackPressed() {
        //   super.onBackPressed();

    }
}
