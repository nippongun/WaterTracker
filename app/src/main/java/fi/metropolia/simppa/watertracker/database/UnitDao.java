package fi.metropolia.simppa.watertracker.database;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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

    @Query("SELECT SUM(volume) FROM unit_table,consumption_table WHERE primary_key=foreign_unit_key AND timestamp= :date")
    default LiveData<Integer> selectVolumByDate(Date date) {
        return null;
    }
}
