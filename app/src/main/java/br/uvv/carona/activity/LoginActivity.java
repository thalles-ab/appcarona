package br.uvv.carona.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import br.uvv.carona.R;

public class LoginActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        setLoginWrapperSize();

        // BY PASS
//        Intent intent = new Intent(this, HomeActivity.class);
//        startActivity(intent);
    }


    /**
     * Verifica se todos os campos de login foram preenchidos e inicia
     * a chamada à API para tentar realizar o login
     * @param view
     */
    public void onClickLogin(View view){
        TextInputLayout inputLogin = (TextInputLayout)findViewById(R.id.input_layout_matricula);
        TextInputLayout inputPassword = (TextInputLayout)findViewById(R.id.input_layout_password);
        EditText loginField = (EditText) findViewById(R.id.loginField);
        EditText passwordField = (EditText) findViewById(R.id.passwordField);
        boolean nothingIsWrong = true;
        if(TextUtils.isEmpty(loginField.getText())){
            inputLogin.setError(getString(R.string.error_empty_field));
            nothingIsWrong = false;
        }else{
            inputLogin.setErrorEnabled(false);
        }
        if(TextUtils.isEmpty(passwordField.getText())){
            inputPassword.setError(getString(R.string.error_empty_field));
            nothingIsWrong = false;
        }else{
            inputPassword.setErrorEnabled(false);
        }

        if(nothingIsWrong){
            //TODO login event

            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }

    public void onClickForgotPassword(View view){

    }

    /**
     * Redireciona o usuário para a tela de cadastro
     * @param view {@link View} com este {@link android.view.View.OnClickListener} atribuído
     */
    public void onClickSignUp(View view){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}