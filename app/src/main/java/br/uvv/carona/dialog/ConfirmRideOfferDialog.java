package br.uvv.carona.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.uvv.carona.R;
import br.uvv.carona.activity.BaseActivity;
import br.uvv.carona.activity.CheckRideOffersActivity;
import br.uvv.carona.model.Ride;

/**
 * Created by CB1772 on 02/05/2016.
 */
public class ConfirmRideOfferDialog extends DialogFragment {
    private static final String ROUTE_TAG = ".ROUTE_TAG";
    private static final String IS_RIDE_REQUEST_TAG = ".IS_RIDE_REQUEST_TAG";

    private Dialog mDialog;

    private TextView mDepartureField;
    private TextView mDestinationField;
    private TextView mDateField;
    private TextView mTimeField;
    private Spinner mNumberPassagers;

    private Ride mRoute;
    private boolean mIsRideRequest;

    /**
     * <p>Cria uma nova instância do dialogo de confirmação da oferta de carona.</p>
     * @param route possui os dados coletados para a carona, como os pontos de saída e destino, e a rota descrita no mapa.
     * @return
     */
    public static ConfirmRideOfferDialog newInstance(Ride route, boolean isRideRequest){
        Bundle args = new Bundle();
        args.putSerializable(ROUTE_TAG, route);
        args.putBoolean(IS_RIDE_REQUEST_TAG, isRideRequest);
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
        this.mNumberPassagers = (Spinner)this.mDialog.findViewById(R.id.numberPassagersSB);

        this.mDialog.findViewById(R.id.cancel_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmRideOfferDialog.this.mDialog.dismiss();
            }
        });
        this.mDialog.findViewById(R.id.confirm_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIsRideRequest){
                    Intent intent = new Intent(v.getContext(), CheckRideOffersActivity.class);
                    intent.putExtra(CheckRideOffersActivity.DEPARTURE_PLACE_TAG, mRoute.startPoint);
                    intent.putExtra(CheckRideOffersActivity.DESTINATION_PLACE_TAG, mRoute.endPoint);
                    startActivity(intent);
                }else{
                    //TODO Start server call
                }
            }
        });

        this.mDateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(ConfirmRideOfferDialog.this.mRoute.expirationDate.getTime());
                Calendar current = Calendar.getInstance();
                if (calendar.getTimeInMillis() < current.getTimeInMillis()) {
                    ConfirmRideOfferDialog.this.mRoute.expirationDate.setTime(current.getTimeInMillis());
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
                        if (current.getTimeInMillis() - newCalendar.getTimeInMillis() > 0) {
                            ConfirmRideOfferDialog.this.mRoute.expirationDate.setTime(current.getTimeInMillis());
                        } else {
                            ConfirmRideOfferDialog.this.mRoute.expirationDate.setTime(newCalendar.getTimeInMillis());
                        }
                        ConfirmRideOfferDialog.this.setTime(ConfirmRideOfferDialog.this.mRoute.expirationDate);
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
                final Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(ConfirmRideOfferDialog.this.mRoute.expirationDate.getTime());
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
                        Calendar saveTime = Calendar.getInstance();
                        if (current.getTimeInMillis() - newCalendar.getTimeInMillis() > 0) {
                            //TODO SHOW INVALID
                            saveTime.set(Calendar.HOUR_OF_DAY, current.get(Calendar.HOUR_OF_DAY));
                            saveTime.set(Calendar.MINUTE, current.get(Calendar.MINUTE) + 1);
                        } else {
                            saveTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            saveTime.set(Calendar.MINUTE, minute);
                        }
                        ConfirmRideOfferDialog.this.mRoute.expirationDate.setTime(saveTime.getTimeInMillis());
                        ConfirmRideOfferDialog.this.setTime(ConfirmRideOfferDialog.this.mRoute.expirationDate);
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
            this.mRoute = (Ride)getArguments().getSerializable(ROUTE_TAG);
            this.mIsRideRequest = getArguments().getBoolean(IS_RIDE_REQUEST_TAG);
        }else {
            this.mRoute = (Ride)savedInstanceState.getSerializable(ROUTE_TAG);
        }

        View scrollView = this.mDialog.findViewById(R.id.content_scroll);
        ViewGroup.LayoutParams lp = scrollView.getLayoutParams();
        lp.height = (int)(((BaseActivity)getActivity()).getSizeDevice().heightPixels * 0.65);
        scrollView.setLayoutParams(lp);

        if(this.mIsRideRequest){
            this.mDialog.findViewById(R.id.numberPassagersWrapper).setVisibility(View.GONE);
        }else{
            this.mDialog.findViewById(R.id.numberPassagersWrapper).setVisibility(View.VISIBLE);
        }

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
        this.mDepartureField.setText(this.mRoute.startPoint.description);
        this.mDestinationField.setText(this.mRoute.endPoint.description);

        if(this.mRoute.expirationDate == null){
            this.mRoute.expirationDate = new Date();
        }
        setTime(this.mRoute.expirationDate);
    }

    private void setTime(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        this.mDateField.setText(dateFormat.format(date.getTime()));
        this.mTimeField.setText(timeFormat.format(date.getTime()));
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
}
