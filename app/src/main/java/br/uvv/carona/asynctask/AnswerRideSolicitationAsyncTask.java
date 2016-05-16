package br.uvv.carona.asynctask;

import org.greenrobot.eventbus.EventBus;

import br.uvv.carona.model.BaseObject;
import br.uvv.carona.model.Ride;
import br.uvv.carona.model.RideSolicitation;
import br.uvv.carona.service.RideSolicitationService;
import br.uvv.carona.util.EventBusEvents;

public class AnswerRideSolicitationAsyncTask extends BaseAsyncTask<RideSolicitation, BaseObject> {

    private boolean mIsAccept;
    private RideSolicitation mSolicitation;

    public AnswerRideSolicitationAsyncTask(boolean isAccept, RideSolicitation rideSolicitation) {
        this.mIsAccept = isAccept;
        this.mSolicitation = rideSolicitation;
    }

    @Override
    protected BaseObject doInBackground(RideSolicitation... params) {
        try{
            if(this.mIsAccept) {
                return RideSolicitationService.acceptSolicitation(this.mSolicitation);
            }
            return RideSolicitationService.refuseSolicitation(this.mSolicitation);
        }catch(Exception e){
            this.mException = e;
        }
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(BaseObject baseObject) {
        boolean success = this.mException == null;
        if(success){
            EventBus.getDefault().post(new EventBusEvents.SuccessAnswerRideSolicitation(this.mIsAccept, true,this.mSolicitation));
        }
        super.onPostExecute(baseObject);
    }
}
