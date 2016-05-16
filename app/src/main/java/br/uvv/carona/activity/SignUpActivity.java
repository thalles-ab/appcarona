package br.uvv.carona.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import br.uvv.carona.R;
import br.uvv.carona.application.AppPartiUVV;
import br.uvv.carona.asynctask.CreateUserAsyncTask;
import br.uvv.carona.asynctask.LoginAsyncTask;
import br.uvv.carona.model.Student;
import br.uvv.carona.util.EventBusEvents;
import br.uvv.carona.util.Md5Generator;
import br.uvv.carona.view.PhoneEditText;

public class SignUpActivity extends BaseActivity {
    private static final String EXTRA_STUDENT = "EXTRA_STUDENT";
    private EditText mUserName;
    private EditText mUserRegistration;
    private PhoneEditText mUserPhone;
    private EditText mUserEmail;
    private EditText mUserPassword;
    private EditText mUserPasswordConfirmation;
    private List<EditText> mFields;

    private Student mStudent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mUserName = (EditText)this.findViewById(R.id.fieldUserName);
        mUserRegistration = (EditText)this.findViewById(R.id.fieldUserRegistration);
        mUserPhone= (PhoneEditText)this.findViewById(R.id.fieldUserPhone);
        mUserEmail = (EditText)this.findViewById(R.id.fieldUserEmail);
        mUserPassword = (EditText)this.findViewById(R.id.fieldUserPassword);
        mUserPasswordConfirmation = (EditText)this.findViewById(R.id.fieldUserConfirmPassword);

        mFields = new ArrayList<>();
        mFields.add(mUserName);
        mFields.add(mUserRegistration);
        mFields.add(mUserEmail);
        mFields.add(mUserPhone);
        mFields.add(mUserPassword);
        mFields.add(mUserPasswordConfirmation);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.lbl_register);

        if(savedInstanceState == null) {
            mStudent = new Student();
        }else{
            mStudent = (Student) savedInstanceState.getSerializable(EXTRA_STUDENT);
        }
    }

    @Subscribe
    public void onSuccessEvent(EventBusEvents.SuccessEvent event){
        new LoginAsyncTask().execute(mStudent);
    }

    @Subscribe
    public void onLoginEvent(EventBusEvents.LoginEvent event){
        AppPartiUVV.saveToken(event.token);
        stopProgressDialog();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    /**
     * <p>
     * Faz todas as verificações de preenchimento dos campos obrigatórios
     * </p>
     * <p>
     * Caso os campos obrigatórios tenham sido preenchidos, será enviado uma chamada
     * à API para realizar a tentativa de cadastro.
     * </p>
     * @param view {@link View} no qual foi atribuído esta função no atributo {@link R.attr}
     */
    public void onClickSignUp(View view){
        if(!checkEditTextEmpty(mFields)){
            if(isUserFormValid()){
                startProgressDialog(R.string.lbl_signing_up);
                fillStudent();
                new CreateUserAsyncTask().execute(mStudent);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(EXTRA_STUDENT, mStudent);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mStudent = (Student) savedInstanceState.getSerializable(EXTRA_STUDENT);
    }

    private void fillStudent(){
        mStudent = new Student();
        mStudent.name = mUserName.getText().toString().trim();
        mStudent.code = mUserRegistration.getText().toString().trim();
        mStudent.email = mUserEmail.getText().toString().trim();
        mStudent.cellPhone = mUserPhone.getCleanText();
        mStudent.password = Md5Generator.formatMD5(mUserPassword.getText().toString().trim());
    }

    private boolean isUserFormValid(){
        boolean valid = true;
        if(mUserPhone.getCleanText().length() < getResources().getInteger(R.integer.phone_min_length)){
            mUserPhone.setError(getString(R.string.error_invalid_phone));
            valid = false;
        }

        if(mUserRegistration.getText().toString().trim().length() < getResources().getInteger(R.integer.input_register_min_length)){
            mUserRegistration.setError(getString(R.string.error_invalid_registration));
            valid = false;
        }

        if(mUserPassword.getText().length() < getResources().getInteger(R.integer.password_min_length)){
            mUserPassword.setError(getString(R.string.error_password_less_than_six));
            valid = false;
        }else if(!mUserPassword.getText().toString().equals(mUserPasswordConfirmation.getText().toString())){
            mUserPassword.setError(getString(R.string.error_password_not_equal));
            mUserPasswordConfirmation.setError(getString(R.string.error_password_not_equal));
            valid = false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(mUserEmail.getText().toString().trim()).matches()){
            mUserEmail.setError(getString(R.string.error_invalid_email));
            valid = false;
        }

        return valid;
    }
}
