package fi.metropolia.simppa.watertracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import fi.metropolia.simppa.watertracker.database.Unit;

public class UnitActivity extends AppCompatActivity {

    //messages to save the volume and unit name
    public static final String EXTRA_MESSAGE_UNIT_NAME = "fi.metropolia.simppa.watertracker.UNIT_NAME";
    public static final String EXTRA_MESSAGE_VOLUME = "fi.metropolia.simppa.watertracker.VOLUME";

    EditText unitName;
    String strUnitName;
    EditText volume;
    int intVolume;
    String strVolume;
    Button addUnit;

    ArrayList<Unit> unitList;
    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);

        volume = findViewById(R.id.textEdit_volume);
        unitName = findViewById(R.id.textEdit_unitname);

        final Button button = findViewById(R.id.button_addUnit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent replyIntent = new Intent();
                // If one of text fields are empty
                if(TextUtils.isEmpty(volume.getText()) || TextUtils.isEmpty(unitName.getText())){
                     setResult(RESULT_CANCELED, replyIntent);
                     // send the message CANCELED, which do not insert any unit to the db
                } else {
                    // otherwise prepare to send the data to
                    strUnitName = unitName.getText().toString();
                    strVolume = volume.getText().toString();
                    // put the name and volme into a message.
                    replyIntent.putExtra(EXTRA_MESSAGE_UNIT_NAME, strUnitName );
                    replyIntent.putExtra(EXTRA_MESSAGE_VOLUME, Integer.parseInt(strVolume) );
                    setResult(RESULT_OK, replyIntent);
                }
                // close the activity since there is no need anymore to keep it open.
                finish();
            }
        });
    }
}
