package br.uvv.carona.asynctask;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import br.uvv.carona.model.Ride;
import br.uvv.carona.model.RideSolicitation;
import br.uvv.carona.service.RideSolicitationService;
import br.uvv.carona.util.EventBusEvents;

public class GetRideSolicitationAsyncTask extends BaseAsyncTask<Ride, List<RideSolicitation>> {

    @Override
    protected List<RideSolicitation> doInBackground(Ride... params) {
        try{
            return RideSolicitationService.getSolicitations();
        }catch (Exception e){
            this.mException = e;
        }
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(List<RideSolicitation> requests) {
        boolean success = requests != null && this.mException != null;
        if(success){
            EventBus.getDefault().post(new EventBusEvents.RideRequestEvent(requests));
        }
        super.onPostExecute(requests);
    }
}
