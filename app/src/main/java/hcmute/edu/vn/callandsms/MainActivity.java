package hcmute.edu.vn.callandsms;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telecom.TelecomManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.callandsms.ui.CallFragment;
import hcmute.edu.vn.callandsms.ui.SmsFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 👇 THÊM ĐOẠN NÀY: Yêu cầu làm default dialer (nếu chưa phải)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String defaultSmsPackage = Telephony.Sms.getDefaultSmsPackage(this);
            if (!getPackageName().equals(defaultSmsPackage)) {
                Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, getPackageName());
                startActivity(intent);
            }
        }

        checkAndRequestPermissions();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d("PERMISSION", "RECEIVE_SMS OK");
        }
            bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Load mặc định SmsFragment
        loadFragment(new SmsFragment());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            if (item.getItemId() == R.id.nav_sms) {
                selectedFragment = new SmsFragment();
            } else if (item.getItemId() == R.id.nav_call) {
                selectedFragment = new CallFragment();
            }
            return loadFragment(selectedFragment);
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "SMS_CHANNEL",
                    "SMS Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Channel for SMS notifications");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
    private void checkAndRequestPermissions() {
        String[] permissions = {
                Manifest.permission.ANSWER_PHONE_CALLS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_SMS
        };

        List<String> permissionsToRequest = new ArrayList<>();

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }

        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    101  // request code
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 101) {
            for (int i = 0; i < permissions.length; i++) {
                Log.d("PERMISSION", permissions[i] + " -> " +
                        (grantResults[i] == PackageManager.PERMISSION_GRANTED ? "GRANTED" : "DENIED"));
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Ứng dụng cần quyền nhận SMS để hoạt động", Toast.LENGTH_LONG).show();
            } else {
                Log.d("PERMISSION", "Đã có quyền RECEIVE_SMS, mọi thứ OK!");
            }
        }
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.containerFragment, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}