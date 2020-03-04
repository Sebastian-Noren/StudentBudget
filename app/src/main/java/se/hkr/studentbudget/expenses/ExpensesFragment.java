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
    private TransactionAdapter transactionAdapter;
    private Handler handler;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);
        Log.d(tag, "In the ExpensesFragment");

        handler = new Handler();
        recyclerView = view.findViewById(R.id.transaction_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TransactionGroupAdapter transactionGroupAdapter = new TransactionGroupAdapter(getContext(),buildItemList());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(transactionGroupAdapter);


      //  fillTransactionsThread(recyclerView);
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

    /*
    //TODO add headers each months
    private void fillTransactionsThread(final RecyclerView recyclerView) {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                AppConstants.fillTransactions(getContext());
                transactionAdapter = new EXpensesMultiViewAdapter(getContext(),);
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

     */
}