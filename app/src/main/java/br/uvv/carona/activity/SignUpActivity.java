package br.uvv.carona.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import br.uvv.carona.R;
import br.uvv.carona.util.EventBusEvents;

public class SignUpActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

        //TODO invalidar o textinputlayout e não o edittext
        TextInputLayout nameInput = (TextInputLayout)this.findViewById(R.id.input_layout_name);
        TextInputLayout registrationInput = (TextInputLayout)this.findViewById(R.id.input_layout_matricula);
        TextInputLayout phoneInput = (TextInputLayout)this.findViewById(R.id.input_layout_phone);
        TextInputLayout passwordInput = (TextInputLayout)this.findViewById(R.id.input_layout_password);
        TextInputLayout confirmPasswordInput = (TextInputLayout)this.findViewById(R.id.input_layout_confirmation);

        EditText name = (EditText)this.findViewById(R.id.fieldUserName);
        EditText registration = (EditText)this.findViewById(R.id.fieldUserRegistration);
        EditText password = (EditText)this.findViewById(R.id.fieldUserPassword);
        EditText phone = (EditText)this.findViewById(R.id.fieldUserPhone);
        EditText confirmPassword = (EditText)this.findViewById(R.id.fieldUserConfirmPassword);

        List<EditText> fields = new ArrayList<>();
        fields.add(name);
        fields.add(registration);
        fields.add(password);
        fields.add(confirmPassword);
        
        if(!checkEditTextEmpty(fields)){
            String p = password.getText().toString();
            String cp = confirmPassword.getText().toString();
            if(!p.equals(cp)){
                password.setError(getString(R.string.error_password_not_equal));
                confirmPassword.setError(getString(R.string.error_password_not_equal));
            }else{

            }
        }
    }
}
