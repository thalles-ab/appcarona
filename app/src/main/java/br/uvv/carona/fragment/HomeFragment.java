package br.uvv.carona.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import br.uvv.carona.R;
import br.uvv.carona.asynctask.StatsAsyncTask;
import br.uvv.carona.model.Statistic;


public class HomeFragment extends Fragment {
    private static final String EXTRA_STATISTIC = "EXTRA_STATISTIC";
    private TextView mTaken;
    private TextView mGiven;
    private Statistic mStatistic;
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_home, container, false);
        this.mTaken = (TextView) root.findViewById(R.id.taken);
        this.mGiven = (TextView) root.findViewById(R.id.given);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true)
    public void onEventStatistic(Statistic event){
        EventBus.getDefault().removeAllStickyEvents();
        mStatistic = event;
        fillStatistics();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(EXTRA_STATISTIC, mStatistic);
        super.onSaveInstanceState(outState);
    }

    private void fillStatistics(){
        mTaken.setText(String.valueOf(mStatistic.amountTakenRides));
        mGiven.setText(String.valueOf(mStatistic.amountGivenRides));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState == null){
            mStatistic = new Statistic();
            new StatsAsyncTask().execute();
        }else{
            mStatistic = (Statistic) savedInstanceState.getSerializable(EXTRA_STATISTIC);
        }
        fillStatistics();
    }
}
