package fi.metropolia.simppa.watertracker;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DailyGoalActivity extends AppCompatActivity {
    private DailyGoal goal = new DailyGoal(0);

    /**
     * @onCreate 1. Displays current daily goal
     * 2. Collects users input about modified daily goal
     * 3. Saves the new value in shared preference
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        final EditText editGoal = findViewById(R.id.editText_editdailygoal);
        final TextView currentGoal = findViewById(R.id.textView_actualcurrentgoal);
        final Button setButton = findViewById(R.id.button_setgoal);

        //Display correct daily goal
        // 1. Open the file: get references
        SharedPreferences prefGet = getSharedPreferences("DailyGoal", Activity.MODE_PRIVATE);
        //2. Read the value, default 0 if not strored
        int onCreateSaved = prefGet.getInt("new goal", 0);
        //3. Display stored goal
        currentGoal.setText(String.valueOf(onCreateSaved) + " ml");

        setButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // text must not be empty
                if(TextUtils.isEmpty(editGoal.getText())){
                    Toast.makeText(getApplicationContext(),R.string.daily_goal_isEmpty, Toast.LENGTH_LONG).show();

                    // Make sure the data is not too large
                } else if(Integer.parseInt(editGoal.getText().toString()) >= 10000){
                    Toast.makeText(getApplicationContext(),R.string.daily_goal_too_large, Toast.LENGTH_LONG).show();

                    // ... or too small/negative
                } else if(Integer.parseInt(editGoal.getText().toString()) <= 1000){
                    Toast.makeText(getApplicationContext(),R.string.daily_goal_too_small, Toast.LENGTH_LONG).show();
                }
                else{

                    String message = editGoal.getText().toString();
                    goal.setDailygoal(Integer.parseInt(message));
                    //currentGoal.setText(String.valueOf(goal.getDailygoal()) + " ml");


                    //Store new Daily Goal to shared preferences
                    // 1. Open the file: get reference
                    SharedPreferences prefPut = getSharedPreferences("DailyGoal", Activity.MODE_PRIVATE);
                    //2. Open the editor to be able to define what is added to shared preferences
                    SharedPreferences.Editor prefEditor = prefPut.edit();
                    //3. Put the key value pairs
                    prefEditor.putInt("new goal", Integer.parseInt(message));
                    //4. Save the changes by commit
                    prefEditor.commit();


                    //Display ne value on the screen
                    // 1. Open the file: get references
                    SharedPreferences prefGet = getSharedPreferences("DailyGoal", Activity.MODE_PRIVATE);
                    //2. Read the value, default 0 if not strored
                    int onCreateSaved = prefGet.getInt("new goal", 0);
                    //3. Display stored goal
                    currentGoal.setText(String.valueOf(onCreateSaved) + " ml");
                    // responf to the user, whether changes are successful
                    Toast.makeText(getApplicationContext(),R.string.changes, Toast.LENGTH_LONG).show();
                }
                finish();
            }
        });
    }
}

