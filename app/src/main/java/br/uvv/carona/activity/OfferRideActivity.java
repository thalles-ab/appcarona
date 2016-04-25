package br.uvv.carona.activity;

import android.os.Bundle;
import android.widget.TextView;

import br.uvv.carona.R;
import br.uvv.carona.model.RouteRide;

public class OfferRideActivity extends BaseActivity {
    public static final String RIDE_ROUTE_TAG = ".RIDE_ROUTE_TAG";

    private RouteRide mRoute;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_ride);

        if(savedInstanceState == null){
            this.mRoute = (RouteRide)getIntent().getSerializableExtra(RIDE_ROUTE_TAG);
        }else{
            this.mRoute = (RouteRide)savedInstanceState.getSerializable(RIDE_ROUTE_TAG);
        }
        setViewContent();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(RIDE_ROUTE_TAG, this.mRoute);
    }

    private void setViewContent(){
        TextView departureField = (TextView)findViewById(R.id.rideDepartureAddress);
        TextView destinationField = (TextView)findViewById(R.id.rideDestinationAddress);

        departureField.setText(this.mRoute.startAddress);
        destinationField.setText(this.mRoute.endAddress);
    }
}
