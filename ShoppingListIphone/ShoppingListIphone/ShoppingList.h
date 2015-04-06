//
//  ShoppingList.h
//  ShoppingListIphone
//
//  Created by Bhavik Maneck on
//  Copyright (c) 2014 Bhavik Maneck. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface ShoppingList : NSManagedObject

@property (nonatomic, retain) NSSet *items;
@end

@interface ShoppingList (CoreDataGeneratedAccessors)

- (void)addItemsObject:(NSManagedObject *)value;
- (void)removeItemsObject:(NSManagedObject *)value;
- (void)addItems:(NSSet *)values;
- (void)removeItems:(NSSet *)values;

@end
