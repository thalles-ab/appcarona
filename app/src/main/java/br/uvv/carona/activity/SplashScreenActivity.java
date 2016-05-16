package br.uvv.carona.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import br.uvv.carona.R;
import br.uvv.carona.application.AppPartiUVV;
import br.uvv.carona.asynctask.AutoLoginAsyncTask;
import br.uvv.carona.asynctask.GetUserInfoAsyncTask;
import br.uvv.carona.model.Error;
import br.uvv.carona.util.EventBusEvents;

public class SplashScreenActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        String token = AppPartiUVV.getToken();

        if(TextUtils.isEmpty(token)){
            Timer task = new Timer();
            task.schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            },1500);
        }else{
            new AutoLoginAsyncTask().execute(token);
        }
    }

    @Subscribe
    public void onErrorEvent(EventBusEvents.ErrorEvent event) {
        onError();
    }

    @Subscribe
    public void onEventErrors(List<Error> erros) {
        onError();
    }

    private void onError(){
        AppPartiUVV.persistUser(null);
        AppPartiUVV.saveToken(null);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Subscribe
    public void onEventGetUser(EventBusEvents.UserEvent event){
        AppPartiUVV.persistUser(event.student);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        stopProgressDialog();
    }

    @Subscribe
    public void onSuccessEvent(EventBusEvents.SuccessEvent event){
        new GetUserInfoAsyncTask().execute(new Long(0));
    }

}
