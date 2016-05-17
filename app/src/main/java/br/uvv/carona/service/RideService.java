package br.uvv.carona.service;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import br.uvv.carona.application.AppPartiUVV;
import br.uvv.carona.httprequest.BaseHttpRequest;
import br.uvv.carona.httprequest.util.HttpMethodUtil;
import br.uvv.carona.httprequest.util.WSResources;
import br.uvv.carona.model.BaseObject;
import br.uvv.carona.model.Place;
import br.uvv.carona.model.Ride;

public class RideService {

    public static List<Ride> getOffersByLocation(Ride ride) throws Exception{
        Type type = new TypeToken<List<Ride>>() {}.getType();
        List<Ride> rides = AppPartiUVV.sGson.fromJson(BaseHttpRequest.createRequest(HttpMethodUtil.POST,
                WSResources.RIDE_LIST,  ride), type);
        return rides;
    }

    public static List<Ride> getUserOffers() throws Exception{
        Type type = new TypeToken<List<Ride>>() {}.getType();
        List<Ride> rides = AppPartiUVV.sGson.fromJson(BaseHttpRequest.createRequest(HttpMethodUtil.GET,
                WSResources.RIDE, null), type);
        return rides;
    }

    public static Ride getRide(Ride ride) throws Exception{
        StringBuilder builder = new StringBuilder("/");
        builder.append(ride.id);
        return AppPartiUVV.sGson.fromJson(BaseHttpRequest.createRequest(HttpMethodUtil.GET,
                WSResources.RIDE+builder.toString(), ride), Ride.class);
    }

    public static BaseObject saveRide(Ride ride) throws Exception{
        if(ride.startPoint.id < 1){
            ride.startPoint.id = 0;
            ride.startPoint.id = AppPartiUVV.sGson.fromJson(BaseHttpRequest
                    .createRequest(HttpMethodUtil.POST, WSResources.PLACE, ride.startPoint), Place.class).id;
        }
        if(ride.endPoint.id < 1){
            ride.endPoint.id = 0;
            ride.endPoint.id = AppPartiUVV.sGson.fromJson(BaseHttpRequest
                    .createRequest(HttpMethodUtil.POST, WSResources.PLACE, ride.endPoint), Place.class).id;
        }
        return AppPartiUVV.sGson.fromJson(BaseHttpRequest.createRequest(HttpMethodUtil.POST, WSResources.RIDE, ride), BaseObject.class);
    }

    public static BaseObject cancelRide(Ride ride) throws Exception{
        StringBuilder builder = new StringBuilder("/");
        builder.append(ride.id);
        return AppPartiUVV.sGson.fromJson(BaseHttpRequest.createRequest(HttpMethodUtil.DELETE,
                WSResources.DELETE_RIDE+builder.toString(), ride), BaseObject.class);
    }

}
