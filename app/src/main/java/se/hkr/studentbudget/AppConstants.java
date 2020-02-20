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
    public static CategoryRowAdapter expenseAdapter, accountAdapter, incomeAdapter;
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
        //Fills expense category
        fillExpenseCategorySpinner(context);
        fillIncomeCategorySpinner(context);
    }


    private static void initAccounts(Context context){
        DataBaseAccess dataBaseAcess = DataBaseAccess.getInstance(context);
        dataBaseAcess.openDatabase();
        accounts = dataBaseAcess.getAccount();
        dataBaseAcess.closeDatabe();
    }

    private static void fillIncomeCategorySpinner(Context context){
        ArrayList<CategoryRowItem> incomeList = new ArrayList<>();
        incomeList.add(new CategoryRowItem("CSN", R.drawable.ic_placeholder));
        incomeList.add(new CategoryRowItem("Salary", R.drawable.ic_placeholder));
        incomeList.add(new CategoryRowItem("Government help", R.drawable.ic_placeholder));
        incomeList.add(new CategoryRowItem("Other", R.drawable.ic_placeholder));
        incomeAdapter = new CategoryRowAdapter(context, incomeList);
    }

    private static void fillExpenseCategorySpinner(Context context){
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

    public static void fillAccountSpinner(Context context){
        ArrayList<CategoryRowItem> accountList = new ArrayList<>();
        if (accounts.size()>0) {
            for (int i = 0; i < accounts.size() ; i++) {
                accountList.add(new CategoryRowItem(accounts.get(i).getAccountName(),accounts.get(i).getImgIcon()));
            }
        }
        accountAdapter = new CategoryRowAdapter(context, accountList);
    }
}
