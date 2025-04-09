package hcmute.edu.vn.callandsms.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.callandsms.ConversationActivity;
import hcmute.edu.vn.callandsms.R;
import hcmute.edu.vn.callandsms.adapter.SmsAdapter;
import hcmute.edu.vn.callandsms.database.AppDatabase;
import hcmute.edu.vn.callandsms.database.SmsEntity;
import hcmute.edu.vn.callandsms.viewmodel.SmsViewModel;

public class SmsFragment extends Fragment {
    private SmsViewModel smsViewModel;
    private SmsAdapter smsAdapter;
    private RecyclerView recyclerView;

    public SmsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_sms, container, false); // Chỉ inflate layout, KHÔNG xử lý RecyclerView ở đây
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        recyclerView = view.findViewById(R.id.recyclerViewSms);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        smsAdapter = new SmsAdapter(requireContext(), new ArrayList<>());
        recyclerView.setAdapter(smsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        smsAdapter.setOnSmsClickListener(sms -> {
            if (sms.getPhoneNumber() != null) {
                Intent intent = new Intent(requireContext(), ConversationActivity.class);
                intent.putExtra("phoneNumber", sms.getPhoneNumber());
                startActivity(intent);
                Log.d("SMS_FRAGMENT", "Clicked SMS with phone: " + sms.getPhoneNumber());
            } else {
                Log.d("SMS_FRAGMENT", "Phone number is null");
            }
        });

        smsViewModel = new ViewModelProvider(this).get(SmsViewModel.class);
        smsViewModel.getSmsList().observe(getViewLifecycleOwner(), new Observer<List<SmsEntity>>() {
            @Override
            public void onChanged(List<SmsEntity> smsEntities) {
                Log.d("SMS_FRAGMENT", "Reloading SMS from DB: " + smsEntities.size());
                smsAdapter.setSmsList(smsEntities);
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.sms_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_new) {
            startActivity(new Intent(requireContext(), NewMessageActivity.class));
            return true;
        } else if (id == R.id.menu_delete) {
            if (smsAdapter.getSelectedSmsIds().isEmpty()) {
                Toast.makeText(requireContext(), "Please select messages to delete", Toast.LENGTH_SHORT).show();
            } else {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Delete selected messages?")
                        .setMessage("Are you sure you want to delete the selected messages?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            List<Integer> selectedIds = smsAdapter.getSelectedSmsIds();
                            new Thread(() -> {
                                AppDatabase db = AppDatabase.getInstance(requireContext());
                                db.smsDao().deleteSmsByIds(selectedIds);
                            }).start();
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
            return true;
        } else if (id == R.id.menu_refresh) {
            // Không cần gọi lại thủ công vì LiveData auto update
            Toast.makeText(requireContext(), "Đã làm mới danh sách SMS", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}