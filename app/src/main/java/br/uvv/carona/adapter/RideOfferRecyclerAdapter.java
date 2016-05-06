package br.uvv.carona.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import br.uvv.carona.R;
import br.uvv.carona.model.RouteRide;

/**
 * Created by CB1772 on 04/05/2016.
 */
public class RideOfferRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<RouteRide> mOffers;

    public RideOfferRecyclerAdapter(List<RouteRide> offers){
        this.mOffers = offers;
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
        RouteRide offer = this.mOffers.get(position);

        viewHolder.driverName.setText(offer.userOffer.name);
        viewHolder.driverPhoto.setImageURI(Uri.parse(offer.userOffer.photoUrl));
    }

    @Override
    public int getItemCount() {
        return this.mOffers.size();
    }

    public class RideOfferViewHolder extends RecyclerView.ViewHolder{
        public SimpleDraweeView driverPhoto;
        public TextView driverName;
        public TextView rideEstimateTime;

        public RideOfferViewHolder(View itemView) {
            super(itemView);

            this.driverPhoto = (SimpleDraweeView)itemView.findViewById(R.id.driverPhoto);
            this.driverName = (TextView)itemView.findViewById(R.id.driverName);
            this.rideEstimateTime = (TextView)itemView.findViewById(R.id.estimateTime);
        }
    }
}
