//
//  MenuDriver.h
//  Week2
//
//  Created by Bhavik Maneck
//  Copyright (c) 2014 Bhavik Maneck. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ShoppingList.h"
#import "Item.h"

@interface MenuDriver : NSObject

@property NSArray* baseItems;
@property ShoppingList* list;

-(id)init;

-(void)displayMainMenu;

-(void)runDriver;

@end
