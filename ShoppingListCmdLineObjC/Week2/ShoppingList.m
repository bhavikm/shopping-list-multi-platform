//
//  ShoppingList.m
//  Week2
//
//  Created by Bhavik Maneck
//  Copyright (c) 2014 Bhavik Maneck. All rights reserved.
//

#import "ShoppingList.h"
#import "Item.h"

@implementation ShoppingList

//Initialise the shopping list without any items to start
-(id)init
{
    if (self = [super init])
    {
        _shoppingItems = [[NSMutableArray alloc] init];
    }
    return self;
}

//Initialise the shopping list with an array of items
-(id)initWithItems:(NSMutableArray*)items
{
    if (self = [super init])
    {
        _shoppingItems = [[NSMutableArray alloc] initWithArray:items];
    }
    return self;
}

//Add new Item to shopping list
-(void)addItem:(id)newItem
{
    [self.shoppingItems addObject:newItem];
}

//Delete item from shopping list
-(void)deleteItem:(int)itemAtIndex
{
    [self.shoppingItems removeObjectAtIndex:itemAtIndex];
}


//Description of all the items in the shopping list
-(void)description
{
    if ([self.shoppingItems count] > 0)
    {
        NSLog(@"Shopping List Items:");
        for (Item *item in self.shoppingItems)
        {
            NSLog(@"%@",[item description]);  //Print description for each item using its own method
        }
    } else {
        NSLog(@"No items in shopping list."); 
    }
}

//Calculate the total price for all the items contained in the shopping list
-(double)totalForAllItems
{
    double total = 0.0;
    
    if ([self.shoppingItems count] > 0)
    {
        for (Item *item in self.shoppingItems)
        {
            total += [item price];
        }
    }
    
    return total;
}


@end
