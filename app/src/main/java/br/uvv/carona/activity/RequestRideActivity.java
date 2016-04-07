package br.uvv.carona.activity;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import br.uvv.carona.R;

public class RequestRideActivity extends BaseActivity {
    private static final String DEPARTURE_SELECTION_TAG = "DEPARTURE_SELECTION_TAG";
    private static final String DESTINATION_SELECTION_TAG = "DESTINATION_SELECTION_TAG";
    private static final String DEPARTURE_UVV_SELECTION_TAG = "DEPARTURE_UVV_SELECTION_TAG";
    private static final String DESTINATION_UVV_SELECTION_TAG = "DESTINATION_UVV_SELECTION_TAG";

    private static final int DEPARTURE_PLACE_REQUEST = 10;
    private static final int DESTINATION_PLACE_REQUEST = 11;

    private Spinner mDepartureSpinner;
    private Spinner mDestinationSpinner;
    private Spinner mDepartureUvvSpinner;
    private Spinner mDestinationUvvSpinner;
    private TextView mDepartureAddress;
    private TextView mDestinationAddress;
    private Button mSelectDeparturePlace;
    private Button mSelectDestinationPlace;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_ride);

        int[] selections = {0,0,0,0};

        if(savedInstanceState == null){

        }else{
            selections[0] = savedInstanceState.getInt(DEPARTURE_SELECTION_TAG);
            selections[1] = savedInstanceState.getInt(DESTINATION_SELECTION_TAG);
            selections[2] = savedInstanceState.getInt(DEPARTURE_UVV_SELECTION_TAG);
            selections[3] = savedInstanceState.getInt(DESTINATION_UVV_SELECTION_TAG);
        }

        setUpContent();
        setUpPlacesOptionsSpinners(selections[0], selections[1], selections[2], selections[3]);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.lbl_request_ride));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(DEPARTURE_SELECTION_TAG, mDepartureSpinner.getSelectedItemPosition());
        outState.putInt(DESTINATION_SELECTION_TAG, mDestinationSpinner.getSelectedItemPosition());
        outState.putInt(DEPARTURE_UVV_SELECTION_TAG, mDepartureUvvSpinner.getSelectedItemPosition());
        outState.putInt(DESTINATION_UVV_SELECTION_TAG, mDestinationUvvSpinner.getSelectedItemPosition());
    }

    private void setUpContent(){
        this.mDepartureSpinner = (Spinner)findViewById(R.id.departurePlacesOptions);
        this.mDestinationSpinner = (Spinner)findViewById(R.id.destinationPlacesOptions);
        this.mDepartureUvvSpinner = (Spinner)findViewById(R.id.departureUvvCampusOptions);
        this.mDestinationUvvSpinner = (Spinner)findViewById(R.id.destinationUvvCampusOptions);
        this.mDepartureAddress = (TextView)findViewById(R.id.departureLocationAddress);
        this.mDestinationAddress = (TextView)findViewById(R.id.destinationLocationAddress);
        this.mSelectDeparturePlace = (Button)findViewById(R.id.chooseDepartureLocation);
        this.mSelectDestinationPlace = (Button)findViewById(R.id.chooseDestinationLocation);
    }

    private void setUpPlacesOptionsSpinners(int selectionDeparture, int selectionDepartureUvv, int selectionDestination, int selectionDestinationUvv){
        this.mDepartureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String)parent.getSelectedItem();
                if(selected.equals(getString(R.string.txt_uvv))){
                    mDepartureUvvSpinner.setVisibility(View.VISIBLE);
                    mDepartureAddress.setVisibility(View.GONE);
                    mSelectDeparturePlace.setVisibility(View.GONE);
                }else if(selected.equals(getString(R.string.txt_other))){
                    mDepartureUvvSpinner.setVisibility(View.GONE);
                    mDepartureAddress.setVisibility(View.VISIBLE);
                    mDepartureAddress.setText("");
                    mSelectDeparturePlace.setVisibility(View.VISIBLE);
                }else{
                    mDepartureUvvSpinner.setVisibility(View.GONE);
                    mDepartureAddress.setVisibility(View.GONE);
                    mSelectDeparturePlace.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        this.mDestinationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String)parent.getSelectedItem();
                if(selected.equals(getString(R.string.txt_uvv))){
                    mDestinationUvvSpinner.setVisibility(View.VISIBLE);
                    mDestinationAddress.setVisibility(View.GONE);
                    mSelectDestinationPlace.setVisibility(View.GONE);
                }else if(selected.equals(getString(R.string.txt_other))){
                    mDestinationUvvSpinner.setVisibility(View.GONE);
                    mDestinationAddress.setVisibility(View.VISIBLE);
                    mDestinationAddress.setText("");
                    mSelectDestinationPlace.setVisibility(View.VISIBLE);
                }else{
                    mDestinationUvvSpinner.setVisibility(View.GONE);
                    mDestinationAddress.setVisibility(View.GONE);
                    mSelectDestinationPlace.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        this.mDepartureSpinner.setSelection(selectionDeparture);
        this.mDestinationSpinner.setSelection(selectionDestination);

        this.mDepartureUvvSpinner.setSelection(selectionDepartureUvv);
        this.mDestinationUvvSpinner.setSelection(selectionDestinationUvv);
    }

    public void onClickChoosePlace(View view){
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(MapActivity.TYPE_MAP_REQUEST, 0);
        int request;
        if(view.getId() == R.id.chooseDepartureLocation){
            request = DEPARTURE_PLACE_REQUEST;
        }else {
            request = DESTINATION_PLACE_REQUEST;
        }
        startActivityForResult(intent, request);
    }

    public void onClickChooseDate(View view){

    }

    public void onClickChooseHour(View view){

    }

    public void onClickCheckOffers(View view){
        boolean ok = true;
        if(this.mDestinationSpinner.getSelectedItem().toString().equals(getString(R.string.txt_select))){
            ((TextView)this.mDestinationSpinner.getSelectedView()).setError("");
            ok = false;
        } else if(this.mDestinationSpinner.getSelectedItem().toString().equals(getString(R.string.txt_uvv)) &&
                this.mDestinationUvvSpinner.getSelectedItem().toString().equals(getString(R.string.txt_select))){
            ((TextView)this.mDestinationUvvSpinner.getSelectedView()).setError("");
            ok = false;
        }
        if(this.mDepartureSpinner.getSelectedItem().toString().equals(getString(R.string.txt_select))){
            ((TextView)this.mDepartureSpinner.getSelectedView()).setError("");
            ok = false;
        } else if(this.mDepartureSpinner.getSelectedItem().toString().equals(getString(R.string.txt_uvv)) &&
                this.mDepartureUvvSpinner.getSelectedItem().toString().equals(getString(R.string.txt_select))){
            ((TextView)this.mDepartureUvvSpinner.getSelectedView()).setError("");
            ok = false;
        }

        if(ok){

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case DEPARTURE_PLACE_REQUEST:
                    if(data != null){

                    }
                    break;
                case DESTINATION_PLACE_REQUEST:
                    if(data != null){

                    }
                    break;
            }
        }
    }
}
