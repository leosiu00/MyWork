package hkucs.freeden.model;

import android.graphics.Color;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import static android.content.ContentValues.TAG;

/**
 * Created by lshung on 22/11/2017.
 */

public class PostThread implements Serializable {
    private static final long serialVersionUID = 1L;
    private int _cat_id;
    private int _id;
    private int _create_time;
    private int _max_reply_dislike_count;
    private String _user_gender;
    private PostCategory _category;

    private String _title;
    private int _no_of_reply;
    private boolean _is_bookmarked;
    private int _dislike_count;
    private int _total_page;
    private String _user_nickname;

    private int _status;
    private int _last_reply_user_id;
    private int _reply_dislike_count;
    private int _reply_like_count;
    private User _user;

    private boolean _is_adu;
//    private JSONObject _remark;
    private String _remark;

    private int _no_of_uni_user_reply;
    private int _last_reply_time;
    private int _like_count;
    private boolean _is_hot;
    private int _max_reply_like_count;



    public PostThread() {
    }

    public PostThread( int cat_id , int id , PostCategory category
            , String title , int no_of_reply , int dislike_count , int total_page , User user
            , int last_reply_time , int like_count , boolean is_hot ) {
        this._cat_id = cat_id;
        this._id = id;
        this._category = category;

        this._title = title;
        this._no_of_reply = no_of_reply;
        this._dislike_count = dislike_count;
        this._total_page = total_page;
        this._user = user;

        this._last_reply_time = last_reply_time;
        this._like_count = like_count;
        this._is_hot = is_hot;
    }

    public PostThread( int cat_id , int id , int create_time , int max_reply_dislike_count , String user_gender , PostCategory category
            , String title , int no_of_reply , boolean is_bookmarked , int dislike_count , int total_page
            , String user_nickname , int status , int last_reply_user_id , int reply_dislike_count , int reply_like_count , User user
            , boolean is_adu , String remark
            , int no_of_uni_user_reply , int last_reply_time , int like_count , boolean is_hot , int max_reply_like_count ) {
        this._cat_id = cat_id;
        this._id = id;
        this._create_time = create_time;
        this._max_reply_dislike_count = max_reply_dislike_count;
        this._user_gender = user_gender;
        this._category = category;

        this._title = title;
        this._no_of_reply = no_of_reply;
        this._is_bookmarked = is_bookmarked;
        this._dislike_count = dislike_count;
        this._total_page = total_page;

        this._user_nickname = user_nickname;
        this._status = status;
        this._last_reply_user_id = last_reply_user_id;
        this._reply_dislike_count = reply_dislike_count;
        this._reply_like_count = reply_like_count;
        this._user = user;

        this._is_adu = is_adu;
        this._remark = remark;

        this._no_of_uni_user_reply = no_of_uni_user_reply;
        this._last_reply_time = last_reply_time;
        this._like_count = like_count;
        this._is_hot = is_hot;
        this._max_reply_like_count = max_reply_like_count;
    }

    public PostThread( JSONObject c ) throws JSONException {
        this(
                c.getInt("cat_id")
                , c.getInt("thread_id")
                , c.getInt("create_time")
                , c.getInt("max_reply_dislike_count")
                , c.getString("user_gender")
                , new PostCategory( c.getJSONObject("category") )
                , c.getString("title")
                , c.getInt("no_of_reply")
                , c.getBoolean("is_bookmarked")
                , c.getInt("dislike_count")
                , c.getInt("total_page")
                , c.getString("user_nickname")
                , c.getInt("status")
                , c.getInt("last_reply_user_id")
                , c.getInt("reply_dislike_count")
                , c.getInt("reply_like_count")
                , new User( c.getJSONObject("user") )
                , c.getBoolean("is_adu")
                , c.getJSONObject("remark").toString()
                , c.getInt("no_of_uni_user_reply")
                , c.getInt("last_reply_time")
                , c.getInt("like_count")
                , c.getBoolean("is_hot")
                , c.getInt("max_reply_like_count")
        );
    }


    public void setCatId( int cat_id ) { this._cat_id = cat_id; }
    public void setId( int id ) { this._id = id; }
    public void setCreateTime( int create_time ) { this._create_time = create_time; }
    public void setMaxReplyDislikeCount( int max_reply_dislike_count ) { this._max_reply_dislike_count = max_reply_dislike_count; }
    public void setUserGender( String user_gender ) { this._user_gender = user_gender; }
    public void setCategory( PostCategory category ) { this._category = category; }

    public void setTitle( String title ) { this._title = title; }
    public void setNoOfReply( int no_of_reply ) { this._no_of_reply = no_of_reply; }
    public void setIsBookmarked( boolean is_bookmarked ) { this._is_bookmarked = is_bookmarked; }
    public void setDislikeCount( int dislike_count ) { this._dislike_count = dislike_count; }
    public void setTotalPage( int total_page ) { this._total_page = total_page; }
    public void setUserNickname( String user_nickname ) { this._user_nickname = user_nickname; }

    public void setStatus( int status ) { this._status = status; }
    public void setLastReplyUserId( int last_reply_user_id ) { this._last_reply_user_id = last_reply_user_id; }
    public void setReplyDislikeCount( int reply_dislike_count ) { this._reply_dislike_count = reply_dislike_count; }
    public void setReplyLikeCount( int reply_like_count ) { this._reply_like_count = reply_like_count; }
    public void setUser( User user ) { this._user = user; }

    public void setIsAdu( boolean is_adu ) { this._is_adu = is_adu; }
    public void setRemark( String remark ) { this._remark = remark; }

    public void setNoOfUniUserReply( int no_of_uni_user_reply ) { this._no_of_uni_user_reply = no_of_uni_user_reply; }
    public void setLastReplyTime( int last_reply_time ) { this._last_reply_time = last_reply_time; }
    public void setLikeCount( int like_count ) { this._like_count = like_count; }
    public void setIsHot( boolean is_hot ) { this._is_hot = is_hot; }
    public void setMaxReplyLikeCount( int max_reply_like_count ) { this._max_reply_like_count = max_reply_like_count; }


    public int getCatId() { return _cat_id; }
    public int getId() { return _id; }
    public int getCreateTime() { return _create_time; }
    public int getMaxReplyDislikeCount() { return _max_reply_dislike_count; }
    public String getUserGender() { return _user_gender; }
    public PostCategory getCategory() { return _category; }

    public String getTitle() { return _title; }
    public int getNoOfReply() { return _no_of_reply; }
    public boolean getIsBookmarked() { return _is_bookmarked; }
    public int getDislikeCount() { return _dislike_count; }
    public int getTotalPage() { return _total_page; }
    public String getUserNickname() { return _user_nickname; }

    public int getStatus() { return _status; }
    public int getLastReplyUserId() { return _last_reply_user_id; }
    public int getReplyDislikeCount() { return _reply_dislike_count; }
    public int getReplyLikeCount() { return _reply_like_count; }
    public User getUser() { return _user; }

    public boolean getIsAdu() { return _is_adu; }
    public String getRemark() { return _remark; }

    public int getNoOfUniUserReply() { return _no_of_uni_user_reply; }
    public int getLastReplyTime() { return _last_reply_time; }
    public int getLikeCount() { return _like_count; }
    public boolean getIsHot() { return _is_hot; }
    public int getMaxReplyLikeCount() { return _max_reply_like_count; }

    public int getVoteCount() {
        return this.getLikeCount() - this.getDislikeCount();
    }
    public int getVoteColor() {
        if( this.getVoteCount() >= 100 ) return Color.parseColor("#8BC34A");
        else if( this.getVoteCount() <= -100 ) return Color.parseColor("#F44336");
        return Color.parseColor("#FFFFFF");
    }
}