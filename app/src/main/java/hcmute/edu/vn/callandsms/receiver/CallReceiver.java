package hcmute.edu.vn.callandsms.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.concurrent.Executors;

import hcmute.edu.vn.callandsms.database.AppDatabase;

public class CallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("CALL_RECEIVER", "Call received");

        if (!Intent.ACTION_NEW_OUTGOING_CALL.equals(intent.getAction())) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            if (TelephonyManager.EXTRA_STATE_RINGING.equals(state) && incomingNumber != null) {
                Log.d("CALL_RECEIVER", "Incoming number: " + incomingNumber);

                // Check blacklist in background thread
                Executors.newSingleThreadExecutor().execute(() -> {
                    boolean isBlacklisted = AppDatabase.getInstance(context)
                            .blacklistDao()
                            .isNumberBlacklisted(incomingNumber);

                    if (isBlacklisted) {
                        Log.d("CALL_RECEIVER", "Number is blacklisted, ending call...");

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
                            if (telecomManager != null) {
                                boolean success = telecomManager.endCall();
                                Log.d("CALL_RECEIVER", "Call end requested. Success: " + success);
                            }
                        }
                    }
                });
            }
        }
    }
}
