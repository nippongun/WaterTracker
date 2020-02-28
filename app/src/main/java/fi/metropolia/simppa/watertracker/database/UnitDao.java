package fi.metropolia.simppa.watertracker.database;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Dao
public interface UnitDao {



    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUnit(Unit unit);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertConsupmtion(Consumption consumption);

    @Query("DELETE FROM unit_table")
    void deleteAll();

    @Query("SELECT * FROM unit_table")
    LiveData<List<Unit>> getUnitList();

    @Query("SELECT * FROM CONSUMPTION_TABLE ORDER BY timestamp DESC")
    LiveData<List<Consumption>> getConsumptionList();

    @Delete
    void deleteUnit(Unit unit);

    @Query("SELECT * FROM unit_table WHERE primary_Key=:id" )
    LiveData<Unit> getUnitById(int id);
    @Query("SELECT * FROM unit_table WHERE unit_name=:name" )
    LiveData<Unit> getUnitByName(String name);

    @Query("SELECT SUM(volume) FROM consumption_table INNER JOIN unit_table ON unit_table.primary_key=consumption_table.foreign_unit_key WHERE consumption_table.timestamp BETWEEN :from AND :to")
    Integer selectVolumByDate(Date from,Date to);
}
