package br.uvv.carona.service;

import br.uvv.carona.application.AppPartiUVV;
import br.uvv.carona.httprequest.BaseHttpRequest;
import br.uvv.carona.httprequest.util.HttpMethodUtil;
import br.uvv.carona.httprequest.util.WSResources;
import br.uvv.carona.model.BaseObject;
import br.uvv.carona.model.Student;

/**
 * Created by geen-20 on 14/05/2016.
 */
public class StudentService {

    public static BaseObject createStudent(Student student) throws Exception {
        return AppPartiUVV.sGson.fromJson(BaseHttpRequest.createRequest(HttpMethodUtil.POST, WSResources.URL_STUDENT, student), BaseObject.class);
    }

    public static BaseObject updateStudent(Student student) throws Exception {
        return AppPartiUVV.sGson.fromJson(BaseHttpRequest.createRequest(HttpMethodUtil.PUT, WSResources.URL_STUDENT, student), BaseObject.class);
    }

    public static Student getStudent(Student student) throws Exception {
        return AppPartiUVV.sGson.fromJson(BaseHttpRequest.createRequest(HttpMethodUtil.PUT, WSResources.URL_STUDENT, student), Student.class);
    }
}