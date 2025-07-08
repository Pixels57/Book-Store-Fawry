package BookStore.example.BookStore.controller;

import BookStore.example.BookStore.model.*;
import BookStore.example.BookStore.service.BookStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class BookStoreController {

    private final BookStore bookStore;

    public BookStoreController() {
        this.bookStore = new BookStore();
        // Add some sample books for demo
        initializeSampleBooks();
    }

    private void initializeSampleBooks() {
        bookStore.addBook(new PaperBook("ISBN-001", "Java: The Complete Reference", 
                                       "Herbert Schildt", 2022, 59.99, 25));
        bookStore.addBook(new EBook("ISBN-002", "Spring Boot in Action", 
                                   "Craig Walls", 2021, 39.99, "PDF"));
        bookStore.addBook(new ShowcaseBook("ISBN-003", "Future of AI Programming", 
                                          "Tech Innovator", 2024));
        bookStore.addBook(new PaperBook("ISBN-004", "Clean Code", 
                                       "Robert C. Martin", 2020, 49.99, 15));
        bookStore.addBook(new EBook("ISBN-005", "Design Patterns", 
                                   "Gang of Four", 2019, 44.99, "EPUB"));
    }

    @GetMapping
    public ResponseEntity<Collection<Book>> getAllBooks() {
        return ResponseEntity.ok(bookStore.getAllBooks());
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<Book> getBook(@PathVariable String isbn) {
        Book book = bookStore.getBook(isbn);
        if (book != null) {
            return ResponseEntity.ok(book);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/paper")
    public ResponseEntity<String> addPaperBook(@RequestBody PaperBookRequest request) {
        try {
            PaperBook book = new PaperBook(request.isbn, request.title, 
                                         request.author, request.yearPublished, 
                                         request.price, request.stock);
            bookStore.addBook(book);
            return ResponseEntity.ok("Paper book added successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/ebook")
    public ResponseEntity<String> addEBook(@RequestBody EBookRequest request) {
        try {
            EBook book = new EBook(request.isbn, request.title, 
                                 request.author, request.yearPublished, 
                                 request.price, request.fileType);
            bookStore.addBook(book);
            return ResponseEntity.ok("EBook added successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/showcase")
    public ResponseEntity<String> addShowcaseBook(@RequestBody ShowcaseBookRequest request) {
        try {
            ShowcaseBook book = new ShowcaseBook(request.isbn, request.title, 
                                               request.author, request.yearPublished);
            bookStore.addBook(book);
            return ResponseEntity.ok("Showcase book added successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/buy")
    public ResponseEntity<PurchaseResponse> buyBook(@RequestBody PurchaseRequest request) {
        try {
            double amount = bookStore.buyBook(request.isbn, request.quantity, 
                                            request.email, request.address);
            return ResponseEntity.ok(new PurchaseResponse("Purchase successful! Thank you for shopping with Quantum Book Store.", amount));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new PurchaseResponse("Error: " + e.getMessage(), 0));
        }
    }

    @DeleteMapping("/outdated/{years}")
    public ResponseEntity<List<Book>> removeOutdatedBooks(@PathVariable int years) {
        List<Book> removedBooks = bookStore.removeOutdatedBooks(years);
        return ResponseEntity.ok(removedBooks);
    }

    // DTOs
    public static class PaperBookRequest {
        public String isbn, title, author;
        public int yearPublished, stock;
        public double price;
    }

    public static class EBookRequest {
        public String isbn, title, author, fileType;
        public int yearPublished;
        public double price;
    }

    public static class ShowcaseBookRequest {
        public String isbn, title, author;
        public int yearPublished;
    }

    public static class PurchaseRequest {
        public String isbn, email, address;
        public int quantity;
    }

    public static class PurchaseResponse {
        public String message;
        public double amount;

        public PurchaseResponse(String message, double amount) {
            this.message = message;
            this.amount = amount;
        }
    }
} 