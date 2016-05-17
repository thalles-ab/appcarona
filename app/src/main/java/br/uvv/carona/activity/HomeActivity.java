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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import br.uvv.carona.R;
import br.uvv.carona.application.AppPartiUVV;
import br.uvv.carona.fragment.HomeFragment;
import br.uvv.carona.fragment.RideStatusFragment;
import br.uvv.carona.httprequest.util.WSResources;
import br.uvv.carona.model.Student;
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

        this.mUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProfileIntent = new Intent(HomeActivity.this, EditProfileActivity.class);
                editProfileIntent.putExtra(EXTRA_USER, mUser);
                startActivity(editProfileIntent);
            }
        });

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
            showHome();
        }else{
            this.mUser = (Student) savedInstanceState.getSerializable(EXTRA_USER);
        }
        setUserInfo();
        disableNavigationViewScrollbars(mNavigationView);
    }

    private void showHome(){
        HomeFragment fragmentHome = HomeFragment.newInstance();
        FragmentTransaction ft4 = getSupportFragmentManager().beginTransaction();
        ft4.replace(R.id.fragment_wrapper, fragmentHome).commit();
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
    protected void onStart() {
        super.onStart();
        if(AppPartiUVV.simuRide == null){
            AppPartiUVV.simuRide = new ArrayList<>();
            AppPartiUVV.simuSolicitation = new ArrayList<>();
            AppPartiUVV.simuSolicitationMade = new ArrayList<>();

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, 2016);
            calendar.set(Calendar.MONTH, 5);
            calendar.set(Calendar.DAY_OF_MONTH, 18);
            calendar.set(Calendar.HOUR_OF_DAY, 6);
            calendar.set(Calendar.MINUTE, 30);
            Date a = calendar.getTime();
            AppPartiUVV.addRide(-20.3554469, -20.354858743617466, -40.3540771, -40.29762055724859,
                    "rtf{BrshuFqHeAc@?yDcISa@ACJOBI@Id@kDp@oGRyAIOMc@sDuHsBaEi@eAXi@lAaCnBqDpA_CpGyLbAiBl@qAv@{BXiAVoBJmC@kBCcAKiAUmA}@kDcB{HgDuO_@eB_B{H{@wD_AwEaBqHqBiFo@sA}EsH_GoJ}JePoCwE}AiC}C{EuBoDkAwBTKfEcCfDkBxEoCjDiBLCl@HfJv@~BiAnBk@~@QNqCRmB\\sEDGHo@HCJQ@UIOCCAA?eANaCYm@oA_CgFoJu@uAx@e@dB_ArDyBfEoCrEyCpC{A|Aw@jCkATK@B@B@@FBLE@QEEfBiAyCmEeCoD",
                    2, a);
            calendar.set(Calendar.HOUR_OF_DAY, 11);
            Date b = calendar.getTime();
            AppPartiUVV.addRide(-20.354858743617466, -20.3554469, -40.29762055724859,-40.3540771,
                    "hqf{Bbr}tF_@m@KDYd@UToDtBaClAeAl@_CjA}A~@]N[D_AKoBe@eAS]MEEKCMBELBLJFD^BJCt@KdAQ`B]hESlC}@EMBYFk@g@OXxAbCnAbC^|@lBpDtB|DlBxDjBpD@HG\\C@GFEF?RFPFDBB@TAVBHo@xHAPOfC_APoBj@_Ab@_Ad@}D[sAM{BUO?KB{EhCwEnCaFtCoBbAKDDHpB`E`C~DrGlKZl@T^nArBHHNh@nHtLtEnHJR~AfC`CfDLVt@`BhAvCRj@xApGjAlFn@rC`@bChBxIlCvLl@vC|@vEbAzD\\zBFhCA~@GfBY`CWpAu@vBmBzDcJ~PuEtIsA`CGJU@cFvA|@bDnAl@TVn@vAlBdEnB`EzC|F\\p@xDbIb@?zHfA",
                    4, b);
            calendar.set(Calendar.DAY_OF_MONTH, 19);
            calendar.set(Calendar.HOUR_OF_DAY, 13);
            calendar.set(Calendar.MINUTE, 30);
            Date c = calendar.getTime();
            AppPartiUVV.addRide(-20.33524172334717, -20.35500019666682, -40.2915071323514, -40.29792666435242,
                    "vub{Bxl|tFSxCMnBtALpCX|BPjE^zGp@lMjAxE^`DZhALtMjAhIv@rBLtPzAnBN~C\\fD^dHj@~Hv@fCVfAx@vDzC^ZuAfBeCvDkClDg@w@_CoDgA_BuAqB",
                    3, c);
            calendar.set(Calendar.HOUR_OF_DAY, 9);
            AppPartiUVV.addRide(-20.35500019666682, -20.33524172334717, -40.29792666435242, -40.2915071323514,
                    "xrf{Bbt}tFoDkFu@oAAUDOFKt@FvBPLBLWdA{An@w@?@@?B?BADEAIEEI@?@A@yAqA{CcCMGaA}@CCCGACy@KgB]iBOoBUkAEW?k@H_Cf@aBRaCd@mAJiDEqAKeDa@oAI}C_@gGi@gCQ_AKi@Im@AiB?q@E}@OeDa@iHq@{E]qCS{BYgCOwBUqD[qAIsAGJqAJqArABtABMtAEl@",
                    2, a);
            AppPartiUVV.addRideSolicitation("Luciana Amália Santigo",
                    "https://pixabay.com/static/uploads/photo/2014/08/20/18/08/woman-422706_960_720.jpg",
                    AppPartiUVV.simuRide.get(1));
            AppPartiUVV.addRideSolicitation("Caio Vítor Ferreira",
                    "https://pixabay.com/static/uploads/photo/2016/01/05/11/36/portrait-1122364_960_720.jpg"
                    , AppPartiUVV.simuRide.get(3));
            AppPartiUVV.addRideSolicitation("Heitor Quirino Salazar",
                    "https://pixabay.com/static/uploads/photo/2015/09/02/12/38/man-918576_960_720.jpg",
                    AppPartiUVV.simuRide.get(2));
            AppPartiUVV.addRideSolicitation("Brenda Gomes",
                    "https://pixabay.com/static/uploads/photo/2016/01/27/10/59/female-1164098_960_720.jpg",
                    AppPartiUVV.simuRide.get(0));
            AppPartiUVV.addRideSolicitation("Luciana Amália Santigo",
                    "https://pixabay.com/static/uploads/photo/2015/11/07/11/50/blonde-1031534_960_720.jpg",
                    AppPartiUVV.simuRide.get(1));
            AppPartiUVV.addRideSolicitation("Inês Magalhães",
                    "https://pixabay.com/static/uploads/photo/2015/07/24/18/27/young-858730_960_720.jpg",
                    AppPartiUVV.simuRide.get(1));
            AppPartiUVV.addRideSolicitation("Clarissa Pinheiro",
                    "https://pixabay.com/static/uploads/photo/2016/02/19/10/06/musician-1209073_960_720.jpg",
                    AppPartiUVV.simuRide.get(2));
            AppPartiUVV.addRideSolicitation("Lorenzo Cardozo",
                    "https://pixabay.com/static/uploads/photo/2015/01/08/18/30/entrepreneur-593371_960_720.jpg",
                    AppPartiUVV.simuRide.get(3));
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
            case R.id.action_home:
                showHome();
                break;
            case R.id.action_logout:
                logout();
                break;
        }
        this.mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}