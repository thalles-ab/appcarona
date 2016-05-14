package br.uvv.carona.asynctask;

import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.PolyUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.uvv.carona.R;
import br.uvv.carona.application.AppPartiUVV;
import br.uvv.carona.httprequest.BaseHttpRequest;
import br.uvv.carona.httprequest.util.HttpMethodUtil;
import br.uvv.carona.model.Place;
import br.uvv.carona.model.Ride;
import br.uvv.carona.model.route.RouteRequest;
import br.uvv.carona.model.route.RouteResult;
import br.uvv.carona.util.EventBusEvents;

/**
 * Created by CB1772 on 23/04/2016.
 */
public class GetRouteAsyncTask extends BaseAsyncTask<RouteRequest, String> {

    private String mGoogleApiKey;

    public GetRouteAsyncTask(String googleApiKey){
        this.mGoogleApiKey = googleApiKey;
    }


    @Override
    protected String doInBackground(RouteRequest... params) {
        try{
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append("https://maps.googleapis.com/maps/api/directions/json?origin=");
            urlBuilder.append(Double.toString(params[0].start.latitude));
            urlBuilder.append(",");
            urlBuilder.append(Double.toString(params[0].start.longitude));
            urlBuilder.append("&destination=");
            urlBuilder.append(Double.toString(params[0].end.latitude));
            urlBuilder.append(",");
            urlBuilder.append(Double.toString(params[0].end.longitude));
            if(params[0].waypoints != null && params[0].waypoints.size() > 0){
                int size = params[0].waypoints.size();
                urlBuilder.append("&waypoints=");
                if(size > 1){
                    urlBuilder.append("via:");
                }
                for(int i = 0; i < size; i++){
                    List<LatLng> l = new ArrayList<>();
                    l.add(params[0].waypoints.get(i));
                    urlBuilder.append("enc:");
                    urlBuilder.append(PolyUtil.encode(l));
                    urlBuilder.append(":");
                    if(i != size-1){
                        urlBuilder.append("|");
                    }
                    l.clear();
                }
            }
            urlBuilder.append("&sensor=false&mode=driving&alternatives=true&language=");
            urlBuilder.append(Locale.getDefault().toString());
            urlBuilder.append("&key=");
            urlBuilder.append(mGoogleApiKey);
            return BaseHttpRequest.createRequestWithAuthorization(HttpMethodUtil.GET, urlBuilder.toString(), null, null);
        }catch (Exception e){
            this.mException = e;
            Log.e("ERROR REQUEST", e.getMessage());
        }
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(String result) {
        boolean success = !TextUtils.isEmpty(result) && this.mException == null;
        if(success){
            try {
                JSONObject route = new JSONObject(result);
                JSONArray status = route.getJSONArray("geocoded_waypoints");
                if(status.length() >= 1){
                    boolean statusOK = true;
                    for(int i = 0; i < status.length(); i++){
                        String r = new JSONObject(status.getString(i)).getString("geocoder_status");
                        statusOK = statusOK && r.toLowerCase().equals("ok");
                    }
                    if(statusOK) {
                        JSONArray routeArray = route.getJSONArray("routes");
                        Type type = new TypeToken<List<RouteResult>>() {
                        }.getType();
                        List<RouteResult> results = AppPartiUVV.sGson.fromJson(routeArray.toString(), type);
                        Ride routeRide = new Ride();
                        routeRide.routeGoogleFormat = results.get(0).overviewPolyLine.points;
                        Place start = new Place();
                        start.description = results.get(0).legs.get(0).startAddress;
                        start.latitude = results.get(0).legs.get(0).startLocation.lat;
                        start.longitude = results.get(0).legs.get(0).startLocation.lng;
                        routeRide.startPoint = start;
                        int legSize = results.get(0).legs.size() - 1;
                        Place end = new Place();
                        end.description = results.get(0).legs.get(legSize).endAddress;
                        end.latitude = results.get(0).legs.get(legSize).endLocation.lat;
                        end.longitude = results.get(0).legs.get(legSize).endLocation.lng;
                        routeRide.endPoint = end;
                        EventBus.getDefault().post(new EventBusEvents.RideEvent(routeRide));
                    }else{
                        this.mException = new Exception(AppPartiUVV.getStringText(R.string.msg_error_couldnt_get_route));
                    }
                }else{
                    this.mException = new Exception(AppPartiUVV.getStringText(R.string.msg_error_couldnt_get_route));
                }
            } catch (JSONException e) {
                this.mException = e;
            }
        }
        super.onPostExecute(result);
    }
}
