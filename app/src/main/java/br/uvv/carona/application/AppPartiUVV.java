package br.uvv.carona.application;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;

import java.util.Calendar;

public class AppPartiUVV extends Application {
    private static final String TOKEN_KEY = "TOKEN_KEY";
    public static Application mApplication;
    //TODO se for usar data definir format
    public static final Gson sGson = new Gson();

    @Override
    public void onCreate() {
        super.onCreate();
        //TODO mdoificar config do fresco para aumentar cache
        Fresco.initialize(this);
        mApplication = this;
    }

    public static void saveToken(String token){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mApplication);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TOKEN_KEY,token);
        editor.apply();
    }

    public static String getToken(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mApplication);
        return preferences.getString(TOKEN_KEY, null);
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
