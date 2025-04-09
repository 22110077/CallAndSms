package hcmute.edu.vn.callandsms.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "blacklist")
public class BlacklistEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String phoneNumber;

    public BlacklistEntity(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
