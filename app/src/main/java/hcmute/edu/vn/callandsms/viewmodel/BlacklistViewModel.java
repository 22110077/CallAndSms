package hcmute.edu.vn.callandsms.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import hcmute.edu.vn.callandsms.database.AppDatabase;
import hcmute.edu.vn.callandsms.database.BlacklistDao;
import hcmute.edu.vn.callandsms.database.BlacklistEntity;

public class BlacklistViewModel extends AndroidViewModel {

    private final BlacklistDao blacklistDao;
    private final LiveData<List<BlacklistEntity>> blacklist;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public BlacklistViewModel(@NonNull Application application) {
        super(application);
        blacklistDao = AppDatabase.getInstance(application).blacklistDao();
        blacklist = blacklistDao.getAll();
    }

    public LiveData<List<BlacklistEntity>> getBlacklist() {
        return blacklist;
    }

    public void insert(BlacklistEntity entity) {
        executor.execute(() -> blacklistDao.insert(entity));
    }

    public void delete(BlacklistEntity entity) {
        executor.execute(() -> blacklistDao.delete(entity));
    }
}
