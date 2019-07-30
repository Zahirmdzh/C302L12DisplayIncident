package sg.edu.rp.c302.c302l12displayincident;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Incident data;
    Double latitude, longitude;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent i = getIntent();
        data = (Incident) i.getSerializableExtra("incident");

        if (i.getExtras() != null) {
            latitude = data.getLatitude();
            Log.d("LATITUDE", latitude.toString());
            longitude = data.getLongitude();
            Log.d("LONGITUDE", longitude.toString());

            type = data.getType();
        }
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

        // Add a marker in Sydney and move the camera
        LatLng poi = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(poi).title(type));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(poi,18));

        UiSettings ui = mMap.getUiSettings();

        ui.setCompassEnabled(true);
        ui.setZoomControlsEnabled(true);

        LatLng poiN = new LatLng(latitude, longitude);
        Marker marker = mMap.addMarker(new
                MarkerOptions()
                .position(poiN)
                .title(type)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    }
}
