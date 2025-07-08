package BookStore.example.BookStore;

import BookStore.example.BookStore.model.*;
import BookStore.example.BookStore.service.BookStore;

public class QuantumBookstoreDemo {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("Quantum book store - Welcome to Quantum Book Store!");
        System.out.println("=".repeat(60));
        
        BookStore bookStore = new BookStore();
        
        // Add books to inventory
        System.out.println("\n1. Adding books to inventory:");
        bookStore.addBook(new PaperBook("ISBN-001", "Java: The Complete Reference", 
                                       "Herbert Schildt", 2022, 59.99, 25));
        bookStore.addBook(new EBook("ISBN-002", "Spring Boot in Action", 
                                   "Craig Walls", 2021, 39.99, "PDF"));
        bookStore.addBook(new ShowcaseBook("ISBN-003", "Future of AI Programming", 
                                          "Tech Innovator", 2024));
        bookStore.addBook(new PaperBook("ISBN-004", "Old Java Patterns", 
                                       "Legacy Author", 2010, 25.99, 5));
        
        // Display inventory
        System.out.println("\n2. Current inventory:");
        bookStore.displayInventory();
        
        // Purchase books
        System.out.println("\n3. Purchasing books:");
        try {
            double cost1 = bookStore.buyBook("ISBN-001", 3, "customer1@example.com", "123 Tech Street");
            double cost2 = bookStore.buyBook("ISBN-002", 1, "customer2@example.com", "");
            System.out.println("Quantum book store - Total spent: $" + (cost1 + cost2));
        } catch (Exception e) {
            System.out.println("Quantum book store - Error: " + e.getMessage());
        }
        
        // Try to buy showcase book (should fail)
        System.out.println("\n4. Attempting to buy showcase book:");
        try {
            bookStore.buyBook("ISBN-003", 1, "customer@example.com", "123 Main St");
        } catch (Exception e) {
            System.out.println("Quantum book store - Expected error: " + e.getMessage());
        }
        
        // Remove outdated books
        System.out.println("\n5. Removing outdated books (older than 12 years):");
        var removedBooks = bookStore.removeOutdatedBooks(12);
        System.out.println("Quantum book store - Removed " + removedBooks.size() + " outdated book(s)");
        
        // Final inventory
        System.out.println("\n6. Final inventory:");
        bookStore.displayInventory();
        
        // Demonstrate duplicate book checking
        System.out.println("\n6.5. Demonstrating duplicate book checking:");
        PaperBook duplicateBook = new PaperBook("ISBN-001", "Duplicate Java Book", 
                                               "Different Author", 2023, 69.99, 15);
        bookStore.addBook(duplicateBook); // This should be rejected
        
        // Show how to update existing book
        System.out.println("\nQuantum book store - Updating existing book:");
        PaperBook updatedBook = new PaperBook("ISBN-001", "Java: The Complete Reference (Updated)", 
                                             "Herbert Schildt", 2023, 64.99, 20);
        bookStore.updateBook(updatedBook);
        
        // Demonstrate extensibility with a custom book type
        System.out.println("\n7. Demonstrating extensibility with custom book type:");
        
        // Custom AudioBook class - shows how easy it is to extend
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
        
        AudioBook audioBook = new AudioBook("ISBN-AUDIO-001", "Clean Architecture", 
                                          "Robert C. Martin", 2020, 29.99, 
                                          "Professional Voice", 600);
        bookStore.addBook(audioBook);
        
        // Purchase the audio book
        try {
            double audioCost = bookStore.buyBook("ISBN-AUDIO-001", 1, "audiobook@example.com", "");
            System.out.println("Quantum book store - Audio book purchased for: $" + audioCost);
        } catch (Exception e) {
            System.out.println("Quantum book store - Error: " + e.getMessage());
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Quantum book store - Demo completed successfully!");
        System.out.println("=".repeat(60));
    }
} 