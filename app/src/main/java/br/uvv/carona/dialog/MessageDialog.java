package br.uvv.carona.dialog;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;

import br.uvv.carona.R;
import br.uvv.carona.activity.BaseActivity;

/**
 * Created by CB1772 on 11/05/2016.
 */
public class MessageDialog extends DialogFragment {
    private static final String MESSAGE_TAG = ".MESSAGE_TAG";
    private static final String ON_DIALOG_BTN_CLICK = ".ON_DIALOG_BTN_CLICK";

    private OnDialogButtonClick mOnClick;

    private Dialog mDialog;

    public static MessageDialog newInstance(String message, OnDialogButtonClick onClick) {
        Bundle args = new Bundle();
        args.putString(MESSAGE_TAG, message);
        args.putSerializable(ON_DIALOG_BTN_CLICK, onClick);
        MessageDialog fragment = new MessageDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        this.mDialog = new Dialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        this.mDialog.setContentView(R.layout.dialog_simple_message);
        this.mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        this.mDialog.setCancelable(false);
        return this.mDialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        changeDialogDimen();

        this.mOnClick = (OnDialogButtonClick)getArguments().getSerializable(ON_DIALOG_BTN_CLICK);
        String message = getArguments().getString(MESSAGE_TAG);
        ((TextView)this.mDialog.findViewById(R.id.message)).setText(message);

        this.mDialog.findViewById(R.id.confirm_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClick.onConfirmClick(mDialog);
            }
        });
    }

    protected void changeDialogDimen() {
        BaseActivity activity = (BaseActivity)getActivity();
        int width = (int)(activity.getSizeDevice().widthPixels * 0.8);
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        this.mDialog.getWindow().setLayout(width, height);
    }

    public abstract static class OnDialogButtonClick implements Serializable{
        public abstract void onConfirmClick(Dialog dialog);
    }
}
