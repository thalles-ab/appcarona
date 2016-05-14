package br.uvv.carona.activity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import br.uvv.carona.R;
import br.uvv.carona.asynctask.CreateUserAsyncTask;
import br.uvv.carona.model.Student;
import br.uvv.carona.util.EventBusEvents;
import br.uvv.carona.view.PhoneEditText;

public class SignUpActivity extends BaseActivity {

    private EditText mUserName;
    private EditText mUserRegistration;
    private PhoneEditText mUserPhone;
    private EditText mUserEmail;
    private EditText mUserPassword;
    private EditText mUserPasswordConfirmation;
    private List<EditText> mFields;

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
    }


    @Subscribe
    @Override
    public void onErrorEvent(EventBusEvents.ErrorEvent event) {

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
                new CreateUserAsyncTask().execute(getStudent());
            }
        }
    }
    @Subscribe
    public void onError(EventBusEvents.ErrorEvent event){
        stopProgressDialog();

    }

    private Student getStudent(){
        Student student = new Student();
        student.name = mUserName.getText().toString().trim();
        student.code = mUserRegistration.getText().toString().trim();
        student.email = mUserEmail.getText().toString().trim();
        student.cellPhone = mUserPhone.getCleanText();
        student.password = mUserPassword.getText().toString().trim();
        return student;
    }

    private boolean isUserFormValid(){
        boolean valid = true;
        if(mUserPhone.getCleanText().length() < 8){
            mUserPhone.setError(getString(R.string.error_invalid_phone));
            valid = false;
        }

        if(mUserPassword.getText().length() < 6){
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
