package br.uvv.carona.util;

import java.util.List;

import br.uvv.carona.model.Place;
import br.uvv.carona.model.Ride;

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
        public List<Ride> routes;
        public Ride route;

        public RouteEvent(List<Ride> routes) {
            this.routes = routes;
        }

        public RouteEvent(Ride route) {
            this.route = route;
        }
    }

}
