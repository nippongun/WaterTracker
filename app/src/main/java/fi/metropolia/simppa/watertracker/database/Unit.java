package fi.metropolia.simppa.watertracker.database;

//public enum DrinkType {Water, Soda, Juice}

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "unit_table")
public class Unit {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "primary_key")
    private int primaryKey;
    @NonNull
    @ColumnInfo(name = "unit_name")
    private String unitName;
    @ColumnInfo(name = "volume")
    private int volume;

    public Unit(@NonNull int primaryKey, @NonNull String unitName, int volume){
        this.primaryKey = primaryKey;
        this.unitName = unitName;
        this.volume = volume;
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public String getUnitName() {
        return unitName;
    }

    public int getVolume() {
        return volume;
    }

    public String toString() {
        return unitName + volume;
    }
}
