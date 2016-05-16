package br.uvv.carona.asynctask;

import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.List;

import br.uvv.carona.application.AppPartiUVV;
import br.uvv.carona.httprequest.BaseHttpRequest;
import br.uvv.carona.httprequest.util.HttpMethodUtil;
import br.uvv.carona.httprequest.util.WSResources;
import br.uvv.carona.model.Place;
import br.uvv.carona.model.Ride;
import br.uvv.carona.util.EventBusEvents;

public class GetRideOffersAsyncTask extends BaseAsyncTask<Ride, List<Ride>> {

    private Ride mRide;

    public GetRideOffersAsyncTask(Ride mRide) {
        this.mRide = mRide;
    }

    @Override
    protected List<Ride> doInBackground(Ride... params) {
        try{
            Type type = new TypeToken<List<Ride>>() {}.getType();
            List<Ride> rides = AppPartiUVV.sGson.fromJson(BaseHttpRequest.createRequestWithAuthorization(HttpMethodUtil.POST,
                    WSResources.RIDE_LIST, AppPartiUVV.getToken(), mRide), type);
            return rides;
        }catch (Exception e){
            this.mException = e;
        }
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(List<Ride> rides) {
        boolean success = rides != null && this.mException == null;
        if(success){
            EventBus.getDefault().post(new EventBusEvents.RideEvent(rides));
        }
        super.onPostExecute(rides);
    }
}
