package hcmute.edu.vn.callandsms.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "call_table")
public class CallEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String phoneNumber;
    public long timestamp;
    public String callType; // "INCOMING", "MISSED", "REJECTED", etc.

    public CallEntity(String phoneNumber, long timestamp, String callType) {
        this.phoneNumber = phoneNumber;
        this.timestamp = timestamp;
        this.callType = callType;
    }
}
