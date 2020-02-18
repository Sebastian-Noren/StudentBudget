package se.hkr.studentbudget;

import java.util.Date;

public class Transactions {

    private int id;
    private String description;
    private double value;
    private String category;
    private String transactionType;
    private Date transactionDate;

    public Transactions(int id, String description, double value, String category, String transactionType, Date transactionDate) {
        this.id = id;
        this.description = description;
        this.value = value;
        this.category = category;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
}
