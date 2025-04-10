package hcmute.edu.vn.callandsms;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.callandsms.adapter.MessageAdapter;
import hcmute.edu.vn.callandsms.database.AppDatabase;
import hcmute.edu.vn.callandsms.database.ConversationEntity;
import hcmute.edu.vn.callandsms.database.SmsDao;
import hcmute.edu.vn.callandsms.database.SmsEntity;

public class ConversationActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private List<SmsEntity> messageList = new ArrayList<>();
    private String phoneNumber;
    private EditText editTextMessage;
    private ImageButton buttonSend;
    private TextView textViewPhoneNumber;
    private ImageButton buttonBack;

    private String normalizePhoneNumber(String number) {
        if (number == null) return "";
        if (number.startsWith("+84")) {
            return number.replace("+84", "0");
        }
        return number;
    }

    private void checkPhoneNumberInDatabase(String phoneNumber) {
        AppDatabase db = AppDatabase.getInstance(this);
        SmsDao smsDao = db.smsDao();

        smsDao.getSmsByPhoneNumber(phoneNumber).observe(this, smsEntity -> {
            if (smsEntity != null) {
                Log.d("DEBUG", "Số điện thoại đã tồn tại trong cơ sở dữ liệu: " + phoneNumber);
            } else {
                Log.d("DEBUG", "Số điện thoại không tồn tại trong cơ sở dữ liệu: " + phoneNumber);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        // Lấy số điện thoại từ Intent
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        Log.d("CONVERSATION_ACTIVITY", "Received phone number: " + phoneNumber);

        if (phoneNumber == null || phoneNumber.isEmpty()) {
            Toast.makeText(this, "Không nhận được số điện thoại", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        phoneNumber = normalizePhoneNumber(phoneNumber);
        Log.d("DEBUG", "Received phone number after normalize: " + phoneNumber);

        // Gán số điện thoại vào header
        textViewPhoneNumber = findViewById(R.id.textViewPhoneNumber);
        buttonBack = findViewById(R.id.buttonBack);
        textViewPhoneNumber.setText(phoneNumber);
        buttonBack.setOnClickListener(v -> finish());

        checkPhoneNumberInDatabase(phoneNumber);

        recyclerView = findViewById(R.id.recyclerViewConversation);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(adapter);

        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        buttonSend.setOnClickListener(v -> sendMessage());

        loadMessages();
    }

    private void loadMessages() {
        AppDatabase db = AppDatabase.getInstance(this);

        db.smsDao().getSmsByPhoneNumber(phoneNumber).observe(this, smsEntities -> {
            Log.d("DEBUG", "Loading messages for: " + phoneNumber);

            messageList.clear();
            messageList.addAll(smsEntities);
            adapter.notifyDataSetChanged();

            if (!messageList.isEmpty()) {
                recyclerView.scrollToPosition(messageList.size() - 1);
            }
        });
    }

    private void sendMessage() {
        String content = editTextMessage.getText().toString().trim();
        if (content.isEmpty()) return;

        SmsEntity newSms = new SmsEntity();
        newSms.setContent(content);
        newSms.setPhoneNumber(normalizePhoneNumber(phoneNumber));
        newSms.setTime(System.currentTimeMillis());
        newSms.setSentByMe(true);

        editTextMessage.setText("");

        messageList.add(newSms);
        adapter.notifyItemInserted(messageList.size() - 1);
        recyclerView.scrollToPosition(messageList.size() - 1);

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);

            // 1. Lưu tin nhắn mới
            db.smsDao().insertSms(newSms);

            // 2. Cập nhật conversation_table
            ConversationEntity conversation = new ConversationEntity();
            conversation.phoneNumber = newSms.getPhoneNumber();
            conversation.latestMessage = newSms.getContent();
            conversation.time = newSms.getTime();
            db.conversationDao().insertOrUpdate(conversation);

            Log.d("SMS_DEBUG", "Inserted message and updated conversation");
        }).start();

    }
}