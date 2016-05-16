package br.uvv.carona.asynctask;

import org.greenrobot.eventbus.EventBus;

import br.uvv.carona.model.Ride;
import br.uvv.carona.service.RideService;
import br.uvv.carona.util.EventBusEvents;

public class GetRideAsyncTask extends BaseAsyncTask<Ride, Ride> {

    @Override
    protected Ride doInBackground(Ride... params) {
        try{
            return RideService.getRide(params[0]);
        }catch(Exception e){
            this.mException = e;
        }
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(Ride ride) {
        boolean success = ride != null && this.mException == null;
        if(success){
            EventBus.getDefault().post(new EventBusEvents.RideEvent(ride));
        }
        super.onPostExecute(ride);
    }
}
