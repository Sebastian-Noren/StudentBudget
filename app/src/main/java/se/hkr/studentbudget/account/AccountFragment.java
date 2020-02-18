package se.hkr.studentbudget.account;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import se.hkr.studentbudget.AppConstants;
import se.hkr.studentbudget.R;
import se.hkr.studentbudget.database.DataBaseAccess;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class AccountFragment extends Fragment implements CreateAccountDialog.OnSelectedInput {

    private RecyclerView recyclerView;
    private AccountAdapter accountAdapter;
    private FloatingActionButton fabBtn;
    private CreateAccountDialog dialog;
    private TextView totalSaldo;
    private int accountSize;
    private String tag = "Info";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        Log.d(tag, "In the settingsFragment");
        recyclerView = view.findViewById(R.id.account_recycleview);
        fabBtn = view.findViewById(R.id.account_fab);
        totalSaldo = view.findViewById(R.id.account_saldo);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // will create recyclerview in linearlayoyt
        accountAdapter = new AccountAdapter(getContext(), AppConstants.accounts);
        recyclerView.setAdapter(accountAdapter);

        accountSize = AppConstants.accounts.size();

        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateAccountDialog();
            }
        });

        countAnimationSaldo();

        DataBaseAccess dataBaseAcess = DataBaseAccess.getInstance(getContext());
        dataBaseAcess.openDatabase();
        String n = dataBaseAcess.getItem();
        dataBaseAcess.closeDatabe();

        return view;
    }

    @Override
    public void saveComplete(String input, double value, String notes) {
        Log.i(tag, "Saves account");

        if (!dialog.isRemoving()) {
            addNewAccount(input, value, notes);
            countAnimationSaldo();
        }
    }

    private void countAnimationSaldo() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, (float) AppConstants.countSaldo()); //0 is min number, 600 is max number
        animator.setDuration(1000); //Duration is in milliseconds
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float amount = (float) animation.getAnimatedValue();
                totalSaldo.setText(String.format("Total: %s kr", String.format("%,.2f", amount)));
            }
        });
        animator.start();

    }

    private void addNewAccount(String input, double value, String notes) {
        accountSize = AppConstants.accounts.size();
        int insertIndex = (accountSize);
        int icon = R.drawable.ic_placeholder;
        Account m = new Account(input, value, notes, icon);
        AppConstants.accounts.add(accountSize, m);
        accountAdapter.notifyItemInserted(insertIndex);

        insertInDatabase(input,value,notes, icon);

    }

    private void openCreateAccountDialog() {
        dialog = new CreateAccountDialog();
        dialog.setTargetFragment(this, 1);
        dialog.show(getFragmentManager(), "dialog");
    }

    private void insertInDatabase(String input, double value, String notes, int icon){
        DataBaseAccess dataBaseAcess = DataBaseAccess.getInstance(getContext());
        dataBaseAcess.openDatabase();
        boolean insertData = dataBaseAcess.inserAccountInDatabase(input,value,notes,icon);
        if (insertData){
            AppConstants.toastMessage(getContext(),"Data Successfully Inserted!");
        }else {
            AppConstants.toastMessage(getContext(),"Something Went Wrong!");
        }
        dataBaseAcess.closeDatabe();
    }

}