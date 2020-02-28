package fi.metropolia.simppa.watertracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<String> myData;
    private List<String> date;



    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView mytextView;
        private TextView date;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mytextView=itemView.findViewById(R.id.textView);
            date=itemView.findViewById(R.id.volumeView);
        }
    }

    public void  setMyData(List<String> dataList,List<String> date){
        this.myData=dataList;
        this.date=date;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View oneView= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,parent,false);
       return new MyViewHolder(oneView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String temp=myData.get(position);
        holder.mytextView.setText(temp);
        String tempTime=date.get(position);
        holder.date.setText(tempTime);
    }

    @Override
    public int getItemCount() {

        return myData.size();
    }
}
