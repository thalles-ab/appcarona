package br.uvv.carona.asynctask;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import br.uvv.carona.application.AppPartiUVV;
import br.uvv.carona.httprequest.BaseHttpRequest;
import br.uvv.carona.httprequest.util.HttpMethodUtil;
import br.uvv.carona.httprequest.util.WSResources;
import br.uvv.carona.model.BaseObject;
import br.uvv.carona.model.Place;
import br.uvv.carona.util.EventBusEvents;

public class RemovePlaceAsyncTask extends BaseAsyncTask<List<Place>, BaseObject> {

    @Override
    protected BaseObject doInBackground(List<Place>... params) {
        try{
            return AppPartiUVV.sGson.fromJson(BaseHttpRequest.createRequest(HttpMethodUtil.POST,
                    WSResources.DELETE_PLACE, params[0]), BaseObject.class);
        }catch (Exception e){
            this.mException = e;
        }
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(BaseObject baseObject) {
        boolean success = this.mException == null;
        if(success){
            EventBus.getDefault().post(new EventBusEvents.PlaceUpdateEvent(true));
        }
        super.onPostExecute(baseObject);
    }
}
