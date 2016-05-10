package br.uvv.carona.activity;

import android.net.Uri;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.awt.font.TextAttribute;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.uvv.carona.R;
import br.uvv.carona.adapter.RideDetailPageAdapter;
import br.uvv.carona.adapter.RideMembersRecyclerAdapter;
import br.uvv.carona.model.Ride;
import br.uvv.carona.model.Student;

public class RideDetailActivity extends BaseActivity {
    public static final String IS_NEW_REQUEST_TAG = ".IS_NEW_REQUEST_TAG";
    public static final String RIDE_TAG = ".RIDE_TAG";

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private RideDetailPageAdapter mViewPagerAdapter;

    private boolean mIsRideRequest;
    private Ride mRide;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_detail);

        if (savedInstanceState == null){
            this.mRide = (Ride)getIntent().getSerializableExtra(RIDE_TAG);
            this.mIsRideRequest = getIntent().getBooleanExtra(IS_NEW_REQUEST_TAG, false);
        }else{
            this.mRide = (Ride)savedInstanceState.getSerializable(RIDE_TAG);
            this.mIsRideRequest = savedInstanceState.getBoolean(IS_NEW_REQUEST_TAG);
        }

        this.mViewPager = (ViewPager)findViewById(R.id.view_pager);
        this.mViewPagerAdapter = new RideDetailPageAdapter(this, getSupportFragmentManager(), this.mRide);
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
}
