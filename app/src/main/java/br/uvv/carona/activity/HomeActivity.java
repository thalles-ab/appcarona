package br.uvv.carona.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.uvv.carona.R;

public class HomeActivity extends BaseActivity {
    private static final String USER_PHOTO_TAG = "USER_PHOTO";

    private ImageView mUserPhotoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setTitle(R.string.lbl_home);

        mUserPhotoView = (ImageView)findViewById(R.id.userPhoto);
        ((TextView)findViewById(R.id.userName)).setText("Fulano de Almeida");
        ((TextView)findViewById(R.id.userCourse)).setText("Ciência da Computação");
        if(savedInstanceState == null){
            //TODO LOAD USER'S PHOTO AND DO OTHER THINGS
        }else{
            mUserPhotoView.setImageBitmap((Bitmap) savedInstanceState.getParcelable(USER_PHOTO_TAG));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bitmap bitmap = ((BitmapDrawable) mUserPhotoView.getDrawable()).getBitmap();
        outState.putParcelable(USER_PHOTO_TAG, bitmap);
    }

    public void onClickEditProfile(View view){
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }

    public void onClickRequestRide(View view){
        Intent intent = new Intent(this, RequestRideActivity.class);
        startActivity(intent);
    }

    public void onClickOfferRide(View view){

    }

    public void onClickLogOut(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}