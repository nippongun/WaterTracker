package fi.metropolia.simppa.watertracker;

import fi.metropolia.simppa.watertracker.database.Consumption;
import fi.metropolia.simppa.watertracker.database.Unit;
import fi.metropolia.simppa.watertracker.database.UnitDatabase;
import fi.metropolia.simppa.watertracker.database.UnitViewModel;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int todayConsumption = 0; //For circle chart
    private int todayGoal; //For circle chart
    private Intent intent;
    private ArrayList<String> unitNameList = new ArrayList<>();
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setNotifications();
        updateChartConsumption();

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


        spinner.setSelection(0);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] unitName = spinner.getSelectedItem().toString().split(" ");
                Log.d("MAIN", "what? " + unitName[0]);
                //UnitDatabase db = UnitDatabase.getDatabase(getApplicationContext());
                if (!unitName[0].equals("SELECT")) {
                    InsertConsumption ic = new InsertConsumption();
                    ic.execute(unitName[0]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });//end of the spinner listener
    }

    @Override
    public void onStart() {
        super.onStart();
        updateChartConsumption();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateChartConsumption();
    }

    public class InsertConsumption extends AsyncTask<String, Void, Long> {
        @Override
        protected Long doInBackground(String... drinks) {
            UnitDatabase db = UnitDatabase.getDatabase(getApplicationContext());
            Consumption consumption = new Consumption(db.unitDao().getUnitByName(drinks[0]).getPrimaryKey(), Calendar.getInstance().getTime());
            long res = db.unitDao().insertConsumption(consumption);
            Log.d("test", "" + res);
            return res;
            //return db.unitDao().insertConsumption(drinks[0]);
        }

        @Override
        protected void onPostExecute(Long id) {
            super.onPostExecute(id);
            //new id exist :)
            spinner.setSelection(0);
            Intent intent = new Intent(MainActivity.this, AllDrinkList.class);
            intent.putExtra("message", "all");
            startActivity(intent);
        }
    }

    public void onButton(View view) {
        if (view.getId() == R.id.button_addunit) {
            intent = new Intent(this, ShowList.class);
        } else if (view.getId() == R.id.button_dailygoal) {
            intent = new Intent(this, DailyGoalActivity.class);
        } else if (view.getId() == R.id.button_chart) {
            intent = new Intent(this, Chart.class);
        }
        startActivity(intent);
    }

    /**
     * Updates the pie chart with daily goal set by the user
     */
    private void updateChartGoal() {
        // Get latest daily goal
        //1. Open the file: get references
        SharedPreferences prefGet = getSharedPreferences("DailyGoal", Activity.MODE_PRIVATE);
        //2. Read the value, default 0 if not strored
        todayGoal = prefGet.getInt("new goal", 0);
        // Update the texts "consumed out of goal" and "XX%"
        TextView statusUpdateTextView = findViewById(R.id.statusUpdateTextView);
        String text = todayConsumption + " ml out of " + todayGoal + " ml";
        statusUpdateTextView.setText(text);
    }

    /**
     * Updates the pie chart and related text view with information about how many % of the daily goal has been fulfilled
     */
    private void updateChartProgress() {
        // Calculate the slice size and update the pie chart:
        TextView percentageTextView = findViewById(R.id.percentageTextView);
        ProgressBar pieChart = findViewById(R.id.stats_progressbar);
        double d = (double) todayConsumption / (double) todayGoal;
        int progress = (int) (d * 100);
        pieChart.setProgress(progress);
        String text = progress + "%";
        percentageTextView.setText(text);

        //Save progress to share preference so it can be retrieved by notifications
        SharedPreferences prefPut = getSharedPreferences("Progress", Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefPut.edit();
        prefEditor.putInt("Progress", progress);
        prefEditor.apply();
    }

    /**
     * Updates pie chart and text view with how much water has been consumed that day
     */
    private void updateChartConsumption() {
        getVolume gv = new getVolume();
        //get today's year month and day then set Date from as the bigining of the day, Date to as the end of the day.
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day, 0, 0, 0);
        Date from = cal.getTime();
        cal.set(year, month, day, 23, 59, 59);
        Date to = cal.getTime();
        //get the whole day's volume and set to chart
        gv.execute(from, to);
    }

    /**
     * Configures when are notifications send
     */
    private void setNotifications() {
        Intent intents = new Intent(MainActivity.this, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intents, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //Set the alarm to start at approximately 2:00 p.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 13);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
        createNotificationChannel();
    }

    /**
     * Sets channel for notifications
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String description = getString(R.string.channel_description);
            NotificationChannel channel = new NotificationChannel("notifyUser", "Reminders", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Search volume by day then in the onpostExecute method update the todayConsumption first then
     * performe updatechart()
     */
    public class getVolume extends AsyncTask<Date, Integer, Integer> {
        UnitViewModel viewModel = new ViewModelProvider(MainActivity.this).get(UnitViewModel.class);

        @Override
        protected void onPostExecute(Integer integer) {
            Log.d("chart", "in main" + integer);
            todayConsumption = integer;
            updateChartGoal();
            updateChartProgress();
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
}
