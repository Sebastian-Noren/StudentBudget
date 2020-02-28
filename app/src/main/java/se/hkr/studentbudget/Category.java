package se.hkr.studentbudget;

public class Category {

    private String categoryname;
    private double totalValue;

    public Category(String categoryname, double totalValue) {
        this.categoryname = categoryname;
        this.totalValue = totalValue;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }
}
