package alonquin.cst2335.group_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Ben on 2018-03-28.
 */

public class OCDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "stations.db";
    public static final int VERSION_NUM = 1;

    public static final String TABLE_NAME = "stations";
    public static final String STATION_NO = "station_number";
    public static final String STATION_NAME = "station_name";


    public static final String TABLE_NAME_ROUTES = "routes";
    public static final String ROUTE_NO = "route_number";



    public OCDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + STATION_NO + " text, " + STATION_NAME +  " text);");

        db.execSQL("CREATE TABLE " + TABLE_NAME_ROUTES + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                 + ROUTE_NO +  " text);");

        Log.i("OCDatabaseHelper", "Calling onCreate()");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ROUTES);
        Log.i("OCDatabaseHelper", "Calling onUpgrade(), oldVersion="
                + oldVer + ". newVersion=" + newVer + ".");
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ROUTES);
        Log.i("OCDatabaseHelper", "Calling onDowngrade(), oldVersion="
                + oldVer + ". newVersion=" + newVer + ".");
        onCreate(db);
    }

}
