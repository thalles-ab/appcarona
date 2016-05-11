package br.uvv.carona.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import br.uvv.carona.R;
import br.uvv.carona.application.AppPartiUVV;
import br.uvv.carona.dialog.MessageDialog;
import br.uvv.carona.util.EventBusEvents;

public abstract class BaseActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION_CODE = 10;
    private ProgressDialog mProgressDialog;

    protected DialogFragment mErrorDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String[] request = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (!AppPartiUVV.hasInternetConnection()) {
            this.mErrorDialog = MessageDialog.newInstance(getString(R.string.msg_turn_on_internet_connection), new MessageDialog.OnDialogButtonClick() {
                @Override
                public void onConfirmClick(Dialog dialog) {
                    dialog.dismiss();
                    if(android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1){
                        ActivityCompat.finishAffinity(dialog.getOwnerActivity());
                    }else {
                        dialog.getOwnerActivity().finishAffinity();
                    }
                }
            });
            this.mErrorDialog.show(getSupportFragmentManager(), "ERROR_NO_CONNECTION");
        } else if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.M &&
                !AppPartiUVV.hasPermission(request)) {
            ActivityCompat.requestPermissions(this, request, REQUEST_LOCATION_CODE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_LOCATION_CODE) {
            boolean temp = true;
            for (int i = 0; i < grantResults.length; i++) {
                temp = temp && grantResults[i] == PackageManager.PERMISSION_GRANTED;
            }
            if (temp){
                if(this instanceof MapActivity){
                    ((MapActivity)this).enableGoToUserLocation(true);
                }
            }else{
                this.mErrorDialog = MessageDialog.newInstance(getString(R.string.msg_allow_location_request), new MessageDialog.OnDialogButtonClick() {
                    @Override
                    public void onConfirmClick(Dialog dialog) {
                        dialog.dismiss();
                        //TODO CLOSE APP
                    }
                });
                this.mErrorDialog.show(getSupportFragmentManager(), "ERROR_NO_PERMISSION");
            }
        }
    }

    public void startProgressDialog(@StringRes int idString){
        if(this.mProgressDialog == null){
            this.mProgressDialog = new ProgressDialog(this);
            this.mProgressDialog.setCancelable(false);
        }
        this.mProgressDialog.setMessage(getString(idString));
        this.mProgressDialog.show();
    }

    public boolean isProgressDialogShowing(){
        return this.mProgressDialog != null && this.mProgressDialog.isShowing();
    }

    protected void stopProgressDialog(){
        if(this.isProgressDialogShowing()){
            this.mProgressDialog.dismiss();
        }
    }

//    abstract void treatError(EventBusEvents.ErrorEvent event);
    protected void treatCommonErrors(EventBusEvents.ErrorEvent event){
        this.mProgressDialog.dismiss();
    }

    @Subscribe
    public void getErrorEvent(EventBusEvents.ErrorEvent event){
        treatCommonErrors(event);
    }

    protected void openGallery(int code){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, code);
    }

    public DisplayMetrics getSizeDevice() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics;
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    //Retorna 'true' caso contenha um EditText sem nenhum dado e marca os mesmos com um erro.
    protected boolean checkEditTextEmpty(@NonNull EditText field){
        List<EditText> fields = new ArrayList<>();
        fields.add(field);
        return checkEditTextEmpty(fields);
    }
    protected boolean checkEditTextEmpty(@NonNull List<EditText> fields){
        if(fields.size() == 0){
            Log.e("CheckEditText", "Must have at least one edittext field");
            return true;
        }else{
            boolean answer = false;
            for (int i = 0; i < fields.size(); i++){
                if(TextUtils.isEmpty(fields.get(i).getText())){
                    answer = true;
                    fields.get(i).setError(getString(R.string.error_fill_field));
                }
            }
            return answer;
        }
    }
}
