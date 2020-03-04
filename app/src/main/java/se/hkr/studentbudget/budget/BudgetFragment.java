package se.hkr.studentbudget.budget;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
    private Button whitdrawbtn;
    private int monster;
    private int currentAmount;

//TODO tabort?
    public interface OnNoteListener{
        void onNoteClick(int changeCurrentValue);
    }

    private OnNoteListener clickMe;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);
        Log.d(tag, "In the BudgetFragment event");
        // use view to get things from the window
        addBtn = view.findViewById(R.id.addBtn);
        whitdrawbtn = view.findViewById(R.id.testingWhitdraw);

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


        whitdrawbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                monster=10;
                currentAmount=currentAmount-monster;
                Log.i(tag,String.valueOf(currentAmount));
                //budgetAdapter.onNoteClick(currentAmount);
//                clickMe.onNoteClick(currentAmount);
                budgetAdapter.notifyDataSetChanged();
                //TODO fixa så jag kan ta bort pengar från proggebarre
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

    private void testingFlow(String value, String categoryTitle, String accountName) {
        //this stuff returns from dialog with info
        int maxAmount = Integer.parseInt(value);
        currentAmount = Integer.parseInt(value);
        Log.i(tag, String.valueOf(maxAmount));
        Log.i(tag, String.valueOf(currentAmount));
        budgetItemArrayList.add(new BudgetItem(currentAmount, maxAmount, R.drawable.ic_placeholder, categoryTitle, accountName));
        budgetAdapter.notifyDataSetChanged();
    }

    private void amountCalc(){

    }
//TODO tabort
//    @Override
//    public void onAttach(Context context) {
//        try {
//            clickMe = (BudgetFragment.OnNoteListener) getTargetFragment();
//        } catch (ClassCastException e) {
//            Log.e(tag, e.toString() + " in CreateAccountDialog");
//        }
//        super.onAttach(context);
//    }



}
