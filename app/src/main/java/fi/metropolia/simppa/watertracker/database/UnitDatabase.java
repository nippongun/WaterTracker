package fi.metropolia.simppa.watertracker.database;

import fi.metropolia.simppa.watertracker.*;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.PrimaryKey;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Unit.class, Consumption.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class UnitDatabase extends RoomDatabase {
    public abstract UnitDao unitDao();
    private static volatile UnitDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static UnitDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (UnitDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            UnitDatabase.class, "unit_database").addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriterExecutor.execute(() -> {
                UnitDao dao = INSTANCE.unitDao();
                //dao.deleteAll();
                Unit unit = new Unit("standard", 500);
                dao.insertUnit(unit);
                unit = new Unit("henlö", 333);
                dao.insertUnit(unit);
                unit = new Unit("small", 100);
                dao.insertUnit(unit);
                unit = new Unit("median", 200);
                dao.insertUnit(unit);


                //add a consumption record
                Date date= Calendar.getInstance().getTime();
                Consumption consumption= new Consumption(1,date);
                dao.insertConsupmtion(consumption);

            });
        }
    };
}
