package com.fit4009.ShoppingListAndroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.fit4009.ShoppingListAndroid.models.Item;
import com.fit4009.ShoppingListAndroid.models.ShoppingList;
import com.fit4009.ShoppingListAndroid.models.ShoppingListItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Set database properties
    public static final String DATABASE_NAME = "ShoppingListDB";
    public static final int DATABASE_VERSION = 5;

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Item.CREATE_STATEMENT);
        db.execSQL(ShoppingList.CREATE_STATEMENT);
        db.execSQL(ShoppingListItems.CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Item.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ShoppingList.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ShoppingListItems.TABLE_NAME);
        onCreate(db);
    }

    //
    // Item database methods
    //
    public void addItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Item.COLUMN_NAME, item.getName());
        values.put(Item.COLUMN_DESCRIPTION, item.getDescription());
        values.put(Item.COLUMN_PRICE, item.getPrice());

        db.insert(Item.TABLE_NAME, null, values);
        db.close();
    }

    public HashMap<Long, Item> getAllItems() {
        HashMap<Long, Item> items = new LinkedHashMap<Long, Item>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Item.TABLE_NAME, null);

        // Add item to hash map for each row result
        if(cursor.moveToFirst()) {
            do {
                Item m = new Item(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3));
                items.put(m.getId(), m);
            } while(cursor.moveToNext());
        }

        // Close cursor
        cursor.close();

        return items;
    }

    //
    // ShoppingList database methods
    //
    public void addShoppingList(ShoppingList list) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ShoppingList.COLUMN_NAME, list.getName());

        long id = db.insert(ShoppingList.TABLE_NAME, null, values);
        db.close();
    }

    public ShoppingList getDefaultShoppingList() {
        // Just a quick method to get first shopping list created
        // You can extend this if you wish to manage multiple parties
        SQLiteDatabase db = this.getWritableDatabase();
        ShoppingList list = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + ShoppingList.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            list = new ShoppingList(cursor.getLong(0), cursor.getString(1));
        }

        cursor.close();
        db.close();
        return list;
    }

    //
    // ShoppingListItems database methods
    //
    public void addItemToShoppingList(Item item, ShoppingList list) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ShoppingListItems.COLUMN_ITEM_ID, item.getId());
        values.put(ShoppingListItems.COLUMN_SHOPPING_LIST_ID, list.getId());

        db.insert(ShoppingListItems.TABLE_NAME, null, values);
    }

    public ArrayList<Item> getItemsFromShoppingList(ShoppingList list) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Get all items in ShoppingListItems for a ShoppingList and create a new ArrayList to return restricted result
        HashMap<Long, Item> itemData = getAllItems();  //Get the different items possible
        ArrayList<Item> resultItems = new ArrayList<Item>();

        // Get all Items from the default shopping list
        Cursor cursor = db.rawQuery("SELECT * FROM " + ShoppingListItems.TABLE_NAME +
                        " WHERE " + ShoppingListItems.COLUMN_SHOPPING_LIST_ID + " = 1",
                null);

        // Add item to list for each row result
        if(cursor.moveToFirst()) {
            do {
                long itemId = cursor.getLong(1);
                resultItems.add(itemData.get(itemId));
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return resultItems;
    };

    public void removeItemFromShoppingList(ShoppingList list, Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        //For many to many relationship between ShoppingList and Items need to only one of a duplicate item
        db.execSQL("DELETE FROM " + ShoppingListItems.TABLE_NAME + " WHERE " +
                ShoppingListItems.COLUMN_SHOPPING_LIST_ID + " = ? AND " +
                        ShoppingListItems.COLUMN_ITEM_ID + " = ? AND " +
                        ShoppingListItems.COLUMN_ID + " IN " +
                        "(SELECT MAX(" + ShoppingListItems.COLUMN_ID + ") FROM " + ShoppingListItems.TABLE_NAME +
                        " WHERE " + ShoppingListItems.COLUMN_SHOPPING_LIST_ID + " = ? AND " +
                        ShoppingListItems.COLUMN_ITEM_ID + " = ?)",
                new String[]{String.valueOf(list.getId()), String.valueOf(item.getId()), String.valueOf(list.getId()), String.valueOf(item.getId())}
        );


        db.close();
    }
}
