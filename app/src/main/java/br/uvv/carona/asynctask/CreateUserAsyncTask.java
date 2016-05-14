package br.uvv.carona.asynctask;

import br.uvv.carona.model.BaseObject;
import br.uvv.carona.model.Student;
import br.uvv.carona.service.StudentService;

/**
 * Created by geen-20 on 14/05/2016.
 */
public class CreateUserAsyncTask extends BaseAsyncTask<Student, BaseObject> {

    @Override
    protected BaseObject doInBackground(Student... params) {
        try{
            return StudentService.createStudent(params[0]);
        }catch (Exception e){
            this.mException = e;
        }
        return super.doInBackground(params);
    }

    @Override
    protected void onPostExecute(BaseObject studentInfo) {
        if(success(studentInfo)){

        }else{
            super.onPostExecute(studentInfo);
        }
    }
}