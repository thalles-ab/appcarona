package br.uvv.carona.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.uvv.carona.R;
import br.uvv.carona.activity.BaseActivity;
import br.uvv.carona.adapter.RideRequestStatusAdapter;
import br.uvv.carona.model.Ride;
import br.uvv.carona.util.RideRequestStatus;

public class RideRequestsFragment extends Fragment {
    private static final String TYPE_REQUEST_TAG = ".TYPE_REQUEST_TAG";
    public static final int TYPE_REQUEST_MADE = 0;
    public static final int TYPE_REQUEST_RECEIVED = 1;

    private RecyclerView mOpenRequests;

    private int mTypeRequest;

    public static RideRequestsFragment newInstance(int typeRequest) {
        RideRequestsFragment fragment = new RideRequestsFragment();
        Bundle args = new Bundle();
        args.putInt(TYPE_REQUEST_TAG, typeRequest);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mTypeRequest = getArguments().getInt(TYPE_REQUEST_TAG);
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
        List<Ride> rides = new ArrayList<>();
        RideRequestStatusAdapter adapter = new RideRequestStatusAdapter(rides, this.getContext());
        this.mOpenRequests.setAdapter(adapter);
        this.mOpenRequests.setLayoutManager(new LinearLayoutManager(this.getContext()));

        ActionBar actionBar = ((BaseActivity)getActivity()).getSupportActionBar();
        if(this.mTypeRequest == TYPE_REQUEST_MADE){
            actionBar.setTitle(getString(R.string.lbl_see_requests_made));
        }else{
            actionBar.setTitle(getString(R.string.lbl_see_requests_received));
        }
    }
}
