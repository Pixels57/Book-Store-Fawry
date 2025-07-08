package BookStore.example.BookStore;

import BookStore.example.BookStore.model.*;
import BookStore.example.BookStore.service.BookStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@DisplayName("Quantum Book Store Full Test Suite")
public class QuantumBookstoreFullTest {

    private BookStore bookStore;

    @BeforeEach
    void setUp() {
        bookStore = new BookStore();
        System.out.println("Quantum book store - Setting up test environment");
    }

    @Test
    @DisplayName("Test Adding Books to Inventory")
    void testAddingBooks() {
        System.out.println("\nQuantum book store - Testing: Adding Books to Inventory");
        
        // Test adding paper book
        PaperBook paperBook = new PaperBook("ISBN-001", "The Java Programming Language", 
                                           "James Gosling", 2020, 45.99, 10);
        bookStore.addBook(paperBook);
        
        // Test adding ebook
        EBook eBook = new EBook("ISBN-002", "Clean Code", "Robert C. Martin", 
                               2019, 29.99, "PDF");
        bookStore.addBook(eBook);
        
        // Test adding showcase book
        ShowcaseBook showcaseBook = new ShowcaseBook("ISBN-003", "Spring Framework Guide", 
                                                    "Rod Johnson", 2021);
        bookStore.addBook(showcaseBook);
        
        // Verify books are added
        assertEquals(paperBook, bookStore.getBook("ISBN-001"));
        assertEquals(eBook, bookStore.getBook("ISBN-002"));
        assertEquals(showcaseBook, bookStore.getBook("ISBN-003"));
        
        bookStore.displayInventory();
    }

    @Test
    @DisplayName("Test Adding Duplicate Books")
    void testAddingDuplicateBooks() {
        System.out.println("\nQuantum book store - Testing: Adding Duplicate Books");
        
        // Add initial book
        PaperBook originalBook = new PaperBook("ISBN-DUP-001", "Original Java Book", 
                                              "Original Author", 2020, 45.99, 10);
        bookStore.addBook(originalBook);
        
        // Try to add book with same ISBN
        PaperBook duplicateBook = new PaperBook("ISBN-DUP-001", "Duplicate Java Book", 
                                               "Different Author", 2021, 55.99, 15);
        bookStore.addBook(duplicateBook);
        
        // Verify original book is still in inventory, not the duplicate
        Book bookInInventory = bookStore.getBook("ISBN-DUP-001");
        assertEquals(originalBook, bookInInventory);
        assertEquals("Original Java Book", bookInInventory.getTitle());
        assertEquals("Original Author", bookInInventory.getAuthor());
        
        // Test update functionality
        PaperBook updatedBook = new PaperBook("ISBN-DUP-001", "Updated Java Book", 
                                             "Updated Author", 2022, 65.99, 20);
        bookStore.updateBook(updatedBook);
        
        // Verify book was updated
        Book updatedBookInInventory = bookStore.getBook("ISBN-DUP-001");
        assertEquals("Updated Java Book", updatedBookInInventory.getTitle());
        assertEquals("Updated Author", updatedBookInInventory.getAuthor());
        assertEquals(65.99, updatedBookInInventory.getPrice(), 0.01);
        
        // Test addOrUpdateBook functionality
        PaperBook newBook = new PaperBook("ISBN-NEW-DUP", "Brand New Book", 
                                         "New Author", 2023, 39.99, 5);
        bookStore.addOrUpdateBook(newBook);
        assertNotNull(bookStore.getBook("ISBN-NEW-DUP"));
        
        PaperBook anotherUpdate = new PaperBook("ISBN-NEW-DUP", "Updated New Book", 
                                               "New Author", 2023, 49.99, 8);
        bookStore.addOrUpdateBook(anotherUpdate);
        assertEquals("Updated New Book", bookStore.getBook("ISBN-NEW-DUP").getTitle());
        assertEquals(49.99, bookStore.getBook("ISBN-NEW-DUP").getPrice(), 0.01);
    }

    @Test
    @DisplayName("Test Removing Outdated Books")
    void testRemovingOutdatedBooks() {
        System.out.println("\nQuantum book store - Testing: Removing Outdated Books");
        
        // Add books with different years
        PaperBook oldBook = new PaperBook("ISBN-OLD-001", "Ancient Programming", 
                                         "Old Author", 2010, 15.99, 5);
        PaperBook newBook = new PaperBook("ISBN-NEW-001", "Modern Development", 
                                         "New Author", 2023, 35.99, 8);
        EBook oldEBook = new EBook("ISBN-OLD-002", "Legacy Systems", 
                                  "Legacy Author", 2005, 19.99, "EPUB");
        
        bookStore.addBook(oldBook);
        bookStore.addBook(newBook);
        bookStore.addBook(oldEBook);
        
        System.out.println("Quantum book store - Before removing outdated books:");
        bookStore.displayInventory();
        
        // Remove books older than 10 years
        List<Book> removedBooks = bookStore.removeOutdatedBooks(10);
        
        System.out.println("Quantum book store - After removing outdated books:");
        bookStore.displayInventory();
        
        // Verify only old books were removed
        assertEquals(2, removedBooks.size());
        assertNull(bookStore.getBook("ISBN-OLD-001"));
        assertNull(bookStore.getBook("ISBN-OLD-002"));
        assertNotNull(bookStore.getBook("ISBN-NEW-001"));
    }

    @Test
    @DisplayName("Test Buying Paper Books")
    void testBuyingPaperBooks() {
        System.out.println("\nQuantum book store - Testing: Buying Paper Books");
        
        PaperBook paperBook = new PaperBook("ISBN-PAPER-001", "Java Concurrency in Practice", 
                                           "Brian Goetz", 2022, 55.99, 5);
        bookStore.addBook(paperBook);
        
        // Test successful purchase
        double amount = bookStore.buyBook("ISBN-PAPER-001", 2, "test@example.com", "123 Main St");
        assertEquals(111.98, amount, 0.01);
        assertEquals(3, paperBook.getStock()); // Stock should be reduced
        
        // Test insufficient stock
        assertThrows(IllegalArgumentException.class, () -> {
            bookStore.buyBook("ISBN-PAPER-001", 5, "test@example.com", "123 Main St");
        });
    }

    @Test
    @DisplayName("Test Buying EBooks")
    void testBuyingEBooks() {
        System.out.println("\nQuantum book store - Testing: Buying EBooks");
        
        EBook eBook = new EBook("ISBN-EBOOK-001", "Design Patterns", 
                               "Gang of Four", 2021, 39.99, "PDF");
        bookStore.addBook(eBook);
        
        // Test successful ebook purchase
        double amount = bookStore.buyBook("ISBN-EBOOK-001", 1, "customer@example.com", "");
        assertEquals(39.99, amount, 0.01);
        
        // Test multiple ebook purchase
        double multipleAmount = bookStore.buyBook("ISBN-EBOOK-001", 3, "customer@example.com", "");
        assertEquals(119.97, multipleAmount, 0.01);
    }

    @Test
    @DisplayName("Test Showcase Books Are Not For Sale")
    void testShowcaseBooksNotForSale() {
        System.out.println("\nQuantum book store - Testing: Showcase Books Not For Sale");
        
        ShowcaseBook showcaseBook = new ShowcaseBook("ISBN-SHOWCASE-001", "Demo Book", 
                                                    "Demo Author", 2023);
        bookStore.addBook(showcaseBook);
        
        // Test that showcase books cannot be purchased
        assertThrows(IllegalArgumentException.class, () -> {
            bookStore.buyBook("ISBN-SHOWCASE-001", 1, "test@example.com", "123 Main St");
        });
        
        assertFalse(showcaseBook.isAvailableForPurchase());
        assertEquals(0.0, showcaseBook.getPrice());
    }

    @Test
    @DisplayName("Test Book Not Found")
    void testBookNotFound() {
        System.out.println("\nQuantum book store - Testing: Book Not Found");
        
        // Test buying non-existent book
        assertThrows(IllegalArgumentException.class, () -> {
            bookStore.buyBook("NON-EXISTENT-ISBN", 1, "test@example.com", "123 Main St");
        });
    }

    @Test
    @DisplayName("Test Book Properties and Author Field")
    void testBookProperties() {
        System.out.println("\nQuantum book store - Testing: Book Properties and Author Field");
        
        PaperBook book = new PaperBook("ISBN-PROP-001", "Effective Java", 
                                      "Joshua Bloch", 2018, 49.99, 10);
        
        assertEquals("ISBN-PROP-001", book.getIsbn());
        assertEquals("Effective Java", book.getTitle());
        assertEquals("Joshua Bloch", book.getAuthor());
        assertEquals(2018, book.getYearPublished());
        assertEquals(49.99, book.getPrice(), 0.01);
        assertEquals(10, book.getStock());
        assertTrue(book.isAvailableForPurchase());
        assertEquals("Paper Book", book.getBookType());
        
        System.out.println("Quantum book store - Book details: " + book);
    }

    @Test
    @DisplayName("Test System Extensibility")
    void testSystemExtensibility() {
        System.out.println("\nQuantum book store - Testing: System Extensibility");
        
        // Create a custom book type to demonstrate extensibility
        class AudioBook extends Book {
            private String narrator;
            private int durationMinutes;
            
            public AudioBook(String isbn, String title, String author, int yearPublished, 
                           double price, String narrator, int durationMinutes) {
                super(isbn, title, author, yearPublished, price);
                this.narrator = narrator;
                this.durationMinutes = durationMinutes;
            }
            
            @Override
            public boolean isAvailableForPurchase() {
                return true;
            }
            
            @Override
            public String getBookType() {
                return "Audio Book";
            }
            
            @Override
            public String toString() {
                return super.toString() + " - Narrator: " + narrator + 
                       ", Duration: " + durationMinutes + " minutes";
            }
        }
        
        AudioBook audioBook = new AudioBook("ISBN-AUDIO-001", "The Clean Coder", 
                                          "Robert C. Martin", 2020, 24.99, 
                                          "Professional Narrator", 480);
        
        bookStore.addBook(audioBook);
        
        // Test that the new book type works with existing functionality
        assertNotNull(bookStore.getBook("ISBN-AUDIO-001"));
        assertTrue(audioBook.isAvailableForPurchase());
        assertEquals("Audio Book", audioBook.getBookType());
        
        System.out.println("Quantum book store - Custom book type added: " + audioBook);
    }

    @Test
    @DisplayName("Test Complete Workflow")
    void testCompleteWorkflow() {
        System.out.println("\nQuantum book store - Testing: Complete Workflow");
        
        // Add various types of books
        bookStore.addBook(new PaperBook("ISBN-WF-001", "Spring Boot in Action", 
                                       "Craig Walls", 2022, 39.99, 15));
        bookStore.addBook(new EBook("ISBN-WF-002", "Microservices Patterns", 
                                   "Chris Richardson", 2021, 34.99, "EPUB"));
        bookStore.addBook(new ShowcaseBook("ISBN-WF-003", "Future of Programming", 
                                          "Tech Visionary", 2024));
        bookStore.addBook(new PaperBook("ISBN-WF-004", "Old Java Book", 
                                       "Vintage Author", 2008, 19.99, 3));
        
        System.out.println("Quantum book store - Initial inventory:");
        bookStore.displayInventory();
        
        // Purchase some books
        double paperBookCost = bookStore.buyBook("ISBN-WF-001", 2, "john@example.com", "456 Oak Ave");
        double eBookCost = bookStore.buyBook("ISBN-WF-002", 1, "jane@example.com", "");
        
        System.out.println("Quantum book store - Total purchases: $" + (paperBookCost + eBookCost));
        
        // Remove outdated books
        List<Book> removedBooks = bookStore.removeOutdatedBooks(12);
        
        System.out.println("Quantum book store - Final inventory:");
        bookStore.displayInventory();
        
        // Verify workflow
        assertTrue(paperBookCost > 0);
        assertTrue(eBookCost > 0);
        assertEquals(1, removedBooks.size());
    }
} 