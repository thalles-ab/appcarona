package br.uvv.carona.model.route;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RouteResult implements Serializable {

    public String copyrights;
    public List<Leg> legs;
    @SerializedName("overview_polyline")
    public Polyline overviewPolyLine;

    public RouteResult() {
        legs = new ArrayList<>();
    }

    public class Polyline implements Serializable{
        public String points;
    }

    public class LatLng implements Serializable{
        public double lat;
        public double lng;
    }

    public class Leg implements Serializable{
        public Distance distance;
        public Duration duration;
        @SerializedName("end_address")
        public String endAddress;
        @SerializedName("end_location")
        public LatLng endLocation;
        @SerializedName("start_address")
        public String startAddress;
        @SerializedName("start_location")
        public LatLng startLocation;
        public List<Step> steps;

        public Leg() {
            steps = new ArrayList<>();
        }

    }

    public class Distance implements Serializable{
        public String text;
        public long value;

        public Distance(String text, long value) {
            this.text = text;
            this.value = value;
        }

    }

    public class Duration implements Serializable{
        public String text;
        public long value;

        public Duration(String text, long value) {
            this.text = text;
            this.value = value;
        }

    }

    public class Step implements Serializable{
        public Distance distance;
        public Duration duration;
        @SerializedName("end_location")
        public LatLng endLocation;
        @SerializedName("start_location")
        public LatLng startLocation;
    }

}
