package com.lavrov.mapdemo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MainActivity extends Activity {

    private IMapController myMapController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);

        MapView mapView = (MapView) findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        myMapController = mapView.getController();
        myMapController.setZoom(14);
        GeoPoint cameraLocation = new GeoPoint(43.1190894D, 131.9024494D);
        myMapController.setCenter(cameraLocation);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            GpsMyLocationProvider gpsMyLocationProvider = new GpsMyLocationProvider(mapView.getContext()) {
                @Override
                public void onLocationChanged(Location location) {
                    super.onLocationChanged(location);
                    myMapController.animateTo(new GeoPoint(location));
                }
            };
            gpsMyLocationProvider.setLocationUpdateMinTime(500);
            gpsMyLocationProvider.setLocationUpdateMinDistance(10);

            MyLocationNewOverlay locationNewOverlay = new MyLocationNewOverlay(mapView);
            locationNewOverlay.enableMyLocation(gpsMyLocationProvider);

            mapView.getOverlays().add(locationNewOverlay);
        }

    }

}
