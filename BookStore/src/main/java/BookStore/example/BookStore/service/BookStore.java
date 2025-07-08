package BookStore.example.BookStore.service;

import BookStore.example.BookStore.model.Book;
import BookStore.example.BookStore.model.PaperBook;
import BookStore.example.BookStore.model.EBook;
import BookStore.example.BookStore.model.ShowcaseBook;

import java.time.Year;
import java.util.*;

public class BookStore {
    private Map<String, Book> inventory;
    private ShippingService shippingService;
    private MailService mailService;

    public BookStore() {
        this.inventory = new HashMap<>();
        this.shippingService = new ShippingServiceImpl();
        this.mailService = new MailServiceImpl();
    }

    // Constructor with dependency injection for testing
    public BookStore(ShippingService shippingService, MailService mailService) {
        this.inventory = new HashMap<>();
        this.shippingService = shippingService;
        this.mailService = mailService;
    }

    public void addBook(Book book) {
        if (inventory.containsKey(book.getIsbn())) {
            Book existingBook = inventory.get(book.getIsbn());
            System.out.println("Quantum book store - Warning: Book with ISBN " + book.getIsbn() + 
                             " already exists in inventory!");
            System.out.println("Quantum book store - Existing: " + existingBook);
            System.out.println("Quantum book store - New book not added: " + book);
            return;
        }
        
        inventory.put(book.getIsbn(), book);
        System.out.println("Quantum book store - Added to inventory: " + book);
    }

    public void updateBook(Book book) {
        if (inventory.containsKey(book.getIsbn())) {
            Book oldBook = inventory.get(book.getIsbn());
            inventory.put(book.getIsbn(), book);
            System.out.println("Quantum book store - Updated book in inventory:");
            System.out.println("Quantum book store - Old: " + oldBook);
            System.out.println("Quantum book store - New: " + book);
        } else {
            System.out.println("Quantum book store - Book with ISBN " + book.getIsbn() + 
                             " not found in inventory. Use addBook() to add new books.");
        }
    }

    public void addOrUpdateBook(Book book) {
        if (inventory.containsKey(book.getIsbn())) {
            updateBook(book);
        } else {
            inventory.put(book.getIsbn(), book);
            System.out.println("Quantum book store - Added to inventory: " + book);
        }
    }

    public List<Book> removeOutdatedBooks(int yearsThreshold) {
        List<Book> removedBooks = new ArrayList<>();
        int currentYear = Year.now().getValue();
        
        Iterator<Map.Entry<String, Book>> iterator = inventory.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Book> entry = iterator.next();
            Book book = entry.getValue();
            
            if (currentYear - book.getYearPublished() > yearsThreshold) {
                removedBooks.add(book);
                iterator.remove();
                System.out.println("Quantum book store - Removed outdated book: " + book.getTitle());
            }
        }
        
        return removedBooks;
    }

    public double buyBook(String isbn, int quantity, String email, String address) {
        Book book = inventory.get(isbn);
        
        if (book == null) {
            throw new IllegalArgumentException("Quantum book store - Book with ISBN " + isbn + " not found");
        }

        if (!book.isAvailableForPurchase()) {
            throw new IllegalArgumentException("Quantum book store - Book '" + book.getTitle() + "' is not available for purchase");
        }

        double totalAmount = 0;

        if (book instanceof PaperBook) {
            PaperBook paperBook = (PaperBook) book;
            paperBook.reduceStock(quantity);
            totalAmount = book.getPrice() * quantity;
            shippingService.shipBook(paperBook, quantity, address);
        } else if (book instanceof EBook) {
            EBook eBook = (EBook) book;
            totalAmount = book.getPrice() * quantity;
            for (int i = 0; i < quantity; i++) {
                mailService.sendEBook(eBook, email);
            }
        }

        System.out.println("Quantum book store - Purchase completed. Total amount: $" + totalAmount);
        return totalAmount;
    }

    public Book getBook(String isbn) {
        return inventory.get(isbn);
    }

    public Collection<Book> getAllBooks() {
        return inventory.values();
    }

    public void displayInventory() {
        System.out.println("Quantum book store - Current Inventory:");
        System.out.println("================================");
        if (inventory.isEmpty()) {
            System.out.println("Quantum book store - No books in inventory");
        } else {
            for (Book book : inventory.values()) {
                System.out.println(book);
            }
        }
        System.out.println("================================");
    }
} 