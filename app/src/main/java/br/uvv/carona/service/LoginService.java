package br.uvv.carona.service;

import br.uvv.carona.application.AppPartiUVV;
import br.uvv.carona.httprequest.BaseHttpRequest;
import br.uvv.carona.httprequest.util.HttpMethodUtil;
import br.uvv.carona.httprequest.util.WSResources;
import br.uvv.carona.model.BaseObject;
import br.uvv.carona.model.Student;
import br.uvv.carona.model.StudentInfo;

/**
 * Created by geen-20 on 14/05/2016.
 */
public class LoginService {

    public static StudentInfo login(Student student) throws Exception {
        return AppPartiUVV.sGson.fromJson(BaseHttpRequest.createRequest(HttpMethodUtil.POST, WSResources.URL_LOGIN, student), StudentInfo.class);
    }

    public static BaseObject loginWithToken(String token) throws Exception {
        return AppPartiUVV.sGson.fromJson(BaseHttpRequest.createRequestWithAuthorization(HttpMethodUtil.POST, WSResources.URL_LOGIN,token, null), BaseObject.class);
    }

}
