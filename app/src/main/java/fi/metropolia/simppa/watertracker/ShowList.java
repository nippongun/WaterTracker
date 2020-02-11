package fi.metropolia.simppa.watertracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

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
    private UnitViewModel unitViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final UnitListAdapter adapter = new UnitListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        unitViewModel = new ViewModelProvider(this).get(UnitViewModel.class);

        unitViewModel.getUnitList().observe(this, new Observer<List<Unit>>() {
            @Override
            public void onChanged(@Nullable final List<Unit> units) {
                adapter.setUnits(units);
            }
        });
    }
}
