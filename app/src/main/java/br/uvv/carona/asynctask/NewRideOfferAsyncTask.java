package br.uvv.carona.asynctask;

import org.greenrobot.eventbus.EventBus;

import br.uvv.carona.application.AppPartiUVV;
import br.uvv.carona.httprequest.BaseHttpRequest;
import br.uvv.carona.httprequest.util.HttpMethodUtil;
import br.uvv.carona.httprequest.util.WSResources;
import br.uvv.carona.model.Ride;
import br.uvv.carona.util.EventBusEvents;

public class NewRideOfferAsyncTask extends BaseAsyncTask<Ride, Void> {

    @Override
    protected Void doInBackground(Ride... params) {
        try{
            Ride ride = params[0];
            if(ride.startPoint.id < 1){
                ride.startPoint.id = 0;
                ride.startPoint = AppPartiUVV.sGson.fromJson(BaseHttpRequest
                        .createRequestWithAuthorization(HttpMethodUtil.POST, WSResources.PLACE,
                                AppPartiUVV.getToken(), ride.startPoint), Place.class);
            }
            if(ride.endPoint.id < 1){
                ride.endPoint.id = 0;
                ride.endPoint = AppPartiUVV.sGson.fromJson(BaseHttpRequest
                        .createRequestWithAuthorization(HttpMethodUtil.POST, WSResources.PLACE,
                                AppPartiUVV.getToken(), ride.endPoint), Place.class);
            }
            BaseHttpRequest.createRequestWithAuthorization(HttpMethodUtil.POST, WSResources.RIDE, AppPartiUVV.getToken(), ride);
        }catch (Exception e){
            this.mException = e;
        }
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        boolean success = this.mException == null;
        if(success) {
            EventBus.getDefault().post(new EventBusEvents.NewRideEvent(success));
        }
        super.onPostExecute(aVoid);
    }
}
