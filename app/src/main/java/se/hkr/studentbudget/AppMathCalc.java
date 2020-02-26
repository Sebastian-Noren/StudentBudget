package se.hkr.studentbudget;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import se.hkr.studentbudget.database.DataBaseAccess;

import static se.hkr.studentbudget.AppConstants.accounts;
import static se.hkr.studentbudget.AppConstants.transactions;


public class AppMathCalc {

    private static String tag = "Info";

    public double countTransactionsTotal() {
        double saldo = 0;
        for (int i = 0; i < transactions.size(); i++) {
            saldo = saldo + transactions.get(i).getValue();
        }
        return saldo;
    }

    public double countTransactionsExpense(){
        double saldo = 0;
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getTransactionType().equals("expense")) {
                saldo = saldo + transactions.get(i).getValue();
            }
        }
        return saldo;
    }

    public int countTransactionExpense(Date currentDay){
        double saldo = 0;
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(currentDay);
        for (int i = 0; i < transactions.size(); i++) {
            cal2.setTime(transactions.get(i).getTransactionDate());
            if (transactions.get(i).getTransactionType().equals("expense") && sameDay(cal1,cal2) ) {
                saldo = saldo + transactions.get(i).getValue();
            }
        }
        return (int)saldo;
    }

    private boolean sameDay(Calendar cal1, Calendar cal2){

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    public double countTransactionsIncome(){
        double saldo = 0;
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getTransactionType().equals("income")) {
                saldo = saldo + transactions.get(i).getValue();
            }
        }
        return saldo;
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
