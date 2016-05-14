package br.uvv.carona.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import br.uvv.carona.R;
import br.uvv.carona.asynctask.CreateUserAsyncTask;
import br.uvv.carona.util.EventBusEvents;

public class SignUpActivity extends BaseActivity {

    private EditText mUserName;
    private EditText mUserRegistration;
    private EditText mUserPhone;
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
        mUserPhone= (EditText)this.findViewById(R.id.fieldUserPhone);
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
            if(isPasswordValid()){
                //TODO chamar async
            }
        }
    }

    private boolean isPasswordValid(){
        boolean valid = true;
        if(mUserPassword.getText().length() < 6){
            mUserPassword.setError(getString(R.string.error_password_less_than_six));
            valid = false;
        }else if(!mUserPassword.getText().toString().equals(mUserPasswordConfirmation.getText().toString())){
            mUserPassword.setError(getString(R.string.error_password_not_equal));
            mUserPasswordConfirmation.setError(getString(R.string.error_password_not_equal));
            valid = false;
        }
        return valid;
    }
}
