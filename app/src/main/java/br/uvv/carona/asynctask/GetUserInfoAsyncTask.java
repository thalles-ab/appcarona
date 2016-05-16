package br.uvv.carona.asynctask;

import org.greenrobot.eventbus.EventBus;

import br.uvv.carona.model.Student;
import br.uvv.carona.service.StudentService;
import br.uvv.carona.util.EventBusEvents;

public class GetUserInfoAsyncTask extends BaseAsyncTask<Long, Student> {
    private Long id;
    @Override
    protected Student doInBackground(Long... params) {
        try {
            id = params[0];
           return StudentService.getStudent(id != null ? id: 0);
        }catch (Exception e){
            this.mException = e;
        }
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(Student student) {
        boolean success = student != null && this.mException == null && student.erros == null;
        if(success){
            if(id == null || id  == 0){
                EventBus.getDefault().post(new EventBusEvents.UserEvent(student));
            }else{
                EventBus.getDefault().post(new EventBusEvents.OpenProfileEvent(student));
            }
        }
        super.onPostExecute(student);
    }
}
