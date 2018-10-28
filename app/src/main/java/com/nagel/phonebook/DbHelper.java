package com.nagel.phonebook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/* The class inherits SQLiteOpenHelper, and the constructor in the base class checks, if there
 * is a database with that name. If not, it will perform onCreate(). If the database is found,
 * the version number is tested and if it is larger than the database version number the method
 * onUpgrade() is performed. If the version number is smaller than the database version number
 * the method onDowngrade() is performed. A SQLiteOpenHelper class should therefore override
 * these three methods – however, in many cases you do not override the last one (the version
 * number will always be higher). */
public class DbHelper extends SQLiteOpenHelper{

    /* The class defines a large number of constants that defines the database. They are not absolutely necessary but they make the code easier to read
   and understand + make the maintenance of the code significantly easier.
   There are two sets of constants for each of the two database tables.
   A constant for the database name and a constant for the database version.
   There are constants for the two SQL expressions that create the database tables. */
    public static final String ZTABLE_NAME = "zipcodes";
    public static final int ZCOLUMN_CODE = 0;
    public static final int ZCOLUMN_CITY = 1;
    public static final String[] ZTABLE_COLUMNS = new String[] { "code", "city" };
    public static final String ATABLE_NAME = "addresses";
    public static final int ACOLUMN_ID = 0;
    public static final int ACOLUMN_FIRSTNAME = 1;
    public static final int ACOLUMN_LASTNAME = 2;
    public static final int ACOLUMN_ADDRESS = 3;
    public static final int ACOLUMN_CODE = 4;
    public static final int ACOLUMN_PHONE = 5;
    public static final int ACOLUMN_MAIL = 6;
    public static final int ACOLUMN_DATE = 7;
    public static final int ACOLUMN_TITLE = 8;
    public static final int ACOLUMN_TEXT = 9;
    public static final String[] ATABLE_COLUMNS = new String[]{
            "id", "firstname", "lastname", "address", "code", "phone", "mail", "date", "title", "text"
    };
    private static final String DBFILENAME = "phonebook.db";
    private static final int DBVERSION = 1;
    private static final String ZINITIAL_SCHEMA = "create table zipcodes (code char(4) " +
            "primary key, city varchar(30) not null)";
    private static final String AINITIAL_SCHEMA = "create table addresses (" +
            "id integer primary key autoincrement," +
            "firstname varchar(30)," +
            "lastname varchar(50)," +
            "address varchar(50)," +
            "code char(4) not null," +
            "phone varchar(20)," +
            "mail varchar(50)," +
            "date varchar(10)," +
            "title varchar(50)," +
            "text varchar(1000)," +
            "foreign key (code) references zipcodes (code))";
    private Context context;

    public DbHelper(Context context) {
        super(context, DBFILENAME, null, DBVERSION);
        this.context = context;
    }

    /* Creates two tables, it is the constructor in the base class that creates the database,
       while onCreate() creates the tables. onCreate() calls the method insertZipcodes().
       It uses a StringBuilder to dynamically create a SQL INSERT using the resource with the zip codes,
       and the method returns the result as a string. onCreate() has a parameter of the type SQLiteDatabase
       and it has methods, such as header execSQL(), which performs a SQL statement.
       The method is used to create the two tables and can generally be used to execute a SQL statement
       represented as a string and thus also the result of insertZipcodes().Program also overrides method onUpgrade.
       -----------------------------------------------------------------------------------------------------------------------------------------
       Which is trivial as there isn't any code, but the meaning is
       that you have to insert the code to be used if the version number is increased (until you define the final version).
       When you develop a program, you will typically be interested in creating the database each time you run the program.
       This can be enforced by increasing the version number, and then adding the code to onUpgrade(), which deletes the existing tables and re-creates them. */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ZINITIAL_SCHEMA);
        db.execSQL(AINITIAL_SCHEMA);
        db.execSQL(insertZipcodes());
    }

    private String insertZipcodes() {
        InputStream stream = context.getResources().openRawResource(R.raw.zipcodes);
        StringBuilder builder = new StringBuilder("insert into zipcodes (code, city) values");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream)))
        {
            addRow(builder, reader.readLine());
            for (String line = reader.readLine(); line != null; line = reader.readLine())
            {
                if (line.length() > 0)
                {
                    builder.append(",");
                    addRow(builder,line);
                }
            }
        }
        catch (Exception e)
        {}
        return builder.toString();
    }

    private void addRow(StringBuilder builder, String line) {
        String[] elems = line.split(",");
        builder.append("('");
        builder.append(elems[0]);
        builder.append("','");
        builder.append(elems[1]);
        builder.append("')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            try {
                db.execSQL("ALTER TABLE addresses ADD COLUMN text VARCHAR(1000) DEFAULT '' ");
            } catch (Exception ex) {}
        }
    }
}
