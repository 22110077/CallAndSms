package hcmute.edu.vn.callandsms.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hcmute.edu.vn.callandsms.R;
import hcmute.edu.vn.callandsms.database.SmsEntity;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<SmsEntity> smsList;

    public MessageAdapter(List<SmsEntity> smsList) {
        this.smsList = smsList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        SmsEntity sms = smsList.get(position);
        holder.textViewMessage.setText(sms.getContent());

        if (sms.isSentByMe()) {
            holder.layoutMessage.setGravity(Gravity.END);
            holder.textViewMessage.setBackgroundResource(R.drawable.bg_message_me);
        } else {
            holder.layoutMessage.setGravity(Gravity.START);
            holder.textViewMessage.setBackgroundResource(R.drawable.bg_message_other);
        }
    }

    @Override
    public int getItemCount() {
        return smsList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutMessage;
        TextView textViewMessage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutMessage = itemView.findViewById(R.id.layoutMessage);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
        }
    }
    public void setSmsList(List<SmsEntity> smsList) {
        this.smsList = smsList;
        notifyDataSetChanged();
    }

}
