package br.uvv.carona.service;

import br.uvv.carona.application.AppPartiUVV;
import br.uvv.carona.httprequest.BaseHttpRequest;
import br.uvv.carona.httprequest.util.HttpMethodUtil;
import br.uvv.carona.httprequest.util.WSResources;
import br.uvv.carona.model.Student;
import br.uvv.carona.model.UploadFile;

/**
 * Created by Nathalia on 15/05/2016.
 */
public class UploadService {
    public static UploadFile upload(Student student) throws Exception {
//        return AppPartiUVV.sGson.fromJson(BaseHttpRequest.createRequest(HttpMethodUtil.POST, WSResources.URL_UPLOAD, student), UploadFile.class);
        return null;
    }

}
