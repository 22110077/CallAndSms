package hcmute.edu.vn.callandsms.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import java.util.concurrent.Executors;

import android.content.pm.PackageManager;

import hcmute.edu.vn.callandsms.database.AppDatabase;
import hcmute.edu.vn.callandsms.database.CallEntity;
import hcmute.edu.vn.callandsms.repository.BlacklistRepository;

public class CallService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state) && phoneNumber != null) {
            Log.d("CallService", "Incoming call: " + phoneNumber);

            Executors.newSingleThreadExecutor().execute(() -> {
                boolean isBlacklisted = BlacklistRepository.getInstance(getApplicationContext()).isBlacklisted(phoneNumber);

                if (isBlacklisted) {
                    rejectCall();
                    saveToCallHistory(phoneNumber, "REJECTED");
                } else {
                    saveToCallHistory(phoneNumber, "INCOMING");
                }
            });
        }

        return START_NOT_STICKY;
    }

    private void rejectCall() {
        TelecomManager telecomManager = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_GRANTED) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) { // API 28+
                telecomManager.endCall();
                Log.d("CallService", "Call rejected using endCall()");
            } else {
                Log.w("CallService", "endCall() not supported on this Android version");
            }
        } else {
            Log.w("CallService", "Permission ANSWER_PHONE_CALLS not granted");
        }
    }

    private void saveToCallHistory(String phoneNumber, String callType) {
        CallEntity call = new CallEntity(phoneNumber, System.currentTimeMillis(), callType);
        AppDatabase.getInstance(this).callDao().insert(call);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}