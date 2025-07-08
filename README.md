# 📚 Quantum Book Store

Welcome to the **Quantum Book Store** - a comprehensive online book management system built with Java and Spring Boot. This system demonstrates object-oriented design principles, extensibility, and modern software architecture patterns.

## 🚀 Features

### 📖 Book Types
- **Paper Books**: Physical books with stock management and shipping
- **EBooks**: Digital books with file type support and email delivery
- **Showcase/Demo Books**: Display-only books not available for purchase

### 🛒 Core Functionality
- ✅ Add books to inventory with duplicate ISBN checking
- ✅ Remove outdated books based on publication year
- ✅ Purchase books with automatic stock management
- ✅ Shipping service integration for paper books
- ✅ Email service integration for ebooks
- ✅ Comprehensive error handling and validation

### 🔧 System Design
- **Extensible Architecture**: Easy to add new book types without modifying existing code
- **Service Layer Pattern**: Clean separation of concerns
- **Abstract Base Classes**: Polymorphic behavior for different book types
- **Comprehensive Testing**: Full test suite with `QuantumBookstoreFullTest`

## 🏗️ Architecture

```
src/
├── main/java/BookStore/example/BookStore/
│   ├── model/
│   │   ├── Book.java              # Abstract base class
│   │   ├── PaperBook.java         # Physical books with stock
│   │   ├── EBook.java             # Digital books
│   │   └── ShowcaseBook.java      # Demo books
│   ├── service/
│   │   ├── BookStore.java         # Main business logic
│   │   ├── ShippingService.java   # Shipping integration
│   │   └── MailService.java       # Email integration
│   ├── QuantumBookstoreDemo.java  # Demo application
│   └── BookStoreApplication.java  # Spring Boot main class
└── test/java/BookStore/example/BookStore/
    └── QuantumBookstoreFullTest.java  # Comprehensive test suite
```

## 🛠️ Technologies Used

- **Java 24**
- **Spring Boot 3.5.4**
- **Maven** for dependency management
- **JUnit 5** for testing
- **Spring Boot Starter Web** for REST API capabilities
- **Spring Boot DevTools** for development productivity

## 🚀 Getting Started

### Prerequisites
- Java 24 or higher
- Maven 3.6+ (or use the included Maven wrapper)

### Running the Application

#### 1. Run the Demo Application
```bash
cd BookStore
./mvnw compile exec:java -Dexec.mainClass="BookStore.example.BookStore.QuantumBookstoreDemo"
```

This will show a complete demonstration of:
- Adding different types of books
- Purchasing books
- Duplicate ISBN handling
- Removing outdated books
- System extensibility with custom book types

#### 2. Run the Spring Boot Application
```bash
cd BookStore
./mvnw spring-boot:run
```

This starts the web server on `http://localhost:8080`

#### 3. Run Tests
```bash
cd BookStore
./mvnw test
```

Run specific test class:
```bash
./mvnw test -Dtest=QuantumBookstoreFullTest
```

#### 4. Build the Project
```bash
cd BookStore
./mvnw clean install
```

## 📋 API Usage Examples

### Book Management

#### Add a Paper Book
```java
PaperBook book = new PaperBook("ISBN-001", "Java Programming", "Author Name", 2023, 49.99, 10);
bookStore.addBook(book);
```

#### Add an EBook
```java
EBook ebook = new EBook("ISBN-002", "Digital Design", "Tech Author", 2023, 29.99, "PDF");
bookStore.addBook(ebook);
```

#### Add a Showcase Book
```java
ShowcaseBook showcase = new ShowcaseBook("ISBN-003", "Future Tech", "Visionary", 2024);
bookStore.addBook(showcase);
```

### Purchase Operations

#### Buy a Paper Book
```java
double cost = bookStore.buyBook("ISBN-001", 2, "customer@email.com", "123 Main St");
// Automatically reduces stock and initiates shipping
```

#### Buy an EBook
```java
double cost = bookStore.buyBook("ISBN-002", 1, "customer@email.com", "");
// Automatically sends download link via email
```

### Inventory Management

#### Remove Outdated Books
```java
List<Book> removed = bookStore.removeOutdatedBooks(10); // Remove books older than 10 years
```

#### Check for Duplicates
```java
// The system automatically prevents duplicate ISBNs
bookStore.addBook(duplicateBook); // Will show warning and not add
```

#### Update Existing Books
```java
bookStore.updateBook(updatedBook);        // Update existing book
bookStore.addOrUpdateBook(book);          // Add new or update existing
```

## 🧪 Testing

The project includes comprehensive testing with `QuantumBookstoreFullTest` that covers:

- ✅ Adding different types of books
- ✅ Duplicate ISBN prevention
- ✅ Stock management for paper books
- ✅ Purchase workflows for all book types
- ✅ Removing outdated books
- ✅ Error handling for invalid operations
- ✅ System extensibility demonstration

### Sample Test Output
```
Quantum book store - Testing: Adding Books to Inventory
Quantum book store - Added to inventory: Paper Book: Java Programming by Author Name (2023) - $49.99 - Stock: 10
Quantum book store - Testing: Buying Paper Books
Quantum book store - Purchase completed. Total amount: $99.98
Quantum book store - Shipping 2 copy(ies) of 'Java Programming' to address: 123 Main St
```

## 🔄 Extensibility

The system is designed for easy extension. Here's how to add a new book type:

```java
public class AudioBook extends Book {
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
```

The new book type automatically works with all existing functionality!

## 🎯 Key Design Patterns

1. **Template Method Pattern**: Abstract `Book` class with concrete implementations
2. **Strategy Pattern**: Different purchase behaviors for different book types
3. **Factory Pattern**: BookStore manages creation and lifecycle of books
4. **Service Layer Pattern**: Clean separation between business logic and presentation
5. **Dependency Injection**: Services can be injected for testing

## 📊 Sample Output

```
============================================================
Quantum book store - Welcome to Quantum Book Store!
============================================================

1. Adding books to inventory:
Quantum book store - Added to inventory: Paper Book: Java: The Complete Reference by Herbert Schildt (2022) - $59.99 - Stock: 25
Quantum book store - Added to inventory: EBook: Spring Boot in Action by Craig Walls (2021) - $39.99 - File Type: PDF
Quantum book store - Added to inventory: Showcase/Demo Book: Future of AI Programming by Tech Innovator (2024) - NOT FOR SALE

2. Current inventory:
================================
Quantum book store - Paper Book: Java: The Complete Reference by Herbert Schildt (2022) - $59.99 - Stock: 25
Quantum book store - EBook: Spring Boot in Action by Craig Walls (2021) - $39.99 - File Type: PDF
Quantum book store - Showcase/Demo Book: Future of AI Programming by Tech Innovator (2024) - NOT FOR SALE
================================

3. Purchasing books:
Quantum book store - Shipping 3 copy(ies) of 'Java: The Complete Reference' to address: 123 Tech Street
Quantum book store - Purchase completed. Total amount: $179.97
Quantum book store - Sending ebook 'Spring Boot in Action' (PDF) to email: customer2@example.com
Quantum book store - Purchase completed. Total amount: $39.99
Quantum book store - Total spent: $219.96
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is created for educational purposes and demonstrates best practices in Java development and Spring Boot applications.

## 👨‍💻 Author

Created as a demonstration of object-oriented design principles, clean architecture, and modern Java development practices.

---

**Quantum book store** - *Where every book finds its perfect reader* 📚✨ 