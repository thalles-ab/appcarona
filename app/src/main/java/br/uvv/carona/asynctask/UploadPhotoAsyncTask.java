package br.uvv.carona.asynctask;

import org.greenrobot.eventbus.EventBus;

import br.uvv.carona.model.Student;
import br.uvv.carona.model.UploadFile;
import br.uvv.carona.service.UploadService;

/**
 * Created by Nathalia on 15/05/2016.
 */
public class UploadPhotoAsyncTask extends BaseAsyncTask<Student, UploadFile> {

    @Override
    protected UploadFile doInBackground(Student... params) {
        try {
            return UploadService.upload(params[0]);
        } catch (Exception e) {
            this.mException = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(UploadFile studentInfo) {
        boolean success = studentInfo != null && this.mException == null;
        if (success) {
            EventBus.getDefault().post(studentInfo);
        }
        super.onPostExecute(studentInfo);
    }
}