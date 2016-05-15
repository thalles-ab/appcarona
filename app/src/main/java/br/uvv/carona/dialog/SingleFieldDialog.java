package br.uvv.carona.dialog;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;

import br.uvv.carona.R;
import br.uvv.carona.activity.BaseActivity;

public class SingleFieldDialog extends DialogFragment {
    private static final String TITLE_TAG = ".TITLE_TAG";
    private static final String HINT_TAG = ".HINT_TAG";
    private static final String INPUT_TYPE = ".INPUT_TYPE";
    private static final String START_TEXT_TAG = ".START_TEXT_TAG";
    private static final String ON_DIALOG_BTN_CLICK = ".ON_DIALOG_BTN_CLICK";

    private OnDialogButtonClick mOnClick;

    private Dialog mDialog;
    private TextInputEditText mField;
    private TextView mTitleField;

    public static SingleFieldDialog newInstance(String title,String hint, String startText, int inputType, OnDialogButtonClick onClick) {
        Bundle args = new Bundle();
        args.putString(TITLE_TAG, title);
        args.putString(HINT_TAG, hint);
        args.putString(START_TEXT_TAG, startText);
        args.putSerializable(ON_DIALOG_BTN_CLICK, onClick);
        args.putInt(INPUT_TYPE, inputType);
        SingleFieldDialog fragment = new SingleFieldDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        this.mDialog = new Dialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        this.mDialog.setContentView(R.layout.dialog_single_field);
        this.mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        this.mField = (TextInputEditText)this.mDialog.findViewById(R.id.dialog_field);
        this.mTitleField = (TextView)this.mDialog.findViewById(R.id.title);
        this.mDialog.setCancelable(false);
        return this.mDialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        changeDialogDimen();

        String title;
        String hint;
        int inputType;
        if(savedInstanceState == null){
            this.mOnClick = (OnDialogButtonClick)getArguments().getSerializable(ON_DIALOG_BTN_CLICK);
            hint = getArguments().getString(HINT_TAG);
            inputType = getArguments().getInt(INPUT_TYPE);
            title = getArguments().getString(TITLE_TAG);
            this.mField.setText(getArguments().getString(START_TEXT_TAG));
        }else{
            this.mOnClick = (OnDialogButtonClick)savedInstanceState.getSerializable(ON_DIALOG_BTN_CLICK);
            hint = savedInstanceState.getString(HINT_TAG);
            inputType = savedInstanceState.getInt(INPUT_TYPE);
            title = savedInstanceState.getString(TITLE_TAG);
        }
        this.mTitleField.setText(title);
        this.mField.setHint(hint);
        this.mField.setInputType(inputType);

        this.mDialog.findViewById(R.id.confirm_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClick.onConfirmClick(mDialog, mField);
            }
        });
        this.mDialog.findViewById(R.id.cancel_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TITLE_TAG, this.mTitleField.getText().toString());
        outState.putString(HINT_TAG, this.mField.getHint().toString());
        outState.putInt(INPUT_TYPE, this.mField.getInputType());
        outState.putSerializable(ON_DIALOG_BTN_CLICK, this.mOnClick);
    }

    protected void changeDialogDimen() {
        BaseActivity activity = (BaseActivity)getActivity();
        int width = (int)(activity.getSizeDevice().widthPixels * 0.8);
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        this.mDialog.getWindow().setLayout(width, height);
    }

    public abstract static class OnDialogButtonClick implements Serializable {
        public abstract void onConfirmClick(Dialog dialog, TextInputEditText field);
    }

}
