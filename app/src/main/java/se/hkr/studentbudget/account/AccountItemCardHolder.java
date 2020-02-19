package se.hkr.studentbudget.account;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import se.hkr.studentbudget.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class AccountItemCardHolder extends RecyclerView.ViewHolder {

    ImageView image;
    TextView title, value, notes;

    public AccountItemCardHolder(@NonNull View itemView, final AccountAdapter.OnCardItemLongClickListener listener) {
        super(itemView);

        this.image = itemView.findViewById(R.id.account_row_img);
        this.title = itemView.findViewById(R.id.account_row_title);
        this.value = itemView.findViewById(R.id.account_value_text);
        this.notes = itemView.findViewById(R.id.account_notes);

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onCardItemLongClick(position);
                    }
                }
                return false;
            }
        });

    }

}
