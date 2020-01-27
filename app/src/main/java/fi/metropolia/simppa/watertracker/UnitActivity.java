package fi.metropolia.simppa.watertracker;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import fi.metropolia.simppa.watertracker.water.Unit;

public class UnitActivity extends AppCompatActivity {

    EditText unitName;
    String strUnitName;
    EditText volume;
    int intVolume;
    String strVolume;
    Button addUnit;

    ArrayList<Unit> unitList;
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

        addUnit.setOnClickListener(click);
        //addUnit.setActivated(false);

        unitList = new ArrayList<Unit>();

        //validateButton();
    }

    public void onButton(){
        strUnitName = unitName.getText().toString();
        strVolume = volume.getText().toString();
        intVolume = Integer.getInteger(strVolume);

        unitList.add(new Unit(strUnitName,intVolume));

        unitName.getText().clear();
        volume.getText().clear();
    }

    private boolean validateButton() {
        if (TextUtils.isEmpty(strUnitName)) {
            unitName.setError("This field cannot be empty");
            return false;
        }
        if (TextUtils.isEmpty(strVolume)) {
            unitName.setError("This field cannot be empty");
            return false;
        }
        return true;
    }
}
