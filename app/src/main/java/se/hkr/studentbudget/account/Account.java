package se.hkr.studentbudget.account;

public class Account {

    private String accountName;
    private double accountValue;
    private String accountNote;
    private int imgIcon;

    public Account(String accountName, double accountValue, String accountNote, int imgIcon) {
        this.accountName = accountName;
        this.accountValue = accountValue;
        this.accountNote = accountNote;
        this.imgIcon = imgIcon;
    }

    public int getImgIcon() {
        return imgIcon;
    }

    public void setImgIcon(int imgIcon) {
        this.imgIcon = imgIcon;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public double getAccountValue() {
        return accountValue;
    }

    public void setAccountValue(double accountValue) {
        this.accountValue = accountValue;
    }

    public String getAccountNote() {
        return accountNote;
    }

    public void setAccountNote(String accountNote) {
        this.accountNote = accountNote;
    }
}
