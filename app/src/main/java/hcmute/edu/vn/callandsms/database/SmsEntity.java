package hcmute.edu.vn.callandsms.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "sms_table")
public class SmsEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "phone_number")
    private String phoneNumber;

    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name = "time")
    private long time;
    public boolean sentByMe;

    // Constructor dùng bởi Room
    public SmsEntity(String content, long time, String phoneNumber, boolean sentByMe) {
        this.content = content;
        this.time = time;
        this.phoneNumber = phoneNumber;
        this.sentByMe = sentByMe;
    }

    // Constructor mặc định
    @Ignore
    public SmsEntity() {
    }
    // Constructor khác - dùng cho hiển thị, không dùng bởi Room
    @Ignore
    public SmsEntity(String content, long time) {
        this.content = content;
        this.time = time;
    }

    // Getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public long getTime() { return time; }
    public void setTime(long time) { this.time = time; }
    public boolean isSentByMe() { return sentByMe; }
    public void setSentByMe(boolean sentByMe) { this.sentByMe = sentByMe; }
}
