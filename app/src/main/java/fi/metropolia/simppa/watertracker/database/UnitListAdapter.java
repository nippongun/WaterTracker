package fi.metropolia.simppa.watertracker.database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fi.metropolia.simppa.watertracker.R;

/*
* The adapter for the list of units.
* References:
* https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter
* https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example
* */
public class UnitListAdapter extends RecyclerView.Adapter<UnitListAdapter.UnitViewHolder> {

    /*
    * Creates the blueprint for the unit item views, which are used later during the process.
    * */
    class UnitViewHolder extends RecyclerView.ViewHolder{
        private final TextView unitItemView;
        private final TextView volumeItemView;
        private UnitViewHolder(View view){
            super(view);
            unitItemView = view.findViewById(R.id.textView);
            volumeItemView = view.findViewById(R.id.volumeView);
        }
    }

    private final LayoutInflater inflater;
    private List<Unit> units;

    public UnitListAdapter(Context context) {inflater = LayoutInflater.from(context);}

    /*
    * Holds the data in a view and gets the specific layout.
    * */
    @Override
    public UnitViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = inflater.inflate(R.layout.recyclerview_item, parent,false);
        return new UnitViewHolder(itemView);
    }

    /*
    * Binds the current unit of the list on the certain position. Sets also the text for the
    * text views.
    * */
    @Override
    public void onBindViewHolder(UnitViewHolder holder, int position){
        if (units != null){
            Unit current = units.get(position);
            holder.unitItemView.setText(current.getUnitName());
            holder.volumeItemView.setText(Integer.toString(current.getVolume()));
        } else {
         holder.unitItemView.setText("No unit");
        }
    }

    public void setUnits(List<Unit> units){
        this.units = units;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        if(this.units != null){
            return  units.size();
        }
        else  return 0;
    }
}
