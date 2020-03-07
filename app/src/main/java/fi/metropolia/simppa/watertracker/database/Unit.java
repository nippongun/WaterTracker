package fi.metropolia.simppa.watertracker.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/*
* Entity class for the units. Contains a name, volume and an auto-generated primary key
* References:
* https://developer.android.com/training/data-storage/room/defining-data
* */
@Entity(tableName = "unit_table")
public class Unit {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "primary_key")
    private long primaryKey;
    @NonNull
    @ColumnInfo(name = "unit_name")
    private String unitName;
    @ColumnInfo(name = "volume")
    private int volume;

    public Unit( @NonNull String unitName, int volume){
        this.unitName = unitName;
        this.volume = volume;
    }

    public long getPrimaryKey() {
        return primaryKey;
    }

    public String getUnitName() {
        return unitName;
    }

    public int getVolume() {
        return volume;
    }

    public void setPrimaryKey(long primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setUnitName(@NonNull String unitName) {
        this.unitName = unitName;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String toString() {
        return unitName + volume;
    }
}
