import java.util.Scanner;

//A ShoppingCartSystem class with the main method to check the functionality of our program
public class ShoppingCartSystem {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in); //Create a scanner object to take input from the user

		Cart store = new Cart(); // Create a new cart object to hold items

		double lastTaxRate = -1; // Initialize a variable to store the tax rate
		double totalWithoutTax = 0.0; 
		double taxAmount = 0.0;
		double totalWithTax = 0.0;

		System.out.println("Welcome to the Shopping Cart System!");

		// Infinite loop for displaying the menu and accepting user input until they choose to exit
		while (true) {
			System.out.println("\nChoose an option:");
			System.out.println("1. Add an item to the cart");
			System.out.println("2. View all items in the cart");
			System.out.println("3. Remove an item from the cart");
			System.out.println("4. Change the quantity of an item in the cart");
			System.out.println("5. View total price");
			System.out.println("6. Proceed to payment");
			System.out.println("7. Exit");

			// Validate the user input and ensure a positive integer choice is made
			int choice = validatePositiveIntInput(scanner, "Enter your choice: ");

			// Switch case to handle different operations based on user choice
			switch (choice) {
			case 1: // Add an item to the cart

				String choice2;//A choice for the user to keep adding items or exit this section
				do {
					// Get item details from user input
					String itemName = validateStringInput(scanner, "\nEnter item name: ");
					double itemPrice = validatePositiveDoubleInput(scanner, "Enter item price: ");
					int itemQuantity = validatePositiveIntInput(scanner, "Enter item quantity: ");

					// Add the item to the cart (creating an object of type Item)
					store.addItem(new Item(itemName, itemPrice, itemQuantity));
					System.out.println("\n" + itemQuantity + " " + itemName + " added successfully!");

					// Ask the user if they want to add another item
					choice2 = validateStringInput(scanner, "\nDo you want to add another item? (Enter Y for yes, any other key for no): ");
				} while (choice2.equalsIgnoreCase("Y")); // Repeat if 'Y' is entered
				System.out.println("\nYour items are ready for payment.");
				break;

			case 2: // View all items in the cart
				store.viewItemsInTheCart();
				break;

			case 3: // Remove an item from the cart
				if (store.isCartEmpty()) break; //Check whether the cart is empty or not
				store.viewItemsInTheCart();//Print the items in the cart so that the user choose the item they want to remove
				int itemToBeRemoved = validatePositiveIntInput(scanner, "Choose item to remove (1 - " + store.getItemCount() + "): ");
				store.removeItem(itemToBeRemoved);
				break;

			case 4: // Change the quantity of an item in the cart
				if (store.isCartEmpty()) break;//Check whether the cart is empty or not
				store.viewItemsInTheCart();//Print the items in the cart so that the user choose which item to change the quantity
				int itemToBeChanged = validatePositiveIntInput(scanner, "Choose item to change quantity (1 - " + store.getItemCount() + "): ");
				int quantityToChangeTo = validatePositiveIntInput(scanner, "Enter the new quantity: ");
				store.changeQuantity(itemToBeChanged, quantityToChangeTo);
				break;

			case 5: // View total price with and without tax
				if (store.isCartEmpty())break;//Check whether the cart is empty or not

				//update the lastTaxRate variable which was previously initialized with -1
				lastTaxRate = validatePositiveDoubleInput(scanner, "Enter tax rate (in %): ");  

				// Calculate and store totals
				totalWithoutTax = store.calculateTotal();
				taxAmount = TaxCalculator.calculateTaxAmount(totalWithoutTax, lastTaxRate);
				totalWithTax = TaxCalculator.calculateTotal(totalWithoutTax, lastTaxRate);

				// Display calculated amounts with consistent rounding to 2 decimal places
				System.out.println("\n--- Total Price Details ---");
				System.out.printf("Total price without tax: $%.2f%n", totalWithoutTax);
				System.out.printf("Tax amount: $%.2f%n", taxAmount);
				System.out.printf("Total price with tax: $%.2f%n", totalWithTax);
				break;

			case 6: // Checkout process
				if (store.isCartEmpty()) break;//Check whether the cart is empty or not

				// Prompt the user to enter the tax rate if they haven't done already in choice 5
				if (lastTaxRate == -1) {
					lastTaxRate = validatePositiveDoubleInput(scanner, "Enter tax rate (in %): ");
					totalWithoutTax = store.calculateTotal();
					taxAmount = TaxCalculator.calculateTaxAmount(totalWithoutTax, lastTaxRate);
					totalWithTax = TaxCalculator.calculateTotal(totalWithoutTax, lastTaxRate);
				}

				// Display checkout summary using previously calculated totals
				System.out.println("\n--- Checkout Summary ---");
				System.out.printf("Total price without tax: $%.2f%n", totalWithoutTax);
				System.out.printf("Tax amount: $%.2f%n", taxAmount);
				System.out.printf("Total price with tax: $%.2f%n", totalWithTax);

				// Call the checkout method
				store.checkOut(scanner, totalWithTax);
				System.out.println("\nThank you for using the Shopping Cart System. Goodbye!");
				scanner.close();
				return; // Exit the program

			case 7: // Exit the program
				System.out.print("Are you sure you want to exit? (Y/N): ");
				String confirmExit = scanner.nextLine().trim();
				if (confirmExit.equalsIgnoreCase("Y")) {
					System.out.println("Thank you for using the Shopping Cart System. Goodbye!");
					scanner.close();
					return; // Exit the program
				}
				break;

			default:
				System.out.println("Invalid choice. Please try again.");
			}
		}
	}

	// Method to validate string input from the user
	// Ensures that the user provides a non-empty string
	public static String validateStringInput(Scanner scanner, String prompt) {
		String input;
		while (true) { // Loop until valid input is entered
			System.out.print(prompt); // Display the prompt to the user
			input = scanner.nextLine().trim(); // Read and trim the user's input to remove extra spaces
			if (!input.isEmpty()) // Check if the input is not empty
				return input; // Return valid input
			System.out.println("Invalid input. Please enter a valid string."); // Inform the user of invalid input
		}
	}

	//Method to validate positive integer input from the user
	//Ensures that the user provides a valid positive integer
	public static int validatePositiveIntInput(Scanner scanner, String prompt) {
		while (true) { // Loop until a valid positive integer is entered
			System.out.print(prompt); // Display the prompt to the user
			if (scanner.hasNextInt()) { // Check if the input is a valid integer
				int input = scanner.nextInt(); // Read the integer input
				scanner.nextLine(); // Consume the newline character to avoid issues with further inputs
				if (input > 0) // Check if the integer is positive
					return input; // Return valid positive integer
			} else
				scanner.nextLine(); // Consume the invalid input to prevent an infinite loop
			System.out.println("Invalid input. Please enter a positive integer."); // Inform the user of invalid input
		}
	}


	//Method to validate positive double input from the user
	//Ensures that the user provides a valid positive double value
	public static double validatePositiveDoubleInput(Scanner scanner, String prompt) {
		while (true) { // Loop until a valid positive double is entered
			System.out.print(prompt); // Display the prompt to the user
			if (scanner.hasNextDouble()) { // Check if the input is a valid double
				double input = scanner.nextDouble(); // Read the double input
				scanner.nextLine(); // Consume the newline character to avoid issues with further inputs
				if (input > 0)// Check if the double is positive
					return input; // Return valid positive double
			} else
				scanner.nextLine(); // Consume the invalid input to prevent an infinite loop
			System.out.println("Invalid input. Please enter a positive number."); // Inform the user of invalid input
		}
	}
}


//Class to represent an item in the shopping cart
class Item {
	private String itemName; 
	private double itemPrice; 
	private int itemQuantity; 

	// Constructor to initialize an item
	public Item(String itemName, double itemPrice, int itemQuantity) {
		this.itemName = itemName; // Set the item's name
		this.itemPrice = itemPrice; // Set the item's price
		this.itemQuantity = itemQuantity; // Set the item's quantity
	}

	// Getter methods
	// Returns the name of the item
	public String getItemName() {
		return itemName;
	}

	// Returns the price of the item
	public double getItemPrice() {
		return itemPrice;
	}

	// Returns the quantity of the item
	public int getItemQuantity() {
		return itemQuantity;
	}

	// Setter method for item quantity
	// Updates the quantity of the item in the cart
	public void setItemQuantity(int itemQuantity) {
		this.itemQuantity = itemQuantity; // Set the new quantity for the item
	}
}

// Class to represent the shopping cart that holds the items
class Cart {
	private Item[] items; // Array to store items in the cart
	private int itemCount; // The current number of items in the cart

	// Constructor initializes the cart with a size of 10 items
	public Cart() {
		items = new Item[100];
		itemCount = 0;
	}

	// Method to get the current number of items in the cart
	public int getItemCount() {
		return itemCount;
	}

	// Method to resize the array if it is full
	private void resizeArray() {
		Item[] newItems = new Item[items.length + 100];
		System.arraycopy(items, 0, newItems, 0, items.length); // Copy existing items to new array
		items = newItems; // Assign the new array to the items reference
	}

	// Method to add an item to the cart
	public void addItem(Item item) {
		if (itemCount == items.length) {
			resizeArray(); // Resize the array if the cart is full
		}
		items[itemCount++] = item; // Add item and increment item count
	}
	
	//Method to check if the cart is empty
	//Returns true if the cart is empty and prints a message, otherwise returns false
	public boolean isCartEmpty() {
		// Check if the item count is zero, meaning the cart is empty
		if (itemCount == 0) {
			System.out.println("\nYour cart is empty!"); // Notify the user that the cart is empty
			return true; // Return true if the cart is empty
		}
		return false; // Return false if the cart has items
	}

	// Method to view all items in the cart
	public void viewItemsInTheCart() {
		if (isCartEmpty()) return;// If the cart is empty, exit the method

		System.out.println("\n--- Items in the Cart ---");
		for (int i = 0; i < itemCount; i++) {
			Item item = items[i];
			System.out.println((i + 1) + ". " + item.getItemName() + " | Price: $" + item.getItemPrice() + " | Quantity: " + item.getItemQuantity());

		}
	}

	// Method to remove an item from the cart
	// Takes an index as input, removes the corresponding item, and shifts the remaining items
	public void removeItem(int index) {
		// Check if the index is valid (within the bounds of the array)
		if (index < 1 || index > itemCount) {
			System.out.println("Invalid index.");
			return; // Exit the method if index is invalid
		}
				
		// Get the name of the item to be removed
		String removedItem = items[index - 1].getItemName();


		// Shift the items in the array to the left, filling the gap left by the removed item
		for (int i = index - 1; i < itemCount - 1; i++) {
			items[i] = items[i + 1]; // Move the next item to the current position
		}

		// Nullify the last item and decrement the item count
		items[--itemCount] = null; 

		// Print a confirmation message with the name of the removed item
		System.out.println("\n" + removedItem + " is removed successfully!");
	}


	//Method to change the quantity of an item in the cart
	//Takes an index and a new quantity as inputs, and updates the item's quantity
	public void changeQuantity(int index, int newQuantity) {
		// Check if the index is valid (within the bounds of the array)
		if (index < 1 || index > itemCount) {
			System.out.println("Invalid index.");
			return; // Exit the method if index is invalid
		}

		// Update the quantity of the item at the specified index
		items[index - 1].setItemQuantity(newQuantity);

		// Print a confirmation message indicating the quantity has been updated
		System.out.println("Quantity updated.");
	}


	//Method to calculate the total price of items in the cart (excluding tax)
	//Iterates through all items in the cart, multiplying their price by quantity and summing the results
	public double calculateTotal() {
		double total = 0; // Initialize the total price as 0
		// Loop through all items in the cart
		for (int i = 0; i < itemCount; i++) {
			// Add the price of each item (price * quantity) to the total
			total += items[i].getItemPrice() * items[i].getItemQuantity();
		}
		return total; // Return the total price
	}

	//Handles the checkout process, including payment, and calculates any remaining amount or return amount
	public void checkOut(Scanner scanner, double totalAmount) {
		double amountPaid; // Amount the user has paid so far
		double remainingAmount; // Amount remaining to be paid
		double additionalPayment; // Additional payment user enters

		// Display the total amount for the user to pay
		System.out.println("\nTotal amount : $" + String.format("%.2f", totalAmount));

		// Prompt the user for the initial payment amount
		amountPaid = ShoppingCartSystem.validatePositiveDoubleInput(scanner, "Enter the amount to pay: ");

		// Continue the payment process until the total amount is met
		do {
			// If the paid amount is less than the total amount, prompt for the remaining balance
			if (totalAmount > amountPaid) {
				remainingAmount = totalAmount - amountPaid;
				additionalPayment = ShoppingCartSystem.validatePositiveDoubleInput(scanner, "Enter the remaining amount ($" + String.format("%.2f", remainingAmount) + "): ");
				amountPaid += additionalPayment; // Add the additional payment to the total paid amount
			}

			// If the total amount equals the paid amount, print the success message
			if (totalAmount == amountPaid) {
				System.out.println("You have completed your payment! Thank you!");
				break; // Exit the loop as payment is complete
			}
			// If the paid amount exceeds the total amount, print the return amount
			else if (amountPaid > totalAmount) {
				System.out.println("\nYou have completed your payment! This is your return: $" + 
						String.format("%.2f", (amountPaid - totalAmount)));
				break; // Exit the loop as payment is complete and return the excess
			}
		} while (amountPaid < totalAmount); // Continue prompting until the total amount is paid
	}
}


//Class to handle tax calculations
//Provides methods to calculate tax amount and total amount including tax
class TaxCalculator {

	// Method to calculate the tax amount based on the total amount and tax rate
	public static double calculateTaxAmount(double total, double taxRate) {
		return total * (taxRate / 100); // Returns tax amount by applying the tax rate to the total
	}

	// Method to calculate the total amount including tax
	public static double calculateTotal(double total, double taxRate) {
		return total + calculateTaxAmount(total, taxRate); // Adds the tax amount to the total price
	}
}

