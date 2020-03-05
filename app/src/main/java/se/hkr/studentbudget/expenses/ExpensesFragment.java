package se.hkr.studentbudget.expenses;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import se.hkr.studentbudget.AppConstants;
import se.hkr.studentbudget.R;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import se.hkr.studentbudget.database.DataBaseAccess;
import se.hkr.studentbudget.transactions.TransactionAdapter;
import se.hkr.studentbudget.transactions.Transactions;

public class ExpensesFragment extends Fragment {
    private String tag = "Info";
    private RecyclerView recyclerView;
    private TransactionGroupAdapter transactionGroupAdapter;
    private Handler handler;
    private TextView yearText;
    private String strCurrentYear;
    private LocalDate localyear;
    private DateTimeFormatter dateFormatter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);
        Log.d(tag, "In the ExpensesFragment");

        handler = new Handler();
        recyclerView = view.findViewById(R.id.transaction_recycler);
        yearText = view.findViewById(R.id.year_text);
        ImageButton leftBtn = view.findViewById(R.id.imageButton_left);
        ImageButton rightBtn = view.findViewById(R.id.imageButton_right);

        dateFormatter = DateTimeFormatter.ofPattern("yyyy", Locale.US);
        localyear = LocalDate.now();
        strCurrentYear = localyear.format(dateFormatter);
        yearText.setText(strCurrentYear);

        leftBtn.setOnClickListener(v -> {
            localyear = localyear.minusYears(1);
            strCurrentYear = localyear.format(dateFormatter);
            yearText.setText(strCurrentYear);
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localyear = localyear.plusYears(1);
                strCurrentYear = localyear.format(dateFormatter);
                yearText.setText(strCurrentYear);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        fillTransactionsThread(recyclerView);
        return view;
    }

    //TODO alot of shit here
    private ArrayList<TransactionGroup> buildItemList() {
        ArrayList<TransactionGroup> transactionGroups = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.US);
        DateTimeFormatter dateFormatterMonths = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        int startMonth = localDate.getMonthValue();
        YearMonth month;
        LocalDate start, end;
        for (int i = startMonth; i > 0; i--) {
            month = YearMonth.from(localDate);
            start = month.atDay(1);
            end = month.atEndOfMonth();
            TransactionGroup transactionGroup = new TransactionGroup(localDate.format(dateFormatter),
                    monthValue(getContext(),start.format(dateFormatterMonths),end.format(dateFormatterMonths)),
                    fillTransactions(getContext(),start.format(dateFormatterMonths),end.format(dateFormatterMonths)));
            transactionGroups.add(transactionGroup);
            localDate = localDate.minusMonths(1);
        }
        return transactionGroups;
    }

    private double monthValue(Context context, String from, String to){
        double value = 0;
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(context);
        dataBaseAccess.openDatabase();
        value = dataBaseAccess.getTotalSumTransactionMonth(from, to);
        dataBaseAccess.closeDatabe();
        return value;
    }

    private ArrayList<Transactions> fillTransactions(Context context, String from, String to) {
        ArrayList<Transactions> transactions;
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(context);
        dataBaseAccess.openDatabase();
        transactions = dataBaseAccess.getAllTransactionsBetweenDates(from, to);
        dataBaseAccess.closeDatabe();
        sortTransactions(transactions);
        return transactions;
    }

    private void sortTransactions(ArrayList<Transactions> listToSort) {
        //Sort transaction with latest first
        Collections.sort(listToSort, new Comparator<Transactions>() {
            @Override
            public int compare(Transactions o1, Transactions o2) {
                if (o1.getTransactionDate() == null || o2.getTransactionDate() == null) {
                    return 0;
                }
                return o2.getTransactionDate().compareTo(o1.getTransactionDate());
            }
        });
        Log.i(tag, "Sorting transactions complete!");
    }

    //TODO add headers each months
    private void fillTransactionsThread(final RecyclerView recyclerView) {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                transactionGroupAdapter = new TransactionGroupAdapter(getContext(), buildItemList());

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