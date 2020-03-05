package se.hkr.studentbudget.budget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import se.hkr.studentbudget.R;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetHolder> {

    private Context context;
    private ArrayList<BudgetItem> budgetItems;
    private OnCardItemLongClickListener onCardItemLongClickListener;

    public interface OnCardItemLongClickListener {
        void onCardItemLongClick(int pos);
    }

    public void setOnCardItemLongClickListener(OnCardItemLongClickListener listener) {
        onCardItemLongClickListener = listener;
    }
    //constructor
    public BudgetAdapter(Context context, ArrayList<BudgetItem> budgetItems) {
        this.context = context;
        this.budgetItems = budgetItems;
    }

    @NonNull
    @Override
    public BudgetHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.progessbar_budget, parent, false);
        return new BudgetHolder(view, onCardItemLongClickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull BudgetHolder holder, int position) {
        BudgetItem budgetItem = this.budgetItems.get(position);
        holder.setDetails(budgetItem);
    }

    @Override
    public int getItemCount() {
        return budgetItems.size();
    }


    //innerclass
    class BudgetHolder extends RecyclerView.ViewHolder {
        TextView title, amountprogress, totalAmountText, accountName;
        ImageView image;
        ProgressBar progressBar;

        BudgetHolder(View itemView, final BudgetAdapter.OnCardItemLongClickListener listener) {

            super(itemView);
            this.image = itemView.findViewById(R.id.progressbar_image);
            this.title = itemView.findViewById(R.id.name_of_progressbar);
            this.amountprogress = itemView.findViewById(R.id.amount_left);
            this.progressBar = itemView.findViewById(R.id.progressbar);
            this.totalAmountText = itemView.findViewById(R.id.amount_leftTotal);
            this.accountName = itemView.findViewById(R.id.accountName);


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

        private void setDetails(BudgetItem budgetItem) {
            int maxValue = (int) budgetItem.getMaxValue();
            int setCurrentProgress = (int) budgetItem.getCurrentValue();

            if (setCurrentProgress>(maxValue/3) &&(setCurrentProgress < (maxValue / 2))) {
                progressBar.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
            } else if (setCurrentProgress < (maxValue / 3)) {
                progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
            }

            image.setImageResource(budgetItem.getImage());
            title.setText(budgetItem.getProgressBarTitle());
            progressBar.setMax(maxValue);
            progressBar.setProgress(setCurrentProgress);
            totalAmountText.setText(String.valueOf(budgetItem.getMaxValue()));
            accountName.setText(String.valueOf(budgetItem.getAccountName()));
            amountprogress.setText(String.valueOf(budgetItem.getCurrentValue()));
        }
    }
}

