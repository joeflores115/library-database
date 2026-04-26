const API_BASE = '/api';

// --- Global State ---
let members = [];
let allBooks = [];
let currentFilter = 'all';

// --- Initialization ---
document.addEventListener('DOMContentLoaded', () => {
    refreshData();

    // Event Listeners
    document.getElementById('person-form').addEventListener('submit', handleAddPerson);
    document.getElementById('book-form').addEventListener('submit', handleAddBook);
    document.getElementById('borrow-form').addEventListener('submit', handleBorrowBook);
    document.getElementById('filter-all').addEventListener('click', () => setFilter('all'));
    document.getElementById('filter-available').addEventListener('click', () => setFilter('available'));
});

// --- Core Logic ---

async function refreshData() {
    await Promise.all([
        fetchMembers(),
        fetchBooks(),
        fetchLoans()
    ]);
    render();
}

async function fetchMembers() {
    try {
        const res = await fetch(`${API_BASE}/members`);
        members = await res.json();
    } catch (e) {
        showToast('Failed to load members', 'error');
    }
}

async function fetchBooks() {
    try {
        // Fetching all at once for simplicity in this MVP
        // In a real app we'd use pagination
        const res = await fetch(`${API_BASE}/books?size=100`);
        const data = await res.json();
        allBooks = data.content || data;
    } catch (e) {
        showToast('Failed to load books', 'error');
    }
}

async function fetchLoans() {
    try {
        const res = await fetch(`${API_BASE}/loans/active`);
        const loans = await res.json();
        renderLoans(loans);
    } catch (e) {
        showToast('Failed to load active loans', 'error');
    }
}

function setFilter(filter) {
    currentFilter = filter;
    document.getElementById('filter-all').classList.toggle('active', filter === 'all');
    document.getElementById('filter-available').classList.toggle('active', filter === 'available');
    renderBooks();
}

// --- Rendering ---

function render() {
    renderDropdowns();
    renderBooks();
}

function renderDropdowns() {
    const ownerSelect = document.getElementById('book-owner-select');
    const borrowerSelect = document.getElementById('borrower-select');
    const bookSelect = document.getElementById('borrow-book-select');

    // Populate People Dropdowns
    const memberOptions = '<option value="" disabled selected>Choose a person...</option>' +
        members.map(m => `<option value="${m.memberId}">${m.name}</option>`).join('');

    ownerSelect.innerHTML = memberOptions;
    borrowerSelect.innerHTML = memberOptions;

    // Populate Available Books Dropdown
    const availableBooks = allBooks.filter(b => b.available);
    bookSelect.innerHTML = '<option value="" disabled selected>Choose a book...</option>' +
        availableBooks.map(b => `<option value="${b.bookId}">${b.title}</option>`).join('');
}

function renderBooks() {
    const grid = document.getElementById('book-grid');
    const booksToDisplay = currentFilter === 'available'
        ? allBooks.filter(b => b.available)
        : allBooks;

    if (booksToDisplay.length === 0) {
        grid.innerHTML = '<p>No books found in this collection.</p>';
        return;
    }

    grid.innerHTML = booksToDisplay.map(b => `
        <div class="book-card ${b.available ? 'available' : 'borrowed'}">
            <div>
                <h4>${b.title}</h4>
                <p class="author">by ${b.author}</p>
                <p class="info"><strong>Owner:</strong> ${b.ownerName}</p>
                ${!b.available ? `<p class="info"><strong>Borrowed by:</strong> ${b.borrowerName}</p>` : ''}
            </div>
            <span class="status-badge ${b.available ? 'status-available' : 'status-borrowed'}">
                ${b.available ? 'Available' : 'Out on Loan'}
            </span>
        </div>
    `).join('');
}

function renderLoans(loans) {
    const container = document.getElementById('loan-list');

    if (loans.length === 0) {
        container.innerHTML = '<p>No books are currently borrowed.</p>';
        return;
    }

    container.innerHTML = loans.map(l => `
        <div class="loan-item">
            <div>
                <strong>${l.bookTitle}</strong> borrowed by <em>${l.borrowerName}</em>
                <br><small>Due: ${l.dueDate}</small>
            </div>
            <button class="btn-return" onclick="handleReturn(${l.loanId})">Return Book</button>
        </div>
    `).join('');
}

// --- Handlers ---

async function handleAddPerson(e) {
    e.preventDefault();
    const data = {
        name: document.getElementById('person-name').value,
        email: document.getElementById('person-email').value
    };

    const success = await postData('/members', data, 'Person added successfully!');
    if (success) {
        document.getElementById('person-form').reset();
        refreshData();
    }
}

async function handleAddBook(e) {
    e.preventDefault();
    const data = {
        title: document.getElementById('book-title').value,
        author: document.getElementById('book-author').value,
        ownerId: document.getElementById('book-owner-select').value
    };

    const success = await postData('/books', data, 'Book added to library!');
    if (success) {
        document.getElementById('book-form').reset();
        refreshData();
    }
}

async function handleBorrowBook(e) {
    e.preventDefault();
    const data = {
        bookId: document.getElementById('borrow-book-select').value,
        borrowerId: document.getElementById('borrower-select').value
    };

    const success = await postData('/loans', data, 'Enjoy your book!');
    if (success) {
        document.getElementById('borrow-form').reset();
        refreshData();
    }
}

async function handleReturn(loanId) {
    try {
        const res = await fetch(`${API_BASE}/loans/${loanId}/return`, { method: 'PUT' });
        if (res.ok) {
            showToast('Book returned to owner.', 'success');
            refreshData();
        } else {
            const err = await res.json();
            showToast(err.message, 'error');
        }
    } catch (e) {
        showToast('Network error while returning book', 'error');
    }
}

// --- Helpers ---

async function postData(path, data, successMsg) {
    try {
        const res = await fetch(`${API_BASE}${path}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (res.ok) {
            showToast(successMsg, 'success');
            return true;
        } else {
            const err = await res.json();
            showToast(err.message || 'Operation failed', 'error');
            return false;
        }
    } catch (e) {
        showToast('Network error', 'error');
        return false;
    }
}

function showToast(msg, type) {
    const toast = document.getElementById('toast');
    toast.innerText = msg;
    toast.className = `toast ${type}`;
    toast.classList.remove('hidden');

    setTimeout(() => {
        toast.classList.add('hidden');
    }, 4000);
}
