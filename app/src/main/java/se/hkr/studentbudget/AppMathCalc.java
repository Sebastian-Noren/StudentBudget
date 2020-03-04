package se.hkr.studentbudget;

import java.util.Calendar;
import java.util.Date;

import static se.hkr.studentbudget.AppConstants.accounts;
import static se.hkr.studentbudget.AppConstants.currentMonthTransaction;

public class AppMathCalc {

    private static String tag = "Info";


    public int countTransactionExpense(Date currentDay) {
        double saldo = 0;
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(currentDay);
        for (int i = 0; i < currentMonthTransaction.size(); i++) {
            cal2.setTime(currentMonthTransaction.get(i).getTransactionDate());
            if (currentMonthTransaction.get(i).getTransactionType().equals(StaticStrings.EXPENSE) && sameDay(cal1, cal2)) {
                saldo = saldo + currentMonthTransaction.get(i).getValue();
            }
        }
        return (int) saldo;
    }

    private boolean sameDay(Calendar cal1, Calendar cal2) {

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    //count total amount for all accounts.
    public double accountTotalSaldo() {
        double saldo = 0;
        for (int i = 0; i < accounts.size(); i++) {
            saldo = saldo + accounts.get(i).getAccountValue();
        }
        return saldo;
    }
}
