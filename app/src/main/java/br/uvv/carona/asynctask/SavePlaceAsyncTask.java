package br.uvv.carona.asynctask;

import org.greenrobot.eventbus.EventBus;

import br.uvv.carona.model.Place;
import br.uvv.carona.service.PlaceService;
import br.uvv.carona.util.EventBusEvents;

public class SavePlaceAsyncTask extends BaseAsyncTask<Place, Place> {

    private boolean isEdit;
    private Place mPlace;

    @Override
    protected Void doInBackground(Place... params) {
        try{
            this.isEdit = params[0].id > 0;
            //As on update there's no return, I must send to view the change if the call was a success
            if(isEdit){
                this.mPlace = params[0];
            }
            return PlaceService.saveOrUpdatePlace(params[0]);
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
