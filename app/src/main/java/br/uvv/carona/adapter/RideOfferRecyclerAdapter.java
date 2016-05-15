package br.uvv.carona.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import br.uvv.carona.R;
import br.uvv.carona.activity.RideDetailActivity;
import br.uvv.carona.model.Place;
import br.uvv.carona.model.Ride;

/**
 * Created by CB1772 on 04/05/2016.
 */
public class RideOfferRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Ride> mOffers;
    private Place mDeparture;
    private Place mDestination;

    public RideOfferRecyclerAdapter(List<Ride> offers, Place departure, Place destination){
        this.mOffers = offers;
        this.mDeparture = departure;
        this.mDestination = destination;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_offer_item, null);
        RideOfferViewHolder viewHolder = new RideOfferViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RideOfferViewHolder viewHolder = (RideOfferViewHolder)holder;
        final Ride offer = this.mOffers.get(position);

        viewHolder.driverName.setText(offer.student.name);
        if(!TextUtils.isEmpty(offer.student.photo)) {
            viewHolder.driverPhoto.setImageURI(Uri.parse(offer.student.photo));
        }
        viewHolder.wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), RideDetailActivity.class);
                i.putExtra(RideDetailActivity.IS_NEW_REQUEST_TAG, true);
                i.putExtra(RideDetailActivity.RIDE_TAG, offer);
                i.putExtra(RideDetailActivity.PLACE_DEP_TAG, mDeparture);
                i.putExtra(RideDetailActivity.PLACE_DES_TAG, mDestination);
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mOffers.size();
    }

    public class RideOfferViewHolder extends RecyclerView.ViewHolder{
        public SimpleDraweeView driverPhoto;
        public TextView driverName;
        public TextView rideTime;
        public TextView rideDate;
        public CardView wrapper;

        public RideOfferViewHolder(View itemView) {
            super(itemView);

            this.driverPhoto = (SimpleDraweeView)itemView.findViewById(R.id.driverPhoto);
            this.driverName = (TextView)itemView.findViewById(R.id.driverName);
            this.rideTime = (TextView)itemView.findViewById(R.id.ride_time);
            this.rideDate = (TextView)itemView.findViewById(R.id.ride_date);
            this.wrapper = (CardView)itemView.findViewById(R.id.ride_wrapper);
        }
    }
}
