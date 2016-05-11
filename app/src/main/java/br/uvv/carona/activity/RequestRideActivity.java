package br.uvv.carona.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.uvv.carona.R;
import br.uvv.carona.dialog.ConfirmRideOfferDialog;
import br.uvv.carona.model.Place;
import br.uvv.carona.model.Ride;
import br.uvv.carona.util.FormType;
import br.uvv.carona.util.MapRequestEnum;

public class RequestRideActivity extends BaseActivity {
    public static final String PLACE_REQUEST_TAG = ".PLACE_REQUEST_TAG";
    public static final String FORM_TYPE_REQUEST_TAG = ".FORM_TYPE_REQUEST_TAG";
    public static final String DEPARTURE_PLACE_TAG = ".DEPARTURE_PLACE_TAG";
    public static final String DESTINATION_PLACE_TAG = ".DESTINATION_PLACE_TAG";
    public static final String RADIO_SELECTION_TAG = ".DEPARTURE_PLACE_TAG";
    public static final String PLACES_TAG = ".PLACES_TAG";

    private RadioGroup mOptions;
    private Button mConfirm;

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
            this.mCurrentStep = getIntent().getIntExtra(PLACE_REQUEST_TAG, 0);
            this.mForm = (FormType)getIntent().getSerializableExtra(FORM_TYPE_REQUEST_TAG);
            this.mPlaces = new ArrayList<>();
            this.mPlaceDeparture = null;
            this.mPlaceDestination = null;
            if(this.mCurrentStep == 1){
                this.mPlaceDeparture = (Place)getIntent().getSerializableExtra(DEPARTURE_PLACE_TAG);
            }
            selectionPosition = -1;
        }else{
            this.mCurrentStep = savedInstanceState.getInt(PLACE_REQUEST_TAG);
            this.mForm = (FormType)savedInstanceState.getSerializable(FORM_TYPE_REQUEST_TAG);
            this.mPlaces = (List<Place>)savedInstanceState.getSerializable(PLACES_TAG);
            this.mPlaceDeparture = (Place)savedInstanceState.getSerializable(DEPARTURE_PLACE_TAG);
            this.mPlaceDestination = (Place)savedInstanceState.getSerializable(DESTINATION_PLACE_TAG);
            selectionPosition = savedInstanceState.getInt(RADIO_SELECTION_TAG);
        }

        setUpActionBar();
        setUpContent(selectionPosition);
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
                    if (this.mCurrentStep == 0) {
                        this.mPlaceDeparture = this.mPlaces.get(index);
                        Intent intent = new Intent(this, RequestRideActivity.class);
                        intent.putExtra(DEPARTURE_PLACE_TAG, this.mPlaceDeparture);
                        intent.putExtra(PLACE_REQUEST_TAG, 1);
                        intent.putExtra(FORM_TYPE_REQUEST_TAG, this.mForm);
                        startActivity(intent);
                    }else{
                        this.mPlaceDestination = this.mPlaces.get(index);
                        if(this.mPlaceDeparture.equals(this.mPlaceDestination)){
                            //TODO SHOW ERROR
                        }else {
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
//                                Intent intent = new Intent(this, CheckRideOffersActivity.class);
//                                intent.putExtra(CheckRideOffersActivity.DEPARTURE_PLACE_TAG, this.mPlaceDeparture);
//                                intent.putExtra(CheckRideOffersActivity.DESTINATION_PLACE_TAG, this.mPlaceDestination);
//                                startActivity(intent);
                            }
                        }
                    }
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
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
        outState.putInt(RADIO_SELECTION_TAG, position);
        outState.putSerializable(FORM_TYPE_REQUEST_TAG, this.mForm);
        outState.putSerializable(PLACES_TAG, (Serializable) this.mPlaces);
        outState.putSerializable(DEPARTURE_PLACE_TAG, this.mPlaceDeparture);
        outState.putSerializable(DESTINATION_PLACE_TAG, this.mPlaceDestination);
    }

    private void setUpContent(int selectedPosition){
        this.mOptions = (RadioGroup)findViewById(R.id.optionsGroup);

        String[] options = getResources().getStringArray(R.array.locations);
        for(int i = 0; i < options.length; i++){
            Place p = new Place();
            p.description = options[i];
            if(i%2 == 0) {
                p.latitude = -20.3558589;
                p.longitude = -40.3548503;
            }else{
                p.latitude = -20.3551569;
                p.longitude = -40.2977617;
            }
            p.id = (i+1)*17;
            this.mPlaces.add(p);
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(this.mPlaces.get(i).description);
            radioButton.setTag("QWEQ" + i);
            this.mOptions.addView(radioButton);
        }
        if(selectedPosition != -1){
            int id = this.mOptions.getChildAt(selectedPosition).getId();
            this.mOptions.check(id);
        }
    }
}
