package br.uvv.carona.util;

import java.util.List;

import br.uvv.carona.model.Error;
import br.uvv.carona.model.Place;
import br.uvv.carona.model.Ride;
import br.uvv.carona.model.RideSolicitation;
import br.uvv.carona.model.Student;

public class EventBusEvents {

    public static class SuccessEvent{
        public boolean success;

        public SuccessEvent(boolean success){
            this.success = success;
        }
    }

    public static class ErrorEvent{
        public String message;

        public ErrorEvent(String message){
            this.message = message;
        }
    }

    public static class ErrorsEvent{
        public List<Error> errors;

        public ErrorsEvent(List<Error> errors){
            this.errors = errors;
        }
    }

    public static class UserEvent{
        public Student student;

        public UserEvent(Student student){
            this.student = student;
        }
    }

    public static class PlaceEvent {
        public Place place;
        public List<Place> places;
        public int callerId = -1;

        public PlaceEvent(Place place){
            this.place = place;
        }

        public PlaceEvent(List<Place> places) {
            this.places = places;
        }

        public PlaceEvent(Place place, int callerId){
            this.place = place;
            this.callerId = callerId;
        }

        public PlaceEvent(List<Place> places, int callerId) {
            this.places = places;
            this.callerId = callerId;
        }
    }

    public static class RideEvent {
        public List<Ride> routes;
        public Ride route;

        public RideEvent(List<Ride> routes) {
            this.routes = routes;
        }

        public RideEvent(Ride route) {
            this.route = route;
        }
    }

    public static class RideRequestEvent{
        public RideSolicitation request;
        public List<RideSolicitation> requests;

        public RideRequestEvent(RideSolicitation request) {
            this.request = request;
        }

        public RideRequestEvent(List<RideSolicitation> requests) {
            this.requests = requests;
        }
    }

    public static class LoginEvent{
        public String token;

        public LoginEvent(String token){
            this.token = token;
        }
    }
}
