package br.uvv.carona.httprequest.util;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Geen on 24/02/2016.
 */
public class OccurrenceListRequestParams {
    public LatLng latLng;
    public int days;
    public double distance;

    public OccurrenceListRequestParams(LatLng latLng, int days, double distance){
        this.latLng = latLng;
        this.days = days;
        this.distance = distance;
    }
}
