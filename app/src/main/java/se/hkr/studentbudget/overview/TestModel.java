package se.hkr.studentbudget.overview;

public class TestModel {

    public static final int CARD1=0;
    public static final int CARD2=1;
    public static final int CARD3=2;

    private int type;

    public TestModel(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
