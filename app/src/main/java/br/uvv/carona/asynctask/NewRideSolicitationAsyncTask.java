package br.uvv.carona.asynctask;

import org.greenrobot.eventbus.EventBus;

import br.uvv.carona.model.BaseObject;
import br.uvv.carona.model.Ride;
import br.uvv.carona.model.RideSolicitation;
import br.uvv.carona.service.RideSolicitationService;
import br.uvv.carona.util.EventBusEvents;

public class NewRideSolicitationAsyncTask extends BaseAsyncTask<RideSolicitation, BaseObject> {

    @Override
    protected BaseObject doInBackground(RideSolicitation... params) {
        try {
            return RideSolicitationService.makeRequest(params[0]);
        }catch (Exception e){
            this.mException = e;
        }
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(BaseObject aVoid) {
        boolean success = this.mException != null;
        EventBus.getDefault().post(new EventBusEvents.SuccessEvent(success));
        super.onPostExecute(aVoid);
    }
}
