package com.mfamilys.mine.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import com.mfamilys.mine.database.table.DailyTable;

/**
 * Created by mfamilys on 16-4-8.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME="Mine";
    private static DatabaseHelper instance=null;
    private static final int DB_VERSION=1;
    public static final String DELETE_TABLE_DATA="delete from ";
    public static final String DROP_TABLE="drop table if exists ";
    private DatabaseHelper(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DailyTable.CREATE_TABLE);
        sqLiteDatabase.execSQL(DailyTable.CREATE_COLLECTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if(i==1){
            sqLiteDatabase.execSQL(DROP_TABLE+DailyTable.NAME);
            sqLiteDatabase.execSQL(DROP_TABLE+DailyTable.COLLECTION_NAME);
            sqLiteDatabase.execSQL(DailyTable.CREATE_TABLE);
            sqLiteDatabase.execSQL(DailyTable.CREATE_COLLECTION_TABLE);
        }
    }

    public static synchronized DatabaseHelper instance(Context context){
        if(instance==null){
            instance=new DatabaseHelper(context,DB_NAME,null,DB_VERSION);
        }
        return instance;
    }
}
