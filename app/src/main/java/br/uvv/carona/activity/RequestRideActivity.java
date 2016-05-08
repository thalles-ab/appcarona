package br.uvv.carona.activity;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.uvv.carona.R;
import br.uvv.carona.model.Place;
import br.uvv.carona.util.MapRequestEnum;

public class RequestRideActivity extends BaseActivity {
    public static final String PLACE_REQUEST_TAG = ".PLACE_REQUEST_TAG";
    public static final String DEPARTURE_PLACE_TAG = ".DEPARTURE_PLACE_TAG";
    public static final String PLACES_TAG = ".PLACES_TAG";
    public static final int DEPARTURE_PLACE_REQUEST = 10;
    public static final int DESTINATION_PLACE_REQUEST = 11;

    private RadioGroup mOptions;
    private Button mConfirm;

    private int mRequestType;
    private List<Place> mPlaces;
    private Place mPlaceOrigin;
    private Place mPlaceDestination;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_ride);

        if(savedInstanceState == null){
            this.mRequestType = getIntent().getIntExtra(PLACE_REQUEST_TAG, -1);
            this.mPlaces = new ArrayList<>();

            if(this.mRequestType == DESTINATION_PLACE_REQUEST){
                this.mPlaceOrigin = (Place)getIntent().getSerializableExtra(DEPARTURE_PLACE_TAG);
            }
        }else{
            this.mRequestType = savedInstanceState.getInt(PLACE_REQUEST_TAG);
            this.mPlaces = (List<Place>)savedInstanceState.getSerializable(PLACES_TAG);
        }

        setUpContent();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        if(this.mRequestType == DEPARTURE_PLACE_REQUEST) {
            getSupportActionBar().setTitle(getString(R.string.lbl_departure));
        }else{
            getSupportActionBar().setTitle(getString(R.string.lbl_destination));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PLACE_REQUEST_TAG, this.mRequestType);
        outState.putSerializable(PLACES_TAG, (Serializable)this.mPlaces);
        if(this.mRequestType == DESTINATION_PLACE_REQUEST) {
            outState.putSerializable(DEPARTURE_PLACE_TAG, mPlaceOrigin);
        }
    }

    private void setUpContent(){
        this.mConfirm = (Button)findViewById(R.id.confirm);
        this.mOptions = (RadioGroup)findViewById(R.id.optionsGroup);

        String[] options = getResources().getStringArray(R.array.locations);

        for(int i = 0; i < options.length; i++){
            Place p = new Place();
            p.address = options[i];
            p.latitude = 0;
            p.longitude = 0;
            p.id = -1;
            this.mPlaces.add(p);
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(this.mPlaces.get(i).address);
            this.mOptions.addView(radioButton);
        }
    }

    public void onClickChoosePlace(View view){
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(MapActivity.TYPE_MAP_REQUEST, 0);
        startActivityForResult(intent, mRequestType);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ride, menu);
        return true;
    }

    public void onClickCheckOffers(View view){
        boolean ok = true;

        int id = mOptions.getCheckedRadioButtonId();
        if(id == -1){
            //TODO SHOW MESSAGE TO REQUEST A SELECTION
        }else {
            View v = this.findViewById(id);
            int index = this.mOptions.indexOfChild(v);

            if (mRequestType == DEPARTURE_PLACE_REQUEST) {
                this.mPlaceOrigin = this.mPlaces.get(index);
            } else {
                this.mPlaceDestination = this.mPlaces.get(index);
            }

            Toast.makeText(this, mPlaces.get(index).address, Toast.LENGTH_LONG).show();

            if (ok) {
                if (this.mRequestType == DEPARTURE_PLACE_REQUEST) {
                    Intent intent = new Intent(this, RequestRideActivity.class);
                    intent.putExtra(RequestRideActivity.PLACE_REQUEST_TAG, DESTINATION_PLACE_REQUEST);
                    intent.putExtra(RequestRideActivity.DEPARTURE_PLACE_TAG, mPlaceOrigin);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, CheckRideOffersActivity.class);
//                    intent.putExtra(MapActivity.DEPARTURE_TAG, this.mPlaceOrigin);
//                    intent.putExtra(MapActivity.DESTINATION_TAG, this.mPlaceDestination);
                    startActivity(intent);
                }
            }
        }
    }
}
