package br.uvv.carona.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import br.uvv.carona.R;
import br.uvv.carona.model.Place;
import br.uvv.carona.util.MapRequestEnum;


public class EditProfileActivity extends BaseActivity {
    private static final String IMAGE_URI_TAG = "IMAGE_URI_TAG";
    private static final int GALLERY_ACTIVITY_CODE = 205;
    private static final int LOCATION_REQUEST_HOUSE = 10;
    private static final int LOCATION_REQUEST_WORK = 11;

    private SimpleDraweeView mPhoto;
    private Uri mLastImageUri;

    private TextView mHouseAddress;
    private TextView mWorkAddress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mPhoto = (SimpleDraweeView)findViewById(R.id.userPhoto);

        if(savedInstanceState == null){

        }else{
            mLastImageUri = savedInstanceState.getParcelable(IMAGE_URI_TAG);
            mPhoto.setImageURI(mLastImageUri);
        }

        this.mHouseAddress = (TextView)findViewById(R.id.userHomeAddress);
        this.mWorkAddress = (TextView)findViewById(R.id.userWorkAddress);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.lbl_edit_profile);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(IMAGE_URI_TAG, mLastImageUri);
    }

    public void onClickSelectPhoto(View view){
        openGallery(GALLERY_ACTIVITY_CODE);
    }

    public void onClickSelectLocation(View view){
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(MapActivity.TYPE_MAP_REQUEST, MapRequestEnum.AddPlace);
        int requestCode;
        if(view.getId() == R.id.changeHomeAddress){
            requestCode = LOCATION_REQUEST_HOUSE;
        }else if(view.getId() == R.id.changeWorkPlaceAddress){
            requestCode = LOCATION_REQUEST_WORK;
        }else{
            requestCode = 0;
        }
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if (requestCode == GALLERY_ACTIVITY_CODE) {
                if (data != null) {
                    mLastImageUri = Uri.parse(data.getDataString());
                    mPhoto.setImageURI(mLastImageUri);
                }
            } else if (requestCode == LOCATION_REQUEST_HOUSE) {
                if (data != null) {
                    List<Place> places = (List<Place>)data.getSerializableExtra(MapActivity.PLACES_TAG);
                    this.mHouseAddress.setText(places.get(0).description);
                }
            } else if (requestCode == LOCATION_REQUEST_WORK) {
                if (data != null) {
                    List<Place> places = (List<Place>)data.getSerializableExtra(MapActivity.PLACES_TAG);
                    this.mWorkAddress.setText(places.get(0).description);
                }
            }
        }
    }
}
