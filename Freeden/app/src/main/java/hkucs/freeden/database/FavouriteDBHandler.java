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

public class FavouriteDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "favouriteDB.db";

    private static final String TABLE_FAVOURITE = "Favourite";
    private static final String COL_ID = "_id";
    private static final String COL_CREATE_DATE = "create_date";

    public FavouriteDBHandler( Context context , String name , SQLiteDatabase.CursorFactory factory, int version ) {
        super( context , DATABASE_NAME , factory , DATABASE_VERSION );
    }

    @Override
    public void onCreate( SQLiteDatabase db ) {
        String sql_create_table_favourite = String.format(
                "CREATE TABLE %s ( %s INTEGER PRIMARY KEY , %s INTEGER )"
                , TABLE_FAVOURITE , COL_ID, COL_CREATE_DATE
        );
        db.execSQL(sql_create_table_favourite);
    }

    @Override
    public void onUpgrade( SQLiteDatabase db , int oldVersion , int newVersion ) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s" , TABLE_FAVOURITE));
        onCreate(db);
    }

    public void deleteFavouriteRecord( int id ) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            String sql_delete_record = String.format(
                    "DELETE FROM %s WHERE %s = %d"
                    , TABLE_FAVOURITE, COL_ID , id
            );
            db.execSQL(sql_delete_record);
            db.setTransactionSuccessful();
        } catch( SQLException e ) {
            Log.d(TAG, "Error while trying to delete record from db:Favourite");
        } catch( IllegalStateException e ) {
            Log.d(TAG, "Error while setting transaction successful: not in transaction or already marked");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteFavouriteRecords( int[] id_list ) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            String sql_delete_multi = String.format(
                    "DELETE FROM %s WHERE %s IN ( "
                    , TABLE_FAVOURITE, COL_ID
            );
            for(int i=0;i<id_list.length;i++) {
                if(i!=0) sql_delete_multi += " , ";
                sql_delete_multi += String.valueOf(id_list[i]);
            }
            sql_delete_multi += " )";

            db.execSQL(sql_delete_multi);
            db.setTransactionSuccessful();
        } catch( SQLException e ) {
            Log.d(TAG, "Error while trying to delete record from db:Favourite");
        } catch( IllegalStateException e ) {
            Log.d(TAG, "Error while setting transaction successful: not in transaction or already marked");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteAllFavouriteRecords() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            String sql_delete_all = String.format(
                    "DELETE FROM %s"
                    , TABLE_FAVOURITE
            );
            db.execSQL(sql_delete_all);
            db.setTransactionSuccessful();
        } catch( SQLException e ) {
            Log.d(TAG, "Error while trying to delete record from db:Favourite");
        } catch( IllegalStateException e ) {
            Log.d(TAG, "Error while setting transaction successful: not in transaction or already marked");
        } finally {
            db.endTransaction();
        }
    }

    // higher efficiency
    public void addOrUpdateFavouriteRecord( GenericRecord record ) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            String sql_insert_record = String.format(
                    "INSERT OR REPLACE INTO %s ( %s , %s )"
                            + " VALUES ( %d , %d )"
                    , TABLE_FAVOURITE
                    , COL_ID , COL_CREATE_DATE
                    , record.getId() , record.getCreateDate()
            );
            db.execSQL(sql_insert_record);

            db.setTransactionSuccessful();
        } catch( SQLException e ) {
            Log.d(TAG, "Error while trying to add record to db:Favourite");
        } catch( IllegalStateException e ) {
            Log.d(TAG, "Error while setting transaction successful: not in transaction or already marked");
        } finally {
            db.endTransaction();
        }
    }

    public GenericRecord getFavouriteRecord( int id ) {
        String where_clause = String.format(" WHERE %s = %d", COL_ID , id);
        List<GenericRecord> l = getFavouriteRecordsInListWhere(where_clause);
        return l.size()==0 ? null : l.get(0);
    }

    public List<GenericRecord> getFavouriteRecordsInList( int[] id_list ) {
        String where_clause = String.format(
                "WHERE %s IN ("
                , COL_ID
        );
        for(int i=0;i<id_list.length;i++) {
            if(i!=0) where_clause += " , ";
            where_clause += String.valueOf(id_list[i]);
        }
        where_clause += " )";
        return getFavouriteRecordsInListWhere(where_clause);
    }

    public List<GenericRecord> getAllFavouriteRecordsInList() {
        return getFavouriteRecordsInListWhere("");
    }

    private List<GenericRecord> getFavouriteRecordsInListWhere( String where_clause ) {
        List<GenericRecord> record_list = new ArrayList<GenericRecord>();
        SQLiteDatabase db = getReadableDatabase();
        String sql_select_all = String.format(
                "SELECT %s , %s FROM %s%s ORDER BY %s DESC"
                , COL_ID , COL_CREATE_DATE , TABLE_FAVOURITE
                , where_clause, COL_CREATE_DATE
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

    public Integer[] getFavouriteRecordsIdsInArray( int[] id_list ) {
        String where_clause = String.format(
                "WHERE %s IN ("
                , COL_ID
        );
        for(int i=0;i<id_list.length;i++) {
            if(i!=0) where_clause += " , ";
            where_clause += String.valueOf(id_list[i]);
        }
        where_clause += " )";
        return getFavouriteRecordIdsInArrayWhere(where_clause);
    }

    public Integer[] getAllFavouriteRecordIdsInArray() {
        return getFavouriteRecordIdsInArrayWhere("");
    }

    private Integer[] getFavouriteRecordIdsInArrayWhere( String where_clause ) {
        ArrayList<Integer> record_arr = new ArrayList<Integer>();
        SQLiteDatabase db = getReadableDatabase();
        String sql_select_all = String.format(
                "SELECT %s FROM %s%s ORDER BY %s DESC"
                , COL_ID , TABLE_FAVOURITE
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
                , COL_ID , COL_CREATE_DATE , TABLE_FAVOURITE
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

    public HashSet<Integer> getFavouriteRecordsIdsInHashSet( int[] id_list ) {
        String where_clause = String.format(
                "WHERE %s IN ("
                , COL_ID
        );
        for(int i=0;i<id_list.length;i++) {
            if(i!=0) where_clause += " , ";
            where_clause += String.valueOf(id_list[i]);
        }
        where_clause += " )";
        return getFavouriteRecordIdsInHashSetWhere(where_clause);
    }

    public HashSet<Integer> getAllFavouriteRecordIdsInHashSet() {
        return getFavouriteRecordIdsInHashSetWhere("");
    }

    private HashSet<Integer> getFavouriteRecordIdsInHashSetWhere( String where_clause ) {
        HashSet<Integer> record_set = new HashSet<Integer>();
        SQLiteDatabase db = getReadableDatabase();
        String sql_select_all = String.format(
                "SELECT %s FROM %s%s ORDER BY %s DESC"
                , COL_ID , TABLE_FAVOURITE
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