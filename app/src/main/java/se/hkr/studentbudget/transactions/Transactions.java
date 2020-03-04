package se.hkr.studentbudget.transactions;

import java.util.Date;

public class Transactions {

    private int id;
    private String description;
    private double value;
    private String category;
    private String transactionType;
    private String transactionAccount;
    private Date transactionDate;
    private int image;

    public Transactions(String description, double value, String category,
                        String transactionType, String transactionAccount, Date transactionDate, int image) {
        this.description = description;
        this.value = value;
        this.category = category;
        this.transactionType = transactionType;
        this.transactionAccount = transactionAccount;
        this.transactionDate = transactionDate;
        this.image = image;
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

    public String getTransactionAccount() {
        return transactionAccount;
    }

    public void setTransactionAccount(String transactionAccount) {
        this.transactionAccount = transactionAccount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Transactions{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", value=" + value +
                ", category='" + category + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", transactionAccount='" + transactionAccount + '\'' +
                ", transactionDate=" + transactionDate +
                ", image=" + image +
                '}';
    }
}
