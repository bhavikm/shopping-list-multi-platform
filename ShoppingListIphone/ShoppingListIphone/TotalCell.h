//
//  TotalCell.h
//  ShoppingListIphone
//
//  Created by Bhavik Maneck
//  Copyright (c) 2014 Bhavik Maneck. All rights reserved.
//

#import <UIKit/UIKit.h>


//For displaying information about totals of items in shopping list (price and total item count)
@interface TotalCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *totalItemsLabel;
@property (weak, nonatomic) IBOutlet UILabel *totalPriceLabel;

@end
