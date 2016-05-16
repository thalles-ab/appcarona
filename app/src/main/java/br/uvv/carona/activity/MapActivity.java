package br.uvv.carona.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import br.uvv.carona.dialog.ConfirmRideOfferDialog;
import br.uvv.carona.dialog.MessageDialog;
import br.uvv.carona.dialog.NewLocationConfirmDialog;
import br.uvv.carona.model.Place;
import br.uvv.carona.model.Ride;
import br.uvv.carona.model.route.RouteRequest;
import br.uvv.carona.service.GeolocationService;
import br.uvv.carona.util.EventBusEvents;
import br.uvv.carona.util.MapRequestEnum;

public class MapActivity extends BaseActivity implements GoogleMap.OnMapLoadedCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback, GoogleMap.OnMapClickListener, OnMarkerDragListener, OnMarkerClickListener{

    public static final String TYPE_MAP_REQUEST = ".TYPE_MAP_REQUEST";
    public static final String PLACE_TAG = ".PLACE_TAG";
    public static final String DEPARTURE_TAG = ".DEPARTURE_TAG";
    public static final String DESTINATION_TAG = ".DESTINATION_TAG";
    public static final String MARKERS_LATLNG_TAG = ".MARKERS_LATLNG_TAG";
    public static final String DRAWED_ROUTE_TAG = ".DRAWED_ROUTE_TAG";

    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private GoogleApiClient mApiClient;

    private MapRequestEnum mTypeMapRequest;

    private Marker mDepartureMarker;
    private Marker mDestinationMarker;
    private Place mDeparturePlace;
    private Place mDestinationPlace;

    private Polyline mRoute;
    private List<Marker> mMarkers;
    private List<LatLng> mMarkersLatLng;
    private Ride mNewRideRoute;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        if(savedInstanceState == null){
            this.mTypeMapRequest = (MapRequestEnum)getIntent().getSerializableExtra(TYPE_MAP_REQUEST);
            this.mDeparturePlace = (Place)getIntent().getSerializableExtra(DEPARTURE_TAG);
            this.mDestinationPlace = (Place)getIntent().getSerializableExtra(DESTINATION_TAG);
        }else{
            this.mTypeMapRequest = (MapRequestEnum)savedInstanceState.getSerializable(TYPE_MAP_REQUEST);
            this.mDeparturePlace = (Place)savedInstanceState.getSerializable(DEPARTURE_TAG);
            this.mDestinationPlace = (Place)savedInstanceState.getSerializable(DESTINATION_TAG);
            this.mMarkersLatLng = savedInstanceState.getParcelableArrayList(MARKERS_LATLNG_TAG);
            this.mNewRideRoute = (Ride)savedInstanceState.getSerializable(DRAWED_ROUTE_TAG);
        }
        this.mMarkers = new ArrayList<>();

        this.mApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        this.mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        this.mMapFragment.getMapAsync(this);

        setupActionBar();
    }

    private void setupActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        if(this.mTypeMapRequest == MapRequestEnum.MarkRoute){
            actionBar.setTitle(R.string.lbl_offer_ride);
            actionBar.setSubtitle(R.string.lbl_route);
        }else{
            actionBar.setTitle(R.string.lbl_add_location);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        this.mApiClient.connect();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(TYPE_MAP_REQUEST, this.mTypeMapRequest);
        if(this.mTypeMapRequest == MapRequestEnum.MarkRoute){
            outState.putSerializable(DEPARTURE_TAG, this.mDeparturePlace);
            outState.putSerializable(DESTINATION_TAG, this.mDestinationPlace);
            if(this.mMarkers != null){
                List<LatLng> positions = new ArrayList<>();
                for(int i = 0; i < this.mMarkers.size(); i++){
                    positions.add(this.mMarkers.get(i).getPosition());
                }
                outState.putParcelableArrayList(MARKERS_LATLNG_TAG, (ArrayList)positions);
            }
            if(this.mNewRideRoute != null){
                outState.putSerializable(DRAWED_ROUTE_TAG, this.mNewRideRoute);
            }
        }else {
            if (this.mDepartureMarker != null) {
                Place place = new Place();
                place.latitude = this.mDepartureMarker.getPosition().latitude;
                place.longitude = this.mDepartureMarker.getPosition().longitude;
                outState.putSerializable(DEPARTURE_TAG, place);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.mApiClient.disconnect();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ride, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_confirm:
                this.onConfirmPlaceClick();
                return true;
            case android.R.id.home:
                this.onCancelClick();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onSuccess(EventBusEvents.SuccessEvent event){
        if(event.success){
            MessageDialog dialog = MessageDialog.newInstance(getString(R.string.msg_ride_sent), new MessageDialog.OnDialogButtonClick() {
                @Override
                public void onConfirmClick(Dialog dialog) {
                    dialog.dismiss();
                    Intent intent = new Intent(dialog.getContext(), HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                }
            });
        }
        this.stopProgressDialog();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if(this.mMap != null) {
            if ((android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.M &&
                    AppPartiUVV.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION)) || android.os.Build.VERSION.SDK_INT != Build.VERSION_CODES.M) {
                LatLng userPosition = getCurrentLocationPosition();
                if (userPosition != null) {
                    this.goToLatLng(userPosition, 1);
                }
            }
        }
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
        this.mMap.setBuildingsEnabled(false);
        this.mMap.setOnMarkerDragListener(this);
        this.mMap.setOnMarkerClickListener(this);

        if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.M &&
                !AppPartiUVV.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
            enableGoToUserLocation(false);
        } else {
            enableGoToUserLocation(true);
        }

        if(this.mDeparturePlace != null) {
            this.mDepartureMarker = this.mMap.addMarker(new MarkerOptions().draggable(false)
                    .position(new LatLng(this.mDeparturePlace.latitude, this.mDeparturePlace.longitude))
                    .anchor(0.5f, 0.62f)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        }
        if(this.mDestinationPlace != null) {
            this.mDestinationMarker = this.mMap.addMarker(new MarkerOptions().draggable(false)
                    .position(new LatLng(this.mDestinationPlace.latitude, this.mDestinationPlace.longitude))
                    .anchor(0.5f, 0.62f)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
        if(this.mTypeMapRequest == MapRequestEnum.MarkRoute){
            if(this.mMarkersLatLng != null){
                for(int i = 0; i < this.mMarkersLatLng.size(); i++){
                    this.mMarkers.add(drawMarker(this.mMarkersLatLng.get(i), BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE), true));
                }
            }
            if(this.mDepartureMarker != null && this.mDestinationMarker != null && this.mNewRideRoute == null){
                makeRouteRequest();
            }else if(mNewRideRoute != null){
                getRoute(new EventBusEvents.RouteEvent(mNewRideRoute));
            }
        }
    }
    public void enableGoToUserLocation(boolean enable){
        if(this.mMap != null){
            if(enable) {
                this.mMap.setMyLocationEnabled(true);
                LatLng cameraPosition = this.mMap.getCameraPosition().target;
                if (cameraPosition.latitude == 0 && cameraPosition.longitude == 0) {
                    LatLng position = getCurrentLocationPosition();
                    if (position != null) {
                        this.goToLatLng(position, 1);
                    }
                }
            }else{
                this.mMap.setMyLocationEnabled(false);
            }
        }
    }
    public void goToLatLng(LatLng latLng, int duration){
        CameraPosition cameraPosition = this.mMap.getCameraPosition();
        cameraPosition = new CameraPosition.Builder()
                .zoom(cameraPosition.zoom < 14.3f ? 15.2f : cameraPosition.zoom)
                .target(latLng)
                .build();
        this.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), duration, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onCancel() {

            }
        });
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
        if (this.mTypeMapRequest == MapRequestEnum.MarkRoute) {
            this.mMarkers.add(drawMarker(latLng, BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE), true));
            makeRouteRequest();
        } else {
            if (this.mDepartureMarker != null) {
                this.mDepartureMarker.remove();
                this.mDepartureMarker = null;
            }

            this.mDepartureMarker = drawMarker(latLng, BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN), true);
        }
    }

    private Marker drawMarker(LatLng position, BitmapDescriptor icon, boolean draggable){
        return this.mMap.addMarker(new MarkerOptions().position(position)
                .anchor(0.5f, 1.0f)
                .icon(icon)
                .draggable(draggable));
    }

    private void makeRouteRequest(){
        List<LatLng> positions = null;
        if(this.mMarkers.size() > 0){
            int size = this.mMarkers.size();
            positions = new ArrayList<>();
            for(int i = 0; i < size; i++){
                positions.add(this.mMarkers.get(i).getPosition());
            }
        }
        new GetRouteAsyncTask(getString(R.string.google_map_api_key))
                .execute(new RouteRequest(this.mDepartureMarker.getPosition(), this.mDestinationMarker.getPosition(), positions));
        this.startProgressDialog(R.string.msg_getting_route);
        if(this.mRoute != null){
            this.mRoute.remove();
            this.mRoute = null;
        }
    }

    public void onConfirmPlaceClick(){
        if(this.mDepartureMarker == null || (this.mDestinationMarker == null && this.mTypeMapRequest == MapRequestEnum.MarkRoute)){
            //TODO SHOW ERROR TO USER
            Toast.makeText(this, "Selecione todos os pontos necessários", Toast.LENGTH_LONG).show();
        }else {
            if(this.mTypeMapRequest == MapRequestEnum.MarkRoute){
                if(this.mNewRideRoute == null){
                    //TODO ask to select
                }else{
                    ConfirmRideOfferDialog confirmRideOfferDialog = ConfirmRideOfferDialog.newInstance(this.mNewRideRoute, false);
                    confirmRideOfferDialog.show(getSupportFragmentManager(), "crod");
                }
            }else {
                Intent intent = new Intent(this, GeolocationService.class);
                intent.putExtra(GeolocationService.LAT_LNG_A_TAG, this.mDepartureMarker.getPosition());
                if (this.mTypeMapRequest == MapRequestEnum.MarkRoute) {
                    intent.putExtra(GeolocationService.LAT_LNG_B_TAG, this.mDestinationMarker.getPosition());
                }
                startService(intent);
            }
        }
    }

    public void onCancelClick(){
        Intent result = new Intent();
        setResult(RESULT_CANCELED, result);
        finish();
    }

    public void sendResult(Place place){
        Intent result = new Intent();
        result.putExtra(PLACE_TAG, place);
        setResult(RESULT_OK, result);
        finish();
    }

    @Subscribe
    public void getPlaceAddress(EventBusEvents.PlaceAddressEvent event){
        this.stopProgressDialog();
        if(this.mTypeMapRequest == MapRequestEnum.AddPlace) {
            NewLocationConfirmDialog.newInstance(event.place).show(getSupportFragmentManager(), "CONFIRM_NEW_PLACE");
        } else{
            sendResult(event.place);
        }
    }

    @Subscribe
    public void getRoute(EventBusEvents.RouteEvent event){
        this.mNewRideRoute = event.route;
        List<LatLng> routePoints = this.mNewRideRoute.getDecodedPoints();
        this.mRoute = this.mMap.addPolyline(new PolylineOptions()
                        .addAll(routePoints)
                        .width(12)
                        .color(getResources().getColor(R.color.route_color))
                        .geodesic(true));
        this.stopProgressDialog();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        makeRouteRequest();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(this.mMarkers.contains(marker)){
            int index = this.mMarkers.indexOf(marker);
            marker.remove();
            this.mMarkers.remove(index);
            makeRouteRequest();
        }
        return true;
    }
}
