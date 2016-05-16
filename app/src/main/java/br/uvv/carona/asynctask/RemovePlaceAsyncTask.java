package br.uvv.carona.asynctask;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import br.uvv.carona.model.BaseObject;
import br.uvv.carona.model.Place;
import br.uvv.carona.service.PlaceService;
import br.uvv.carona.util.EventBusEvents;

public class RemovePlaceAsyncTask extends BaseAsyncTask<List<Place>, BaseObject> {

    @Override
    protected BaseObject doInBackground(List<Place>... params) {
        try{
            return PlaceService.removePlaces(params[0]);
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
