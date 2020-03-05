package se.hkr.studentbudget.budget;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import se.hkr.studentbudget.AppConstants;

import se.hkr.studentbudget.R;
import se.hkr.studentbudget.transactions.CategoryRowItem;


public class BudgetDialog extends DialogFragment {
    private String tag = "Info";
    private TextView inputAmountText;
    private Spinner spinnerBudget;
    private Spinner spinnerAccountSelect;
    private Button saveBtnB;
    private String categoryTitle;
    private String clickedAccountName;
    private int image;


    // the one method to rule them all
    public interface SaveInput {
        void save(String value, int image, String categoryTitle, String clickedAccountName);
    }

    private SaveInput saveInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget_popup, container, false);
        Log.d(tag, "In the BudgetDialog event");
        inputAmountText = view.findViewById(R.id.amount);
        saveBtnB = view.findViewById(R.id.savebtnB);
        spinnerBudget = view.findViewById(R.id.spinnerBudget);
        spinnerBudget.setAdapter(AppConstants.expenseAdapter);

        AppConstants.fillAccountSpinner(getContext());
        spinnerAccountSelect = view.findViewById(R.id.spinnerAccountSelectBudget);
        spinnerAccountSelect.setAdapter(AppConstants.accountAdapter);

        spinnerBudget.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CategoryRowItem clickedItem = (CategoryRowItem) adapterView.getItemAtPosition(i);
                categoryTitle = clickedItem.getmCategoryName();
                image = clickedItem.getmCategoryIcon();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerAccountSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CategoryRowItem clickedItem = (CategoryRowItem) adapterView.getItemAtPosition(i);
                clickedAccountName = clickedItem.getmCategoryName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        saveBtnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //This is the total amount money
                String inputAmount = inputAmountText.getText().toString().trim();
                if(!checkExistingProgressBar(categoryTitle)){
                    if (!inputAmount.isEmpty()){
                        saveInput.save(inputAmount, image, categoryTitle, clickedAccountName);
                        getDialog().dismiss();
                    }else{
                        AppConstants.toastMessage(getContext(), "Enter amount!");
                    }
                }else{
                    AppConstants.toastMessage(getContext(), "budget already exists!");
                }

                AppConstants.hideSoftKeyboard(getActivity());
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        try {
            saveInput = (BudgetDialog.SaveInput) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(tag, e.toString() + " in CreateAccountDialog");
        }
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.colorPickerStyle);
        // this setStyle is VERY important.
        // STYLE_NO_FRAME means that I will provide my own layout and style for the whole dialog
        // so for example the size of the default dialog will not get in my way
        // the style extends the default one. see bellow.
    }

    private boolean checkExistingProgressBar(String input){
        for (int i = 0; i < AppConstants.budgetProgressBar.size(); i++) {
            if (AppConstants.budgetProgressBar.get(i).getProgressBarTitle().equals(input)){
                return true;
            }
        }
        return false;
    }

}
