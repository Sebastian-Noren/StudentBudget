package se.hkr.studentbudget.overview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import se.hkr.studentbudget.R;
import se.hkr.studentbudget.transactions.Transactions;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionHolder> {

    private Context context;
    private ArrayList<Transactions> transactions;

    public TransactionAdapter(Context context, ArrayList<Transactions> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transaction, parent, false);
        return new TransactionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHolder holder, int position) {
        Transactions transactions = this.transactions.get(position);
        holder.setDetails(transactions);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    class TransactionHolder extends RecyclerView.ViewHolder {

        private TextView txtCategory, txtAccount, txtAmount, txtDate;
        private ImageView icon;
        private View sds;

        TransactionHolder(View itemView) {
            super(itemView);
            sds = itemView.findViewById(R.id.rectangle_at_the_top);
            txtCategory = itemView.findViewById(R.id.trans_item_category);
            txtAccount = itemView.findViewById(R.id.trans_item_account);
            txtAmount = itemView.findViewById(R.id.trans_item_amount);
            txtDate = itemView.findViewById(R.id.trans_item_date);
            icon = itemView.findViewById(R.id.trans_item_image);
        }

        void setDetails(Transactions details) {
            icon.setImageResource(details.getImage());
            txtCategory.setText(details.getCategory());
            txtAmount.setText(String.format("%,.2f", details.getValue()));
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String strDate = dateFormatter.format(details.getTransactionDate());
            txtAccount.setText(details.getTransactionAccount());
            txtDate.setText(strDate);

            if (details.getTransactionType().equals("expense")) {
                sds.setBackgroundColor(context.getColor(R.color.colorExpense));
            }else {
                sds.setBackgroundColor(context.getColor(R.color.colorIncome));
            }

        }
    }
}