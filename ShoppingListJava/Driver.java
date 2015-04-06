import java.util.*;

/**
 * Driver class is the main controller class that runs the program and controls the logic of reading input
 * from the user, displaying appropiate menu and text, creating and storing objects. This is done
 * through methods of appropiate classes for each function.
 *
 * @author (Bhavik Maneck)
 * @version (v1)
 */
public class Driver {
    private ShoppingList shoppingList;
    private ArrayList<Item> items;
    private Menu itemsMenu;

    /**
     * Constructor for objects of class Driver
     */
    public Driver() {
        shoppingList = new ShoppingList();

        //Create set of basic items that can be added to shopping list
        items = new ArrayList<Item>();

        Item apple = new Item("Apple", "A tasty Apple", 0.25);
        Item banana = new Item("Banana", "A ripe Banana", 0.5);
        Item breadLoaf = new Item("Bread Loaf", "A rye bread loaf", 1.25);
        Item toothpaste = new Item("Toothpaste", "Mint flavoured toothpaste", 2.40);
        Item milk = new Item("Milk", "Full cream milk", 3.00);

        items.add(apple);
        items.add(banana);
        items.add(breadLoaf);
        items.add(toothpaste);
        items.add(milk);

        //Setup the items menu
        String menuTitle = "\nAdd shopping iist items from:\n";
        ArrayList<String> menuOptions = new ArrayList<String>();
        for (Item item : items) {
            menuOptions.add(item.toString());
        }
        menuOptions.add("Back\n");
        itemsMenu = new Menu(menuTitle, menuOptions);
    }

    /*
     *  Run the program on the command line with command: java Driver
     */
    public static void main(String[] args) {
        Driver shoppingListDriver = new Driver();
        shoppingListDriver.runDriver();
    }

    /*
     * Main method for continuously displaying the menu, reading option from the user and calling program option methods
     */
    public void runDriver() {
        boolean continueProgram = true;
        int optionChosen = 0;

        String menuTitle = "\nShopping List:\n";
        ArrayList<String> menuOptions = new ArrayList<String>();
        menuOptions.add("Add Item to Shopping List\n");
        menuOptions.add("Remove Item from Shopping List\n");
        menuOptions.add("Display Shopping List and Total Price\n");
        menuOptions.add("Quit\n");

        try {
            Menu driverMenu = new Menu(menuTitle, menuOptions);
            UserInput userInput = new UserInput();
            while (continueProgram) {
                userInput.clearScreen();
                driverMenu.displayMenu();

                //Read the integer the user inputted, 0 is returned by readIntegerFromUser for error
                optionChosen = userInput.readIntegerFromUser(4, "Please choose an option between 1 and 4: ");
                userInput.clearScreen();

                switch (optionChosen) {
                    // Add item
                    case 1:
                        //Display the items menu
                        itemsMenu.displayMenu();

                        //Read the integer the user inputted, 0 is returned by readIntegerFromUser for error
                        int shoppingItemChosen = userInput.readIntegerFromUser(itemsMenu.getNumberOfOptions(), "Please choose an option between 1 and " + itemsMenu.getNumberOfOptions() + ": ");

                        if (shoppingItemChosen > 0 && shoppingItemChosen < itemsMenu.getNumberOfOptions()) {
                            Item selectedItem = items.get(shoppingItemChosen - 1);
                            shoppingList.addItem(selectedItem);
                            System.out.print("\nAdded Item to Shopping List.\n");
                            userInput.pressEnterToContinue();
                        }

                        break;

                    // Delete item
                    case 2:
                        //Display menu of items in shopping list
                        ArrayList<Item> currentList = shoppingList.getShoppingListItems();

                        if (currentList.size() > 0) {
                            //Setup the shopping list items menu
                            String deleteMenuTitle = "\nRemove a shopping iist item:\n";
                            ArrayList<String> deleteMenuOptions = new ArrayList<String>();
                            for (Item item : currentList) {
                                deleteMenuOptions.add(item.getName() + "\n");  //Just add the names of the items
                            }
                            deleteMenuOptions.add("Back\n");
                            Menu currentItemsMenu = new Menu(deleteMenuTitle, deleteMenuOptions);

                            //Display the list of current items
                            currentItemsMenu.displayMenu();

                            //Read the integer the user inputted, 0 is returned by readIntegerFromUser for error
                            int deleteItemChosen = userInput.readIntegerFromUser(currentItemsMenu.getNumberOfOptions(), "Please choose an option between 1 and " + currentItemsMenu.getNumberOfOptions() + ": ");

                            if (deleteItemChosen > 0 && deleteItemChosen < currentItemsMenu.getNumberOfOptions()) {
                                Item selectedItem = currentList.get(deleteItemChosen - 1);
                                shoppingList.deleteItem(deleteItemChosen - 1);
                                System.out.print("\nItem deleted from Shopping List.\n");
                                userInput.pressEnterToContinue();
                            }
                        } else {
                            System.out.print("\nNo items in shopping list.\n");
                            userInput.pressEnterToContinue();
                        }

                        break;

                    // Display Shopping List items and total price
                    case 3:
                        System.out.println(shoppingList.description());

                        System.out.println("Total Price: $" + Double.toString(shoppingList.totalPrice()) + "\n");
                        userInput.pressEnterToContinue();
                        break;

                    // Exit the program
                    case 4:
                        continueProgram = false;
                        break;

                    // Out of bounds option
                    default:
                        System.out.print("\nError! That option does not exist!\n");
                        break;
                }
            }
        } catch (IllegalStateException e) {
            System.out.print("\nError creating menu! " + e.getMessage() + "\n");
        }
    }

}