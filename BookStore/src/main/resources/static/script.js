// Global variables
let allBooks = [];
let currentBookType = 'paper';
let currentFilter = 'all';

// API Base URL
const API_BASE = '/api/books';

// DOM Content Loaded
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
});

// Initialize the application
function initializeApp() {
    console.log('Quantum book store: Initializing frontend application...');
    setupEventListeners();
    loadBooks();
    updateInventoryStats();
}

// Setup event listeners
function setupEventListeners() {
    // Navigation tabs
    document.querySelectorAll('.nav-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            switchTab(this.dataset.tab);
        });
    });

    // Book type selector
    document.querySelectorAll('.book-type-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            selectBookType(this.dataset.type);
        });
    });

    // Filter buttons
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            filterBooks(this.dataset.filter);
        });
    });

    // Search input
    document.getElementById('searchInput').addEventListener('input', function() {
        searchBooks(this.value);
    });

    // Add book form
    document.getElementById('addBookForm').addEventListener('submit', handleAddBook);

    // Purchase form
    document.getElementById('purchaseForm').addEventListener('submit', handlePurchase);

    // Modal close
    document.querySelector('.close').addEventListener('click', closePurchaseModal);

    // Modal backdrop close
    document.getElementById('purchaseModal').addEventListener('click', function(e) {
        if (e.target === this) {
            closePurchaseModal();
        }
    });

    // Quantity change in purchase modal
    document.getElementById('quantity').addEventListener('input', updateTotalAmount);
}

// Tab switching
function switchTab(tabName) {
    console.log(`Quantum book store: Switching to ${tabName} tab`);
    
    // Update navigation buttons
    document.querySelectorAll('.nav-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    document.querySelector(`[data-tab="${tabName}"]`).classList.add('active');

    // Update tab content
    document.querySelectorAll('.tab-content').forEach(content => {
        content.classList.remove('active');
    });
    document.getElementById(tabName).classList.add('active');

    // Load specific tab data
    if (tabName === 'browse') {
        loadBooks();
    } else if (tabName === 'manage') {
        updateInventoryStats();
    }
}

// Book type selection
function selectBookType(type) {
    console.log(`Quantum book store: Selected book type: ${type}`);
    currentBookType = type;

    // Update buttons
    document.querySelectorAll('.book-type-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    document.querySelector(`[data-type="${type}"]`).classList.add('active');

    // Show/hide form fields based on type
    updateFormFields(type);
}

// Update form fields based on book type
function updateFormFields(type) {
    const priceGroup = document.querySelector('.price-group');
    const stockGroup = document.querySelector('.stock-group');
    const filetypeGroup = document.querySelector('.filetype-group');
    const priceInput = document.getElementById('price');

    // Reset visibility
    priceGroup.style.display = 'block';
    stockGroup.style.display = 'none';
    filetypeGroup.style.display = 'none';

    switch(type) {
        case 'paper':
            stockGroup.style.display = 'block';
            priceInput.required = true;
            break;
        case 'ebook':
            filetypeGroup.style.display = 'block';
            priceInput.required = true;
            break;
        case 'showcase':
            priceGroup.style.display = 'none';
            priceInput.required = false;
            priceInput.value = '0';
            break;
    }
}

// Load all books
async function loadBooks() {
    console.log('Quantum book store: Loading books from server...');
    try {
        showLoading();
        const response = await fetch(API_BASE);
        if (response.ok) {
            allBooks = await response.json();
            displayBooks(allBooks);
            console.log(`Quantum book store: Loaded ${allBooks.length} books successfully`);
        } else {
            throw new Error('Failed to load books');
        }
    } catch (error) {
        console.error('Quantum book store: Error loading books:', error);
        showNotification('Failed to load books', 'error');
        document.getElementById('booksGrid').innerHTML = 
            '<p style="text-align: center; color: #666;">Failed to load books. Please try again.</p>';
    }
}

// Display books in grid
function displayBooks(books) {
    const grid = document.getElementById('booksGrid');
    
    if (books.length === 0) {
        grid.innerHTML = '<p style="text-align: center; color: #666;">No books found.</p>';
        return;
    }

    grid.innerHTML = books.map(book => createBookCard(book)).join('');
}

// Create book card HTML
function createBookCard(book) {
    const bookType = book.type || getBookTypeFromClass(book);
    const stockInfo = getStockInfo(book);
    const isPurchasable = book.price > 0 && (!book.stock || book.stock > 0);
    
    return `
        <div class="book-card" data-type="${bookType}">
            <div class="book-type ${bookType.toLowerCase().replace(/[^a-z]/g, '')}">${bookType}</div>
            <h3 class="book-title">${book.title}</h3>
            <p class="book-author">by ${book.author}</p>
            <div class="book-details">
                <span>ISBN: ${book.isbn}</span>
                <span>Year: ${book.yearPublished}</span>
            </div>
            ${stockInfo}
            <div class="book-price">
                ${book.price > 0 ? `$${book.price.toFixed(2)}` : 'FREE (Demo)'}
            </div>
            <div class="book-actions">
                ${isPurchasable ? 
                    `<button class="btn btn-primary" onclick="openPurchaseModal('${book.isbn}')">
                        <i class="fas fa-shopping-cart"></i> Buy Now
                    </button>` :
                    `<button class="btn btn-secondary" disabled>
                        <i class="fas fa-ban"></i> ${book.price === 0 ? 'Demo Only' : 'Out of Stock'}
                    </button>`
                }
                <button class="btn btn-secondary" onclick="viewBookDetails('${book.isbn}')">
                    <i class="fas fa-info-circle"></i> Details
                </button>
            </div>
        </div>
    `;
}

// Get book type from Java class name
function getBookTypeFromClass(book) {
    if (book.stock !== undefined) return 'Paper Book';
    if (book.fileType !== undefined) return 'EBook';
    return 'Showcase/Demo Book';
}

// Get stock information
function getStockInfo(book) {
    if (book.stock === undefined) return '';
    
    let stockClass = 'stock-out';
    let stockText = 'Out of Stock';
    
    if (book.stock > 20) {
        stockClass = 'stock-high';
        stockText = `${book.stock} in stock`;
    } else if (book.stock > 10) {
        stockClass = 'stock-medium';
        stockText = `${book.stock} in stock`;
    } else if (book.stock > 0) {
        stockClass = 'stock-low';
        stockText = `Only ${book.stock} left`;
    }
    
    return `<div class="stock-indicator ${stockClass}">${stockText}</div>`;
}

// Filter books
function filterBooks(filter) {
    console.log(`Quantum book store: Filtering books by: ${filter}`);
    currentFilter = filter;

    // Update filter buttons
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    document.querySelector(`[data-filter="${filter}"]`).classList.add('active');

    // Filter and display books
    let filteredBooks = allBooks;
    if (filter !== 'all') {
        filteredBooks = allBooks.filter(book => getBookTypeFromClass(book) === filter);
    }

    displayBooks(filteredBooks);
}

// Search books
function searchBooks(query) {
    if (!query.trim()) {
        filterBooks(currentFilter);
        return;
    }

    console.log(`Quantum book store: Searching for: ${query}`);
    const searchTerm = query.toLowerCase();
    const searchResults = allBooks.filter(book => 
        book.title.toLowerCase().includes(searchTerm) ||
        book.author.toLowerCase().includes(searchTerm) ||
        book.isbn.toLowerCase().includes(searchTerm)
    );

    displayBooks(searchResults);
}

// Handle add book form submission
async function handleAddBook(e) {
    e.preventDefault();
    console.log(`Quantum book store: Adding new ${currentBookType} book...`);

    const formData = new FormData(e.target);
    const bookData = Object.fromEntries(formData.entries());

    // Convert numeric fields
    bookData.yearPublished = parseInt(bookData.yearPublished);
    if (bookData.price) bookData.price = parseFloat(bookData.price);
    if (bookData.stock) bookData.stock = parseInt(bookData.stock);

    try {
        const endpoint = `${API_BASE}/${currentBookType}`;
        const response = await fetch(endpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(bookData)
        });

        const result = await response.text();
        
        if (response.ok) {
            showNotification(result, 'success');
            e.target.reset();
            loadBooks(); // Refresh the books list
            updateInventoryStats();
            console.log(`Quantum book store: ${result}`);
        } else {
            throw new Error(result);
        }
    } catch (error) {
        console.error('Quantum book store: Error adding book:', error);
        showNotification(error.message, 'error');
    }
}

// Open purchase modal
function openPurchaseModal(isbn) {
    console.log(`Quantum book store: Opening purchase modal for ISBN: ${isbn}`);
    const book = allBooks.find(b => b.isbn === isbn);
    if (!book) return;

    document.getElementById('purchaseIsbn').value = isbn;
    document.getElementById('quantity').value = 1;
    
    // Show/hide address field based on book type
    const addressGroup = document.querySelector('.address-group');
    const bookType = getBookTypeFromClass(book);
    if (bookType === 'Paper Book') {
        addressGroup.style.display = 'block';
        document.getElementById('address').required = true;
    } else {
        addressGroup.style.display = 'none';
        document.getElementById('address').required = false;
    }

    // Display book details
    document.getElementById('bookDetails').innerHTML = `
        <div class="book-summary">
            <h3>${book.title}</h3>
            <p>by ${book.author}</p>
            <p><strong>Price:</strong> $${book.price.toFixed(2)}</p>
            ${book.stock ? `<p><strong>Available:</strong> ${book.stock} units</p>` : ''}
        </div>
    `;

    updateTotalAmount();
    document.getElementById('purchaseModal').style.display = 'block';
}

// Close purchase modal
function closePurchaseModal() {
    document.getElementById('purchaseModal').style.display = 'none';
    document.getElementById('purchaseForm').reset();
}

// Update total amount in purchase modal
function updateTotalAmount() {
    const isbn = document.getElementById('purchaseIsbn').value;
    const quantity = parseInt(document.getElementById('quantity').value) || 1;
    const book = allBooks.find(b => b.isbn === isbn);
    
    if (book) {
        const total = book.price * quantity;
        document.getElementById('totalAmount').textContent = total.toFixed(2);
    }
}

// Handle purchase form submission
async function handlePurchase(e) {
    e.preventDefault();
    console.log('Quantum book store: Processing purchase...');

    const formData = new FormData(e.target);
    const purchaseData = {
        isbn: formData.get('purchaseIsbn') || document.getElementById('purchaseIsbn').value,
        quantity: parseInt(formData.get('quantity')),
        email: formData.get('email'),
        address: formData.get('address') || ''
    };

    try {
        const response = await fetch(`${API_BASE}/buy`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(purchaseData)
        });

        const result = await response.json();
        
        if (response.ok) {
            showNotification(`${result.message} Amount paid: $${result.amount.toFixed(2)}`, 'success');
            closePurchaseModal();
            loadBooks(); // Refresh to update stock
            updateInventoryStats();
            console.log(`Quantum book store: Purchase completed successfully. Amount: $${result.amount}`);
        } else {
            throw new Error(result.message || 'Purchase failed');
        }
    } catch (error) {
        console.error('Quantum book store: Error processing purchase:', error);
        showNotification(error.message, 'error');
    }
}

// View book details
function viewBookDetails(isbn) {
    const book = allBooks.find(b => b.isbn === isbn);
    if (!book) return;

    const bookType = getBookTypeFromClass(book);
    let details = `
        📚 Title: ${book.title}
        👤 Author: ${book.author}
        🏷️ ISBN: ${book.isbn}
        📅 Year: ${book.yearPublished}
        💰 Price: $${book.price.toFixed(2)}
        📦 Type: ${bookType}
    `;

    if (book.stock !== undefined) {
        details += `\n📊 Stock: ${book.stock} units`;
    }
    if (book.fileType) {
        details += `\n📄 File Type: ${book.fileType}`;
    }

    alert(`Quantum Book Store - Book Details\n\n${details}`);
}

// Remove outdated books
async function removeOutdatedBooks() {
    const years = parseInt(document.getElementById('yearsThreshold').value);
    if (!years || years < 1) {
        showNotification('Please enter a valid years threshold', 'error');
        return;
    }

    console.log(`Quantum book store: Removing books older than ${years} years...`);

    try {
        const response = await fetch(`${API_BASE}/outdated/${years}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            const removedBooks = await response.json();
            showNotification(`Removed ${removedBooks.length} outdated books`, 'success');
            loadBooks();
            updateInventoryStats();
            console.log(`Quantum book store: Successfully removed ${removedBooks.length} outdated books`);
        } else {
            throw new Error('Failed to remove outdated books');
        }
    } catch (error) {
        console.error('Quantum book store: Error removing outdated books:', error);
        showNotification(error.message, 'error');
    }
}

// Update inventory statistics
function updateInventoryStats() {
    console.log('Quantum book store: Updating inventory statistics...');
    
    const stats = {
        total: allBooks.length,
        paperBooks: allBooks.filter(b => getBookTypeFromClass(b) === 'Paper Book').length,
        ebooks: allBooks.filter(b => getBookTypeFromClass(b) === 'EBook').length,
        showcase: allBooks.filter(b => getBookTypeFromClass(b) === 'Showcase/Demo Book').length,
        totalStock: allBooks.reduce((sum, book) => sum + (book.stock || 0), 0),
        totalValue: allBooks.reduce((sum, book) => sum + (book.price * (book.stock || 1)), 0)
    };

    document.getElementById('inventoryStats').innerHTML = `
        <div class="stat-item">
            <div class="stat-number">${stats.total}</div>
            <div class="stat-label">Total Books</div>
        </div>
        <div class="stat-item">
            <div class="stat-number">${stats.paperBooks}</div>
            <div class="stat-label">Paper Books</div>
        </div>
        <div class="stat-item">
            <div class="stat-number">${stats.ebooks}</div>
            <div class="stat-label">E-Books</div>
        </div>
        <div class="stat-item">
            <div class="stat-number">${stats.showcase}</div>
            <div class="stat-label">Showcase Books</div>
        </div>
        <div class="stat-item">
            <div class="stat-number">${stats.totalStock}</div>
            <div class="stat-label">Total Stock</div>
        </div>
        <div class="stat-item">
            <div class="stat-number">$${stats.totalValue.toFixed(0)}</div>
            <div class="stat-label">Inventory Value</div>
        </div>
    `;
}

// Show loading spinner
function showLoading() {
    document.getElementById('booksGrid').innerHTML = `
        <div class="loading">
            <div class="spinner"></div>
        </div>
    `;
}

// Show notification
function showNotification(message, type = 'info') {
    console.log(`Quantum book store: ${message}`);
    
    const notification = document.getElementById('notification');
    const messageEl = document.getElementById('notificationMessage');
    
    messageEl.textContent = message;
    notification.className = `notification ${type}`;
    notification.classList.add('show');
    
    setTimeout(() => {
        notification.classList.remove('show');
    }, 5000);
}

// Utility function to format currency
function formatCurrency(amount) {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD'
    }).format(amount);
}

// Error handling for fetch requests
function handleFetchError(error) {
    console.error('Quantum book store: Network error:', error);
    if (error.name === 'TypeError' && error.message.includes('fetch')) {
        showNotification('Network error. Please check if the server is running.', 'error');
    } else {
        showNotification('An unexpected error occurred. Please try again.', 'error');
    }
}

// Make sure all functions are globally accessible
window.openPurchaseModal = openPurchaseModal;
window.viewBookDetails = viewBookDetails;
window.removeOutdatedBooks = removeOutdatedBooks; 