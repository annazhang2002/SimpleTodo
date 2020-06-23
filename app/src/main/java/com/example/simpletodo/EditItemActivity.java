package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    EditText etItem;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        etItem = findViewById(R.id.etItem);
        btnSave = findViewById(R.id.btnSave);

        // Change the title of the page
        getSupportActionBar().setTitle("Edit item");

        etItem.setText(getIntent().getStringExtra(MainActivity.ITEM_TEXT));

        // when the user is done editing and clicks save
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create an intent which will contain the results
                Intent intent = new Intent();

                // pass the data
                intent.putExtra(MainActivity.ITEM_TEXT, etItem.getText().toString());
                intent.putExtra(MainActivity.ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.ITEM_POSITION));

                // set the results of the intent
                setResult(RESULT_OK, intent);

                // finish activity
                finish();
            }
        });
    }
}