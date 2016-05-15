package br.uvv.carona.model;

import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.uvv.carona.model.enums.TypeDay;
import br.uvv.carona.model.enums.TypeSituation;

public class Ride extends BaseObject {
    public Student student;
    public List<Student> students;
    public TypeDay dayType;
    public Date expirationDate;
    public int quantityPassengers;
    public String routeGoogleFormat;
    public Place startPoint;
    public Place endPoint;
    public TypeSituation situation;

    public Ride() {
        this.students = new ArrayList<>();
    }

    public List<LatLng> getDecodedPoints(){
        return (TextUtils.isEmpty(this.routeGoogleFormat)) ? new ArrayList<LatLng>() : PolyUtil.decode(this.routeGoogleFormat);
    }
}
