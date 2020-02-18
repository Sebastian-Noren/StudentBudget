package se.hkr.studentbudget.transactions;

public class CategoryRowItem {

    private String mCategoryName;
    private int mCategoryIcon;

    public CategoryRowItem(String categoryName, int catagoryIcon){
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
