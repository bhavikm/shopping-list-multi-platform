//
//  ShoppingList.h
//  Week2
//
//  Created by Bhavik Maneck
//  Copyright (c) 2014 Bhavik Maneck. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ShoppingList : NSObject

@property NSMutableArray* shoppingItems;

//Initialisation methods
-(id)init;
-(id)initWithItems:(NSMutableArray*)items;

//Action methods
-(void)addItem:(id)newItem;
-(void)deleteItem:(int)itemAtIndex;

//Information methods
-(void)description;
-(double)totalForAllItems;

@end
