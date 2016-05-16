package br.uvv.carona.asynctask;

import org.greenrobot.eventbus.EventBus;

import br.uvv.carona.model.Student;
import br.uvv.carona.model.StudentInfo;
import br.uvv.carona.service.LoginService;
import br.uvv.carona.service.StudentService;
import br.uvv.carona.util.EventBusEvents;

public class LoginAsyncTask extends BaseAsyncTask<Student, StudentInfo> {

    @Override
    protected StudentInfo doInBackground(Student... params) {
        try{
            StudentInfo student = LoginService.login(params[0]);
            return student;
        }catch (Exception e){
            this.mException = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(StudentInfo studentInfo) {
        boolean success = studentInfo != null && this.mException == null && studentInfo.erros == null;
        if(success){
            EventBus.getDefault().post(new EventBusEvents.LoginEvent(studentInfo.token));
        }
        super.onPostExecute(studentInfo);
    }
}
