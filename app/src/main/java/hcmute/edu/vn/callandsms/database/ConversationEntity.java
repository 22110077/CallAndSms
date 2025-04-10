package hcmute.edu.vn.callandsms.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "conversation_table")
public class ConversationEntity {
    @PrimaryKey
    @NonNull
    public String phoneNumber;

    public String latestMessage;
    public long time;
}
