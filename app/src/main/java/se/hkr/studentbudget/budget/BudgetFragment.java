package se.hkr.studentbudget.budget;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import se.hkr.studentbudget.AppConstants;
import se.hkr.studentbudget.R;
import se.hkr.studentbudget.StaticStrings;
import se.hkr.studentbudget.database.DataBaseAccess;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BudgetFragment extends Fragment implements BudgetDialog.SaveInput {
    private String tag = "Info";
    private FloatingActionButton addBtn;
    private BudgetDialog dialog;
    private RecyclerView budgetRecycle;
    //DENNA ARRYN ANVÄNDER VI INTE
    //private ArrayList<BudgetItem> budgetItemArrayList;
    private BudgetAdapter budgetAdapter;
    private Button whitdrawbtn;
    private int monster;
    private int currentAmount;
    private int size;

    //TODO 1. currentValue - läsa in från databasen
    //TODO 2. koppla rätt account till rätt accountProgressBar
    //TODO 3. Progressbarsen ska resetas efter varje månad - kolla på dates och någon slags reset.
    //TODO 4. ska inte kunna gå under 0 för progressbar
    //TODO 5. fixa savebtn - ha något slags "safenet"
    //TODO 6. fixa så man kan bara lägga till en progressbar för en kategori
    //TODO 7. fixa så att när man skapar en progressbar man hämtar hem rätt data från databasen


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);
        Log.d(tag, "In the BudgetFragment event");
        addBtn = view.findViewById(R.id.addBtn);
        whitdrawbtn = view.findViewById(R.id.testingWhitdraw);

        init();
        budgetAdapter = new BudgetAdapter(getContext(), AppConstants.budgetProgressBar);
        budgetRecycle = view.findViewById(R.id.budget_frame);
        budgetRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
        budgetRecycle.setAdapter(budgetAdapter);
        size = AppConstants.budgetProgressBar.size();


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreateCategory();
            }
        });
        //TODO fixa så jag kan ta bort pengar från proggebarre

        //TODO detta tar minskar progressbar men ska inte användas.
        whitdrawbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monster = 300;
                currentAmount = currentAmount - monster;
                AppConstants.toastMessage(getContext(), String.valueOf(currentAmount));

                int updateIndex = 0;
                AppConstants.budgetProgressBar.set(updateIndex, new BudgetItem(currentAmount, 500, R.drawable.ic_placeholder, "bajs", "skolbajs"));
                budgetAdapter.notifyItemChanged(updateIndex);
            }
        });


        return view;
    }


    @Override
    public void save(String value, String categoryTitle, String clickedAccountName) {
        testingFlow(value, categoryTitle, clickedAccountName);
    }

    private void openCreateCategory() {
        dialog = new BudgetDialog();
        dialog.setTargetFragment(this, 1);
        dialog.show(getFragmentManager(), "dialogBudget");
    }


    //skapar ny item
    private void testingFlow(String value, String categoryTitle, String accountName) {
        int updateIndex = AppConstants.budgetProgressBar.size();
        //this stuff returns from dialog with info
        int maxAmount = Integer.parseInt(value);
        currentAmount = Integer.parseInt(value);
        Log.i(tag, String.valueOf(maxAmount));
        Log.i(tag, String.valueOf(currentAmount));
        AppConstants.budgetProgressBar.add(new BudgetItem(maxAmount-testDate(), maxAmount, R.drawable.ic_placeholder, categoryTitle, accountName));
        budgetAdapter.notifyItemInserted(updateIndex);


    }

    private void init() {
        AppConstants.budgetProgressbarFiller(getContext());
        //int siezeTest = AppConstants.budgetProgressBar.size();
       // budgetAdapter.notifyItemInserted(0);
        budgetAdapter.notifyDataSetChanged();
       // testDate();
    }

    private double testDate() {
        LocalDate startMonth = YearMonth.now().atDay(1);
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String strDateFrom = startMonth.format(dateFormatter);
        String strDateTo = today.format(dateFormatter);
        DataBaseAccess dataBaseAccess = DataBaseAccess.getInstance(getContext());
        dataBaseAccess.openDatabase();
        double value = Math.abs(dataBaseAccess.getTotalSumCategory("Food/Drinks", StaticStrings.EXPENSE, strDateFrom, strDateTo));


        dataBaseAccess.closeDatabe();
        AppConstants.toastMessage(getContext(), String.valueOf(value));
        return value;
    }


}
