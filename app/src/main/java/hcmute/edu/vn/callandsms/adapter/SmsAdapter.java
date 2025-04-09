package hcmute.edu.vn.callandsms.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import hcmute.edu.vn.callandsms.R;
import hcmute.edu.vn.callandsms.database.SmsEntity;

public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.SmsViewHolder> {
    private Context context;
    private List<SmsEntity> smsList;
    private List<Integer> selectedSmsIds = new ArrayList<>();
    private OnSmsClickListener listener;

    // Giao diện lắng nghe click
    public interface OnSmsClickListener {
        void onSmsClick(SmsEntity sms);
    }

    public void setOnSmsClickListener(OnSmsClickListener listener) {
        this.listener = listener;
    }

    // Constructor dùng Context
    public SmsAdapter(Context context, List<SmsEntity> smsList) {
        this.context = context;
        this.smsList = smsList != null ? smsList : new ArrayList<>();
    }

    // Cập nhật danh sách
    public void setSmsList(List<SmsEntity> list) {
        this.smsList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SmsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sms, parent, false);
        return new SmsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SmsViewHolder holder, int position) {
        SmsEntity sms = smsList.get(position);
        Log.d("SMS_ADAPTER", "Binding SMS: " + sms.getContent());

        holder.phoneText.setText(sms.getPhoneNumber());
        holder.contentText.setText(sms.getContent());
        holder.timeText.setText(DateFormat.getDateTimeInstance().format(sms.getTime()));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSmsClick(sms);
            }
        });

        holder.checkBox.setOnCheckedChangeListener(null); // Tránh trigger lại khi setChecked
        holder.checkBox.setChecked(selectedSmsIds.contains(sms.id));

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectSms(sms.id);
            } else {
                selectedSmsIds.remove((Integer) sms.id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return smsList != null ? smsList.size() : 0;
    }

    public static class SmsViewHolder extends RecyclerView.ViewHolder {
        TextView phoneText, contentText, timeText;
        CheckBox checkBox;

        public SmsViewHolder(@NonNull View itemView) {
            super(itemView);
            phoneText = itemView.findViewById(R.id.textPhone);
            contentText = itemView.findViewById(R.id.textContent);
            timeText = itemView.findViewById(R.id.textTime);
            checkBox = itemView.findViewById(R.id.checkboxSelect);
        }
    }

    // Danh sách lựa chọn
    public void selectSms(int smsId) {
        if (!selectedSmsIds.contains(smsId)) {
            selectedSmsIds.add(smsId);
        }
    }

    public List<Integer> getSelectedSmsIds() {
        return selectedSmsIds;
    }

    public void clearSelectedSms() {
        selectedSmsIds.clear();
        notifyDataSetChanged();
    }
}