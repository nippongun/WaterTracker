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
import java.util.List;

import fi.metropolia.simppa.watertracker.database.Unit;
import fi.metropolia.simppa.watertracker.database.UnitListAdapter;
import fi.metropolia.simppa.watertracker.database.UnitViewModel;
import fi.metropolia.simppa.watertracker.database.UnitListAdapter;
public class ShowList extends AppCompatActivity {

    public static final int NEW_UNIT_ACTIVITY_REQUEST_CODE = 1;

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

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowList.this, UnitActivity.class);
                startActivityForResult(intent, NEW_UNIT_ACTIVITY_REQUEST_CODE );
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == NEW_UNIT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            Unit unit = new Unit(data.getStringExtra(UnitActivity.EXTRA_MESSAGE_UNIT_NAME)
                    ,data.getIntExtra(UnitActivity.EXTRA_MESSAGE_VOLUME,0));
            unitViewModel.insertUnit(unit);
        } else {
            Toast.makeText(getApplicationContext(),R.string.isEmpty, Toast.LENGTH_LONG).show();
        }
    }
}
