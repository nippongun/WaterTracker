package fi.metropolia.simppa.watertracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import fi.metropolia.simppa.watertracker.water.Unit;

public class ShowList extends AppCompatActivity {
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);


        File destination = new File("data/data/fi.metropolia.simppa.watertracker/shared_prefs/UnitActivity.xml");
        BufferedReader br = new BufferedReader( new FileReader(destination));
        Gson gson = new Gson();
        ArrayList<Unit> unitList = gson.fromJson()
        lv = (ListView) findViewById(R.id.unitListView);

        lv.setAdapter( new ArrayAdapter<President>(
                this,
                R.layout.unit_item_layout,
                PresidentList.getInstance().getPresidents()));
    }

}
