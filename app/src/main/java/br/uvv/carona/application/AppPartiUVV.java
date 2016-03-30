package br.uvv.carona.application;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class AppPartiUVV extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
