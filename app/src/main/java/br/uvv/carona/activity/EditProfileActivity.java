package br.uvv.carona.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

import br.uvv.carona.R;


public class EditProfileActivity extends BaseActivity {
    private static final int GALLERY_ACTIVITY_CODE = 205;
    private static final String IMAGE_URI_TAG = "IMAGE_URI_TAG";

    private SimpleDraweeView mPhoto;
    private Uri mLastImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mPhoto = (SimpleDraweeView)findViewById(R.id.userPhoto);

        if(savedInstanceState == null){

        }else{
            mLastImageUri = savedInstanceState.getParcelable(IMAGE_URI_TAG);
            mPhoto.setImageURI(mLastImageUri);
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.lbl_edit_profile);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(IMAGE_URI_TAG, mLastImageUri);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_ACTIVITY_CODE){
            if(data!=null) {
                mLastImageUri = Uri.parse(data.getDataString());
                mPhoto.setImageURI(mLastImageUri);
            }
        }
    }

    public void onClickSelectPhoto(View view){
        openGallery(GALLERY_ACTIVITY_CODE);
    }
}
