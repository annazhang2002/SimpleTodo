package com.example.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import org.apache.commons.io.FileUtils;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // a numeric code to identify the edit activity
    public final static int EDIT_REQUEST_CODE = 20;
    // keys used for passing data between activities
    public final static String ITEM_TEXT = "itemText";
    public final static String ITEM_POSITION = "itemPosition";

    // list of items in the todo list
    ArrayList<String> items;

    // declare the itemAdapter
    ItemsAdapter itemsAdapter;

    // declare the views in our xml file
    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;



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

        // method to set function for onLongClick on RV item
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

        // method to set function for onClick on RV item
        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "Single click at position" + position);

                // create the new activity
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);

                // pass the data being edited
                i.putExtra(ITEM_TEXT, items.get(position));
                i.putExtra(ITEM_POSITION, position);

                // display the activity
                startActivityForResult(i, EDIT_REQUEST_CODE);
            }
        };


        // Getting an adapter object from the class we just made
        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            // retrieve the updated text value
            String itemText = data.getStringExtra(ITEM_TEXT);
            // extract the original position of the edited item from the position key
            int position = data.getExtras().getInt(ITEM_POSITION);

            // update the model with the new item text
            items.set(position, itemText);

            // notify the adapter
            itemsAdapter.notifyItemChanged(position);

            // persist the changes
            saveItems();

            // show the user a Toast
            Toast.makeText(getApplicationContext(), "Item successfully updated", Toast.LENGTH_SHORT).show();

        } else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
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