package fi.metropolia.simppa.watertracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Preconditions;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.metropolia.simppa.watertracker.database.Consumption;
import fi.metropolia.simppa.watertracker.database.ConsumptionViewModel;
import fi.metropolia.simppa.watertracker.database.Unit;



/**
 * This class will get all the records of consumptions in the database and use a recyclerView to
 * display them
 * */

public class AllDrinkList extends AppCompatActivity {
    private String intentValue, consumpItem;

    public void btOnclick(View view) {
        Intent intent = new Intent(this, Chart.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_drink_list);

        Intent intent = getIntent();
        intentValue = intent.getStringExtra("message");
        //for all the consumption records, will be implement by day or month etc in the next version
        if (intentValue.equals("all")) {

            ConsumptionViewModel viewModel = new ViewModelProvider(this).get(ConsumptionViewModel.class);
            viewModel.getAllConsumption().observe(this, new Observer<List<Consumption>>() {
                @Override
                public void onChanged(List<Consumption> consumptions) {
                    ArrayList<String> itemList = new ArrayList<>();
                    ArrayList<String> volumList = new ArrayList<>();

                    for (Consumption con : consumptions) {
                        viewModel.getUnitById(con.getForeigenUnitKey()).observe(AllDrinkList.this, new Observer<Unit>() {
                            @Override
                            public void onChanged(Unit unit) {
                                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                String formattedDate = df.format(con.getTimeStamp());

                                consumpItem = formattedDate + "     " + unit.getUnitName();
                                itemList.add(consumpItem);
                                volumList.add("" + unit.getVolume() + " ml");
                                Log.d("my", "size of itemList" + itemList.size());
                                Log.d("my", "size of volume" + volumList.size());

                                // Bug fix from Patricie: delete the dummy entry or any other 0 volume entry from the list
                                for (int i = 0; i < volumList.size(); i++) //Inner for loop
                                {
                                    // Delete entries with volume = 0
                                    if (volumList.get(i).equals("0 ml"))   //Comparison If. Is I+1 greater than I?
                                    {
                                        //delete from entries
                                        itemList.remove(i);
                                        volumList.remove(i);
                                    }
                                }
                                //End of bug fix

                                //Bug fix from Patricie: this should order the consumption list by date.
                                //https://www.dreamincode.net/forums/topic/348658-linking-elements-of-two-arraylists-together-for-a-bubble-sort/
                                String tempName;  //temp variable for name sort
                                String tempVolume; //temp variable for grade sortfor (int x = 0; x < itemList.size() - 1; x++)  //Outer for loop

                                for (int i = 0; i < itemList.size()-1; i++) //Inner for loop
                                {
                                    //Sort lists by date
                                    if (itemList.get(i).compareTo(itemList.get(i + 1)) < 0)   //Comparison If. Is I+1 greater than I?
                                    {
                                        //dates list
                                        tempName = itemList.get(i);           //put top element in temp
                                        itemList.set(i, itemList.get(i + 1)); //put second element in top slot
                                        itemList.set(i + 1, tempName);          //put top element in 2nd slot

                                        //and in the same way volumes list
                                        tempVolume = volumList.get(i);
                                        volumList.set(i, volumList.get(i + 1));
                                        volumList.set(i + 1, tempVolume);
                                    }
                                }
                                //End of sorting


                                RecyclerView recyclerView = findViewById(R.id.dinkList_recyclerView);
                                recyclerView.setLayoutManager(new LinearLayoutManager(AllDrinkList.this));
                                MyAdapter myAdapter = new MyAdapter();
                                myAdapter.setMyData(itemList, volumList);
                                recyclerView.setAdapter(myAdapter);
                            }
                        });
                    }
                }
            });
        }
    }
}
