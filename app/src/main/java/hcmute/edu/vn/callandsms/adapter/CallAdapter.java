package hcmute.edu.vn.callandsms.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.List;

import hcmute.edu.vn.callandsms.R;
import hcmute.edu.vn.callandsms.database.CallEntity;

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.CallViewHolder> {

    private List<CallEntity> callList;

    public void setCallList(List<CallEntity> list) {
        this.callList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_call, parent, false);
        return new CallViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallViewHolder holder, int position) {
        CallEntity call = callList.get(position);
        holder.phoneText.setText(call.phoneNumber);
        holder.typeText.setText(call.callType);
        holder.timeText.setText(DateFormat.getDateTimeInstance().format(call.timestamp));
    }

    @Override
    public int getItemCount() {
        return callList != null ? callList.size() : 0;
    }

    public static class CallViewHolder extends RecyclerView.ViewHolder {
        TextView phoneText, typeText, timeText;

        public CallViewHolder(@NonNull View itemView) {
            super(itemView);
            phoneText = itemView.findViewById(R.id.textPhone);
            typeText = itemView.findViewById(R.id.textType);
            timeText = itemView.findViewById(R.id.textTime);
        }
    }
}
