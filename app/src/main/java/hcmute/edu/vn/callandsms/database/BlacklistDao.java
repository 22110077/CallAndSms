package hcmute.edu.vn.callandsms.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BlacklistDao {

    @Insert
    void insert(BlacklistEntity entity);

    @Delete
    void delete(BlacklistEntity entity);

    @Query("SELECT * FROM blacklist")
    LiveData<List<BlacklistEntity>> getAll();

    @Query("SELECT * FROM blacklist WHERE phoneNumber = :phone LIMIT 1")
    BlacklistEntity findByPhone(String phone);

    @Query("SELECT * FROM blacklist WHERE phoneNumber = :phoneNumber LIMIT 1")
    BlacklistEntity findByPhoneSync(String phoneNumber);

}
