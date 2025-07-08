package BookStore.example.BookStore.model;

public abstract class Book {
    protected String isbn;
    protected String title;
    protected String author;
    protected int yearPublished;
    protected double price;

    public Book(String isbn, String title, String author, int yearPublished, double price) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.yearPublished = yearPublished;
        this.price = price;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getYearPublished() {
        return yearPublished;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public abstract boolean isAvailableForPurchase();
    public abstract String getBookType();

    @Override
    public String toString() {
        return String.format("Quantum book store - %s: %s by %s (%d) - $%.2f", 
                           getBookType(), title, author, yearPublished, price);
    }
} 