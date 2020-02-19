package se.hkr.studentbudget;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import se.hkr.studentbudget.account.Account;
import se.hkr.studentbudget.database.DataBaseAccess;
import se.hkr.studentbudget.transactions.CategoryRowAdapter;
import se.hkr.studentbudget.transactions.CategoryRowItem;

public class AppConstants {

    private static String tag = "Info";
    private static ArrayList<CategoryRowItem> spendingList;
    private static ArrayList<CategoryRowItem> accountList;
    public static CategoryRowAdapter expenseAdapter, accountAdapter, incomeAdapter;
    public static ArrayList<Account> accounts;

    //Initialize the whole foundation of the application.
    static void applicationInitialization(Context context) {
        Log.d(tag, "AppConstant.Class Initialization");
        initList(context);

    }

    public static void toastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private static void initList(Context context) {

        DataBaseAccess dataBaseAcess = DataBaseAccess.getInstance(context);
        dataBaseAcess.openDatabase();
        accounts = dataBaseAcess.getAccount();
        dataBaseAcess.closeDatabe();

        //Fills expense category
        spendingList = new ArrayList<>();
        spendingList.add(new CategoryRowItem("Home", R.drawable.ic_placeholder));
        spendingList.add(new CategoryRowItem("Food/Drinks", R.drawable.ic_placeholder));
        spendingList.add(new CategoryRowItem("Entertainment", R.drawable.ic_placeholder));
        spendingList.add(new CategoryRowItem("Transportation", R.drawable.ic_placeholder));
        spendingList.add(new CategoryRowItem("Shopping", R.drawable.ic_placeholder));
        spendingList.add(new CategoryRowItem("Health", R.drawable.ic_placeholder));
        spendingList.add(new CategoryRowItem("Travels", R.drawable.ic_placeholder));
        spendingList.add(new CategoryRowItem("Pets", R.drawable.ic_placeholder));
        spendingList.add(new CategoryRowItem("Other", R.drawable.ic_placeholder));

        accountList = new ArrayList<>();
        //TODO make this work without an account.
        //accountList.add(new CategoryRowItem(accounts.get(0).getAccountName(),accounts.get(0).getImgIcon()));
        accountList.add(new CategoryRowItem("dummy",R.drawable.ic_placeholder));

        expenseAdapter = new CategoryRowAdapter(context, spendingList);
        accountAdapter = new CategoryRowAdapter(context, accountList);
    }

    //count total amount for all accounts.
    public static double accountTotalSaldo() {
        double saldo = 0;
        for (int i = 0; i < accounts.size(); i++) {
            saldo = saldo + accounts.get(i).getAccountValue();
        }
        return saldo;
    }
}
