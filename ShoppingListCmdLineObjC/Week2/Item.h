//
//  Item.h
//  Week2
//
//  Created by Bhavik Maneck
//  Copyright (c) 2014 Bhavik Maneck. All rights reserved.
//

#import <Foundation/Foundation.h>

// Shopping List Item
@interface Item : NSObject

@property NSString* name;
@property NSString* itemDescription;
@property (nonatomic) double price;

-(id)initWithName:(NSString*)name description:(NSString*)itemDesc andPrice:(double)price;
-(NSString*)description;

@end
