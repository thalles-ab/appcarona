package br.uvv.carona.view;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by geen-20 on 14/05/2016.
 */
public class PhoneEditText extends AppCompatEditText {
    private boolean isUpdating;

    private int positioning[] = { 1, 2, 3, 6, 7, 8, 9, 11, 12, 13, 14, 15 };

    public PhoneEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();

    }

    public PhoneEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();

    }

    public PhoneEditText(Context context) {
        super(context);
        initialize();

    }

    public String getCleanText() {
        String text = PhoneEditText.this.getText().toString();
        text = text.replaceAll("[^0-9]*", "");
        return text.trim();
    }

    private void initialize() {
        final int maxNumberLength = 11;
        this.setKeyListener(keylistenerNumber);

        this.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(TextUtils.isEmpty(getCleanText())){
                    if(hasFocus){
                        PhoneEditText.this.setText("(  )     -     ");
                        PhoneEditText.this.setSelection(1);
                    }else{
                        PhoneEditText.this.setText("");
                    }
                }
            }
        });

        this.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String current = s.toString();

                if(current.length() == 0){
                    return;
                }
                if (isUpdating) {
                    isUpdating = false;
                    return;

                }
                String number = current.replaceAll("[^0-9]*", "");
                if (number.length() > 11)
                    number = number.substring(0, 11);

                int length = number.length();
                String paddedNumber = padNumber(number, maxNumberLength);

                String ddd = paddedNumber.substring(0, 2);
                String part1 = paddedNumber.substring(2, 6);
                String part2 = paddedNumber.substring(6, 11).trim();

                String phone = "(" + ddd + ") " + part1 + "-" + part2;
                isUpdating = true;
                PhoneEditText.this.setText(phone);

                PhoneEditText.this.setSelection(positioning[length]);

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        });
    }

    protected String padNumber(String number, int maxLength) {
        String padded = new String(number);
        for (int i = 0; i < maxLength - number.length(); i++)
            padded += " ";
        return padded;

    }

    private final KeylistenerNumber keylistenerNumber = new KeylistenerNumber();

    private class KeylistenerNumber extends NumberKeyListener {

        public int getInputType() {
            return InputType.TYPE_CLASS_NUMBER
                    | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;

        }

        @Override
        protected char[] getAcceptedChars() {
            return new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                    '9' };

        }
    }
}