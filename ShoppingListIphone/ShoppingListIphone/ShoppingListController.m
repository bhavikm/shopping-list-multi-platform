//
//  ShoppingListController.m
//  ShoppingListIphone
//
//  Created by Bhavik Maneck
//  Copyright (c) 2014 Bhavik Maneck. All rights reserved.
//

#import "ShoppingListController.h"

@implementation ShoppingListController

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    //Here we create a fetch request. It basically says I want to fetch all of the Party objects
    NSFetchRequest* fetchRequest = [[NSFetchRequest alloc] initWithEntityName:@"ShoppingList"];
    
    //We run the fetch request on the managed object context
    NSError* error;
    NSArray* result = [self.managedObjectContext executeFetchRequest:fetchRequest error:&error];
    if(result == nil)
    {
        //If not result array at all, error time
        NSLog(@"Could not fetch Party:\n%@", error.userInfo);
    }
    else if ([result count] == 0)
    {
        //If we get an array but there is no parties in it
        //Create a new one
        self.currentShoppingList = [NSEntityDescription insertNewObjectForEntityForName:@"ShoppingList" inManagedObjectContext:self.managedObjectContext];
    }
    else
    {
        //Otherwise just grab the first party and use that
        self.currentShoppingList = [result firstObject];
        self.shoppingListItems = [self.currentShoppingList.items allObjects];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return 2;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    switch(section)
    {
        case 0:
            return [self.shoppingListItems count];
        case 1:
            return 1;
    }
    return 0;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    //This method wants us to return a Cell for a given section and row (this is know as an indexPath)
    if(indexPath.section == 0)
    {
        //If it's the first section (section 0) then we need to create a Monster Cell
        ItemCell *cell = (ItemCell*)[tableView dequeueReusableCellWithIdentifier:@"ItemCell" forIndexPath:indexPath];
        
        //Choose the right monster (the row of the index path)
        Item* item = [self.shoppingListItems objectAtIndex:indexPath.row];
        
        // Configure the cell...
        cell.itemNameLabel.text = item.itemName;
        cell.itemDescriptionLabel.text = item.itemDescription;
        cell.priceLabel.text = [NSString stringWithFormat:@"$%@", item.price];
        
        return cell;
    }
    else
    {
        //If it's the second section we'll just get a basic cell
        TotalCell *cell = (TotalCell*)[tableView dequeueReusableCellWithIdentifier:@"TotalCell" forIndexPath:indexPath];
        
        //Set the text to display the total monsters
        cell.totalItemsLabel.text = [NSString stringWithFormat:@"Total Items: %d", [self.shoppingListItems count]];
        cell.totalPriceLabel.text = [NSString stringWithFormat:@"$%.2f", [[self totalPriceCurrentItems] floatValue]];
        
        return cell;
    }
    //return nil;
}


- (NSNumber*)totalPriceCurrentItems
{
    NSNumber *total = [NSNumber numberWithFloat:0.0];
    for (Item *itemInList in self.shoppingListItems)
    {
        total = [NSNumber numberWithFloat:([total floatValue] + [[itemInList price] floatValue])];
    }
    return total;
}

- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    //This method asks us if a certain row is able to be edited.
    //Editing a row can involve moving it or deleting it. In this case we say that all rows in section 0 can be editted.
    if(indexPath.section == 0)
        return YES;
    
    return NO;
}

- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    //This method defineds what happens when a row is editted. In or case what happens when a row is deleted.
    if (editingStyle == UITableViewCellEditingStyleDelete)
    {
        //If a row is deleted we removed the monster from the party
        Item* itemToRemove = [self.shoppingListItems objectAtIndex:indexPath.row];
        [self.currentShoppingList removeItemsObject:itemToRemove];
        
        //Refresh the partyMonsterList so that it shows a correct reflection of the currentParty
        self.shoppingListItems = [self.currentShoppingList.items allObjects];
        
        //Delete the row from the table
        [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
        
        //And reload the total monster row so that information is correct.
        [self.tableView reloadRowsAtIndexPaths:@[[NSIndexPath indexPathForRow:0 inSection:1]] withRowAnimation:UITableViewRowAnimationFade];
        
        //Save the changes made to the manage object context
        NSError* error;
        if(![self.managedObjectContext save:&error])
        {
            NSLog(@"Could not save deletion:\n%@", error.userInfo);
        }
    }
}

-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    //This method allows us to get ready for a Segue. It runs right before the segue fires.
    
    //If this case we use the segue identifier to work out which segue it is.
    if([segue.identifier isEqualToString:@"AddItemSegue"])
    {
        AddItemController* controller = segue.destinationViewController;

        controller.managedObjectContext = self.managedObjectContext;
        controller.delegate = self;
    }
}

-(void)addItem:(Item *)item
{
    //This method is defined in the AddMonsterDelegate protocol and it is how the AddMonsterController informs us which monster to add to the party.
    [self.currentShoppingList addItemsObject:item];
    //Once we add the monster to the party we should update the partyMonsterList and reload the table to make sure it's displayed.
    self.shoppingListItems = [self.currentShoppingList.items allObjects];
    [self.tableView reloadData];
    
    //We should save the Managed Object Context
    NSError* error;
    if(![self.managedObjectContext save:&error])
    {
        NSLog(@"Could not save item insertion:\n%@", error.userInfo);
    }
}


@end
