package br.uvv.carona.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.Subscribe;

import br.uvv.carona.R;
import br.uvv.carona.application.AppPartiUVV;
import br.uvv.carona.fragment.RideStatusFragment;
import br.uvv.carona.httprequest.util.WSResources;
import br.uvv.carona.model.Student;
import br.uvv.carona.util.EventBusEvents;
import br.uvv.carona.util.FormType;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static final String EXTRA_USER= "EXTRA_USER";

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private FrameLayout mFragmentWrapper;
    private SimpleDraweeView mUserPhoto;
    private TextView mUserName;

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
        this.mUserPhoto = (SimpleDraweeView) mNavigationView.getHeaderView(0).findViewById(R.id.user_photo);
        this.mUserName = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.user_name);


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
        if(savedInstanceState == null){
            this.mUser = AppPartiUVV.getStudent();
        }else{
            this.mUser = (Student) savedInstanceState.getSerializable(EXTRA_USER);
        }
        setUserInfo();
        disableNavigationViewScrollbars(mNavigationView);
    }

    private void disableNavigationViewScrollbars(NavigationView navigationView) {
        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }

    private void setUserInfo(){
        mUserName.setText(mUser.name);
        if(!TextUtils.isEmpty(mUser.photo)){
            mUserPhoto.setImageURI(Uri.parse(WSResources.BASE_UPLOAD_URL + mUser.photo));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(this.mUser != null){
            outState.putSerializable(EXTRA_USER, this.mUser);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_white_24dp);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUser = AppPartiUVV.getStudent();
        setUserInfo();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_edit_profile:
                Intent editProfileIntent = new Intent(this, EditProfileActivity.class);
                editProfileIntent.putExtra(EXTRA_USER, mUser);
                startActivity(editProfileIntent);
                break;
            case R.id.action_offer_ride:
                Intent offerRide = new Intent(this, RequestRideStep1Activity.class);
                offerRide.putExtra(RequestRideStep1Activity.FORM_TYPE_REQUEST_TAG, FormType.OfferRide);
                offerRide.putExtra(RequestRideStep1Activity.PLACE_REQUEST_TAG, 0);
                startActivity(offerRide);
                break;
            case R.id.action_request_ride:
                Intent requestRideIntent = new Intent(this, RequestRideStep1Activity.class);
                requestRideIntent.putExtra(RequestRideStep1Activity.FORM_TYPE_REQUEST_TAG, FormType.RequestRide);
                requestRideIntent.putExtra(RequestRideStep1Activity.PLACE_REQUEST_TAG, 0);
                startActivity(requestRideIntent);
                break;
            case R.id.action_check_requests_made:
                RideStatusFragment fragmentMade = RideStatusFragment.newInstance(RideStatusFragment.TYPE_REQUEST_MADE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_wrapper, fragmentMade).commit();
                break;
            case R.id.action_check_requests_received:
                RideStatusFragment fragmentReceived = RideStatusFragment.newInstance(RideStatusFragment.TYPE_REQUEST_RECEIVED);
                FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                ft2.replace(R.id.fragment_wrapper, fragmentReceived).commit();
                break;
            case R.id.action_edit_places:
                Intent editPlacesIntent = new Intent(this, EditPlacesActivity.class);
                startActivity(editPlacesIntent);
                break;
            case R.id.action_check_offered_rides:
                RideStatusFragment fragmentRides = RideStatusFragment.newInstance(RideStatusFragment.TYPE_ACTIVE_RIDE);
                FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                ft3.replace(R.id.fragment_wrapper, fragmentRides).commit();
                break;
            case R.id.action_logout:
                logout();
                break;
        }
        this.mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}