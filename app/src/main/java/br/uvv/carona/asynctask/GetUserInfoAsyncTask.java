package br.uvv.carona.asynctask;

import org.greenrobot.eventbus.EventBus;

import br.uvv.carona.application.AppPartiUVV;
import br.uvv.carona.httprequest.BaseHttpRequest;
import br.uvv.carona.httprequest.util.HttpMethodUtil;
import br.uvv.carona.httprequest.util.WSResources;
import br.uvv.carona.model.Student;
import br.uvv.carona.util.EventBusEvents;

public class GetUserInfoAsyncTask extends BaseAsyncTask<Integer, Student> {

    @Override
    protected Student doInBackground(Integer... params) {
        try {
            StringBuilder url = new StringBuilder();
            url.append(WSResources.GET_STUDENT);
            url.append("?id=");
            if(params == null){
                url.append("-1");
            }else{
                url.append(params[0]);
            }
            return AppPartiUVV.sGson.fromJson(BaseHttpRequest.createRequest(HttpMethodUtil.GET, url.toString(), null, null), Student.class);
        }catch (Exception e){
            this.mException = e;
        }
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(Student student) {
        boolean success = student != null && this.mException == null;
        if(success){
            EventBus.getDefault().post(new EventBusEvents.UserEvent(student));
        }
        super.onPostExecute(student);
    }
}
