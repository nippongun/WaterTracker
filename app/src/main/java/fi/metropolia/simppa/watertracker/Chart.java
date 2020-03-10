package fi.metropolia.simppa.watertracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import fi.metropolia.simppa.watertracker.database.UnitViewModel;

public class Chart extends AppCompatActivity {

    int year=Calendar.getInstance().get(Calendar.YEAR);
    int month= Calendar.getInstance().get(Calendar.MONTH);
    int day=Calendar.getInstance().get(Calendar.DAY_OF_MONTH);



    /**
     * to find out the volume by date.
     * **/

    public class GetVolume extends AsyncTask<Date, Void, List<Integer>> {

        private List<Integer> volumeList= new ArrayList<>();
        UnitViewModel viewModel = new ViewModelProvider(Chart.this).get(UnitViewModel.class);

        private Date addDays(Date date)
        {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, -1); //minus number would decrement the days
            return cal.getTime();
        }

        /***
         * set the volume list returned from doInBackground()  to the chart and rendering it
         */


        @Override
        protected void onPostExecute(List<Integer> integes) {
            Calendar cal=Calendar.getInstance();
            cal.set(year,month,day,0,0,0);


            List<DataEntry> data = new ArrayList<>();
            data.add(new ValueDataEntry(""+cal.get(Calendar.DAY_OF_MONTH)+"/"+(cal.get(Calendar.MONTH)+1), integes.get(0)));
            cal.add(Calendar.DATE, -1);//change the date to one day before
            data.add(new ValueDataEntry(""+cal.get(Calendar.DAY_OF_MONTH)+"/"+(cal.get(Calendar.MONTH)+1), integes.get(1)));
            cal.add(Calendar.DATE, -1);//change the date to one day before
            data.add(new ValueDataEntry(""+cal.get(Calendar.DAY_OF_MONTH)+"/"+(cal.get(Calendar.MONTH)+1), integes.get(2)));
            cal.add(Calendar.DATE, -1);//change the date to one day before
            data.add(new ValueDataEntry(""+cal.get(Calendar.DAY_OF_MONTH)+"/"+(cal.get(Calendar.MONTH)+1), integes.get(3)));
            cal.add(Calendar.DATE, -1);//change the date to one day before
            data.add(new ValueDataEntry(""+cal.get(Calendar.DAY_OF_MONTH)+"/"+(cal.get(Calendar.MONTH)+1), integes.get(4)));
            Collections.reverse(data);
            chartRendering(data);
        }
        /**
         * get the volume by date if there is no record will return 0
         */

        @Override
        protected List<Integer> doInBackground(Date... dates) {
            if(viewModel.selectVolumeByDate(dates[0],dates[1])==null) {
                volumeList.add(0);
            }else{
                volumeList.add(viewModel.selectVolumeByDate(dates[0],dates[1]));

            }
            dates[0]=addDays(dates[0]);
            dates[1]=addDays(dates[1]);
            if(viewModel.selectVolumeByDate(dates[0],dates[1])==null) {
                volumeList.add(0);
            }else{
                volumeList.add(viewModel.selectVolumeByDate(dates[0],dates[1]));

            }
            dates[0]=addDays(dates[0]);
            dates[1]=addDays(dates[1]);
            if(viewModel.selectVolumeByDate(dates[0],dates[1])==null) {
                volumeList.add(0);
            }else{
                volumeList.add(viewModel.selectVolumeByDate(dates[0],dates[1]));

            }
            dates[0]=addDays(dates[0]);
            dates[1]=addDays(dates[1]);
            if(viewModel.selectVolumeByDate(dates[0],dates[1])==null) {
                volumeList.add(0);
            }else{
                volumeList.add(viewModel.selectVolumeByDate(dates[0],dates[1]));

            }
            dates[0]=addDays(dates[0]);
            dates[1]=addDays(dates[1]);
            if(viewModel.selectVolumeByDate(dates[0],dates[1])==null) {
                volumeList.add(0);
            }else{
                volumeList.add(viewModel.selectVolumeByDate(dates[0],dates[1]));

            }
            Log.d("chart","size of volumList "+volumeList.size());

            return volumeList;
        }

    }



    /**
     * start everything
     * **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year,month,day,0,0,0);
        Date from=cal.getTime();
        cal.set(year,month,day,23,59,59);
        Date to=cal.getTime();





        GetVolume gv= new GetVolume();

        gv.execute(from,to);
















    }

    /**
     * the actually chart rendering method the data set as parameter
    * */

    public void chartRendering( List<DataEntry> data ){
        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));
        Cartesian cartesian = AnyChart.column();
        Column column = cartesian.column(data);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("ml {%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title("Recent 5 days water intake");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("ml {%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Date");
        cartesian.yAxis(0).title("Water Intake Amount");

        anyChartView.setChart(cartesian);

    }


}
