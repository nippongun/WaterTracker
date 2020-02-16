package fi.metropolia.simppa.watertracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DailyGoalActivity extends AppCompatActivity {
    //public static final String MESSAGE = "com.example.myfirstapp.MESSAGE";
    private DailyGoal goal = new DailyGoal(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        final EditText editGoal = findViewById(R.id.editText);
        final TextView currentGoal = findViewById(R.id.currentGoal);
        final Button setButton = findViewById(R.id.buttonSetGoal);

        setButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                String message = editGoal.getText().toString();
                goal.setDailygoal(Integer.parseInt(message));
                currentGoal.setText(String.valueOf(goal.getDailygoal()) + " ml");

            }
        });

        //hideNavigationBar();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //hideNavigationBar();
    }

    /** private void hideNavigationBar() {
     this.getWindow().getDecorView()
     .setSystemUiVisibility(
     View.SYSTEM_UI_FLAG_FULLSCREEN |
     View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
     View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
     View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
     View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
     View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
     }*/
}
