package fi.metropolia.simppa.watertracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;

import fi.metropolia.simppa.watertracker.database.Unit;

public class UnitActivity extends AppCompatActivity {

    EditText unitName;
    String strUnitName;
    EditText volume;
    int intVolume;
    String strVolume;
    Button addUnit;

    ArrayList<Unit> unitList;
    SharedPreferences mPrefs;
    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onButton();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);

        unitName = findViewById(R.id.unitName);
        volume = findViewById(R.id.volume);
        addUnit = findViewById(R.id.addUnit);

        //addUnit.setOnClickListener(click);
        //addUnit.setActivated(false);

        //unitList = new ArrayList<Unit>();

        //mPrefs = getPreferences(MODE_PRIVATE);
        //validateButton();

        addUnit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(unitName.getText()) || TextUtils.isEmpty((volume.getText()))){
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    strUnitName = unitName.getText().toString();
                    replyIntent.putExtra("Extra",strUnitName);
                    intVolume = Integer.parseInt(volume.getText().toString());
                    replyIntent.putExtra("extra", intVolume);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }

    public void onButton(){
        strUnitName = unitName.getText().toString();
        strVolume = volume.getText().toString();
        intVolume = Integer.parseInt(strVolume);

        unitList.add(new Unit(0,strUnitName,intVolume));

        Editor editor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(unitList.get(unitList.size()-1));
        editor.putString(strUnitName,json);

        editor.commit();

        unitName.getText().clear();
        volume.getText().clear();
    }
}
