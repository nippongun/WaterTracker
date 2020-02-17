package fi.metropolia.simppa.watertracker.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "consumption_table", foreignKeys = @ForeignKey(entity = Unit.class,
                                                                    parentColumns = "primary_key",
                                                                    childColumns = "foreign_unit_key",
                                                                    onDelete = CASCADE))
public class Consumption {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "primary_key")
    private int primaryKey;
    @ColumnInfo(name = "foreign_unit_key")
    private int foreigenUnitKey;

    @TypeConverters({Converters.class})
    @ColumnInfo(name = "timestamp")
    private Date timeStamp;

    public Consumption(int foreigenUnitKey, Date timeStamp){
        this.foreigenUnitKey = foreigenUnitKey;
        this.timeStamp = timeStamp;
    }

    public int getForeigenUnitKey() {
        return foreigenUnitKey;
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setForeigenUnitKey(int foreigenUnitKey) {
        this.foreigenUnitKey = foreigenUnitKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
