package br.uvv.carona.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.uvv.carona.R;
import br.uvv.carona.adapter.RideOfferRecyclerAdapter;
import br.uvv.carona.asynctask.GetRideOffersAsyncTask;
import br.uvv.carona.model.Place;
import br.uvv.carona.model.Ride;
import br.uvv.carona.model.Student;
import br.uvv.carona.util.EventBusEvents;

public class CheckRideOffersActivity extends BaseActivity {
    public static final String DEPARTURE_PLACE_TAG = ".DEPARTURE_PLACE_TAG";
    public static final String DESTINATION_PLACE_TAG = ".DESTINATION_PLACE_TAG";
    private static final String OFFERS_LIST_TAG = ".OFFERS_LIST_TAG";
    private static final String OFFERS_ADAPTER_TAG = ".OFFERS_ADAPTER_TAG";

    private RecyclerView mRecyclerView;
    private RideOfferRecyclerAdapter mRecyclerAdapter;

    private List<Ride> mOffers;
    private Place mDeparture;
    private Place mDestination;
    private boolean showDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_ride_offers);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        if(savedInstanceState == null){
            this.mOffers = new ArrayList<>();
            this.mDeparture = (Place)getIntent().getSerializableExtra(DEPARTURE_PLACE_TAG);
            this.mDestination = (Place)getIntent().getSerializableExtra(DESTINATION_PLACE_TAG);
            Ride r = new Ride();
            r.endPoint = this.mDestination;
            r.startPoint = this.mDeparture;
            new GetRideOffersAsyncTask(r).execute();
            startProgressDialog(R.string.msg_searching_ride);
        }else{
            linearLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(OFFERS_ADAPTER_TAG));
            this.mOffers = (List<Ride>)savedInstanceState.getSerializable(OFFERS_LIST_TAG);
            this.mDeparture = (Place)savedInstanceState.getSerializable(DEPARTURE_PLACE_TAG);
            this.mDestination = (Place)savedInstanceState.getSerializable(DESTINATION_PLACE_TAG);
        }

        Place departure = (Place)getIntent().getSerializableExtra(DEPARTURE_PLACE_TAG);
        Place destination = (Place)getIntent().getSerializableExtra(DESTINATION_PLACE_TAG);

        this.mRecyclerView = (RecyclerView)findViewById(R.id.offersList);
        this.mRecyclerAdapter = new RideOfferRecyclerAdapter(this.mOffers, this.mDeparture, this.mDestination);
        this.mRecyclerView.setAdapter(this.mRecyclerAdapter);
        this.mRecyclerView.setLayoutManager(linearLayoutManager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.lbl_request_ride));
        getSupportActionBar().setSubtitle(getString(R.string.lbl_offered_rides));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(OFFERS_LIST_TAG, (Serializable) this.mOffers);
        outState.putParcelable(OFFERS_ADAPTER_TAG, this.mRecyclerView.getLayoutManager().onSaveInstanceState());
        outState.putSerializable(DEPARTURE_PLACE_TAG, this.mDeparture);
        outState.putSerializable(DESTINATION_PLACE_TAG, this.mDestination);
    }

   @Subscribe
    public void onGetRides(EventBusEvents.RideEvent event){
        this.mRecyclerAdapter.addItems(event.rides);
        this.stopProgressDialog();
    }
}
