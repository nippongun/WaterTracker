package fi.metropolia.simppa.watertracker.database;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Database;

import java.util.Date;
import java.util.List;

public class UnitRepository {
    private  UnitDao unitDao;
    private LiveData<List<Unit>> unitList;
    private LiveData<List<Consumption>> consumptionList;

    private LiveData<Unit> unit;
    UnitRepository(Application application){
        UnitDatabase db = UnitDatabase.getDatabase(application);
        unitDao = db.unitDao();
        unitList = unitDao.getUnitList();
        consumptionList = unitDao.getConsumptionList();

    }
    /*public LiveData<Unit> getUnitByName(String name){
        return unitDao.getUnitByName(name);
    }*/

    public LiveData<Unit> getUnitById(long id){
        return unitDao.getUnitByIdLive(id);
    }

    public LiveData<List<Unit>> getUnitList() {
        return unitList;
    }

    public LiveData<List<Consumption>> getConsumptionList() {
        return consumptionList;
    }

    void insertUnit(Unit unit){
        UnitDatabase.databaseWriterExecutor.execute(()->{
             unitDao.insertUnit(unit);
        });
    }
    /*void insertConsumption(Consumption con){
        UnitDatabase.databaseWriterExecutor.execute(()->{
            unitDao.insertConsupmtion(con);
        });
    }

    void deleteUnit(Unit unit){
        UnitDatabase.databaseWriterExecutor.execute(()->{
            unitDao.deleteUnit(unit);
        });

    }*/



    public Integer selectVolumeByDate(Date from,Date to){
        return unitDao.selectVolumByDate(from,to);

    }
}
