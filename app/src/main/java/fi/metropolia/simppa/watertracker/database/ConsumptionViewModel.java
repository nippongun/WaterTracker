package fi.metropolia.simppa.watertracker.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ConsumptionViewModel extends AndroidViewModel {

    private  UnitRepository repository;

    private LiveData<List<Consumption>> consumptionList;//add consumptionList although it is UnitViewModel

    public ConsumptionViewModel (Application application){
        super(application);
        repository = new UnitRepository(application);
        consumptionList=repository.getConsumptionList();//get all consumption
    }
    public LiveData<List<Consumption>> getAllConsumption(){
        return consumptionList;
    }
    public LiveData<Unit> getUnitById(long id) {
        return repository.getUnitById(id);
    }
}
