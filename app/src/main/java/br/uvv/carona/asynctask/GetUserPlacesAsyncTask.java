package br.uvv.carona.asynctask;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import br.uvv.carona.model.Place;
import br.uvv.carona.service.PlaceService;
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
            return PlaceService.getUserPlaces();
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
