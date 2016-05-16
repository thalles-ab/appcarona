package br.uvv.carona.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.uvv.carona.R;
import br.uvv.carona.model.Error;

public class ErrorDialogFragment extends DialogFragment {

    public static ErrorDialogFragment newInstance(String message) {
        ErrorDialogFragment frag = new ErrorDialogFragment();
        Bundle args = new Bundle();
        args.putString("message", message);
        frag.setArguments(args);
        return frag;
    }

    public static ErrorDialogFragment newInstance(List<Error> erros) {
        String msg = "";
        for (Error item : erros) {
            msg = item.message + "\n";
        }
        return newInstance(msg);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String msg = getArguments().getString("message");
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.lbl_information)
                .setMessage(msg)
                .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getDialog().dismiss();
                    }
                }).create();
    }
}