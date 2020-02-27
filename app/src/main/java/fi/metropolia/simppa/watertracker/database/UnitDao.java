package fi.metropolia.simppa.watertracker.database;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UnitDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertUnit(Unit unit);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertConsumption(Consumption consumption);

    @Query("DELETE FROM unit_table")
    void deleteAll();

    @Query("SELECT * FROM unit_table")
    LiveData<List<Unit>> getUnitList();

    @Query("SELECT * FROM CONSUMPTION_TABLE ORDER BY timestamp DESC")
    LiveData<List<Consumption>> getConsumptionList();

    @Delete
    void deleteUnit(Unit unit);

    @Query("SELECT * FROM unit_table WHERE primary_Key=:id" )
    Unit getUnitById(long id);
    @Query("SELECT * FROM unit_table WHERE primary_Key=:id" )
    LiveData<Unit> getUnitByIdLive(long id);
    @Query("SELECT * FROM unit_table WHERE unit_name=:name" )
    Unit getUnitByName(String name);
}
