package br.uvv.carona.util;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import br.uvv.carona.model.Place;
import br.uvv.carona.model.route.RouteResult;

public class EventBusEvents {

    public static class ErrorEvent{
        public String message;

        public ErrorEvent(String message){
            this.message = message;
        }
    }

    public static class PlaceAddressEvent{
        public Place place;
        public List<Place> places;

        public PlaceAddressEvent(Place place){
            this.place = place;
        }

        public PlaceAddressEvent(List<Place> places) {
            this.places = places;
        }
    }

    public static class RouteEvent{
        public List<LatLng> latLngs;
        public RouteResult routeResult;

        public RouteEvent(List<LatLng> latLngs) {
            this.latLngs = latLngs;
        }

        public RouteEvent(List<LatLng> latLngs, RouteResult routeResult) {
            this.latLngs = latLngs;
            this.routeResult = routeResult;
        }
    }

}
