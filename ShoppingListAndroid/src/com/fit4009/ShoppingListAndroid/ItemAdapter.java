package com.fit4009.ShoppingListAndroid;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.fit4009.ShoppingListAndroid.models.Item;

import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Item> items;

    public ItemAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Item getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // Check if the view has been created for the row otherwise inflate it
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_shopping_item, null); // List layout with item layouts
        }

        // Get the TextViews
        TextView itemNameTextView = (TextView)view.findViewById(R.id.itemNameTextView);
        TextView descriptionTextView = (TextView)view.findViewById(R.id.descriptionTextView);
        TextView priceTextView = (TextView)view.findViewById(R.id.priceTextView);

        // Assign values to the TextViews using the Item object
        itemNameTextView.setText(items.get(i).getName());
        descriptionTextView.setText(items.get(i).getDescription());
        priceTextView.setText("$"+Double.toString(items.get(i).getPrice()));

        //Set item name in each list view row to be blue
        itemNameTextView.setTextColor(Color.parseColor("#0000FF"));

        //Set price in each list view row to be green
        priceTextView.setTextColor(Color.parseColor("#00FF00"));

        // Return the completed View of the row being processed
        return view;
    }

}
