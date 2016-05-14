package br.uvv.carona.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by geen-20 on 14/05/2016.
 */
public class BaseTextWatcher implements TextWatcher {
    private EditText mEditText;

    public BaseTextWatcher(EditText editText){
        this.mEditText =  editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mEditText.setError(null);
    }
}
