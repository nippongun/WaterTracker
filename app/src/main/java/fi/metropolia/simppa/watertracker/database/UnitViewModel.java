package fi.metropolia.simppa.watertracker.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

public class UnitViewModel extends AndroidViewModel {

    private  UnitRepository repository;

    private LiveData<List<Unit>> unitList;
    private LiveData<List<Consumption>> consumptionList;//add consumptionList although it is UnitViewModel
    private LiveData<Unit> unit;//for getUnitById

    public void insertConsumption(Consumption con){
        repository.insertConsumption(con);

    }

    public UnitViewModel (Application application){
        super(application);

        repository = new UnitRepository(application);
        unitList = repository.getUnitList();
        consumptionList=repository.getConsumptionList();//get all consumption
    }

    public LiveData<Unit> getUnitById(int id) {
        return repository.getUnitById(id);

    }
    public LiveData<Unit> getUnitByName(String name) {
        unit=repository.getUnitByName(name);
        return unit;

    }

    public LiveData<List<Consumption>> getAllConsumption(){return consumptionList;}

    public LiveData<List<Unit>> getUnitList(){return unitList;}

    public void insertUnit(Unit unit) {repository.insertUnit(unit);}

    public void deleteUnit(Unit unit) {repository.deleteUnit(unit);}

    public LiveData<Integer> selectVolumeByDate(Date date){
        return repository.selectVolumeByDate(date);

    }

}
