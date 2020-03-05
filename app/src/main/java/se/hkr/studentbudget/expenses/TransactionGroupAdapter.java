package se.hkr.studentbudget.expenses;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

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

        float total = (float) item.getValue();
        if (total < 0) {
            itemViewHolder.monthValue.setTextColor(mContext.getColor(R.color.colorExpense));
        }
        ValueAnimator animator = ValueAnimator.ofFloat(0, total); //0 is min number, 600 is max number
        animator.setDuration(1000); //Duration is in milliseconds
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float amount = (float) animation.getAnimatedValue();
                itemViewHolder.monthValue.setText(String.format("%s kr", String.format(Locale.getDefault(),"%,.2f", amount)));
            }
        });
        animator.start();

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
