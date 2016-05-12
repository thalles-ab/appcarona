package br.uvv.carona.asynctask;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import br.uvv.carona.model.Place;
import br.uvv.carona.model.Ride;
import br.uvv.carona.model.RideRequest;
import br.uvv.carona.util.EventBusEvents;

/**
 * Created by CB1772 on 12/05/2016.
 */
public class GetRideRequestsAsyncTask extends BaseAsyncTask<Ride, List<RideRequest>> {

    @Override
    protected List<RideRequest> doInBackground(Ride... params) {
        try{
            //TODO MAKE SERVER CALL
        }catch (Exception e){
            this.mException = e;
        }
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(List<RideRequest> requests) {
        boolean success = requests != null && this.mException != null;
        if(success){
            EventBus.getDefault().post(new EventBusEvents.RideRequestEvent(requests));
        }
        super.onPostExecute(requests);
    }
}
