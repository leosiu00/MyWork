package hkucs.freeden.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import hkucs.freeden.model.dbmodels.GenericRecord;

import static android.content.ContentValues.TAG;

/**
 * Created by lshung on 10/11/2017.
 */

public class WatchLaterDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "watchLaterDB.db";

    private static final String TABLE_WATCH_LATER = "WatchLater";
    private static final String COL_ID = "_id";
    private static final String COL_CREATE_DATE = "create_date";

    public WatchLaterDBHandler( Context context , String name , SQLiteDatabase.CursorFactory factory, int version ) {
        super( context , DATABASE_NAME , factory , DATABASE_VERSION );
    }

    @Override
    public void onCreate( SQLiteDatabase db ) {
        String sql_create_table_watch_later = String.format(
                "CREATE TABLE %s ( %s INTEGER PRIMARY KEY , %s INTEGER )"
                , TABLE_WATCH_LATER, COL_ID, COL_CREATE_DATE
        );
        db.execSQL(sql_create_table_watch_later);
    }

    @Override
    public void onUpgrade( SQLiteDatabase db , int oldVersion , int newVersion ) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s" , TABLE_WATCH_LATER));
        onCreate(db);
    }

    public void deleteWatchLaterRecord( int id ) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            String sql_delete_record = String.format(
                    "DELETE FROM %s WHERE %s = %d"
                    , TABLE_WATCH_LATER, COL_ID , id
            );
            db.execSQL(sql_delete_record);
            db.setTransactionSuccessful();
        } catch( SQLException e ) {
            Log.d(TAG, "Error while trying to delete record from db:WatchLater");
        } catch( IllegalStateException e ) {
            Log.d(TAG, "Error while setting transaction successful: not in transaction or already marked");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteWatchLaterRecords( int[] id_list ) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            String sql_delete_multi = String.format(
                    "DELETE FROM %s WHERE %s IN ( "
                    , TABLE_WATCH_LATER, COL_ID
            );
            for(int i=0;i<id_list.length;i++) {
                if(i!=0) sql_delete_multi += " , ";
                sql_delete_multi += String.valueOf(id_list[i]);
            }
            sql_delete_multi += " )";

            db.execSQL(sql_delete_multi);
            db.setTransactionSuccessful();
        } catch( SQLException e ) {
            Log.d(TAG, "Error while trying to delete record from db:WatchLater");
        } catch( IllegalStateException e ) {
            Log.d(TAG, "Error while setting transaction successful: not in transaction or already marked");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteAllWatchLaterRecords() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            String sql_delete_all = String.format(
                    "DELETE FROM %s"
                    , TABLE_WATCH_LATER
            );
            db.execSQL(sql_delete_all);
            db.setTransactionSuccessful();
        } catch( SQLException e ) {
            Log.d(TAG, "Error while trying to delete record from db:WatchLater");
        } catch( IllegalStateException e ) {
            Log.d(TAG, "Error while setting transaction successful: not in transaction or already marked");
        } finally {
            db.endTransaction();
        }
    }

    // higher efficiency
    public void addOrUpdateWatchLaterRecord( GenericRecord record ) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            String sql_insert_record = String.format(
                    "INSERT OR REPLACE INTO %s ( %s , %s )"
                            + " VALUES ( %d , %d )"
                    , TABLE_WATCH_LATER
                    , COL_ID , COL_CREATE_DATE
                    , record.getId() , record.getCreateDate()
            );
            db.execSQL(sql_insert_record);

            db.setTransactionSuccessful();
        } catch( SQLException e ) {
            Log.d(TAG, "Error while trying to add record to db:WatchLater");
        } catch( IllegalStateException e ) {
            Log.d(TAG, "Error while setting transaction successful: not in transaction or already marked");
        } finally {
            db.endTransaction();
        }
    }

    public GenericRecord getWatchLaterRecord( int id ) {
        String where_clause = String.format(" WHERE %s = %d", COL_ID , id);
        List<GenericRecord> l = getWatchLaterRecordsInListWhere(where_clause);
        return l.size()==0 ? null : l.get(0);
    }

    public List<GenericRecord> getWatchLaterRecordsInList( int[] id_list ) {
        String where_clause = String.format(
                "WHERE %s IN ("
                , COL_ID
        );
        for(int i=0;i<id_list.length;i++) {
            if(i!=0) where_clause += " , ";
            where_clause += String.valueOf(id_list[i]);
        }
        where_clause += " )";
        return getWatchLaterRecordsInListWhere(where_clause);
    }

    public List<GenericRecord> getAllWatchLaterRecordsInList() {
        return getWatchLaterRecordsInListWhere("");
    }

    private List<GenericRecord> getWatchLaterRecordsInListWhere( String where_clause ) {
        List<GenericRecord> record_list = new ArrayList<GenericRecord>();
        SQLiteDatabase db = getReadableDatabase();
        String sql_select_all = String.format(
                "SELECT %s , %s FROM %s%s"
                , COL_ID , COL_CREATE_DATE , TABLE_WATCH_LATER
                , where_clause
        );
        Cursor cursor = db.rawQuery(sql_select_all, null);
        if(cursor.moveToFirst()) {
            do {
                GenericRecord record = new GenericRecord(
                        cursor.getInt(cursor.getColumnIndex(COL_ID))
                        , cursor.getLong(cursor.getColumnIndex(COL_CREATE_DATE))
                );

                record_list.add(record);
            } while(cursor.moveToNext());
        }
        return record_list;
    }

    public Integer[] getWatchLaterRecordsIdsInArray( int[] id_list ) {
        String where_clause = String.format(
                "WHERE %s IN ("
                , COL_ID
        );
        for(int i=0;i<id_list.length;i++) {
            if(i!=0) where_clause += " , ";
            where_clause += String.valueOf(id_list[i]);
        }
        where_clause += " )";
        return getWatchLaterRecordIdsInArrayWhere(where_clause);
    }

    public Integer[] getAllWatchLaterRecordIdsInArray() {
        return getWatchLaterRecordIdsInArrayWhere("");
    }

    private Integer[] getWatchLaterRecordIdsInArrayWhere( String where_clause ) {
        ArrayList<Integer> record_arr = new ArrayList<Integer>();
        SQLiteDatabase db = getReadableDatabase();
        String sql_select_all = String.format(
                "SELECT %s FROM %s%s ORDER BY %s DESC"
                , COL_ID , TABLE_WATCH_LATER
                , where_clause, COL_CREATE_DATE
        );
        Cursor cursor = db.rawQuery(sql_select_all, null);
        if(cursor.moveToFirst()) {
            do {
                int record = cursor.getInt(cursor.getColumnIndex(COL_ID));
                record_arr.add(record);
            } while(cursor.moveToNext());
        }
        Integer[] arr = (Integer[]) record_arr.toArray(new Integer[record_arr.size()]);
        return arr;
    }

    public HashSet<GenericRecord> getHistoryRecordsInHashSet( int[] id_list ) {
        String where_clause = String.format(
                "WHERE %s IN ("
                , COL_ID
        );
        for(int i=0;i<id_list.length;i++) {
            if(i!=0) where_clause += " , ";
            where_clause += String.valueOf(id_list[i]);
        }
        where_clause += " )";
        return getHistoryRecordsInHashSetWhere(where_clause);
    }

    public HashSet<GenericRecord> getAllHistoryRecordsInHashSet() {
        return getHistoryRecordsInHashSetWhere("");
    }

    private HashSet<GenericRecord> getHistoryRecordsInHashSetWhere( String where_clause ) {
        HashSet<GenericRecord> record_set = new HashSet<GenericRecord>();
        SQLiteDatabase db = getReadableDatabase();
        String sql_select_all = String.format(
                "SELECT %s , %s FROM %s%s ORDER BY %s DESC"
                , COL_ID , COL_CREATE_DATE , TABLE_WATCH_LATER
                , where_clause, COL_CREATE_DATE
        );
        Cursor cursor = db.rawQuery(sql_select_all, null);
        if(cursor.moveToFirst()) {
            do {
                GenericRecord record = new GenericRecord(
                        cursor.getInt(cursor.getColumnIndex(COL_ID))
                        , cursor.getLong(cursor.getColumnIndex(COL_CREATE_DATE))
                );

                record_set.add(record);
            } while(cursor.moveToNext());
        }
        return record_set;
    }

    public HashSet<Integer> getWatchLaterRecordsIdsInHashSet( int[] id_list ) {
        String where_clause = String.format(
                "WHERE %s IN ("
                , COL_ID
        );
        for(int i=0;i<id_list.length;i++) {
            if(i!=0) where_clause += " , ";
            where_clause += String.valueOf(id_list[i]);
        }
        where_clause += " )";
        return getWatchLaterRecordIdsInHashSetWhere(where_clause);
    }

    public HashSet<Integer> getAllWatchLaterRecordIdsInHashSet() {
        return getWatchLaterRecordIdsInHashSetWhere("");
    }

    private HashSet<Integer> getWatchLaterRecordIdsInHashSetWhere( String where_clause ) {
        HashSet<Integer> record_set = new HashSet<Integer>();
        SQLiteDatabase db = getReadableDatabase();
        String sql_select_all = String.format(
                "SELECT %s FROM %s%s ORDER BY %s DESC"
                , COL_ID , TABLE_WATCH_LATER
                , where_clause, COL_CREATE_DATE
        );
        Cursor cursor = db.rawQuery(sql_select_all, null);
        if(cursor.moveToFirst()) {
            do {
                int record = cursor.getInt(cursor.getColumnIndex(COL_ID));
                record_set.add(record);
            } while(cursor.moveToNext());
        }
        return record_set;
    }
}