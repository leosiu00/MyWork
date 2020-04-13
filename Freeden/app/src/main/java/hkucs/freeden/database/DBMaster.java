package hkucs.freeden.database;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import hkucs.freeden.model.dbmodels.BlockedUserRecord;
import hkucs.freeden.model.dbmodels.GenericRecord;
import hkucs.freeden.model.dbmodels.HistoryRecord;
import hkucs.freeden.model.User;

/**
 * HANDLES ALL DATABASE BECAUSE NO REASON
 * INSERT MORE METHODS IF NEEDED HERE
 * Created by lshung on 10/11/2017.
 */

public class DBMaster {
    Context context;
    protected HistoryDBHandler hdb;
    protected int hdb_version = 1;
    protected FavouriteDBHandler fdb;
    protected int fdb_version = 1;
    protected WatchLaterDBHandler wldb;
    protected int wldb_version = 1;
    protected BlockedUserDBHandler budb;
    protected int budb_version = 1;

    public DBMaster( Context context ) {
        this.context = context;
        hdb = new HistoryDBHandler(context, null, null, hdb_version);
        fdb = new FavouriteDBHandler(context, null, null, fdb_version);
        wldb = new WatchLaterDBHandler(context, null, null, wldb_version);
        budb = new BlockedUserDBHandler(context, null, null, budb_version);
    }



    public void close() {
        hdb.close();
        fdb.close();
        wldb.close();
        budb.close();
    }



    public void addOrDeleteHistoryPostThread( int thread_id ) {
        if(hdb.getHistoryRecord(thread_id)==null) addHistoryPostThread(thread_id);
        else deleteHistoryPostThread(thread_id);
    }

    public void addHistoryPostThread( int thread_id ) {
        hdb.addOrUpdateHistoryRecord(new HistoryRecord(thread_id, 1, 1));
    }

    public void deleteHistoryPostThread( int thread_id ) {
        hdb.deleteHistoryRecord(thread_id);
    }

    public void deleteHistoryPostThreads( int[] thread_id_arr ) {
        hdb.deleteHistoryRecords(thread_id_arr);
    }

    public void deleteAllHistoryPostThread() {
        hdb.deleteAllHistoryRecords();
    }

    public List<HistoryRecord> getAllHistoryPostThreadsInList() {
        return hdb.getAllHistoryRecordsInList();
    }

    public Integer[] getAllHistoryPostThreadIdsInArray() {
        return hdb.getAllHistoryRecordIdsInArray();
    }

    public HashSet<HistoryRecord> getAllHistoryPostThreadsInHashSet() {
        return hdb.getAllHistoryRecordsInHashSet();
    }

    public HashSet<Integer> getAllHistoryPostThreadIdsInHashSet() {
        return hdb.getAllHistoryRecordIdsInHashSet();
    }



    public void saveOrUnsavePostThread( int thread_id ) {
        if(fdb.getFavouriteRecord(thread_id)==null) savePostThread(thread_id);
        else unsavePostThread(thread_id);
    }

    public void savePostThread( int thread_id ) {
        fdb.addOrUpdateFavouriteRecord(new GenericRecord(thread_id));
    }

    public void unsavePostThread(int thread_id ) {
        fdb.deleteFavouriteRecord(thread_id);
    }

    public void unsavePostThreads(int[] thread_id_arr ) {
        fdb.deleteFavouriteRecords(thread_id_arr);
    }

    public void unsaveAllPostThreads() {
        fdb.deleteAllFavouriteRecords();
    }

    public List<GenericRecord> getAllSavedPostThreadsInList() {
        return fdb.getAllFavouriteRecordsInList();
    }

    public Integer[] getAllSavedPostThreadIdsInArray() {
        return fdb.getAllFavouriteRecordIdsInArray();
    }

    public HashSet<GenericRecord> getAllSavedPostThreadsInHashSet() {
        return fdb.getAllHistoryRecordsInHashSet();
    }

    public HashSet<Integer> getAllSavedPostThreadIdsInHashSet() {
        return fdb.getAllFavouriteRecordIdsInHashSet();
    }



    public void watchLaterOrUnwatchLaterPostThread( int thread_id ) {
        if(wldb.getWatchLaterRecord(thread_id)==null) watchLaterPostThread(thread_id);
        else unwatchLaterPostThread(thread_id);
    }

    public void watchLaterPostThread( int thread_id ) {
        wldb.addOrUpdateWatchLaterRecord(new GenericRecord(thread_id));
    }

    public void unwatchLaterPostThread(int thread_id ) {
        wldb.deleteWatchLaterRecord(thread_id);
    }

    public void unwatchLaterPostThreads(int[] thread_id_arr ) {
        wldb.deleteWatchLaterRecords(thread_id_arr);
    }

    public void unwatchLaterAllPostThreads() {
        wldb.deleteAllWatchLaterRecords();
    }

    public List<GenericRecord> getAllWatchLaterPostThreadsInList() {
        return wldb.getAllWatchLaterRecordsInList();
    }

    public Integer[] getAllWatchLaterPostThreadIdsInArray() {
        return wldb.getAllWatchLaterRecordIdsInArray();
    }

    public HashSet<GenericRecord> getAllWatchLaterPostThreadsInHashSet() {
        return wldb.getAllHistoryRecordsInHashSet();
    }

    public HashSet<Integer> getAllWatchLaterPostThreadIdsInHashSet() {
        return wldb.getAllWatchLaterRecordIdsInHashSet();
    }



    public void blockOrUnblockUser( User user ) {
        if(budb.getBlockedUser(user.getId())==null) blockUser(user);
        else unblockUser(user);
    }

    public void blockUser( User user ) {
        budb.addOrUpdateBlockedUser(new BlockedUserRecord(user));
    }

    public void unblockUser( User user ) {
        budb.deleteBlockedUser(user.getId());
    }

    public void unblockUser( int user_id ) {
        budb.deleteBlockedUser(user_id);
    }

    public void unblockUsers( List<User> user_list ) {
        ArrayList<User> user_arr_list= (ArrayList) user_list;
        int[] user_id_arr = new int[user_arr_list.size()];
        for(int i=0; i<user_arr_list.size(); i++) user_id_arr[i] = user_arr_list.get(i).getId();
        budb.deleteBlockedUsers(user_id_arr);
    }

    public void unblockUsers( int[] user_id_arr ) {
        budb.deleteBlockedUsers(user_id_arr);
    }

    public void unblockAllUsers() {
        budb.deleteAllBlockedUsers();
    }

    public List<BlockedUserRecord> getAllBlockedUsersInList() {
        return budb.getAllBlockedUsersInList();
    }

    public Integer[] getAllBlockedUserIdsInArray() {
        return budb.getAllBlockedUserIdsInArray();
    }

    public HashSet<BlockedUserRecord> getAllBlockedUsersInHashSet() {
        return budb.getAllBlockedUsersInHashSet();
    }

    public HashSet<Integer> getAllBlockedUserIdsInHashSet() {
        return budb.getAllBlockedUserIdsInHashSet();
    }
}
