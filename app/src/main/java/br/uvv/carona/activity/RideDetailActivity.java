package br.uvv.carona.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.greenrobot.eventbus.Subscribe;

import br.uvv.carona.R;
import br.uvv.carona.adapter.RideDetailPageAdapter;
import br.uvv.carona.asynctask.GetRideAsyncTask;
import br.uvv.carona.asynctask.NewRideSolicitationAsyncTask;
import br.uvv.carona.dialog.MessageDialog;
import br.uvv.carona.model.Place;
import br.uvv.carona.model.Ride;
import br.uvv.carona.model.RideSolicitation;
import br.uvv.carona.util.EventBusEvents;

public class RideDetailActivity extends BaseActivity {
    public static final String IS_NEW_REQUEST_TAG = ".IS_NEW_REQUEST_TAG";
    public static final String RIDE_TAG = ".RIDE_TAG";
    public static final String PLACE_DEP_TAG = ".PLACE_DEP_TAG";
    public static final String PLACE_DES_TAG = ".PLACE_DES_TAG";

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private RideDetailPageAdapter mViewPagerAdapter;

    private boolean mIsRideRequest;
    private Ride mRide;
    private Place mDeparture;
    private Place mDestination;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_detail);

        if (savedInstanceState == null){
            this.mRide = (Ride)getIntent().getSerializableExtra(RIDE_TAG);
            this.mIsRideRequest = getIntent().getBooleanExtra(IS_NEW_REQUEST_TAG, false);
            this.mDeparture = (Place)getIntent().getSerializableExtra(PLACE_DEP_TAG);
            this.mDestination = (Place)getIntent().getSerializableExtra(PLACE_DES_TAG);
        }else{
            this.mRide = (Ride)savedInstanceState.getSerializable(RIDE_TAG);
            this.mIsRideRequest = savedInstanceState.getBoolean(IS_NEW_REQUEST_TAG);
        }

        this.mViewPager = (ViewPager)findViewById(R.id.view_pager);
        this.mViewPagerAdapter = new RideDetailPageAdapter(this, getSupportFragmentManager(), this.mRide, this.mDeparture, this.mDestination);
        this.mViewPager.setAdapter(this.mViewPagerAdapter);
        this.mTabLayout = (TabLayout) findViewById(R.id.tabs);
        this.mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        this.mTabLayout.setupWithViewPager(this.mViewPager);

        setUpActionBar();
    }

    private void setUpActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(R.string.lbl_ride);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(this.mIsRideRequest){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_ride, menu);
            return true;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_confirm:
                startProgressDialog(R.string.msg_sending_solicitation);
                RideSolicitation solicitation = new RideSolicitation();
                solicitation.ride = this.mRide;
                new NewRideSolicitationAsyncTask().execute(solicitation);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(RIDE_TAG, this.mRide);
        outState.putBoolean(IS_NEW_REQUEST_TAG, this.mIsRideRequest);
    }

    @Subscribe
    public void onSuccess(EventBusEvents.SuccessEvent event){
        if(event.success){
            MessageDialog.newInstance(getString(R.string.msg_solicitation_success), new MessageDialog.OnDialogButtonClick() {
                @Override
                public void onConfirmClick(Dialog dialog) {
                    Intent intent = new Intent(dialog.getContext(), HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    dialog.dismiss();
                    startActivity(intent);
                }
            });
        }
        stopProgressDialog();
    }
}
