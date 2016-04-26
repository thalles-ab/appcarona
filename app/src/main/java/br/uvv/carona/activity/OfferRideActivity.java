package br.uvv.carona.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.uvv.carona.R;
import br.uvv.carona.model.RouteRide;

public class OfferRideActivity extends BaseActivity {
    public static final String RIDE_ROUTE_TAG = ".RIDE_ROUTE_TAG";
    public static final String RIDE_DATE_TAG = ".RIDE_DATE_TAG";

    private RouteRide mRoute;
    private Date mDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_ride);

        if(savedInstanceState == null){
            this.mRoute = (RouteRide)getIntent().getSerializableExtra(RIDE_ROUTE_TAG);
            this.mDate = null;
        }else{
            this.mRoute = (RouteRide)savedInstanceState.getSerializable(RIDE_ROUTE_TAG);
            this.mDate = (Date)savedInstanceState.getSerializable(RIDE_DATE_TAG);
        }
        setViewContent();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.lbl_confirm_ride);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(RIDE_ROUTE_TAG, this.mRoute);
        outState.putSerializable(RIDE_DATE_TAG, this.mDate);
    }

    private void setViewContent(){
        TextView departureField = (TextView)findViewById(R.id.rideDepartureAddress);
        TextView destinationField = (TextView)findViewById(R.id.rideDestinationAddress);

        departureField.setText(this.mRoute.startAddress);
        destinationField.setText(this.mRoute.endAddress);

        TextView dateField = (TextView)findViewById(R.id.rideDate);
        TextView timeField = (TextView)findViewById(R.id.rideHour);

        if(this.mDate == null){
            this.mDate = new Date();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("kk:mm");
        dateField.setText(dateFormat.format(this.mDate));
        timeField.setText(timeFormat.format(this.mDate));
    }

    public void onClickChangeDateTime(View view){
        //TODO SHOW CALENDAR TO CHOSE DATE AND TIME
    }
}
