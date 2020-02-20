package se.hkr.studentbudget;

import android.content.Context;
import android.util.Log;

import se.hkr.studentbudget.database.DataBaseAccess;

import static se.hkr.studentbudget.AppConstants.accounts;
import static se.hkr.studentbudget.AppConstants.transactions;


public class AppMathCalc {

    private static String tag = "Info";

    public void countTransactions() {
        double saldo = 0;
        for (int i = 0; i < transactions.size(); i++) {
            saldo = saldo + transactions.get(i).getValue();
        }
        Log.i(tag, String.valueOf(saldo));
    }

    //count total amount for all accounts.
    public double accountTotalSaldo() {
        double saldo = 0;
        for (int i = 0; i < accounts.size(); i++) {
            saldo = saldo + accounts.get(i).getAccountValue();
        }
        return saldo;
    }

    public void updateAccountAmount(int accountChoiceIndex, double amountValue, String clickedAccountName, Context context) {
        double newValue = accounts.get(accountChoiceIndex).getAccountValue() + amountValue;
        accounts.get(accountChoiceIndex).setAccountValue(newValue);
        updateValueInDatabase(context, clickedAccountName, newValue);
    }


    private void updateValueInDatabase(final Context context, final String clickedAccountName, final double amountValue) {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                DataBaseAccess dataBaseAcess = DataBaseAccess.getInstance(context);
                dataBaseAcess.openDatabase();
                boolean insertData = dataBaseAcess.updateAccountInDatabase(amountValue, clickedAccountName);
                if (insertData) {
                    Log.i(tag, "Account update saved in database completed!");
                } else {
                    Log.e(tag, "Something Went Wrong!");
                }
                dataBaseAcess.closeDatabe();
            }
        });
        th.start();
    }

}
