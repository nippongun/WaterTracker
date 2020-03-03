package fi.metropolia.simppa.watertracker;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DailyGoalActivity extends AppCompatActivity {
    private DailyGoal goal = new DailyGoal(0);
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        final Button setButton = findViewById(R.id.button_setgoal);
        displayGoal();
        setButton.setOnClickListener(v -> {
            handleInput();
            finish();
        });
    }
    /**
     * Collects users input and validates and saves it
     */
    private void handleInput() {
        final EditText editGoal = findViewById(R.id.editText_editdailygoal);
        if (TextUtils.isEmpty(editGoal.getText())) {
            setResult(RESULT_CANCELED);
            Toast.makeText(getApplicationContext(), R.string.daily_goal_isEmpty, Toast.LENGTH_LONG).show();
        } else {
            message = editGoal.getText().toString();
            goal.setDailygoal(Integer.parseInt(message));
            saveGoal();
        }
    }
    /**
     * Displays current daily goal
     */
    private void displayGoal() {
        final TextView currentGoal = findViewById(R.id.textView_actualcurrentgoal);
        SharedPreferences prefGet = getSharedPreferences("DailyGoal", Activity.MODE_PRIVATE);
        int onCreateSaved = prefGet.getInt("new goal", 0);
        String text = onCreateSaved + " ml";
        currentGoal.setText(text);
    }

    /**
     * Saves new goal to shared preferences
     */
    private void saveGoal() {
        SharedPreferences prefPut = getSharedPreferences("DailyGoal", Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefPut.edit();
        prefEditor.putInt("new goal", Integer.parseInt(message));
        prefEditor.apply();
    }
}
