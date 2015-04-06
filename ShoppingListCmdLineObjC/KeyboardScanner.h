//
//  KeyboardScanner.h
//  TripPlanner
//
//  Created by Elliott Wilson on 25/02/14.
//  Copyright (c) 2014 Elliott Wilson. All rights reserved.
//

//This simple class contains a number of static methods
//used to read input from the keyboard (stdin)

#import <Foundation/Foundation.h>

@interface KeyboardScanner : NSObject

+(NSString*)readStringFromKeyboard;
+(int)readIntFromKeyboard;
+(double)readDoubleFromKeyboard;

@end
