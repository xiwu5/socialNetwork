/*
 * Xi Wu - 22C - Midterm 
 * This program let the user:
 * -Adding a profile in the social netwrok
 * -Deleting a profile in the social network
 * -Update a profile's information
 * -Adding/Deleting friends
 */
package socialnetwork;
import java.util.Iterator;
import java.util.Scanner;

import Midterm.AList;

public class SocialNetwork {

	private static UndirectedGraph<Profile> socialNetworkProfileGraph = new UndirectedGraph<Profile>();
	private static Scanner scanner = new Scanner(System.in); 

	public static void main(String[] args){

		while (true) {
			System.out.println("Welcome to the Social Network! ");
			System.out.println("1: Create a profile");
			System.out.println("2: Delete a profile");
			System.out.println("3: Update a profile");
			System.out.println("4. Search for other profiles");     
			System.out.println("5. Add friends");
			System.out.println("6. Remove friends");
			System.out.println("7: Display all profiles");        
			System.out.println("0: Exit \n");
			System.out.println("Enter choice:");

			// Catch the exception when input is invalid
			Integer n = getIntInput();
			if (n == null) {
				continue;
			}

			if (n == 0) {
				System.out.println("Thanks for using our social network! Have a nice day!");
				break;
			}
			else if (n == 1) {
				addProfile();
			} 
			else if (n == 2) {
				removeProfile();
			}
			else if (n == 3)
				updateProfile();
			else if (n == 4)
				searchProfile();
			else if (n == 5)
				addFriends();        
			else if (n == 6)
				removeFriends();
			else if (n == 7) {
				display();
			}
			System.out.println("");
		}       
	}

	/* This function add profile when user enter 1
	 * User can't add the same profile again
	 */
	public static void addProfile(){
		Profile newProfile = null;
		String newName = null;
		while (true) {
			// Get the profile's name from the user
			System.out.println("Enter name:");
			System.out.println("-1: Back to main menu.");
			newName = scanner.nextLine();
			newProfile = new Profile(newName);
			if (newName.equals("-1")) {
				return;
			}

			// Don't allow the user to add a profile that already exists.
			if (getProfileFromName(newName) == null) {
				// Update this profile's status
				System.out.println("Enter " + newName + "'s status (online/offline/busy...): ");
				String status = scanner.nextLine();
				newProfile.setStatus(status);

				// Update profile's image.
				System.out.println("Enter " + newName + "'s image (.jpg/.png): ");
				String image = scanner.nextLine();
				newProfile.setImage(image);
				
				// add a new profile to social network profile graph
				socialNetworkProfileGraph.addVertex(newProfile);
				System.out.println("Done adding the new profile!");
				break;	
			}
			else {
				System.out.println("You can't add the existing profile. ");
				return;
			}	
		}
	}



	/* This function delete profile based on user's input
	 * 
	 */
	public static void removeProfile() {
		while (true) {
			System.out.println("Which one do you want to delete? Please enter the name. ");
			// Display exsiting profiles in the social network.
			displayProfileNames();
			System.out.println("-1: Back to main menu.");

			// Get user's input and check validation
			String profileToDeleteName = scanner.nextLine();
			// Option to go back 
			if (profileToDeleteName.equals("-1")) {
				return;
			}

			Profile profileToDelete = getProfileFromName(profileToDeleteName);
			
			// Remove the profile, ask the user to enter again if the input doesn't exist
			if(getProfileFromName(profileToDeleteName) != null) {
				socialNetworkProfileGraph.removeVertex(profileToDelete);
				System.out.println("Done removing the profile!");
				break;
			}
			else {
				System.out.println(profileToDeleteName + " is not in our social network. Please enter a valid input.");
			}
		}
	}


	/*
	 * This function update profile
	 */
	public static void updateProfile() {
		while (true) {
			System.out.println("Which one do you want to update? Please enter the name. ");
			// Display exsiting profiles in the social network.
			displayProfileNames();
			System.out.println("-1: Back to main menu.");


			// Get the name of the profile and check invalidation 
			String profileToUpdateName = scanner.nextLine();

			// Handle the "go back" option
			if (profileToUpdateName.equals("-1")) {
				return;
			}
			// Based on the input name to find the profile.
			Profile profileToUpdate = getProfileFromName(profileToUpdateName);
			
			// Ask the user to enter again if the input is not in socialNetworkProfileList.
			if(getProfileFromName(profileToUpdateName) != null) {
				while (true) {
					System.out.println("Which information do you want to update? ");
					System.out.println("-1: Back to main menu.");
					System.out.println("1: name ");
					System.out.println("2: image");
					System.out.println("3: status");

					// Get the specific choice of the profile
					Integer choice;
					choice = getIntInput();
					if (choice == null) {
						continue;
					}
					if (choice == -1) {
						return;
					}

					if (choice == 1) { 					// Update profile's name
						System.out.println("What's the new name?");
						String name = scanner.nextLine();
						profileToUpdate.setName(name);
						break;
					} else if (choice == 2) { 			// Update profile's image
						System.out.println("What's the new image?");
						String image = scanner.nextLine();
						profileToUpdate.setImage(image);
						break;
					} else if (choice == 3) { 			// Update profile's status
						System.out.println("What's the new status?");
						String status = scanner.nextLine();
						profileToUpdate.setStatus(status);
						break;
					} else {							// Check the input's validation
						System.out.println("Out of option. Please enter again: ");
					}
				}

				// Display a message to user
				System.out.println("Done! Updated profile:");
				printProfile(profileToUpdate);
				break;
			}
			else {
				System.out.println(profileToUpdateName + " is not in our social network. Please enter a valid input.");
			}	
		}	
	}


	/*
	 * This function search profile based on user's input
	 */
	public static void searchProfile() {
		while (true) {
			System.out.println("Enter the name you want to search: ");
			System.out.println("-1: Back to main menu.");
			String searchName = scanner.nextLine();
			if (searchName.equals("-1")) {
				return;
			}
			// change user's input into Profile
			Profile profileToSearch = getProfileFromName(searchName);
			if(getProfileFromName(searchName) != null) {
				System.out.println(searchName + " is in our social network.\n");
				printProfile(profileToSearch);
				break;
			}
			else {
				System.out.println("Not Found. Please enter again.");
			}
		}
	}

	/*
	 * This function add profile's friends
	 * User can't add him/herself as a friend
	 */
	public static void addFriends() {
		// Check if there are profiles in social network
		if (socialNetworkProfileGraph.getNumberOfVertices() == 0){ 
			System.out.println("There's no user in our social network, please add some profile first!");
		}
		// Check if there is only one profile in social network
		else if (socialNetworkProfileGraph.getNumberOfVertices() == 1) {
			System.out.println("There's only you in this social network.");
		}
		else {
			while (true) {
				// Get profile's name from user
				System.out.println("\nSelect profile adding friend, please enter a name: ");
				// Display exsiting profiles in the social network.
				displayProfileNames();
				System.out.println("-1: Back to main menu.");
	
				// Get the name of the profile and check invalidation 
				String friendsName = scanner.nextLine();
				if (friendsName.equals("-1")) {
					return;
				}
				// change user's input into Profile
				Profile profileToAddFriends = getProfileFromName(friendsName);
				
				if(getProfileFromName(friendsName) != null) {
					while (true) {
						System.out.println("Who will be " + profileToAddFriends.getName() + "'s new friend? Please enter the name.");
						System.out.println("-1: Back to main menu.");
						String newFriend = scanner.nextLine();
						if (newFriend.equals(friendsName)) {
							System.out.println("You can't add yourself.");
							break;
						}
					
						if (newFriend.equals("-1")) {
							return;
						}		
						// change user's input into Profile
						Profile newFriendProfile = getProfileFromName(newFriend);
						
						if(getProfileFromName(newFriend) != null) {
							// Add a friend to this selected profile
							socialNetworkProfileGraph.addEdge(profileToAddFriends, newFriendProfile);
							System.out.println("Done to add a new friend!");
						}
						else {
							System.out.println(newFriend + " is not in the network. ");
						}
						// add more friends
						// display friends list
						displayFriendsList(profileToAddFriends);
						return;
					}	
					
				}
				else {
					System.out.println(friendsName + " is not in our social network. Please enter again.");
				}
			}
		}
	}


	/*
	 * This function remove friends based on user's input
	 */
	public static void removeFriends() {
		Profile aFriendToRemoveProfile = null;
		String aFriendToRemoveName = null;
		while (true) {
			// Get the profile's name from the user
			System.out.println("Select profile to remove friends. Please enter name: ");
			displayProfileNames();
			System.out.println("-1: Back to main menu.");

			// Get the name of the profile and check invalidation 
			aFriendToRemoveName = scanner.nextLine();
			if (aFriendToRemoveName.equals("-1")) {
				return;
			}		
			// change user's input into Profile
			aFriendToRemoveProfile = getProfileFromName(aFriendToRemoveName);
			
			// Handle the case when total number of friends in this social network is less than 1.
			if (!hasFriend(aFriendToRemoveProfile)) {
				System.out.println("No friends to delete. Please add friends first.");
				return;
			}
			
			// Ask the user to enter again if the input is not in socialNetworkProfileList.
			if (getProfileFromName(aFriendToRemoveName) != null) {
				break;
			}
			else {
				System.out.println(aFriendToRemoveName + " is not in our social network. Please enter again.");
			}
		}

		
		while (true) {
			displayFriendsList(aFriendToRemoveProfile);
			System.out.println("Who will be deleted? Please enter name.");
			System.out.println("-1: Back to main menu.");
		
			// Get the name of the friend's profile and check invalidation 
			String bFriendToRemoveName = scanner.nextLine();
		
			// Provide go back function
			if (bFriendToRemoveName.equals("-1")) {
				return;
			}
			
			if (bFriendToRemoveName.equals(aFriendToRemoveName)) {
				System.out.println("You can't delete yourself!");
				break;
			}
			Profile bFriendToRemoveProfile = getProfileFromName(bFriendToRemoveName);
			// Remove the friend.
			// Ask the user to enter again if the input is out of index in the friends list.
			if(getProfileFromName(bFriendToRemoveName) != null) {
				// Delete the friend
				socialNetworkProfileGraph.removeEdge(aFriendToRemoveProfile, bFriendToRemoveProfile);	
				System.out.println("Done removing the friend!");
				break;
			} else {
				System.out.println("You don't have him/her as your friend.");
			}
		}	
	}

	/*
	 * This function display all the profiles
	 * including each profile's specific information and their friends' name and friends' friends' name
	 */
	public static void display() {
		System.out.println("---------- Profiles -----------");
		int count = 1;
		Iterator<Profile> it = socialNetworkProfileGraph.getVerticesKeyIterator();
		while(it.hasNext()) {
			Profile profile = it.next();
			System.out.println(count + ".");
			  printProfile(profile);
			  System.out.println("\n");
			  count++;
		}
	}
	


	/*
	 * This is a helper function to check the integer input's validation
	 */
	private static Integer getIntInput() {
		Integer input = null;
		try {
			input = Integer.parseInt(scanner.nextLine());
		} catch (Exception e) {
			System.out.println("Please enter a valid number.");
		}
		return input;
	}
	
	
	// This function display all the profie's names
	// The names would show on the menu
	private static void displayProfileNames() {
		Iterator<Profile> it = socialNetworkProfileGraph.getVerticesKeyIterator();
		while(it.hasNext()) {
			  System.out.println(it.next().getName());
		}
	}
	
	// This is a helper function
	// based on selected profile's name to find the profile and return the profile
	private static Profile getProfileFromName(String name) {
		Iterator<Profile> it = socialNetworkProfileGraph.getVerticesKeyIterator();
		while (it.hasNext()) {
			Profile profile = it.next();
			  if (profile.getName().equals(name)) {
				  return profile;
			  }
		}
		return null;
	}
	
	// Display the name of friend's friend based on selected profile
	private static void printProfile(Profile profile) {
		AList<String> immediateFriendNames = new AList<String>();
		immediateFriendNames.add(profile.getName()); // Include myself
		// Update the immediate friends.
		Iterator<VertexInterface<Profile>> it = socialNetworkProfileGraph.getVertex(profile).getNeighborIterator();
        while (it.hasNext()) {
            VertexInterface<Profile> thisFiend = it.next();
            String thisFriendName = thisFiend.getLabel().getName();
            immediateFriendNames.add(thisFriendName);
        }
		
		System.out.println(profile.toString());
		// print profile's friends
		displayFriendsList(profile);
		// print friends' friends
		System.out.println("Friends' friends: ");
		QueueInterface<Profile> q = socialNetworkProfileGraph.getBreadthFirstTraversal(profile); 
		printQueue(q, immediateFriendNames);
	}
	
	// Print the name of friend's friend through breadFirstTraversal
		public static void printQueue(QueueInterface<Profile> q, AList<String> immediateFriendNames)
		{
			while (!q.isEmpty()) {
			Profile profile = q.dequeue();
				if (!immediateFriendNames.contains(profile.getName())) {
					System.out.print(profile.getName()+ " ");
				}
			}
		}
	
	
	// Display the selected profile's friends list based on input
	private static void displayFriendsList(Profile profile) {
       Iterator<VertexInterface<Profile>> it = socialNetworkProfileGraph.getVertex(profile).getNeighborIterator();
        System.out.println(profile.getName() + "'s friend(s):");
        while (it.hasNext()) {
            VertexInterface<Profile> thisFiend = it.next();
            String thisFriendName = thisFiend.getLabel().getName();
            System.out.println(thisFriendName + " ");
        }
    }
	
	// Return if the profile has a friend
	private static boolean hasFriend(Profile profile) {
		return socialNetworkProfileGraph.getVertex(profile).hasNeighbor();
	}

}

/*Output:
 * Welcome to the Social Network! 
1: Create a profile
2: Delete a profile
3: Update a profile
4. Search for other profiles
5. Add friends
6. Remove friends
7: Display all profiles
0: Exit 

Enter choice:
1
Enter name:
-1: Back to main menu.
joyce
Enter joyce's status (online/offline/busy...): 
busy
Enter joyce's image (.jpg/.png): 
joyce.jpg
Done adding the new profile!

Welcome to the Social Network! 
1: Create a profile
2: Delete a profile
3: Update a profile
4. Search for other profiles
5. Add friends
6. Remove friends
7: Display all profiles
0: Exit 

Enter choice:
1
Enter name:
-1: Back to main menu.
celine
Enter celine's status (online/offline/busy...): 
online
Enter celine's image (.jpg/.png): 
celine.jpg
Done adding the new profile!

Welcome to the Social Network! 
1: Create a profile
2: Delete a profile
3: Update a profile
4. Search for other profiles
5. Add friends
6. Remove friends
7: Display all profiles
0: Exit 

Enter choice:
1
Enter name:
-1: Back to main menu.
xi
Enter xi's status (online/offline/busy...): 
lazy
Enter xi's image (.jpg/.png): 
xi.png
Done adding the new profile!

Welcome to the Social Network! 
1: Create a profile
2: Delete a profile
3: Update a profile
4. Search for other profiles
5. Add friends
6. Remove friends
7: Display all profiles
0: Exit 

Enter choice:
1
Enter name:
-1: Back to main menu.
wu
Enter wu's status (online/offline/busy...): 
sad
Enter wu's image (.jpg/.png): 
wu.jpg
Done adding the new profile!

Welcome to the Social Network! 
1: Create a profile
2: Delete a profile
3: Update a profile
4. Search for other profiles
5. Add friends
6. Remove friends
7: Display all profiles
0: Exit 

Enter choice:
1
Enter name:
-1: Back to main menu.
a
Enter a's status (online/offline/busy...): 
a
Enter a's image (.jpg/.png): 
a.jpg
Done adding the new profile!

Welcome to the Social Network! 
1: Create a profile
2: Delete a profile
3: Update a profile
4. Search for other profiles
5. Add friends
6. Remove friends
7: Display all profiles
0: Exit 

Enter choice:
2
Which one do you want to delete? Please enter the name. 
a
wu
xi
celine
joyce
-1: Back to main menu.
a
Done removing the profile!

Welcome to the Social Network! 
1: Create a profile
2: Delete a profile
3: Update a profile
4. Search for other profiles
5. Add friends
6. Remove friends
7: Display all profiles
0: Exit 

Enter choice:
7
---------- Profiles -----------
1.
name = wu,
image = wu.jpg,
status = sad,
wu's friend(s):
Friends' friends: 


2.
name = xi,
image = xi.png,
status = lazy,
xi's friend(s):
Friends' friends: 


3.
name = celine,
image = celine.jpg,
status = online,
celine's friend(s):
Friends' friends: 


4.
name = joyce,
image = joyce.jpg,
status = busy,
joyce's friend(s):
Friends' friends: 



Welcome to the Social Network! 
1: Create a profile
2: Delete a profile
3: Update a profile
4. Search for other profiles
5. Add friends
6. Remove friends
7: Display all profiles
0: Exit 

Enter choice:
5

Select profile adding friend, please enter a name: 
wu
xi
celine
joyce
-1: Back to main menu.
wu
Who will be wu's new friend? Please enter the name.
-1: Back to main menu.
xi
Done to add a new friend!
wu's friend(s):
xi 

Welcome to the Social Network! 
1: Create a profile
2: Delete a profile
3: Update a profile
4. Search for other profiles
5. Add friends
6. Remove friends
7: Display all profiles
0: Exit 

Enter choice:
7
---------- Profiles -----------
1.
name = wu,
image = wu.jpg,
status = sad,
wu's friend(s):
xi 
Friends' friends: 


2.
name = xi,
image = xi.png,
status = lazy,
xi's friend(s):
wu 
Friends' friends: 


3.
name = celine,
image = celine.jpg,
status = online,
celine's friend(s):
Friends' friends: 


4.
name = joyce,
image = joyce.jpg,
status = busy,
joyce's friend(s):
Friends' friends: 



Welcome to the Social Network! 
1: Create a profile
2: Delete a profile
3: Update a profile
4. Search for other profiles
5. Add friends
6. Remove friends
7: Display all profiles
0: Exit 

Enter choice:
5

Select profile adding friend, please enter a name: 
wu
xi
celine
joyce
-1: Back to main menu.
wu
Who will be wu's new friend? Please enter the name.
-1: Back to main menu.
joyce
Done to add a new friend!
wu's friend(s):
xi 
joyce 

Welcome to the Social Network! 
1: Create a profile
2: Delete a profile
3: Update a profile
4. Search for other profiles
5. Add friends
6. Remove friends
7: Display all profiles
0: Exit 

Enter choice:
7
---------- Profiles -----------
1.
name = wu,
image = wu.jpg,
status = sad,
wu's friend(s):
xi 
joyce 
Friends' friends: 


2.
name = xi,
image = xi.png,
status = lazy,
xi's friend(s):
wu 
Friends' friends: 
joyce 

3.
name = celine,
image = celine.jpg,
status = online,
celine's friend(s):
Friends' friends: 


4.
name = joyce,
image = joyce.jpg,
status = busy,
joyce's friend(s):
wu 
Friends' friends: 
xi 


Welcome to the Social Network! 
1: Create a profile
2: Delete a profile
3: Update a profile
4. Search for other profiles
5. Add friends
6. Remove friends
7: Display all profiles
0: Exit 

Enter choice:
4
Enter the name you want to search: 
-1: Back to main menu.
bob
Not Found. Please enter again.
Enter the name you want to search: 
-1: Back to main menu.
xi
xi is in our social network.

name = xi,
image = xi.png,
status = lazy,
xi's friend(s):
wu 
Friends' friends: 
joyce 
Welcome to the Social Network! 
1: Create a profile
2: Delete a profile
3: Update a profile
4. Search for other profiles
5. Add friends
6. Remove friends
7: Display all profiles
0: Exit 

Enter choice:
3
Which one do you want to update? Please enter the name. 
wu
xi
celine
joyce
-1: Back to main menu.
wu
Which information do you want to update? 
-1: Back to main menu.
1: name 
2: image
3: status
1
What's the new name?
wu5
Done! Updated profile:
name = wu5,
image = wu.jpg,
status = sad,
wu5's friend(s):
xi 
joyce 
Friends' friends: 

Welcome to the Social Network! 
1: Create a profile
2: Delete a profile
3: Update a profile
4. Search for other profiles
5. Add friends
6. Remove friends
7: Display all profiles
0: Exit 

Enter choice:
6
Select profile to remove friends. Please enter name: 
wu5
xi
celine
joyce
-1: Back to main menu.
-1

Welcome to the Social Network! 
1: Create a profile
2: Delete a profile
3: Update a profile
4. Search for other profiles
5. Add friends
6. Remove friends
7: Display all profiles
0: Exit 

Enter choice:
5

Select profile adding friend, please enter a name: 
wu5
xi
celine
joyce
-1: Back to main menu.
tao
tao is not in our social network. Please enter again.

Select profile adding friend, please enter a name: 
wu5
xi
celine
joyce
-1: Back to main menu.
1
1 is not in our social network. Please enter again.

Select profile adding friend, please enter a name: 
wu5
xi
celine
joyce
-1: Back to main menu.
-1

Welcome to the Social Network! 
1: Create a profile
2: Delete a profile
3: Update a profile
4. Search for other profiles
5. Add friends
6. Remove friends
7: Display all profiles
0: Exit 

Enter choice:
1
Enter name:
-1: Back to main menu.
tao
Enter tao's status (online/offline/busy...): 
busy
Enter tao's image (.jpg/.png): 
.jpg
Done adding the new profile!

Welcome to the Social Network! 
1: Create a profile
2: Delete a profile
3: Update a profile
4. Search for other profiles
5. Add friends
6. Remove friends
7: Display all profiles
0: Exit 

Enter choice:
5

Select profile adding friend, please enter a name: 
tao
wu5
xi
celine
joyce
-1: Back to main menu.
tao
Who will be tao's new friend? Please enter the name.
-1: Back to main menu.
xi
Done to add a new friend!
tao's friend(s):
xi 

Welcome to the Social Network! 
1: Create a profile
2: Delete a profile
3: Update a profile
4. Search for other profiles
5. Add friends
6. Remove friends
7: Display all profiles
0: Exit 

Enter choice:
7
---------- Profiles -----------
1.
name = tao,
image = .jpg,
status = busy,
tao's friend(s):
xi 
Friends' friends: 
wu5 joyce 

2.
name = wu5,
image = wu.jpg,
status = sad,
wu5's friend(s):
xi 
joyce 
Friends' friends: 
tao 

3.
name = xi,
image = xi.png,
status = lazy,
xi's friend(s):
wu5 
tao 
Friends' friends: 
joyce 

4.
name = celine,
image = celine.jpg,
status = online,
celine's friend(s):
Friends' friends: 


5.
name = joyce,
image = joyce.jpg,
status = busy,
joyce's friend(s):
wu5 
Friends' friends: 
xi tao 


Welcome to the Social Network! 
1: Create a profile
2: Delete a profile
3: Update a profile
4. Search for other profiles
5. Add friends
6. Remove friends
7: Display all profiles
0: Exit 

Enter choice:
2
Which one do you want to delete? Please enter the name. 
tao
wu5
xi
celine
joyce
-1: Back to main menu.
-1

Welcome to the Social Network! 
1: Create a profile
2: Delete a profile
3: Update a profile
4. Search for other profiles
5. Add friends
6. Remove friends
7: Display all profiles
0: Exit 

Enter choice:
5

Select profile adding friend, please enter a name: 
tao
wu5
xi
celine
joyce
-1: Back to main menu.
tao
Who will be tao's new friend? Please enter the name.
-1: Back to main menu.
wu
wu is not in the network. 
tao's friend(s):
xi 

Welcome to the Social Network! 
1: Create a profile
2: Delete a profile
3: Update a profile
4. Search for other profiles
5. Add friends
6. Remove friends
7: Display all profiles
0: Exit 

Enter choice:
5

Select profile adding friend, please enter a name: 
tao
wu5
xi
celine
joyce
-1: Back to main menu.
tao
Who will be tao's new friend? Please enter the name.
-1: Back to main menu.
wu5
Done to add a new friend!
tao's friend(s):
xi 
wu5 

Welcome to the Social Network! 
1: Create a profile
2: Delete a profile
3: Update a profile
4. Search for other profiles
5. Add friends
6. Remove friends
7: Display all profiles
0: Exit 

Enter choice:
2
Which one do you want to delete? Please enter the name. 
tao
wu5
xi
celine
joyce
-1: Back to main menu.
wu5
Done removing the profile!

Welcome to the Social Network! 
1: Create a profile
2: Delete a profile
3: Update a profile
4. Search for other profiles
5. Add friends
6. Remove friends
7: Display all profiles
0: Exit 

Enter choice:
7
---------- Profiles -----------
1.
name = tao,
image = .jpg,
status = busy,
tao's friend(s):
xi 
Friends' friends: 


2.
name = xi,
image = xi.png,
status = lazy,
xi's friend(s):
tao 
Friends' friends: 


3.
name = celine,
image = celine.jpg,
status = online,
celine's friend(s):
Friends' friends: 


4.
name = joyce,
image = joyce.jpg,
status = busy,
joyce's friend(s):
Friends' friends: 



Welcome to the Social Network! 
1: Create a profile
2: Delete a profile
3: Update a profile
4. Search for other profiles
5. Add friends
6. Remove friends
7: Display all profiles
0: Exit 

Enter choice:
0
Thanks for using our social network! Have a nice day!
*/
