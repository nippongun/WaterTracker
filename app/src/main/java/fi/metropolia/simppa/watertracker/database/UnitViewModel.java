package fi.metropolia.simppa.watertracker.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class UnitViewModel extends AndroidViewModel {

    private  UnitRepository repository;

    private LiveData<List<Unit>> unitList;
    private LiveData<List<Consumption>> consumptionList;//add consumptionList although it is UnitViewModel

    public UnitViewModel (Application application){
        super(application);
        repository = new UnitRepository(application);
        consumptionList=repository.getConsumptionList();//get all consumption
        unitList = repository.getUnitList();
    }

    /*public LiveData<Unit> getUnitById(long id) {
        return repository.getUnitById(id);
    }
    public LiveData<Unit> getUnitByName(String name) {
        return repository.getUnitByName(name);
    }*/

    public LiveData<List<Unit>> getUnitList(){return unitList;}

    public void insertUnit(Unit unit) {repository.insertUnit(unit);}

    /*public void insertConsumption(Consumption con){
        repository.insertConsumption(con);
    }*/
}
