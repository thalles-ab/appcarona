package br.uvv.carona.asynctask;

import org.greenrobot.eventbus.EventBus;

import br.uvv.carona.model.RideRequest;
import br.uvv.carona.util.EventBusEvents;

/**
 * Created by CB1772 on 12/05/2016.
 */
public class NewRideRequestAsyncTask extends BaseAsyncTask<RideRequest, Void> {

    @Override
    protected Void doInBackground(RideRequest... params) {
        try {
            //TODO SERVER CALL
        }catch (Exception e){
            this.mException = e;
        }
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        boolean success = this.mException != null;
        EventBus.getDefault().post(new EventBusEvents.SuccessEvent(success));
        super.onPostExecute(aVoid);
    }
}