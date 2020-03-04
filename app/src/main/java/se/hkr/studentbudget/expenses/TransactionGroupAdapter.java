package se.hkr.studentbudget.expenses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import se.hkr.studentbudget.R;
import se.hkr.studentbudget.transactions.TransactionAdapter;

public class TransactionGroupAdapter extends RecyclerView.Adapter<TransactionGroupAdapter.ItemViewHolder> {

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private ArrayList<TransactionGroup> itemList;
    private Context mContext;

    TransactionGroupAdapter(Context context, ArrayList<TransactionGroup> itemList) {
        this.itemList = itemList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_transactiongroup, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        TransactionGroup item = itemList.get(i);
        itemViewHolder.tvItemTitle.setText(item.getItemTitle());

        //TODO set value in sek and change color depending on
        itemViewHolder.monthValue.setText(String.valueOf(item.getValue()));

        // Create layout manager with initial prefetch item count
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                itemViewHolder.rvSubItem.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(item.getSubItemList().size());

        // Create sub item view adapter
        TransactionAdapter transactionAdapter = new TransactionAdapter(mContext, item.getSubItemList());

        itemViewHolder.rvSubItem.setLayoutManager(layoutManager);
        itemViewHolder.rvSubItem.setAdapter(transactionAdapter);
        itemViewHolder.rvSubItem.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemTitle, monthValue;
        private RecyclerView rvSubItem;

        ItemViewHolder(View itemView) {
            super(itemView);
            tvItemTitle = itemView.findViewById(R.id.tv_item_title);
            monthValue = itemView.findViewById(R.id.trans_item_total);
            rvSubItem = itemView.findViewById(R.id.rv_sub_item);
        }
    }
}
