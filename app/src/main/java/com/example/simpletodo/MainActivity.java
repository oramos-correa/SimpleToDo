package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> items;

    Button LeAdd;
    EditText TextField;
    RecyclerView LeList;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LeAdd = findViewById(R.id.LeAdd);
        TextField = findViewById(R.id.TextField);
        LeList = findViewById(R.id.LeList);

        //TextField.setText("yep");

        loadItems();
/*
        items = new ArrayList<>();
        items.add("AC");
        items.add("DC");
        items.add("123");
*/
        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                items.remove(position);
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was Removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        //ItemsAdapter itemsAdapter = new ItemsAdapter(items, onLongClickListener);
        itemsAdapter = new ItemsAdapter(items, onLongClickListener);
        LeList.setAdapter(itemsAdapter);
        LeList.setLayoutManager(new LinearLayoutManager(this));

        //Callback when the view is clicked
        LeAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getItem =TextField.getText().toString();
                items.add(getItem);
                itemsAdapter.notifyItemInserted(items.size() - 1);
                TextField.setText("");
                Toast.makeText(getApplicationContext(), "Item was Added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });

    }
    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }

    //Loads objects and makes sure they load in
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    //saves items
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}