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
        public Object object;

        public SuccessEvent(boolean success){
            this.success = success;
        }

        public SuccessEvent(boolean success, Object object){
            this.success = success;
            this.object = object;
        }
    }

    public static class SuccessAnswerRideSolicitation{
        public boolean isAccept;
        public boolean success;
        public RideSolicitation solicitation;

        public SuccessAnswerRideSolicitation(boolean isAccept, boolean success, RideSolicitation solicitation) {
            this.isAccept = isAccept;
            this.success = success;
            this.solicitation = solicitation;
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

    public static class PlaceAddressEvent{
        public Place place;
        public List<Place> places;
        public int callerId = -1;

        public PlaceAddressEvent(Place place){
            this.place = place;
        }

        public PlaceAddressEvent(List<Place> places) {
            this.places = places;
        }

        public PlaceAddressEvent(Place place, int callerId){
            this.place = place;
            this.callerId = callerId;
        }

        public PlaceAddressEvent(List<Place> places, int callerId) {
            this.places = places;
            this.callerId = callerId;
        }
    }

    public static class RideEvent {
        public List<Ride> rides;
        public Ride ride;
        public boolean success = false;

        public RideEvent(List<Ride> rides) {
            this.rides = rides;
        }

        public RideEvent(Ride ride) {
            this.ride = ride;
        }
    }

    public static class RouteEvent{
        public Ride route;
        public boolean success = false;

        public RouteEvent(Ride route) {
            this.route = route;
        }
    }

    public static class NewRideEvent{
        public boolean success;

        public NewRideEvent(boolean success) {
            this.success = success;
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

    public static class PlaceUpdateEvent{
        public boolean isDelete;
        public Place place;

        public PlaceUpdateEvent(boolean isDelete) {
            this.isDelete = isDelete;
            place = null;
        }

        public PlaceUpdateEvent(boolean isDelete, Place place) {
            this.isDelete = isDelete;
            this.place = place;
        }
    }

    public static class LoginEvent{
        public String token;

        public LoginEvent(String token){
            this.token = token;
        }
    }
}
