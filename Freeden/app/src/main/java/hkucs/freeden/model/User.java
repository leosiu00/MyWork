package hkucs.freeden.model;

import android.graphics.Color;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by lshung on 22/11/2017.
 */

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    protected int _status;
    protected int _id;
    protected int _level;
    protected String _gender;
    protected long _create_time;
    protected boolean _is_following;
    protected String _level_name;
    protected boolean _is_blocked;
    protected String _nickname;

    public User() {
    }

    public User( int id , String nickname , String gender , String level_name ) {
        this._id = id;
        this._nickname = nickname;
        this._gender = gender;
        this._level_name = level_name;
    }

    public User( int id , String nickname , String gender , String level_name , boolean is_blocked ) {
        this._id = id;
        this._nickname = nickname;
        this._gender = gender;
        this._level_name = level_name;
        this._is_blocked = is_blocked;
    }

    // full constructor
    public User( int status , int id , int level , String gender , long create_time , boolean is_following , String level_name , boolean is_blocked , String nickname ) {
        this._status = status;
        this._id = id;
        this._level = level;
        this._gender = gender;
        this._create_time = create_time;
        this._is_following = is_following;
        this._level_name = level_name;
        this._is_blocked = is_blocked;
        this._nickname = nickname;
    }

    public User( JSONObject c ) throws JSONException {
        this(
                c.getInt("status")
                , c.getInt("user_id")
                , c.getInt("level")
                , c.getString("gender")
                , c.getInt("create_time")
                , c.getBoolean("is_following")
                , c.getString("level_name")
                , c.getBoolean("is_blocked")
                , c.getString("nickname")
        );
    }

    public void setStatus( int status ) { this._status = status; }
    public void setId( int id ) { this._id = id; }
    public void setLevel( int level ) { this._level = level; }
    public void setGender( String gender ) { this._gender = gender; }
    public void setCreateTime( long create_time ) { this._create_time = create_time; }
    public void setIsFollowing( boolean is_following ) { this._is_following = is_following; }
    public void setLevelName( String level_name ) { this._level_name = level_name; }
    public void setIsBlocked( boolean is_blocked ) { this._is_blocked = is_blocked; }
    public void setNickname( String nickname ) { this._nickname = nickname; }

    public int getStatus() { return _status; }
    public int getId() { return _id; }
    public int getLevel() { return _level; }
    public String getGender() { return _gender; }
    public long getCreateTime() { return _create_time; }
    public boolean getIsFollowing() { return _is_following; }
    public String getLevelName() { return _level_name; }
    public boolean getIsBlocked() { return _is_blocked; }
    public String getNickname() { return _nickname; }

    public int getColor() {
        if( this.getLevelName().equals("站長") ) return Color.parseColor("#FFEE58");
        else if( this.getGender().equals("M") ) return Color.parseColor("#42A5F5");
        return Color.parseColor("#EF5350");
    }
}