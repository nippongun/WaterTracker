package fi.metropolia.simppa.watertracker.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "consumption_table", foreignKeys = @ForeignKey(entity = Unit.class, parentColumns = "primaryKey", childColumns = "foreignUnitKey"))
public class Consumption {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "primary_key")
    private int primaryKey;
    @ColumnInfo(name = "foreign_unit_key")
    private int foreigenUnitKey;

    private Date timeStamp;

    public int getForeigenUnitKey() {
        return foreigenUnitKey;
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }
}
