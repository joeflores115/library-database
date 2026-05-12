document.addEventListener('DOMContentLoaded', () => {
    // Current State
    let books = [];
    let members = [];
    let loans = [];

    // --- Navigation & Modals ---
    const navLinks = document.querySelectorAll('.nav-links li');
    const sections = document.querySelectorAll('.content-section');

    window.switchSection = (sectionId) => {
        sections.forEach(s => s.classList.add('hidden'));
        navLinks.forEach(l => l.classList.remove('active'));

        const section = document.getElementById(`${sectionId}-section`);
        if (section) section.classList.remove('hidden');

        const activeLink = document.querySelector(`[data-section="${sectionId}"]`);
        if (activeLink) activeLink.classList.add('active');

        loadData();
    };

    navLinks.forEach(link => {
        link.addEventListener('click', () => {
            switchSection(link.getAttribute('data-section'));
        });
    });

    window.openModal = (modalId) => {
        document.getElementById('modal-overlay').classList.remove('hidden');
        document.getElementById(modalId).classList.remove('hidden');
        document.body.style.overflow = 'hidden'; // Prevent scroll
    };

    window.closeModal = (modalId) => {
        document.getElementById('modal-overlay').classList.add('hidden');
        document.getElementById(modalId).classList.add('hidden');
        document.body.style.overflow = '';
    };

    window.closeAllModals = () => {
        document.querySelectorAll('.modal').forEach(m => m.classList.add('hidden'));
        document.getElementById('modal-overlay').classList.add('hidden');
        document.body.style.overflow = '';
    };

    // --- API & Data ---
    const fetchData = async (url) => {
        try {
            const response = await fetch(url);
            if (!response.ok) throw new Error(`API Error: ${response.status}`);
            return await response.json();
        } catch (e) {
            showToast(e.message, 'error');
            return null;
        }
    };

    const loadData = async () => {
        showLoading(true);
        const [booksData, membersData, loansData] = await Promise.all([
            fetchData('/api/books'),
            fetchData('/api/members'),
            fetchData('/api/loans/active')
        ]);

        books = booksData?.content || [];
        members = Array.isArray(membersData) ? membersData : (membersData?.content || []);
        loans = Array.isArray(loansData) ? loansData : (loansData?.content || []);

        renderAll();
        showLoading(false);
    };

    const renderAll = () => {
        renderStats();
        renderActivity();
        renderBooks();
        renderMembers();
        renderLoans();
        updateDropdowns();
    };

    // --- Rendering ---
    const renderStats = () => {
        document.getElementById('stat-total-books').textContent = books.length;
        document.getElementById('stat-available-books').textContent = books.filter(b => b.available).length;
        document.getElementById('stat-borrowed-books').textContent = books.filter(b => !b.available).length;
        document.getElementById('stat-total-members').textContent = members.length;
    };

    const renderActivity = () => {
        const activityList = document.getElementById('recent-activity-list');
        const items = [
            ...books.slice(-3).map(b => ({
                text: `<strong>${b.title}</strong> was added to the library`,
                icon: '📖',
                ts: b.createdAt
            })),
            ...loans.slice(-3).map(l => ({
                text: `<strong>${l.borrowerName}</strong> borrowed <strong>${l.bookTitle}</strong>`,
                icon: '🤝',
                ts: l.loanDate
            }))
        ].sort((a, b) => new Date(b.ts) - new Date(a.ts)).slice(0, 5);

        if (items.length === 0) {
            activityList.innerHTML = '<p class="text-muted">No recent actions recorded.</p>';
            return;
        }

        activityList.innerHTML = items.map(item => `
            <div class="activity-item">
                <div class="activity-icon">${item.icon}</div>
                <div>${item.text}</div>
            </div>
        `).join('');
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
            grid.innerHTML = '<div class="card" style="grid-column: 1/-1; text-align: center; color: var(--text-muted);">No books found. Try adjusting your search.</div>';
            return;
        }

        grid.innerHTML = filtered.map(book => `
            <div class="book-card">
                <span class="status-badge ${book.available ? 'status-available' : 'status-borrowed'}">
                    ${book.available ? 'Available' : 'Borrowed'}
                </span>
                <div class="item-title">${book.title}</div>
                <div class="item-subtitle">by ${book.author}</div>
                <div class="item-subtitle">Owner: <strong>${book.ownerName}</strong></div>
                ${!book.available ? `<div class="item-subtitle">Currently with: <strong>${book.borrowerName}</strong></div>` : ''}
            </div>
        `).join('');
    };

    const renderMembers = () => {
        const grid = document.getElementById('member-grid');
        if (members.length === 0) {
            grid.innerHTML = '<div class="card" style="grid-column: 1/-1; text-align: center; color: var(--text-muted);">Invite your first member to get started!</div>';
            return;
        }
        grid.innerHTML = members.map(m => `
            <div class="book-card">
                <div class="item-title">${m.name}</div>
                <div class="item-subtitle">${m.email}</div>
            </div>
        `).join('');
    };

    const renderLoans = () => {
        const container = document.getElementById('loan-list');
        if (loans.length === 0) {
            container.innerHTML = '<p class="text-muted" style="text-align: center; padding: 2rem;">No active loans. Everything is in its place!</p>';
            return;
        }
        container.innerHTML = loans.map(loan => `
            <div class="activity-item" style="justify-content: space-between">
                <div style="display: flex; align-items: center; gap: 1rem;">
                    <div class="activity-icon">🤝</div>
                    <div>
                        <strong>${loan.bookTitle}</strong><br>
                        <small class="text-muted">Borrowed by ${loan.borrowerName}</small>
                    </div>
                </div>
                <button class="btn-sm" style="background: var(--warning)" onclick="returnBook(${loan.loanId})">Return Book</button>
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

        borrowBookSelect.innerHTML = '<option value="" disabled selected>Choose an available book...</option>' +
            books.filter(b => b.available).map(b => `<option value="${b.bookId}">${b.title}</option>`).join('');
    };

    // --- Actions ---
    const handleFormSubmit = async (formId, modalId, url, method, successMsg) => {
        const form = document.getElementById(formId);
        form.onsubmit = async (e) => {
            e.preventDefault();
            const formData = new FormData(form);
            const data = Object.fromEntries(formData.entries());

            // Special handling for selects since FormData might need manual mapping if IDs are needed
            if (formId === 'book-form') data.ownerId = document.getElementById('book-owner-select').value;
            if (formId === 'borrow-form') {
                data.bookId = document.getElementById('borrow-book-select').value;
                data.borrowerId = document.getElementById('borrower-select').value;
            }

            const res = await fetch(url, {
                method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });

            if (res.ok) {
                showToast(successMsg, 'success');
                form.reset();
                closeModal(modalId);
                loadData();
            } else {
                const err = await res.json();
                showToast(err.message || 'Action failed', 'error');
            }
        };
    };

    handleFormSubmit('person-form', 'member-modal', '/api/members', 'POST', 'Member invited!');
    handleFormSubmit('book-form', 'book-modal', '/api/books', 'POST', 'Book registered!');
    handleFormSubmit('borrow-form', 'borrow-modal', '/api/loans', 'POST', 'Loan created!');

    window.returnBook = async (loanId) => {
        const res = await fetch(`/api/loans/${loanId}/return`, { method: 'PUT' });
        if (res.ok) {
            showToast('Book returned!', 'success');
            loadData();
        } else {
            showToast('Failed to return book', 'error');
        }
    };

    // --- Utils ---
    const showLoading = (isLoading) => {
        const main = document.querySelector('.main-content');
        main.style.opacity = isLoading ? '0.6' : '1';
        main.style.pointerEvents = isLoading ? 'none' : 'auto';
    };

    const showToast = (msg, type = 'success') => {
        const toast = document.getElementById('toast');
        toast.textContent = msg;
        toast.className = `toast ${type}`;
        toast.classList.remove('hidden');
        setTimeout(() => toast.classList.add('hidden'), 3000);
    };

    document.getElementById('book-search').oninput = renderBooks;
    document.getElementById('book-filter').onchange = renderBooks;

    // Initial Load
    loadData();
});
