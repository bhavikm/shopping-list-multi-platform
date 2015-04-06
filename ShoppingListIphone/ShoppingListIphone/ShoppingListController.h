//
//  ShoppingListController.h
//  ShoppingListIphone
//
//  Created by Bhavik Maneck
//  Copyright (c) 2014 Bhavik Maneck. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreData/CoreData.h>
#import "ShoppingList.h"
#import "Item.h"
#import "ItemCell.h"
#import "TotalCell.h"
#import "AddItemController.h"

@interface ShoppingListController : UITableViewController <AddItemDelegate>

- (NSNumber*)totalPriceCurrentItems;

@property (strong, nonatomic) NSManagedObjectContext* managedObjectContext;
@property (strong, nonatomic) ShoppingList* currentShoppingList;
@property (strong, nonatomic) NSArray* shoppingListItems;

@end
