package com.example.sqliteapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button btnAdd, btnViewAll;
    private EditText name, age;
    private Switch active;
    private ListView customerList;
private ArrayAdapter arrayAdapter;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.btnAdd = findViewById(R.id.btn_add);
        this.btnViewAll = findViewById(R.id.btn_viewAll);

        age = findViewById(R.id.et_age);
        name = findViewById(R.id.et_name);
        active = findViewById(R.id.sw_active);
        customerList = findViewById(R.id.lv_customerList);
        DatabaseHelper db = new DatabaseHelper(MainActivity.this);
        ShowCustomersList(db);
        btnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseHelper db = new DatabaseHelper(MainActivity.this);
                ShowCustomersList(db);
            }
        });
        customerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomerModel clicked = (CustomerModel)parent.getItemAtPosition(position);
                DatabaseHelper db = new DatabaseHelper(MainActivity.this);
                db.deleteCustomer(clicked);
                Toast.makeText(MainActivity.this,"Deleted "+clicked.toString(), Toast.LENGTH_SHORT).show();
                ShowCustomersList(db);
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    CustomerModel c = new CustomerModel(-1,
                            name.getText().toString(),
                            Integer.parseInt(age.getText().toString()),
                            active.isChecked());
                    DatabaseHelper db = new DatabaseHelper(MainActivity.this);
                    boolean verdict = db.addCustomer(c);
                    Toast.makeText(MainActivity.this, verdict?"Added":"Not Added", Toast.LENGTH_SHORT).show();
                    ShowCustomersList(db);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void ShowCustomersList(DatabaseHelper db) {
        arrayAdapter = new ArrayAdapter<CustomerModel>(
                MainActivity.this, android.R.layout.simple_list_item_1, db.getPeople()
        );
        customerList.setAdapter(arrayAdapter);
    }
}