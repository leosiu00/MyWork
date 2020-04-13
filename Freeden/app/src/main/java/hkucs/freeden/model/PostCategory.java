package hkucs.freeden.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import static android.content.ContentValues.TAG;

/**
 * Created by lshung on 22/11/2017.
 */

public class PostCategory implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean _postable;
    private int _id;
    private String _name;

    public PostCategory() {
    }

    public PostCategory( int id , String name ) {
        this._id = id;
        this._name = name;
    }

    public PostCategory( boolean postable , int id , String name ) {
        this._postable = postable;
        this._id = id;
        this._name = name;
    }

    public PostCategory( JSONObject c ) throws JSONException {
        this(
                c.getBoolean("postable")
                , c.getInt("cat_id")
                , c.getString("name")
        );
    }

    public void setPostable( boolean postable ) { this._postable = postable; }
    public void setId( int id ) { this._id = id; }
    public void setName( String name ) { this._name = name; }

    public boolean getPostable() { return _postable; }
    public int getId() { return _id; }
    public String getName() { return _name; }
}