package se.hkr.studentbudget.account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import se.hkr.studentbudget.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AccountAdapter extends RecyclerView.Adapter<AccountItemCardHolder> {

    Context context;
    private ArrayList<Account> accounts; // this array lis create a which parameters defiune in our model class;
    private OnCardItemLongClickListener onCardItemLongClickListener;

    public interface OnCardItemLongClickListener {
        void onCardItemLongClick(int pos);
    }

    public void setOnCardItemLongClickListener(OnCardItemLongClickListener listener) {
        onCardItemLongClickListener = listener;
    }

    public AccountAdapter(Context context, ArrayList<Account> accountArrayList) {
        this.context = context;
        this.accounts = accountArrayList;
    }

    @NonNull
    @Override
    public AccountItemCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_card, null); // this line inflate the account_card

        return new AccountItemCardHolder(view, onCardItemLongClickListener); // this will return our view to holder class
    }

    @Override
    public void onBindViewHolder(@NonNull AccountItemCardHolder holder, int position) {

        holder.title.setText(accounts.get(position).getAccountName());
        holder.value.setText(String.format("%,.2f", accounts.get(position).getAccountValue()));
        holder.notes.setText(accounts.get(position).getAccountNote());
        holder.image.setImageResource(accounts.get(position).getImgIcon()); // used images resource. will use images
        //in resource folder which is drawable

    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }
}
