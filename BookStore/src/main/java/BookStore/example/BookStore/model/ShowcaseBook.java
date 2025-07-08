package BookStore.example.BookStore.model;

public class ShowcaseBook extends Book {
    
    public ShowcaseBook(String isbn, String title, String author, int yearPublished) {
        super(isbn, title, author, yearPublished, 0.0); // Price is 0 for showcase books
    }

    @Override
    public boolean isAvailableForPurchase() {
        return false;
    }

    @Override
    public String getBookType() {
        return "Showcase/Demo Book";
    }

    @Override
    public void setPrice(double price) {
        System.out.println("Quantum book store - Warning: Cannot set price for showcase books");
    }

    @Override
    public String toString() {
        return String.format("Quantum book store - %s: %s by %s (%d) - NOT FOR SALE", 
                           getBookType(), getTitle(), getAuthor(), getYearPublished());
    }
} 