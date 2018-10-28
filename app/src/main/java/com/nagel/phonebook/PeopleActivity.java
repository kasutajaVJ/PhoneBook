package com.nagel.phonebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/* PeopleActivity displays a list of contacts found in the database, it’s a simple activity with a ListVew.
Clicking on a contact PersonActivity is shown. The Java code contains nothing regarding databases. */
public class PeopleActivity extends AppCompatActivity {

    private ArrayAdapter<Person> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        People people = (People)intent.getSerializableExtra("people");
        ListView view = findViewById(R.id.lstPeople);
        view.setAdapter(adapter = new ArrayAdapter<>(this, android.R.layout. simple_list_item_1,
                people.getPeople()));
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showPerson(adapter.getItem(position));
            }
        });
    }

    private void showPerson(Person person) {
        Intent intent = new Intent(this, PersonActivity.class);
        intent.putExtra("person", person);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
