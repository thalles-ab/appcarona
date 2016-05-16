package br.uvv.carona.service;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import br.uvv.carona.application.AppPartiUVV;
import br.uvv.carona.httprequest.BaseHttpRequest;
import br.uvv.carona.httprequest.util.HttpMethodUtil;
import br.uvv.carona.httprequest.util.WSResources;
import br.uvv.carona.model.Ride;

public class RideService {

    public static List<Ride> getOffersByLocation(Ride ride) throws Exception{
        Type type = new TypeToken<List<Ride>>() {}.getType();
        List<Ride> rides = AppPartiUVV.sGson.fromJson(BaseHttpRequest.createRequestWithAuthorization(HttpMethodUtil.POST,
                WSResources.RIDE_LIST, AppPartiUVV.getToken(), ride), type);
        return rides;
    }

    public static List<Ride> getUserOffers() throws Exception{
        Type type = new TypeToken<List<Ride>>() {}.getType();
        //TODO
//        List<Ride> rides = AppPartiUVV.sGson.fromJson(BaseHttpRequest.createRequestWithAuthorization(HttpMethodUtil.POST,
//                WSResources.RIDE_LIST, AppPartiUVV.getToken(), ride), type);
        return null;
    }

}
