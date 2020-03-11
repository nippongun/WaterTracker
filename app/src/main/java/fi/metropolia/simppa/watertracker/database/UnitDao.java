package fi.metropolia.simppa.watertracker.database;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

/*
* The Data accesss object provides provides an interface between the Room database and
* the queries. It also allows to create own queries for various tasks.
* See the UnitDatabase class for resources and references
* */

@Dao
public interface UnitDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertUnit(Unit unit);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertConsumption(Consumption consumption);

    @Query("SELECT * FROM unit_table")
    LiveData<List<Unit>> getUnitList();

    @Query("SELECT * FROM CONSUMPTION_TABLE ORDER BY timestamp DESC")
    LiveData<List<Consumption>> getConsumptionList();

    @Query("SELECT * FROM unit_table WHERE primary_Key=:id" )
    LiveData<Unit> getUnitByIdLive(long id);
    @Query("SELECT * FROM unit_table WHERE unit_name=:name" )
    Unit getUnitByName(String name);

    // Get the volume between two days, primarily for an entire day
    @Query("SELECT SUM(volume) FROM consumption_table INNER JOIN unit_table ON unit_table.primary_key=consumption_table.foreign_unit_key WHERE consumption_table.timestamp BETWEEN :from AND :to")
    Integer selectVolumByDate(Date from,Date to);

}
