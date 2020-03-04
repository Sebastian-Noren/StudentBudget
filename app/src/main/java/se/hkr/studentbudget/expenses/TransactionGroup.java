package se.hkr.studentbudget.expenses;

import java.util.ArrayList;

import se.hkr.studentbudget.transactions.Transactions;

public class TransactionGroup {
    private String itemTitle;
    private double value;
    private ArrayList<Transactions> subItemList;

    public TransactionGroup(String itemTitle, double value, ArrayList<Transactions> subItemList) {
        this.itemTitle = itemTitle;
        this.subItemList = subItemList;
        this.value = value;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public ArrayList<Transactions> getSubItemList() {
        return subItemList;
    }

    public void setSubItemList(ArrayList<Transactions> subItemList) {
        this.subItemList = subItemList;
    }
}
