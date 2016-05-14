package br.uvv.carona.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.Subscribe;

import br.uvv.carona.R;
import br.uvv.carona.asynctask.GetUserInfoAsyncTask;
import br.uvv.carona.fragment.RideRequestsFragment;
import br.uvv.carona.model.Student;
import br.uvv.carona.util.EventBusEvents;
import br.uvv.carona.util.FormType;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final String USER_LOGGED_TAG = ".USER_LOGGED_TAG";

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private FrameLayout mFragmentWrapper;

    private Student mUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        this.mFragmentWrapper = (FrameLayout)findViewById(R.id.fragment_wrapper);
        this.mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        this.mNavigationView.setNavigationItemSelectedListener(this);
        this.mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(this.mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }else{
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_white_24dp);
        }


        this.mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Log.i("DRAWER", "OPEN");
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Log.i("DRAWER", "CLOSE");
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_white_24dp);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                Log.i("DRAWER", "CHANGED");
            }
        });

        if(this.mUser == null){
            new GetUserInfoAsyncTask().execute();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(this.mUser != null){
            outState.putSerializable(USER_LOGGED_TAG, this.mUser);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_white_24dp);
            } else {
                drawer.openDrawer(GravityCompat.START);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onGetUserResult(EventBusEvents.UserEvent event){
        mUser = event.student;
        if(this.mUser.photo != null) {
            ((SimpleDraweeView) this.mNavigationView.getHeaderView(0).findViewById(R.id.user_photo)).setImageURI(Uri.parse(this.mUser.photo));
        }
        ((TextView)this.mNavigationView.getHeaderView(0).findViewById(R.id.user_name)).setText(this.mUser.name);
    }

    @Subscribe
    @Override
    void onErrorEvent(EventBusEvents.ErrorEvent event) {
        treatCommonErrors(event);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_white_24dp);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_edit_profile:
                Intent editProfileIntent = new Intent(this, EditProfileActivity.class);
                startActivity(editProfileIntent);
                break;
            case R.id.action_offer_ride:
                Intent offerRide = new Intent(this, RequestRideActivity.class);
                offerRide.putExtra(RequestRideActivity.FORM_TYPE_REQUEST_TAG, FormType.OfferRide);
                offerRide.putExtra(RequestRideActivity.PLACE_REQUEST_TAG, 0);
                startActivity(offerRide);
                break;
            case R.id.action_request_ride:
                Intent requestRideIntent = new Intent(this, RequestRideActivity.class);
                requestRideIntent.putExtra(RequestRideActivity.FORM_TYPE_REQUEST_TAG, FormType.RequestRide);
                requestRideIntent.putExtra(RequestRideActivity.PLACE_REQUEST_TAG, 0);
                startActivity(requestRideIntent);
                break;
            case R.id.action_check_requests_made:
                RideRequestsFragment fragmentMade = RideRequestsFragment.newInstance(RideRequestsFragment.TYPE_REQUEST_MADE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_wrapper, fragmentMade).commit();
                break;
            case R.id.action_check_requests_received:
                RideRequestsFragment fragmentReceived = RideRequestsFragment.newInstance(RideRequestsFragment.TYPE_REQUEST_RECEIVED);
                FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                ft2.replace(R.id.fragment_wrapper, fragmentReceived).commit();
                break;
            case R.id.action_edit_places:
                //TODO
                break;
            case R.id.action_logout:
                Intent logoutIntent = new Intent(this, LoginActivity.class);
                startActivity(logoutIntent);
                finish();
                break;
        }

        this.mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}