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

public class PlaceService {

    public static List<Place> getUserPlaces() throws Exception{
        Type type = new TypeToken<List<Place>>() {}.getType();
        List<Place> places = AppPartiUVV.sGson.fromJson(BaseHttpRequest.createRequest(HttpMethodUtil.GET,
                WSResources.PLACE, null), type);
        return places;
    }

    public static BaseObject removePlaces(List<Place> places) throws Exception{
        return AppPartiUVV.sGson.fromJson(BaseHttpRequest.createRequest(HttpMethodUtil.POST,
                WSResources.DELETE_PLACE,  places), BaseObject.class);
    }

    public static Place saveOrUpdatePlace(Place place) throws Exception{
        return AppPartiUVV.sGson.fromJson(BaseHttpRequest
                .createRequest(HttpMethodUtil.POST, WSResources.PLACE, place), Place.class);
    }

}
