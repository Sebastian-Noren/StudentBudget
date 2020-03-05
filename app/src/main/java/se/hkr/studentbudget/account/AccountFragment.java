package se.hkr.studentbudget.account;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import se.hkr.studentbudget.AppConstants;
import se.hkr.studentbudget.AppMathCalc;
import se.hkr.studentbudget.R;
import se.hkr.studentbudget.database.DataBaseAccess;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class AccountFragment extends Fragment implements CreateAccountDialog.OnSelectedInput,
        DeleteAccountDialog.OnDelectClick {

    private RecyclerView recyclerView;
    private AccountAdapter accountAdapter;
    private FloatingActionButton fabBtn;
    private CreateAccountDialog dialog;
    private DeleteAccountDialog deleteDialog;
    private TextView totalSaldo;
    private int accountSize, accountRemoveIndex;
    private String tag = "Info";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        Log.d(tag, "In the AccountFragment");
        recyclerView = view.findViewById(R.id.account_recycleview);
        fabBtn = view.findViewById(R.id.account_fab);
        totalSaldo = view.findViewById(R.id.account_saldo);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // will create recyclerview in linearlayoyt
        accountAdapter = new AccountAdapter(getContext(), AppConstants.accounts);
        recyclerView.setAdapter(accountAdapter);

        accountSize = AppConstants.accounts.size();

        accountAdapter.setOnCardItemLongClickListener(new AccountAdapter.OnCardItemLongClickListener() {
            @Override
            public void onCardItemLongClick(int pos) {
                accountRemoveIndex = pos;
                openDeleteAccount(pos);
            }
        });

        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateAccountDialog();
            }
        });

        countAnimationSaldo();

        if (!AppConstants.accountExist()) {
        openCreateAccountDialog();
        }


        return view;
    }

    @Override
    public void deleteAccountClick() {
        removeAccount();
        if (!AppConstants.accountExist()) {
            openCreateAccountDialog();
        }
    }

    @Override
    public void saveComplete(String input, double value, String notes) {
        Log.i(tag, "Saves account");
        if (!dialog.isRemoving()) {
            addNewAccount(input, value, notes);
            countAnimationSaldo();
        }
    }

    private void removeAccount() {
        String account = AppConstants.accounts.get(accountRemoveIndex).getAccountName();
        //removeTransactionWithAccount(account);
        AppConstants.accounts.remove(accountRemoveIndex);
        accountAdapter.notifyItemRemoved(accountRemoveIndex);
        countAnimationSaldo();
        deleteAccountInDatabase(account);
    }

    private void countAnimationSaldo() {
        AppMathCalc calc = new AppMathCalc();
        ValueAnimator animator = ValueAnimator.ofFloat(0, (float) calc.accountTotalSaldo()); //0 is min number, 600 is max number
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
        int icon = R.drawable.icons_wallet;
        Account m = new Account(input, value, notes, icon);
        AppConstants.accounts.add(m);
        accountAdapter.notifyItemInserted(insertIndex);
        insertInDatabase(input, value, notes, icon);
    }

    private void openDeleteAccount(int pos) {
        String account = AppConstants.accounts.get(pos).getAccountName();
        deleteDialog = new DeleteAccountDialog(account);
        deleteDialog.setTargetFragment(this, 1);
        deleteDialog.show(getFragmentManager(), "deleteDialog");
    }

    private void openCreateAccountDialog() {
        dialog = new CreateAccountDialog();
        dialog.getDialog();
        dialog.setTargetFragment(this, 2);
        dialog.show(getFragmentManager(), "dialog");
    }

    private void deleteAccountInDatabase(final String input) {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                DataBaseAccess dataBaseAcess = DataBaseAccess.getInstance(getContext());
                dataBaseAcess.openDatabase();
                boolean removeAccount = dataBaseAcess.deleteAccount(input);
                if (removeAccount) {
                    Log.i(tag, "Account removed from database!");
                } else {
                    Log.e(tag, "Something Went Wrong!");
                }
                boolean removeTransactions = dataBaseAcess.deleteAllTransactionsFromAccount(input);
                if (removeTransactions) {
                    Log.i(tag, "Transactions removed from database Complete!");
                } else {
                    Log.e(tag, "Something Went Wrong!");
                }
                dataBaseAcess.closeDatabe();
            }
        });
        th.start();
    }



    private void insertInDatabase(final String input, final double value, final String notes, final int icon) {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                DataBaseAccess dataBaseAcess = DataBaseAccess.getInstance(getContext());
                dataBaseAcess.openDatabase();
                boolean insertData = dataBaseAcess.insertAccountInDatabase(input, value, notes, icon);
                if (insertData) {
                    Log.i(tag, "Account saved to database!");
                } else {
                    Log.e(tag, "Something Went Wrong!");
                }
                dataBaseAcess.closeDatabe();
            }
        });
        th.start();
    }
}