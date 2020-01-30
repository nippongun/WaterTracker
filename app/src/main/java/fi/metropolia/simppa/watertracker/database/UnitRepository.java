package fi.metropolia.simppa.watertracker.database;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Database;

import java.util.List;

public class UnitRepository {
    private  UnitDao unitDao;
    private LiveData<List<Unit>> unitList;
    private LiveData<List<Consumption>> consumptionList;
    UnitRepository(Application application){
        UnitDatabase db = UnitDatabase.getDatabase(application);
        unitDao = db.unitDao();
        unitList = unitDao.getUnitList();
        consumptionList = unitDao.getConsumptionList();
    }

    public LiveData<List<Unit>> getUnitList() {
        return unitList;
    }

    public LiveData<List<Consumption>> getConsumptionList() {
        return consumptionList;
    }

    void insertUnit(Unit unit){
        UnitDatabase.databaseWriterExecutor.execute(()->{
            unitDao.insert(unit);
        });
    }
}
