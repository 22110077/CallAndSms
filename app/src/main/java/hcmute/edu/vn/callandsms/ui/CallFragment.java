package hcmute.edu.vn.callandsms.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hcmute.edu.vn.callandsms.R;
import hcmute.edu.vn.callandsms.adapter.CallAdapter;
import hcmute.edu.vn.callandsms.service.BlacklistActivity;
import hcmute.edu.vn.callandsms.viewmodel.CallViewModel;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CallFragment extends Fragment {

    private CallViewModel callViewModel;
    private CallAdapter callAdapter;

    public CallFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_call, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewCall);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        callAdapter = new CallAdapter();
        recyclerView.setAdapter(callAdapter);

        callViewModel = new ViewModelProvider(this).get(CallViewModel.class);
        callViewModel.getCallList().observe(getViewLifecycleOwner(), callList -> {
            callAdapter.setCallList(callList);
        });

        // 👉 Thêm đoạn này để mở BlacklistActivity khi bấm nút
        FloatingActionButton btnGoToBlacklist = view.findViewById(R.id.btnGoToBlacklist);
        btnGoToBlacklist.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), BlacklistActivity.class);
            startActivity(intent);
        });
    }
}
