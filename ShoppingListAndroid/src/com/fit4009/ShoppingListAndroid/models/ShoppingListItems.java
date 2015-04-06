package com.fit4009.ShoppingListAndroid.models;

/**
 * Created by Bhavik Maneck
 */
public class ShoppingListItems {
    // Database constants
    public static final String TABLE_NAME = "shopping_list_items";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ITEM_ID = "item_id";
    public static final String COLUMN_SHOPPING_LIST_ID = "shopping_list_id";

    // PRIMARY KEY has own declaration for multiple columns
    public static final String CREATE_STATEMENT =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    COLUMN_ITEM_ID + " INTEGER NOT NULL, " +
                    COLUMN_SHOPPING_LIST_ID + " INTEGER NOT NULL " +
                    ")";

    // Properties
    private long id;
    private Item item;
    private ShoppingList list;

    public ShoppingListItems(long id, Item item, ShoppingList list)  {
        this.id = id;
        this.item = item;
        this.list = list;
    }

    public ShoppingListItems(Item item, ShoppingList list)  {
        this.item = item;
        this.list = list;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public ShoppingList getShoppingList() {
        return list;
    }

    public void setShoppingList(ShoppingList list) {
        this.list = list;
    }
}

