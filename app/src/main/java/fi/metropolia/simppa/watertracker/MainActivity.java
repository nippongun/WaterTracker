package fi.metropolia.simppa.watertracker;

import fi.metropolia.simppa.watertracker.database.Consumption;
import fi.metropolia.simppa.watertracker.database.Unit;
import fi.metropolia.simppa.watertracker.database.UnitDatabase;
import fi.metropolia.simppa.watertracker.database.UnitViewModel;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button addUnitButton;
    private Button dailyGoalButton;
    private Button chartButton;
    private int todayConsumption = 0; //For circle chart
    private Intent intent;
    private ArrayList<String> unitNameList = new ArrayList<>();
    private Spinner spinner;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("test", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addUnitButton = findViewById(R.id.button_addunit);
        dailyGoalButton = findViewById(R.id.button_dailygoal);
        chartButton = findViewById(R.id.button_chart);
        progressBar = findViewById(R.id.stats_progressbar);
        //Set notifications
        Intent intent = new Intent(MainActivity.this, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //Set the alarm to start at approximately 2:00 p.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 14);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        createNotificationChannel();
        //End of notifications

        getVolume gv= new getVolume();

        //get today's year month and day then set Date from as the beginning of the day, Date to as the end of the day.

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);//month give you a value start from 0
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);


        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year,month,day,0,0,0);
        Date from=cal.getTime();

        cal.set(year,month,day,23,59,59);
        Date to=cal.getTime();

        //get the whole day's volume and set to chart
        gv.execute(from,to);
        /*
         * From Feihua
         * populate the spinner in the main_activity
         *
         */
        //get view Model
        UnitViewModel unitViewModel = new ViewModelProvider(this).get(UnitViewModel.class);
        //get all units from database through view model and live data
        unitViewModel.getUnitList().observe(this, units -> {

            //empty the list so every item are not populate again and again
            unitNameList.clear();

            int i = 0;
            for (Unit unit : units) {

                if (i == 0) {
                    i++;
                    unitNameList.add(unit.getUnitName());
                    continue;
                }

                unitNameList.add(unit.getUnitName() + " " + unit.getVolume() + "ml");
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_unit_style, unitNameList);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_style);
            spinner = findViewById(R.id.main_spinner_chooseUnit);

            spinner.setAdapter(adapter);
        });//end of unitViewModel.getUnitList().observe



        /*
        add a listener to the spinner when you select one item of spinners the text get extracted
        then split into String arrays. the fist element of the array is the name of the unit you selected.
        after that perform a database query find the ID of the unit then combine the id and a timestamp we create a
        new object of consumption. then insert it to the database. after that the a new intent created add a string
        for the next activity to know it needs to show the full list. then start the intent.
        * */
        spinner = findViewById(R.id.main_spinner_chooseUnit);

        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] unitName = new String[2];
                String[] string = spinner.getSelectedItem().toString().split(" ");
                Log.d("MAIN", "what? " + string[0]);

                // This lines make sure that unit names with spaces are getting connected
                // properly to avoid a NullPointException
                // If the temporary String is larger than 2
                if(string.length > 2){
                    // put the first element into the array
                    unitName[0] = string[0] + " ";
                    //then add ther remaining bits
                    for(int i = 1; i < string.length-1;i++){
                        unitName[0] += string[i];
                        // and make sure the names are properly spaced
                        if (i<string.length-2){
                            unitName[0] += " ";
                        }
                    }
                    if(unitName[0].equals(getString(R.string.select_an_dummy))){
                        unitName[0] = getString(R.string.dummy_name);
                    }
                    // otherwise just continue
                } else {
                    unitName = string;
                }
                Log.d("unit",unitName[0]);
                //UnitDatabase db = UnitDatabase.getDatabase(getApplicationContext());
                if (!unitName[0].equals(getString(R.string.dummy_name))){
                    InsertConsumption ic = new InsertConsumption();
                    ic.execute(unitName[0]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });//end of the spinner listener
    }

    /**update the piechart again onResume
     * **/
    @Override
    public void onResume() {
        super.onResume();
        updateChart();

        getVolume gv= new getVolume();


        int year=Calendar.getInstance().get(Calendar.YEAR);
        int month= Calendar.getInstance().get(Calendar.MONTH);
        int day=Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year,month,day,0,0,0);
        Date from=cal.getTime();



        cal.set(year,month,day,23,59,59);
        Date to=cal.getTime();

        //get the whole day's volume and set to chart
        gv.execute(from,to);


    }

    @Override
    public void onStart(){
        super.onStart();
        getVolume gv= new getVolume();


        int year=Calendar.getInstance().get(Calendar.YEAR);
        int month= Calendar.getInstance().get(Calendar.MONTH);
        int day=Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year,month,day,0,0,0);
        Date from=cal.getTime();

        cal.set(year,month,day,23,59,59);
        Date to=cal.getTime();

        //get the whole day's volume and set to chart
        gv.execute(from,to);
    }

    /*
    * Handles the buttons of the main activity
    * */
    public void onButton(View view) {

        if (view.getId() == addUnitButton.getId()) {
            intent = new Intent(this, ShowList.class);
        } else if (view.getId() == dailyGoalButton.getId()) {
            intent = new Intent(this, DailyGoalActivity.class);
        } else if (view.getId() == chartButton.getId()) {
            intent = new Intent(this, Chart.class);
        } else if (view.getId() == progressBar.getId()){
            intent = new Intent(this, AllDrinkList.class);
            intent.putExtra("message","all");
        }
        startActivity(intent);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String description = getString(R.string.channel_description);
            NotificationChannel channel = new NotificationChannel("notifyUser", "Reminders", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    //update circle chart
    @SuppressLint("SetTextI18n")
    private void updateChart() {
        // Get latest daily goal
        // 1. Open the file: get references
        SharedPreferences prefGet = getSharedPreferences("DailyGoal", MODE_PRIVATE);
        //2. Read the value, default 0 if not strored
        //For circle chart
        int todayGoal = prefGet.getInt("new goal", 0);

        // Update the texts "consumed out of goal" and "XX%"
        TextView statusUpdateTextView = findViewById(R.id.statusUpdateTextView);
        TextView percentageTextView = findViewById(R.id.percentageTextView);
        statusUpdateTextView.setText((todayConsumption) + getString(R.string.ml_out_of) + todayGoal + getString(R.string.ml));

        // Calculate the slice size and update the pie chart:
        ProgressBar pieChart = findViewById(R.id.stats_progressbar);
        double d = (double) todayConsumption / (double) todayGoal;
        int progress = (int) (d * 100);
        pieChart.setProgress(progress);
        percentageTextView.setText((progress) + "%");
        Log.d("TEST", String.valueOf(todayGoal));

        //Save progress to share preference so it can be retrieved by notifications
        SharedPreferences prefPut = getSharedPreferences("Progress", MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefPut.edit();
        prefEditor.putInt("Progress", progress);
        prefEditor.apply();
    }

    /**
     * Search volume by day then in the onpostExecute method update the todayConsumption first then
     * performe updatechart()
     * References:
     * https://developer.android.com/reference/android/os/AsyncTask
     * */
    public class getVolume extends AsyncTask<Date, Integer, Integer> {
        UnitViewModel viewModel = new ViewModelProvider(MainActivity.this).get(UnitViewModel.class);

        @Override
        protected void onPostExecute(Integer integer) {
            Log.d("chart", "in main"+integer);
            todayConsumption=integer;
            updateChart();
            Log.d("chart", "in main today" + integer);
        }

        @Override
        protected Integer doInBackground(Date... dates) {
            if (viewModel.selectVolumeByDate(dates[0], dates[1]) == null) {
                return 0;
            } else {
                return viewModel.selectVolumeByDate(dates[0], dates[1]);
            }
        }
    }

    /*
    * An async task makes sure, that the foreground of an app does not freeze and cause the Android system
    * to forcefully close the app. Thus, heavier tasks are executed on their own thread in the background.
    * In this case, the consumption gets inserted into the database
    * References:
    * https://developer.android.com/reference/android/os/AsyncTask
    * */
    public class InsertConsumption extends AsyncTask<String, Void, Long> {
        @Override
        protected Long doInBackground(String... drinks) {
            UnitDatabase db = UnitDatabase.getDatabase(getApplicationContext());
            Consumption consumption = new Consumption(db.unitDao().getUnitByName(drinks[0]).getPrimaryKey(),Calendar.getInstance().getTime());
            return db.unitDao().insertConsumption(consumption);
            // returns the primary key of the newly created consumption
        }

        // starts a new activity only after it run the thread
        @Override
        protected void onPostExecute(Long id) {
            super.onPostExecute(id);
            spinner.setSelection(0);
            Intent intent = new Intent(MainActivity.this, AllDrinkList.class);
            intent.putExtra("message", "all");
            startActivity(intent);
        }
    }
}
//Todo: Add-hoc input