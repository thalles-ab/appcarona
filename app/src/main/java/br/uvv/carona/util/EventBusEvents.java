package br.uvv.carona.util;

import java.util.List;

import br.uvv.carona.model.Place;
import br.uvv.carona.model.Ride;
import br.uvv.carona.model.RideRequest;
import br.uvv.carona.model.Student;
import br.uvv.carona.model.StudentInfo;

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

    public static class UserEvent{
        public Student student;

        public UserEvent(Student student){
            this.student = student;
        }
    }

    public static class PlaceEvent {
        public Place place;
        public List<Place> places;

        public PlaceEvent(Place place){
            this.place = place;
        }

        public PlaceEvent(List<Place> places) {
            this.places = places;
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
        public RideRequest request;
        public List<RideRequest> requests;

        public RideRequestEvent(RideRequest request) {
            this.request = request;
        }

        public RideRequestEvent(List<RideRequest> requests) {
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
