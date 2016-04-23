package br.uvv.carona.activity;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.uvv.carona.R;
import br.uvv.carona.application.AppPartiUVV;
import br.uvv.carona.asynctask.GetRouteAsyncTask;
import br.uvv.carona.model.Place;
import br.uvv.carona.model.route.RouteResult;
import br.uvv.carona.service.GeolocationService;
import br.uvv.carona.util.EnumUtil;
import br.uvv.carona.util.EventBusEvents;

public class MapActivity extends BaseActivity implements GoogleMap.OnMapLoadedCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback, GoogleMap.OnMapClickListener{

    public static final String TYPE_MAP_REQUEST = ".TYPE_MAP_REQUEST";
    public static final String PLACES_TAG = ".PLACES_TAG";
    public static final String DEPARTURE_TAG = ".DEPARTURE_TAG";
    public static final String DESTINATION_TAG = ".DESTINATION_TAG";

    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private GoogleApiClient mApiClient;

    private EnumUtil.MapRequestEnum mTypeMapRequest;

    private Marker mSelectedPlaceA;
    private Marker mSelectedPlaceB;

    private Polyline mRoute;
    private List<Polyline> mRoutes;

    private boolean mSelectDepartureLocation = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        if(savedInstanceState == null){
            this.mTypeMapRequest = (EnumUtil.MapRequestEnum)getIntent().getSerializableExtra(TYPE_MAP_REQUEST);
        }else{
            this.mTypeMapRequest = (EnumUtil.MapRequestEnum)savedInstanceState.getSerializable(TYPE_MAP_REQUEST);
        }

        if(this.mTypeMapRequest != EnumUtil.MapRequestEnum.MarkRoute){
            this.findViewById(R.id.selectOptionWrapper).setVisibility(View.GONE);
            this.mSelectDepartureLocation = true;
        }

        this.mApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        this.mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        this.mMapFragment.getMapAsync(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(TYPE_MAP_REQUEST, this.mTypeMapRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLoadedCallback(this);
        this.mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.mMap.getUiSettings().setCompassEnabled(false);
        this.mMap.getUiSettings().setMyLocationButtonEnabled(false);
        this.mMap.setBuildingsEnabled(false);

        if(android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.M &&
                !AppPartiUVV.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)){
            //TODO Show Dialog and request
        }else {
            this.mMap.setMyLocationEnabled(true);
            LatLng userPosition = getCurrentLocationPosition();
            if(userPosition != null) {
                this.goToLatLng(userPosition);
            }
        }
    }
    public void goToLatLng(LatLng latLng){
        CameraPosition cameraPosition = this.mMap.getCameraPosition();
        cameraPosition = new CameraPosition.Builder()
                .zoom(cameraPosition.zoom < 14.3f ? 15.2f : cameraPosition.zoom)
                .target(latLng)
                .build();
        this.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    protected LatLng getCurrentLocationPosition() {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mApiClient);
        if(location == null){
            return null;
        } else{
            return new LatLng(location.getLatitude(), location.getLongitude());
        }
    }

    @Override
    public void onMapLoaded() {

    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(this.mSelectDepartureLocation){
            if(this.mSelectedPlaceA != null){
                this.mSelectedPlaceA.remove();
                this.mSelectedPlaceA = null;
            }

            MarkerOptions markerOptions = new MarkerOptions().position(latLng).anchor(0.5f, 1.0f);
            this.mSelectedPlaceA = this.mMap.addMarker(markerOptions);
        }else{
            if(this.mSelectedPlaceB != null){
                this.mSelectedPlaceB.remove();
                this.mSelectedPlaceB = null;
            }

            MarkerOptions markerOptions = new MarkerOptions().position(latLng).anchor(0.5f, 1.0f);
            this.mSelectedPlaceB = this.mMap.addMarker(markerOptions);
        }

        if(this.mSelectedPlaceA != null && this.mSelectedPlaceB != null && this.mTypeMapRequest == EnumUtil.MapRequestEnum.MarkRoute){
            new GetRouteAsyncTask(getString(R.string.google_map_api_key)).execute(this.mSelectedPlaceA.getPosition(),this.mSelectedPlaceB.getPosition());
            this.startProgressDialog(R.string.msg_getting_route);
            if(this.mRoute != null){
                this.mRoute.remove();
                this.mRoute = null;
            }
        }
    }

    public void onMarkByClick(View view){
        if(view.getId() == R.id.selectDeparture){
            this.mSelectDepartureLocation = true;
        }else{
            this.mSelectDepartureLocation = false;
        }
    }

    public void onConfirmPlaceClick(View view){
        if(this.mSelectedPlaceA == null || (this.mSelectedPlaceB == null && this.mTypeMapRequest == EnumUtil.MapRequestEnum.MarkRoute)){
            //TODO SHOW ERROR TO USER
            Toast.makeText(this, "Selecione todos os pontos necessários", Toast.LENGTH_LONG).show();
        }else {
            if(this.mTypeMapRequest == EnumUtil.MapRequestEnum.MarkRoute){

            }else {
                Intent intent = new Intent(this, GeolocationService.class);
                intent.putExtra(GeolocationService.LAT_LNG_A_TAG, this.mSelectedPlaceA.getPosition());
                if (this.mTypeMapRequest == EnumUtil.MapRequestEnum.MarkRoute) {
                    intent.putExtra(GeolocationService.LAT_LNG_B_TAG, this.mSelectedPlaceB.getPosition());
                }
                startService(intent);
            }
        }
    }

    public void onCancelClick(View view){
        Intent result = new Intent();
        setResult(RESULT_CANCELED, result);
        finish();
    }

    private void sendResult(List<Place> places){
        Intent result = new Intent();
        result.putExtra(PLACES_TAG, (Serializable) places);
        setResult(RESULT_OK, result);
        finish();
    }

    @Subscribe
    public void getPlaceAddress(EventBusEvents.PlaceAddressEvent event){
        //TODO verify event's content
        if (this.mTypeMapRequest == EnumUtil.MapRequestEnum.AddPlace) {

        } else if (this.mTypeMapRequest == EnumUtil.MapRequestEnum.MarkRoute) {

        }
        sendResult(event.places);
    }

    @Subscribe
    public void getRoute(EventBusEvents.RouteEvent event){
        this.mRoute = this.mMap.addPolyline(new PolylineOptions()
                        .addAll(event.latLngs)
                        .width(12)
                        .color(0xDFAF5f5F)//Google maps blue color
                        .geodesic(true)
        );
        this.stopProgressDialog();
    }
}
