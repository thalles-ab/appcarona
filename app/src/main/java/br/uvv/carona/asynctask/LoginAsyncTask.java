package br.uvv.carona.asynctask;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

import br.uvv.carona.model.Student;
import br.uvv.carona.model.StudentInfo;
import br.uvv.carona.util.EventBusEvents;

/**
 * Created by CB1772 on 12/05/2016.
 */
public class LoginAsyncTask extends BaseAsyncTask<Student, String> {

    @Override
    protected String doInBackground(Student... params) {
        try{
            //TODO WEBSERVER CALL
            StudentInfo studentInfo = new StudentInfo();
            studentInfo.expirationToken = new Date();
            studentInfo.expirationToken.setTime(studentInfo.expirationToken.getTime()+72000000);
            studentInfo.student = params[0];
            studentInfo.token = "asdasdas4a5sdas4dAsf";
            return "asdasdas4a5sdas4dAsf";
        }catch (Exception e){
            this.mException = e;
        }
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(String studentInfo) {
        boolean success = studentInfo != null && this.mException == null;
        if(success){
            EventBus.getDefault().post(new EventBusEvents.LoginEvent(studentInfo));
        }
        super.onPostExecute(studentInfo);
    }
}
