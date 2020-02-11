package fi.metropolia.simppa.watertracker.database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fi.metropolia.simppa.watertracker.R;

public class UnitListAdapter extends RecyclerView.Adapter<UnitListAdapter.UnitViewHolder> {
    class UnitViewHolder extends RecyclerView.ViewHolder{
        private final TextView unitItemView;

        private UnitViewHolder(View view){
            super(view);
            unitItemView = itemView.findViewById(R.id.textView);
        }
    }

    private final LayoutInflater inflater;
    private List<Unit> units;

    public UnitListAdapter(Context context) {inflater = LayoutInflater.from(context);}

    @Override
    public UnitViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = inflater.inflate(R.layout.recyclerview_item, parent,false);
        return new UnitViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UnitViewHolder holder, int position){
        if (units != null){
            Unit current = units.get(position);
            holder.unitItemView.setText(current.getUnitName());
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
