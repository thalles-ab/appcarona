package br.uvv.carona.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import br.uvv.carona.model.BaseObject;
import br.uvv.carona.util.EventBusEvents;

/**
 * Created by CB1772 on 23/04/2016.
 */
public class BaseAsyncTask<Params, T> extends AsyncTask<Params, T, T> {
    protected Exception mException;

    @Override
    protected T doInBackground(Params... params) {
        return null;
    }


    //TODO tratar erros do baseObject
    @Override
    protected void onPostExecute(T t) {
        super.onPostExecute(t);
        if(this.mException != null){
            if(this.mException.getMessage() != null) {
                Log.e("GET_ROUTE", this.mException.getMessage());
            }
            EventBus.getDefault().post(new EventBusEvents.ErrorEvent(mException.getMessage()));
        }
    }

    public boolean success(T t){
        BaseObject ob = (BaseObject) t;
        return mException == null && (ob.erros == null || ob.erros.isEmpty());
    }
}
