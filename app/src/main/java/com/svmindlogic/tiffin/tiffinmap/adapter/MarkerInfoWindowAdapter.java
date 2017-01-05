package com.svmindlogic.tiffin.tiffinmap.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.svmindlogic.tiffin.tiffinmap.ActivityMapAddress;
import com.svmindlogic.tiffin.tiffinmap.model.M_MapAddress;
import com.svmindlogic.tiffinmap.R;

/**
 * Created by akash on 26/12/15.
 */
public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter
{
    public MarkerInfoWindowAdapter()
    {
    }

    @Override
    public View getInfoWindow(Marker marker)
    {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker)
    {
//        View v  = getLayoutInflater().inflate(R.layout.infowindow_layout, null);
//
//        M_MapAddress myMarker =ActivityMapAddress.addressMapList.get(marker);
//        TextView markerLabel = (TextView)v.findViewById(R.id.title);
//        markerLabel.setText(myMarker.getAddress());

        return null;
    }
}