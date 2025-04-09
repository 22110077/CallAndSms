package hcmute.edu.vn.callandsms.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import hcmute.edu.vn.callandsms.database.AppDatabase;
import hcmute.edu.vn.callandsms.database.SmsEntity;
import hcmute.edu.vn.callandsms.database.SmsDao;
public class SmsViewModel extends AndroidViewModel {

    private final SmsDao smsDao; // ✅ Thêm dòng này
    private final LiveData<List<SmsEntity>> smsList;

    public SmsViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        smsDao = db.smsDao(); // ✅ Gán biến smsDao
        smsList = smsDao.getAllSms();
    }

    public LiveData<List<SmsEntity>> getSmsList() {
        return smsList;
    }

    public void insert(SmsEntity sms) {
        new Thread(() -> smsDao.insertSms(sms)).start(); // ✔ giờ đã hợp lệ
    }
}