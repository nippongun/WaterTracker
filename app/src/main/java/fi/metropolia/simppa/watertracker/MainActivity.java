package fi.metropolia.simppa.watertracker;

import fi.metropolia.simppa.watertracker.database.Unit;
import fi.metropolia.simppa.watertracker.database.UnitViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button b1, b2, b3;
    Intent intent;
    private ArrayList<String> unitNameList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = findViewById(R.id.button);
        b2 = findViewById(R.id.button2);
        b3 = findViewById(R.id.setDailyGoalButton);


        /*
         * From Feihua
         * populate the spinner in the main_activity
         *
          */
        //get view Model
        UnitViewModel unitViewModel= new ViewModelProvider(this).get(UnitViewModel.class);
        //get all units from database through view model and live data
        unitViewModel.getUnitList().observe(this,new Observer<List<Unit>>(){

            @Override
            public void onChanged(List<Unit> units) {
                Log.d("my",""+units.size());
                //empty the list so every item are not populate again and again
                unitNameList.clear();
                for(Unit unit:units){
                    unitNameList.add(unit.getUnitName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, unitNameList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Spinner spinner=findViewById(R.id.main_spinner_chooseUnit);
                spinner.setAdapter(adapter);



            }
        });//end of unitViewModel.getUnitList().observe







    }

    public void onButton(View view) {
        if (view.getId() == b1.getId()) {
            intent = new Intent(this, UnitActivity.class);
            startActivity(intent);
        } else if (view.getId() == b2.getId()) {
            intent = new Intent(this, ShowList.class);
            startActivity(intent);
        } else if (view.getId() == b3.getId()) {
            intent = new Intent(this, DailyGoalSettings.class);
            startActivity(intent);
        }
    }



}
