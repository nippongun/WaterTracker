package fi.metropolia.simppa.watertracker.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
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
    private long primaryKey;
    @ColumnInfo(name = "foreign_unit_key")
    private long foreigenUnitKey;

    @TypeConverters({Converters.class})
    @ColumnInfo(name = "timestamp")
    private Date timeStamp;

    public Consumption(long foreigenUnitKey, Date timeStamp){
        this.foreigenUnitKey = foreigenUnitKey;
        this.timeStamp = timeStamp;
    }

    public long getForeigenUnitKey() {
        return foreigenUnitKey;
    }

    public long getPrimaryKey() {
        return primaryKey;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setPrimaryKey(long primaryKey) {
        this.primaryKey = primaryKey;
    }
}
