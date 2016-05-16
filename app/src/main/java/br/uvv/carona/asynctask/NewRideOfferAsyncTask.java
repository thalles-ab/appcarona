package br.uvv.carona.asynctask;

import org.greenrobot.eventbus.EventBus;

import br.uvv.carona.application.AppPartiUVV;
import br.uvv.carona.httprequest.BaseHttpRequest;
import br.uvv.carona.httprequest.util.HttpMethodUtil;
import br.uvv.carona.httprequest.util.WSResources;
import br.uvv.carona.model.BaseObject;
import br.uvv.carona.model.Place;
import br.uvv.carona.model.Ride;
import br.uvv.carona.service.RideService;
import br.uvv.carona.util.EventBusEvents;

public class NewRideOfferAsyncTask extends BaseAsyncTask<Ride, BaseObject> {

    @Override
    protected BaseObject doInBackground(Ride... params) {
        try{
            return RideService.saveRide(params[0]);
        }catch (Exception e){
            this.mException = e;
        }
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(BaseObject aVoid) {
        boolean success = this.mException == null;
        if(success) {
            EventBus.getDefault().post(new EventBusEvents.NewRideEvent(success));
        }
        super.onPostExecute(aVoid);
    }
}
