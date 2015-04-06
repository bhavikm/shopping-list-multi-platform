/**
 * Item class for different types of shopping items
 *
 * @author (Bhavik Maneck)
 * @version (v1)
 */
public class Item {
    private String name;
    private String description;
    private double price;

    //Default Contructor
    public Item(String itemName, String itemDescription, double itemPrice) {
        name = itemName;
        description = itemDescription;
        price = itemPrice;
    }

    public String toString() {
        String description = "Item: " + getName() + " -- Description: " + getDescription() + " -- Price: $" + Double.toString(getPrice()) + "\n";
        return description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public void setName(String newName) {
        name = newName;
    }

    public void setDescription(String newDescription) {
        description = newDescription;
    }

    public void setPrice(double newPrice) {
        price = newPrice;
    }
}
