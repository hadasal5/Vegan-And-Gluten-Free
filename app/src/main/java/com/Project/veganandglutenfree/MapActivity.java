package com.Project.veganandglutenfree;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
//import com.google.android.gms.location.LocationListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener,GoogleMap.OnMarkerClickListener{

    GoogleMap mMap;
    Button returnToMainMenu;

    LocationManager locationManager;
    LocationListener locationListener;
    double lat,lon;

    private DatabaseReference restRef;
    private DatabaseReference buisRef;
    Marker marker;

    Marker buis_marker;

    List<Rests> restList;
    List<Buiss> buisList;

    //Map<String, String> mMarkerMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        returnToMainMenu = findViewById(R.id.buttonReturnToMainMenuFromProfile);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        ChildEventListener mChildEventListener;
        restRef= FirebaseDatabase.getInstance().getReference("resturant");
        restRef.push().setValue(marker);

        buisRef= FirebaseDatabase.getInstance().getReference("buisness");
        buisRef.push().setValue(buis_marker);

        restList = new ArrayList<>();
        buisList = new ArrayList<>();

        returnToMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MapActivity.this, MainMenu.class);
                startActivity(i);
                finish();
            }
        });

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();

                Log.e("lat: ", String.valueOf(lat));
                Log.e("lon: ", String.valueOf(lon));

            }
        };


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}
                    ,1);
        }

        else
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,500,50, locationListener);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1 && permissions.length >0 && ContextCompat.checkSelfPermission(this
                ,Manifest.permission.ACCESS_FINE_LOCATION) ==PackageManager.PERMISSION_GRANTED)
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,500,50, locationListener);

        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng userLocation = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions()
                .position(userLocation)
                .title("Your location"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(telaviv));

        googleMap.setOnMarkerClickListener(this);
        int height = 80;
        int width = 80;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.green_marker);
        Bitmap b=bitmapdraw.getBitmap();
        final Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        restRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot  s : dataSnapshot.getChildren()){
                    Rests rest = s.getValue(Rests.class);
                    restList.add(rest);
                    LatLng location=new LatLng((rest.lat),(rest.lon));
                    mMap.addMarker(new MarkerOptions().position(location).title(rest.name)).setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 13));



                    //Log.e("list ", String.valueOf(restList.size()));

                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        buisRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot  s : dataSnapshot.getChildren()){
                    Buiss buis = s.getValue(Buiss.class);
                    buisList.add(buis);
                    LatLng location=new LatLng((buis.lat),(buis.lon));
                    buis_marker = mMap.addMarker(new MarkerOptions().position(location).title(buis.name));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 13));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
/*
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {

                String title = marker.getTitle();
                Rests rest = null;

                for (int i = 0; i < restList.size(); i++) {
                    if (restList.get(i).getName().equals(title))
                        rest = restList.get(i);
                }
                Intent intent = new Intent(MapActivity.this, RestProfile.class);
                intent.putExtra("restIDs", rest.id);
                startActivity(intent);

                return false;
            }

        });
        */

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                if (buis_marker.equals(marker)) {
                    String title = marker.getTitle();
                    Buiss buis = null;
                    for (int i = 0; i < buisList.size(); i++) {
                        if (buisList.get(i).getName().equals(title))
                            buis = buisList.get(i);
                    }
                    //loadNavigationView(String.valueOf(buis.lat),String.valueOf(buis.lon));

                    Intent intent2 = new Intent(MapActivity.this, BuisProfile.class);
                    intent2.putExtra("buisIDs", buis.id);
                    startActivity(intent2);
                }
                else {
                    String title = marker.getTitle();
                    Rests rest = null;

                    for (int i = 0; i < restList.size(); i++) {
                        if (restList.get(i).getName().equals(title))
                            rest = restList.get(i);
                    }

                    //loadNavigationView(String.valueOf(rest.lat),String.valueOf(rest.lon));

                    Intent intent = new Intent(MapActivity.this, RestProfile.class);
                    intent.putExtra("restIDs", rest.id);
                    startActivity(intent);
                }
                return false;
            }


        });
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }


/*
    public void loadNavigationView(String lat,String lng){
        Uri navigation = Uri.parse("google.navigation:q="+lat+","+lng+"");
        Intent navigationIntent = new Intent(Intent.ACTION_VIEW, navigation);
        navigationIntent.setPackage("com.google.android.apps.maps");
        startActivity(navigationIntent);
    }
*/

}