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
import br.uvv.carona.fragment.RideSolicitationsFragment;
import br.uvv.carona.model.RideSolicitation;
import br.uvv.carona.util.DateFormatUtil;

/**
 * Created by CB1772 on 10/05/2016.
 */
public class RideRequestStatusAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<RideSolicitation> mRequests;
    private Context mContext;
    private int mTypeRequest;

    public RideRequestStatusAdapter(List<RideSolicitation> offers, Context context, int typeRequest){
        this.mRequests = offers;
        this.mContext = context;
        this.mTypeRequest = typeRequest;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ride_request_status, null);
        RideOfferViewHolder viewHolder = new RideOfferViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RideOfferViewHolder viewHolder = (RideOfferViewHolder)holder;
        final RideSolicitation request = this.mRequests.get(position);

        viewHolder.driverName.setText(request.student.name);
        if(!TextUtils.isEmpty(request.student.photo)) {
            viewHolder.driverPhoto.setImageURI(Uri.parse(request.student.photo));
        }
        viewHolder.wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final ListPopupWindow options = new ListPopupWindow(v.getContext());
                options.setAnchorView(v);
                options.setWidth(AppBarLayout.LayoutParams.WRAP_CONTENT);
                final ArrayAdapter<String> adapter;
                if(RideRequestStatusAdapter.this.mTypeRequest == RideSolicitationsFragment.TYPE_REQUEST_MADE){
                    String[] optionsTxt = {mContext.getString(R.string.lbl_see_ride),
                            mContext.getString(R.string.lbl_cancel_request)};
                    adapter = new ArrayAdapter(v.getContext(), R.layout.layout_ride_request_option_item, optionsTxt);
                }else{
                    String[] optionsTxt = {mContext.getString(R.string.lbl_see_ride),
                            mContext.getString(R.string.lbl_accept_request),
                            mContext.getString(R.string.lbl_refuse_request)};
                    adapter = new ArrayAdapter(v.getContext(), R.layout.layout_ride_request_option_item, optionsTxt);
                }
                options.setAdapter(adapter);
                options.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String text = adapter.getItem(position);
                        if(text.equals(mContext.getString(R.string.lbl_see_ride))){
                            Intent intent = new Intent(v.getContext(), RideDetailActivity.class);
                            intent.putExtra(RideDetailActivity.IS_NEW_REQUEST_TAG, true);
                            intent.putExtra(RideDetailActivity.RIDE_TAG, request.ride);
                            v.getContext().startActivity(intent);
                        }else if(text.equals(mContext.getString(R.string.lbl_cancel_request))){
                            //TODO
                        }else if(text.equals(mContext.getString(R.string.lbl_accept_request))){
                            //TODO
                        }else{
                            //TODO
                        }
                        options.dismiss();
                    }
                });
                options.show();
            }
        });
        if(request.ride.expirationDate != null){
            String hour = DateFormatUtil.formatHourView.format(request.ride.expirationDate);
            viewHolder.rideTime.setText(hour);
            Calendar expirationDate = Calendar.getInstance();
            Calendar now = Calendar.getInstance();
            expirationDate.setTimeInMillis(request.ride.expirationDate.getTime());
            if(expirationDate.get(Calendar.DAY_OF_YEAR) != now.get(Calendar.DAY_OF_YEAR)){
                String date = DateFormatUtil.formatDateView.format(request.ride.expirationDate);
                viewHolder.rideDate.setText(date);
            }else{
                viewHolder.rideDate.setText(this.mContext.getString(R.string.txt_today));
            }
        }

        switch (request.status){
            case Accepted:
                viewHolder.rideRequestStatus.setText("Aceito");
                viewHolder.rideRequestStatus.setBackgroundResource(R.drawable.bg_ride_status_accepted);
                break;
            case Refused:
                viewHolder.rideRequestStatus.setText("Recusado");
                viewHolder.rideRequestStatus.setBackgroundResource(R.drawable.bg_ride_status_refused);
                break;
            case Waiting:
                viewHolder.rideRequestStatus.setText("Aguardando");
                viewHolder.rideRequestStatus.setBackgroundResource(R.drawable.bg_ride_status_waiting);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return this.mRequests.size();
    }

    public void addRideRequest(RideSolicitation request){
        if(this.mRequests.contains(request)){

        }else {
            this.mRequests.add(request);
            this.notifyItemInserted(this.mRequests.size()-1);
        }
    }

    public void addRideRequest(List<RideSolicitation> requests){
        if(this.mRequests.size() > 0){
            for(int i = 0; i < requests.size(); i++){
                addRideRequest(requests.get(i));
            }
        }else{
            this.changeRideRequestList(requests);
        }
    }

    public void changeRideRequestList(List<RideSolicitation> requests){
        this.mRequests = requests;
        this.notifyDataSetChanged();
    }

    public void removeRideRequest(int position){
        this.mRequests.remove(position);
        this.notifyItemRemoved(position);
    }

    public void removeRideRequest(RideSolicitation request){
        int position = this.mRequests.indexOf(request);
        removeRideRequest(position);
    }

    public void clearList(){
        this.mRequests.clear();
        this.notifyDataSetChanged();
    }

    public class RideOfferViewHolder extends RecyclerView.ViewHolder{
        public SimpleDraweeView driverPhoto;
        public TextView driverName;
        public TextView rideDate;
        public TextView rideTime;
        public TextView rideRequestStatus;
        public CardView wrapper;

        public RideOfferViewHolder(View itemView) {
            super(itemView);

            this.driverPhoto = (SimpleDraweeView)itemView.findViewById(R.id.driverPhoto);
            this.driverName = (TextView)itemView.findViewById(R.id.driverName);
            this.rideTime = (TextView)itemView.findViewById(R.id.ride_time);
            this.rideDate = (TextView)itemView.findViewById(R.id.ride_date);
            this.wrapper = (CardView)itemView.findViewById(R.id.ride_wrapper);
            this.rideRequestStatus = (TextView)itemView.findViewById(R.id.request_status);
        }
    }
}
