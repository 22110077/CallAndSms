package hcmute.edu.vn.callandsms.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import hcmute.edu.vn.callandsms.database.AppDatabase;
import hcmute.edu.vn.callandsms.database.BlacklistDao;
import hcmute.edu.vn.callandsms.database.BlacklistEntity;

public class BlacklistRepository {
    private static BlacklistRepository instance;
    private final BlacklistDao blacklistDao;

    private BlacklistRepository(Context context) {
        blacklistDao = AppDatabase.getInstance(context).blacklistDao();
    }

    public static synchronized BlacklistRepository getInstance(Context context) {
        if (instance == null) {
            instance = new BlacklistRepository(context.getApplicationContext());
        }
        return instance;
    }

    public boolean isBlacklisted(String phoneNumber) {
        BlacklistEntity entity = blacklistDao.findByPhoneSync(phoneNumber);
        return entity != null;
    }
}