package se.hkr.studentbudget.budget;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.hkr.studentbudget.AppConstants;
import se.hkr.studentbudget.R;
import se.hkr.studentbudget.StaticStrings;
import se.hkr.studentbudget.database.DataBaseAccess;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class BudgetFragment extends Fragment implements BudgetDialog.SaveInput, DeleteBudgetProgressBar.OnDelectClick {
    private String tag = "Info";
    private FloatingActionButton addBtn;
    private BudgetDialog dialog;
    private RecyclerView budgetRecycle;
    private BudgetAdapter budgetAdapter;
    private int currentAmount;
    private DeleteBudgetProgressBar deleteDialog;
    int index;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);
        Log.d(tag, "In the BudgetFragment event");
        addBtn = view.findViewById(R.id.addBtn);
        budgetRecycle = view.findViewById(R.id.budget_frame);
        budgetAdapter = new BudgetAdapter(getContext(), AppConstants.budgetProgressBar);
        budgetAdapter.setOnCardItemLongClickListener(new BudgetAdapter.OnCardItemLongClickListener() {
            @Override
            public void onCardItemLongClick(int pos) {
                index = pos;
                openDeleteBudget(pos);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreateCategory();
            }
        });

        return view;
    }


    @Override
    public void save(String value, int image, String categoryTitle, String clickedAccountName) {
        testingFlow(value, image, categoryTitle, clickedAccountName);
    }

    private void openCreateCategory() {
        dialog = new BudgetDialog();
        dialog.setTargetFragment(this, 1);
        dialog.show(getFragmentManager(), "dialogBudget");
    }


    //skapar ny progressbar item
    private void testingFlow(String value, int image, String categoryTitle, String accountName) {
        int updateIndex = AppConstants.budgetProgressBar.size();
        //this stuff returns from dialog with info
        int maxAmount = Integer.parseInt(value);
        currentAmount = Integer.parseInt(value);
        Log.i(tag, String.valueOf(maxAmount));
        Log.i(tag, String.valueOf(currentAmount));
        BudgetItem budgetItem = new BudgetItem(maxAmount, image, categoryTitle, accountName);
        budgetItem.setCurrentValue(maxAmount - testDate(getContext(), categoryTitle));
        AppConstants.budgetProgressBar.add(budgetItem);
        insertToDatabase(maxAmount, image, categoryTitle, accountName);
        budgetAdapter.notifyItemInserted(updateIndex);
    }

    private void init() {
        AppConstants.initProgressBars(getContext());
    }

    private double testDate(Context context, String categoryTitle) {
        LocalDate startMonth = YearMonth.now().atDay(1);
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String strDateFrom = startMonth.format(dateFormatter);
        String strDateTo = today.format(dateFormatter);
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(context);
        dataBaseAccess.openDatabase();
        double value = Math.abs(dataBaseAccess.getTotalSumCategory(categoryTitle, StaticStrings.EXPENSE, strDateFrom, strDateTo));
        dataBaseAccess.closeDatabe();

        return value;
    }

    private void insertToDatabase(int maxAmount, int image, String categoryTitle, String accountName) {
        DataBaseAccess dataBaseAcess = DataBaseAccess.getInstance(getContext());
        dataBaseAcess.openDatabase();
        boolean insertData = dataBaseAcess.insertPorgressbarInDatabase(maxAmount, image, categoryTitle, accountName);
        if (insertData) {
            Log.i(tag, "ProgressBar saved to database!");
        } else {
            Log.e(tag, "Something Went Wrong!");
        }
        dataBaseAcess.closeDatabe();
    }
    private void openDeleteBudget(int pos) {
        String budget = AppConstants.budgetProgressBar.get(pos).getProgressBarTitle();
        deleteDialog = new DeleteBudgetProgressBar(budget);
        deleteDialog.setTargetFragment(this, 1);
        deleteDialog.show(getFragmentManager(), "deleteDialog");
    }

    private void deleteBudgetInDatabase(final String input) {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                DataBaseAccess dataBaseAcess = DataBaseAccess.getInstance(getContext());
                dataBaseAcess.openDatabase();
                boolean removeBudget = dataBaseAcess.deleteBudget(input);
                if (removeBudget) {
                    Log.i(tag, "Budget removed from database!");
                } else {
                    Log.e(tag, "Something Went Wrong!");
                }
                dataBaseAcess.closeDatabe();
            }
        });
        th.start();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(tag, "OverViewFragment: In the onAttach() event");
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(tag, "OverViewFragment: In the onResume() event");
        budgetRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
        budgetRecycle.setItemAnimator(new DefaultItemAnimator());
        budgetRecycle.setAdapter(budgetAdapter);
    }

    @Override
    public void deleteBudgetClick() {
        String budgetToDelete = AppConstants.budgetProgressBar.get(index).getProgressBarTitle();
        AppConstants.budgetProgressBar.remove(index);

        budgetAdapter.notifyItemRemoved(index);
        deleteBudgetInDatabase(budgetToDelete);
    }

}


