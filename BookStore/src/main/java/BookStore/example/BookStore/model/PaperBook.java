package BookStore.example.BookStore.model;

public class PaperBook extends Book {
    private int stock;

    public PaperBook(String isbn, String title, String author, int yearPublished, double price, int stock) {
        super(isbn, title, author, yearPublished, price);
        this.stock = stock;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void reduceStock(int quantity) {
        if (quantity > stock) {
            throw new IllegalArgumentException("Quantum book store - Insufficient stock. Available: " + stock + ", Requested: " + quantity);
        }
        this.stock -= quantity;
    }

    @Override
    public boolean isAvailableForPurchase() {
        return stock > 0;
    }

    @Override
    public String getBookType() {
        return "Paper Book";
    }

    @Override
    public String toString() {
        return super.toString() + " - Stock: " + stock;
    }
} 