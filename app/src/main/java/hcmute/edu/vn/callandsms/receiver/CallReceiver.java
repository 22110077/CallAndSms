package hcmute.edu.vn.callandsms.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import hcmute.edu.vn.callandsms.service.CallService;

public class CallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("CallReceiver", "onReceive: action = " + intent.getAction());

        if (intent.getAction() != null && intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
            Intent serviceIntent = new Intent(context, CallService.class);
            serviceIntent.putExtras(intent); // chuyển tiếp toàn bộ intent
            context.startService(serviceIntent);
        }
    }
}