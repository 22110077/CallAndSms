package hcmute.edu.vn.callandsms.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface SmsDao {
    @Query("SELECT * FROM sms_table ORDER BY time DESC")
    LiveData<List<SmsEntity>> getAllSms();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSms(SmsEntity sms);
    // Thêm phương thức xóa tin nhắn theo danh sách ID
    @Query("DELETE FROM sms_table WHERE id IN (:smsIds)")
    void deleteSmsByIds(List<Integer> smsIds);
    @Query("SELECT * FROM sms_table WHERE phone_number = :phoneNumber ORDER BY time ASC")
    LiveData<List<SmsEntity>> getSmsByPhoneNumber(String phoneNumber);
}
