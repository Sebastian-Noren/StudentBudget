package se.hkr.studentbudget.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import se.hkr.studentbudget.transactions.Transactions;
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

    //Transaction table
    private static final String TABLE_TRANSACTIONS = "transactions";
    private static final String TRANSAC_ID_COL1 = "id";
    private static final String TRANSAC_DESCRIPTION_COL2 = "description";
    private static final String TRANSAC_VALUE_COL3 = "value";
    private static final String TRANSAC_CATEGORY_COL4 = "category";
    private static final String TRANSAC_TRANSACTYPE_COL5 = "transactionType";
    private static final String TRANSAC_ACCOUNT_NAME_COL6 = "account_name";
    private static final String TRANSAC_DATETIME_COL7 = "dateTime";
    private static final String TRANSAC_IMG_COL8 = "image";

    //TABLE USER
    private static final String TABLE_USER = "user";
    private static final String USER_USERNAME = "username";
    private static final String USER_PASSWORD = "password";

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
    public boolean insertTransactionInDatabase(String description, double value, String category,
                                               String transactionType, String transactionAccount, String transactionDate, int image) {
        ContentValues accountContent = new ContentValues();
        accountContent.put(TRANSAC_DESCRIPTION_COL2, description);
        accountContent.put(TRANSAC_VALUE_COL3, value);
        accountContent.put(TRANSAC_CATEGORY_COL4, category);
        accountContent.put(TRANSAC_TRANSACTYPE_COL5, transactionType);
        accountContent.put(TRANSAC_ACCOUNT_NAME_COL6, transactionAccount);
        accountContent.put(TRANSAC_DATETIME_COL7, transactionDate);
        accountContent.put(TRANSAC_IMG_COL8, image);
        long result = db.insert(TABLE_TRANSACTIONS, null, accountContent);

        if (result == -1) {
            Log.e(tag, "Could not insert transaction in database");
            return false;
        } else {
            Log.i(tag, "Insert completed");
            return true;
        }
    }

    //get all saved accounts and return them to app constants
    public ArrayList<Transactions> getAllTransactions() {
        ArrayList<Transactions> transactionsFromDatabase = new ArrayList<>();
        Cursor c;
        Transactions m;
     //   String query = String.format("SELECT * FROM %s", TABLE_TRANSACTIONS);
        String query = String.format("SELECT * FROM %s WHERE DATE(%s) BETWEEN '2020-01-01' AND  '2020-03-31'", TABLE_TRANSACTIONS, TRANSAC_DATETIME_COL7);
        c = db.rawQuery(query, null);
        while (c.moveToNext()) {

            int id = c.getInt(0);
            String textDesc = c.getString(1);
            double value = c.getDouble(2);
            String category = c.getString(3);
            String transactionType = c.getString(4);
            String transactionAccount = c.getString(5);
            String transactionDate = c.getString(6);
            int image = c.getInt(7);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = null;
            try {
                date = format.parse(transactionDate);
            } catch (ParseException e) {
                Log.e(tag, e.getMessage() + "date conversation fail!");
            }
            m = new Transactions(textDesc, value, category, transactionType, transactionAccount, date, image);
            m.setId(id);
            transactionsFromDatabase.add(m);
        }
        c.close();
        Log.i(tag, "Reading in Transaction completed without problem");
        return transactionsFromDatabase;
    }

    public boolean updateAccountInDatabase(double accountValue, String account) {
        String whereClause = ACCOUNT_NAME_COL1 + "='" + account + "'";
        ContentValues accountContent = new ContentValues();
        accountContent.put(ACCOUNT_VALUE_COL2, accountValue);
        long result = db.update(TABLE_ACCOUNT, accountContent, whereClause, null);
        if (result == -1) {
            Log.e(tag, "Could not insert in database");
            return false;
        } else {
            Log.i(tag, "update completed in database");
            return true;
        }
    }

    //get total sum of categeroy between dates
    public double getTotalSumCategory(String category, String type, String from, String to) {
        Cursor c;
        double value = 0;
        String query = String.format("SELECT SUM(%s) FROM %s WHERE %s = '%s' AND %s = '%s' AND DATE(%s) BETWEEN '%s' AND  '%s'", TRANSAC_VALUE_COL3, TABLE_TRANSACTIONS, TRANSAC_CATEGORY_COL4, category, TRANSAC_TRANSACTYPE_COL5,type, TRANSAC_DATETIME_COL7, from, to);
        c = db.rawQuery(query, null);
        while (c.moveToNext()) {
             value = c.getDouble(0);
        }
        c.close();
        Log.i(tag, "Reading in total sum " + value);
        return value;
    }

    public double getTotalSumTransactionType(String test, String from, String to){
        Cursor c;
        double value = 0;
        String query = String.format("SELECT SUM(%s) FROM %s WHERE %s = '%s' AND DATE(%s) BETWEEN '%s' AND  '%s'", TRANSAC_VALUE_COL3, TABLE_TRANSACTIONS, TRANSAC_TRANSACTYPE_COL5, test, TRANSAC_DATETIME_COL7, from, to);
        c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            value = c.getDouble(0);
        }
        c.close();
        Log.i(tag, "Reading in total sum " + value);
        return value;
    }


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

    public boolean insertPinToDatabase(String hashedPin) {
        ContentValues pinContent = new ContentValues();
        pinContent.put(USER_USERNAME, "user");
        pinContent.put(USER_PASSWORD, hashedPin);
        long result = db.insert(TABLE_USER, null, pinContent);
        if (result == -1) {
            Log.e(tag, "Insert failed");
            return false;
        } else {
            Log.i(tag, "Insert completed");
            return true;
        }
    }


    public boolean existingUser() {
        Cursor c;
        String query = "SELECT count(*) FROM user";
        c = db.rawQuery(query, null);
        c.moveToFirst();
        int count = c.getInt(0);

        if (count > 0) {
            return true;
        } else {
            return false;
        }

    }

    //Delete selected account
    public boolean deleteAccount(String account) {

        String whereClause = TRANSAC_ACCOUNT_NAME_COL6 + "='" + account + "'";
        long result = db.delete(TABLE_ACCOUNT, whereClause, null);
        if (result == -1) {
            Log.e(tag, "Could not remove Transactions in database");
            return false;
        } else {
            Log.i(tag, account + " Transactions removed");
            return true;
        }
    }

    public boolean deleteAllTransactionsFromAccount(String account) {

        String whereClause = ACCOUNT_NAME_COL1 + "='" + account + "'";
        long result = db.delete(TABLE_TRANSACTIONS, whereClause, null);
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
        Account m;
        String query = String.format("SELECT * FROM %s", TABLE_ACCOUNT);
        c = db.rawQuery(query, null);

        while (c.moveToNext()) {
            String name = c.getString(0);
            double value = c.getDouble(1);
            String notes = c.getString(2);
            int icon = c.getInt(3);

            m = new Account(name, value, notes, icon);
            accountFromDatabase.add(m);
        }
        c.close();
        return accountFromDatabase;
    }

    public void readUsers(){
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_USER,null);

        while (c.moveToNext()){
            String username = c.getString(0);
            String password = c.getString(1);
            Log.i(tag,username +" " +password);
        }
    }

    public String getPinFromDatabase(){
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_USER,null);
        c.moveToFirst();
        String password = c.getString(1);
        return password;

    }

}
