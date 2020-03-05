package fi.metropolia.simppa.watertracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import fi.metropolia.simppa.watertracker.database.Unit;
import fi.metropolia.simppa.watertracker.database.UnitListAdapter;
import fi.metropolia.simppa.watertracker.database.UnitViewModel;
import fi.metropolia.simppa.watertracker.database.UnitListAdapter;
public class ShowList extends AppCompatActivity {

    public static final int NEW_UNIT_ACTIVITY_REQUEST_CODE = 1;
    int minVolume;
    int maxVolume;
    private UnitViewModel unitViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

        minVolume = R.integer.minVolume;
        maxVolume = R.integer.maxVolume;

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final UnitListAdapter adapter = new UnitListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        unitViewModel = new ViewModelProvider(this).get(UnitViewModel.class);

        unitViewModel.getUnitList().observe(this, new Observer<List<Unit>>() {
            @Override
            public void onChanged(@Nullable final List<Unit> units) {
                adapter.setUnits(units.subList(1,units.size()));
            }
        });

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowList.this, UnitActivity.class);
                startActivityForResult(intent, NEW_UNIT_ACTIVITY_REQUEST_CODE );
            }
        });
    }

    /*
    * This method handles the data that comes sent back by the activity "UnitActivity"
    *
    * */
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        int volume = data.getIntExtra(UnitActivity.EXTRA_MESSAGE_VOLUME,0);
        // handle the data and requests
        // only if the data is correct, a unit can be created and inserted into the database
        if(requestCode == NEW_UNIT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            Unit unit = new Unit(data.getStringExtra(UnitActivity.EXTRA_MESSAGE_UNIT_NAME)
                    ,volume);
            unitViewModel.insertUnit(unit);
        } else {
            //otherwise shoot toasts and tell the user what went wrong
            if(volume > maxVolume){
                // A unit can't be too large
                Toast.makeText(getApplicationContext(),R.string.unit_too_large, Toast.LENGTH_LONG).show();
            } else if (volume < minVolume){
                //or too small
                Toast.makeText(getApplicationContext(),R.string.unit_too_small, Toast.LENGTH_LONG).show();
            }else{
                // If the user enters
                Toast.makeText(getApplicationContext(), R.string.isEmpty, Toast.LENGTH_LONG).show();
            }

        }
    }
}
