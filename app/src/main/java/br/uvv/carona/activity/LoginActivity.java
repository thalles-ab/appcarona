package br.uvv.carona.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import br.uvv.carona.R;
import br.uvv.carona.application.AppPartiUVV;
import br.uvv.carona.asynctask.GetUserInfoAsyncTask;
import br.uvv.carona.asynctask.LoginAsyncTask;
import br.uvv.carona.model.Student;
import br.uvv.carona.util.EventBusEvents;
import br.uvv.carona.util.Md5Generator;

public class LoginActivity extends BaseActivity {
    private EditText mLogin;
    private EditText mPassword;
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
        mLogin = (EditText) findViewById(R.id.input_field_registry);
        mPassword = (EditText) findViewById(R.id.input_field_password);

        List<EditText> fields = new ArrayList<>();
        fields.add(mLogin);
        fields.add(mPassword);

        if(!checkEditTextEmpty(fields)){
            Student student = new Student();
            student.code = mLogin.getText().toString().trim();
            student.password = Md5Generator.formatMD5(mPassword.getText().toString().trim());
            startProgressDialog(R.string.lbl_entering);
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
        stopProgressDialog();
        AppPartiUVV.saveToken(event.token);
        new GetUserInfoAsyncTask().execute(new Long(0));
    }

    @Subscribe
    public void onEventGetUser(EventBusEvents.UserEvent event){
        stopProgressDialog();
        AppPartiUVV.persistUser(event.student);
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}