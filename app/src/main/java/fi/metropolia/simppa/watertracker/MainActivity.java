package fi.metropolia.simppa.watertracker;

import fi.metropolia.simppa.watertracker.database.Consumption;
import fi.metropolia.simppa.watertracker.database.Unit;
import fi.metropolia.simppa.watertracker.database.UnitViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.AsyncTask;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    Button b1, b2, b3, b4;
    Intent intent;
    int todayConsumption = 0; //For circle chart
    int todayGoal; //For circle chart

    private ArrayList<String> unitNameList = new ArrayList<>();
    private Spinner spinner;
    private boolean isinitial = true;

    int waterConsumed = 0; //For circle chart
    int waterGoal = 2500; //For circle chart
    private DailyGoal goal = new DailyGoal(2500); //to obtain updated Daily Goal


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = findViewById(R.id.button);
        b2 = findViewById(R.id.button2);
        b3 = findViewById(R.id.setDailyGoalButton);
        b4 = findViewById(R.id.statsButton);

        String defaultTextForSpinner = "text here";


        //spinner.setAdapter(new CustomSpinnerAdapter(this, R.layout.spinner_row, arrayForSpinner, defaultTextForSpinner));


        /*
         * From Feihua
         * populate the spinner in the main_activity
         *
         */
        //get view Model
        UnitViewModel unitViewModel = new ViewModelProvider(this).get(UnitViewModel.class);
        //get all units from database through view model and live data
        unitViewModel.getUnitList().observe(this, new Observer<List<Unit>>() {

            @Override
            public void onChanged(List<Unit> units) {

                //empty the list so every item are not populate again and again
                unitNameList.clear();
                for (Unit unit : units) {
                    unitNameList.add(unit.getUnitName() + " " + unit.getVolume() + "ml");
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, unitNameList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner = findViewById(R.id.main_spinner_chooseUnit);
                spinner.setAdapter(adapter);


            }

        });//end of unitViewModel.getUnitList().observe



        /*
        add a listener to the spinner when you select one item of spinners the text get extracted
        then split into String arrays. the fist element of the array is the name of the unit you selected.
        after that perform a database query find the ID of the unit then combine the id and a timestamp we create a
        new object of consumption. then insert it to the database. after that the a new intent created add a string
        for the next activity to know it needs to show the full list. then start the intent.
        * */

        spinner = findViewById(R.id.main_spinner_chooseUnit);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            UnitViewModel waterViewModel = new ViewModelProvider(MainActivity.this).get(UnitViewModel.class);

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] unitName = spinner.getSelectedItem().toString().split(" ");

                waterViewModel.getUnitByName(unitName[0]).observe(MainActivity.this, new Observer<Unit>() {
                            @Override
                            public void onChanged(Unit unit) {

                                Consumption drink = new Consumption(unit.getPrimaryKey(), Calendar.getInstance().getTime());

                                if (!isinitial) {
                                    new InsertConsumption().execute(drink);
                                    Intent intent = new Intent(MainActivity.this, AllDrinkList.class);
                                    intent.putExtra("message", "all");
                                    startActivity(intent);
                                }
                                isinitial = false;


                            }
                        }
                );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });//end of the spinner listener


    }

    @Override
    public void onResume() {
        super.onResume();
        updateChart();

    }

    public class InsertConsumption extends AsyncTask<Consumption, Void, Void> {
        UnitViewModel viewModel = new ViewModelProvider(MainActivity.this).get(UnitViewModel.class);

        @Override
        protected Void doInBackground(Consumption... drinks) {

            viewModel.insertConsumption(drinks[0]);
            return null;
        }
    }


    public void onButton(View view) {
        if (view.getId() == b1.getId()) {
            intent = new Intent(this, UnitActivity.class);
            startActivity(intent);
        } else if (view.getId() == b2.getId()) {
            intent = new Intent(this, ShowList.class);
            startActivity(intent);
        } else if (view.getId() == b3.getId()) {
            intent = new Intent(this, DailyGoalActivity.class);
            startActivity(intent);
        } else if (view.getId() == b4.getId()) {
            Log.d("TEST", "stats button clicked");
            intent = new Intent(this, StatisticsActivity.class);
            startActivity(intent);
            Log.d("TEST", "stats activity started");
        }
    }

    //this function should be changed once we know daily consumption; also field and button from activity_main should be removed
    public void addBurned(View v) {
        // Get the new value from a user input and update:
        EditText burnedEditText = findViewById(R.id.burned);
        todayConsumption = Integer.parseInt(burnedEditText.getText().toString());
        updateChart();
    }

    //update circle chart
    private void updateChart() {
        // Get latest daily goal
        // 1. Open the file: get references
        SharedPreferences prefGet = getSharedPreferences("DailyGoal", Activity.MODE_PRIVATE);
        //2. Read the value, default 0 if not strored
        todayGoal = prefGet.getInt("new goal", 0);

        // Update the texts "consumed out of goal" and "XX%"
        TextView statusUpdateTextView = findViewById(R.id.statusUpdateTextView);
        TextView percentageTextView = findViewById(R.id.percentageTextView);
        statusUpdateTextView.setText(String.valueOf(todayConsumption) + " ml out of " + String.valueOf(todayGoal) + " ml");

        // Calculate the slice size and update the pie chart:
        ProgressBar pieChart = findViewById(R.id.stats_progressbar);
        double d = (double) todayConsumption / (double) todayGoal;
        int progress = (int) (d * 100);
        pieChart.setProgress(progress);
        percentageTextView.setText(String.valueOf(progress) + "%");
        Log.d("TEST", String.valueOf(todayGoal));
    }


}
