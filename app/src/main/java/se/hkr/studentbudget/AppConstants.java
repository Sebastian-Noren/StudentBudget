package se.hkr.studentbudget;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import se.hkr.studentbudget.account.Account;
import se.hkr.studentbudget.database.DataBaseAccess;
import se.hkr.studentbudget.transactions.CategoryRowAdapter;
import se.hkr.studentbudget.transactions.CategoryRowItem;
import se.hkr.studentbudget.transactions.Transactions;

public class AppConstants {

    private static String tag = "Info";
    public static CategoryRowAdapter expenseAdapter, accountAdapter, incomeAdapter;
    public static ArrayList<Account> accounts;
    public static ArrayList<Transactions> transactions;
    public static ArrayList<Transactions> currentMonthTransaction;
    public static ArrayList<CategoryRowItem> expenseList;
    public static ArrayList<CategoryRowItem> incomeList;

    //Initialize the whole foundation of the application.
    static void applicationInitialization(Context context) {
        Log.i(tag, "AppConstant.Class Initialization");
        initList(context);
    }

    private static void initList(Context context) {
        transactions = new ArrayList<>();
        currentMonthTransaction = new ArrayList<>();
        initAccounts(context);
        //Fills expense and income category
        fillExpenseCategorySpinner(context);
        fillIncomeCategorySpinner(context);
        Log.i(tag, "AppConstant.Class Initialization Complete!");
    }

    private static void initAccounts(Context context) {
        Log.i(tag, "Init accounts!");
        DataBaseAccess dataBaseAcess = DataBaseAccess.getInstance(context);
        dataBaseAcess.openDatabase();
        accounts = dataBaseAcess.getAccount();
        dataBaseAcess.closeDatabe();
        accountExist();
        Log.i(tag, "Init accounts complete!");
    }

    private static void fillIncomeCategorySpinner(Context context) {
        Log.i(tag, "Init income category");
        incomeList = new ArrayList<>();
        incomeList.add(new CategoryRowItem(StaticStrings.CSN, R.drawable.icons_csn));
        incomeList.add(new CategoryRowItem(StaticStrings.SALARY, R.drawable.icons_salary));
        incomeList.add(new CategoryRowItem(StaticStrings.GOVERMENT_HELP, R.drawable.icons_gov));
        incomeList.add(new CategoryRowItem(StaticStrings.INCOME_OTHER, R.drawable.icons_other_income));
        incomeAdapter = new CategoryRowAdapter(context, incomeList);
        Log.i(tag, "Init income category Complete!");
    }

    private static void fillExpenseCategorySpinner(Context context) {
        Log.i(tag, "Init expense category");
        expenseList = new ArrayList<>();
        expenseList.add(new CategoryRowItem(StaticStrings.HOME, R.drawable.icons_home));
        expenseList.add(new CategoryRowItem(StaticStrings.FOOD, R.drawable.icons_food));
        expenseList.add(new CategoryRowItem(StaticStrings.ALCOHOL, R.drawable.icons_beer));
        expenseList.add(new CategoryRowItem(StaticStrings.ENTERTAINMENT, R.drawable.icons_entertainment));
        expenseList.add(new CategoryRowItem(StaticStrings.TRANSPORTATION, R.drawable.icons_transport));
        expenseList.add(new CategoryRowItem(StaticStrings.SHOPPING, R.drawable.icons_shopping));
        expenseList.add(new CategoryRowItem(StaticStrings.HEALTH, R.drawable.icons_health));
        expenseList.add(new CategoryRowItem(StaticStrings.TRAVELS, R.drawable.icons_travel));
        expenseList.add(new CategoryRowItem(StaticStrings.PETS, R.drawable.icons_pets));
        expenseList.add(new CategoryRowItem(StaticStrings.EXPENSE_OTHER, R.drawable.icons_other_expense));
        expenseAdapter = new CategoryRowAdapter(context, expenseList);
        Log.i(tag, "Init expense category Complete!");
    }

    public static void initCurrentMonthsTransactions(final Context context){
        Log.i(tag, "READ: current months transactions.");
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                DataBaseAccess dataBaseAcess = DataBaseAccess.getInstance(context);
                dataBaseAcess.openDatabase();
                currentMonthTransaction = dataBaseAcess.getAllTransactions();
                dataBaseAcess.closeDatabe();
                sortTransactions(currentMonthTransaction);
                Log.i(tag, "READ: current months transactions Complete!");
            }
        });
        th.start();
    }

    public static void fillTransactions(Context context) {
        DataBaseAccess dataBaseAcess = DataBaseAccess.getInstance(context);
        dataBaseAcess.openDatabase();
        transactions = dataBaseAcess.getAllTransactions();
        dataBaseAcess.closeDatabe();
        sortTransactions(transactions);

    }

    public static void fillAccountSpinner(Context context) {
        ArrayList<CategoryRowItem> accountList = new ArrayList<>();
        if (accountExist()) {
            for (int i = 0; i < accounts.size(); i++) {
                accountList.add(new CategoryRowItem(accounts.get(i).getAccountName(), accounts.get(i).getImgIcon()));
            }
        }
        accountAdapter = new CategoryRowAdapter(context, accountList);
    }

    private static void sortTransactions(ArrayList<Transactions> listToSort) {
        //Sort transaction with latest first
        Collections.sort(listToSort, new Comparator<Transactions>() {
            @Override
            public int compare(Transactions o1, Transactions o2) {
                if (o1.getTransactionDate() == null || o2.getTransactionDate() == null) {
                    return 0;
                }
                return o2.getTransactionDate().compareTo(o1.getTransactionDate());
            }
        });
        Log.i(tag, "Sorting transactions complete!");
    }

    public static void toastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (activity.getCurrentFocus() != null && inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            Log.e(tag, e.getMessage());
        }
    }

    public static boolean accountExist() {
        return accounts.size() > 0;
    }
}
