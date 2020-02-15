package fi.metropolia.simppa.watertracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    Button b1, b2, b3;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = findViewById(R.id.button);
        b2 = findViewById(R.id.button2);
        b3 = findViewById(R.id.setDailyGoalButton);
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
