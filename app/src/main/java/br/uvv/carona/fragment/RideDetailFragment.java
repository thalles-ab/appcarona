package br.uvv.carona.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;

import br.uvv.carona.R;
import br.uvv.carona.adapter.RideMembersRecyclerAdapter;
import br.uvv.carona.asynctask.GetRideAsyncTask;
import br.uvv.carona.model.Ride;
import br.uvv.carona.util.DateFormatUtil;
import br.uvv.carona.util.EventBusEvents;

public class RideDetailFragment extends Fragment {
    private static final String FRAG_RIDE_DETAIL_TAG = ".FRAG_RIDE_DETAIL_TAG";

    private RecyclerView mRecyclerView;
    private RideMembersRecyclerAdapter mAdapter;
    private SimpleDraweeView mDriverPhoto;
    private TextView mDriverName;
    private TextView mDriverPhone;
    private TextView mRideTime;

    private Ride mRide;

    public static RideDetailFragment newInstance(Ride ride) {
        RideDetailFragment fragment = new RideDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(FRAG_RIDE_DETAIL_TAG, ride);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null) {
            if (getArguments() != null) {
                this.mRide = (Ride) getArguments().getSerializable(FRAG_RIDE_DETAIL_TAG);
            }

            new GetRideAsyncTask().execute(this.mRide);
        }else{
            this.mRide = (Ride) savedInstanceState.getSerializable(FRAG_RIDE_DETAIL_TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_ride_detail, container, false);
        this.mRecyclerView = (RecyclerView)root.findViewById(R.id.members_list);
        this.mDriverPhoto = (SimpleDraweeView)root.findViewById(R.id.user_photo);
        this.mDriverName = (TextView)root.findViewById(R.id.user_name);
        this.mRideTime = (TextView)root.findViewById(R.id.ride_time);
        this.mDriverPhone = (TextView)root.findViewById(R.id.driver_phone);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mAdapter = new RideMembersRecyclerAdapter(this.mRide.students);
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        setInfoOnScreen();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(FRAG_RIDE_DETAIL_TAG, this.mRide);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void setInfoOnScreen(){
        if(!TextUtils.isEmpty(this.mRide.student.photo)){
            this.mDriverPhoto.setImageURI(Uri.parse(this.mRide.student.photo));
        }
        this.mDriverName.setText(this.mRide.student.name);

        if(this.mRide.expirationDate != null){
            this.mRideTime.setText(DateFormatUtil.formatHourView.format(this.mRide.expirationDate));
        }

        if(this.mRide.student.allowCellphone) {
            this.mDriverPhone.setText(this.mRide.student.cellPhone);
        }
    }

    @Subscribe
    public void onGetRideEvent(EventBusEvents.RideEvent event){
        this.mRide = event.ride;
        this.mAdapter.replaceContent(this.mRide.students);
    }
}
