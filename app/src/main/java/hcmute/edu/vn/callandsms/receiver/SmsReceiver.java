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
import android.widget.Toast;

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
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            Log.d("SmsReceiver", "Permission RECEIVE_SMS not granted");

            // 👉 Gửi broadcast về Activity hoặc thông báo bằng Toast (nếu có)
            Toast.makeText(context, "Không có quyền đọc SMS. Vui lòng cấp quyền trong Cài đặt.", Toast.LENGTH_LONG).show();

            return;
        }
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
                    // Kiểm tra quyền RECEIVE_SMS
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS)
                            != PackageManager.PERMISSION_GRANTED) {
                        Log.d("SMS_RECEIVER", "Chưa có quyền RECEIVE_SMS");
                        return;
                    }

                    // Kiểm tra quyền POST_NOTIFICATIONS (Android 13+)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                                != PackageManager.PERMISSION_GRANTED) {
                            Log.d("SMS_RECEIVER", "Chưa có quyền POST_NOTIFICATIONS");
                            return;
                        }
                    }

                    // Tạo thông báo
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "SMS_CHANNEL")
                            .setSmallIcon(R.drawable.ic_sms)
                            .setContentTitle("Tin nhắn mới từ: " + sender)
                            .setContentText(messageBody)
                            .setPriority(NotificationCompat.PRIORITY_HIGH);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                    int notificationId = (int) System.currentTimeMillis(); // hoặc sender.hashCode();
                    notificationManager.notify(notificationId, builder.build());

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

                    Log.d("SMS_RECEIVER", "Có quyền RECEIVE_SMS => xử lý tin nhắn");

                    // Check blacklist
                    if (isNumberBlacklisted(sender, context)) {
                        abortBroadcast(); // Chặn tin nhắn nếu số nằm trong blacklist
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