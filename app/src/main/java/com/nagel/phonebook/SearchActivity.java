package com.nagel.phonebook;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/* SearchActivity is also simple and is used to enter search criteria, code is executed in SQL SELECT, but using the class People. */
public class SearchActivity extends AppCompatActivity {

    private EditText txtFirstName, txtLastName, txtAddress, txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtFirstName = findViewById(R.id.etFirst_Name);
        txtLastName = findViewById(R.id.etLast_Name);
        txtAddress = findViewById(R.id.etAddress);
        txtTitle = findViewById(R.id.etTitle);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void onClears(View view)
    {
        txtFirstName.setText("");
        txtLastName.setText("");
        txtAddress.setText("");
        txtTitle.setText("");
    }

    public void onOk(View view)
    {
        String fname = txtFirstName.getText().toString().trim();
        String lname = txtLastName.getText().toString().trim();
        String addr = txtAddress.getText().toString().trim();
        String title = txtTitle.getText().toString().trim();
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        People persons = new People(db, fname, lname, addr, title);
        if (persons.getPeople().size() == 0) {
            Toast.makeText(this, "No contacts found", Toast.LENGTH_LONG).show();
            db.close();
        }
        else if (persons.getPeople().size() == 1) {
            Intent intent = new Intent(this, PersonActivity.class);
            intent.putExtra("person", persons.getPeople().get(0));
            db.close();
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(this, PeopleActivity.class);
            intent.putExtra("persons", persons);
            db.close();
            startActivity(intent);
            finish();
        }
    }
}
