package HKR.studentbudget;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import HKR.studentbudget.transactions.CategoryRowAdapter;
import HKR.studentbudget.transactions.CategoryRowItem;

public class AppConstants {

    private static String tag = "Info";
    private static ArrayList<CategoryRowItem> spendingList;
    private static ArrayList<CategoryRowItem> accountList;
    public static CategoryRowAdapter expenseAdapter, accountAdapter, incomeAdapter;

    static void applicationInitialization(Context context){
        Log.d(tag, "AppConstant.Class Initialization");
        initList(context);

    }



    private static void initList(Context context) {
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
        accountList.add(new CategoryRowItem("Wallet",R.drawable.ic_placeholder));

        expenseAdapter = new CategoryRowAdapter(context, spendingList);
        accountAdapter = new CategoryRowAdapter(context,accountList);


    }
}
