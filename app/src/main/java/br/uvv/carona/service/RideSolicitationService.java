package br.uvv.carona.service;

import br.uvv.carona.application.AppPartiUVV;
import br.uvv.carona.httprequest.BaseHttpRequest;
import br.uvv.carona.httprequest.util.HttpMethodUtil;
import br.uvv.carona.httprequest.util.WSResources;
import br.uvv.carona.model.BaseObject;
import br.uvv.carona.model.Ride;

public class RideSolicitationService {

    public static BaseObject makeRequest(Ride ride) throws Exception{
        return AppPartiUVV.sGson.fromJson(BaseHttpRequest.createRequest(HttpMethodUtil.POST, WSResources.RIDE_SOLICITATION, ride), BaseObject.class);
    }

}
