package br.uvv.carona.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.uvv.carona.R;
import br.uvv.carona.adapter.PlaceListAdapter;
import br.uvv.carona.asynctask.GetUserPlacesAsyncTask;
import br.uvv.carona.asynctask.SavePlaceAsyncTask;
import br.uvv.carona.dialog.MessageDialog;
import br.uvv.carona.dialog.SingleFieldDialog;
import br.uvv.carona.model.Place;
import br.uvv.carona.util.EventBusEvents;
import br.uvv.carona.util.MapRequestEnum;

public class EditPlacesActivity extends BaseActivity implements PlaceListAdapter.OnChangeListener {
    private static final int REQUEST_NEW_PLACE_CODE = 10;
    private static final String PLACE_LIST_TAG = ".PLACE_LIST_TAG";
    private static final String SELECTED_ITEMS_TAG = ".SELECTED_ITEMS_TAG";
    private static final String LAYOUT_MANAGER_STATE_TAG = ".LAYOUT_MANAGER_STATE_TAG";

    private List<Place> mPlaces;
    private RecyclerView mRecyclerView;
    private PlaceListAdapter mAdapter;

    private Menu mMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_places);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        if(savedInstanceState == null){
            this.mPlaces = new ArrayList<>();
            startProgressDialog(R.string.msg_getting_places);
            new GetUserPlacesAsyncTask().execute();
        }else{
            this.mPlaces = (List<Place>)savedInstanceState.getSerializable(PLACE_LIST_TAG);
            layoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE_TAG));
        }

        this.mRecyclerView = (RecyclerView)findViewById(R.id.places_list);
        this.mAdapter = new PlaceListAdapter(this, this.mPlaces, this);
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setLayoutManager(layoutManager);
        setUpToolbar();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_place, menu);
        this.mMenu = menu;
        if(this.mAdapter != null){
            this.onChange(0);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, MapActivity.class);
                intent.putExtra(MapActivity.TYPE_MAP_REQUEST, MapRequestEnum.AddPlace);
                startActivityForResult(intent,REQUEST_NEW_PLACE_CODE);
                return true;
            case R.id.action_delete:
                this.mAdapter.deleteSelectedPlaces();
                return true;
            case R.id.action_edit:
                final Place place = this.mAdapter.getSingleSelectedPlace();
                if(place != null){
                    SingleFieldDialog.newInstance(getString(R.string.lbl_edit_place), getString(R.string.lbl_description),
                            place.description, InputType.TYPE_CLASS_TEXT,
                            new SingleFieldDialog.OnDialogButtonClick() {
                                @Override
                                public void onConfirmClick(Dialog dialog, TextInputEditText field) {
                                    String userInput = field.getText().toString();
                                    if(TextUtils.isEmpty(userInput)){
                                        field.setError(getString(R.string.error_fill_field));
                                    }else{
                                        place.description = userInput;
                                        dialog.dismiss();
                                        EditPlacesActivity.this.startProgressDialog(R.string.msg_saving_place);
                                        new SavePlaceAsyncTask().execute(place);
                                    }
                                }
                            }).show(getSupportFragmentManager(), "EDIT_PLACE_DESC");
                }
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpToolbar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.lbl_places);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        if(this.mAdapter.getSelectedItemCount() > 0){

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PLACE_LIST_TAG, (Serializable)this.mPlaces);
        outState.putParcelable(LAYOUT_MANAGER_STATE_TAG, this.mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Subscribe
    public void onGetPlacesEvent(EventBusEvents.PlaceEvent event){
        this.mPlaces = event.places;
        this.mAdapter.replaceContent(this.mPlaces);
        stopProgressDialog();
    }

    @Subscribe void onSuccessEvent(EventBusEvents.SuccessEvent event){
        if(event.success){
            this.mAdapter.clearSelectedPlaces();
            this.mAdapter.notifyDataSetChanged();
        }
        stopProgressDialog();
    }

    @Override
    public void onChange(Object object) {
        int qnt = (int)object;
        boolean edit = false;
        boolean delete = false;
        if(qnt > 0){
            delete = true;
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
            if(qnt == 1){
                edit = true;
            }
        }else{
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }
        this.mMenu.findItem(R.id.action_edit).setVisible(edit).setEnabled(edit);
        this.mMenu.findItem(R.id.action_delete).setVisible(delete).setEnabled(delete);
    }

    @Override
    public void onBackPressed() {
        if(this.mAdapter.getSelectedItemCount() > 0){
            this.mAdapter.clearSelectedPlaces();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(data != null) {
                if (requestCode == REQUEST_NEW_PLACE_CODE) {
                    Place place = (Place)data.getSerializableExtra(MapActivity.PLACE_TAG);
                    this.mAdapter.addPlace(place);
                }
            }
        }
    }
}
