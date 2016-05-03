package br.uvv.carona.model.route;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.List;

/**
 * Created by CB1772 on 24/04/2016.
 */
public class RouteRequest implements Serializable {
    public LatLng start;
    public LatLng end;
    public List<LatLng> waypoints;

    public RouteRequest(LatLng start, LatLng end, List<LatLng> waypoints) {
        this.start = start;
        this.end = end;
        this.waypoints = waypoints;
    }
}
