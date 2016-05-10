package br.uvv.carona.model;

import br.uvv.carona.model.enums.TypePlace;

public class Place extends BaseObject {
    public Student student;
    public double latitude;
    public double longitude;
    public String description;
    public TypePlace placeType;

    public Place() {
    }

    public Place(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Place place = (Place) o;
        return place.id == this.id;
    }
}
