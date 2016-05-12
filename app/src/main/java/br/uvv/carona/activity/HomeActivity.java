package br.uvv.carona.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.uvv.carona.R;
import br.uvv.carona.adapter.RideRequestStatusAdapter;
import br.uvv.carona.model.Place;
import br.uvv.carona.model.Ride;
import br.uvv.carona.model.Student;
import br.uvv.carona.util.FormType;
import br.uvv.carona.util.MapRequestEnum;
import br.uvv.carona.util.RideRequestStatus;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final String USER_PHOTO_TAG = "USER_PHOTO";

    private RecyclerView mOpenRequests;

    private Student mUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setTitle("Pedidos");

        this.mOpenRequests = (RecyclerView)findViewById(R.id.requests);
        List<Ride> rides = new ArrayList<>();
        addRoute(rides, "Kelvin Xisto", "https://fbcdn-sphotos-g-a.akamaihd.net/hphotos-ak-prn2/v/t1.0-9/10177326_627135940702784_5893836890535122136_n.jpg?oh=eb0d8ad6ac0c55da34a6bc04dc8e1dc5&oe=57E264B7&__gda__=1474788685_82a21e07502b783b55a5f2123c6d3174", RideRequestStatus.Accepted,0);
        addRoute(rides, "Thalles Batista", "https://fbcdn-sphotos-c-a.akamaihd.net/hphotos-ak-xlf1/v/t1.0-9/12938121_988732927881768_6529938344297270094_n.jpg?oh=6e7d6cbd4126f6abbd824819307abbf6&oe=57DA049A&__gda__=1470231269_93348ef2a4ccbfcff7f40e09137964af", RideRequestStatus.Refused,21600000);
        addRoute(rides, "Nathalia Simmer", "", RideRequestStatus.Waiting, 3600000);
        addRoute(rides, "Hugo Capucho", "https://fbcdn-sphotos-h-a.akamaihd.net/hphotos-ak-xlp1/v/t1.0-9/10520753_500765296693585_4504038945712839250_n.jpg?oh=9e61927f878bc926151614ea2ca5fad9&oe=57ADB447&__gda__=1470150485_e052129c8f561a6fe72598b00a88ccbd", RideRequestStatus.Waiting,-21600000);
        addRoute(rides, "Maico Williams", "https://fbcdn-sphotos-h-a.akamaihd.net/hphotos-ak-xpf1/v/t1.0-9/12687915_789418377830785_800951950674169245_n.jpg?oh=b04f8e267ec891105690d2e8756c30a5&oe=57A6C7C6&__gda__=1471208455_3e675fcda8fb047775068e74a15c647d", RideRequestStatus.Waiting, 600000);
        addRoute(rides, "Luiz Guilherme Picorelli", "https://scontent-grt2-1.xx.fbcdn.net/v/t1.0-9/14322_494597020571892_1636329166_n.jpg?oh=3207dab00a494c35db21dd2417e2550b&oe=57AF1FB5", RideRequestStatus.Accepted, 43200000);
        addRoute(rides, "Luiz Fernando", "https://fbcdn-sphotos-b-a.akamaihd.net/hphotos-ak-xlp1/v/t1.0-9/12072540_908418319242515_1407167122670896977_n.jpg?oh=eb604ae3a1bede26947e8f9925b5db99&oe=57A742D4&__gda__=1470902982_674e781f3571cca085a91e7abd89966f", RideRequestStatus.Waiting, -600000);
        RideRequestStatusAdapter adapter = new RideRequestStatusAdapter(rides, this);
        this.mOpenRequests.setAdapter(adapter);
        this.mOpenRequests.setLayoutManager(new LinearLayoutManager(this));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }else{
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_white_24dp);
        }
        navigationView.setCheckedItem(navigationView.indexOfChild(navigationView.findViewById(R.id.action_check_open_requests)));

        mUser = new Student();
        mUser.name = "Julio Almeida";
        if(this.mUser.photo != null) {
            ((SimpleDraweeView) navigationView.getHeaderView(0).findViewById(R.id.user_photo)).setImageURI(Uri.parse(this.mUser.photo));
        }
        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.user_name)).setText(this.mUser.name);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        Bitmap bitmap = ((BitmapDrawable) mUserPhotoView.getDrawable()).getBitmap();
//        outState.putParcelable(USER_PHOTO_TAG, bitmap);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_white_24dp);
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

    private void addRoute(List<Ride> rides, String name, String photoUrl, RideRequestStatus status, int timeMilli){
        Ride r = new Ride();
        Student s = new Student();
        s.name = name;
        s.photo = photoUrl;
        s.cellPhone = "(27)91234-1234";
        r.student = s;
        r.status = status;
        Place a = new Place();
        a.latitude = -20.3558589;
        a.longitude = -40.3548503;
        Place b = new Place();
        b.latitude = -20.3551569;
        b.longitude = -40.2977617;
        r.startPoint = a;
        r.endPoint = b;
        r.routeGoogleFormat = "bwf{BxxhuFaH}@QEMAcBeDyDcISa@ACJOBI@Id@kDp@oGRyAIOMc@sDuHsBaEi@eAXi@lAaCnBqDpA_CpGyLbAiBl@qAv@{BXiAVoBJmC@kBCcAKiAUmA}@kDcB{HgDuO_@eB_B{H{@wD_AwEaBqHqBiFo@sA}EsH_GoJ}JePoCwE}AiC}C{EuBoDkAwBTKfEcCfDkBxEoCjDiBLCl@HfJv@~BiAnBk@~@QNqCRmB\\sEDGHo@HCJQ@UIOCCAA?eANaCYm@oA_CgFoJu@uAx@e@dB_ArDyBfEoCrEyCpC{A|Aw@jCkATK@B@B@@FBLE@QEEfBiAyCmE_B_C";
        r.students = new ArrayList<>();
        r.expirationDate = new Date();
        r.expirationDate.setTime(r.expirationDate.getTime() - timeMilli);
        addPassagerInRide(r.students, "Pedro Mariani", "https://fbcdn-photos-c-a.akamaihd.net/hphotos-ak-xpt1/v/t1.0-0/p526x296/13178874_1161802050510814_7542300537672029753_n.jpg?oh=7c84a4f4570ef32b37a87a0185b66f3e&oe=57A0585C&__gda__=1470576976_0722f1db01f78570237f2cede1697a8e");
        addPassagerInRide(r.students, "Rico Suhete", "https://fbcdn-sphotos-c-a.akamaihd.net/hphotos-ak-xpt1/v/t1.0-9/1012046_408549042687472_97019190266198367_n.jpg?oh=dee06d0dec521cfa33fe3d168b9e60a7&oe=57AFCE73&__gda__=1474252079_3c452c9fc60c66deabe3515c54d8541a");
        rides.add(r);
    }

    private void addPassagerInRide(List<Student> passagers, String name, String photoUrl){
        Student student = new Student();
        student.name = name;
        student.photo = photoUrl;
        passagers.add(student);
    }
}