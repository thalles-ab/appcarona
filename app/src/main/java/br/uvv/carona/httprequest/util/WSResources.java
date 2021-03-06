package br.uvv.carona.httprequest.util;

/**
 * Created by Geen on 10/02/2016.
 */
public class WSResources {
    public static final String BASE_URL = "http://ec2-52-38-251-192.us-west-2.compute.amazonaws.com/wscarona/api";
    public static final String BASE_UPLOAD_URL = "http://ec2-52-38-251-192.us-west-2.compute.amazonaws.com/carona-upload/";
    public static final String URL_UPLOAD = "http://ec2-52-38-251-192.us-west-2.compute.amazonaws.com/carona-upload/api/file";
    public static final String RIDE = BASE_URL + "/ride";
    public static final String PLACE = BASE_URL + "/place";
    public static final String RIDE_SOLICITATION = BASE_URL + "/solicitationRide";
    public static final String RIDE_LIST = RIDE + "/list";
    public static final String RIDE_SOLICITATION_LIST = RIDE_SOLICITATION + "/list";
    public static final String GET_STUDENT = BASE_URL + "/";
    public static final String URL_STUDENT = BASE_URL + "/student";
    public static final String URL_LOGIN = BASE_URL + "/login";
    public static final String URL_STATISTIC = BASE_URL + "/stats";
    public static final String SEARCH_PLACE = PLACE + "/search/";
    public static final String DELETE_PLACE = PLACE + "/delete";
    public static final String DELETE_RIDE = RIDE + "/delete";
}
