import java.util.*;

/**
 * Shopping List class holds list of Items
 *
 * @author (Bhavik Maneck)
 * @version (v1)
 */
public class ShoppingList {
    private ArrayList<Item> items;

    /**
     * Constructor for ShoppingList
     */
    public ShoppingList() {
        items = new ArrayList<Item>();
    }

    public String description() {
        String itemsString = "Items currently in your shopping list:\n\n";

        if (items.size() > 0) {
            for (Item item : items) {
                itemsString += item.toString();
            }

        } else {
            itemsString += "No items in shopping list\n";
        }

        return itemsString;
    }

    public double totalPrice() {
        double totalPrice = 0.0;
        for (Item item : items) {
            totalPrice += item.getPrice();
        }

        return totalPrice;
    }

    public void addItem(Item newItem) {
        items.add(newItem);
    }

    public void deleteItem(int itemIndex) {
        items.remove(itemIndex);
    }

    public ArrayList<Item> getShoppingListItems() {
        return items;
    }
}