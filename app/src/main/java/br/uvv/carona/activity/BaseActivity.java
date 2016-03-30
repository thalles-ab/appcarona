package br.uvv.carona.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import br.uvv.carona.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
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
