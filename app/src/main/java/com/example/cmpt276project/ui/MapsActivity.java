package com.example.cmpt276project.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.cmpt276project.R;
import com.example.cmpt276project.model.AllRestaurant;
import com.example.cmpt276project.model.Inspection;
import com.example.cmpt276project.model.Restaurant;
import com.example.cmpt276project.model.RestaurantManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;


import java.util.Objects;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private RestaurantManager manager;

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    private Location mLastKnownLocation;
    private final LatLng surrey = new LatLng(49.187500, -122.849000);
    private final int DEFAULT_ZOOM = 10;

    // Declare a variable for the cluster manager.
    private ClusterManager<AllRestaurant> mClusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        manager = RestaurantManager.getInstance();

        // Construct a GeoDataClient.
//        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
//        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        setupToolbar();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }

    // Setup toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);

        MenuItem viewRestaurantListItem = (MenuItem) menu.findItem(R.id.ToolbarMenu_switch_context);
        viewRestaurantListItem.setVisible(true);
        viewRestaurantListItem.setTitle(R.string.MapsActivity_toolbar_list_btn_text);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.ToolbarMenu_back:
                finish();
                return true;
            case R.id.ToolbarMenu_switch_context:
                startActivity(RestaurantListActivity.makeIntent(this));
                return true;
        }
        return super.onOptionsItemSelected(item);
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

        // get permission
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();


        // set clusters / add restaurant markers
        setUpCluster();

    }

    /**
    *   Reference Document: Google Maps Platform: https://developers.google.com/maps/documentation/javascript/adding-a-google-map
    */

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d("Get Current Location", "Current location is null. Using defaults.");
                            Log.e("Get Current Location", "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(surrey, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    // Marker clustering
    private void setUpCluster() {
        // Position the map.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<AllRestaurant>(this, mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.

        mMap.setOnCameraIdleListener(mClusterManager);

        mMap.setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        addRestaurant();

        mClusterManager.setRenderer(new MyClusterRenderer(getApplicationContext()));
    }

    public Bitmap resizeMapIcons (String iconName, int width, int height) {
        Bitmap imageBitmap  = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    // add restaurant markers
    private void addRestaurant() {
        for (Restaurant tmp:manager.getRestaurantList()) {
            double lat = tmp.getLatitude();
            double lng = tmp.getLongitude();
            String title = tmp.getName();
            String snippet;
            String hazard;
            Inspection inspection = tmp.getInspectionByIndex(0);

            switch (inspection.getHazardRating()) {
                case LOW:
                    snippet = "Hazard Level: LOW";
                    hazard = "hazard_low";
                    break;
                case MODERATE:
                    snippet = "Hazard Level: MODERATE";
                    hazard = "hazard_mid";
                    break;
                case HIGH:
                    snippet = "Hazard Level: HIGH";
                    hazard = "hazard_high";
                    break;
                default:
                    snippet = "Hazard Level: No Inspection Yet";
                    hazard = "";
                    break;
            }

            AllRestaurant offsetItem = new AllRestaurant(lat, lng, title, snippet, hazard);
            mClusterManager.addItem(offsetItem);
        }
    }

    private class MyClusterRenderer extends DefaultClusterRenderer<AllRestaurant> {
        public MyClusterRenderer(Context context) {
            super(context, mMap, mClusterManager);
        }
        @Override
        protected void onBeforeClusterItemRendered(@NonNull AllRestaurant item, @NonNull MarkerOptions markerOptions) {
//            super.onBeforeClusterItemRendered(item, markerOptions);
            Bitmap resized = resizeMapIcons(item.getHazard(), 100, 100);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resized));
        }
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, MapsActivity.class);
    }

}