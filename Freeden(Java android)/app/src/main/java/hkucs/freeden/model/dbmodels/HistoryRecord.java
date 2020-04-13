package hkucs.freeden.model.dbmodels;

/**
 * Created by lshung on 10/11/2017.
 */

public class HistoryRecord {
    private int _id;
    private int _msg_num;
    private int _page;
    private long _last_mod_date;

    public HistoryRecord() {
    }

    public HistoryRecord( int id , int msg_num , int page ) {
        this._id = id;
        this._msg_num = msg_num;
        this._page = page;
        this._last_mod_date = System.currentTimeMillis();
    }

    public HistoryRecord( int id , int msg_num , int page , long last_mod_date ) {
        this._id = id;
        this._msg_num = msg_num;
        this._page = page;
        this._last_mod_date = last_mod_date;
    }

    public void setId( int id ) { this._id = id; }
    public void setMsgNum( int msg_num ) { this._msg_num = msg_num; }
    public void setPage( int page ) { this._page = page; }
    public void setLastModDate( long last_mod_date ) { this._last_mod_date = last_mod_date; }

    public int getId() { return _id; }
    public int getMsgNum() { return _msg_num; }
    public int getPage() { return _page; }
    public long getLastModDate() { return _last_mod_date; }
}