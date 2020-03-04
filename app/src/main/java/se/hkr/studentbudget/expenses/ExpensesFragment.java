package se.hkr.studentbudget.expenses;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
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
    private TransactionGroupAdapter transactionGroupAdapter;
    private Handler handler;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);
        Log.d(tag, "In the ExpensesFragment");

        handler = new Handler();
        recyclerView = view.findViewById(R.id.transaction_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        fillTransactionsThread(recyclerView);
        return view;
    }

    //TODO alot of shit here
    private ArrayList<TransactionGroup> buildItemList() {
        ArrayList<TransactionGroup> transactionGroups = new ArrayList<>();
        for (int i=0; i<12; i++) {
            TransactionGroup transactionGroup = new TransactionGroup("Month "+i, 25, AppConstants.currentMonthTransaction);
            transactionGroups.add(transactionGroup);
        }
        return transactionGroups;
    }

    private List<SubItem> buildSubItemList() {
        List<SubItem> subItemList = new ArrayList<>();
        for (int i=0; i<3; i++) {
            SubItem subItem = new SubItem("Sub Item "+i, "Description "+i);
            subItemList.add(subItem);
        }
        return subItemList;
    }

    //TODO add headers each months
    private void fillTransactionsThread(final RecyclerView recyclerView) {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                transactionGroupAdapter = new TransactionGroupAdapter(getContext(),buildItemList());

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(transactionGroupAdapter);
                        transactionGroupAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        th.start();
    }


}