package br.uvv.carona.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.uvv.carona.R;
import br.uvv.carona.adapter.RideOfferRecyclerAdapter;
import br.uvv.carona.model.Place;
import br.uvv.carona.model.Ride;
import br.uvv.carona.model.Student;
import br.uvv.carona.util.EventBusEvents;

public class CheckRideOffersActivity extends BaseActivity {
    public static final String DEPARTURE_PLACE_TAG = ".DEPARTURE_PLACE_TAG";
    public static final String DESTINATION_PLACE_TAG = ".DESTINATION_PLACE_TAG";
    private static final String OFFERS_LIST_TAG = ".OFFERS_LIST_TAG";
    private static final String OFFERS_ADAPTER_TAG = ".OFFERS_ADAPTER_TAG";

    private RecyclerView mRecyclerView;
    private RideOfferRecyclerAdapter mRecyclerAdapter;

    private List<Ride> mOffers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_ride_offers);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        if(savedInstanceState == null){
            this.mOffers = new ArrayList<>();
            addRoute("Kelvin Xisto", "https://fbcdn-sphotos-g-a.akamaihd.net/hphotos-ak-prn2/v/t1.0-9/10177326_627135940702784_5893836890535122136_n.jpg?oh=eb0d8ad6ac0c55da34a6bc04dc8e1dc5&oe=57E264B7&__gda__=1474788685_82a21e07502b783b55a5f2123c6d3174");
            addRoute("Thalles Batista", "https://fbcdn-sphotos-c-a.akamaihd.net/hphotos-ak-xlf1/v/t1.0-9/12938121_988732927881768_6529938344297270094_n.jpg?oh=6e7d6cbd4126f6abbd824819307abbf6&oe=57DA049A&__gda__=1470231269_93348ef2a4ccbfcff7f40e09137964af");
            addRoute("Nathalia Simmer", "");
            addRoute("Hugo Capucho", "https://fbcdn-sphotos-h-a.akamaihd.net/hphotos-ak-xlp1/v/t1.0-9/10520753_500765296693585_4504038945712839250_n.jpg?oh=9e61927f878bc926151614ea2ca5fad9&oe=57ADB447&__gda__=1470150485_e052129c8f561a6fe72598b00a88ccbd");
            addRoute("Maico Williams", "https://fbcdn-sphotos-h-a.akamaihd.net/hphotos-ak-xpf1/v/t1.0-9/12687915_789418377830785_800951950674169245_n.jpg?oh=b04f8e267ec891105690d2e8756c30a5&oe=57A6C7C6&__gda__=1471208455_3e675fcda8fb047775068e74a15c647d");
            addRoute("Luiz Guilherme Picorelli", "https://scontent-grt2-1.xx.fbcdn.net/v/t1.0-9/14322_494597020571892_1636329166_n.jpg?oh=3207dab00a494c35db21dd2417e2550b&oe=57AF1FB5");
            addRoute("Luiz Fernando", "https://fbcdn-sphotos-b-a.akamaihd.net/hphotos-ak-xlp1/v/t1.0-9/12072540_908418319242515_1407167122670896977_n.jpg?oh=eb604ae3a1bede26947e8f9925b5db99&oe=57A742D4&__gda__=1470902982_674e781f3571cca085a91e7abd89966f");
        }else{
            linearLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(OFFERS_ADAPTER_TAG));
            this.mOffers = (List<Ride>)savedInstanceState.getSerializable(OFFERS_LIST_TAG);
        }

        Place departure = (Place)getIntent().getSerializableExtra(DEPARTURE_PLACE_TAG);
        Place destination = (Place)getIntent().getSerializableExtra(DESTINATION_PLACE_TAG);

        this.mRecyclerView = (RecyclerView)findViewById(R.id.offersList);
        this.mRecyclerAdapter = new RideOfferRecyclerAdapter(this.mOffers, departure, destination);
        this.mRecyclerView.setAdapter(this.mRecyclerAdapter);
        this.mRecyclerView.setLayoutManager(linearLayoutManager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.lbl_request_ride));
        getSupportActionBar().setSubtitle(getString(R.string.lbl_offered_rides));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(OFFERS_LIST_TAG, (Serializable) this.mOffers);
        outState.putParcelable(OFFERS_ADAPTER_TAG, this.mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Subscribe
    @Override
    public void onErrorEvent(EventBusEvents.ErrorEvent event) {
        this.stopProgressDialog();
        treatCommonErrors(event);
    }

    private void addRoute(String name, String photoUrl){
        Ride r = new Ride();
        Student s = new Student();
        s.name = name;
        s.photo = photoUrl;
        s.cellphone = "(27)91234-1234";
        r.student = s;
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
        addPassagerInRide(r.students, "Pedro Mariani", "https://fbcdn-photos-c-a.akamaihd.net/hphotos-ak-xpt1/v/t1.0-0/p526x296/13178874_1161802050510814_7542300537672029753_n.jpg?oh=7c84a4f4570ef32b37a87a0185b66f3e&oe=57A0585C&__gda__=1470576976_0722f1db01f78570237f2cede1697a8e");
        addPassagerInRide(r.students, "Rico Suhete", "https://fbcdn-sphotos-c-a.akamaihd.net/hphotos-ak-xpt1/v/t1.0-9/1012046_408549042687472_97019190266198367_n.jpg?oh=dee06d0dec521cfa33fe3d168b9e60a7&oe=57AFCE73&__gda__=1474252079_3c452c9fc60c66deabe3515c54d8541a");
        this.mOffers.add(r);
    }

    private void addPassagerInRide(List<Student> passagers, String name, String photoUrl){
        Student student = new Student();
        student.name = name;
        student.photo = photoUrl;
        passagers.add(student);
    }
}
