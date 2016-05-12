package br.uvv.carona.asynctask;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import br.uvv.carona.model.Place;
import br.uvv.carona.model.Ride;
import br.uvv.carona.util.EventBusEvents;

public class GetRideOffersAsyncTask extends BaseAsyncTask<Place, List<Ride>> {

    @Override
    protected List<Ride> doInBackground(Place... params) {
        try{
            //TODO MAKE SERVER CALL
        }catch (Exception e){
            this.mException = e;
        }
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(List<Ride> rides) {
        boolean success = rides != null && this.mException != null;
        if(success){
            EventBus.getDefault().post(new EventBusEvents.RideEvent(rides));
        }
        super.onPostExecute(rides);
    }
}
