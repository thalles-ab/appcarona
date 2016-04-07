package br.uvv.carona.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.uvv.carona.R;

public class MapActivity extends BaseActivity implements GoogleMap.OnMapLoadedCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback, GoogleMap.OnMapClickListener{

    public static final String TYPE_MAP_REQUEST = "TYPE_MAP_REQUEST";
    public static final String SELECTED_PLACE_LATLNG_TAG = "SELECTED_PLACE_LATLNG_TAG";
    public static final String SELECTED_PLACE_ADDRESS_TAG = "SELECTED_PLACE_ADDRESS_TAG";
    public static final String SELECTED_ROUTE_TAG = "SELECTED_ROUTE_TAG";

    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;

    private int mTypeMapRequest = 0;

    private Marker mSelectedPlaceA;
    private Marker mSelectedPlaceB;

    private boolean mSelectionStart = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        if(savedInstanceState == null){
            mTypeMapRequest = getIntent().getIntExtra(TYPE_MAP_REQUEST, - 1);
        }else{
            mTypeMapRequest = savedInstanceState.getInt(TYPE_MAP_REQUEST);
        }
        if(mTypeMapRequest == -1){
            Log.e("Erro Request", "Tipo de Requisição não expecificado");
        }

        this.mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        this.mMapFragment.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TYPE_MAP_REQUEST, mTypeMapRequest);
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
    }

    @Override
    public void onMapLoaded() {

    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(this.mSelectionStart){
            if(this.mSelectedPlaceA != null){
                this.mSelectedPlaceA.remove();
                this.mSelectedPlaceA = null;
            }

            MarkerOptions markerOptions = new MarkerOptions().position(latLng).anchor(0.5f, 1.0f);
            this.mSelectedPlaceA = mMap.addMarker(markerOptions);
        }else{
            if(this.mSelectedPlaceB != null){
                this.mSelectedPlaceB.remove();
                this.mSelectedPlaceB = null;
            }

            MarkerOptions markerOptions = new MarkerOptions().position(latLng).anchor(0.5f, 1.0f);
            this.mSelectedPlaceB = mMap.addMarker(markerOptions);
        }

        if(this.mSelectedPlaceA != null && this.mSelectedPlaceB != null && this.mTypeMapRequest == 2){
            //TODO GENERATE ROUTE
        }
    }

    public void onSendClick(View view){
        //TODO PEGAR O ENDEREÇO COMPLETO DO LOCAL SELECIONADO PARA ENTÃO ENVIAR
        String[] addresses = null;
        List<LatLng> startEnd = new ArrayList<>();
        List<LatLng> route = new ArrayList<>();


        if(this.mSelectedPlaceA == null || (this.mSelectedPlaceB == null && this.mTypeMapRequest > 0)){
            //TODO SHOW ERROR TO USER
            Toast.makeText(this, "Selecione todas os pontos necessários", Toast.LENGTH_LONG).show();
        }else {
            if (this.mTypeMapRequest == 0) {
                String[] ad = {"teste"};
                addresses = ad;
                startEnd.add(this.mSelectedPlaceA.getPosition());
            } else if (this.mTypeMapRequest > 0 && this.mTypeMapRequest < 3) {
                String[] ad = {"teste", "teste"};
                startEnd.add(this.mSelectedPlaceA.getPosition());
                startEnd.add(this.mSelectedPlaceB.getPosition());
                addresses = ad;
                if (this.mTypeMapRequest == 2) {
                    route.add(this.mSelectedPlaceA.getPosition());
                    route.add(this.mSelectedPlaceB.getPosition());
                }
            }
            sendResult(addresses, route, startEnd);
        }
    }

    public void onCancelClick(View view){
        Intent result = new Intent();
        setResult(RESULT_CANCELED, result);
        finish();
    }

    private void sendResult(String[] name, List<LatLng> route, List<LatLng> geoLocations){
        Intent result = new Intent();
        result.putExtra(this.SELECTED_PLACE_LATLNG_TAG, (Serializable) geoLocations);
        result.putExtra(this.SELECTED_ROUTE_TAG, (Serializable)route);
        result.putExtra(this.SELECTED_PLACE_ADDRESS_TAG, name);
        setResult(RESULT_OK, result);
        finish();
    }
}
