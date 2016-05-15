package br.uvv.carona.activity;

import android.os.Bundle;

import br.uvv.carona.R;
import br.uvv.carona.util.EventBusEvents;

public class EditPlacesActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_places);
    }

    @Override
    public void onErrorEvent(EventBusEvents.ErrorEvent event) {

    }
}
