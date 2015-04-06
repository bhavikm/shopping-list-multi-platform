//
//  main.m
//  Week2
//
//  Created by Bhavik Maneck on 10/03/14.
//  Copyright (c) 2014 Bhavik Maneck. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "MenuDriver.h"

int main(int argc, const char * argv[])
{

    @autoreleasepool {
        
        //Create and run the driver which will hand everything else
        MenuDriver *menu = [[MenuDriver alloc] init];
        [menu runDriver];
    }
    
    return 0;
}