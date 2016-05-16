package br.uvv.carona.service;

import java.util.HashMap;
import java.util.Map;

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

    public static Student getStudent(Long id) throws Exception {
        Map<String, Object> map = new HashMap<>();
        if(id > 0){
            map.put("id", id);
        }
        return AppPartiUVV.sGson.fromJson(BaseHttpRequest.createRequest(HttpMethodUtil.GET, WSResources.URL_STUDENT, map.isEmpty() ? null : map), Student.class);
    }
}