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

import hkucs.freeden.model.dbmodels.HistoryRecord;

import static android.content.ContentValues.TAG;

/**
 * Created by lshung on 10/11/2017.
 */

public class HistoryDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "historyDB.db";

    private static final String TABLE_HISTORY = "History";
    private static final String COL_ID = "_id";
    private static final String COL_MSG_NUM = "msg_num";
    private static final String COL_PAGE = "page";
    private static final String COL_LAST_MOD_DATE = "last_mod_date";

    public HistoryDBHandler( Context context , String name , SQLiteDatabase.CursorFactory factory, int version ) {
        super( context , DATABASE_NAME , factory , DATABASE_VERSION );
    }

    @Override
    public void onCreate( SQLiteDatabase db ) {
        String sql_create_table_history = String.format(
                "CREATE TABLE %s ( %s INTEGER PRIMARY KEY , %s INTEGER , %s INTEGER , %s INTEGER )"
                , TABLE_HISTORY , COL_ID , COL_MSG_NUM , COL_PAGE , COL_LAST_MOD_DATE
        );
        db.execSQL(sql_create_table_history);
    }

    @Override
    public void onUpgrade( SQLiteDatabase db , int oldVersion , int newVersion ) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s" , TABLE_HISTORY));
        onCreate(db);
    }

    public void deleteHistoryRecord( int id ) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            String sql_delete_record = String.format(
                    "DELETE FROM %s WHERE %s = %d"
                    , TABLE_HISTORY, COL_ID , id
            );
            db.execSQL(sql_delete_record);
            db.setTransactionSuccessful();
        } catch( SQLException e ) {
            Log.d(TAG, "Error while trying to delete record from db:History");
        } catch( IllegalStateException e ) {
            Log.d(TAG, "Error while setting transaction successful: not in transaction or already marked");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteHistoryRecords( int[] id_list ) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            String sql_delete_multi = String.format(
                    "DELETE FROM %s WHERE %s IN ( "
                    , TABLE_HISTORY, COL_ID
            );
            for(int i=0;i<id_list.length;i++) {
                if(i!=0) sql_delete_multi += " , ";
                sql_delete_multi += String.valueOf(id_list[i]);
            }
            sql_delete_multi += " )";

            db.execSQL(sql_delete_multi);
            db.setTransactionSuccessful();
        } catch( SQLException e ) {
            Log.d(TAG, "Error while trying to delete record from db:History");
        } catch( IllegalStateException e ) {
            Log.d(TAG, "Error while setting transaction successful: not in transaction or already marked");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteAllHistoryRecords() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            String sql_delete_all = String.format(
                    "DELETE FROM %s"
                    , TABLE_HISTORY
            );
            db.execSQL(sql_delete_all);
            db.setTransactionSuccessful();
        } catch( SQLException e ) {
            Log.d(TAG, "Error while trying to delete record from db:History");
        } catch( IllegalStateException e ) {
            Log.d(TAG, "Error while setting transaction successful: not in transaction or already marked");
        } finally {
            db.endTransaction();
        }
    }

    // higher efficiency
    public void addOrUpdateHistoryRecord( HistoryRecord record ) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            String sql_insert_record = String.format(
                    "INSERT OR REPLACE INTO %s ( %s , %s , %s , %s )"
                            + " VALUES ( %d , %d , %d , %d )"
                    , TABLE_HISTORY
                    , COL_ID , COL_MSG_NUM , COL_PAGE , COL_LAST_MOD_DATE
                    , record.getId() , record.getMsgNum() , record.getPage() , record.getLastModDate()
            );
            db.execSQL(sql_insert_record);

            db.setTransactionSuccessful();
        } catch( SQLException e ) {
            Log.d(TAG, "Error while trying to add record to db:History");
        } catch( IllegalStateException e ) {
            Log.d(TAG, "Error while setting transaction successful: not in transaction or already marked");
        } finally {
            db.endTransaction();
        }
    }

    public HistoryRecord getHistoryRecord( int id ) {
        String where_clause = String.format(" WHERE %s = %d", COL_ID , id);
        List<HistoryRecord> l = getHistoryRecordsInListWhere(where_clause);
        return l.size()==0 ? null : l.get(0);
    }

    public List<HistoryRecord> getHistoryRecordsInList( int[] id_list ) {
        String where_clause = String.format(
                "WHERE %s IN ("
                , COL_ID
        );
        for(int i=0;i<id_list.length;i++) {
            if(i!=0) where_clause += " , ";
            where_clause += String.valueOf(id_list[i]);
        }
        where_clause += " )";
        return getHistoryRecordsInListWhere(where_clause);
    }

    public List<HistoryRecord> getAllHistoryRecordsInList() {
        return getHistoryRecordsInListWhere("");
    }

    private List<HistoryRecord> getHistoryRecordsInListWhere( String where_clause ) {
        List<HistoryRecord> record_list = new ArrayList<HistoryRecord>();
        SQLiteDatabase db = getReadableDatabase();
        String sql_select_all = String.format(
                "SELECT %s , %s , %s , %s FROM %s%s ORDER BY %s DESC"
                , COL_ID , COL_MSG_NUM , COL_PAGE , COL_LAST_MOD_DATE , TABLE_HISTORY
                , where_clause, COL_LAST_MOD_DATE
        );
        Cursor cursor = db.rawQuery(sql_select_all, null);
        if(cursor.moveToFirst()) {
            do {
                HistoryRecord record = new HistoryRecord(
                        cursor.getInt(cursor.getColumnIndex(COL_ID))
                        , cursor.getInt(cursor.getColumnIndex(COL_MSG_NUM))
                        , cursor.getInt(cursor.getColumnIndex(COL_PAGE))
                        , cursor.getLong(cursor.getColumnIndex(COL_LAST_MOD_DATE))
                );

                record_list.add(record);
            } while(cursor.moveToNext());
        }
        return record_list;
    }

    public Integer[] getHistoryRecordsIdsInArray( int[] id_list ) {
        String where_clause = String.format(
                "WHERE %s IN ("
                , COL_ID
        );
        for(int i=0;i<id_list.length;i++) {
            if(i!=0) where_clause += " , ";
            where_clause += String.valueOf(id_list[i]);
        }
        where_clause += " )";
        return getHistoryRecordIdsInArrayWhere(where_clause);
    }

    public Integer[] getAllHistoryRecordIdsInArray() {
        return getHistoryRecordIdsInArrayWhere("");
    }

    private Integer[] getHistoryRecordIdsInArrayWhere( String where_clause ) {
        ArrayList<Integer> record_arr = new ArrayList<Integer>();
        SQLiteDatabase db = getReadableDatabase();
        String sql_select_all = String.format(
                "SELECT %s FROM %s%s ORDER BY %s DESC"
                , COL_ID , TABLE_HISTORY
                , where_clause, COL_LAST_MOD_DATE
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

    public HashSet<HistoryRecord> getHistoryRecordsInHashSet( int[] id_list ) {
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

    public HashSet<HistoryRecord> getAllHistoryRecordsInHashSet() {
        return getHistoryRecordsInHashSetWhere("");
    }

    private HashSet<HistoryRecord> getHistoryRecordsInHashSetWhere( String where_clause ) {
        HashSet<HistoryRecord> record_set = new HashSet<HistoryRecord>();
        SQLiteDatabase db = getReadableDatabase();
        String sql_select_all = String.format(
                "SELECT %s , %s , %s , %s FROM %s%s ORDER BY %s DESC"
                , COL_ID , COL_MSG_NUM , COL_PAGE , COL_LAST_MOD_DATE , TABLE_HISTORY
                , where_clause, COL_LAST_MOD_DATE
        );
        Cursor cursor = db.rawQuery(sql_select_all, null);
        if(cursor.moveToFirst()) {
            do {
                HistoryRecord record = new HistoryRecord(
                        cursor.getInt(cursor.getColumnIndex(COL_ID))
                        , cursor.getInt(cursor.getColumnIndex(COL_MSG_NUM))
                        , cursor.getInt(cursor.getColumnIndex(COL_PAGE))
                        , cursor.getLong(cursor.getColumnIndex(COL_LAST_MOD_DATE))
                );

                record_set.add(record);
            } while(cursor.moveToNext());
        }
        return record_set;
    }

    public HashSet<Integer> getHistoryRecordsIdsInHashSet( int[] id_list ) {
        String where_clause = String.format(
                "WHERE %s IN ("
                , COL_ID
        );
        for(int i=0;i<id_list.length;i++) {
            if(i!=0) where_clause += " , ";
            where_clause += String.valueOf(id_list[i]);
        }
        where_clause += " )";
        return getHistoryRecordIdsInHashSetWhere(where_clause);
    }

    public HashSet<Integer> getAllHistoryRecordIdsInHashSet() {
        return getHistoryRecordIdsInHashSetWhere("");
    }

    private HashSet<Integer> getHistoryRecordIdsInHashSetWhere( String where_clause ) {
        HashSet<Integer> record_set = new HashSet<Integer>();
        SQLiteDatabase db = getReadableDatabase();
        String sql_select_all = String.format(
                "SELECT %s FROM %s%s ORDER BY %s DESC"
                , COL_ID , TABLE_HISTORY
                , where_clause, COL_LAST_MOD_DATE
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