package br.uvv.carona.activity;

import android.content.Intent;
import android.os.Bundle;

import br.uvv.carona.R;

public class SplashScreenActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
