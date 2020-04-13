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

import hkucs.freeden.model.dbmodels.BlockedUserRecord;

import static android.content.ContentValues.TAG;

/**
 * Created by lshung on 10/11/2017.
 */

public class BlockedUserDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "blockedUserDB.db";

    private static final String TABLE_BLOCKED_USER = "BlockedUser";
    private static final String COL_STATUS = "status";
    private static final String COL_ID = "_id";
    private static final String COL_LEVEL = "level";
    private static final String COL_GENDER = "gender";
    private static final String COL_CREATE_TIME = "create_time";
    private static final String COL_IS_FOLLOWING = "is_following";
    private static final String COL_LEVEL_NAME = "level_name";
    private static final String COL_IS_BLOCKED = "is_blocked";
    private static final String COL_NICKNAME = "nickname";
    private static final String COL_BLOCK_DATE = "block_date";

    public BlockedUserDBHandler( Context context , String name , SQLiteDatabase.CursorFactory factory, int version ) {
        super( context , DATABASE_NAME , factory , DATABASE_VERSION );
    }

    @Override
    public void onCreate( SQLiteDatabase db ) {
        String sql_create_table_blocked_user = String.format(
                "CREATE TABLE %s ( %s INTEGER , %s INTEGER PRIMARY KEY , %s INTEGER , %s TEXT " +
                        ", %s INTEGER , %s INTEGER , %s TEXT " +
                        ", %s INTEGER , %s TEXT , %s INTEGER )"
                , TABLE_BLOCKED_USER, COL_STATUS, COL_ID, COL_LEVEL, COL_GENDER
                , COL_CREATE_TIME, COL_IS_FOLLOWING, COL_LEVEL_NAME
                , COL_IS_BLOCKED, COL_NICKNAME, COL_BLOCK_DATE
        );
        db.execSQL(sql_create_table_blocked_user);
    }

    @Override
    public void onUpgrade( SQLiteDatabase db , int oldVersion , int newVersion ) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s" , TABLE_BLOCKED_USER));
        onCreate(db);
    }

    public void deleteBlockedUser( int id ) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            String sql_delete_record = String.format(
                    "DELETE FROM %s WHERE %s = %d"
                    , TABLE_BLOCKED_USER, COL_ID , id
            );
            db.execSQL(sql_delete_record);
            db.setTransactionSuccessful();
        } catch( SQLException e ) {
            Log.d(TAG, "Error while trying to delete record from db:BlockedUser");
        } catch( IllegalStateException e ) {
            Log.d(TAG, "Error while setting transaction successful: not in transaction or already marked");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteBlockedUsers( int[] id_list ) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            String sql_delete_multi = String.format(
                    "DELETE FROM %s WHERE %s IN ( "
                    , TABLE_BLOCKED_USER, COL_ID
            );
            for(int i=0;i<id_list.length;i++) {
                if(i!=0) sql_delete_multi += " , ";
                sql_delete_multi += String.valueOf(id_list[i]);
            }
            sql_delete_multi += " )";

            db.execSQL(sql_delete_multi);
            db.setTransactionSuccessful();
        } catch( SQLException e ) {
            Log.d(TAG, "Error while trying to delete record from db:BlockedUser");
        } catch( IllegalStateException e ) {
            Log.d(TAG, "Error while setting transaction successful: not in transaction or already marked");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteAllBlockedUsers() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            String sql_delete_all = String.format(
                    "DELETE FROM %s"
                    , TABLE_BLOCKED_USER
            );
            db.execSQL(sql_delete_all);
            db.setTransactionSuccessful();
        } catch( SQLException e ) {
            Log.d(TAG, "Error while trying to delete record from db:BlockedUser");
        } catch( IllegalStateException e ) {
            Log.d(TAG, "Error while setting transaction successful: not in transaction or already marked");
        } finally {
            db.endTransaction();
        }
    }

    // higher efficiency
    public void addOrUpdateBlockedUser( BlockedUserRecord user ) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            String sql_insert_record = String.format(
                    "INSERT OR REPLACE INTO %s ( %s , %s , %s , %s " +
                            ", %s , %s , %s " +
                            ", %s , %s , %s )" +
                            " VALUES ( %d , %d , %d , '%s' " +
                            ", %d  , %d  , '%s'  " +
                            ", %d  , '%s' , %d )"
                    , TABLE_BLOCKED_USER, COL_STATUS, COL_ID, COL_LEVEL, COL_GENDER
                    , COL_CREATE_TIME, COL_IS_FOLLOWING, COL_LEVEL_NAME
                    , COL_IS_BLOCKED, COL_NICKNAME, COL_BLOCK_DATE
                    , user.getStatus(), user.getId(), user.getLevel()
                    , user.getGender(), user.getCreateTime()
                    , ( user.getIsFollowing() ? 1 : 0 ), user.getLevelName()
                    , ( user.getIsBlocked() ? 1 : 0 ), user.getNickname(), user.getBlockDate()
            );
            Log.d(TAG, sql_insert_record);
            db.execSQL(sql_insert_record);

            db.setTransactionSuccessful();
        } catch( SQLException e ) {
            Log.d(TAG, "Error while trying to add record to db:BlockedUser");
        } catch( IllegalStateException e ) {
            Log.d(TAG, "Error while setting transaction successful: not in transaction or already marked");
        } finally {
            db.endTransaction();
        }
    }

    public BlockedUserRecord getBlockedUser( int id ) {
        String where_clause = String.format(" WHERE %s = %d", COL_ID , id);
        List<BlockedUserRecord> l = getBlockedUsersInListWhere(where_clause);
        return l.size()==0 ? null : l.get(0);
    }

    public List<BlockedUserRecord> getBlockedUsersInList( int[] id_list ) {
        String where_clause = String.format(
                "WHERE %s IN ("
                , COL_ID
        );
        for(int i=0;i<id_list.length;i++) {
            if(i!=0) where_clause += " , ";
            where_clause += String.valueOf(id_list[i]);
        }
        where_clause += " )";
        return getBlockedUsersInListWhere(where_clause);
    }

    public List<BlockedUserRecord> getAllBlockedUsersInList() {
        return getBlockedUsersInListWhere("");
    }

    private List<BlockedUserRecord> getBlockedUsersInListWhere( String where_clause ) {
        List<BlockedUserRecord> record_list = new ArrayList<BlockedUserRecord>();
        SQLiteDatabase db = getReadableDatabase();
        String sql_select_all = String.format(
                "SELECT %s , %s , %s , %s " +
                        ", %s , %s , %s " +
                        ", %s , %s , %s" +
                        " FROM %s%s ORDER BY %s DESC"
                , COL_STATUS, COL_ID, COL_LEVEL, COL_GENDER
                , COL_CREATE_TIME, COL_IS_FOLLOWING, COL_LEVEL_NAME
                , COL_IS_BLOCKED, COL_NICKNAME, COL_BLOCK_DATE
                , TABLE_BLOCKED_USER, where_clause, COL_BLOCK_DATE
        );
        Cursor cursor = db.rawQuery(sql_select_all, null);
        if(cursor.moveToFirst()) {
            do {
                BlockedUserRecord user = new BlockedUserRecord(
                        cursor.getInt(cursor.getColumnIndex(COL_STATUS))
                        , cursor.getInt(cursor.getColumnIndex(COL_ID))
                        , cursor.getInt(cursor.getColumnIndex(COL_LEVEL))
                        , cursor.getString(cursor.getColumnIndex(COL_GENDER))
                        , cursor.getInt(cursor.getColumnIndex(COL_CREATE_TIME))
                        , ( cursor.getInt(cursor.getColumnIndex(COL_IS_FOLLOWING))==1 ? true : false )
                        , cursor.getString(cursor.getColumnIndex(COL_LEVEL_NAME))
                        , ( cursor.getInt(cursor.getColumnIndex(COL_IS_BLOCKED))==1 ? true : false )
                        , cursor.getString(cursor.getColumnIndex(COL_NICKNAME))
                        , cursor.getLong(cursor.getColumnIndex(COL_BLOCK_DATE))
                );

                record_list.add(user);
            } while(cursor.moveToNext());
        }
        return record_list;
    }

    public Integer[] getBlockedUsersIdsInArray( int[] id_list ) {
        String where_clause = String.format(
                "WHERE %s IN ("
                , COL_ID
        );
        for(int i=0;i<id_list.length;i++) {
            if(i!=0) where_clause += " , ";
            where_clause += String.valueOf(id_list[i]);
        }
        where_clause += " )";
        return getBlockedUserIdsInArrayWhere(where_clause);
    }

    public Integer[] getAllBlockedUserIdsInArray() {
        return getBlockedUserIdsInArrayWhere("");
    }

    private Integer[] getBlockedUserIdsInArrayWhere( String where_clause ) {
        ArrayList<Integer> record_arr = new ArrayList<Integer>();
        SQLiteDatabase db = getReadableDatabase();
        String sql_select_all = String.format(
                "SELECT %s FROM %s%s ORDER BY %s DESC"
                , COL_ID , TABLE_BLOCKED_USER
                , where_clause, COL_BLOCK_DATE
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

    public HashSet<BlockedUserRecord> getBlockedUsersInHashSet( int[] id_list ) {
        String where_clause = String.format(
                "WHERE %s IN ("
                , COL_ID
        );
        for(int i=0;i<id_list.length;i++) {
            if(i!=0) where_clause += " , ";
            where_clause += String.valueOf(id_list[i]);
        }
        where_clause += " )";
        return getBlockedUsersInHashSetWhere(where_clause);
    }

    public HashSet<BlockedUserRecord> getAllBlockedUsersInHashSet() {
        return getBlockedUsersInHashSetWhere("");
    }

    private HashSet<BlockedUserRecord> getBlockedUsersInHashSetWhere( String where_clause ) {
        HashSet<BlockedUserRecord> record_set = new HashSet<BlockedUserRecord>();
        SQLiteDatabase db = getReadableDatabase();
        String sql_select_all = String.format(
                "SELECT %s , %s , %s , %s " +
                        ", %s , %s , %s " +
                        ", %s , %s , %s )" +
                        " FROM %s%s ORDER BY %s DESC"
                , COL_STATUS, COL_ID, COL_LEVEL, COL_GENDER
                , COL_CREATE_TIME, COL_IS_FOLLOWING, COL_LEVEL_NAME
                , COL_IS_BLOCKED, COL_NICKNAME, COL_BLOCK_DATE
                , TABLE_BLOCKED_USER, where_clause, COL_BLOCK_DATE
        );
        Cursor cursor = db.rawQuery(sql_select_all, null);
        if(cursor.moveToFirst()) {
            do {
                BlockedUserRecord user = new BlockedUserRecord(
                        cursor.getInt(cursor.getColumnIndex(COL_STATUS))
                        , cursor.getInt(cursor.getColumnIndex(COL_ID))
                        , cursor.getInt(cursor.getColumnIndex(COL_LEVEL))
                        , cursor.getString(cursor.getColumnIndex(COL_GENDER))
                        , cursor.getInt(cursor.getColumnIndex(COL_CREATE_TIME))
                        , ( cursor.getInt(cursor.getColumnIndex(COL_IS_FOLLOWING))==1 ? true : false )
                        , cursor.getString(cursor.getColumnIndex(COL_LEVEL_NAME))
                        , ( cursor.getInt(cursor.getColumnIndex(COL_IS_BLOCKED))==1 ? true : false )
                        , cursor.getString(cursor.getColumnIndex(COL_NICKNAME))
                        , cursor.getLong(cursor.getColumnIndex(COL_BLOCK_DATE))
                );

                record_set.add(user);
            } while(cursor.moveToNext());
        }
        return record_set;
    }

    public HashSet<Integer> getBlockedUsersIdsInHashSet( int[] id_list ) {
        String where_clause = String.format(
                "WHERE %s IN ("
                , COL_ID
        );
        for(int i=0;i<id_list.length;i++) {
            if(i!=0) where_clause += " , ";
            where_clause += String.valueOf(id_list[i]);
        }
        where_clause += " )";
        return getBlockedUserIdsInHashSetWhere(where_clause);
    }

    public HashSet<Integer> getAllBlockedUserIdsInHashSet() {
        return getBlockedUserIdsInHashSetWhere("");
    }

    private HashSet<Integer> getBlockedUserIdsInHashSetWhere( String where_clause ) {
        HashSet<Integer> record_set = new HashSet<Integer>();
        SQLiteDatabase db = getReadableDatabase();
        String sql_select_all = String.format(
                "SELECT %s FROM %s%s ORDER BY %s DESC"
                , COL_ID , TABLE_BLOCKED_USER
                , where_clause, COL_BLOCK_DATE
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