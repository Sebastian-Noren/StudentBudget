package se.hkr.studentbudget.overview;

public class TestModel {

    public static final int EXPENSES_CARD = 0;
    public static final int SUMMARY_CARD = 1;
    public static final int ACCOUNT_CARD = 2;
    public static final int TRANSACTIONS_CARD = 3;
    public static final int BUDGET_CARD = 4;

    private int type;

    public TestModel(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
