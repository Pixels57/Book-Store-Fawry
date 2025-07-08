package BookStore.example.BookStore.model;

public class EBook extends Book {
    private String fileType;

    public EBook(String isbn, String title, String author, int yearPublished, double price, String fileType) {
        super(isbn, title, author, yearPublished, price);
        this.fileType = fileType;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public boolean isAvailableForPurchase() {
        return true;
    }

    @Override
    public String getBookType() {
        return "EBook";
    }

    @Override
    public String toString() {
        return super.toString() + " - File Type: " + fileType;
    }
} 