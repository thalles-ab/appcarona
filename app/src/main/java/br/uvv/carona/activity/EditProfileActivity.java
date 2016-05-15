package br.uvv.carona.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.uvv.carona.R;
import br.uvv.carona.asynctask.UploadPhotoAsyncTask;
import br.uvv.carona.httprequest.util.WSResources;
import br.uvv.carona.model.Student;
import br.uvv.carona.model.UploadFile;
import br.uvv.carona.util.EventBusEvents;
import br.uvv.carona.util.MapRequestEnum;
import br.uvv.carona.view.PhoneEditText;


public class EditProfileActivity extends BaseActivity {
    private static final String EXTRA_IMAGE_URI = "EXTRA_IMAGE_URI";
    private static final String EXTRA_STUDENT = "EXTRA_STUDENT";
    private static final int EXTRA_GALLERY_ACTIVITY = 205;
    private EditText mUserName;
    private EditText mUserRegistration;
    private PhoneEditText mUserPhone;
    private EditText mUserEmail;
    private List<EditText> mFields;

    private Student mStudent;
    private SimpleDraweeView mPhoto;
    private Uri mLastImageUri;
    private Toolbar mToolbar;
    private Uri mOutputFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mPhoto = (SimpleDraweeView)findViewById(R.id.userPhoto);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mUserName = (EditText)this.findViewById(R.id.fieldUserName);
        mUserRegistration = (EditText)this.findViewById(R.id.fieldUserRegistration);
        mUserPhone= (PhoneEditText)this.findViewById(R.id.fieldUserPhone);
        mUserEmail = (EditText)this.findViewById(R.id.fieldUserEmail);

        mFields = new ArrayList<>();
        mFields.add(mUserName);
        mFields.add(mUserRegistration);
        mFields.add(mUserEmail);
        mFields.add(mUserPhone);

        if(savedInstanceState == null){
            mStudent = new Student();
        }else{
            mLastImageUri = savedInstanceState.getParcelable(EXTRA_IMAGE_URI);
            mPhoto.setImageURI(mLastImageUri);
            mStudent = (Student) savedInstanceState.get(EXTRA_STUDENT);
        }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mStudent.name);
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
        this.stopProgressDialog();
        treatCommonErrors(event);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(EXTRA_IMAGE_URI, mLastImageUri);
        outState.putSerializable(EXTRA_STUDENT, mStudent);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mStudent = (Student) savedInstanceState.getSerializable(EXTRA_STUDENT);
        mLastImageUri = savedInstanceState.getParcelable(EXTRA_IMAGE_URI);
    }

    public void onClickSelectPhoto(View view){
        openGallery(EXTRA_GALLERY_ACTIVITY);
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
        if (resultCode == RESULT_OK) {
            if (requestCode == EXTRA_GALLERY_ACTIVITY) {
                final boolean isCamera;
                if (data == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }
                Uri selectedImageUri;
                if (isCamera) {
                    selectedImageUri = mOutputFile;
                } else {
                    selectedImageUri = data == null ? null : data.getData();
                }
                String filePath;
                if (selectedImageUri != null && "content".equals(selectedImageUri.getScheme())) {
                    Cursor cursor = this.getContentResolver().query(selectedImageUri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
                    cursor.moveToFirst();
                    filePath = cursor.getString(0);
                    cursor.close();
                } else {
                    filePath = selectedImageUri.getPath();
                }
                File photo = new File(filePath);
                if (photo.exists()) {
                    mStudent.file = photo;
                    new UploadPhotoAsyncTask().execute(mStudent);
                }
            }
        }
    }

    //TODO retorno
    @Subscribe
    public void onUploadEvent(UploadFile file){
        if(!TextUtils.isEmpty(file.url)){
            mStudent.photo = WSResources.BASE_UPLOAD_URL+file.url;
            mLastImageUri = Uri.parse(mStudent.photo);
            mPhoto.setImageURI(mLastImageUri);
        }
    }

    private void openImageIntent() {
        mOutputFile = getOutputMediaFileUri();

        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutputFile);
            cameraIntents.add(intent);
        }

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");

        final Intent chooserIntent = Intent.createChooser(galleryIntent,getString(R.string.lbl_select_source));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));
        startActivityForResult(chooserIntent, EXTRA_GALLERY_ACTIVITY);
    }

    private Uri getOutputMediaFileUri() {
        File imagesFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), getString(R.string.app_name));
        if (!imagesFolder.exists()) {
            imagesFolder.mkdirs();
        }
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
        File file = new File(imagesFolder, getString(R.string.app_name)+"_"+date.format(new Date())+".png");
        return Uri.fromFile(file);
    }

    public void onClickUploadPhoto(View view) {
        openImageIntent();
    }
}
