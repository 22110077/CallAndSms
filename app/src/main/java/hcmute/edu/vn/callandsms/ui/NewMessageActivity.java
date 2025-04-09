package hcmute.edu.vn.callandsms.ui;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.callandsms.R;
import hcmute.edu.vn.callandsms.adapter.MessageAdapter;
import androidx.lifecycle.ViewModelProvider;

import hcmute.edu.vn.callandsms.database.SmsEntity;
import hcmute.edu.vn.callandsms.viewmodel.SmsViewModel;

public class NewMessageActivity extends AppCompatActivity {
    private EditText editTextPhoneNumber;
    private EditText editTextMessage;
    private Button buttonSend;
    private SmsViewModel smsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        smsViewModel = new ViewModelProvider(this).get(SmsViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
            getSupportActionBar().setTitle("New Message");
        }

        // Ánh xạ view
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);
        String phoneNumber = getIntent().getStringExtra("phoneNumber");

        // Gán dữ liệu RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewMessages);
        List<SmsEntity> smsList = new ArrayList<>();

        MessageAdapter adapter = new MessageAdapter(smsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Gửi SMS
        buttonSend.setOnClickListener(v -> {
            sendSms();

            // Cập nhật tin nhắn vào danh sách và làm mới adapter
            String message = editTextMessage.getText().toString();
            if (!message.isEmpty()) {
                SmsEntity sms = new SmsEntity(message, System.currentTimeMillis(), phoneNumber, true);
                smsList.add(sms);
                adapter.notifyItemInserted(smsList.size() - 1);
                recyclerView.scrollToPosition(smsList.size() - 1);
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void sendSms() {
        String phoneNumber = editTextPhoneNumber.getText().toString();
        String message = editTextMessage.getText().toString();

        if (phoneNumber.isEmpty() || message.isEmpty()) {
            Toast.makeText(this, "Please enter both phone number and message", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(NewMessageActivity.this, "Message sent successfully", Toast.LENGTH_SHORT).show();

            // ✅ Lưu vào Room database
            SmsEntity sms = new SmsEntity(message, System.currentTimeMillis(), phoneNumber, true);
            smsViewModel.insert(sms);

            // Quay lại màn hình trước
            Intent intent = new Intent(NewMessageActivity.this, SmsFragment.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to send message: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
