package HKR.studentbudget.transactions;

public class SpendingRowItem {

    private String mCategoryName;
    private int mCategoryIcon;

    public SpendingRowItem(String categoryName, int catagoryIcon){
        this.mCategoryName = categoryName;
        this.mCategoryIcon = catagoryIcon;
    }

    public String getmCategoryName() {
        return mCategoryName;
    }

    public int getmCategoryIcon() {
        return mCategoryIcon;
    }
}
