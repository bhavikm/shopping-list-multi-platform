//
//  Item.m
//  Week2
//
//  Created by Bhavik Maneck
//  Copyright (c) 2014 Bhavik Maneck. All rights reserved.
//

#import "Item.h"

@implementation Item

-(id)initWithName:(NSString*)itemName description:(NSString*)itemDesc andPrice:(double)itemPrice
{
    if (self = [super init])
    {
        _name = itemName;
        _itemDescription = itemDesc;
        _price = itemPrice;
        
    }
    return self;
}

//Outputs description of Item object
-(NSString*)description
{
    return [NSString stringWithFormat:@"Item name: %@\nItem Description: %@\nItem Price: $%.2f\n", self.name, self.itemDescription, self.price];
}

@end