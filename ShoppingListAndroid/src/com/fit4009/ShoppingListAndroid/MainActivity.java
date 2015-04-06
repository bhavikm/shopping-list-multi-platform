package com.fit4009.ShoppingListAndroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.fit4009.ShoppingListAndroid.models.Item;
import com.fit4009.ShoppingListAndroid.models.ShoppingList;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends Activity {

    public static final int ADD_ITEM_REQUEST = 1;
    //public static final String JSON_DOWNLOAD_LOCATION = "http://elliott-wilson.com/items/allItems.php";

    //Using webservice from my website
    public static final String JSON_DOWNLOAD_LOCATION = "http://bhavikm.org/fit4039/items.php";

    private ListView itemsList;
    private ArrayList<Item> shoppingListItems;
    private ShoppingList defaultShoppingList;
    private DatabaseHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Get our database helper
        dbHelper = new DatabaseHelper(getApplicationContext());

        // Check if any items exist, if not get data from web service
        if (dbHelper.getAllItems().size() == 0) {
            /*
            // Old statements that don't use web service to initialise item data

            // Create some start items
            Item item1 = new Item("Apple", "A tasty apple", 0.25);
            Item item2 = new Item("Banana", "A ripe banana", 0.35);
            Item item3 = new Item("Toothpaste", "Mint toothpaste", 1.25);
            Item item4 = new Item("Milk", "Full cream milk", 2.50);
            Item item5 = new Item("Bread Loaf", "A rye loaf of bread", 3.10);

            // Add them to the database
            dbHelper.addItem(item1);
            dbHelper.addItem(item2);
            dbHelper.addItem(item3);
            dbHelper.addItem(item4);
            dbHelper.addItem(item5);
            */

            //Download JSON data from server to populate item types
            new SetupItemsDatasetTask().execute(JSON_DOWNLOAD_LOCATION);

            // And add a default shopping list (which we will use for our main ListView)
            ShoppingList list = new ShoppingList("Default shopping list");
            dbHelper.addShoppingList(list);
        }

        // Get the ListView associated with layout
        itemsList = (ListView)findViewById(R.id.itemsList);

        // Create our adapter and associate ArrayList
        defaultShoppingList = dbHelper.getDefaultShoppingList();
        shoppingListItems = dbHelper.getItemsFromShoppingList(defaultShoppingList);
        ItemAdapter adapter = new ItemAdapter(this, shoppingListItems);

        //For debug, print out all the shopping list items attached to the default shopping list
        /*
        for (Item i : shoppingListItems)
            System.out.println("Item: " + i.getName() + "\n");
        */

        // Associate adapter with ListView
        itemsList.setAdapter(adapter);

        // Show dialog when holding list item to remove it
        itemsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                // Build dialog to delete item
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Remove item?");
                builder.setMessage("Are you sure you wish to remove this item from the shopping list?");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Remove item from Database
                        Item item = shoppingListItems.get(position);
                        dbHelper.removeItemFromShoppingList(defaultShoppingList, item);

                        // Update ListView
                        refreshListView();
                        Toast.makeText(getBaseContext(), "Item has been removed.", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Close the dialog
                        dialogInterface.cancel();
                    }
                });

                // Create and show dialog
                builder.create().show();

                return false;
            }
        });



        // Update item count
        updateItemCountAndPrice();
    }


    // Creates menu items for ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // ActionBar handler for selected items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                // Move to AddItemActivity and await result
                Intent i = new Intent(this, AddItemActivity.class);
                startActivityForResult(i, ADD_ITEM_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // We override this method when we are expecting a result from an
    // activity we have called.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_ITEM_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());

                // Grab the Item object out of the intent
                Item item = data.getParcelableExtra("result");

                dbHelper.addItemToShoppingList(item, defaultShoppingList);
                refreshListView();
                Toast.makeText(this, "Added item to list.", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void updateItemCountAndPrice() {
        // Get total number of items
        int totalItems = shoppingListItems.size();

        //Calculate total price of items in list
        double totalPrice = 0.0;
        for (Item i : shoppingListItems)
            totalPrice = totalPrice + i.getPrice();

        // Update TextView
        TextView itemCountText = (TextView)findViewById(R.id.totalItemsText);
        itemCountText.setText("Total Items: " + totalItems + " - Total Price: $" + totalPrice);
    }

    private void refreshListView() {
        // Get current items, update ListView then items count and price
        shoppingListItems = dbHelper.getItemsFromShoppingList(defaultShoppingList);
        itemsList.setAdapter(new ItemAdapter(this, shoppingListItems));
        updateItemCountAndPrice();
    }



    //For connecting to web service and receing items via JSON response
    private class SetupItemsDatasetTask extends AsyncTask<String, Void, String> {

        //Asynchronously download item data
        protected String doInBackground(String... urls) {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(urls[0]);

            try {
                HttpResponse response = client.execute(request);
                InputStream input = response.getEntity().getContent();

                String result = "";
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder sb = new StringBuilder();
                while ((result = reader.readLine()) != null) {
                    sb.append(result);
                }

                return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        //Parse the JSON response into individual components and add to database
        protected void onPostExecute(String results) {
            if (results != null) {
                try {
                    JSONArray itemContents = new JSONArray(results);

                    for (int i = 0; i < itemContents.length(); i++) {
                        JSONObject itemJson = itemContents.getJSONObject(i);
                        Item item = new Item(itemJson.getLong("id"),
                                itemJson.getString("name"),
                                itemJson.getString("description"),
                                itemJson.getDouble("price"));
                        dbHelper.addItem(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
