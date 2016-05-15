package br.uvv.carona.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.uvv.carona.R;
import br.uvv.carona.model.Place;
import br.uvv.carona.util.EventBusEvents;

public class GeolocationService extends IntentService {
    public static final String LAT_LNG_A_TAG = ".LAT_LNG_A_TAG";
    public static final String LAT_LNG_B_TAG = ".LAT_LNG_B_TAG";

    public GeolocationService() {
        super("GeolocationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        LatLng latLngA = intent.getParcelableExtra(LAT_LNG_A_TAG);
        LatLng latLngB = null;
        if(intent.hasExtra(LAT_LNG_B_TAG)){
            latLngB = intent.getParcelableExtra(LAT_LNG_B_TAG);
        }

        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Place> places = new ArrayList<>();
        String message = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(latLngA.latitude, latLngA.longitude, 3);
            Place placeA = new Place(latLngA.latitude, latLngA.longitude);
            if(addresses != null && addresses.size() > 0){
                int mAL = addresses.get(0).getMaxAddressLineIndex();
                StringBuilder stringBuilder = new StringBuilder();
                for(int i = 0; i < mAL; i++){
                    if(i != 0){
                        stringBuilder.append(" - ");
                    }
                    stringBuilder.append(addresses.get(0).getAddressLine(i));
                }
                placeA.description = stringBuilder.toString();
            }
            places.add(placeA);
            if(latLngB != null){
                List<Address> addressesB = geocoder.getFromLocation(latLngB.latitude, latLngB.longitude, 3);
                Place placeB = new Place(latLngB.latitude, latLngB.longitude);
                if(addressesB != null && addressesB.size() > 0){
                    int mAL = addressesB.get(0).getMaxAddressLineIndex();
                    StringBuilder stringBuilder = new StringBuilder();
                    for(int i = 0; i < mAL; i++){
                        stringBuilder.append(addressesB.get(0).getAddressLine(i));
                    }
                    placeB.description = stringBuilder.toString();
                }
                places.add(placeB);
            }
        } catch (IOException e) {
            Log.e("ERROR GEOCODER", e.getMessage());
            message = getString(R.string.msg_error_get_address);
        }

        if(places == null || places.size() == 0){
            EventBus.getDefault().post(new EventBusEvents.ErrorEvent(message));
        } else{
            if(places.size() == 1){
                EventBus.getDefault().post(new EventBusEvents.PlaceEvent(places.get(0)));
            }else {
                EventBus.getDefault().post(new EventBusEvents.PlaceEvent(places));
            }
        }
    }
}
