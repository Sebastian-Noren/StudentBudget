package se.hkr.studentbudget.budget;

public class BudgetItem {
    private double currentValue;
    private double maxValue;
    int image;
    String progressBarTitle;
    String accountName;

    public BudgetItem(double maxValue, int image, String progressBarTitle, String accountName) {
        this.maxValue = maxValue;
        this.image = image;
        this.progressBarTitle = progressBarTitle;
        this.accountName = accountName;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getProgressBarTitle() {
        return progressBarTitle;
    }

    public void setProgressBarTitle(String progressBarTitle) {
        this.progressBarTitle = progressBarTitle;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    @Override
    public String toString() {
        return "BudgetItem{" +
                "currentValue=" + currentValue +
                ", maxValue=" + maxValue +
                ", image=" + image +
                ", progressBarTitle='" + progressBarTitle + '\'' +
                ", accountName='" + accountName + '\'' +
                '}';
    }
}
