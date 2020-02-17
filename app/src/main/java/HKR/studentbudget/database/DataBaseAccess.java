package HKR.studentbudget.database;

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

    //private constructor so that object creation rom outside the class is avoided
    private DataBaseAccess(Context context){
        this.openHelper = new DatabaseOpenHelper(context);
    }

    //To return a single instance of database
    public static DataBaseAccess getInstance(Context context){
        if (instance==null){
            instance = new DataBaseAccess(context);
        }
        return instance;
    }

    //Open conection to the database
    public void openDatabase(){
        this.db = openHelper.getWritableDatabase();
    }

    //closing connection to database
    public void closeDatabe(){
        if (db!=null){
            this.db.close();
        }
    }

    //Methods to get results from database

    //Example method
    public String getItem(){
    c=db.rawQuery("SELECT * FROM transactions",null);

    while (c.moveToNext()){
       // Log.i("Info", c.getString(0));
        //Log.i("Info", String.valueOf(c.getDouble(1)));
    }
    return "";
    }
}
