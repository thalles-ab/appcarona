package br.uvv.carona.asynctask;

import org.greenrobot.eventbus.EventBus;

import br.uvv.carona.model.Statistic;
import br.uvv.carona.service.CommomService;

/**
 * Created by Nathalia on 16/05/2016.
 */
public class StatsAsyncTask  extends BaseAsyncTask<Void, Statistic> {

    @Override
    protected Statistic doInBackground(Void... params) {
        try {
            Statistic student = CommomService.statistics();
            return student;
        } catch (Exception e) {
            this.mException = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Statistic studentInfo) {
        boolean success = studentInfo != null && this.mException == null && studentInfo.erros == null;
        if (success) {
            EventBus.getDefault().postSticky(studentInfo);
        }
        super.onPostExecute(studentInfo);
    }
}