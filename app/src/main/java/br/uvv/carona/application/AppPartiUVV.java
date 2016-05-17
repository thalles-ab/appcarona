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
import android.util.Log;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.uvv.carona.model.Place;
import br.uvv.carona.model.Ride;
import br.uvv.carona.model.RideSolicitation;
import br.uvv.carona.model.Student;
import br.uvv.carona.util.RideSolicitationStatus;

public class AppPartiUVV extends Application {
    private static final String PERSIST_FILENAME = "persist.partiuvv.info";
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZZZ";
    private static final String TOKEN_KEY = "TOKEN_KEY";
    public static Application mApplication;
    public static Gson sGson;
    private static Student mStudent;

    public static List<Ride> simuRide;
    public static List<RideSolicitation> simuSolicitation;
    public static List<RideSolicitation> simuSolicitationMade = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        sGson = new GsonBuilder().setLongSerializationPolicy(LongSerializationPolicy.STRING).setDateFormat(DATE_FORMAT).create();
        Fresco.initialize(this);
        mApplication = this;
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(mApplication)
                .setDownsampleEnabled(true)
                .setMainDiskCacheConfig(getDefaultMainDiskCacheConfig(mApplication))
                .build();
        Fresco.initialize(mApplication, config);
        if (mStudent == null) {
            mStudent = readUser();
        }


    }

    public static void addRide(double latS, double latE, double lngS, double lngE, String gRoute, int max, Date date){
        Ride ride = new Ride();
        ride.student = readUser();
        ride.routeGoogleFormat = gRoute;
        ride.quantityPassengers = max;
        Place start = new Place();
        start.latitude = latS;
        start.longitude = lngS;
        Place end = new Place();
        end.latitude = latE;
        end.longitude = lngE;
        ride.endPoint = end;
        ride.startPoint = start;
        ride.students = new ArrayList<>();
        ride.expirationDate = date;
        ride.id = simuRide.size()+1;
        simuRide.add(ride);
    }

    public static void addRideSolicitation(String name, String photoUrl, Ride ride){
        RideSolicitation solicitation = new RideSolicitation();
        solicitation.id = simuSolicitation.size()+1;
        solicitation.ride = ride;
        Student student = new Student();
        student.name = name;
        student.photo = photoUrl;
        solicitation.student = student;
        simuSolicitation.add(solicitation);
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

    private static synchronized Student readUser() {
        Student object = null;
        try {
            File file = new File(mApplication.getFilesDir(), PERSIST_FILENAME);
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fis);
            object = (Student) in.readObject();
            in.close();
            fis.close();
        } catch (FileNotFoundException e) {
            return null;
        } catch (Exception ex) {
            Log.e(mApplication.getClass().getName(), "Erro ao recuperar as informações persistentes.", ex);
        }
        return object;
    }

    public static synchronized void persistUser(Student object) {
        try {
            File file = new File(mApplication.getFilesDir(), PERSIST_FILENAME);
            FileOutputStream fos = new FileOutputStream(file, false);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(object);
            out.close();
            fos.close();
        } catch (Exception ex) {
            Log.e(mApplication.getClass().getName(), "Erro ao salvar as informações persistentes.", ex);
        }
    }

    private static DiskCacheConfig getDefaultMainDiskCacheConfig(final Context context) {
        return DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryPathSupplier(
                        new Supplier<File>() {
                            @Override
                            public File get() {
                                return context.getApplicationContext().getCacheDir();
                            }
                        })
                .setBaseDirectoryName("image_cache")
                .setMaxCacheSize(40 * ByteConstants.MB)
                .setMaxCacheSizeOnLowDiskSpace(10 * ByteConstants.MB)
                .setMaxCacheSizeOnVeryLowDiskSpace(2 * ByteConstants.MB)
                .build();
    }

    public static Student getStudent(){
        return readUser();
    }
}
