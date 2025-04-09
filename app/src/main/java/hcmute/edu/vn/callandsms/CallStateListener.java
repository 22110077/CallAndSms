package hcmute.edu.vn.callandsms;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallStateListener extends PhoneStateListener {

    private Context context;

    public CallStateListener(Context context) {
        this.context = context;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        if (state == TelephonyManager.CALL_STATE_RINGING) {
            Log.d("CallListener", "Cuộc gọi đến từ: " + incomingNumber);
            NotificationHelper.showNotification(context, "Cuộc gọi đến", "Từ số: " + incomingNumber);
        }
    }
}
