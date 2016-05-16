package br.uvv.carona.service;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import br.uvv.carona.application.AppPartiUVV;
import br.uvv.carona.httprequest.BaseHttpRequest;
import br.uvv.carona.httprequest.util.HttpMethodUtil;
import br.uvv.carona.httprequest.util.WSResources;
import br.uvv.carona.model.BaseObject;
import br.uvv.carona.model.Ride;
import br.uvv.carona.model.RideSolicitation;

public class RideSolicitationService {

    public static BaseObject makeRequest(RideSolicitation solicitation) throws Exception{
        return AppPartiUVV.sGson.fromJson(BaseHttpRequest.createRequest(HttpMethodUtil.POST, WSResources.RIDE_SOLICITATION, solicitation), BaseObject.class);
    }

    public static List<RideSolicitation> getSolicitations() throws Exception{
        Type type = new TypeToken<List<RideSolicitation>>() {}.getType();
        List<RideSolicitation> solicitations = AppPartiUVV.sGson.fromJson(BaseHttpRequest.createRequest(HttpMethodUtil.GET,
                WSResources.RIDE_SOLICITATION_LIST, null), type);
        return solicitations;
    }

    public static BaseObject acceptSolicitation(RideSolicitation solicitation) throws Exception{
        return AppPartiUVV.sGson.fromJson(BaseHttpRequest.createRequest(HttpMethodUtil.PUT, WSResources.RIDE_SOLICITATION, solicitation), BaseObject.class);
    }

    public static BaseObject refuseSolicitation(RideSolicitation solicitation) throws Exception{
        StringBuilder builder = new StringBuilder("/");
        builder.append(solicitation.id);
        return AppPartiUVV.sGson.fromJson(BaseHttpRequest.createRequest(HttpMethodUtil.DELETE,
                WSResources.RIDE_SOLICITATION+builder.toString(), solicitation), BaseObject.class);
    }

}
