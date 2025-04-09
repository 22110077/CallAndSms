package hcmute.edu.vn.callandsms.receiver;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executors;

import hcmute.edu.vn.callandsms.R;
import hcmute.edu.vn.callandsms.database.AppDatabase;
import hcmute.edu.vn.callandsms.database.SmsEntity;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                String format = bundle.getString("format"); // Important for API >=23

                if (pdus != null) {
                    for (Object pdu : pdus) {
                        SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu, format);
                        String sender = sms.getDisplayOriginatingAddress();
                        String messageBody = sms.getMessageBody();
                        long timestamp = sms.getTimestampMillis();
                        Log.d("SMS_RECEIVER", "Insert: " + messageBody + " - phone: " + sender);

                        // Show notification
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "SMS_CHANNEL")
                                .setSmallIcon(R.drawable.ic_sms)
                                .setContentTitle("Tin nhắn mới từ: " + sender)
                                .setContentText(messageBody)
                                .setPriority(NotificationCompat.PRIORITY_HIGH);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                                    != PackageManager.PERMISSION_GRANTED) {
                                // Không có quyền gửi thông báo, bạn có thể log hoặc bỏ qua
                                return;
                            }
                        }
                        notificationManager.notify(0, builder.build());

                        SmsEntity smsEntity = new SmsEntity();
                        smsEntity.setContent(messageBody);
                        smsEntity.setTime(timestamp);
                        smsEntity.setPhoneNumber(sender);
                        smsEntity.setSentByMe(false);

                        Log.d("DEBUG", "Before insert to DB: " + messageBody);
                        Executors.newSingleThreadExecutor().execute(() -> {
                            try {
                                AppDatabase.getInstance(context.getApplicationContext())
                                        .smsDao()
                                        .insertSms(smsEntity);
                                Log.d("DEBUG", "After insert to DB: " + messageBody);
                            } catch (Exception e) {
                                Log.e("DEBUG", "Insert failed: " + e.getMessage(), e);
                            }
                        });

                        // Check blacklist
                        if (isNumberBlacklisted(sender, context)) {
                            abortBroadcast(); // Chặn tin nhắn nếu số nằm trong blacklist
                        }
                    }
                }
            }
        }
    }

    // Kiểm tra số điện thoại có nằm trong danh sách đen không
    private boolean isNumberBlacklisted(String phoneNumber, Context context) {
        return context.getSharedPreferences("blacklist", Context.MODE_PRIVATE)
                .contains(phoneNumber);
    }
}