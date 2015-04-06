//
//  AddItemController.m
//  ShoppingListIphone
//
//  Created by Bhavik Maneck
//  Copyright (c) 2014 Bhavik Maneck. All rights reserved.
//

#import "AddItemController.h"

@interface AddItemController ()

@end

@implementation AddItemController


-(void)viewDidLoad
{
    [super viewDidLoad];
    
    self.itemSearchBar.delegate = self;
    
    //When the view loads we want to start the download process
    [self fetchItemDataByName:@""];
    [self downloadItemData];
}

-(void)fetchItemDataByName:(NSString*)name
{
    //The fetch request, asking for ItemData entities
    NSFetchRequest* fetchRequest = [[NSFetchRequest alloc] initWithEntityName:@"ItemData"];
    
    //If the name string isn't empty...
    if(![name isEqualToString:@""])
    {
        //...we use a Predicate to select the correct monsters based on the search name
        NSPredicate* nameSelect = [NSPredicate predicateWithFormat:@"itemName contains[cd] %@", name];
        [fetchRequest setPredicate:nameSelect];
    }
    
    //The sort descriptor is used to arrange the results based on name (alphabetically)
    NSSortDescriptor* nameSort = [NSSortDescriptor sortDescriptorWithKey:@"itemName" ascending:YES];
    [fetchRequest setSortDescriptors:@[nameSort]];
    
    //We attempt to execute the fetch request
    NSError* error;
    self.allItems = [self.managedObjectContext executeFetchRequest:fetchRequest error:&error];
    //Deal with errors
    if(self.allItems == nil)
    {
        NSLog(@"Could not fetch Item Data:\n%@", error.userInfo);
    }
    [self.tableView reloadData];
}

-(void)downloadItemData
{
    //Load last update
    double lastUpdate = [self loadLastUpdate];
    
    NSLog(@"Last udpate was: %f", lastUpdate);
    
    NSURL* url;
    if(lastUpdate == -1)
    {
        //No update, ask for all
        url = [NSURL URLWithString:@"http://elliott-wilson.com/items/allItems.php"];
        
    }
    else
    {
        //Get latest items
        url = [NSURL URLWithString:[NSString stringWithFormat:@"http://elliott-wilson.com/items/allItems.php?lastChecked=%f", lastUpdate]];
        
    }
    
    //Create a request object based on the selected URL
    NSURLRequest* request = [NSURLRequest requestWithURL:url];
    
    //Execute the request asynchronously
    [NSURLConnection sendAsynchronousRequest:request queue:[NSOperationQueue mainQueue] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error)
     {
         //This block of code will run when the request is complete
         if(error == nil)
         {
             //If there is no error we will parse the response (which will save it into CoreData)
             int numberOfItems = [self parseItemJSON:data];
             //Fetch the monsterData objects and load them into the table
             if(numberOfItems > 0)
                 [self fetchItemDataByName:self.itemSearchBar.text];
             //And save the current time to the disk (this is the last updated time)
             [self saveLastUpdate];
         }
         else
         {
             //If there are errors, output the error info
             NSLog(@"Connection Error:\n%@", error.userInfo);
         }
     }];
    
    
    [self.tableView reloadData];
}

-(void)saveLastUpdate
{
    //Work out the current date and time using the +date method from NSDate
    NSDate* currentDate = [NSDate date];
    //Work out the number of second since 1970. This is a double value which we will save in an NSNumber
    NSNumber* epochTime = [NSNumber numberWithDouble:[currentDate timeIntervalSince1970]];
    //Create a dictionary that contains this date number
    NSDictionary* dictionary = [NSDictionary dictionaryWithObject:epochTime forKey:@"lastUpdate"];
    
    //Use the NSPropertyListSerialization class to create an NSData object that we can save to the disk
    NSError* error;
    NSData* plist = [NSPropertyListSerialization dataWithPropertyList:dictionary format:NSPropertyListXMLFormat_v1_0 options:0 error:&error];
    
    //Find the documents directory
    NSString *rootPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
    //And create a path string that leads to a file named listUpdate.plist in the documents directory.
    NSString *plistPath = [rootPath stringByAppendingPathComponent:@"lastUpdate.plist"];
    //Finally, write the NSData object to that location
    [plist writeToFile:plistPath atomically:YES];
}

-(double)loadLastUpdate
{
    //Find the documents directory
    NSString *rootPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
    //And create a path string that leads to a file named listUpdate.plist in the documents directory.
    NSString *plistPath = [rootPath stringByAppendingPathComponent:@"lastUpdate.plist"];
    //Use that path to try and load that file
    NSData *plistData = [NSData dataWithContentsOfFile:plistPath];
    
    //If we get no data back...
    if(plistData == nil)
    {
        //...it means that the file couldn't be read (mostly likely because it doesn't exist)
        NSLog(@"No plistData, probably doesn't exist");
        return -1;
    }
    
    //Attempt to parse the file back into a dictionary
    NSError *error;
    id plist = [NSPropertyListSerialization propertyListWithData:plistData options:NSPropertyListImmutable format:nil error:&error];
    
    //If that fails...
    if(plist == nil)
    {
        //...it mostly like means that we opena file that isn't a Property List (or that the file is corrupted)
        NSLog(@"Error opening file:\n%@", error.userInfo);
        return -1;
    }
    
    //Finally, if the file we opened did load and is a dictionary...
    if([plist isKindOfClass:[NSDictionary class]])
    {
        //...we read the lastUpdated value out of there and return it
        NSDictionary* dict = (NSDictionary*)plist;
        NSNumber* lastUpdate = [dict valueForKey:@"lastUpdate"];
        return [lastUpdate doubleValue];
    }
    
    return -1;
}

-(int)parseItemJSON:(NSData*)data
{
    //We take the data we received online and try to parse it using the NSJSONSerialization class
    NSError* error;
    id result = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:&error];
    
    //If we get no result...
    if(result == nil)
    {
        //... it probably wasn't a JSON file we received
        NSLog(@"Error parsing JSON:\n%@", error.userInfo);
        return 0;
    }
    
    //If the result is an array...
    if([result isKindOfClass:[NSArray class]])
    {
        //...then it should be the array of monsters!
        NSArray* itemArray = (NSArray*)result;
        NSLog(@"Found %lu new items!", (unsigned long)[itemArray count]);
        
        //Loop through and process each entry
        for (NSDictionary* item in itemArray)
        {
            //Create a new MonsterData object for each entry
            ItemData* itemData = [NSEntityDescription insertNewObjectForEntityForName:@"ItemData" inManagedObjectContext:self.managedObjectContext];
            //And get the name and type from the dictionary
            itemData.itemName = [item objectForKey:@"name"];
            itemData.itemDescription = [item objectForKey:@"description"];
            itemData.price = [item objectForKey:@"price"];
        }
        
        //After we create all of these objects, we need to save the managed object context.
        NSError* error;
        if(![self.managedObjectContext save:&error])
        {
            NSLog(@"Could not save downloaded monster data:\n%@", error.userInfo);
        }
        return (int)[itemArray count];
    }
    else
    {
        //If it wasn't an array then the JSON received isn't in the right format.
        NSLog(@"Unexpected JSON format");
        return 0;
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
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // Return the number of rows in the section.
    return [self.allItems count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    ItemCell *cell = (ItemCell*)[tableView dequeueReusableCellWithIdentifier:@"ItemCell" forIndexPath:indexPath];
    
    //Select the correct monster based on the row.
    ItemData* item = [self.allItems objectAtIndex:indexPath.row];
    
    // Setup the cell labels
    cell.itemNameLabel.text = item.itemName;
    cell.itemDescriptionLabel.text = item.itemDescription;
    cell.priceLabel.text = [NSString stringWithFormat:@"$%@", item.price];

    
    return cell;
}


-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    //This method runs when the user taps on a row. The indexPath parameter tells you which section and which row.
    
    //In this case we pick the right MonsterData object.
    ItemData* selectedItemData = [self.allItems objectAtIndex:indexPath.row];
    
    Item* newItem = [NSEntityDescription insertNewObjectForEntityForName:@"Item" inManagedObjectContext:self.managedObjectContext];
    
    //We then set the name and type of this new monster to the values specified in the MonsterData object.
    newItem.itemName = selectedItemData.itemName;
    newItem.itemDescription = selectedItemData.itemDescription;
    newItem.price = selectedItemData.price;
    
    //Tell our delegate to add it
    [self.delegate addItem:newItem];
    
    //Then tell our view controller to pop the current view (which is us) and take us back to the the party list.
    [self.navigationController popViewControllerAnimated:YES];
}

-(void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText
{
    [self fetchItemDataByName:searchText];
}

@end
