package hcmute.edu.vn.callandsms.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CallDao {
    @Insert
    void insert(CallEntity call);

    @Query("SELECT * FROM call_table ORDER BY timestamp DESC")
    LiveData<List<CallEntity>> getAllCalls();
}
