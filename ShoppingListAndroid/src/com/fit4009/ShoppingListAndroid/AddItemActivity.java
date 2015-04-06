package com.fit4009.ShoppingListAndroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.fit4009.ShoppingListAndroid.models.Item;

import java.util.ArrayList;

public class AddItemActivity extends Activity {

    ListView itemAddList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // Get the add item ListView
        itemAddList = (ListView)findViewById(R.id.addItemList);

        // Get the database helper
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());

        //Create list of all possible items
        ArrayList<Item> items = new ArrayList<Item>(dbHelper.getAllItems().values());

        //Get adapter for item rows
        ItemAdapter adapter = new ItemAdapter(this, items);

        // Set adapter to ListView
        itemAddList.setAdapter(adapter);

        // Add a click listener for list items
        itemAddList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Grab the selected Item
                Item result = (Item)itemAddList.getAdapter().getItem(i);

                // Return the object to the MainActivity and close this activity
                Intent intent = new Intent();
                intent.putExtra("result", result);
                intent.putExtra("itemName", result.getName());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}