package br.uvv.carona.asynctask;

import org.greenrobot.eventbus.EventBus;

import br.uvv.carona.model.BaseObject;
import br.uvv.carona.model.Student;
import br.uvv.carona.service.StudentService;
import br.uvv.carona.util.EventBusEvents;

/**
 * Created by Nathalia on 15/05/2016.
 */
public class UpdateUserAsyncTask extends BaseAsyncTask<Student, BaseObject> {

    @Override
    protected BaseObject doInBackground(Student... params) {
        try{
            return StudentService.updateStudent(params[0]);
        }catch (Exception e){
            this.mException = e;
        }
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(BaseObject baseObject) {
        boolean success = mException == null && baseObject == null;
        if(success){
            EventBus.getDefault().post(new EventBusEvents.SuccessEvent(true));
        }else{
            super.onPostExecute(baseObject);
        }
    }
}