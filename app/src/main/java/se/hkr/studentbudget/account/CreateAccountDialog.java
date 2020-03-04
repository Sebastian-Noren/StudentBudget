package se.hkr.studentbudget.account;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

import androidx.appcompat.app.AppCompatDialogFragment;
import se.hkr.studentbudget.AppConstants;
import se.hkr.studentbudget.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CreateAccountDialog extends AppCompatDialogFragment {
    private String tag = "Info";
    private TextView inputAccountName, inputAccountNotes, inputAccountValue;

    public interface OnSelectedInput {
        void saveComplete(String input, double value, String notes);
    }

    private OnSelectedInput onSelectedInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_new_account_popup, container, false);
        Log.d(tag, "Create account dialog open");
        inputAccountName = view.findViewById(R.id.input_new_account);
        inputAccountValue = view.findViewById(R.id.input_inital_amount);
        inputAccountNotes = view.findViewById(R.id.input_account_notes);

        Button saveBtn = view.findViewById(R.id.account_saveBtn);
        Button cancelBtn = view.findViewById(R.id.account_cancelBtn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(tag, "Cancel clicked");
                if (!AppConstants.accountExist()) {
                    getDialog().dismiss();
                    Objects.requireNonNull(getActivity()).finish();
                    Log.i(tag, "Exit application");
                }
                AppConstants.hideSoftKeyboard(getActivity());
                getDialog().dismiss();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(tag, "Save clicked");
                double value;
                String str1 = inputAccountName.getText().toString().trim();
                String str2 = inputAccountValue.getText().toString().trim();
                String str3 = inputAccountNotes.getText().toString().trim();

                if (!str1.isEmpty()) {
                    if (!searchName(str1)) {
                        if (str2.isEmpty()) {
                            value = 0;
                        } else {
                            value = Double.parseDouble(str2);
                        }
                        getDialog().dismiss();
                        onSelectedInput.saveComplete(str1, value, str3);
                    } else {
                        AppConstants.toastMessage(getContext(), "Account already exist!");
                    }
                } else {
                    AppConstants.toastMessage(getContext(), "Must enter a Account name!");
                }
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
            onSelectedInput = (OnSelectedInput) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(tag, e.toString() + " in CreateAccountDialog");
        }
        super.onAttach(context);
    }

    private boolean searchName(String input) {
        for (int i = 0; i < AppConstants.accounts.size(); i++) {
            if (AppConstants.accounts.get(i).getAccountName().equals(input)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(tag, "In the onDestroyView() event");
        if (!AppConstants.accountExist()) Objects.requireNonNull(getActivity()).finish();
    }

}
