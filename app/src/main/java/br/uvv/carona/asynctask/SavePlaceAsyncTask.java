package br.uvv.carona.asynctask;

import org.greenrobot.eventbus.EventBus;

import br.uvv.carona.application.AppPartiUVV;
import br.uvv.carona.httprequest.BaseHttpRequest;
import br.uvv.carona.httprequest.util.HttpMethodUtil;
import br.uvv.carona.httprequest.util.WSResources;
import br.uvv.carona.model.Place;
import br.uvv.carona.util.EventBusEvents;

public class SavePlaceAsyncTask extends BaseAsyncTask<Place, Void> {

    @Override
    protected Void doInBackground(Place... params) {
        try{
            AppPartiUVV.sGson.fromJson(BaseHttpRequest
                    .createRequestWithAuthorization(HttpMethodUtil.POST, WSResources.PLACE,
                            AppPartiUVV.getToken(), params[0]), Place.class);
            return null;
        }catch (Exception e){
            this.mException = e;
        }
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        boolean success = this.mException == null;
        EventBus.getDefault().post(new EventBusEvents.SuccessEvent(success));
        super.onPostExecute(aVoid);
    }
}
