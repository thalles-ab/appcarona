package br.uvv.carona.asynctask;

import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.uvv.carona.application.AppPartiUVV;
import br.uvv.carona.httprequest.BaseHttpRequest;
import br.uvv.carona.httprequest.util.HttpMethodUtil;
import br.uvv.carona.model.route.RouteResult;
import br.uvv.carona.util.EventBusEvents;

/**
 * Created by CB1772 on 23/04/2016.
 */
public class GetRouteAsyncTask extends BaseAsyncTask<LatLng, String> {

    private String mGoogleApiKey;

    public GetRouteAsyncTask(String googleApiKey){
        this.mGoogleApiKey = googleApiKey;
    }


    @Override
    protected String doInBackground(LatLng... params) {
        if(params.length == 2){
            try{
                StringBuilder urlBuilder = new StringBuilder();
                urlBuilder.append("https://maps.googleapis.com/maps/api/directions/json?origin=");
                urlBuilder.append(Double.toString(params[0].latitude));
                urlBuilder.append(",");
                urlBuilder.append(Double.toString(params[0].longitude));
                urlBuilder.append("&destination=");
                urlBuilder.append(Double.toString(params[1].latitude));
                urlBuilder.append(",");
                urlBuilder.append(Double.toString(params[1].longitude));
                urlBuilder.append("&sensor=false&mode=driving&alternatives=true&key=");
                urlBuilder.append(mGoogleApiKey);
                return BaseHttpRequest.createRequest(HttpMethodUtil.GET, urlBuilder.toString(), null, null);
            }catch (Exception e){
                this.mException = e;
            }
        }
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(String result) {
        boolean success = !TextUtils.isEmpty(result) && this.mException == null;
        if(success){
            //TODO RETURN ROUTE
            try {
                JSONObject route = new JSONObject(result);
                JSONArray routeArray = route.getJSONArray("routes");
                JSONObject routes = routeArray.getJSONObject(0);
                JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
                String encodedString = overviewPolylines.getString("points");
                List<LatLng> list = decodePoly(encodedString);

                RouteResult r = AppPartiUVV.sGson.fromJson(result, RouteResult.class);

                EventBus.getDefault().post(new EventBusEvents.RouteEvent(list, r));
            } catch (JSONException e) {
                this.mException = e;
            }
        }
        super.onPostExecute(result);
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }
}
