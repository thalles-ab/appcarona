package br.uvv.carona.asynctask;

import org.greenrobot.eventbus.EventBus;

import br.uvv.carona.application.AppPartiUVV;
import br.uvv.carona.model.BaseObject;
import br.uvv.carona.model.Ride;
import br.uvv.carona.model.RideSolicitation;
import br.uvv.carona.service.RideSolicitationService;
import br.uvv.carona.util.EventBusEvents;
import br.uvv.carona.util.RideSolicitationStatus;

public class NewRideSolicitationAsyncTask extends BaseAsyncTask<RideSolicitation, BaseObject> {
    RideSolicitation rideSolicitation;

    @Override
    protected BaseObject doInBackground(RideSolicitation... params) {
        try {
            this.rideSolicitation = params[0];
            return RideSolicitationService.makeRequest(params[0]);
        }catch (Exception e){
            this.mException = e;
        }
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(BaseObject object) {
        boolean success = this.mException == null && (object == null || object.erros == null || object.erros.size() == 0);
        if(success) {
            rideSolicitation.status = RideSolicitationStatus.Waiting;
            rideSolicitation.student = AppPartiUVV.getStudent();
            AppPartiUVV.simuSolicitationMade.add(rideSolicitation);
            EventBus.getDefault().post(new EventBusEvents.SuccessEvent(success));
        }
        super.onPostExecute(object);
    }
}
