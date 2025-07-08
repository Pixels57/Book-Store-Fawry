package BookStore.example.BookStore.service;

import BookStore.example.BookStore.model.EBook;

public interface MailService {
    void sendEBook(EBook book, String email);
}

class MailServiceImpl implements MailService {
    @Override
    public void sendEBook(EBook book, String email) {
        System.out.println("Quantum book store - Sending ebook '" + book.getTitle() + 
                         "' (" + book.getFileType() + ") to email: " + email);
    }
} 