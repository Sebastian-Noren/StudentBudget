package se.hkr.studentbudget.budget;

import android.content.Context;
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

    //constructor
    public BudgetAdapter(Context context, ArrayList<BudgetItem> budgetItems) {
        this.context = context;
        this.budgetItems = budgetItems;
    }

    @NonNull
    @Override
    public BudgetHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.progessbar_budget, parent, false);
        return new BudgetHolder(view);
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
    //TODO tabort implements
    class BudgetHolder extends RecyclerView.ViewHolder {
        TextView title, amountprogress, totalAmountText, accountName;
        ImageView image;
        ProgressBar progressBar;

        BudgetHolder(View itemView) {

            super(itemView);
            this.image = itemView.findViewById(R.id.progressbar_image);
            this.title = itemView.findViewById(R.id.name_of_progressbar);
            this.amountprogress = itemView.findViewById(R.id.amount_left);
            this.progressBar = itemView.findViewById(R.id.progressbar);
            this.totalAmountText = itemView.findViewById(R.id.amount_leftTotal);
            this.accountName = itemView.findViewById(R.id.accountName);

        }

        public void setDetails(BudgetItem budgetItem) {
            title.setText(budgetItem.getProgressBarTitle());
            progressBar.setMax((int) budgetItem.getMaxValue());
            progressBar.setProgress((int) budgetItem.getCurrentValue());
            totalAmountText.setText(String.valueOf(budgetItem.getMaxValue()));
            accountName.setText(String.valueOf(budgetItem.getAccountName()));
            amountprogress.setText(String.valueOf(budgetItem.getCurrentValue()));
        }
    }


}

