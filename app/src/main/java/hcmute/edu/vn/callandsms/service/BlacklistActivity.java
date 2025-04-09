package hcmute.edu.vn.callandsms.service;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import hcmute.edu.vn.callandsms.R;
import hcmute.edu.vn.callandsms.adapter.BlacklistAdapter;
import hcmute.edu.vn.callandsms.database.BlacklistEntity;
import hcmute.edu.vn.callandsms.viewmodel.BlacklistViewModel;

public class BlacklistActivity extends AppCompatActivity {

    private BlacklistViewModel viewModel;
    private BlacklistAdapter adapter;
    private EditText phoneNumberEditText;
    private Button addButton;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacklist);

        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        addButton = findViewById(R.id.addButton);
        recyclerView = findViewById(R.id.recyclerViewBlacklist);

        viewModel = new ViewModelProvider(this).get(BlacklistViewModel.class);

        adapter = new BlacklistAdapter();
        adapter.setOnItemClickListener(entity -> {
            viewModel.delete(entity);
            Toast.makeText(this, "Removed: " + entity.phoneNumber, Toast.LENGTH_SHORT).show();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        viewModel.getBlacklist().observe(this, adapter::setBlacklist);

        addButton.setOnClickListener(v -> {
            String phone = phoneNumberEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(phone)) {
                viewModel.insert(new BlacklistEntity(phone));
                phoneNumberEditText.setText("");
                Toast.makeText(this, "Added: " + phone, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Enter phone number", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
