package br.uvv.carona.asynctask;

import br.uvv.carona.model.BaseObject;
import br.uvv.carona.model.Ride;
import br.uvv.carona.service.RideService;

public class CancelRideAsyncTask extends BaseAsyncTask<Ride, BaseObject> {

    @Override
    protected BaseObject doInBackground(Ride... params) {
        try{
            return RideService.cancelRide(params[0]);
        }catch(Exception e){
            this.mException = e;
        }
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(BaseObject baseObject) {
        boolean success = this.mException == null;
        if(success){

        }
        super.onPostExecute(baseObject);
    }
}
