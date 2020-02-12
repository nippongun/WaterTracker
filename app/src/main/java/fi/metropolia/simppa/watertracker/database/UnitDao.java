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
    void insertUnit(Unit unit);

    @Query("DELETE FROM unit_table")
    void deleteAll();

    @Query("SELECT * FROM unit_table")
    LiveData<List<Unit>> getUnitList();

    @Query("SELECT * FROM CONSUMPTION_TABLE")
    LiveData<List<Consumption>> getConsumptionList();

    @Delete
    void deleteUnit(Unit unit);
}
