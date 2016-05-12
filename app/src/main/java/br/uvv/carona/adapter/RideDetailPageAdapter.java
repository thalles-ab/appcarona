package br.uvv.carona.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.uvv.carona.R;
import br.uvv.carona.fragment.RideDetailFragment;
import br.uvv.carona.fragment.RideMapFragment;
import br.uvv.carona.model.Place;
import br.uvv.carona.model.Ride;

/**
 * Created by CB1772 on 08/05/2016.
 */
public class RideDetailPageAdapter extends FragmentPagerAdapter {
    public static final int TOTAL_TAB_COUNT = 2;
    public static final int MEMBERS_AND_DETAILS = 0;
    public static final int MAP = 1;
    private Context mContext;
    private Ride mRide;
    private Place mDeparture;
    private Place mDestination;

    public RideDetailPageAdapter(Context context, FragmentManager fm, Ride ride, Place departure, Place destination) {
        super(fm);
        this.mContext = context;
        this.mRide = ride;
        this.mDeparture = departure;
        this.mDestination = destination;
    }

    public Fragment getItem(int pos) {
        switch (pos) {
            case MEMBERS_AND_DETAILS:
                return new RideDetailFragment().newInstance(this.mRide);
            case MAP:
                return new RideMapFragment().newInstance(this.mRide, this.mDeparture, this.mDestination);
        }
        return null;
    }


    public int getCount() {
        return TOTAL_TAB_COUNT;
    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
            case MEMBERS_AND_DETAILS:
                return mContext.getString(R.string.lbl_ride_detail);
            case MAP:
                return mContext.getString(R.string.lbl_map);
        }
        return null;
    }
}
