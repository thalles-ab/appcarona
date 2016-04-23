package br.uvv.carona.model;

/**
 * Created by CB1772 on 17/04/2016.
 */
public class Place extends BaseObject {
    public String address;
    public double latitude;
    public double longitude;

    public Place() {
    }

    public Place(String address) {
        this.address = address;
    }

    public Place(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Place(String address, double latitude, double longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
