package br.uvv.carona.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.uvv.carona.R;
import br.uvv.carona.util.FormType;
import br.uvv.carona.util.MapRequestEnum;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final String USER_PHOTO_TAG = "USER_PHOTO";

    private ImageView mUserPhotoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setTitle(R.string.app_name);

        mUserPhotoView = (ImageView)findViewById(R.id.userPhoto);
        ((TextView)findViewById(R.id.userName)).setText("Fulano de Almeida");
        ((TextView)findViewById(R.id.userCourse)).setText("Ciência da Computação");
        if(savedInstanceState == null){
            //TODO LOAD USER'S PHOTO AND DO OTHER THINGS
        }else{
            mUserPhotoView.setImageBitmap((Bitmap) savedInstanceState.getParcelable(USER_PHOTO_TAG));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bitmap bitmap = ((BitmapDrawable) mUserPhotoView.getDrawable()).getBitmap();
        outState.putParcelable(USER_PHOTO_TAG, bitmap);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    public void onClickEditProfile(){
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }

    public void onClickRequestRide(){
        Intent intent = new Intent(this, RequestRideActivity.class);
        intent.putExtra(RequestRideActivity.FORM_TYPE_REQUEST_TAG, FormType.RequestRide);
        intent.putExtra(RequestRideActivity.PLACE_REQUEST_TAG, 0);
        startActivity(intent);
    }

    public void onClickOfferRide(){
        Intent intent = new Intent(this, RequestRideActivity.class);
        intent.putExtra(RequestRideActivity.FORM_TYPE_REQUEST_TAG, FormType.OfferRide);
        intent.putExtra(RequestRideActivity.PLACE_REQUEST_TAG, 0);
        startActivity(intent);
    }

    public void onClickCheckRequestRide(){
    }

    public void onClickCheckCompletedRides(){
    }

    public void onClickLogOut(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_edit_profile:
                onClickEditProfile();
                break;
            case R.id.action_offer_ride:
                onClickOfferRide();
                break;
            case R.id.action_request_ride:
                onClickRequestRide();
                break;
            case R.id.action_check_open_requests:
                onClickCheckRequestRide();
                break;
            case R.id.action_check_finished_rides:
                onClickCheckCompletedRides();
                break;
            case R.id.action_logout:
                onClickLogOut();
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}