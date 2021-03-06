package br.uvv.carona.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.uvv.carona.R;
import br.uvv.carona.activity.BaseActivity;
import br.uvv.carona.adapter.RideStatusAdapter;
import br.uvv.carona.application.AppPartiUVV;
import br.uvv.carona.model.Place;
import br.uvv.carona.model.Ride;
import br.uvv.carona.model.RideSolicitation;
import br.uvv.carona.model.Student;
import br.uvv.carona.util.EventBusEvents;
import br.uvv.carona.util.RideSolicitationStatus;

public class RideStatusFragment extends Fragment {
    private static final String TYPE_REQUEST_TAG = ".TYPE_REQUEST_TAG";
    private static final String LIST_ITEMS_TAG = ".LIST_ITEMS_TAG";
    private static final String LAYOUT_MANAGER_TAG = ".LAYOUT_MANAGER_TAG";
    public static final int TYPE_REQUEST_MADE = 0;
    public static final int TYPE_REQUEST_RECEIVED = 1;
    public static final int TYPE_ACTIVE_RIDE = 2;

    private RecyclerView mOpenRequests;
    private RideStatusAdapter mAdapter;

    private int mTypeRequest;
    private List<RideSolicitation> mSolicitations;
    private List<Ride> mRides;

    public static RideStatusFragment newInstance(int typeRequest) {
        RideStatusFragment fragment = new RideStatusFragment();
        Bundle args = new Bundle();
        args.putInt(TYPE_REQUEST_TAG, typeRequest);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            this.mTypeRequest = getArguments().getInt(TYPE_REQUEST_TAG);
            if(this.mTypeRequest == TYPE_ACTIVE_RIDE){
                this.mRides = AppPartiUVV.simuRide;
            }else if(this.mTypeRequest == TYPE_REQUEST_RECEIVED){
                this.mSolicitations = AppPartiUVV.simuSolicitation;
            }else{
                this.mSolicitations = AppPartiUVV.simuSolicitationMade;
            }
        }else{
            this.mTypeRequest = savedInstanceState.getInt(TYPE_REQUEST_TAG);
            if(this.mTypeRequest == TYPE_ACTIVE_RIDE){
                this.mRides = (List<Ride>) savedInstanceState.getSerializable(LIST_ITEMS_TAG);
            }else {
                this.mSolicitations = (List<RideSolicitation>) savedInstanceState.getSerializable(LIST_ITEMS_TAG);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_ride_solicitations, container, false);
        this.mOpenRequests = (RecyclerView)root.findViewById(R.id.solicitations);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(this.mTypeRequest == TYPE_ACTIVE_RIDE){
            this.mAdapter = new RideStatusAdapter(this.mRides, this.getContext());
        }else{
            this.mAdapter = new RideStatusAdapter(this.mSolicitations, this.getContext(), this.mTypeRequest);
        }
        this.mOpenRequests.setAdapter(this.mAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        if(savedInstanceState != null){
            layoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(LAYOUT_MANAGER_TAG));
        }

        this.mOpenRequests.setLayoutManager(layoutManager);

        ActionBar actionBar = ((BaseActivity)getActivity()).getSupportActionBar();
        if(this.mTypeRequest == TYPE_REQUEST_MADE){
            actionBar.setTitle(R.string.lbl_see_requests_made);
        }else if(mTypeRequest == TYPE_REQUEST_RECEIVED){
            actionBar.setTitle(R.string.lbl_see_requests_received);
        }else{
            actionBar.setTitle(R.string.lbl_offered_rides);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TYPE_REQUEST_TAG, this.mTypeRequest);
        if(this.mTypeRequest == TYPE_ACTIVE_RIDE){
            outState.putSerializable(LIST_ITEMS_TAG, (Serializable)this.mRides);
        }else {
            outState.putSerializable(LIST_ITEMS_TAG, (Serializable) this.mSolicitations);
        }
        outState.putParcelable(LAYOUT_MANAGER_TAG, this.mOpenRequests.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onGetRideRequests(EventBusEvents.RideRequestEvent event){
        this.mAdapter.addItems((List<Object>)(Object)event.requests);
    }

    @Subscribe
    public void onGetRideRequests(EventBusEvents.RideEvent event){
        this.mAdapter.addItems((List<Object>)(Object)event.rides);
    }
}
