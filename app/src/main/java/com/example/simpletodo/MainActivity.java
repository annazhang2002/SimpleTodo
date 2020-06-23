package com.example.simpletodo;

import android.os.Bundle;
import org.apache.commons.io.FileUtils;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // list of items in the todo list
    ArrayList<String> items;

    // declare the views in our xml file
    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find all of the views by their ids
        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        // load items from the data file
        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                // delete item from the model
                items.remove(position);

                // notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        // Getting an adapter object from the class we just made
        itemsAdapter = new ItemsAdapter(items, onLongClickListener);
        // Connect your RV to the adapter
        rvItems.setAdapter(itemsAdapter);
        // Will default put your items in a vertical layout
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        // method for when the add button is clicked
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // convert the editable in the input to a string todo
                String todoItem = etItem.getText().toString();

                // add the todoItem to the model
                items.add(todoItem);

                // notify the adapter that an item is inserted
                // the position is the last item
                itemsAdapter.notifyItemInserted(items.size()-1);

                // clear the editText value
                etItem.setText("");

                // giving the user feedback via toast pop up message that an item was added
                // Toast included length is LENGTH_SHORT and alternative is LENGTH_LONG
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });

    }

    private File getDataFile() {
        // returning the file with the directory and the name of the file
        return new File(getFilesDir(), "data.txt");
    }

    // method loads items by reading every line of the data file
    private void loadItems() {
        try {
            // read the data from the file, populate into an Arraylist and assign that to be items
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivty", "Error reading items", e);
            items = new ArrayList<>();
        }

    }

    // method saves items by writing them into the data file
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivty", "Error reading items", e);
        }
    }
}