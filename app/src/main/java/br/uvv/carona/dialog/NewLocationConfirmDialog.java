package br.uvv.carona.dialog;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import br.uvv.carona.R;
import br.uvv.carona.activity.MapActivity;
import br.uvv.carona.asynctask.SavePlaceAsyncTask;
import br.uvv.carona.model.Place;
import br.uvv.carona.util.EventBusEvents;

public class NewLocationConfirmDialog extends DialogFragment implements View.OnClickListener{
    private static final String PLACE_TAG = ".PLACE_TAG";

    private Place mPlace;

    private Dialog mDialog;
    private EditText mAddressField;
    private EditText mDescriptionField;

    public static NewLocationConfirmDialog newInstance(Place place) {
        Bundle args = new Bundle();
        args.putSerializable(PLACE_TAG, place);
        NewLocationConfirmDialog fragment = new NewLocationConfirmDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null){
            this.mPlace = (Place)getArguments().getSerializable(PLACE_TAG);
        }else{
            this.mPlace = (Place)savedInstanceState.getSerializable(PLACE_TAG);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.mDialog = new Dialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        this.mDialog.setContentView(R.layout.dialog_confirm_new_place);
        this.mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));

        this.mAddressField = (EditText)this.mDialog.findViewById(R.id.place_address);
        this.mDescriptionField = (EditText)this.mDialog.findViewById(R.id.place_description);

        this.mDialog.findViewById(R.id.cancel_action).setOnClickListener(this);
        this.mDialog.findViewById(R.id.confirm_action).setOnClickListener(this);

        return this.mDialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(this.mPlace != null) {
            String address = this.mPlace.description;
            this.mAddressField.setText(address);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PLACE_TAG, this.mPlace);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.cancel_action){
            this.mDialog.dismiss();
        }else{
            String description = this.mDescriptionField.getText().toString();
            if(TextUtils.isEmpty(description)){
                mDescriptionField.setError(getString(R.string.error_fill_field));
            }else {
                this.mPlace.description = description;
                ((MapActivity) this.mDialog.getOwnerActivity()).startProgressDialog(R.string.msg_saving_place);
                new SavePlaceAsyncTask().execute(this.mPlace);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onSuccessEvent(EventBusEvents.SuccessEvent event){
        if(event.success){
            ((MapActivity) this.mDialog.getOwnerActivity()).sendResult(this.mPlace);
            this.mDialog.dismiss();
        }
    }
}
