package com.bas.android.muralmaps;

/**
 * Created by Bas on 10/9/17.
 */


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import io.realm.RealmResults;


public class Maps_tab extends Fragment implements OnMapReadyCallback, LocationListener {


    private GoogleMap mMap;
    private MapView mapView;
    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.maps_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivity = (MainActivity) this.getActivity();

        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent(getContext(), ArtActivity.class);
                intent.putExtra("art",marker.getTitle());
                startActivity(intent);
                return false;
            }
        });

        // Add a marker in Sydney and move the camera

        LatLng dsm = new LatLng(41.583, -93.639);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dsm, 17.0f));

        ArrayList<Art> availableArt = getAvailableArt();
        for (Art a : availableArt) {
            LatLng pin = new LatLng(a.getLat(), a.getLng());
            mMap.addMarker(new MarkerOptions().position(pin).title(a.getId()));
        }

        if (ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        mMap.setMyLocationEnabled(true);

    }

    public ArrayList<Art> getAvailableArt(){
        ArrayList<Art> art2 = new ArrayList<Art>();
        RealmResults<Art> arts = mainActivity.realm.where(Art.class).findAll();
        String choice = mainActivity.filterSpinner.getSelectedItem().toString();

        if (choice == "Most Popular") {
            for (Art art: arts)
            {
                if(art2.isEmpty())
                {
                    art2.add(art);
                }
                for(Art artX: art2)
                {
                    if (art.getPopularity() > artX.getPopularity())
                    {
                        int posi = art2.indexOf(artX);
                        art2.add(posi, art);
                    }
                    else{
                        art2.add(art);
                    }

                }

            }
        }
        if (choice == "Favorites")
        {
            for (Art art : arts)
            {
                if(art.getLike())
                {
                    art2.add(art);
                }
            }
        }
        else {
            for (Art art : arts) {
                System.out.println(art);
                art2.add(art);
                // Boolean isPresent = false;
                // try {
                //     for (Vote vote : art.getVotes()) {
                //        Log.d("Warning: OWNER USERNAME", vote.getOwner().getUsername().toString());
                //      Log.d("MYUSERNAMEINPUT", mainActivity.user.getUsername().toString());
//
                //                  if (vote.getOwner().getUsername().equals(mainActivity.user.getUsername())) {
                //                    isPresent = true;
                //              }
                //        }
                //      if (isPresent == false) {
                //        art2.add(art);
                //  }
                //}catch (Exception e){ Log.d("Exception", e.getMessage()+e.getStackTrace());}
            }

        }
        return art2;
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


}

