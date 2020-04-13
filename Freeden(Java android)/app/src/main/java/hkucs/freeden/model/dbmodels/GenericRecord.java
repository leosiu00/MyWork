package hkucs.freeden.model.dbmodels;

/**
 * Created by lshung on 10/11/2017.
 */

public class GenericRecord {
    private int _id;
    private long _create_date;

    public GenericRecord() {
    }

    public GenericRecord(int id ) {
        this._id = id;
        this._create_date = System.currentTimeMillis();
    }

    public GenericRecord(int id , long create_date ) {
        this._id = id;
        this._create_date = create_date;
    }

    public void setId( int id ) { this._id = id; }
    public void setCreateDate( long create_date ) { this._create_date = create_date; }

    public int getId() { return _id; }
    public long getCreateDate() { return _create_date; }
}
