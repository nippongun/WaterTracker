package fi.metropolia.simppa.watertracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import fi.metropolia.simppa.watertracker.database.Consumption;
import fi.metropolia.simppa.watertracker.database.ConsumptionViewModel;
import fi.metropolia.simppa.watertracker.database.Unit;
import fi.metropolia.simppa.watertracker.database.UnitViewModel;

public class AllDrinkList extends AppCompatActivity {
    private String intentValue, consumpItem;



    public void btOnclick(View view){

        Intent intent= new Intent(this, Chart.class);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_drink_list);




        Intent intent=getIntent();
        intentValue=intent.getStringExtra("message");

        if(intentValue.equals("all")){

            ConsumptionViewModel viewModel= new ViewModelProvider(this).get(ConsumptionViewModel.class);
            viewModel.getAllConsumption().observe(this,new Observer<List<Consumption>>() {
                @Override
                public void onChanged(List<Consumption> consumptions) {
                    ArrayList<String> itemList= new ArrayList<>();
                    ArrayList<String> volumList= new ArrayList<>();

                    for(Consumption con:consumptions){
                        viewModel.getUnitById(con.getForeigenUnitKey()).observe(AllDrinkList.this, new Observer<Unit>() {
                            @Override
                            public void onChanged(Unit unit) {
                                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                String formattedDate = df.format(con.getTimeStamp());

                                consumpItem= formattedDate+"     "+unit.getUnitName();
                                itemList.add(consumpItem);

                                volumList.add(""+unit.getVolume()+" ml");
                                Log.d("my","size of itemList"+itemList.size());
                                Log.d("my","size of volume"+volumList.size());

                                RecyclerView recyclerView= findViewById(R.id.dinkList_recyclerView);
                                recyclerView.setLayoutManager(new LinearLayoutManager(AllDrinkList.this));
                                MyAdapter myAdapter= new MyAdapter();
                                myAdapter.setMyData(itemList,volumList);
                                recyclerView.setAdapter(myAdapter);

                            }
                        });
                    }





                }
            });


        }

    }
}
