package br.uvv.carona.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import br.uvv.carona.R;
import br.uvv.carona.model.Place;
import br.uvv.carona.util.EventBusEvents;
import br.uvv.carona.util.MapRequestEnum;


public class EditProfileActivity extends BaseActivity {
    private static final String IMAGE_URI_TAG = "IMAGE_URI_TAG";
    private static final int GALLERY_ACTIVITY_CODE = 205;
    private static final int LOCATION_REQUEST_HOUSE = 10;
    private static final int LOCATION_REQUEST_WORK = 11;

    private SimpleDraweeView mPhoto;
    private Uri mLastImageUri;
    private Toolbar mToolbar;

    private TextView mHouseAddress;
    private TextView mWorkAddress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mPhoto = (SimpleDraweeView)findViewById(R.id.userPhoto);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if(savedInstanceState == null){

        }else{
            mLastImageUri = savedInstanceState.getParcelable(IMAGE_URI_TAG);
            mPhoto.setImageURI(mLastImageUri);
        }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Fulano da Silva");
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_profile:
                //TODO Atualizar perfil
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    @Override
    public void onErrorEvent(EventBusEvents.ErrorEvent event) {

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

            requestCode = 0;
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
            }
        }
    }

    public void onClickUploadPhoto(View view) {
        //TODO upload foto para o servidor
    }
}
