package br.uvv.carona.model;

import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.uvv.carona.model.route.RouteResult;

public class RouteRide extends BaseObject {
    public Student userOffer;
    public Date creation;
    public Date validation;
    public Calendar validationCalendar;
    public String encodedPoints;
    public String endAddress;
    public RouteResult.LatLng endLocation;
    public String startAddress;
    public RouteResult.LatLng startLocation;

    public List<LatLng> getDecodedPoints(){
        return (TextUtils.isEmpty(this.encodedPoints)) ? new ArrayList<LatLng>() : PolyUtil.decode(this.encodedPoints);
    }

}
