// Book class & attributes
public class Book {
	private int BookID;
	private String Title;
	private String Author;
	private String Category;
	private int PublishedYear;
	private String ISBN;

	// Constructor to add a new book record
	public Book(int BookID, String Title, String Author, String Category, int PublishedYear, String ISBN) {
		this.BookID = BookID;
		this.Title = Title;
		this.Author = Author;
		this.Category = Category;
		this.PublishedYear = PublishedYear;
		this.ISBN = ISBN;
	}

	// Setters and Getters for book records
	public int getBookID() {
		return BookID;
	}

	public void setBookID(int BookID) {
		this.BookID = BookID;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String Title) {
		this.Title = Title;
	}

	public String getAuthor() {
		return Author;
	}

	public void setAuthor(String Author) {
		this.Author = Author;
	}

	public String getCategory() {
		return Category;
	}

	public void setCategory(String Category) {
		this.Category = Category;
	}

	public int getPublishedYear() {
		return PublishedYear;
	}

	public void setPublishedYear(int PublishedYear) {
		this.PublishedYear = PublishedYear;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String ISBN) {
		this.ISBN = ISBN;
	}
}