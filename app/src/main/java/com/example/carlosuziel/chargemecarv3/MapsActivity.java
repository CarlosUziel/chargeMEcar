package com.example.carlosuziel.chargemecarv3;/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This demo shows how GMS Location can be used to check for changes to the users location.  The
 * "My Location" button uses GMS Location to set the blue dot representing the users location.
 * Permission for {@link android.Manifest.permission#ACCESS_FINE_LOCATION} is requested at run
 * time. If the permission has not been granted, the Activity is finished with an error message.
 */
public class MapsActivity extends AppCompatActivity
        implements
        OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {


    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;
    private HashMap map;
    private int distance = 5;
    private Double[] myLocation = {28.073318, -15.451263};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createMarkerDict();
        setContentView(R.layout.charge_me_car);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void createMarkerDict(){
        map = new HashMap<String,Double[]>();

        //map.put("Marker 1", new Double[]{28.106016, -15.446016});
        map.put("Marker 2", new Double[]{28.128344, -15.448463});
        map.put("Marker 3", new Double[]{28.141448, -15.427295});
        map.put("Marker 4", new Double[]{28.125250, -15.432091});
        map.put("Marker 5", new Double[]{28.062840, -15.546022});
        map.put("Marker 6", new Double[]{28.150932, -15.533614});

        map.put("Marker 7", new Double[]{28.106848, -15.418456});
        map.put("Marker 8", new Double[]{28.115183, -15.449013});
        map.put("Marker 9", new Double[]{28.120474, -15.434859});
        map.put("Marker 10", new Double[]{28.129006, -15.441866});
        map.put("Marker 11", new Double[]{28.153813, -15.418177});

        map.put("Marker 11", new Double[]{28.104348, -15.426633});
        map.put("Marker 11", new Double[]{28.071120, -15.429805});
        map.put("Marker 11", new Double[]{28.117739, -15.446916});

    }

    /**
     * Puts the markers on the map
     */
    private void generateMarkers(){
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            Double[] coordinates = (Double[]) pair.getValue();

            LatLng marker = new LatLng(coordinates[0], coordinates[1]);
            if (getDistance((Double[]) pair.getValue()) < distance){
                if(Math.random() < 0.5){
                    mMap.addMarker(new MarkerOptions().position(marker).title((String) pair.getKey()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                }else{
                    mMap.addMarker(new MarkerOptions().position(marker).title((String) pair.getKey()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                }
            }else{
                mMap.addMarker(new MarkerOptions().position(marker).title((String) pair.getKey()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }

            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    private double getDistance(Double[] coordinates) {
        double lat1 = myLocation[0];
        double lon1 = myLocation[1];
        double lat2 = coordinates[0];
        double lon2 = coordinates[1];

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        //distance in KM
        dist = dist * 1.609344;

        return dist;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        generateMarkers();

        mMap.addCircle(new CircleOptions()
            .center(new LatLng(myLocation[0],myLocation[1]))
            .radius(distance*1000)
            .strokeColor(0x350d5b59)
            .fillColor(0x350d5b59));
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
        CameraUpdateFactory.zoomBy(6);

    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        //Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    /**
     *  Start after pressing findNearest button
     */
    public void findNearest(View view){

        Double[] nearestCoordinates = getNearestStation();
        LatLng nearest = new LatLng(nearestCoordinates[0],nearestCoordinates[1]);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(nearest));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));

    }

    private Double[] getNearestStation() {
        createMarkerDict();
        Iterator it = map.entrySet().iterator();
        Double[] nearestStation = {0.0,0.0,99999999.0};
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();

            Double[] coordinates = (Double[]) pair.getValue();

            double newDistance = getDistance((Double[]) pair.getValue());

            if (newDistance < nearestStation[2]) {
                nearestStation[0] = coordinates[0];
                nearestStation[1] = coordinates[1];
                nearestStation[2] = newDistance;
            }

            it.remove(); // avoids a ConcurrentModificationException
        }
        return nearestStation;
    }

}
