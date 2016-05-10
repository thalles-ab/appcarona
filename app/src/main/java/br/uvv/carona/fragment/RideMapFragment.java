package br.uvv.carona.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import br.uvv.carona.R;
import br.uvv.carona.model.Ride;

public class RideMapFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {
    private static final String FRAG_RIDE_TAG = ".FRAG_RIDE_TAG";

    private GoogleMap mMap;

    private Ride mRide;

    public static RideMapFragment newInstance(Ride ride) {
        RideMapFragment fragment = new RideMapFragment();
        Bundle args = new Bundle();
        args.putSerializable(FRAG_RIDE_TAG, ride);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null) {
            if (getArguments() != null) {
                this.mRide = (Ride)getArguments().getSerializable(FRAG_RIDE_TAG);
            }
        }else{
            this.mRide = (Ride)savedInstanceState.getSerializable(FRAG_RIDE_TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_ride_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(FRAG_RIDE_TAG, this.mRide);
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(this.mRide.startPoint.latitude, this.mRide.startPoint.longitude), 14);
        this.mMap.animateCamera(cameraUpdate, 1, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onCancel() {

            }
        });
        this.mMap.addMarker(new MarkerOptions().anchor(0.5f, 1).position(new LatLng(this.mRide.startPoint.latitude, this.mRide.startPoint.longitude)));
        this.mMap.addMarker(new MarkerOptions().anchor(0.5f,1).position(new LatLng(this.mRide.endPoint.latitude, this.mRide.endPoint.longitude)));
        this.mMap.addPolyline(new PolylineOptions().addAll(this.mRide.getDecodedPoints()).color(getResources().getColor(R.color.route_color)));
    }
}
