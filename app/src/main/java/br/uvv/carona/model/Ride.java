package br.uvv.carona.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Ride extends BaseObject {

    @SerializedName("dono")
    public Student owner;
    public Date creation;
    public Date validation;
    public int maxQuantity;

}
