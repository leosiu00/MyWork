package hkucs.freeden.model.dbmodels;

import hkucs.freeden.model.User;

/**
 * Created by lshung on 10/11/2017.
 */

public class BlockedUserRecord extends User {
    protected long _block_date;

    public BlockedUserRecord() {
    }

    public BlockedUserRecord( int id , String nickname , String gender , String level_name ) {
        super(id, nickname, gender, level_name);
        this._block_date = System.currentTimeMillis();
    }

    public BlockedUserRecord( int id , String nickname , String gender , String level_name , long block_date ) {
        super(id, nickname, gender, level_name);
        this._block_date = block_date;
    }

    public BlockedUserRecord( int id , String nickname , String gender , String level_name , boolean is_blocked ) {
        super(id, nickname, gender, level_name, is_blocked);
        this._block_date = System.currentTimeMillis();
    }

    public BlockedUserRecord( int id , String nickname , String gender , String level_name , boolean is_blocked , long block_date ) {
        super(id, nickname, gender, level_name, is_blocked);
        this._block_date = block_date;
    }

    // full constructor
    public BlockedUserRecord( int status , int id , int level , String gender , long create_time , boolean is_following , String level_name , boolean is_blocked , String nickname ) {
        super(status, id, level, gender, create_time, is_following, level_name, is_blocked, nickname);
        this._block_date = System.currentTimeMillis();
    }

    // full constructor with block_date
    public BlockedUserRecord( int status , int id , int level , String gender , long create_time , boolean is_following , String level_name , boolean is_blocked , String nickname , long block_date ) {
        super(status, id, level, gender, create_time, is_following, level_name, is_blocked, nickname);
        this._block_date = block_date;
    }

    public BlockedUserRecord( User user ) {
        super();
        this._status = user.getStatus();
        this._id = user.getId();
        this._level = user.getLevel();
        this._gender = user.getGender();
        this._create_time = user.getCreateTime();
        this._is_following = user.getIsFollowing();
        this._level_name = user.getLevelName();
        this._is_blocked = user.getIsBlocked();
        this._nickname = user.getNickname();
        this._block_date = System.currentTimeMillis();
    }

    public BlockedUserRecord( User user , long block_date ) {
        super();
        this._status = user.getStatus();
        this._id = user.getId();
        this._level = user.getLevel();
        this._gender = user.getGender();
        this._create_time = user.getCreateTime();
        this._is_following = user.getIsFollowing();
        this._level_name = user.getLevelName();
        this._is_blocked = user.getIsBlocked();
        this._nickname = user.getNickname();
        this._block_date = block_date;
    }

    public void setBlockDate( long block_date ) { this._block_date = block_date; }

    public long getBlockDate() { return _block_date; }

    public User toUser() { return (User) this; }
}
