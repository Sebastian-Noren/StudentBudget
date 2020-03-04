package se.hkr.studentbudget.expenses;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import se.hkr.studentbudget.AppConstants;
import se.hkr.studentbudget.R;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import se.hkr.studentbudget.transactions.TransactionAdapter;

public class ExpensesFragment extends Fragment {
    private String tag = "Info";
    private RecyclerView recyclerView;
    private TransactionAdapter transactionAdapter;
    private Handler handler;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);
        Log.d(tag, "In the ExpensesFragment");

        handler = new Handler();
        recyclerView = view.findViewById(R.id.transaction_recycler);
        //  AppConstants.fillTransactions(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(null);
        fillTransactionsThread(recyclerView);
        return view;
    }

    //TODO add headers each months
    private void fillTransactionsThread(final RecyclerView recyclerView) {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                AppConstants.fillTransactions(getContext());
                transactionAdapter = new TransactionAdapter(getContext(), AppConstants.transactions);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("Info", "print transactions inside filltransaction");
                        recyclerView.setAdapter(transactionAdapter);
                        transactionAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        th.start();
    }
}