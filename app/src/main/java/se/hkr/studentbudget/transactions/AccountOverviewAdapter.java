package se.hkr.studentbudget.transactions;

import android.animation.ValueAnimator;
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
import se.hkr.studentbudget.AppMathCalc;
import se.hkr.studentbudget.R;
import se.hkr.studentbudget.account.Account;

public class AccountOverviewAdapter extends RecyclerView.Adapter<AccountOverviewAdapter.TransactionHolder> {

    private Context context;
    private ArrayList<Account> accountArrayList;

    public AccountOverviewAdapter(Context context, ArrayList<Account> accounts) {
        this.context = context;
        this.accountArrayList = accounts;
    }

    @NonNull
    @Override
    public TransactionHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_account, parent, false);
        return new TransactionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHolder holder, int position) {
        Account transactions = this.accountArrayList.get(position);
        holder.setDetails(transactions);
    }

    @Override
    public int getItemCount() {
        return accountArrayList.size();
    }

    class TransactionHolder extends RecyclerView.ViewHolder {

        private TextView txtAccount, txtAmount;


        TransactionHolder(View itemView) {
            super(itemView);

            txtAccount = itemView.findViewById(R.id.account_item_name);
            txtAmount = itemView.findViewById(R.id.account_item_amount);
        }

        void setDetails(Account details) {
            ValueAnimator animator = ValueAnimator.ofFloat(0, (float) details.getAccountValue()); //0 is min number, 600 is max number
            animator.setDuration(1000); //Duration is in milliseconds
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    float amount = (float) animation.getAnimatedValue();
                    txtAmount.setText(String.format("%s kr", String.format("%,.2f", amount)));
                }
            });
            animator.start();

            txtAccount.setText(details.getAccountName());
        }
    }


}