package br.uvv.carona.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Calendar;
import java.util.List;

import br.uvv.carona.R;
import br.uvv.carona.activity.RideDetailActivity;
import br.uvv.carona.fragment.RideStatusFragment;
import br.uvv.carona.model.Ride;
import br.uvv.carona.model.RideSolicitation;
import br.uvv.carona.model.Student;
import br.uvv.carona.model.enums.TypeSituation;
import br.uvv.carona.util.DateFormatUtil;

public class RideStatusAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<RideSolicitation> mSolicitations;
    private List<Ride> mRides;
    private Context mContext;
    private int mTypeSolicitation;

    public RideStatusAdapter(List<RideSolicitation> offers, Context context, int typeSolicitation){
        this.mSolicitations = offers;
        this.mContext = context;
        this.mTypeSolicitation = typeSolicitation;
    }

    public RideStatusAdapter(List<Ride> offers, Context context){
        this.mRides = offers;
        this.mContext = context;
        this.mTypeSolicitation = RideStatusFragment.TYPE_ACTIVE_RIDE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ride_solicitation_status, null);
        RideOfferViewHolder viewHolder = new RideOfferViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RideOfferViewHolder viewHolder = (RideOfferViewHolder)holder;
        final Ride ride;
        Student student;
        if(this.mTypeSolicitation == RideStatusFragment.TYPE_ACTIVE_RIDE){
            ride = this.mRides.get(position);
            student = ride.student;
        }else {
            RideSolicitation solicitation = this.mSolicitations.get(position);
            ride = solicitation.ride;
            if (this.mTypeSolicitation == RideStatusFragment.TYPE_REQUEST_MADE) {
                student = solicitation.student;
            } else {
                student = solicitation.ride.student;
            }
        }
        viewHolder.driverName.setText(student.name);
        if(!TextUtils.isEmpty(student.photo)) {
            viewHolder.driverPhoto.setImageURI(Uri.parse(student.photo));
        }
        if(this.mTypeSolicitation == RideStatusFragment.TYPE_ACTIVE_RIDE){
            viewHolder.wrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final ListPopupWindow options = new ListPopupWindow(v.getContext());
                    options.setAnchorView(v);
                    options.setWidth(AppBarLayout.LayoutParams.WRAP_CONTENT);
                    final ArrayAdapter<String> adapter;
                    String[] optionsTxt = {mContext.getString(R.string.lbl_see_ride),
                            mContext.getString(R.string.lbl_cancel_offer)};
                    adapter = new ArrayAdapter(v.getContext(), R.layout.layout_ride_solicitation_option_item, optionsTxt);
                    options.setAdapter(adapter);
                    options.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String text = adapter.getItem(position);
                            if(text.equals(mContext.getString(R.string.lbl_see_ride))){
                                Intent intent = new Intent(v.getContext(), RideDetailActivity.class);
                                intent.putExtra(RideDetailActivity.IS_NEW_REQUEST_TAG, false);
                                intent.putExtra(RideDetailActivity.RIDE_TAG, ride);
                                v.getContext().startActivity(intent);
                            }else{
                                //TODO
                            }
                            options.dismiss();
                        }
                    });
                    options.show();
                }
            });
        }else {
            viewHolder.wrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final ListPopupWindow options = new ListPopupWindow(v.getContext());
                    options.setAnchorView(v);
                    options.setWidth(AppBarLayout.LayoutParams.WRAP_CONTENT);
                    final ArrayAdapter<String> adapter;
                    if (RideStatusAdapter.this.mTypeSolicitation == RideStatusFragment.TYPE_REQUEST_MADE) {
                        String[] optionsTxt = {mContext.getString(R.string.lbl_see_ride),
                                mContext.getString(R.string.lbl_cancel_offer)};
                        adapter = new ArrayAdapter(v.getContext(), R.layout.layout_ride_solicitation_option_item, optionsTxt);
                    } else {
                        String[] optionsTxt = {mContext.getString(R.string.lbl_see_ride),
                                mContext.getString(R.string.lbl_accept_solicitation),
                                mContext.getString(R.string.lbl_refuse_solicitation)};
                        adapter = new ArrayAdapter(v.getContext(), R.layout.layout_ride_solicitation_option_item, optionsTxt);
                    }
                    options.setAdapter(adapter);
                    options.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String text = adapter.getItem(position);
                            if (text.equals(mContext.getString(R.string.lbl_see_ride))) {
                                Intent intent = new Intent(v.getContext(), RideDetailActivity.class);
                                intent.putExtra(RideDetailActivity.IS_NEW_REQUEST_TAG, true);
                                intent.putExtra(RideDetailActivity.RIDE_TAG, ride);
                                v.getContext().startActivity(intent);
                            } else if (text.equals(mContext.getString(R.string.lbl_cancel_solicitation))) {
                                //TODO
                            } else if (text.equals(mContext.getString(R.string.lbl_accept_solicitation))) {
                                //TODO
                            } else {
                                //TODO
                            }
                            options.dismiss();
                        }
                    });
                    options.show();
                }
            });
        }
        if(ride.expirationDate != null){
            String hour = DateFormatUtil.formatHourView.format(ride.expirationDate);
            viewHolder.rideTime.setText(hour);
            Calendar expirationDate = Calendar.getInstance();
            Calendar now = Calendar.getInstance();
            expirationDate.setTimeInMillis(ride.expirationDate.getTime());
            if(expirationDate.get(Calendar.DAY_OF_YEAR) != now.get(Calendar.DAY_OF_YEAR)){
                String date = DateFormatUtil.formatDateView.format(ride.expirationDate);
                viewHolder.rideDate.setText(date);
            }else{
                viewHolder.rideDate.setText(this.mContext.getString(R.string.txt_today));
            }
        }

        if(ride.situation == TypeSituation.Enable) {
            if(this.mTypeSolicitation == RideStatusFragment.TYPE_ACTIVE_RIDE){
                viewHolder.rideSolicitationStatus.setText(this.mContext.getString(R.string.txt_waiting_response));
                viewHolder.rideSolicitationStatus.setBackgroundResource(R.drawable.bg_ride_status_waiting);
            }else {
                switch (this.mSolicitations.get(position).status) {
                    case Accepted:
                        viewHolder.rideSolicitationStatus.setText(this.mContext.getString(R.string.txt_accepted));
                        viewHolder.rideSolicitationStatus.setBackgroundResource(R.drawable.bg_ride_status_accepted);
                        break;
                    case Refused:
                        viewHolder.rideSolicitationStatus.setText(this.mContext.getString(R.string.txt_denied));
                        viewHolder.rideSolicitationStatus.setBackgroundResource(R.drawable.bg_ride_status_refused);
                        break;
                    case Waiting:
                        viewHolder.rideSolicitationStatus.setText(this.mContext.getString(R.string.txt_waiting_response));
                        viewHolder.rideSolicitationStatus.setBackgroundResource(R.drawable.bg_ride_status_waiting);
                        break;
                }
            }
        }else{
            viewHolder.rideSolicitationStatus.setText(this.mContext.getString(R.string.txt_cancelled));
            viewHolder.rideSolicitationStatus.setBackgroundResource(R.drawable.bg_ride_status_refused);
        }
    }

    @Override
    public int getItemCount() {
        return (this.mTypeSolicitation == RideStatusFragment.TYPE_ACTIVE_RIDE) ? this.mRides.size() : this.mSolicitations.size();
    }

    public void addItem(Object object){
        if(this.mTypeSolicitation == RideStatusFragment.TYPE_ACTIVE_RIDE){
            if (!this.mRides.contains(object)) {
                this.mRides.add((Ride)object);
                this.notifyItemInserted(this.mRides.size() - 1);
            }
        }else {
            if (!this.mSolicitations.contains(object)) {
                this.mSolicitations.add((RideSolicitation)object);
                this.notifyItemInserted(this.mSolicitations.size() - 1);
            }
        }
    }

    public void addItems(List<Object> objects){
        for (int i = 0; i < objects.size(); i++) {
            addItem(objects.get(i));
        }
    }

    public void removeItem(int position){
        this.mSolicitations.remove(position);
        this.notifyItemRemoved(position);
    }

    public void removeItem(Object object){
        int position;
        if(this.mTypeSolicitation == RideStatusFragment.TYPE_ACTIVE_RIDE){
            position = this.mRides.indexOf((Ride)object);
        }else{
            position = this.mSolicitations.indexOf((RideSolicitation)object);
        }
        removeItem(position);
    }

    public void clearList(){
        this.mSolicitations.clear();
        this.notifyDataSetChanged();
    }

    public class RideOfferViewHolder extends RecyclerView.ViewHolder{
        public SimpleDraweeView driverPhoto;
        public TextView driverName;
        public TextView rideDate;
        public TextView rideTime;
        public TextView rideSolicitationStatus;
        public CardView wrapper;

        public RideOfferViewHolder(View itemView) {
            super(itemView);

            this.driverPhoto = (SimpleDraweeView)itemView.findViewById(R.id.driverPhoto);
            this.driverName = (TextView)itemView.findViewById(R.id.driverName);
            this.rideTime = (TextView)itemView.findViewById(R.id.ride_time);
            this.rideDate = (TextView)itemView.findViewById(R.id.ride_date);
            this.wrapper = (CardView)itemView.findViewById(R.id.ride_wrapper);
            this.rideSolicitationStatus = (TextView)itemView.findViewById(R.id.ride_status);
        }
    }
}
