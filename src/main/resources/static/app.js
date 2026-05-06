document.addEventListener('DOMContentLoaded', () => {
    // Current State
    let books = [];
    let members = [];
    let loans = [];

    // Navigation logic
    const navLinks = document.querySelectorAll('.nav-links li');
    const sections = document.querySelectorAll('.content-section');

    window.switchSection = (sectionId) => {
        sections.forEach(s => s.classList.add('hidden'));
        navLinks.forEach(l => l.classList.remove('active'));

        document.getElementById(`${sectionId}-section`).classList.remove('hidden');
        const activeLink = document.querySelector(`[data-section="${sectionId}"]`);
        if (activeLink) activeLink.classList.add('active');

        loadData(); // Refresh data whenever we switch sections
    };

    navLinks.forEach(link => {
        link.addEventListener('click', () => {
            const sectionId = link.getAttribute('data-section');
            switchSection(sectionId);
        });
    });

    // Loading State
    const showLoading = (isLoading) => {
        const btn = document.querySelector('.nav-links li.active');
        if (btn) {
            btn.style.opacity = isLoading ? '0.5' : '1';
        }
    };

    // API Calls
    const fetchData = async (url) => {
        try {
            const response = await fetch(url);
            if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
            return await response.json();
        } catch (e) {
            showToast(e.message, 'error');
            return null;
        }
    };

    const loadData = async () => {
        showLoading(true);
        const booksData = await fetchData('/api/books');
        const membersData = await fetchData('/api/members');
        const loansData = await fetchData('/api/loans/active');

        if (booksData) books = booksData.content || [];
        if (membersData) members = Array.isArray(membersData) ? membersData : (membersData.content || []);
        if (loansData) loans = Array.isArray(loansData) ? loansData : (loansData.content || []);

        renderDashboard();
        renderBooks();
        renderMembers();
        renderLoans();
        updateDropdowns();
        showLoading(false);
    };

    // Rendering Logic
    const renderDashboard = () => {
        document.getElementById('stat-total-books').textContent = books.length;
        document.getElementById('stat-available-books').textContent = books.filter(b => b.available).length;
        document.getElementById('stat-borrowed-books').textContent = books.filter(b => !b.available).length;
        document.getElementById('stat-total-members').textContent = members.length;

        const activityList = document.getElementById('recent-activity-list');
        activityList.innerHTML = '';

        // Mock recent activity based on loans and new books
        const recentItems = [
            ...books.slice(-2).map(b => ({ text: `New book added: ${b.title}`, type: 'book' })),
            ...loans.slice(-3).map(l => ({ text: `${l.borrowerName} borrowed ${l.bookTitle}`, type: 'loan' }))
        ];

        if (recentItems.length === 0) {
            activityList.innerHTML = '<p class="text-muted">No recent activity found.</p>';
        } else {
            recentItems.forEach(item => {
                const div = document.createElement('div');
                div.className = 'activity-item';
                div.innerHTML = `<span>${item.type === 'book' ? '📖' : '🤝'}</span> <span>${item.text}</span>`;
                activityList.appendChild(div);
            });
        }
    };

    const renderBooks = () => {
        const grid = document.getElementById('book-grid');
        const searchTerm = document.getElementById('book-search').value.toLowerCase();
        const filterVal = document.getElementById('book-filter').value;

        const filtered = books.filter(b => {
            const matchesSearch = b.title.toLowerCase().includes(searchTerm) || b.author.toLowerCase().includes(searchTerm);
            const matchesFilter = filterVal === 'all' ||
                                (filterVal === 'available' && b.available) ||
                                (filterVal === 'borrowed' && !b.available);
            return matchesSearch && matchesFilter;
        });

        if (filtered.length === 0) {
            grid.innerHTML = '<div class="card" style="grid-column: 1/-1; text-align: center; color: var(--text-muted);">No books match your criteria. Try a different search or filter.</div>';
            return;
        }

        grid.innerHTML = filtered.map(book => `
            <div class="book-card">
                <span class="status-badge ${book.available ? 'status-available' : 'status-borrowed'}">
                    ${book.available ? 'Available' : 'Borrowed'}
                </span>
                <div class="item-title">${book.title}</div>
                <div class="item-subtitle">by ${book.author}</div>
                <div class="item-subtitle">Owned by: ${book.ownerName}</div>
                ${!book.available ? `<div class="item-subtitle">Current borrower: <strong>${book.borrowerName}</strong></div>` : ''}
            </div>
        `).join('');
    };

    const renderMembers = () => {
        const grid = document.getElementById('member-grid');
        if (members.length === 0) {
            grid.innerHTML = '<div class="card" style="grid-column: 1/-1; text-align: center; color: var(--text-muted);">No members found.</div>';
            return;
        }
        grid.innerHTML = members.map(m => `
            <div class="member-card">
                <div class="item-title">${m.name}</div>
                <div class="item-subtitle">${m.email}</div>
            </div>
        `).join('');
    };

    const renderLoans = () => {
        const container = document.getElementById('loan-list');
        if (loans.length === 0) {
            container.innerHTML = '<p class="text-muted">No active loans.</p>';
            return;
        }
        container.innerHTML = loans.map(loan => `
            <div class="loan-item">
                <div>
                    <strong>${loan.bookTitle}</strong><br>
                    <small>Borrowed by ${loan.borrowerName}</small>
                </div>
                <button class="return-btn" onclick="returnBook(${loan.loanId})">Return</button>
            </div>
        `).join('');
    };

    const updateDropdowns = () => {
        const ownerSelect = document.getElementById('book-owner-select');
        const borrowBookSelect = document.getElementById('borrow-book-select');
        const borrowerSelect = document.getElementById('borrower-select');

        const memberOptions = '<option value="" disabled selected>Select person...</option>' +
            members.map(m => `<option value="${m.memberId}">${m.name}</option>`).join('');

        ownerSelect.innerHTML = memberOptions;
        borrowerSelect.innerHTML = memberOptions;

        borrowBookSelect.innerHTML = '<option value="" disabled selected>Choose a book...</option>' +
            books.filter(b => b.available).map(b => `<option value="${b.bookId}">${b.title}</option>`).join('');
    };

    // Actions
    document.getElementById('person-form').onsubmit = async (e) => {
        e.preventDefault();
        const data = {
            name: document.getElementById('person-name').value,
            email: document.getElementById('person-email').value
        };
        const res = await fetch('/api/members', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        if (res.ok) {
            showToast('Member added successfully!', 'success');
            e.target.reset();
            loadData();
        } else {
            const err = await res.json();
            showToast(err.message || 'Failed to add member', 'error');
        }
    };

    document.getElementById('book-form').onsubmit = async (e) => {
        e.preventDefault();
        const data = {
            title: document.getElementById('book-title').value,
            author: document.getElementById('book-author').value,
            ownerId: document.getElementById('book-owner-select').value
        };
        const res = await fetch('/api/books', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        if (res.ok) {
            showToast('Book registered!', 'success');
            e.target.reset();
            loadData();
        } else {
            showToast('Failed to register book', 'error');
        }
    };

    document.getElementById('borrow-form').onsubmit = async (e) => {
        e.preventDefault();
        const data = {
            bookId: document.getElementById('borrow-book-select').value,
            borrowerId: document.getElementById('borrower-select').value
        };
        const res = await fetch('/api/loans', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        if (res.ok) {
            showToast('Loan confirmed!', 'success');
            e.target.reset();
            loadData();
        } else {
            const err = await res.json();
            showToast(err.message || 'Failed to create loan', 'error');
        }
    };

    window.returnBook = async (loanId) => {
        const res = await fetch(`/api/loans/${loanId}/return`, { method: 'PUT' });
        if (res.ok) {
            showToast('Book returned!', 'success');
            loadData();
        } else {
            showToast('Failed to return book', 'error');
        }
    };

    // Search and Filter Listeners
    document.getElementById('book-search').oninput = renderBooks;
    document.getElementById('book-filter').onchange = renderBooks;

    // Toast Utility
    const showToast = (msg, type = 'success') => {
        const toast = document.getElementById('toast');
        toast.textContent = msg;
        toast.className = `toast ${type}`;
        toast.classList.remove('hidden');
        setTimeout(() => toast.classList.add('hidden'), 3000);
    };

    // Initial Load
    loadData();
});
