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
import br.uvv.carona.adapter.RideSolicitationStatusAdapter;
import br.uvv.carona.model.Ride;
import br.uvv.carona.model.RideSolicitation;
import br.uvv.carona.model.Student;
import br.uvv.carona.util.EventBusEvents;
import br.uvv.carona.util.RideSolicitationStatus;

public class RideSolicitationsFragment extends Fragment {
    private static final String TYPE_REQUEST_TAG = ".TYPE_REQUEST_TAG";
    private static final String LIST_RIDE_REQUEST_TAG = ".LIST_RIDE_REQUEST_TAG";
    private static final String LAYOUT_MANAGER_TAG = ".LAYOUT_MANAGER_TAG";
    public static final int TYPE_REQUEST_MADE = 0;
    public static final int TYPE_REQUEST_RECEIVED = 1;

    private RecyclerView mOpenRequests;
    private RideSolicitationStatusAdapter mAdapter;

    private int mTypeRequest;
    private List<RideSolicitation> mRequests;

    public static RideSolicitationsFragment newInstance(int typeRequest) {
        RideSolicitationsFragment fragment = new RideSolicitationsFragment();
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
            this.mRequests = new ArrayList<>();
            for(int i = 0;  i< ((this.mTypeRequest == TYPE_REQUEST_MADE) ? 10 : 15); i++){
                RideSolicitation request = new RideSolicitation();
                request.status = (i % 3 == 0) ? RideSolicitationStatus.Accepted : (i % 3 == 1) ? RideSolicitationStatus.Waiting : RideSolicitationStatus.Refused;
                request.ride = new Ride();
                request.ride.expirationDate = new Date();
                if(i % 5 == 0){
                    request.ride.expirationDate.setTime(request.ride.expirationDate.getTime() - 86400000);
                }
                request.ride.student = new Student();
                request.student = new Student();
                request.student.name = "Fulano-"+(i+1);
                this.mRequests.add(request);
            }
        }else{
            this.mTypeRequest = savedInstanceState.getInt(TYPE_REQUEST_TAG);
            this.mRequests = (List<RideSolicitation>)savedInstanceState.getSerializable(LIST_RIDE_REQUEST_TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_ride_requests, container, false);
        this.mOpenRequests = (RecyclerView)root.findViewById(R.id.requests);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mAdapter = new RideSolicitationStatusAdapter(this.mRequests, this.getContext(), this.mTypeRequest);
        this.mOpenRequests.setAdapter(this.mAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        if(savedInstanceState == null){

        }else{
            layoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(LAYOUT_MANAGER_TAG));
        }

        this.mOpenRequests.setLayoutManager(layoutManager);

        ActionBar actionBar = ((BaseActivity)getActivity()).getSupportActionBar();
        if(this.mTypeRequest == TYPE_REQUEST_MADE){
            actionBar.setTitle(getString(R.string.lbl_see_requests_made));
        }else{
            actionBar.setTitle(getString(R.string.lbl_see_requests_received));
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
        outState.putSerializable(LIST_RIDE_REQUEST_TAG, (Serializable) this.mRequests);
        outState.putParcelable(LAYOUT_MANAGER_TAG, this.mOpenRequests.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onGetRideRequests(EventBusEvents.RideRequestEvent event){
        this.mAdapter.addRideRequest(event.requests);
    }
}
