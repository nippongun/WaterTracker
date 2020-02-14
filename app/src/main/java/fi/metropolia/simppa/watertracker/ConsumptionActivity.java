package fi.metropolia.simppa.watertracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Spinner;

import java.util.List;

import fi.metropolia.simppa.watertracker.database.Consumption;
import fi.metropolia.simppa.watertracker.database.UnitListAdapter;
import fi.metropolia.simppa.watertracker.database.UnitViewModel;

public class ConsumptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumption);

        UnitListAdapter ula = new UnitListAdapter(this);
        UnitViewModel unitViewModel = new ViewModelProvider(this).get(UnitViewModel.class);
        Spinner spinner = findViewById(R.id.spinner);
        //spinner.setAdapter(ula);b
    }
}
