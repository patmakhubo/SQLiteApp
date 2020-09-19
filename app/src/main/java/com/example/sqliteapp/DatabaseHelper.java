package com.example.sqliteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String CUSTOMER_TABLE = "CUSTOMER_TABLE";
    private static final String ID = "ID";
    private static final String CUSTOMER_NAME = "CUSTOMER_NAME";
    private static final String CUSTOMER_AGE = "CUSTOMER_AGE";
    private static final String ACTIVE_CUSTOMER = "ACTIVE_CUSTOMER";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "customer.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql =
        "CREATE TABLE " + CUSTOMER_TABLE + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CUSTOMER_NAME + " TEXT, " + CUSTOMER_AGE + " INT, " + ACTIVE_CUSTOMER + " BOOL) ";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CUSTOMER_TABLE);
        this.onCreate(db);
    }

    public boolean addCustomer (CustomerModel customer) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CUSTOMER_NAME, customer.getName());
        cv.put(CUSTOMER_AGE, customer.getAge());
        cv.put(ACTIVE_CUSTOMER, customer.isActive());

        long x = database.insert(this.CUSTOMER_TABLE, null,cv);
        if(x == -1) return false; else return true;
    }

    public boolean deleteCustomer(CustomerModel model){
        SQLiteDatabase delRec= getWritableDatabase();
        String query = "DELETE FROM " + CUSTOMER_TABLE + " WHERE " + ID + " = " + model.getId();
        Cursor cur = delRec.rawQuery(query,null);
        return cur.moveToFirst();
    }
    public List<CustomerModel> getPeople(){
        List<CustomerModel> returnList = new ArrayList<>();
        String query = "SELECT * FROM " + this.CUSTOMER_TABLE;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                int age =  cursor.getInt(2);
                boolean active = cursor.getInt(3) == 1 ? true : false;
                CustomerModel newCustomer = new CustomerModel(id,name,age,active);
                returnList.add(newCustomer);
            }while(cursor.moveToNext());
        }
        database.close();
        return returnList;
    }
}
