package hcmute.edu.vn.callandsms.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import hcmute.edu.vn.callandsms.database.AppDatabase;
import hcmute.edu.vn.callandsms.database.CallEntity;

public class CallViewModel extends AndroidViewModel {

    private final LiveData<List<CallEntity>> callList;

    public CallViewModel(@NonNull Application application) {
        super(application);
        callList = AppDatabase.getInstance(application).callDao().getAllCalls();
    }

    public LiveData<List<CallEntity>> getCallList() {
        return callList;
    }
}
