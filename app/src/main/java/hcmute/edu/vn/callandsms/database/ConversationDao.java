package hcmute.edu.vn.callandsms.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ConversationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(ConversationEntity conversation);

    @Query("SELECT * FROM conversation_table ORDER BY time DESC")
    LiveData<List<ConversationEntity>> getAllConversations();

    @Query("DELETE FROM conversation_table WHERE phoneNumber = :phoneNumber")
    void deleteConversation(String phoneNumber);
}
