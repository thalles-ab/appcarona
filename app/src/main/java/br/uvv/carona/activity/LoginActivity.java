package br.uvv.carona.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import org.greenrobot.eventbus.Subscribe;

import br.uvv.carona.R;
import br.uvv.carona.application.AppPartiUVV;
import br.uvv.carona.asynctask.GetUserInfoAsyncTask;
import br.uvv.carona.asynctask.LoginAsyncTask;
import br.uvv.carona.model.Student;
import br.uvv.carona.util.EventBusEvents;

public class LoginActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    /**
     * Verifica se todos os campos de login foram preenchidos e inicia
     * a chamada à API para tentar realizar o login
     * @param view
     */
    public void onClickLogin(View view){
        TextInputLayout inputLogin = (TextInputLayout)findViewById(R.id.input_layout_matricula);
        TextInputLayout inputPassword = (TextInputLayout)findViewById(R.id.input_layout_password);
        EditText loginField = (EditText) findViewById(R.id.input_field_registry);
        EditText passwordField = (EditText) findViewById(R.id.input_field_password);
        String login = loginField.getText().toString();
        String password = passwordField.getText().toString();
        boolean nothingIsWrong = true;
        if(TextUtils.isEmpty(login)){
            inputLogin.setError(getString(R.string.error_empty_field));
            nothingIsWrong = false;
        }else{
            inputLogin.setErrorEnabled(false);
        }
        if(TextUtils.isEmpty(password)){
            inputPassword.setError(getString(R.string.error_empty_field));
            nothingIsWrong = false;
        }else{
            inputPassword.setErrorEnabled(false);
        }

        if(nothingIsWrong){
            Student student = new Student();
            student.code = login;
            student.password = password;
            new LoginAsyncTask().execute(student);
        }
    }

    public void onClickForgotPassword(View view){
        //TODO
    }

    /**
     * Redireciona o usuário para a tela de cadastro
     * @param view {@link View} com este {@link android.view.View.OnClickListener} atribuído
     */
    public void onClickSignUp(View view){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    @Subscribe
    public void onLoginResult(EventBusEvents.LoginEvent event){
        AppPartiUVV.saveToken(event.token);
        Intent intent = new Intent(this, HomeActivity.class);
        this.stopProgressDialog();
        startActivity(intent);
    }

    @Subscribe
    @Override
    public void onErrorEvent(EventBusEvents.ErrorEvent event) {
        this.stopProgressDialog();
        treatCommonErrors(event);
    }

}