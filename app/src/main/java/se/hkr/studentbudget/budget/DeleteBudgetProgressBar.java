package se.hkr.studentbudget.budget;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import se.hkr.studentbudget.AppConstants;
import se.hkr.studentbudget.R;


public class DeleteBudgetProgressBar extends AppCompatDialogFragment {
    private String tag = "Info";
    private TextView headline;

    private String budgetToDelete;

    public DeleteBudgetProgressBar(String budgetToDelete) {
        this.budgetToDelete = budgetToDelete;
    }

    public interface OnDelectClick {
        void deleteBudgetClick();
    }

    private OnDelectClick onDelectClick;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delete_budget_popup, container, false);

        Log.d(tag, "DeleteAccount dialog open");

        headline = view.findViewById(R.id.deleteAccountHeadline);
        headline.setText(String.format("Delete '%s'", budgetToDelete));

        Button saveBtn = view.findViewById(R.id.account_saveBtn);
        Button cancelBtn = view.findViewById(R.id.account_cancelBtn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(tag, "Cancel clicked");
                getDialog().dismiss();
                AppConstants.hideSoftKeyboard(getActivity());
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(tag, "Accept clicked");
                onDelectClick.deleteBudgetClick();
                getDialog().dismiss();
                AppConstants.hideSoftKeyboard(getActivity());

            }
        });

        return view;
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

    @Override
    public void onAttach(Context context) {
        try {
            onDelectClick = (OnDelectClick) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(tag, e.toString() + " in CreateAccountDialog");
        }
        super.onAttach(context);
    }
}
