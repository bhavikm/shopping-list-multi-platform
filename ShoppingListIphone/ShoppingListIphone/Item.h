//
//  Item.h
//  ShoppingListIphone
//
//  Created by Bhavik Maneck
//  Copyright (c) 2014 Bhavik Maneck. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class ShoppingList;

@interface Item : NSManagedObject

@property (nonatomic, retain) NSString * itemName;
@property (nonatomic, retain) NSString * itemDescription;
@property (nonatomic, retain) NSNumber * price;
@property (nonatomic, retain) ShoppingList *list;

@end
