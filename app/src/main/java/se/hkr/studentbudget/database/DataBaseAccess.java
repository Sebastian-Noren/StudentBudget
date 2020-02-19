package se.hkr.studentbudget.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import se.hkr.studentbudget.account.Account;

public class DataBaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DataBaseAccess instance;
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

    // insert account into the database
    public boolean insertAccountInDatabase(String accountName, double accountValue, String accountNotes, int img) {
        ContentValues accountContent = new ContentValues();
        accountContent.put(ACCOUNT_NAME_COL1, accountName);
        accountContent.put(ACCOUNT_VALUE_COL2, accountValue);
        accountContent.put(ACCOUNT_NOTE_COL3, accountNotes);
        accountContent.put(ACCOUNT_IMG_COL4, img);
        long result = db.insert(TABLE_ACCOUNT, null, accountContent);

        if (result == -1) {
            Log.e(tag, "Could not insert in database");
            return false;
        } else {
            Log.i(tag, "Insert completed");
            return true;
        }
    }

    //Delete selected account
    public boolean deleteAccount(String account) {

        String whereClause = ACCOUNT_NAME_COL1 + "='" + account + "'";
        long result = db.delete(TABLE_ACCOUNT, whereClause, null);
        if (result == -1) {
            Log.e(tag, "Could not remove account in database");
            return false;
        } else {
            Log.i(tag, account + " removed completed");
            return true;
        }
    }

    //get all saved accounts and return them to app constants
    public ArrayList<Account> getAccount() {
        ArrayList<Account> accountFromDatabase = new ArrayList<>();
        Cursor c;
        String query = String.format("SELECT * FROM %s", TABLE_ACCOUNT);
        c = db.rawQuery(query, null);

        while (c.moveToNext()) {
            String name = c.getString(0);
            double value = c.getDouble(1);
            String notes = c.getString(2);
            int icon = c.getInt(3);

            Account m = new Account(name, value, notes, icon);
            accountFromDatabase.add(m);
        }

        return accountFromDatabase;
    }
}
