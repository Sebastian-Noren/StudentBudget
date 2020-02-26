package se.hkr.studentbudget;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import se.hkr.studentbudget.account.Account;
import se.hkr.studentbudget.database.DataBaseAccess;
import se.hkr.studentbudget.transactions.AccountOverviewAdapter;
import se.hkr.studentbudget.transactions.CategoryRowAdapter;
import se.hkr.studentbudget.transactions.CategoryRowItem;
import se.hkr.studentbudget.transactions.TransactionAdapter;
import se.hkr.studentbudget.transactions.Transactions;

public class AppConstants {

    private static String tag = "Info";
    public static CategoryRowAdapter expenseAdapter, accountAdapter, incomeAdapter;
    public static TransactionAdapter transactionAdapter;
    public static AccountOverviewAdapter accountOverviewAdapter;
    public static ArrayList<Account> accounts;
    public static ArrayList<Transactions> transactions;

    //Initialize the whole foundation of the application.
    static void applicationInitialization(Context context) {
        Log.d(tag, "AppConstant.Class Initialization");
        initList(context);
    }

    public static void toastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private static void initList(Context context) {
        transactions = new ArrayList<>();
        initAccounts(context);
        initTransactions(context);
        //Fills expense and income category
        fillExpenseCategorySpinner(context);
        fillIncomeCategorySpinner(context);
    }

    private static void initAccounts(Context context){
        Log.i(tag, "init accounts");
        DataBaseAccess dataBaseAcess = DataBaseAccess.getInstance(context);
        dataBaseAcess.openDatabase();
        accounts = dataBaseAcess.getAccount();
        dataBaseAcess.closeDatabe();
        accountExist();
    }

    private static void fillIncomeCategorySpinner(Context context){
        Log.i(tag, "Init income category");
        ArrayList<CategoryRowItem> incomeList = new ArrayList<>();
        incomeList.add(new CategoryRowItem("CSN", R.drawable.ic_placeholder));
        incomeList.add(new CategoryRowItem("Salary", R.drawable.ic_placeholder));
        incomeList.add(new CategoryRowItem("Government help", R.drawable.ic_placeholder));
        incomeList.add(new CategoryRowItem("Other", R.drawable.ic_placeholder));
        incomeAdapter = new CategoryRowAdapter(context, incomeList);
    }

    private static void fillExpenseCategorySpinner(Context context){
        Log.i(tag, "Init expense category");
        ArrayList<CategoryRowItem> expenseList = new ArrayList<>();
        expenseList.add(new CategoryRowItem("Home", R.drawable.ic_placeholder));
        expenseList.add(new CategoryRowItem("Food/Drinks", R.drawable.ic_placeholder));
        expenseList.add(new CategoryRowItem("Alcohol/Tobacco", R.drawable.ic_placeholder));
        expenseList.add(new CategoryRowItem("Entertainment", R.drawable.ic_placeholder));
        expenseList.add(new CategoryRowItem("Transportation", R.drawable.ic_placeholder));
        expenseList.add(new CategoryRowItem("Shopping", R.drawable.ic_placeholder));
        expenseList.add(new CategoryRowItem("Health", R.drawable.ic_placeholder));
        expenseList.add(new CategoryRowItem("Travels", R.drawable.ic_placeholder));
        expenseList.add(new CategoryRowItem("Pets", R.drawable.ic_placeholder));
        expenseList.add(new CategoryRowItem("Other", R.drawable.ic_placeholder));
        expenseAdapter = new CategoryRowAdapter(context, expenseList);
    }

    private static void initTransactions(final Context context){
        Log.i(tag, "Init transactions");
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                DataBaseAccess dataBaseAcess = DataBaseAccess.getInstance(context);
                dataBaseAcess.openDatabase();
                transactions = dataBaseAcess.getAllTransactions();
                dataBaseAcess.closeDatabe();
                sortTransactions();

            }
        });
        th.start();
    }

    public static void fillAccountsOverview(Context context){
        accountOverviewAdapter = new AccountOverviewAdapter(context, accounts);
        accountOverviewAdapter.notifyDataSetChanged();
    }


    public static void fillTransactions(Context context){
        sortTransactions();
        transactionAdapter = new TransactionAdapter(context, transactions);
        transactionAdapter.notifyDataSetChanged();
        Log.i(tag,"TransactionAdapter complete!");

    }

    public static void fillAccountSpinner(Context context){
        ArrayList<CategoryRowItem> accountList = new ArrayList<>();
        if (accountExist()) {
            for (int i = 0; i < accounts.size() ; i++) {
                accountList.add(new CategoryRowItem(accounts.get(i).getAccountName(),accounts.get(i).getImgIcon()));
            }
        }
        accountAdapter = new CategoryRowAdapter(context, accountList);
    }

    private static void sortTransactions(){
        //Sort transaction with latest first
        Collections.sort(transactions, new Comparator<Transactions>() {
            @Override
            public int compare(Transactions o1, Transactions o2) {
                if (o1.getTransactionDate() == null || o2.getTransactionDate() == null) {
                    return 0;
                }
                return o2.getTransactionDate().compareTo(o1.getTransactionDate());
            }
        });
        Log.i(tag, "Sorting transactions done");
    }

    public static boolean accountExist(){
        return accounts.size() > 0;
    }
}
