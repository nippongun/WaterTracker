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

/*
* The database is the heart of the app and provides structured data for storing and managing the
* users' inputs. The database follows the singleton design pattern to avoid performance losses due
* to multiple instances.
*
* Resources and references are for the entire package 'database'
* https://developer.android.com/training/data-storage/room
* https://developer.android.com/jetpack/androidx/releases/room
* */
@Database(entities = {Unit.class, Consumption.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class UnitDatabase extends RoomDatabase {
    //The database uses the DAO to handle the data. It allows the database faster handling
    //for asynchronous tasks.
    public abstract UnitDao unitDao();
    private static volatile UnitDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static UnitDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            // the keyword synchronized allows statements to be executed after the thread has been finished
            //https://docs.oracle.com/javase/tutorial/essential/concurrency/locksync.html
            synchronized (UnitDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            UnitDatabase.class, "unit_database").addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    /*
    * A callback allows to manage data on multiple occasions such as onCreate
    * In this case, predefined units are created on creation of the database
    * */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriterExecutor.execute(() -> {
                UnitDao dao = INSTANCE.unitDao();
                //The following unit is a dummy necessary for spinner selection on the main activity.
                Unit unit = new Unit("SELECT AN ITEM", 0);
                dao.insertUnit(unit);
                unit = new Unit("standard", 500);
                dao.insertUnit(unit);


                //add a consumption record
//                Date date= Calendar.getInstance().getTime();
//                Consumption consumption= new Consumption(1,date);
//                dao.insertConsumption(consumption);

            });
        }
    };
}
