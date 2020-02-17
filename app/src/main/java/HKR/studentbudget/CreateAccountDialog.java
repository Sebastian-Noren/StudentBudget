package HKR.studentbudget;


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
import androidx.fragment.app.DialogFragment;


public class CreateAccountDialog extends DialogFragment {
    private String tag = "Info";
    private TextView inputAccountName, inputAccountNotes, inputAccountValue;

    public interface OnSelectedInput {
        void inputString(String input, double value, String notes);
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


                 if (str2.isEmpty()) {
                     value = 0;
                 }else {
                     value = Double.parseDouble(str2);
                 }
                onSelectedInput.inputString(str1, value, str3);
                getDialog().dismiss();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        try {
            onSelectedInput = (OnSelectedInput) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(tag,e.toString() + " in CreateAccountDialog");
        }
        super.onAttach(context);
    }
}
