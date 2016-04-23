package br.uvv.carona.model.route;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RouteResult implements Serializable {
    @SerializedName("geocoded_waypoints")
    public List<GeocodedWaypoints> geocodedWaypoints;
    @SerializedName("routes")
    public List<Route> routes;

    public class GeocodedWaypoints implements Serializable {
        @SerializedName("geocoder_status")
        public String geocoderStatus;
        @SerializedName("place_id")
        public String placeId;
        @SerializedName("types")
        List<String> types;
    }

    public class Route implements Serializable {
        public Bound bounds;
        public String copyrights;
        public List<Leg> legs;

        public class Bound implements Serializable{
            public LatLngRoute northeast;
            public LatLngRoute southwest;

            public class LatLngRoute implements Serializable{
                public double lat;
                public double lng;
            }
        }

        public class Leg implements Serializable{
            public Distance distance;
            public Duration duration;
            @SerializedName("start_address")
            public String startAddress;
            @SerializedName("start_location")
            public Bound.LatLngRoute startLocation;
            @SerializedName("end_address")
            public String endAddress;
            @SerializedName("end_location")
            public Bound.LatLngRoute endLocation;
            public List<Step> steps;

            public class Distance implements Serializable{
                public String text;
                public long value;
            }

            public class Duration implements Serializable{
                public String text;
                public long value;
            }

            public class Step implements Serializable{
                public Distance distance;
                public Duration duration;
                public String startAddress;
                @SerializedName("start_location")
                public Bound.LatLngRoute startLocation;
                @SerializedName("end_location")
                public Bound.LatLngRoute endLocation;
                @SerializedName("html_instructions")
                public String htmlInstructions;
            }
        }
    }

}
