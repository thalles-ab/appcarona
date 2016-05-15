package br.uvv.carona.asynctask;

import org.greenrobot.eventbus.EventBus;

import br.uvv.carona.model.BaseObject;
import br.uvv.carona.service.LoginService;
import br.uvv.carona.util.EventBusEvents;

/**
 * Created by geen-20 on 14/05/2016.
 */
public class AutoLoginAsyncTask extends BaseAsyncTask<String, BaseObject> {

    @Override
    protected BaseObject doInBackground(String... params) {
        try{
            return LoginService.loginWithToken(params[0]);
        }catch (Exception e){
            this.mException = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(BaseObject studentInfo) {
        boolean success = studentInfo == null && this.mException == null;
        if(success){
            EventBus.getDefault().post(new EventBusEvents.SuccessEvent(true));
        }
        super.onPostExecute(studentInfo);
    }
}
