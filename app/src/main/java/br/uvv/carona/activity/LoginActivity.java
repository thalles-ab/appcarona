package br.uvv.carona.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import br.uvv.carona.R;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setLoginWrapperSize();
    }

    private void setLoginWrapperSize(){
        RelativeLayout wrapper = (RelativeLayout)findViewById(R.id.loginWrapper);
        ViewGroup.LayoutParams layoutParams = wrapper.getLayoutParams();
        layoutParams.height = (int)(this.getWindowManager().getDefaultDisplay().getHeight() * 0.85);
        layoutParams.width = (int)(this.getWindowManager().getDefaultDisplay().getWidth() * 0.85);
    }

    public void onClickLogin(View view){
        EditText loginField = (EditText)findViewById(R.id.loginField);
        EditText passwordField = (EditText)findViewById(R.id.passwordField);
        boolean nothingIsWrong = true;
        if(TextUtils.isEmpty(loginField.getText())){
            loginField.setError(getString(R.string.error_empty_field));
            nothingIsWrong = false;
        }
        if(TextUtils.isEmpty(passwordField.getText())){
            passwordField.setError(getString(R.string.error_empty_field));
            nothingIsWrong = false;
        }

        if(nothingIsWrong){
            //TODO login event

            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }

    public void onClickSignUp(View view){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}