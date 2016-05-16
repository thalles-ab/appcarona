package br.uvv.carona.asynctask;

import org.greenrobot.eventbus.EventBus;

import br.uvv.carona.model.BaseObject;
import br.uvv.carona.model.RideSolicitation;
import br.uvv.carona.service.RideSolicitationService;
import br.uvv.carona.util.EventBusEvents;

public class AnswerRideSolicitationAsyncTask extends BaseAsyncTask<RideSolicitation, BaseObject> {

    @Override
    protected BaseObject doInBackground(RideSolicitation... params) {
        try{
            return RideSolicitationService.acceptSolicitation(params[0]);
        }catch(Exception e){
            this.mException = e;
        }
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(BaseObject baseObject) {
        boolean success = this.mException == null;
        if(success){
            EventBus.getDefault().post(new EventBusEvents.SuccessAnswerRideSolicitation(true, true));
        }
        super.onPostExecute(baseObject);
    }
}
