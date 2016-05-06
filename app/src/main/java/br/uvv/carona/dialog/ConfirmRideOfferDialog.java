package br.uvv.carona.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.uvv.carona.R;
import br.uvv.carona.activity.BaseActivity;
import br.uvv.carona.model.RouteRide;

/**
 * Created by CB1772 on 02/05/2016.
 */
public class ConfirmRideOfferDialog extends DialogFragment implements SeekBar.OnSeekBarChangeListener {
    private static final String ROUTE_TAG = ".ROUTE_TAG";

    private Dialog mDialog;

    private TextView mDepartureField;
    private TextView mDestinationField;
    private TextView mDateField;
    private TextView mTimeField;
    private SeekBar mNumberPassagers;
    private TextView mNumberPassagersCounter;

    private RouteRide mRoute;

    /**
     * <p>Cria uma nova instância do dialogo de confirmação da oferta de carona.</p>
     * @param route possui os dados coletados para a carona, como os pontos de saída e destino, e a rota descrita no mapa.
     * @return
     */
    public static ConfirmRideOfferDialog newInstance(RouteRide route){
        Bundle args = new Bundle();
        args.putSerializable(ROUTE_TAG, route);
        ConfirmRideOfferDialog fragment = new ConfirmRideOfferDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        this.mDialog = new Dialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        this.mDialog.setContentView(R.layout.dialog_confirm_ride_offer);
        this.mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));

        this.mDepartureField = (TextView)this.mDialog.findViewById(R.id.rideDepartureAddress);
        this.mDestinationField = (TextView)this.mDialog.findViewById(R.id.rideDestinationAddress);
        this.mDateField = (TextView)this.mDialog.findViewById(R.id.rideDate);
        this.mTimeField = (TextView)this.mDialog.findViewById(R.id.rideHour);
        this.mNumberPassagersCounter = (TextView)this.mDialog.findViewById(R.id.numberPassagersCounter);
        this.mNumberPassagers = (SeekBar)this.mDialog.findViewById(R.id.numberPassagersSB);

        this.mDialog.findViewById(R.id.cancel_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmRideOfferDialog.this.mDialog.dismiss();
            }
        });
        this.mDialog.findViewById(R.id.confirm_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Start server call
            }
        });

        this.mDateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = ConfirmRideOfferDialog.this.mRoute.validationCalendar;
                Calendar current = Calendar.getInstance();
                if(calendar.getTimeInMillis() < current.getTimeInMillis()){
                    ConfirmRideOfferDialog.this.mRoute.validationCalendar.setTimeInMillis(current.getTimeInMillis());
                }
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar current = Calendar.getInstance();
                        current.set(Calendar.SECOND, 0);
                        current.set(Calendar.MILLISECOND, 0);
                        Calendar newCalendar = Calendar.getInstance();
                        newCalendar.set(Calendar.YEAR, year);
                        newCalendar.set(Calendar.MONTH, monthOfYear);
                        newCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        newCalendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
                        newCalendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
                        newCalendar.set(Calendar.SECOND, 0);
                        newCalendar.set(Calendar.MILLISECOND, 0);
                        if(current.getTimeInMillis() - newCalendar.getTimeInMillis() > 0){
                            ConfirmRideOfferDialog.this.mRoute.validationCalendar.setTimeInMillis(current.getTimeInMillis());
                        }else{
                            ConfirmRideOfferDialog.this.mRoute.validationCalendar.setTimeInMillis(newCalendar.getTimeInMillis());
                        }
                        ConfirmRideOfferDialog.this.setTime(ConfirmRideOfferDialog.this.mRoute.validationCalendar);
                    }
                }, year, month, day);
                current.set(Calendar.HOUR_OF_DAY, 0);
                current.set(Calendar.MINUTE, 0);
                current.set(Calendar.SECOND, 0);
                current.set(Calendar.MILLISECOND, 0);
                int time = current.get(Calendar.HOUR_OF_DAY) * current.get(Calendar.MINUTE) * current.get(Calendar.SECOND) * 1000;
                datePickerDialog.getDatePicker().setMinDate(current.getTimeInMillis() - time);
                datePickerDialog.getDatePicker().setMaxDate(current.getTimeInMillis() + 172800000);
                datePickerDialog.show();
            }
        });

        this.mTimeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = ConfirmRideOfferDialog.this.mRoute.validationCalendar;
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minu = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar current = Calendar.getInstance();
                        current.set(Calendar.SECOND, 0);
                        current.set(Calendar.MILLISECOND, 0);
                        Calendar newCalendar = Calendar.getInstance();
                        newCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
                        newCalendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
                        newCalendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
                        newCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        newCalendar.set(Calendar.MINUTE, minute);
                        newCalendar.set(Calendar.SECOND, 0);
                        newCalendar.set(Calendar.MILLISECOND, 0);
                        if(current.getTimeInMillis() - newCalendar.getTimeInMillis() > 0){
                            Toast.makeText(view.getContext(), "Hora inválida", Toast.LENGTH_LONG).show();
                            ConfirmRideOfferDialog.this.mRoute.validationCalendar.set(Calendar.HOUR_OF_DAY, current.get(Calendar.HOUR_OF_DAY));
                            ConfirmRideOfferDialog.this.mRoute.validationCalendar.set(Calendar.MINUTE, current.get(Calendar.MINUTE) + 1);
                        }else {
                            ConfirmRideOfferDialog.this.mRoute.validationCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            ConfirmRideOfferDialog.this.mRoute.validationCalendar.set(Calendar.MINUTE, minute);
                        }
                        ConfirmRideOfferDialog.this.setTime(ConfirmRideOfferDialog.this.mRoute.validationCalendar);
                    }
                }, hour, minu, true);

                timePickerDialog.show();
            }
        });

        return this.mDialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState == null){
            this.mRoute = (RouteRide)getArguments().getSerializable(ROUTE_TAG);
        }else{
            this.mRoute = (RouteRide)savedInstanceState.getSerializable(ROUTE_TAG);
        }

        View scrollView = this.mDialog.findViewById(R.id.content_scroll);
        ViewGroup.LayoutParams lp = scrollView.getLayoutParams();
        lp.height = (int)(((BaseActivity)getActivity()).getSizeDevice().heightPixels * 0.65);
        scrollView.setLayoutParams(lp);

        setRouteValuesOnScreen();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ROUTE_TAG, this.mRoute);
    }

    /**
     * Preenche os campos do dialogo com as informações contidas em mRoute e seta como data default a data e hora atual
     */
    private void setRouteValuesOnScreen(){
        this.mDepartureField.setText(this.mRoute.startAddress);
        this.mDestinationField.setText(this.mRoute.endAddress);

        if(this.mRoute.validationCalendar == null){
            this.mRoute.validationCalendar = Calendar.getInstance();
        }
        setTime(this.mRoute.validationCalendar);
    }

    private void setTime(Calendar calendar){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        this.mDateField.setText(dateFormat.format(calendar.getTime()));
        this.mTimeField.setText(timeFormat.format(calendar.getTime()));

        this.mNumberPassagers.setOnSeekBarChangeListener(this);
        this.mNumberPassagers.setProgress(3);
    }

    @Override
    public void onResume() {
        super.onResume();
        changeDialogDimen();
    }

    /**
     * Método utilizado apenas para alterar as dimenções da janela que envolve o dialogo.
     */
    protected void changeDialogDimen() {
        BaseActivity activity = (BaseActivity)getActivity();
        int width = activity.getSizeDevice().widthPixels - (activity.dpToPx(20) * 2);
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        this.mDialog.getWindow().setLayout(width, height);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        this.mNumberPassagersCounter.setText(Integer.toString(progress+1));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
