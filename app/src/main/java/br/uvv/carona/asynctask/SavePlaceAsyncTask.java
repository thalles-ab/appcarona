package br.uvv.carona.asynctask;

import org.greenrobot.eventbus.EventBus;

import br.uvv.carona.application.AppPartiUVV;
import br.uvv.carona.httprequest.BaseHttpRequest;
import br.uvv.carona.httprequest.util.HttpMethodUtil;
import br.uvv.carona.httprequest.util.WSResources;
import br.uvv.carona.model.Place;
import br.uvv.carona.util.EventBusEvents;

public class SavePlaceAsyncTask extends BaseAsyncTask<Place, Place> {

    private boolean isEdit;
    private Place mPlace;

    @Override
    protected Place doInBackground(Place... params) {
        try{
            this.isEdit = params[0].id > 0;
            if(isEdit){
                this.mPlace = params[0];
            }
            return AppPartiUVV.sGson.fromJson(BaseHttpRequest
                    .createRequest(HttpMethodUtil.POST, WSResources.PLACE, params[0]), Place.class);
        }catch (Exception e){
            this.mException = e;
        }
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(Place object) {
        boolean success = this.mException == null;
        if(success) {
            if(this.isEdit){
                EventBus.getDefault().post(new EventBusEvents.PlaceUpdateEvent(false, this.mPlace));
            }
            EventBus.getDefault().post(new EventBusEvents.SuccessEvent(success, object));
        }
        super.onPostExecute(object);
    }
}
