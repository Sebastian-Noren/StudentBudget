package se.hkr.studentbudget;

import android.util.Log;

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

    public void updateAccountAmount(int accountChoiceIndex, double amountValue){
      double newValue = accounts.get(accountChoiceIndex).getAccountValue() + amountValue;
      accounts.get(accountChoiceIndex).setAccountValue(newValue);
      //TODO save update to database
    }

}
