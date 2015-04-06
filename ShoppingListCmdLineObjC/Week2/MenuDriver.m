//
//  MenuDriver.m
//  Week2
//
//  Created by Bhavik Maneck
//  Copyright (c) 2014 Bhavik Maneck. All rights reserved.
//

#import "MenuDriver.h"
#import "KeyboardScanner.h"

@implementation MenuDriver

-(id)init
{
    if (self = [super init])
    {
        _list = [[ShoppingList alloc] init];
        
        //Make base items that user can choose from to add to shoppping list
        Item* apple = [[Item alloc] initWithName:@"Apple" description:@"A tasty Apple" andPrice:0.25];
        Item* banana = [[Item alloc] initWithName:@"Banana" description:@"A ripe banana" andPrice:1.00];
        Item* breadLoaf = [[Item alloc] initWithName:@"Bread Loaf" description:@"A loaf of rye bread" andPrice:3.00];
        Item* toothpaste = [[Item alloc] initWithName:@"Toothpaste" description:@"Mint toothpaste" andPrice:2.25];
        Item* milk = [[Item alloc] initWithName:@"Milk" description:@"Full cream milk" andPrice:4.00];
        
        _baseItems = [[NSArray alloc] initWithObjects:apple, banana, breadLoaf, toothpaste, milk, nil];
    }
    return self;
}

-(void)displayMainMenu
{
    //Display main options for menu
    NSLog(@"Shopping List Menu:\n\n");
    NSLog(@"1 - Add Item to Shopping List\n");
    NSLog(@"2 - Remove Item from Shopping List\n");
    NSLog(@"3 - Display Shopping List and Total Price\n");
    NSLog(@"4 - Quit\n\n");
    
    NSLog(@"Please select an option from 1 to 4...\n");
}

-(void)runDriver
{
    int optionChosen = 0;
    
    // Until the user chooses option 4 to quit the program, keeping getting input and dealing with it
    while (optionChosen != 4)
    {
        [self displayMainMenu];
        
        optionChosen = [KeyboardScanner readIntFromKeyboard];
        
        if (optionChosen == 1) { //Add Item to Shopping List
            
            NSLog(@"Items to add to your Shopping List:\n\n");
            for (int i = 1; (i-1) < [self.baseItems count]; i++) {
                NSLog(@"%d - %@\n",i,[(Item *)[self.baseItems objectAtIndex:(i-1)] description]);
            }
            
            NSLog(@"Please select an option from 1 to %d...\n",(int)[self.baseItems count]);
            int itemOptionChosen = [KeyboardScanner readIntFromKeyboard];
            
            //If a valid item choice is picked remove it from shopping list
            if (itemOptionChosen >= 1 && itemOptionChosen <= [self.baseItems count])
            {
                [self.list addItem:[self.baseItems objectAtIndex:(itemOptionChosen-1)]];
                NSLog(@"Item %@ added!\n", [(Item *)[self.baseItems objectAtIndex:(itemOptionChosen-1)] name]);
            } else {
                NSLog(@"Invalid item choice!\n");
            }
            
            
        } else if (optionChosen == 2) { //Remove Item from Shopping List
            
            //Only proceed if there are items in the shopping list
            if ([[self.list shoppingItems] count] > 0)
            {
                //List out the items (only the name) in the shopping list with a position for each item
                NSLog(@"Items to remove from your Shopping List:\n\n");
                for (int i = 1; (i-1) < [[self.list shoppingItems] count]; i++) {
                    NSLog(@"%d - %@\n",i,[(Item *)[[self.list shoppingItems] objectAtIndex:(i-1)] name]);
                }
                
                //Ask to delete item based on its position
                NSLog(@"Please select an option from 1 to %d...\n",(int)[[self.list shoppingItems] count]);
                int itemOptionChosen = [KeyboardScanner readIntFromKeyboard];
            
                //If a valid item position is picked remove it from shopping list
                if (itemOptionChosen >= 1 && itemOptionChosen <= [[self.list shoppingItems] count])
                {
                    //Get the name of the Item being removed so can print out its name after its deleted
                    NSString* deletedItemName = [NSString stringWithString:[(Item *)[[self.list shoppingItems] objectAtIndex:(itemOptionChosen-1)] name]];
                    
                    //Delete the item
                    [self.list deleteItem:(itemOptionChosen-1)];
                    NSLog(@"Item %@ removed!\n", deletedItemName);
                    
                } else {
                    NSLog(@"Invalid item choice!\n");
                }
                
            } else {
                NSLog(@"No items in shopping list at the moment!\n");
            }

            
        } else if (optionChosen == 3) { //Display Shopping List and Total Price
            
            [self.list description];
            NSLog(@"Total Price: $%.2f\n",[self.list totalForAllItems]);
            
        } else if (optionChosen == 4) { //Quit
            
            NSLog(@"Thank you for using the Shopping List!");
            
        } else {  //Not a valid option chosen by user
            
            NSLog(@"Invalid item choice!\n");
        
        }
    }
}

@end
