package hcmute.edu.vn.callandsms.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.callandsms.R;
import hcmute.edu.vn.callandsms.database.BlacklistEntity;

public class BlacklistAdapter extends RecyclerView.Adapter<BlacklistAdapter.BlacklistViewHolder> {

    private List<BlacklistEntity> blacklist = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onRemoveClick(BlacklistEntity entity);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setBlacklist(List<BlacklistEntity> blacklist) {
        this.blacklist = blacklist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BlacklistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_blacklist, parent, false);
        return new BlacklistViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BlacklistViewHolder holder, int position) {
        BlacklistEntity entity = blacklist.get(position);
        holder.tvPhoneNumber.setText(entity.phoneNumber);
        holder.btnRemove.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRemoveClick(entity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return blacklist.size();
    }

    static class BlacklistViewHolder extends RecyclerView.ViewHolder {
        TextView tvPhoneNumber;
        Button btnRemove;

        public BlacklistViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}
