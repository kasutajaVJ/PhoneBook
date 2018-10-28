package com.nagel.phonebook;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* The class Zipcodes represents all zip codes. The class has a single instance variable which is used for the Zipcode objects.
 * The list is initialized in the constructor its parameter is an open database.
 * The constructor starts by creating a Cursor which is an object that represents a SQL SELECT and can be used to traverse the result.
 * SELECT statement is performed using the method query().
 * The method has a parameter for each of the elements that can be included in a SELECT statement, I have only set values for the first two parameters,
 * since the statement should retrieve all zip codes in the database.
 * You should note how the table name and column names are defined using the constants in the class DbHelper.
 * After the statement has been completed, you can traverse the rows using the Cursor object. */
public class Zipcodes implements Serializable {

    private List<Zipcode> zipcodes = new ArrayList<>();

    public Zipcodes(SQLiteDatabase db){
        try{
            Cursor cursor = db.query(DbHelper.ZTABLE_NAME, DbHelper.ZTABLE_COLUMNS, null, null, null, null, null);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
            {
                String code = cursor.getString(DbHelper.ZCOLUMN_CODE);
                String city = cursor.getString(DbHelper.ZCOLUMN_CITY);
                zipcodes.add(new Zipcode(code,city));
            }
            cursor.close();
        }
        catch (Exception ex) { zipcodes.clear(); }
    }

    public List<Zipcode> getZipcodes(){ return zipcodes; }
}
