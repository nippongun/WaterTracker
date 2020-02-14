package fi.metropolia.simppa.watertracker.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class UnitViewModel extends AndroidViewModel {

    private  UnitRepository repository;

    private LiveData<List<Unit>> unitList;

    public UnitViewModel (Application application){
        super(application);

        repository = new UnitRepository(application);
        unitList = repository.getUnitList();
    }

    public LiveData<List<Unit>> getUnitList(){return unitList;}

    public void insertUnit(Unit unit) {repository.insertUnit(unit);}

    public void deleteUnit(Unit unit) {repository.deleteUnit(unit);}
}
