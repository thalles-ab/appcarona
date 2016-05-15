package br.uvv.carona.asynctask;

import org.greenrobot.eventbus.EventBus;

import br.uvv.carona.model.Student;
import br.uvv.carona.service.StudentService;
import br.uvv.carona.util.EventBusEvents;

public class GetUserInfoAsyncTask extends BaseAsyncTask<Long, Student> {

    @Override
    protected Student doInBackground(Long... params) {
        try {
           return StudentService.getStudent(params[0] != null ? params[0].longValue(): 0);
        }catch (Exception e){
            this.mException = e;
        }
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(Student student) {
        boolean success = student != null && this.mException == null && student.erros == null;
        if(success){
            EventBus.getDefault().post(new EventBusEvents.UserEvent(student));
        }
        super.onPostExecute(student);
    }
}
