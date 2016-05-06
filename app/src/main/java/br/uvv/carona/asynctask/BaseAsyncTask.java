package br.uvv.carona.asynctask;

import android.os.AsyncTask;

import org.greenrobot.eventbus.EventBus;

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

    @Override
    protected void onPostExecute(T t) {
        super.onPostExecute(t);
        if(this.mException != null){
            EventBus.getDefault().post(new EventBusEvents.ErrorEvent(mException.getMessage()));
        }
    }
}
