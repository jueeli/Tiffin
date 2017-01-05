package com.svmindlogic.tiffin.tiffinmap.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class NetWorkConnection {

    public boolean isWifiConnected = false;
    public boolean isMobileConnected = false;
    public boolean isNetConnected = false;

    public static String readFromFile(String path, Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(path);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(
                        inputStream);
                BufferedReader bufferedReader = new BufferedReader(
                        inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
                ret.getBytes();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    public boolean isNetAvailable(Object connectivityService) {

        ConnectivityManager connectivityManager = (ConnectivityManager) connectivityService;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null
                && activeNetworkInfo.isConnectedOrConnecting()) {

            isNetConnected = true;
            isWifiConnected = activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            isMobileConnected = activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        } else {

            isNetConnected = false;
            isWifiConnected = false;
            isMobileConnected = false;
        }
        return isNetConnected;

    }

}
