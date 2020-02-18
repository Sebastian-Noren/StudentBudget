package se.hkr.studentbudget.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DataBaseAccess instance;
    private Cursor c = null;
    private String tag = "Info";

    //Account table values
    private static final String TABLE_ACCOUNT = "account";
    private static final String ACCOUNT_NAME_COL1 = "account_name";
    private static final String ACCOUNT_VALUE_COL2 = "account_value";
    private static final String ACCOUNT_NOTE_COL3 = "account_note";
    private static final String ACCOUNT_IMG_COL4 = "account_imgicon";

    //private constructor so that object creation rom outside the class is avoided
    private DataBaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    //To return a single instance of database
    public static DataBaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DataBaseAccess(context);
        }
        return instance;
    }

    //Open conection to the database
    public void openDatabase() {
        this.db = openHelper.getWritableDatabase();
    }

    //closing connection to database
    public void closeDatabe() {
        if (db != null) {
            this.db.close();
        }
    }

    //Methods to get results from database



     public boolean inserAccountInDatabase(String accountName, double accountValue, String accountNotes, int img){
         ContentValues accountContent = new ContentValues();
         accountContent.put(ACCOUNT_NAME_COL1,accountName);
         accountContent.put(ACCOUNT_VALUE_COL2,accountValue);
         accountContent.put(ACCOUNT_NOTE_COL3,accountNotes);
         accountContent.put(ACCOUNT_IMG_COL4,img);

         long result = db.insert(TABLE_ACCOUNT,null,accountContent);

         if (result == -1){
             Log.e(tag, "Could not insert in database");
             return false;
         }else {
             Log.i(tag, "Insert completed");
             return true;
         }
     }

    //Example method
    public String getItem() {
        c = db.rawQuery("SELECT * FROM account", null);

        while (c.moveToNext()) {
             Log.i("Info", c.getString(0));
            Log.i("Info", String.valueOf(c.getDouble(1)));
            Log.i("Info", c.getString(2));
            Log.i("Info", String.valueOf(c.getInt(3)));
        }
        return "";
    }
}
