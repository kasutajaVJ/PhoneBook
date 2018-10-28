package com.nagel.phonebook;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* This class in principle works in the same way as Zipcodes class but here the constructor instead determines a list of Person objects.
Also the difference is how the SQL SELECT statement is defined.
The first finds all contacts that have a specific zip code, but this time the Cursor object is created using a method rawQuery().
It has two parameters, the first being a regular SELECT statement written as a string.
In the same way as you have seen before, the SQL statement may have parameters as indicated by ? and the last parameter for the method rawQuery()
should be an array of values to be substituted for the ? placeholders.
After the statement is completed the Cursor uses the object in the same way as before, but this time, Person objects are created.
Note how the individual columns are referenced with constants from the class DbHelper.
The other constructor has an addition to a reference to an open database with four parameters, all of which are strings.
The constructor must find all contacts where the first name starts with a certain value, where the last name starts with a certain value, where
the address contains a certain value and the title contains a particular value.
The Cursor object is created this time using the method query(), but the first four parameters are used to define a WHERE part.
The actual WHERE part is a string (an expression), and the following parameter is an array of strings to the expression’s placeholders. */
public class People implements Serializable {

    private List<Person> persons = new ArrayList();

    public People(SQLiteDatabase db, Zipcode zipcode)
    {
        try {
            String[] params = new String[]{zipcode.getCode()};
            Cursor cursor = db.rawQuery("select * from addresses where code = ?", params);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                int id = cursor.getInt(DbHelper.ACOLUMN_ID);
                String fname = cursor.getString(DbHelper.ACOLUMN_FIRSTNAME);
                String lname = cursor.getString(DbHelper.ACOLUMN_LASTNAME);
                String addr = cursor.getString(DbHelper.ACOLUMN_ADDRESS);
                String phone = cursor.getString(DbHelper.ACOLUMN_PHONE);
                String mail = cursor.getString(DbHelper.ACOLUMN_MAIL);
                String date = cursor.getString(DbHelper.ACOLUMN_DATE);
                String title = cursor.getString(DbHelper.ACOLUMN_TITLE);
                String text = cursor.getString(DbHelper.ACOLUMN_TEXT);
                persons.add(new Person(id, fname, lname, addr, zipcode, phone, mail, date, title, text));
            }
            cursor.close();
        }
        catch (Exception ex)
        {
            persons.clear();
        }
    }

    public People(SQLiteDatabase db, String firstname, String lastname, String address, String persontitle)
    {
        try {
            Cursor cursor = db.query(DbHelper.ATABLE_NAME, DbHelper.ATABLE_COLUMNS,
                    "firstname like ? and lastname like ? and address like ? and title like ?", new String[]
                            { firstname + "%", lastname + "%", "%" + address + "%", "%" + persontitle + "%" },
                    null, null, null);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
            {
                int id = cursor.getInt(DbHelper.ACOLUMN_ID);
                String fname = cursor.getString(DbHelper.ACOLUMN_FIRSTNAME);
                String lname = cursor.getString(DbHelper.ACOLUMN_LASTNAME);
                String addr = cursor.getString(DbHelper.ACOLUMN_ADDRESS);
                String code = cursor.getString(DbHelper.ACOLUMN_CODE);
                String phone = cursor.getString(DbHelper.ACOLUMN_PHONE);
                String mail = cursor.getString(DbHelper.ACOLUMN_MAIL);
                String date = cursor.getString(DbHelper.ACOLUMN_DATE);
                String title = cursor.getString(DbHelper.ACOLUMN_TITLE);
                String text = cursor.getString(DbHelper.ACOLUMN_TEXT);
                persons.add(new Person(id, fname, lname, addr, getZipcode(db, code), phone, mail, date, title, text));
            }
            cursor.close();
        }
        catch (Exception ex)
        {
            persons.clear();
        }
    }

    public List<Person> getPeople()
    {
        return persons;
    }

    private Zipcode getZipcode(SQLiteDatabase db, String code)
    {
        Cursor cursor = db.query(DbHelper.ZTABLE_NAME, DbHelper.ZTABLE_COLUMNS, "code = ?", new String[] { code },
                null, null, null);
        cursor.moveToFirst();
        return new Zipcode(cursor.getString(DbHelper.ZCOLUMN_CODE), cursor.getString(DbHelper.ZCOLUMN_CITY));
    }
}
