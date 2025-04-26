import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.*;
import java.util.Optional;

public class LibraryManagementFX extends Application {

	// Create a library, table and list to hold the book records
	private LibraryInfo Library = new LibraryInfo();
	private TableView<Book> BookTableView = new TableView<>();
	private ObservableList<Book> bookList = FXCollections.observableArrayList();

	@Override
	public void start(Stage loadingStage) {
		loadingStage.setTitle("Library Management System");

		// Create a BorderPane for the layout
		BorderPane borderPane = new BorderPane();

		// Title Text
		Label titleLabel = new Label("Welcome to the Library Management System");
		titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");

		// Description Text
		Label descriptionLabel = new Label("Click the button above to load book records from a file.");
		descriptionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666; -fx-padding: 10px;");

		// Button to load books
		Button loadingButton = new Button("Click to Load Book Records");
		loadingButton.setStyle(
				"-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-border-radius: 5px;");

		// Load action for the button
		loadingButton.setOnAction(e -> loadBooksFromFile(loadingStage));

		// Add title and description to the top and the button to the center
		borderPane.setTop(titleLabel);
		BorderPane.setAlignment(titleLabel, Pos.CENTER);
		borderPane.setCenter(loadingButton);
		borderPane.setBottom(descriptionLabel);
		BorderPane.setAlignment(descriptionLabel, Pos.CENTER);

		// Add some padding to the edges of the BorderPane for a better layout
		borderPane.setPadding(new Insets(20));

		// Create the scene with the BorderPane and set it to the stage
		Scene initialScene = new Scene(borderPane, 600, 250);
		loadingStage.setScene(initialScene);
		loadingStage.show();
	}

	// Method to display books in a tableview
	private void showBooksTable() {
		Stage tableStage = new Stage();
		tableStage.setTitle("Book List");

		// Title Label for the table window
		Label tableTitle = new Label("Books in Library");
		tableTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");

		// Define the table's columns (ID, Title, Author, etc)
		TableColumn<Book, Integer> IDColumn = new TableColumn<>("Book ID");
		IDColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBookID()).asObject());
		IDColumn.setPrefWidth(100);
		IDColumn.setSortable(true);

		TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
		titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
		titleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		titleColumn.setOnEditCommit(event -> {
			Book book = event.getRowValue();
			book.setTitle(event.getNewValue());
		});
		titleColumn.setPrefWidth(300);
		titleColumn.setEditable(true);
		titleColumn.setSortable(true);

		TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
		authorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuthor()));
		authorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		authorColumn.setOnEditCommit(event -> {
			Book book = event.getRowValue();
			book.setAuthor(event.getNewValue());
		});
		authorColumn.setPrefWidth(200);
		authorColumn.setEditable(true);
		authorColumn.setSortable(true);

		TableColumn<Book, String> categoryColumn = new TableColumn<>("Category");
		categoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));
		categoryColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		categoryColumn.setOnEditCommit(event -> {
			Book book = event.getRowValue();
			book.setCategory(event.getNewValue());
		});
		categoryColumn.setPrefWidth(150);
		categoryColumn.setEditable(true);
		categoryColumn.setSortable(true);

		TableColumn<Book, Integer> yearColumn = new TableColumn<>("Published Year");
		yearColumn.setCellValueFactory(
				cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getPublishedYear())
						.asObject());
		yearColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		yearColumn.setOnEditCommit(event -> {
			Book book = event.getRowValue();
			book.setPublishedYear(event.getNewValue());
		});
		yearColumn.setPrefWidth(150);
		yearColumn.setEditable(true);
		yearColumn.setSortable(true);

		TableColumn<Book, String> ISBNColumn = new TableColumn<>("ISBN");
		ISBNColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getISBN()));
		ISBNColumn.setPrefWidth(200);
		ISBNColumn.setSortable(true);

		// Make table and book records editable
		BookTableView.getColumns().setAll(IDColumn, titleColumn, authorColumn, categoryColumn, yearColumn, ISBNColumn);
		BookTableView.setItems(bookList);
		BookTableView.setEditable(true);

		// Create buttons (insert, delete, search, etc)
		Button insertButton = new Button("Insert Book Record");
		Button deleteButton = new Button("Delete Book Record");
		Button searchButton = new Button("Search Book");
		Button statsButton = new Button("Display Statistics");
		Button checkAuthorButton = new Button("Check Author Activity");
		Button saveButton = new Button("Save Updated Data");

		// Add a style for the buttons to give a nicer look
		String buttonStyle = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px;";
		insertButton.setStyle(buttonStyle);
		deleteButton.setStyle(buttonStyle);
		searchButton.setStyle(buttonStyle);
		checkAuthorButton.setStyle(buttonStyle);
		saveButton.setStyle(buttonStyle);

		// Set button actions
		insertButton.setOnAction(e -> insertBookRecord());
		deleteButton.setOnAction(e -> deleteBookRecord());
		searchButton.setOnAction(e -> searchBookRecord());
		checkAuthorButton.setOnAction(e -> openAuthorCheckWindow());
		saveButton.setOnAction(e -> saveUpdatedBooks());

		// Style the statistics ComboBox
		ComboBox<String> statsComboBox = new ComboBox<>();
		statsComboBox.getItems().addAll("Number of books by category", "Number of books by author",
				"Number of books by year", "Year with the most books published", "Year with the least books published",
				"Author with the most books published", "Author with the least books published");
		statsComboBox.setValue("Number of books by category");
		statsComboBox.setStyle(
				"-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px;");

		// Style the input field (TextField)
		TextField inputField = new TextField();
		inputField.setPrefWidth(250);
		inputField.setPromptText("Enter category, author, or year");
		inputField.setStyle(
				"-fx-background-color: #f0f0f0; -fx-border-color: #ddd; -fx-border-radius: 5px; -fx-font-size: 14px; -fx-padding: 5px 10px;");

		// Style the "Display Statistics" button
		statsButton.setStyle(
				"-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px;");

		// Display Statistics Button action
		statsButton.setOnAction(e -> displayStatistics(statsComboBox.getValue(), inputField.getText()));

		// HBox for statistics section
		HBox statsControls = new HBox(10, statsComboBox, inputField, statsButton);
		statsControls.setAlignment(Pos.CENTER);
		statsControls.setPadding(new Insets(10));

		// VBox for buttons
		VBox buttonBox = new VBox(10, insertButton, deleteButton, searchButton, checkAuthorButton, saveButton);
		buttonBox.setAlignment(Pos.CENTER);

		// VBox for Main stage layout
		VBox vbox = new VBox(10, tableTitle, BookTableView, buttonBox, statsControls);
		vbox.setAlignment(Pos.CENTER);

		// Main layout in BorderPane
		BorderPane borderPane = new BorderPane();
		borderPane.setCenter(vbox);

		// Set scene and show stage
		Scene tableScene = new Scene(borderPane, 1200, 600);
		tableStage.setScene(tableScene);
		tableStage.show();

		// Style the TableView
		BookTableView.setStyle("-fx-border-color: #388E3C; " + "-fx-border-width: 2px; "
				+ "-fx-background-color: #f9f9f9; " + "-fx-font-size: 14px;");

		// Style the Table headers (set background and font)
		BookTableView.lookup(".column-header")
				.setStyle("-fx-background-color: #4CAF50; " + "-fx-text-fill: white; " + "-fx-font-size: 16px; ");

		// Style the Table cells (set text color and padding)
		BookTableView.lookup(".table-cell").setStyle("-fx-padding: 10px; " + "-fx-text-fill: #333333;");

		// Style the selected row
		BookTableView.setRowFactory(tv -> {
			TableRow<Book> row = new TableRow<>();
			row.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
				if (isSelected) {
					row.setStyle("-fx-background-color: #388E3C; -fx-text-fill: white;");
				} else {
					row.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
				}
			});
			return row;
		});
	}

	// Method to load book records from a file using a file chooser
	private void loadBooksFromFile(Stage stage) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select Book File");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

		// Open a file chooser dialog to select a file
		File selectedFile = fileChooser.showOpenDialog(stage);
		if (selectedFile != null) {
			// Read the selected file and load data
			readBooksFile(selectedFile);
			stage.close();
			showBooksTable();
		}
	}

	// Method to read book records from the selected file
	private void readBooksFile(File file) {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			boolean firstLine = true;

			// Read each line from the file
			while ((line = reader.readLine()) != null) {
				if (firstLine) {
					// Check if the first line contains the header, if so, skip it
					if (line.contains("BookID")) {
						firstLine = false;
						continue;
					}
					// Don't skip the first line if it contains a book record
					firstLine = false;
				}

				// Split lines by comma and space
				String[] data = line.split(", ");

				// Parse all data in the book record
				if (data.length == 6) {
					try {
						int bookID = Integer.parseInt(data[0]);
						String title = data[1];
						String author = data[2];
						String category = data[3];
						int publishedYear = Integer.parseInt(data[4]);
						String isbn = data[5];

						// Create a new book object and add it to the library
						Book newBook = new Book(bookID, title, author, category, publishedYear, isbn);
						Library.insertBook(bookID, title, author, category, publishedYear, isbn);
						bookList.add(newBook);

						// Skip line if it contains invalid data
					} catch (NumberFormatException e) {
					}
				}
			}
			// If any reading errors happens
		} catch (IOException e) {
		}
	}

	// Method to display a window for book record insertion
	private void insertBookRecord() {
		Stage insertStage = new Stage();
		insertStage.setTitle("Insert a New Book");

		Label idLabel = new Label("Book ID:");
		TextField idField = new TextField();

		Label titleLabel = new Label("Title:");
		TextField titleField = new TextField();

		Label authorLabel = new Label("Author:");
		TextField authorField = new TextField();

		Label categoryLabel = new Label("Category:");
		TextField categoryField = new TextField();

		Label yearLabel = new Label("Published Year:");
		TextField yearField = new TextField();

		Label isbnLabel = new Label("ISBN:");
		TextField isbnField = new TextField();

		// Set a button to insert the book record into the table
		Button submitButton = new Button("Insert");
		submitButton.setOnAction(e -> {
			try {
				int bookID = Integer.parseInt(idField.getText());
				String title = titleField.getText().trim();
				String author = authorField.getText().trim();
				String category = categoryField.getText().trim();
				int publishedYear = Integer.parseInt(yearField.getText());
				String isbn = isbnField.getText().trim();

				// Ensure the release year is not a future year
				int currentYear = java.time.Year.now().getValue();

				// If year in the future, don't add
				if (publishedYear > currentYear) {
					showAlert(Alert.AlertType.ERROR, "Invalid Year", "Published Year cannot be a future year.");
					return;
				}

				// Ensure all data fields are not empty
				if (title.isEmpty() || author.isEmpty() || category.isEmpty() || isbn.isEmpty()) {
					showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled.");
					return;
				}

				// Check if the book being added has a duplicate bookID or ISBN
				for (int i = 0; i < bookList.size(); i++) {
					Book book = bookList.get(i);
					if (book.getBookID() == bookID) {
						showAlert(Alert.AlertType.ERROR, "Duplicate ID", "A book with this ID already exists.");
						return;
					}
					if (book.getISBN().equals(isbn)) {
						showAlert(Alert.AlertType.ERROR, "Duplicate ISBN", "A book with this ISBN already exists.");
						return;
					}
				}

				// Add the book to the list and show it in the table
				Library.insertBook(bookID, title, author, category, publishedYear, isbn);
				bookList.add(new Book(bookID, title, author, category, publishedYear, isbn));
				showAlert(Alert.AlertType.CONFIRMATION, "Successful", "Book record has been added to table.");
				insertStage.close();

				// Make sure the bookID and year entered are integers
			} catch (NumberFormatException ex) {
				showAlert(Alert.AlertType.ERROR, "Invalid Input", "Book ID and Published Year must be valid numbers.");
			}
		});

		// Use GridPane for insertion layout
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(20));
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setAlignment(Pos.CENTER);

		grid.add(idLabel, 0, 0);
		grid.add(idField, 1, 0);
		grid.add(titleLabel, 0, 1);
		grid.add(titleField, 1, 1);
		grid.add(authorLabel, 0, 2);
		grid.add(authorField, 1, 2);
		grid.add(categoryLabel, 0, 3);
		grid.add(categoryField, 1, 3);
		grid.add(yearLabel, 0, 4);
		grid.add(yearField, 1, 4);
		grid.add(isbnLabel, 0, 5);
		grid.add(isbnField, 1, 5);
		grid.add(submitButton, 1, 6);

		// Set scene and show the insertion window
		Scene scene = new Scene(grid, 350, 300);
		insertStage.setScene(scene);
		insertStage.show();
	}

	// Method to display a window that removes a book record by bookID
	private void deleteBookRecord() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Delete Book");
		dialog.setHeaderText("Delete a Book Record");
		dialog.setContentText("Enter Book ID to delete:");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			try {
				int bookID = Integer.parseInt(result.get());

				if (Library.removeBook(bookID)) {
					bookList.removeIf(book -> book.getBookID() == bookID);
					showAlert(Alert.AlertType.CONFIRMATION, "Successful", "Book record has been deleted from table.");
				} else {
					showAlert(Alert.AlertType.ERROR, "Error", "Book ID not found.");
				}
			} catch (NumberFormatException e) {
				showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid Book ID.");
			}
		}
	}

	// Method to find a book record by ID, Title or Author
	private void searchBookRecord() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Search Book");
		dialog.setHeaderText("Search Book Record");
		dialog.setContentText("Enter Book ID, Title, or Author:");

		// Search for book records depending on the input
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			String searchText = result.get().trim().toLowerCase();

			// Make sure input is not empty
			if (searchText.isEmpty()) {
				showAlert(Alert.AlertType.ERROR, "Error", "Please enter a Book ID, Title, or Author to search.");
				return;
			}

			StringBuilder bookDetails = new StringBuilder();
			boolean bookFound = false;

			// Parse input and check if the entered ID exists in the table
			try {
				int bookID = Integer.parseInt(searchText);
				for (int i = 0; i < bookList.size(); i++) {
					Book book = bookList.get(i);
					// If yes, return the book record
					if (book.getBookID() == bookID) {
						bookDetails.append(returnBookRecords(book)).append("\n\n");
						bookFound = true;
						break;
					}
				}
			} catch (NumberFormatException ignored) {
				for (int i = 0; i < bookList.size(); i++) {
					Book book = bookList.get(i);
					// If Author name is entered, check if it exists
					if (book.getTitle().toLowerCase().contains(searchText)
							|| book.getAuthor().toLowerCase().contains(searchText)) {
						bookDetails.append(returnBookRecords(book)).append("\n\n");
						bookFound = true;
					}
				}
			}

			// Return the book if found
			if (bookFound) {
				showAlert(Alert.AlertType.INFORMATION, "Books Found", bookDetails.toString());
				// Otherwise, return an error message
			} else {
				showAlert(Alert.AlertType.ERROR, "Not Found", "No book found matching the given criteria.");
			}
		}
	}

	// Return the book records found using the search function
	private String returnBookRecords(Book book) {
		return "Book ID: " + book.getBookID() + "\nTitle: " + book.getTitle() + "\nAuthor: " + book.getAuthor()
				+ "\nCategory: " + book.getCategory() + "\nPublished Year: " + book.getPublishedYear() + "\nISBN: "
				+ book.getISBN() + "";
	}

	// Method to return specific statistics about book records
	private void displayStatistics(String choice, String input) {
		// Year with the most books published
		if (choice.equals("Year with the most books published")) {
			int maxYear = -1;
			int maxCount = 0;
			StringBuilder booksList = new StringBuilder();

			for (int i = 0; i < bookList.size(); i++) {
				Book book = bookList.get(i);
				int year = book.getPublishedYear();
				int count = Library.booksByYearNum(year);

				if (count > maxCount) {
					maxCount = count;
					maxYear = year;
					booksList.setLength(0);
				}
				if (year == maxYear) {
					booksList.append("- ").append(book.getTitle()).append("\n");
				}
			}

			if (maxYear != -1) {
				showAlert(Alert.AlertType.INFORMATION, "Statistics", "Year with the most books published: " + maxYear
						+ " (" + maxCount + " books)\n\nBooks:\n" + booksList);
			} else {
				showAlert(Alert.AlertType.INFORMATION, "Statistics", "No books available.");
			}
			return;
		}

		// Year with the least books published
		if (choice.equals("Year with the least books published")) {
			int minYear = -1;
			int minCount = Integer.MAX_VALUE;
			StringBuilder booksList = new StringBuilder();

			for (int i = 0; i < bookList.size(); i++) {
				Book book = bookList.get(i);
				int year = book.getPublishedYear();
				int count = Library.booksByYearNum(year);

				if (count < minCount) {
					minCount = count;
					minYear = year;
					booksList.setLength(0);
				}
				if (year == minYear) {
					booksList.append("- ").append(book.getTitle()).append("\n");
				}
			}

			if (minYear != -1) {
				showAlert(Alert.AlertType.INFORMATION, "Statistics", "Year with the least books published: " + minYear
						+ " (" + minCount + " books)\n\nBooks:\n" + booksList);
			} else {
				showAlert(Alert.AlertType.INFORMATION, "Statistics", "No books available.");
			}
			return;
		}

		// Author with the most books published
		if (choice.equals("Author with the most books published")) {
			String maxAuthor = null;
			int maxCount = 0;
			StringBuilder booksList = new StringBuilder();

			for (int i = 0; i < bookList.size(); i++) {
				Book book = bookList.get(i);
				String author = book.getAuthor();
				int count = Library.countBooksByAuthor(author);

				if (count > maxCount) {
					maxCount = count;
					maxAuthor = author;
					booksList.setLength(0);
				}
				if (author.equalsIgnoreCase(maxAuthor)) {
					booksList.append("- ").append(book.getTitle()).append("\n");
				}
			}

			if (maxAuthor != null) {
				showAlert(Alert.AlertType.INFORMATION, "Statistics", "Author with the most books published: "
						+ maxAuthor + " (" + maxCount + " books)\n\nBooks:\n" + booksList);
			} else {
				showAlert(Alert.AlertType.INFORMATION, "Statistics", "No books available.");
			}
			return;
		}

		// Author with the least books published
		if (choice.equals("Author with the least books published")) {
			String minAuthor = null;
			int minCount = Integer.MAX_VALUE;
			StringBuilder booksList = new StringBuilder();

			for (int i = 0; i < bookList.size(); i++) {
				Book book = bookList.get(i);
				String author = book.getAuthor();
				int count = Library.countBooksByAuthor(author);

				if (count < minCount) {
					minCount = count;
					minAuthor = author;
					booksList.setLength(0);
				}
				if (author.equalsIgnoreCase(minAuthor)) {
					booksList.append("- ").append(book.getTitle()).append("\n");
				}
			}

			if (minAuthor != null) {
				showAlert(Alert.AlertType.INFORMATION, "Statistics", "Author with the least books published: "
						+ minAuthor + " (" + minCount + " books)\n\nBooks:\n" + booksList);
			} else {
				showAlert(Alert.AlertType.INFORMATION, "Statistics", "No books available.");
			}
			return;
		}

		// Validate input only for cases where input is required
		if (input.isEmpty() && (choice.equals("Number of books by category")
				|| choice.equals("Number of books by author") || choice.equals("Number of books by year"))) {
			showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid input.");
			return;
		}

		int count = 0;
		StringBuilder booksFound = new StringBuilder();
		String resultMessage = "";

		// Count books by category, author, or year
		try {
			if (choice.equals("Number of books by category")) {
				for (int i = 0; i < bookList.size(); i++) {
					Book book = bookList.get(i);
					if (book.getCategory().equalsIgnoreCase(input)) {
						count++;
						booksFound.append("- ").append(book.getTitle()).append("\n");
					}
				}
				resultMessage = "Total books in category '" + input + "': " + count;
			} else if (choice.equals("Number of books by author")) {
				for (int i = 0; i < bookList.size(); i++) {
					Book book = bookList.get(i);
					if (book.getAuthor().equalsIgnoreCase(input)) {
						count++;
						booksFound.append("- ").append(book.getTitle()).append("\n");
					}
				}
				resultMessage = "Total books by author '" + input + "': " + count;
			} else if (choice.equals("Number of books by year")) {
				int year = Integer.parseInt(input);
				for (int i = 0; i < bookList.size(); i++) {
					Book book = bookList.get(i);
					if (book.getPublishedYear() == year) {
						count++;
						booksFound.append("- ").append(book.getTitle()).append("\n");
					}
				}
				resultMessage = "Total books published in year " + year + ": " + count;
			}

			if (count == 0) {
				showAlert(Alert.AlertType.INFORMATION, "No Results", resultMessage + "\nNo books found.");
			} else {
				showAlert(Alert.AlertType.INFORMATION, "Books Found", resultMessage + "\n\n" + booksFound);
			}
		} catch (NumberFormatException e) {
			showAlert(Alert.AlertType.ERROR, "Error", "Invalid year format. Please enter a valid year.");
		}
	}

	// Method to display a window that checks author activity
	private void openAuthorCheckWindow() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Check Author Activity");
		dialog.setHeaderText("Enter the author's name:");
		dialog.setContentText("Author Name:");

		// Retrieve the input and check if it exists in the table
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			String author = result.get().trim();
			if (author.isEmpty()) {
				showAlert(Alert.AlertType.ERROR, "Error", "Please enter an author name.");
				// Check if the author has published in the last 5 years
			} else {
				boolean isActive = Library.AuthorActiveOrNot(author);
				// If yes, then author is active
				if (isActive) {
					showAlert(Alert.AlertType.INFORMATION, "Author Active",
							author + " is still active (published in the last 5 years). ");
					// Otherwise, author is not active
				} else {
					showAlert(Alert.AlertType.INFORMATION, "Author Inactive",
							author + " is not active (no books in the last 5 years). ");
				}
			}
		}
	}

	// Method to save the updated records to a new file (file chooser)
	private void saveUpdatedBooks() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Book Data");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

		// Set default file name
		fileChooser.setInitialFileName("updatedBooks.txt");

		// Show save dialog
		File file = fileChooser.showSaveDialog(null);

		if (file != null) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
				// Write the header line
				writer.write("BookID, Title, Author, Category, Published Year, ISBN");
				writer.newLine();

				// Navigate through all books and print them to the file
				for (int i = 0; i < bookList.size(); i++) {
					Book book = bookList.get(i);
					String line = String.format("%03d, %s, %s, %s, %d, %s", book.getBookID(), book.getTitle(),
							book.getAuthor(), book.getCategory(), book.getPublishedYear(), book.getISBN());
					writer.write(line);
					writer.newLine();
				}
				showAlert(Alert.AlertType.CONFIRMATION, "Success",
						"Book data has been saved to " + file.getAbsolutePath());
				// If any saving errors happens
			} catch (IOException e) {
				showAlert(Alert.AlertType.ERROR, "Error", "There was an error saving book records to file.");
				e.printStackTrace();
			}
		}
	}

	// Updated alert method to handle both error and info messages
	private void showAlert(Alert.AlertType alertType, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	// Launch the FX program
	public static void main(String[] args) {
		launch(args);
	}
}