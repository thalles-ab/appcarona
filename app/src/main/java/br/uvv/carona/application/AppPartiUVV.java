package br.uvv.carona.application;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
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
    protected static final String TOKEN_SP_TAG = ".TOKEN_SP_TAG";

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        mApplication = this;
    }

    public static void saveToken(String token){
        SharedPreferences prefs = mApplication.getSharedPreferences(
                "br.uvv.carona", Context.MODE_PRIVATE);
        prefs.edit()
                .putString(TOKEN_SP_TAG, token)
                .commit();
    }

    public static String getToken(){
        SharedPreferences prefs = mApplication.getSharedPreferences(
                "br.uvv.carona", Context.MODE_PRIVATE);
        if(prefs != null){
            return prefs.getString(TOKEN_SP_TAG, null);
        }
        return null;
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
