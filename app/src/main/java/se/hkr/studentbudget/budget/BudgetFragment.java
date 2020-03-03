package se.hkr.studentbudget.budget;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.hkr.studentbudget.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class BudgetFragment extends Fragment implements BudgetDialog.SaveInput {
    private String tag = "Info";
    private FloatingActionButton addBtn;
    private BudgetDialog dialog;
    private RecyclerView budgetRecycle;
    private ArrayList<BudgetItem> budgetItemArrayList;
    private BudgetAdapter budgetAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);
        Log.d(tag, "In the BudgetFragment event");
        // use view to get things from the window
        addBtn = view.findViewById(R.id.addBtn);

        budgetItemArrayList = new ArrayList<>();
        budgetAdapter = new BudgetAdapter(getContext(), budgetItemArrayList);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreateCategory();
            }
        });

        budgetRecycle = view.findViewById(R.id.budget_frame);
        budgetRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
        budgetRecycle.setAdapter(budgetAdapter);

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

    private void testingFlow(String value, String categoryTitle, String accountName) {
        //this stuff returns from dialog with info
        int maxAmount = Integer.parseInt(value);
        int currentAmount = Integer.parseInt(value);
        currentAmount = currentAmount - 10;
        Log.e(tag, String.valueOf(maxAmount));
        Log.e(tag, String.valueOf(currentAmount));
        budgetItemArrayList.add(new BudgetItem(currentAmount, maxAmount, R.drawable.ic_placeholder, categoryTitle, accountName));
        budgetAdapter.notifyDataSetChanged();
    }

}
