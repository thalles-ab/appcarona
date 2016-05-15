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
import br.uvv.carona.util.EventBusEvents;

public class GetUserPlacesAsyncTask extends BaseAsyncTask<Void, List<Place>> {
    private int mCallerId;
    public GetUserPlacesAsyncTask(){
        mCallerId = -1;
    }

    public GetUserPlacesAsyncTask(int callerId){
        this.mCallerId = callerId;
    }

    @Override
    protected List<Place> doInBackground(Void... params) {
        try{
            Type type = new TypeToken<List<Place>>() {}.getType();
            List<Place> places = AppPartiUVV.sGson.fromJson(BaseHttpRequest.createRequestWithAuthorization(HttpMethodUtil.GET, WSResources.PLACE, AppPartiUVV.getToken(), null), type);
            return places;
        }catch(Exception e){
            this.mException = e;
        }
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(List<Place> places) {
        boolean success = places != null && this.mException == null;
        if(success){
            EventBus.getDefault().post(new EventBusEvents.PlaceEvent(places, this.mCallerId));
        }
        super.onPostExecute(places);
    }
}
