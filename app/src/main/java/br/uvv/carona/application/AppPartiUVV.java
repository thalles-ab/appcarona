package br.uvv.carona.application;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;

import java.util.Calendar;

public class AppPartiUVV extends Application {
    public static Application mApplication;
    public static final Gson sGson = new Gson();

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        mApplication = this;
    }

    public static void saveToken(String token){

    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean hasPermission(String... permissions){
        boolean hasPermission = true;
        for(String permission : permissions){
            hasPermission = hasPermission &&
                    ContextCompat.checkSelfPermission(mApplication, permission) == PackageManager.PERMISSION_GRANTED;
        }
        return hasPermission;
    }

    public static String getStringText(@StringRes int id){
        return mApplication.getString(id);
    }

    public static boolean hasInternetConnection(){
        ConnectivityManager cManager = (ConnectivityManager) mApplication.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
