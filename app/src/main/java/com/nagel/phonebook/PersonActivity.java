package com.nagel.phonebook;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.Calendar;

/* Is used to display contact details, but also used to create new contacts, edit existing contacts, and
delete contacts.

The class defines several variables, but for the sake of the code, the most important are
the first two, which define a Zipcode and a Person object, respectively. The other variables
are components in the user interface and are initialized in onCreate(). When the relevant
activity is opened, a parameter is transferred, either a Zipcode object (if you want to create
a new contact) or a Person object (if you want to view or edit a contact). */
public class PersonActivity extends AppCompatActivity {

    private Zipcode zipcode = null;
    private Person person = null;
    private EditText txtZip, txtFirstName, txtLastName, txtAddress, txtPhone, txtEmail, txtDate, txtTitle, txtText;


    /* onCreate() starts
    by referencing a Zipcode object, and if this reference is not null, it is a Zipcode object that
    has been transferred and the variable zipcode is initialized (while the variable person is still
    null). Next, the field txtDate is set to date, as this field must contain the date of last change
    of the contact. If obj is zero, it is because the transferred parameter is a Person object, and
    the variable person is then initialized with this object. It is the object to be displayed in the
    user interface, and all fields are initialized with the object’s values. Finally, note that the
    txtDate and txtZip fields are read-only as these fields can not be changed. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        Intent intent = getIntent();
        txtFirstName = findViewById(R.id.etfirstname);
        txtLastName = findViewById(R.id.etlastname);
        txtAddress = findViewById(R.id.etaddress);
        txtPhone = findViewById(R.id.etphone);
        txtEmail = findViewById(R.id.etemail);
        txtDate = findViewById(R.id.etdate);
        txtTitle = findViewById(R.id.ettitle);
        txtText = findViewById(R.id.etdescription);
        txtZip = findViewById(R.id.etzip);
        Object obj = intent.getSerializableExtra("zipcode");
        if (obj != null) {
            zipcode = (Zipcode) obj;
            Calendar cal = Calendar.getInstance();
            txtDate.setText(String.format("%02d-%02d-%d", cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)));
        }
        else
        {
            person = (Person)intent.getSerializableExtra("person");
            txtFirstName.setText(person.getFirstname());
            txtLastName.setText(person.getLastname());
            txtAddress.setText(person.getAddress());
            txtPhone.setText(person.getPhone());
            txtEmail.setText(person.getMail());
            txtDate.setText(person.getDate());
            txtTitle.setText(person.getTitle());
            txtText.setText(person.getDescription());
            zipcode = person.getZipcode();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtZip.setText(zipcode.toString());
        disable(txtDate);
        disable(txtZip);
    }

    /* disables edittext fields*/
    private void disable(EditText view)
    {
        view.setKeyListener(null);
        view.setEnabled(false);
    }
    /* adds a back button to the ACTIONBAR*/
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    /* onOkay() tests whether a first name has been entered (it is assumed that a contact must have
    a first name). If so, the handler determines the other values entered in the user interface,
    and then create an object that represents the database. Here you must note the syntax as
    well as the database is writeable. Next, a ContentValue object is created containing
    the values to be written to the database, whether it is because a new row must be created
    or it is an existing row to be updated. Note again the syntax and note how to refer to the
    individual columns using the constants defined in the class DbHelper. Next, the variable
    person is tested, and if it is null, it is an INSERT and otherwise it should be an UPDATE.
    Basically, it happens the same way, in the one case, you use the method insert() method,
    while in the second case, you use the method update(), except that in the latter case you
    should specify a WHERE part. */
    public void onOkay(View view)
    {
        String fname = txtFirstName.getText().toString().trim();
        if (fname.length() > 0)
        {
            String lname = txtLastName.getText().toString().trim();
            String addr = txtAddress.getText().toString().trim();
            String phone = txtPhone.getText().toString().trim();
            String mail = txtEmail.getText().toString().trim();
            String date = txtDate.getText().toString().trim();
            String title = txtTitle.getText().toString().trim();
            String text = txtText.getText().toString().trim();

            DbHelper dbHelper = new DbHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues(8);
            values.put(DbHelper.ATABLE_COLUMNS[DbHelper.ACOLUMN_FIRSTNAME], fname);
            values.put(DbHelper.ATABLE_COLUMNS[DbHelper.ACOLUMN_LASTNAME], lname);
            values.put(DbHelper.ATABLE_COLUMNS[DbHelper.ACOLUMN_ADDRESS], addr);
            values.put(DbHelper.ATABLE_COLUMNS[DbHelper.ACOLUMN_CODE], zipcode.getCode());
            values.put(DbHelper.ATABLE_COLUMNS[DbHelper.ACOLUMN_PHONE], phone);
            values.put(DbHelper.ATABLE_COLUMNS[DbHelper.ACOLUMN_MAIL], mail);
            values.put(DbHelper.ATABLE_COLUMNS[DbHelper.ACOLUMN_TITLE], title);
            values.put(DbHelper.ATABLE_COLUMNS[DbHelper.ACOLUMN_TEXT], text);

            if (person == null)
            {
                values.put(DbHelper.ATABLE_COLUMNS[DbHelper.ACOLUMN_DATE], date);
                db.insert(DbHelper.ATABLE_NAME, null, values);
            }
            else
            {
                Calendar cal = Calendar.getInstance();
                values.put(DbHelper.ATABLE_COLUMNS[DbHelper.ACOLUMN_DATE], String.format("%02d-%02d-%d", cal.get(Calendar.DAY_OF_MONTH),
                        cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)));
                String[] args = { "" + person.getId() };
                db.update(DbHelper.ATABLE_NAME, values, "id = ?", args);
            }
            db.close();
            onSupportNavigateUp();
        }
    }

    /* Finally, there is the last event handler used to delete a contact and thus perform a SQL
    DELETE. It’s basically done just in the same way as above, but where you must use the
    method delete(). */
    public void onRemove(View view)
    {
        if (person != null)
        {
            DbHelper dbHelper = new DbHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String[] args = { "" + person.getId() };
            db.delete(DbHelper.ATABLE_NAME, "id = ?", args);
            db.close();
            onSupportNavigateUp();
        }
    }
}