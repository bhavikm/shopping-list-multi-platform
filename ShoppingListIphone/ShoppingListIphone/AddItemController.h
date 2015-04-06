//
//  AddItemController.h
//  ShoppingListIphone
//
//  Created by Bhavik Maneck
//  Copyright (c) 2014 Bhavik Maneck. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreData/CoreData.h>
#import "ItemData.h"
#import "Item.h"
#import "ItemCell.h"

@protocol AddItemDelegate <NSObject>

-(void)addItem:(Item*)item;

@end

@interface AddItemController : UITableViewController <UISearchBarDelegate>

@property (strong, nonatomic) NSManagedObjectContext* managedObjectContext;
@property (strong, nonatomic) NSArray* allItems;
@property (weak, nonatomic) id<AddItemDelegate> delegate;

@property (weak, nonatomic) IBOutlet UISearchBar *itemSearchBar;

@end
