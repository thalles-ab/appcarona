package br.uvv.carona.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.uvv.carona.R;
import br.uvv.carona.adapter.RideOfferRecyclerAdapter;
import br.uvv.carona.model.RouteRide;
import br.uvv.carona.model.Student;

public class CheckRideOffersActivity extends BaseActivity {
    private static final String OFFERS_LIST_TAG = ".OFFERS_LIST_TAG";
    private static final String OFFERS_ADAPTER_TAG = ".OFFERS_ADAPTER_TAG";

    private RecyclerView mRecyclerView;
    private RideOfferRecyclerAdapter mRecyclerAdapter;

    private List<RouteRide> mOffers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_ride_offers);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        if(savedInstanceState == null){
            this.mOffers = new ArrayList<>();
            for(int i = 0; i < 27; i++){
                RouteRide r = new RouteRide();
                Student s = new Student();
                s.name = "Fulano-" + (i+1);
                s.photoUrl = "http://www.serebii.net/art/th/" + (i+1) + ".png";
                r.userOffer = s;
                this.mOffers.add(r);
            }
        }else{
            linearLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(OFFERS_ADAPTER_TAG));
            this.mOffers = (List<RouteRide>)savedInstanceState.getSerializable(OFFERS_LIST_TAG);
        }

        this.mRecyclerView = (RecyclerView)findViewById(R.id.offersList);
        this.mRecyclerAdapter = new RideOfferRecyclerAdapter(this.mOffers);
        this.mRecyclerView.setAdapter(this.mRecyclerAdapter);
        this.mRecyclerView.setLayoutManager(linearLayoutManager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(OFFERS_LIST_TAG, (Serializable) this.mOffers);
        outState.putParcelable(OFFERS_ADAPTER_TAG, this.mRecyclerView.getLayoutManager().onSaveInstanceState());
    }
}
