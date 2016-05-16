package br.uvv.carona.asynctask;

import android.os.AsyncTask;

import org.greenrobot.eventbus.EventBus;

import java.net.ConnectException;

import br.uvv.carona.exception.AuthenticationException;
import br.uvv.carona.exception.CommonException;
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

    @Override
    protected void onPostExecute(T t) {
        tratarErros();
        if(t == null){
           return;
        }
        if (t instanceof BaseObject) {
            BaseObject object = ((BaseObject) t);
            if (object.erros != null) {
                EventBus.getDefault().post(object.erros);
            }
        }
    }

    protected void tratarErros() {
        if (this.mException != null) {
            if (this.mException instanceof CommonException || this.mException instanceof ConnectException) {
                EventBus.getDefault().post(new EventBusEvents.ErrorEvent(mException.getMessage()));
            } else if (this.mException instanceof AuthenticationException) {
                EventBus.getDefault().post(new AuthenticationException());
            }
        }
    }
}
