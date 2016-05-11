package br.uvv.carona.activity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import br.uvv.carona.R;

public class SplashScreenActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(this.mErrorDialog == null || !this.mErrorDialog.getShowsDialog()){
            Timer task = new Timer();
            task.schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            },2000);
        }
    }
}
