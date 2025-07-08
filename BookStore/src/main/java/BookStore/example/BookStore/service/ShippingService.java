package BookStore.example.BookStore.service;

import BookStore.example.BookStore.model.PaperBook;

public interface ShippingService {
    void shipBook(PaperBook book, int quantity, String address);
}

class ShippingServiceImpl implements ShippingService {
    @Override
    public void shipBook(PaperBook book, int quantity, String address) {
        System.out.println("Quantum book store - Shipping " + quantity + " copy(ies) of '" + 
                         book.getTitle() + "' to address: " + address);
    }
} 