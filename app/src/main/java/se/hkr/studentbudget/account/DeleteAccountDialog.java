package se.hkr.studentbudget.account;


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
import se.hkr.studentbudget.R;


public class DeleteAccountDialog extends DialogFragment {
    private String tag = "Info";
    private TextView headline;

    private String accountToDelete;

    public DeleteAccountDialog(String accountToDelete) {
        this.accountToDelete = accountToDelete;
    }

    public interface OnDelectClick {
        void deleteAccountClick();
    }

    private OnDelectClick onDelectClick;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delete_account_popup, container, false);
        Log.d(tag, "DeleteAccount dialog open");

        headline = view.findViewById(R.id.deleteAccountHeadline);
        headline.setText(String.format("Delete '%s'", accountToDelete));

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

                Log.i(tag, "Accept clicked");
                onDelectClick.deleteAccountClick();
                getDialog().dismiss();

            }
        });

        return view;
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
