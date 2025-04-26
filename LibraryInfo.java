import java.util.ArrayList;
import java.util.Calendar;

public class LibraryInfo {

	// Store books in an ArrayList
	private ArrayList<Book> Books;

	// Constructor to initialize a new ArrayList of books
	public LibraryInfo() {
		Books = new ArrayList<>();
	}

	// Method to add a book record to the ArrayList
	public void insertBook(int bookID, String title, String author, String category, int publishedYear, String isbn) {
		Book newBook = new Book(bookID, title, author, category, publishedYear, isbn);
		// Add the book to the list
		Books.add(newBook);
	}

	// Method to remove a book record from the ArrayList using it's ID
	public boolean removeBook(int bookID) {
		// Loop through books and find the matching ID
		for (int i = 0; i < Books.size(); i++) {
			// Remove book from the list if ID matches
			if (Books.get(i).getBookID() == bookID) {
				Books.remove(i);
				return true;
			}
		}
		// If BookID was not found
		return false;
	}

	// Method to search for a book record by it's bookID
	public Book searchBookByID(int bookID) {
		// Find the matching bookID and return it's book record if found
		for (int i = 0; i < Books.size(); i++) {
			if (Books.get(i).getBookID() == bookID) {
				return Books.get(i);
			}
		}
		// If entered ID was not found
		return null;
	}

	// Method to search for a book record by it's Title
	public Book searchBookByTitle(String title) {
		// Find the matching title and return the book record if found
		for (int i = 0; i < Books.size(); i++) {
			if (Books.get(i).getTitle().equalsIgnoreCase(title)) {
				return Books.get(i);
			}
		}
		// If entered title was not found
		return null;
	}

	// Method to count the number of books of a category
	public int booksByCategoryNum(String category) {
		int count = 0;
		// Loop through books and return the books from the chosen category
		for (int i = 0; i < Books.size(); i++) {
			if (Books.get(i).getCategory().equalsIgnoreCase(category)) {
				count++;
			}
		}
		// Return the number of books found
		return count;
	}

	// Method to count the number of books of an author
	public int booksByAuthorNum(String author) {
		int count = 0;
		// Find and return all books by the author
		for (int i = 0; i < Books.size(); i++) {
			if (Books.get(i).getAuthor().equalsIgnoreCase(author)) {
				count++;
			}
		}
		// Return the number of books found
		return count;
	}

	// Method to count the number of books published in a specific year
	public int booksByYearNum(int year) {
		int count = 0;
		// Find and return all books from that year
		for (int i = 0; i < Books.size(); i++) {
			if (Books.get(i).getPublishedYear() == year) {
				count++;
			}
		}
		// Return the number of books found
		return count;
	}

	// Method to find the year with the most number of books
	public void yearWithMostBooks() {
		// Set max counters to find the year
		int maxYear = -1, maxCount = 0;

		// Find the book and each book's published year
		for (int i = 0; i < Books.size(); i++) {
			int year = Books.get(i).getPublishedYear();
			// Get the number of books in that year
			int count = booksByYearNum(year);

			// If the current year has more books than the max year, update the max value
			if (count > maxCount) {
				maxCount = count;
				maxYear = year;
			}
		}

		// Display the year found with most books
		if (maxYear != -1) {
			System.out.println("Year with the most books published: " + maxYear + " (" + maxCount + " books)");
			// If no books are in the list
		} else {
			System.out.println("No books available.");
		}
	}

	// Method to find the year with the smallest number of books
	public void yearWithLeastBooks() {
		// Set min counters to find the year
		int minYear = -1, minCount = Integer.MAX_VALUE;
    
		// Find the book and each book's published year
		for (int i = 0; i < Books.size(); i++) {
			int year = Books.get(i).getPublishedYear();
			// Get the number of books in that year
			int count = booksByYearNum(year);

			// If the current year has less books than the min year, update the min value
			if (count < minCount) {
				minCount = count;
				minYear = year;
			}
		}

		// Display the year found with the least books
		if (minYear != -1) {
			System.out.println("Year with the least books published: " + minYear + " (" + minCount + " books)");
			// If no books are in the list
		} else {
			System.out.println("No books available.");
		}
	}

	// Method to find the author that has the most books
	public void findAuthorWithMaxBooks() {
		// Variables used to find the author
		String maxAuthor = null;
		int maxCount = 0;

		// Find the book and each book's author
		for (int i = 0; i < Books.size(); i++) {
			String author = Books.get(i).getAuthor();
			// Get the number of books made by that author
			int count = countBooksByAuthor(author);

			// If the current author has more books, update the max value
			if (count > maxCount) {
				maxCount = count;
				maxAuthor = author;
			}
		}

		// Display the author and his books
		if (maxAuthor != null) {
			System.out.println("Author with the most books: " + maxAuthor + " (" + maxCount + " books)");
			displayBooksByAuthor(maxAuthor);
			// If no books are in the list
		} else {
			System.out.println("No books available.");
		}
	}

	// Method to find the author that has the least books
	public void findAuthorWithMinBooks() {
		// Variables used to find the author
		String minAuthor = null;
		int minCount = Integer.MAX_VALUE;

		// Find the book and each book's author
		for (int i = 0; i < Books.size(); i++) {
			String author = Books.get(i).getAuthor();
			// Get the number of books made by that author
			int count = countBooksByAuthor(author);

			// If the current author has less books, update the min value
			if (count < minCount) {
				minCount = count;
				minAuthor = author;
			}
		}

		// Display the author and his books
		if (minAuthor != null) {
			System.out.println("Author with the fewest books: " + minAuthor + " (" + minCount + " books)");
			displayBooksByAuthor(minAuthor);
			// If no books are in the list
		} else {
			System.out.println("No books available.");
		}
	}

	// Helper method to count the number of books by a specific author
	public int countBooksByAuthor(String author) {
		int count = 0;

		// Go through the books and find the matching author
		for (int i = 0; i < Books.size(); i++) {
			if (Books.get(i).getAuthor().equalsIgnoreCase(author)) {
				count++;
			}
		}
		// Return the number of books by that author
		return count;
	}

	// Method to display all books by a specific author
	public void displayBooksByAuthor(String author) {
		System.out.println("Books by " + author + ":");
		// Find the books by that author and print them
		for (int i = 0; i < Books.size(); i++) {
			if (Books.get(i).getAuthor().equalsIgnoreCase(author)) {
				System.out.println("- " + Books.get(i).getTitle());
			}
		}
	}

	// Method to check if an author is active or not
	public boolean AuthorActiveOrNot(String author) {
		// Get the current year using the calendar
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		for (int i = 0; i < Books.size(); i++) {
			// Author is active if his last book was published in the last 5 years
			if (Books.get(i).getAuthor().equalsIgnoreCase(author)
					&& Books.get(i).getPublishedYear() >= (currentYear - 5)) {
				return true;
			}
		}
		// Otherwise, the author is not active
		return false;
	}
}