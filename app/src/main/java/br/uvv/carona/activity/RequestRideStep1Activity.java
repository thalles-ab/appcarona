package br.uvv.carona.activity;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.uvv.carona.R;
import br.uvv.carona.application.AppPartiUVV;
import br.uvv.carona.asynctask.GetUserPlacesAsyncTask;
import br.uvv.carona.dialog.ConfirmRideOfferDialog;
import br.uvv.carona.model.Place;
import br.uvv.carona.model.Ride;
import br.uvv.carona.service.GeolocationService;
import br.uvv.carona.util.EventBusEvents;
import br.uvv.carona.util.FormType;
import br.uvv.carona.util.MapRequestEnum;

public class RequestRideStep1Activity extends BaseActivity implements ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final String PLACE_REQUEST_TAG = ".PLACE_REQUEST_ORIGIN_TAG";
    public static final String FORM_TYPE_REQUEST_TAG = ".FORM_TYPE_REQUEST_ORIGIN_TAG";
    public static final String DEPARTURE_PLACE_TAG = ".DEPARTURE_PLACE_ORIGIN_TAG";
    public static final String DESTINATION_PLACE_TAG = ".DESTINATION_PLACE_ORIGIN_TAG";
    public static final String RADIO_SELECTION_TAG = ".DEPARTURE_PLACE_ORIGIN_TAG";
    public static final String PLACES_TAG = ".PLACES_ORIGIN_TAG";
    public static final int REQUEST_NEW_PLACE_CODE = 10;

    private RadioGroup mOptions;
    private GoogleApiClient mApiClient;

    private int mCurrentStep;
    private FormType mForm;
    private List<Place> mPlaces;
    private Place mPlaceDeparture;
    private Place mPlaceDestination;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_ride);

        int selectionPosition;
        if(savedInstanceState == null){
            this.mCurrentStep = getIntent().getIntExtra(PLACE_REQUEST_TAG, -1);
            this.mForm = (FormType)getIntent().getSerializableExtra(FORM_TYPE_REQUEST_TAG);
            this.mPlaces = new ArrayList<>();
            this.mPlaceDeparture = null;
            this.mPlaceDestination = null;
            if(this.mCurrentStep == 1){
                this.mPlaceDeparture = (Place)getIntent().getSerializableExtra(DEPARTURE_PLACE_TAG);
            }
            selectionPosition = -1;

            startProgressDialog(R.string.msg_getting_places);
            new GetUserPlacesAsyncTask(this.mCurrentStep).execute();

        }else{
            this.mCurrentStep = savedInstanceState.getInt(PLACE_REQUEST_TAG);
            this.mForm = (FormType)savedInstanceState.getSerializable(FORM_TYPE_REQUEST_TAG+this.mCurrentStep);
            this.mPlaces = (List<Place>)savedInstanceState.getSerializable(PLACES_TAG+this.mCurrentStep);
            this.mPlaceDeparture = (Place)savedInstanceState.getSerializable(DEPARTURE_PLACE_TAG+this.mCurrentStep);
            this.mPlaceDestination = (Place)savedInstanceState.getSerializable(DESTINATION_PLACE_TAG+this.mCurrentStep);
            selectionPosition = savedInstanceState.getInt(RADIO_SELECTION_TAG+this.mCurrentStep);
        }

        this.mApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        setUpActionBar();
        setUpContent(this.mPlaces, selectionPosition);
    }



    @Override
    protected void onStart() {
        super.onStart();
        this.mApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.mApiClient.disconnect();
    }

    private void setUpActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        if(this.mForm == FormType.OfferRide){
            actionBar.setTitle(getString(R.string.lbl_offer_ride));
        }else {
            actionBar.setTitle(getString(R.string.lbl_request_ride));
        }
        if(this.mCurrentStep == 0) {
            actionBar.setSubtitle(getString(R.string.lbl_departure));
        }else{
            actionBar.setSubtitle(getString(R.string.lbl_destination));
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ride, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_confirm:
                int id = mOptions.getCheckedRadioButtonId();
                if(id == -1){
                    //TODO SHOW ERROR
                }else {
                    View v = this.findViewById(id);
                    int index = this.mOptions.indexOfChild(v);
                    Place place = this.mPlaces.get(index);
                    boolean ok = true;
                    if(place.id == -1){
                        if((android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.M &&
                                AppPartiUVV.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION)) || android.os.Build.VERSION.SDK_INT != Build.VERSION_CODES.M) {
                            Location location = LocationServices.FusedLocationApi.getLastLocation(mApiClient);
                            if (location == null) {
                                //TODO
                                Log.i("GET_LOC", "Couldn't get current location");
                            } else {
                                Intent intent = new Intent(this, GeolocationService.class);
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                intent.putExtra(GeolocationService.LAT_LNG_A_TAG, latLng);
                                startService(intent);
                            }
                        }else{
                            //TODO
                        }
                        ok = false;
                    }else if(place.id == -2){
                        Intent intent = new Intent(this, MapActivity.class);
                        intent.putExtra(MapActivity.TYPE_MAP_REQUEST, MapRequestEnum.OtherPlace);
                        startActivityForResult(intent,REQUEST_NEW_PLACE_CODE);
                        ok = false;
                    }
                    if(ok) {
                        goToNextStep(place);
                    }
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToNextStep(Place place){
        if (this.mCurrentStep == 0) {
            this.mPlaceDeparture = place;
            Intent intent = new Intent(this, RequestRideStep1Activity.class);
            intent.putExtra(DEPARTURE_PLACE_TAG, this.mPlaceDeparture);
            intent.putExtra(PLACE_REQUEST_TAG, 1);
            intent.putExtra(FORM_TYPE_REQUEST_TAG, this.mForm);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            this.mPlaceDestination = place;
            if (this.mPlaceDeparture.equals(this.mPlaceDestination) && this.mPlaceDeparture.id != -2) {
                //TODO SHOW ERROR
            } else {
                if (this.mForm == FormType.OfferRide) {
                    Intent intent = new Intent(this, MapActivity.class);
                    intent.putExtra(MapActivity.DEPARTURE_TAG, this.mPlaceDeparture);
                    intent.putExtra(MapActivity.DESTINATION_TAG, this.mPlaceDestination);
                    intent.putExtra(MapActivity.TYPE_MAP_REQUEST, MapRequestEnum.MarkRoute);
                    startActivity(intent);
                } else {
                    Ride ride = new Ride();
                    ride.startPoint = this.mPlaceDeparture;
                    ride.endPoint = this.mPlaceDestination;
                    ConfirmRideOfferDialog.newInstance(ride, true).show(getSupportFragmentManager(), ".RequestConfirm");
                }
            }
        }
    }

    @Subscribe
    public void onGetPlaces(EventBusEvents.PlaceEvent event){
        if(this.mCurrentStep == event.callerId) {
            if (event.place == null) {
                String[] options = getResources().getStringArray(R.array.locations);
                for (int i = 0; i < options.length; i++) {
                    Place p = new Place();
                    p.description = options[i];
                    p.id = -1 * (i + 1);
                    if (!event.places.contains(p)) {
                        event.places.add(p);
                    }
                }
                this.stopProgressDialog();
                setUpContent(event.places, -1);
            } else {
                Place place = event.place;
                place.id = -1;
                this.stopProgressDialog();
                goToNextStep(place);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PLACE_REQUEST_TAG, this.mCurrentStep);
        int position = mOptions.getCheckedRadioButtonId();
        if(position != -1) {
            View v = this.findViewById(position);
            position = this.mOptions.indexOfChild(v);
        }
        outState.putInt(RADIO_SELECTION_TAG+this.mCurrentStep, position);
        outState.putSerializable(FORM_TYPE_REQUEST_TAG+this.mCurrentStep, this.mForm);
        outState.putSerializable(PLACES_TAG+this.mCurrentStep, (Serializable) this.mPlaces);
        outState.putSerializable(DEPARTURE_PLACE_TAG+this.mCurrentStep, this.mPlaceDeparture);
        outState.putSerializable(DESTINATION_PLACE_TAG+this.mCurrentStep, this.mPlaceDestination);
    }

    private void setUpContent(List<Place> places, int selectedPosition){
        this.mOptions = (RadioGroup)findViewById(R.id.optionsGroup);
        int c1 = this.mOptions.getChildCount();
        this.mPlaces = places;
        for(int i = 0; i < this.mPlaces.size(); i++){
            Place place = this.mPlaces.get(i);
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(place.description);
            this.mOptions.addView(radioButton);
        }
        int c2 = this.mOptions.getChildCount();
        if(selectedPosition != -1){
            int id = this.mOptions.getChildAt(selectedPosition).getId();
            this.mOptions.check(id);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(data != null){
                if(requestCode == REQUEST_NEW_PLACE_CODE){
                    Place place = (Place)data.getSerializableExtra(MapActivity.PLACE_TAG);
                    place.id = -2;
                    goToNextStep(place);
                }
            }
        }
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
}
