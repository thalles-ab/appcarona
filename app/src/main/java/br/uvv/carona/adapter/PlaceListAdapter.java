package br.uvv.carona.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.uvv.carona.R;
import br.uvv.carona.model.Place;

public class PlaceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Place> mPlaces;
    private Map<Integer, Place> mSelected;
    private Context mContext;
    private OnChangeListener mOnChange;

    public PlaceListAdapter(Context context, List<Place> places, OnChangeListener onChange, Map<Integer, Place> selectedPlaces){
        this.mContext = context;
        this.mPlaces = places;
        this.mSelected = selectedPlaces;
        this.mOnChange = onChange;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.layout_item_place, parent, false);
        PlaceViewHolder holder = new PlaceViewHolder(root);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Place place = this.mPlaces.get(position);
        PlaceViewHolder viewHolder = (PlaceViewHolder)holder;

        viewHolder.placeDescription.setText(place.description);

        viewHolder.itemWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaceListAdapter.this.onItemClick(position);
            }
        });
        viewHolder.itemWrapper.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PlaceListAdapter.this.onItemLongClick(position);
                return true;
            }
        });

        if(this.mSelected.containsKey(position)){
            int color = this.mContext.getResources().getColor(R.color.colorAccent);
            viewHolder.itemWrapper.setBackgroundColor(color);
        }else{
            viewHolder.itemWrapper.setBackgroundResource(0);
        }

        int paddingA = this.mContext.getResources().getDimensionPixelSize(R.dimen.padding_place_item_a);
        int paddingB = this.mContext.getResources().getDimensionPixelSize(R.dimen.padding_place_item_b);
        if(position == 0){
            viewHolder.itemWrapper.setPadding(paddingB,paddingB,paddingB,paddingA);
        }else if(position == getItemCount()-1){
            viewHolder.itemWrapper.setPadding(paddingB, paddingA, paddingB,paddingB);
        }else{
            viewHolder.itemWrapper.setPadding(paddingB, paddingA, paddingB,paddingA);
        }
    }

    @Override
    public int getItemCount() {
        return this.mPlaces.size();
    }

    public int getSelectedItemCount(){
        return this.mSelected.size();
    }

    public Place getSingleSelectedPlace(){
        if(this.mSelected.size() == 1) {
            return this.mSelected.get(this.mSelected.keySet().toArray()[0]);
        }
        return null;
    }

    private void onItemClick(int position){
        if(this.mSelected.containsKey(position)){
            this.mSelected.remove(position);
            this.notifyItemChanged(position);
        }else if(this.mSelected.size() > 0){
            onItemLongClick(position);
        }
        //TODO SHOW OPTIONS
        this.mOnChange.onChange(this.getSelectedItemCount());
    }

    private void onItemLongClick(int position){
        if(this.mSelected.containsKey(position)){
            this.mSelected.remove(position);
            return;
        }
        Place place = this.mPlaces.get(position);
        this.mSelected.put(position, place);
        this.notifyItemChanged(position);
        this.mOnChange.onChange(this.getSelectedItemCount());
    }

    public void clearSelectedPlaces(){
        this.mSelected.clear();
        this.notifyDataSetChanged();
        this.mOnChange.onChange(this.getSelectedItemCount());
    }

    public void deleteSelectedPlaces(){
        for(int i = this.mSelected.size()-1 ; i >= 0; i--){
            int key = (int)this.mSelected.keySet().toArray()[i];
            int position = this.mPlaces.indexOf(this.mSelected.get(key));
            deletePlace(position);
        }
        this.mSelected.clear();
        int size = this.mPlaces.size();
        if(size > 0) {
            this.notifyItemRangeChanged(0, this.mPlaces.size());
        }else{
            this.notifyDataSetChanged();
        }
        this.mOnChange.onChange(this.getSelectedItemCount());
    }

    private void deletePlace(int position){
        this.notifyItemRemoved(position);
        this.mPlaces.remove(position);
    }

    public void addPlace(Place place){
        this.mPlaces.add(place);
        this.notifyItemInserted(this.mPlaces.indexOf(place));
    }

    public void replacePlace(Place place){
        if(this.mPlaces.contains(place)){
            int pos = this.mPlaces.indexOf(place);
            this.mPlaces.remove(pos);
            this.mPlaces.add(pos, place);
            this.notifyItemChanged(pos);
        }else{
            this.mPlaces.add(place);
            this.notifyDataSetChanged();
        }

    }

    public void replaceContent(List<Place> places){
        this.mPlaces.clear();
        this.mPlaces.addAll(places);
        this.notifyDataSetChanged();
    }

    public Map<Integer, Place> getSelected(){
        return this.mSelected;
    }

    private class PlaceViewHolder extends RecyclerView.ViewHolder{
        public TextView placeDescription;
        public CardView contentWrapper;
        public LinearLayout itemWrapper;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            this.placeDescription = (TextView)itemView.findViewById(R.id.place_description);
            this.itemWrapper = (LinearLayout)itemView.findViewById(R.id.item_wrapper);
            this.contentWrapper = (CardView)itemView.findViewById(R.id.content_wrapper);
        }
    }

    public interface OnChangeListener{
        void onChange(Object object);
    }
}
