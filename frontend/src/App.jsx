import React, { useState, useEffect } from 'react';
import {
  BookOpen, Search, Plus, Trash2, Edit3, Sparkles, CheckCircle,
  AlertCircle, Bookmark, RefreshCw, X, Eye, FileText, ArrowRight,
  TrendingUp, Star, Layers, Calendar, User, Clock, Terminal, Check
} from 'lucide-react';

// Initial seed books matching data/backup.txt & sql/sample_data.sql
const INITIAL_BOOKS = [
  {
    id: 1,
    title: "Clean Code: A Handbook of Agile Software Craftsmanship",
    author: "Robert C. Martin",
    category: "Computer Science",
    publicationYear: 2008,
    availableCopies: 4,
    totalCopies: 5,
    isbn: "978-0132350884",
    pages: 464,
    publisher: "Prentice Hall",
    rating: 4.8,
    summary: "Even bad code can function. But if code isn't clean, it can bring a development organization to its knees.",
    coverImageUrl: "https://images.unsplash.com/photo-1532012164546-f432f2e3edd4?w=500&auto=format&fit=crop",
    tags: "Software,Best Practice,Clean Code",
    ebook: false
  },
  {
    id: 2,
    title: "Effective Java (3rd Edition)",
    author: "Joshua Bloch",
    category: "Computer Science",
    publicationYear: 2018,
    availableCopies: 3,
    totalCopies: 3,
    isbn: "978-0134685991",
    pages: 412,
    publisher: "Addison-Wesley",
    rating: 4.9,
    summary: "The definitive guide to best practices in the Java programming language.",
    coverImageUrl: "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=500&auto=format&fit=crop",
    tags: "Java,Architecture,Design Patterns",
    ebook: false
  },
  {
    id: 3,
    title: "Dune",
    author: "Frank Herbert",
    category: "Science Fiction",
    publicationYear: 1965,
    availableCopies: 5,
    totalCopies: 6,
    isbn: "978-0441013593",
    pages: 896,
    publisher: "Ace Books",
    rating: 4.7,
    summary: "Set on the desert planet Arrakis, Dune is the story of the boy Paul Atreides, heir to a noble family.",
    coverImageUrl: "https://images.unsplash.com/photo-1506466010722-395aa2bef877?w=500&auto=format&fit=crop",
    tags: "Sci-Fi,Epic,Space Opera",
    ebook: false
  },
  {
    id: 4,
    title: "Pride and Prejudice",
    author: "Jane Austen",
    category: "Classic Literature",
    publicationYear: 1813,
    availableCopies: 2,
    totalCopies: 4,
    isbn: "978-0141439518",
    pages: 279,
    publisher: "Penguin Classics",
    rating: 4.6,
    summary: "A romantic novel that charts the emotional development of the protagonist Elizabeth Bennet.",
    coverImageUrl: "https://images.unsplash.com/photo-1476275466078-4007374efbbe?w=500&auto=format&fit=crop",
    tags: "Classic,Romance,Literature",
    ebook: true,
    fileSizeMb: 2.4,
    downloadFormat: "EPUB"
  },
  {
    id: 5,
    title: "Thinking, Fast and Slow",
    author: "Daniel Kahneman",
    category: "Psychology & Mind",
    publicationYear: 2011,
    availableCopies: 3,
    totalCopies: 5,
    isbn: "978-0374533557",
    pages: 499,
    publisher: "Farrar Straus",
    rating: 4.5,
    summary: "A tour de force on how our minds make choices and decisions by Nobel laureate Daniel Kahneman.",
    coverImageUrl: "https://images.unsplash.com/photo-1497633762265-9d179a990aa6?w=500&auto=format&fit=crop",
    tags: "Psychology,Cognition,Decision Making",
    ebook: true,
    fileSizeMb: 5.1,
    downloadFormat: "PDF"
  },
  {
    id: 6,
    title: "Designing Data-Intensive Applications",
    author: "Martin Kleppmann",
    category: "Computer Science",
    publicationYear: 2017,
    availableCopies: 2,
    totalCopies: 3,
    isbn: "978-1449370756",
    pages: 616,
    publisher: "O'Reilly Media",
    rating: 4.9,
    summary: "The big ideas behind reliable, scalable, and maintainable systems.",
    coverImageUrl: "https://images.unsplash.com/photo-1512820790803-83ca734da794?w=500&auto=format&fit=crop",
    tags: "System Design,Distributed Systems,Database",
    ebook: false
  },
  {
    id: 7,
    title: "Sapiens: A Brief History of Humankind",
    author: "Yuval Noah Harari",
    category: "Classic Literature",
    publicationYear: 2015,
    availableCopies: 6,
    totalCopies: 8,
    isbn: "978-0062316097",
    pages: 464,
    publisher: "Harper",
    rating: 4.7,
    summary: "Explores how biology and history have defined us and enhanced our understanding of what it means to be human.",
    coverImageUrl: "https://images.unsplash.com/photo-1457369804613-52c61a468e7d?w=500&auto=format&fit=crop",
    tags: "History,Anthropology,Science",
    ebook: true,
    fileSizeMb: 8.2,
    downloadFormat: "PDF"
  },
  {
    id: 8,
    title: "Introduction to Algorithms (CLRS)",
    author: "Thomas H. Cormen",
    category: "Computer Science",
    publicationYear: 2009,
    availableCopies: 1,
    totalCopies: 2,
    isbn: "978-0262033848",
    pages: 1312,
    publisher: "MIT Press",
    rating: 4.6,
    summary: "A comprehensive textbook covering modern algorithms and data structures in depth.",
    coverImageUrl: "https://images.unsplash.com/photo-1516979187457-637abb4f9353?w=500&auto=format&fit=crop",
    tags: "Algorithms,Data Structures,Academic",
    ebook: false
  }
];

const INITIAL_LOANS = [
  {
    recordId: 101,
    bookId: 1,
    bookTitle: "Clean Code: A Handbook of Agile Software Craftsmanship",
    userName: "Aarav Patel",
    borrowDate: "2026-09-18",
    dueDate: "2026-10-02",
    status: "OVERDUE",
    fineAmount: 1.00
  },
  {
    recordId: 102,
    bookId: 3,
    bookTitle: "Dune",
    userName: "Priya Sharma",
    borrowDate: "2026-09-29",
    dueDate: "2026-10-13",
    status: "BORROWED",
    fineAmount: 0.00
  }
];

export default function App() {
  const [books, setBooks] = useState(INITIAL_BOOKS);
  const [loans, setLoans] = useState(INITIAL_LOANS);
  const [activeTab, setActiveTab] = useState('discovery'); // 'discovery', 'catalog', 'loans', 'review2'
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('All');
  const [viewMode, setViewMode] = useState('grid'); // 'grid' or 'table'
  const [backendOnline, setBackendOnline] = useState(false);
  const [toastMessage, setToastMessage] = useState(null);

  // Modals
  const [selectedBook, setSelectedBook] = useState(null);
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [editingBook, setEditingBook] = useState(null);
  const [borrowModalBook, setBorrowModalBook] = useState(null);
  const [borrowerName, setBorrowerName] = useState('');

  // Form State for Add / Edit
  const [formData, setFormData] = useState({
    title: '',
    author: '',
    category: 'Computer Science',
    publicationYear: 2024,
    availableCopies: 3,
    totalCopies: 3,
    isbn: '',
    pages: 350,
    publisher: 'Academic Press',
    rating: 4.5,
    summary: '',
    coverImageUrl: '',
    tags: 'Featured,Recommended',
    ebook: false,
    fileSizeMb: 3.5,
    downloadFormat: 'EPUB'
  });

  // Check Backend Status & Sync
  const checkBackend = async () => {
    try {
      const res = await fetch('/api/books');
      if (res.ok) {
        const data = await res.json();
        if (Array.isArray(data) && data.length > 0) {
          setBooks(data);
        }
        setBackendOnline(true);
      } else {
        setBackendOnline(false);
      }
    } catch (err) {
      setBackendOnline(false);
    }
  };

  useEffect(() => {
    checkBackend();
    const interval = setInterval(checkBackend, 5000);
    return () => clearInterval(interval);
  }, []);

  const showToast = (msg) => {
    setToastMessage(msg);
    setTimeout(() => setToastMessage(null), 3500);
  };

  // Categories list
  const categories = ['All', 'Computer Science', 'Science Fiction', 'Classic Literature', 'Psychology & Mind'];

  // Filtered books
  const filteredBooks = books.filter(b => {
    const matchesCat = selectedCategory === 'All' || (b.category && b.category.toLowerCase() === selectedCategory.toLowerCase());
    const q = searchQuery.toLowerCase().trim();
    const matchesQuery = !q ||
      b.title.toLowerCase().includes(q) ||
      b.author.toLowerCase().includes(q) ||
      (b.isbn && b.isbn.toLowerCase().includes(q)) ||
      (b.tags && b.tags.toLowerCase().includes(q));
    return matchesCat && matchesQuery;
  });

  // Functional: Add Record
  const handleAddBook = async (e) => {
    e.preventDefault();
    const localId = Math.floor(Date.now() % 1000000);
    const bookPayload = {
      ...formData,
      id: 0,
      coverImageUrl: formData.coverImageUrl || "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=500&auto=format&fit=crop"
    };

    if (backendOnline) {
      try {
        const res = await fetch('/api/books', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(bookPayload)
        });
        if (res.ok) {
          const saved = await res.json();
          setBooks(prev => [saved, ...prev]);
        } else {
          setBooks(prev => [{ ...bookPayload, id: localId }, ...prev]);
        }
      } catch (err) {
        setBooks(prev => [{ ...bookPayload, id: localId }, ...prev]);
      }
    } else {
      setBooks(prev => [{ ...bookPayload, id: localId }, ...prev]);
    }

    setIsAddModalOpen(false);
    showToast(`"${bookPayload.title}" added to catalog!`);
    resetForm();
  };

  // Functional: Update Record
  const handleUpdateBook = async (e) => {
    e.preventDefault();
    if (!editingBook) return;

    const updated = { ...editingBook, ...formData };

    if (backendOnline) {
      try {
        await fetch(`/api/books/${editingBook.id}`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(updated)
        });
      } catch (err) {
        console.error(err);
      }
    }

    setBooks(prev => prev.map(b => b.id === editingBook.id ? updated : b));
    setEditingBook(null);
    showToast(`Updated "${updated.title}" successfully.`);
    resetForm();
  };

  // Functional: Delete Record
  const handleDeleteBook = async (id, title) => {
    if (confirm(`Are you sure you want to remove "${title}" from the catalog?`)) {
      if (backendOnline) {
        try {
          await fetch(`/api/books/${id}`, { method: 'DELETE' });
        } catch (err) {
          console.error(err);
        }
      }
      setBooks(prev => prev.filter(b => b.id !== id));
      if (selectedBook && selectedBook.id === id) setSelectedBook(null);
      showToast(`Removed "${title}" from library.`);
    }
  };

  // Borrow Action
  const handleBorrowConfirm = () => {
    if (!borrowModalBook) return;
    const name = borrowerName.trim() || 'Student Reader';

    if (borrowModalBook.availableCopies <= 0) {
      alert("No copies currently available!");
      return;
    }

    // Decrement available copies
    setBooks(prev => prev.map(b => {
      if (b.id === borrowModalBook.id) {
        return { ...b, availableCopies: Math.max(0, b.availableCopies - 1) };
      }
      return b;
    }));

    const newLoan = {
      recordId: Date.now(),
      bookId: borrowModalBook.id,
      bookTitle: borrowModalBook.title,
      userName: name,
      borrowDate: new Date().toISOString().split('T')[0],
      dueDate: new Date(Date.now() + 14 * 86400000).toISOString().split('T')[0],
      status: 'BORROWED',
      fineAmount: 0.0
    };

    setLoans(prev => [newLoan, ...prev]);
    showToast(`Checked out "${borrowModalBook.title}" to ${name}!`);
    setBorrowModalBook(null);
    setBorrowerName('');
  };

  // Return Action
  const handleReturnBook = (recordId, bookId, bookTitle) => {
    setLoans(prev => prev.map(l => {
      if (l.recordId === recordId) {
        return { ...l, status: 'RETURNED' };
      }
      return l;
    }));

    setBooks(prev => prev.map(b => {
      if (b.id === bookId) {
        return { ...b, availableCopies: Math.min(b.totalCopies, b.availableCopies + 1) };
      }
      return b;
    }));

    showToast(`Returned "${bookTitle}". Thank you!`);
  };

  const openEditModal = (book) => {
    setEditingBook(book);
    setFormData({
      title: book.title,
      author: book.author,
      category: book.category,
      publicationYear: book.publicationYear,
      availableCopies: book.availableCopies,
      totalCopies: book.totalCopies,
      isbn: book.isbn || '',
      pages: book.pages || 300,
      publisher: book.publisher || '',
      rating: book.rating || 4.5,
      summary: book.summary || '',
      coverImageUrl: book.coverImageUrl || '',
      tags: book.tags || '',
      ebook: !!book.ebook,
      fileSizeMb: book.fileSizeMb || 2.5,
      downloadFormat: book.downloadFormat || 'EPUB'
    });
  };

  const resetForm = () => {
    setFormData({
      title: '',
      author: '',
      category: 'Computer Science',
      publicationYear: 2024,
      availableCopies: 3,
      totalCopies: 3,
      isbn: '',
      pages: 350,
      publisher: 'Academic Press',
      rating: 4.5,
      summary: '',
      coverImageUrl: '',
      tags: 'Featured,Recommended',
      ebook: false,
      fileSizeMb: 3.5,
      downloadFormat: 'EPUB'
    });
  };

  // Calculate quick stats
  const totalTitles = books.length;
  const totalAvailable = books.reduce((acc, b) => acc + (b.availableCopies || 0), 0);
  const activeLoansCount = loans.filter(l => l.status === 'BORROWED' || l.status === 'OVERDUE').length;
  const totalEbooks = books.filter(b => b.ebook).length;

  return (
    <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
      
      {/* Toast Notification */}
      {toastMessage && (
        <div style={{
          position: 'fixed',
          top: 24,
          right: 24,
          zIndex: 9999,
          background: 'linear-gradient(135deg, #10b981 0%, #059669 100%)',
          color: '#ffffff',
          padding: '12px 24px',
          borderRadius: 'var(--radius-md)',
          boxShadow: '0 10px 25px rgba(16, 185, 129, 0.4)',
          fontWeight: 600,
          display: 'flex',
          alignItems: 'center',
          gap: 10,
          animation: 'fadeIn 0.3s ease-out'
        }}>
          <CheckCircle size={18} />
          {toastMessage}
        </div>
      )}

      {/* TOP NAVBAR */}
      <header style={{
        position: 'sticky',
        top: 0,
        zIndex: 50,
        borderBottom: '1px solid var(--border-subtle)',
        background: 'rgba(9, 13, 22, 0.85)',
        backdropFilter: 'blur(16px)',
        padding: '14px 32px'
      }}>
        <div style={{
          maxWidth: 1360,
          margin: '0 auto',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          flexWrap: 'wrap',
          gap: 16
        }}>
          
          {/* Logo & System Brand */}
          <div style={{ display: 'flex', alignItems: 'center', gap: 14 }}>
            <div style={{
              width: 44,
              height: 44,
              borderRadius: 'var(--radius-md)',
              background: 'linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              boxShadow: '0 4px 20px rgba(99, 102, 241, 0.4)'
            }}>
              <BookOpen size={24} color="#ffffff" />
            </div>
            <div>
              <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <h1 style={{ fontSize: '1.35rem', fontWeight: 800, letterSpacing: '-0.02em', margin: 0 }}>
                  Book<span style={{ color: '#818cf8' }}>Sphere</span>
                </h1>
                <span className="badge badge-purple" style={{ fontSize: '0.65rem' }}>v2.0</span>
              </div>
              <p style={{ fontSize: '0.78rem', color: 'var(--text-muted)', margin: 0 }}>
                Digital Library Management & Book Discovery System
              </p>
            </div>
          </div>

          {/* Navigation Pill Tabs */}
          <nav style={{
            display: 'flex',
            alignItems: 'center',
            background: 'rgba(15, 23, 42, 0.8)',
            border: '1px solid var(--border-subtle)',
            borderRadius: 9999,
            padding: 4,
            gap: 4
          }}>
            <button
              onClick={() => setActiveTab('discovery')}
              style={{
                display: 'flex',
                alignItems: 'center',
                gap: 6,
                padding: '8px 16px',
                borderRadius: 9999,
                fontSize: '0.85rem',
                fontWeight: 600,
                border: 'none',
                cursor: 'pointer',
                background: activeTab === 'discovery' ? 'var(--primary)' : 'transparent',
                color: activeTab === 'discovery' ? '#ffffff' : 'var(--text-muted)',
                transition: 'all 0.2s'
              }}
            >
              <Sparkles size={16} /> Discovery
            </button>

            <button
              onClick={() => setActiveTab('catalog')}
              style={{
                display: 'flex',
                alignItems: 'center',
                gap: 6,
                padding: '8px 16px',
                borderRadius: 9999,
                fontSize: '0.85rem',
                fontWeight: 600,
                border: 'none',
                cursor: 'pointer',
                background: activeTab === 'catalog' ? 'var(--primary)' : 'transparent',
                color: activeTab === 'catalog' ? '#ffffff' : 'var(--text-muted)',
                transition: 'all 0.2s'
              }}
            >
              <Layers size={16} /> Catalog ({books.length})
            </button>

            <button
              onClick={() => setActiveTab('loans')}
              style={{
                display: 'flex',
                alignItems: 'center',
                gap: 6,
                padding: '8px 16px',
                borderRadius: 9999,
                fontSize: '0.85rem',
                fontWeight: 600,
                border: 'none',
                cursor: 'pointer',
                background: activeTab === 'loans' ? 'var(--primary)' : 'transparent',
                color: activeTab === 'loans' ? '#ffffff' : 'var(--text-muted)',
                transition: 'all 0.2s'
              }}
            >
              <Bookmark size={16} /> Loans ({activeLoansCount})
            </button>
          </nav>

          {/* Right Action: Add Book & Backend Status */}
          <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
            <div style={{
              display: 'flex',
              alignItems: 'center',
              gap: 8,
              padding: '6px 12px',
              borderRadius: 9999,
              background: backendOnline ? 'rgba(16, 185, 129, 0.12)' : 'rgba(245, 158, 11, 0.12)',
              border: `1px solid ${backendOnline ? 'rgba(16, 185, 129, 0.3)' : 'rgba(245, 158, 11, 0.3)'}`,
              fontSize: '0.78rem',
              fontWeight: 500,
              color: backendOnline ? '#6ee7b7' : '#fcd34d'
            }}>
              <div className="pulsing-dot" style={{ backgroundColor: backendOnline ? '#10b981' : '#f59e0b' }} />
              <span>{backendOnline ? 'Spring Boot API Active (:8080)' : 'Local File Persistence Mode'}</span>
            </div>

            <button
              onClick={() => { resetForm(); setIsAddModalOpen(true); }}
              className="btn btn-primary btn-sm"
              style={{ display: 'flex', alignItems: 'center', gap: 6 }}
            >
              <Plus size={16} /> Add Book
            </button>
          </div>
        </div>
      </header>

      {/* MAIN CONTAINER */}
      <main style={{ maxWidth: 1360, margin: '0 auto', padding: '32px 24px', flex: 1, width: '100%' }}>

        {/* HERO RIBBON: SEARCH & METRICS */}
        <section style={{ marginBottom: 36 }}>
          <div className="glass-panel" style={{
            padding: '32px',
            background: 'linear-gradient(135deg, rgba(15, 23, 42, 0.9) 0%, rgba(30, 27, 75, 0.4) 100%)',
            border: '1px solid rgba(99, 102, 241, 0.25)',
            position: 'relative',
            overflow: 'hidden'
          }}>
            <div style={{ maxWidth: 760, position: 'relative', zIndex: 2 }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 8, marginBottom: 8 }}>
                <span className="badge badge-cyan">Java 21 • Spring Boot • React</span>
                <span className="badge badge-emerald">Console & Web Dual-Mode</span>
              </div>
              <h2 style={{ fontSize: '2.4rem', fontWeight: 800, lineHeight: 1.15, marginBottom: 12 }}>
                Discover, Manage & Borrow From The <span style={{ background: 'linear-gradient(90deg, #818cf8, #c084fc)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent' }}>Digital Library</span>
              </h2>
              <p style={{ color: 'var(--text-muted)', fontSize: '1rem', marginBottom: 24, lineHeight: 1.6 }}>
                Unified library system supporting physical volumes and digital e-books. Powered by background indexing threads, robust object-oriented architecture, and persistent audit logging.
              </p>

              {/* Search Bar Input */}
              <div style={{
                display: 'flex',
                alignItems: 'center',
                background: 'rgba(9, 13, 22, 0.85)',
                border: '1px solid rgba(255, 255, 255, 0.15)',
                borderRadius: 'var(--radius-lg)',
                padding: '6px 14px',
                boxShadow: '0 8px 30px rgba(0, 0, 0, 0.4)'
              }}>
                <Search size={20} color="var(--primary-light)" style={{ marginRight: 10 }} />
                <input
                  type="text"
                  placeholder="Search by Title, Author, Category, ISBN or Tags..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  style={{
                    background: 'transparent',
                    border: 'none',
                    outline: 'none',
                    color: '#ffffff',
                    fontSize: '0.95rem',
                    flex: 1,
                    padding: '8px 0'
                  }}
                />
                {searchQuery && (
                  <button
                    onClick={() => setSearchQuery('')}
                    style={{ background: 'transparent', border: 'none', color: 'var(--text-dim)', cursor: 'pointer' }}
                  >
                    <X size={18} />
                  </button>
                )}
              </div>
            </div>

            {/* Quick Metrics Cards */}
            <div style={{
              display: 'grid',
              gridTemplateColumns: 'repeat(auto-fit, minmax(160px, 1fr))',
              gap: 16,
              marginTop: 28,
              borderTop: '1px solid rgba(255, 255, 255, 0.08)',
              paddingTop: 24
            }}>
              <div style={{ background: 'rgba(255, 255, 255, 0.03)', padding: '14px 18px', borderRadius: 'var(--radius-md)' }}>
                <div style={{ fontSize: '0.78rem', color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.05em' }}>Unique Titles</div>
                <div style={{ fontSize: '1.75rem', fontWeight: 700, color: '#ffffff' }}>{totalTitles}</div>
              </div>
              <div style={{ background: 'rgba(255, 255, 255, 0.03)', padding: '14px 18px', borderRadius: 'var(--radius-md)' }}>
                <div style={{ fontSize: '0.78rem', color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.05em' }}>Available Copies</div>
                <div style={{ fontSize: '1.75rem', fontWeight: 700, color: '#10b981' }}>{totalAvailable}</div>
              </div>
              <div style={{ background: 'rgba(255, 255, 255, 0.03)', padding: '14px 18px', borderRadius: 'var(--radius-md)' }}>
                <div style={{ fontSize: '0.78rem', color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.05em' }}>Active Loans</div>
                <div style={{ fontSize: '1.75rem', fontWeight: 700, color: '#f59e0b' }}>{activeLoansCount}</div>
              </div>
              <div style={{ background: 'rgba(255, 255, 255, 0.03)', padding: '14px 18px', borderRadius: 'var(--radius-md)' }}>
                <div style={{ fontSize: '0.78rem', color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.05em' }}>Digital E-Books</div>
                <div style={{ fontSize: '1.75rem', fontWeight: 700, color: '#06b6d4' }}>{totalEbooks}</div>
              </div>
            </div>
          </div>
        </section>

        {/* TAB 1: DISCOVERY FEED */}
        {activeTab === 'discovery' && (
          <div>
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 20 }}>
              <div>
                <h3 style={{ fontSize: '1.5rem', fontWeight: 700 }}>Intelligent Book Discovery</h3>
                <p style={{ fontSize: '0.85rem', color: 'var(--text-muted)' }}>
                  Computed asynchronously using background thread workers for instantaneous recommendations
                </p>
              </div>
              <div style={{ display: 'flex', gap: 8 }}>
                <span className="badge badge-purple"><TrendingUp size={14} /> High Reader Satisfaction</span>
              </div>
            </div>

            {/* Trending Cards Row */}
            <div style={{
              display: 'grid',
              gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))',
              gap: 24,
              marginBottom: 40
            }}>
              {books.filter(b => b.rating >= 4.7).map(book => (
                <div key={book.id} className="glass-panel" style={{
                  overflow: 'hidden',
                  transition: 'transform 0.2s, border-color 0.2s',
                  display: 'flex',
                  flexDirection: 'column'
                }}>
                  <div style={{ height: 160, position: 'relative', overflow: 'hidden' }}>
                    <img
                      src={book.coverImageUrl}
                      alt={book.title}
                      style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                    />
                    <div style={{
                      position: 'absolute',
                      inset: 0,
                      background: 'linear-gradient(to top, rgba(15, 23, 42, 1) 0%, transparent 70%)'
                    }} />
                    <div style={{ position: 'absolute', top: 12, right: 12 }}>
                      <span className="badge badge-amber"><Star size={12} fill="#f59e0b" /> {book.rating}</span>
                    </div>
                    {book.ebook && (
                      <div style={{ position: 'absolute', top: 12, left: 12 }}>
                        <span className="badge badge-cyan">E-Book ({book.downloadFormat})</span>
                      </div>
                    )}
                  </div>

                  <div style={{ padding: '18px', flex: 1, display: 'flex', flexDirection: 'column' }}>
                    <span style={{ fontSize: '0.75rem', color: 'var(--primary-light)', fontWeight: 600, textTransform: 'uppercase' }}>
                      {book.category}
                    </span>
                    <h4 style={{ fontSize: '1.1rem', fontWeight: 700, margin: '6px 0', lineHeight: 1.3 }}>
                      {book.title}
                    </h4>
                    <p style={{ fontSize: '0.85rem', color: 'var(--text-muted)', marginBottom: 12 }}>
                      by {book.author} • {book.publicationYear}
                    </p>
                    <p style={{
                      fontSize: '0.8rem',
                      color: 'var(--text-dim)',
                      flex: 1,
                      overflow: 'hidden',
                      display: '-webkit-box',
                      WebkitLineClamp: 3,
                      WebkitBoxOrient: 'vertical',
                      marginBottom: 16
                    }}>
                      {book.summary}
                    </p>

                    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', borderTop: '1px solid var(--border-subtle)', paddingTop: 14 }}>
                      <span style={{ fontSize: '0.8rem', color: book.availableCopies > 0 ? '#10b981' : '#f43f5e' }}>
                        {book.availableCopies > 0 ? `${book.availableCopies} available` : 'Checked out'}
                      </span>
                      <div style={{ display: 'flex', gap: 6 }}>
                        <button
                          onClick={() => setSelectedBook(book)}
                          className="btn btn-secondary btn-sm"
                        >
                          <Eye size={14} /> Preview
                        </button>
                        <button
                          onClick={() => setBorrowModalBook(book)}
                          disabled={book.availableCopies <= 0}
                          className="btn btn-primary btn-sm"
                        >
                          Borrow
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              ))}
            </div>

            {/* Curated Categories Exploration */}
            <h3 style={{ fontSize: '1.3rem', fontWeight: 700, marginBottom: 16 }}>Explore By Topic</h3>
            <div style={{ display: 'flex', gap: 10, flexWrap: 'wrap', marginBottom: 32 }}>
              {categories.map(cat => (
                <button
                  key={cat}
                  onClick={() => { setSelectedCategory(cat); setActiveTab('catalog'); }}
                  style={{
                    padding: '10px 18px',
                    borderRadius: 'var(--radius-md)',
                    border: '1px solid var(--border-subtle)',
                    background: 'rgba(255, 255, 255, 0.04)',
                    color: '#ffffff',
                    fontWeight: 500,
                    cursor: 'pointer',
                    display: 'flex',
                    alignItems: 'center',
                    gap: 8,
                    transition: 'all 0.2s'
                  }}
                >
                  <BookOpen size={16} color="var(--primary-light)" />
                  {cat}
                </button>
              ))}
            </div>
          </div>
        )}

        {/* TAB 2: LIBRARY CATALOG (CRUD) */}
        {activeTab === 'catalog' && (
          <div>
            {/* Category Filter Chips & Controls */}
            <div style={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'space-between',
              flexWrap: 'wrap',
              gap: 16,
              marginBottom: 24
            }}>
              <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap' }}>
                {categories.map(cat => (
                  <button
                    key={cat}
                    onClick={() => setSelectedCategory(cat)}
                    style={{
                      padding: '6px 14px',
                      borderRadius: 9999,
                      fontSize: '0.82rem',
                      fontWeight: 600,
                      border: '1px solid',
                      borderColor: selectedCategory === cat ? 'var(--primary)' : 'var(--border-subtle)',
                      background: selectedCategory === cat ? 'var(--primary)' : 'rgba(255, 255, 255, 0.04)',
                      color: selectedCategory === cat ? '#ffffff' : 'var(--text-muted)',
                      cursor: 'pointer'
                    }}
                  >
                    {cat}
                  </button>
                ))}
              </div>

              <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
                <span style={{ fontSize: '0.85rem', color: 'var(--text-dim)' }}>
                  Showing {filteredBooks.length} of {books.length} titles
                </span>
                <div style={{ display: 'flex', background: 'rgba(255, 255, 255, 0.05)', borderRadius: 'var(--radius-sm)', padding: 2 }}>
                  <button
                    onClick={() => setViewMode('grid')}
                    style={{
                      padding: '6px 10px',
                      background: viewMode === 'grid' ? 'var(--primary)' : 'transparent',
                      border: 'none',
                      borderRadius: 'var(--radius-sm)',
                      color: '#ffffff',
                      cursor: 'pointer'
                    }}
                  >
                    Grid
                  </button>
                  <button
                    onClick={() => setViewMode('table')}
                    style={{
                      padding: '6px 10px',
                      background: viewMode === 'table' ? 'var(--primary)' : 'transparent',
                      border: 'none',
                      borderRadius: 'var(--radius-sm)',
                      color: '#ffffff',
                      cursor: 'pointer'
                    }}
                  >
                    Table
                  </button>
                </div>
              </div>
            </div>

            {/* CATALOG GRID VIEW */}
            {viewMode === 'grid' ? (
              <div style={{
                display: 'grid',
                gridTemplateColumns: 'repeat(auto-fill, minmax(290px, 1fr))',
                gap: 24
              }}>
                {filteredBooks.map(book => (
                  <div key={book.id} className="glass-panel" style={{
                    display: 'flex',
                    flexDirection: 'column',
                    overflow: 'hidden',
                    border: '1px solid var(--border-subtle)'
                  }}>
                    <div style={{ height: 180, position: 'relative' }}>
                      <img
                        src={book.coverImageUrl}
                        alt={book.title}
                        style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                      />
                      <div style={{
                        position: 'absolute',
                        inset: 0,
                        background: 'linear-gradient(to top, rgba(15, 23, 42, 0.95) 0%, rgba(15, 23, 42, 0.1) 80%)'
                      }} />
                      <div style={{ position: 'absolute', top: 10, left: 10 }}>
                        <span className={`badge ${book.ebook ? 'badge-cyan' : 'badge-purple'}`}>
                          {book.ebook ? `E-Book (${book.downloadFormat})` : 'Physical'}
                        </span>
                      </div>
                      <div style={{ position: 'absolute', top: 10, right: 10 }}>
                        <span className="badge badge-amber"><Star size={12} fill="#f59e0b" /> {book.rating}</span>
                      </div>
                    </div>

                    <div style={{ padding: 20, flex: 1, display: 'flex', flexDirection: 'column' }}>
                      <span style={{ fontSize: '0.75rem', color: 'var(--primary-light)', fontWeight: 600 }}>
                        {book.category}
                      </span>
                      <h4 style={{ fontSize: '1.1rem', fontWeight: 700, margin: '6px 0 4px', lineHeight: 1.3 }}>
                        {book.title}
                      </h4>
                      <p style={{ fontSize: '0.85rem', color: 'var(--text-muted)', marginBottom: 12 }}>
                        by {book.author} ({book.publicationYear})
                      </p>

                      <div style={{
                        background: 'rgba(255, 255, 255, 0.03)',
                        borderRadius: 'var(--radius-sm)',
                        padding: '8px 12px',
                        fontSize: '0.78rem',
                        color: 'var(--text-muted)',
                        marginBottom: 16
                      }}>
                        <div><strong>ISBN:</strong> {book.isbn}</div>
                        <div><strong>Copies:</strong> {book.availableCopies} available of {book.totalCopies}</div>
                      </div>

                      <div style={{ display: 'flex', gap: 6, marginTop: 'auto' }}>
                        <button
                          onClick={() => setSelectedBook(book)}
                          className="btn btn-secondary btn-sm"
                          style={{ flex: 1 }}
                        >
                          <Eye size={14} /> Preview
                        </button>
                        <button
                          onClick={() => openEditModal(book)}
                          className="btn btn-secondary btn-sm"
                          title="Update Record"
                        >
                          <Edit3 size={14} />
                        </button>
                        <button
                          onClick={() => handleDeleteBook(book.id, book.title)}
                          className="btn btn-danger btn-sm"
                          title="Delete Record"
                        >
                          <Trash2 size={14} />
                        </button>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              /* CATALOG TABLE VIEW (For Admin review) */
              <div className="glass-panel" style={{ overflowX: 'auto', padding: 16 }}>
                <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left', fontSize: '0.88rem' }}>
                  <thead>
                    <tr style={{ borderBottom: '1px solid var(--border-subtle)', color: 'var(--text-muted)' }}>
                      <th style={{ padding: '12px 16px' }}>ID</th>
                      <th style={{ padding: '12px 16px' }}>Title & Author</th>
                      <th style={{ padding: '12px 16px' }}>Category</th>
                      <th style={{ padding: '12px 16px' }}>ISBN</th>
                      <th style={{ padding: '12px 16px' }}>Type</th>
                      <th style={{ padding: '12px 16px' }}>Copies</th>
                      <th style={{ padding: '12px 16px' }}>Rating</th>
                      <th style={{ padding: '12px 16px', textAlign: 'right' }}>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredBooks.map(book => (
                      <tr key={book.id} style={{ borderBottom: '1px solid rgba(255, 255, 255, 0.04)' }}>
                        <td style={{ padding: '12px 16px', fontWeight: 600 }}>#{book.id}</td>
                        <td style={{ padding: '12px 16px' }}>
                          <div style={{ fontWeight: 600, color: '#ffffff' }}>{book.title}</div>
                          <div style={{ fontSize: '0.78rem', color: 'var(--text-muted)' }}>by {book.author}</div>
                        </td>
                        <td style={{ padding: '12px 16px' }}>
                          <span className="badge badge-purple" style={{ fontSize: '0.7rem' }}>{book.category}</span>
                        </td>
                        <td style={{ padding: '12px 16px', fontFamily: 'monospace', fontSize: '0.8rem' }}>{book.isbn}</td>
                        <td style={{ padding: '12px 16px' }}>
                          <span className={`badge ${book.ebook ? 'badge-cyan' : 'badge-emerald'}`} style={{ fontSize: '0.7rem' }}>
                            {book.ebook ? 'E-Book' : 'Physical'}
                          </span>
                        </td>
                        <td style={{ padding: '12px 16px' }}>
                          <span style={{ color: book.availableCopies > 0 ? '#10b981' : '#f43f5e', fontWeight: 600 }}>
                            {book.availableCopies} / {book.totalCopies}
                          </span>
                        </td>
                        <td style={{ padding: '12px 16px' }}>
                          <span style={{ color: '#f59e0b', fontWeight: 600 }}>★ {book.rating}</span>
                        </td>
                        <td style={{ padding: '12px 16px', textAlign: 'right' }}>
                          <div style={{ display: 'inline-flex', gap: 6 }}>
                            <button onClick={() => setSelectedBook(book)} className="btn btn-secondary btn-sm"><Eye size={14} /></button>
                            <button onClick={() => openEditModal(book)} className="btn btn-secondary btn-sm"><Edit3 size={14} /></button>
                            <button onClick={() => handleDeleteBook(book.id, book.title)} className="btn btn-danger btn-sm"><Trash2 size={14} /></button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        )}

        {/* TAB 3: ACTIVE LOANS & BORROWING */}
        {activeTab === 'loans' && (
          <div>
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 20 }}>
              <div>
                <h3 style={{ fontSize: '1.5rem', fontWeight: 700 }}>Active Library Loans & Returns</h3>
                <p style={{ fontSize: '0.85rem', color: 'var(--text-muted)' }}>
                  Tracks borrowing periods, due dates, and polymorphic late fee calculations.
                </p>
              </div>
            </div>

            <div className="glass-panel" style={{ overflowX: 'auto', padding: 20 }}>
              <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left', fontSize: '0.88rem' }}>
                <thead>
                  <tr style={{ borderBottom: '1px solid var(--border-subtle)', color: 'var(--text-muted)' }}>
                    <th style={{ padding: '12px 16px' }}>Loan ID</th>
                    <th style={{ padding: '12px 16px' }}>Book Title</th>
                    <th style={{ padding: '12px 16px' }}>Borrower</th>
                    <th style={{ padding: '12px 16px' }}>Borrow Date</th>
                    <th style={{ padding: '12px 16px' }}>Due Date</th>
                    <th style={{ padding: '12px 16px' }}>Status</th>
                    <th style={{ padding: '12px 16px' }}>Late Fee</th>
                    <th style={{ padding: '12px 16px', textAlign: 'right' }}>Action</th>
                  </tr>
                </thead>
                <tbody>
                  {loans.map(loan => (
                    <tr key={loan.recordId} style={{ borderBottom: '1px solid rgba(255, 255, 255, 0.04)' }}>
                      <td style={{ padding: '12px 16px', fontWeight: 600 }}>#{loan.recordId}</td>
                      <td style={{ padding: '12px 16px', fontWeight: 600, color: '#ffffff' }}>{loan.bookTitle}</td>
                      <td style={{ padding: '12px 16px' }}>
                        <div style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
                          <User size={14} color="var(--primary-light)" /> {loan.userName}
                        </div>
                      </td>
                      <td style={{ padding: '12px 16px', color: 'var(--text-muted)' }}>{loan.borrowDate}</td>
                      <td style={{ padding: '12px 16px', color: loan.status === 'OVERDUE' ? '#f43f5e' : 'var(--text-muted)' }}>
                        {loan.dueDate}
                      </td>
                      <td style={{ padding: '12px 16px' }}>
                        <span className={`badge ${loan.status === 'OVERDUE' ? 'badge-rose' : loan.status === 'RETURNED' ? 'badge-emerald' : 'badge-amber'}`}>
                          {loan.status}
                        </span>
                      </td>
                      <td style={{ padding: '12px 16px', fontWeight: 600, color: loan.fineAmount > 0 ? '#f43f5e' : '#10b981' }}>
                        ${loan.fineAmount.toFixed(2)}
                      </td>
                      <td style={{ padding: '12px 16px', textAlign: 'right' }}>
                        {loan.status !== 'RETURNED' ? (
                          <button
                            onClick={() => handleReturnBook(loan.recordId, loan.bookId, loan.bookTitle)}
                            className="btn btn-secondary btn-sm"
                          >
                            Return Book
                          </button>
                        ) : (
                          <span style={{ fontSize: '0.8rem', color: '#10b981', display: 'flex', alignItems: 'center', justifyContent: 'flex-end', gap: 4 }}>
                            <Check size={14} /> Completed
                          </span>
                        )}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}

      </main>

      {/* FOOTER */}
      <footer style={{
        borderTop: '1px solid var(--border-subtle)',
        padding: '24px 32px',
        textAlign: 'center',
        color: 'var(--text-dim)',
        fontSize: '0.85rem',
        background: 'rgba(9, 13, 22, 0.9)'
      }}>
        BookSphere Digital Library Management & Book Discovery System • Enterprise Edition • Built with Spring Boot 3.4 & React 19
      </footer>

      {/* MODAL: BOOK DETAILS & READING PREVIEW */}
      {selectedBook && (
        <div className="modal-overlay" onClick={() => setSelectedBook(null)}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 20 }}>
              <div>
                <span className="badge badge-purple" style={{ marginBottom: 8 }}>{selectedBook.category}</span>
                <h3 style={{ fontSize: '1.4rem', fontWeight: 800 }}>{selectedBook.title}</h3>
                <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>by {selectedBook.author}</p>
              </div>
              <button
                onClick={() => setSelectedBook(null)}
                style={{ background: 'transparent', border: 'none', color: 'var(--text-dim)', cursor: 'pointer' }}
              >
                <X size={20} />
              </button>
            </div>

            <div style={{ display: 'flex', gap: 20, marginBottom: 20, flexWrap: 'wrap' }}>
              <img
                src={selectedBook.coverImageUrl}
                alt={selectedBook.title}
                style={{ width: 140, height: 190, objectFit: 'cover', borderRadius: 'var(--radius-md)' }}
              />
              <div style={{ flex: 1, minWidth: 200, display: 'flex', flexDirection: 'column', gap: 8, fontSize: '0.85rem' }}>
                <div><strong>ISBN:</strong> {selectedBook.isbn}</div>
                <div><strong>Publisher:</strong> {selectedBook.publisher} ({selectedBook.publicationYear})</div>
                <div><strong>Pages:</strong> {selectedBook.pages}</div>
                <div><strong>Format:</strong> {selectedBook.ebook ? `Digital E-Book (${selectedBook.downloadFormat}, ${selectedBook.fileSizeMb} MB)` : 'Hardcover / Physical Print'}</div>
                <div><strong>Rating:</strong> <span style={{ color: '#f59e0b' }}>★ {selectedBook.rating} / 5.0</span></div>
                <div><strong>Inventory:</strong> {selectedBook.availableCopies} available of {selectedBook.totalCopies}</div>
                <div><strong>Tags:</strong> {selectedBook.tags}</div>
              </div>
            </div>

            <div style={{ background: 'rgba(255, 255, 255, 0.03)', padding: 16, borderRadius: 'var(--radius-md)', marginBottom: 20 }}>
              <h5 style={{ fontSize: '0.9rem', marginBottom: 6, color: 'var(--primary-light)' }}>Synopsis & Overview:</h5>
              <p style={{ fontSize: '0.85rem', color: 'var(--text-muted)', lineHeight: 1.6 }}>{selectedBook.summary}</p>
            </div>

            <div style={{ display: 'flex', justifyContent: 'flex-end', gap: 10 }}>
              <button onClick={() => setSelectedBook(null)} className="btn btn-secondary">
                Close
              </button>
              <button
                onClick={() => { setBorrowModalBook(selectedBook); setSelectedBook(null); }}
                disabled={selectedBook.availableCopies <= 0}
                className="btn btn-primary"
              >
                Borrow Book
              </button>
            </div>
          </div>
        </div>
      )}

      {/* MODAL: ADD / EDIT BOOK (FUNCTIONAL REQUIREMENTS) */}
      {(isAddModalOpen || editingBook) && (
        <div className="modal-overlay" onClick={() => { setIsAddModalOpen(false); setEditingBook(null); }}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 20 }}>
              <h3 style={{ fontSize: '1.3rem', fontWeight: 700 }}>
                {editingBook ? 'Edit Book Details' : 'Add New Book Record'}
              </h3>
              <button
                onClick={() => { setIsAddModalOpen(false); setEditingBook(null); }}
                style={{ background: 'transparent', border: 'none', color: 'var(--text-dim)', cursor: 'pointer' }}
              >
                <X size={20} />
              </button>
            </div>

            <form onSubmit={editingBook ? handleUpdateBook : handleAddBook} style={{ display: 'flex', flexDirection: 'column', gap: 14 }}>
              <div>
                <label style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>Book Title *</label>
                <input
                  type="text"
                  required
                  value={formData.title}
                  onChange={(e) => setFormData({ ...formData, title: e.target.value })}
                  style={{
                    width: '100%',
                    padding: '8px 12px',
                    borderRadius: 'var(--radius-sm)',
                    background: 'rgba(255, 255, 255, 0.05)',
                    border: '1px solid var(--border-subtle)',
                    color: '#ffffff',
                    marginTop: 4
                  }}
                />
              </div>

              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
                <div>
                  <label style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>Author *</label>
                  <input
                    type="text"
                    required
                    value={formData.author}
                    onChange={(e) => setFormData({ ...formData, author: e.target.value })}
                    style={{
                      width: '100%',
                      padding: '8px 12px',
                      borderRadius: 'var(--radius-sm)',
                      background: 'rgba(255, 255, 255, 0.05)',
                      border: '1px solid var(--border-subtle)',
                      color: '#ffffff',
                      marginTop: 4
                    }}
                  />
                </div>
                <div>
                  <label style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>Category *</label>
                  <select
                    value={formData.category}
                    onChange={(e) => setFormData({ ...formData, category: e.target.value })}
                    style={{
                      width: '100%',
                      padding: '8px 12px',
                      borderRadius: 'var(--radius-sm)',
                      background: '#0f172a',
                      border: '1px solid var(--border-subtle)',
                      color: '#ffffff',
                      marginTop: 4
                    }}
                  >
                    <option value="Computer Science">Computer Science</option>
                    <option value="Science Fiction">Science Fiction</option>
                    <option value="Classic Literature">Classic Literature</option>
                    <option value="Psychology & Mind">Psychology & Mind</option>
                    <option value="Data Science & AI">Data Science & AI</option>
                  </select>
                </div>
              </div>

              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: 12 }}>
                <div>
                  <label style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>ISBN *</label>
                  <input
                    type="text"
                    required
                    value={formData.isbn}
                    onChange={(e) => setFormData({ ...formData, isbn: e.target.value })}
                    style={{
                      width: '100%',
                      padding: '8px 12px',
                      borderRadius: 'var(--radius-sm)',
                      background: 'rgba(255, 255, 255, 0.05)',
                      border: '1px solid var(--border-subtle)',
                      color: '#ffffff',
                      marginTop: 4
                    }}
                  />
                </div>
                <div>
                  <label style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>Year</label>
                  <input
                    type="number"
                    value={formData.publicationYear}
                    onChange={(e) => setFormData({ ...formData, publicationYear: parseInt(e.target.value) || 2024 })}
                    style={{
                      width: '100%',
                      padding: '8px 12px',
                      borderRadius: 'var(--radius-sm)',
                      background: 'rgba(255, 255, 255, 0.05)',
                      border: '1px solid var(--border-subtle)',
                      color: '#ffffff',
                      marginTop: 4
                    }}
                  />
                </div>
                <div>
                  <label style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>Copies</label>
                  <input
                    type="number"
                    min="1"
                    value={formData.availableCopies}
                    onChange={(e) => setFormData({
                      ...formData,
                      availableCopies: parseInt(e.target.value) || 1,
                      totalCopies: parseInt(e.target.value) || 1
                    })}
                    style={{
                      width: '100%',
                      padding: '8px 12px',
                      borderRadius: 'var(--radius-sm)',
                      background: 'rgba(255, 255, 255, 0.05)',
                      border: '1px solid var(--border-subtle)',
                      color: '#ffffff',
                      marginTop: 4
                    }}
                  />
                </div>
              </div>

              {/* Book Cover Image Input with Live Preview */}
              <div style={{
                background: 'rgba(255, 255, 255, 0.03)',
                padding: '14px',
                borderRadius: 'var(--radius-md)',
                border: '1px solid var(--border-subtle)'
              }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 8 }}>
                  <label style={{ fontSize: '0.82rem', fontWeight: 600, color: '#ffffff' }}>
                    Book Cover Image URL *
                  </label>
                  <span style={{ fontSize: '0.74rem', color: 'var(--primary-light)' }}>
                    Paste an image link or select a preset
                  </span>
                </div>

                <div style={{ display: 'flex', gap: 14, alignItems: 'center' }}>
                  {/* Live Thumbnail Preview */}
                  <div style={{
                    width: 64,
                    height: 84,
                    borderRadius: 'var(--radius-sm)',
                    overflow: 'hidden',
                    background: '#090d16',
                    border: '1px solid rgba(99, 102, 241, 0.3)',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    flexShrink: 0,
                    boxShadow: '0 4px 12px rgba(0,0,0,0.4)'
                  }}>
                    {formData.coverImageUrl ? (
                      <img
                        src={formData.coverImageUrl}
                        alt="Preview"
                        style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                        onError={(e) => {
                          e.target.onerror = null;
                          e.target.src = 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=500&auto=format&fit=crop';
                        }}
                      />
                    ) : (
                      <BookOpen size={24} color="var(--text-dim)" />
                    )}
                  </div>

                  {/* Input and Preset Buttons */}
                  <div style={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 8 }}>
                    <input
                      type="url"
                      placeholder="https://images.unsplash.com/... or any book cover URL"
                      value={formData.coverImageUrl}
                      onChange={(e) => setFormData({ ...formData, coverImageUrl: e.target.value })}
                      style={{
                        width: '100%',
                        padding: '9px 12px',
                        borderRadius: 'var(--radius-sm)',
                        background: 'rgba(255, 255, 255, 0.06)',
                        border: '1px solid var(--border-subtle)',
                        color: '#ffffff',
                        fontSize: '0.85rem'
                      }}
                    />

                    {/* Quick Preset Selector Chips */}
                    <div style={{ display: 'flex', gap: 6, flexWrap: 'wrap' }}>
                      <button
                        type="button"
                        onClick={() => setFormData({ ...formData, coverImageUrl: 'https://images.unsplash.com/photo-1532012164546-f432f2e3edd4?w=500&auto=format&fit=crop' })}
                        style={{ fontSize: '0.72rem', padding: '4px 8px', borderRadius: 4, background: 'rgba(99, 102, 241, 0.15)', border: '1px solid rgba(99, 102, 241, 0.3)', color: '#a5b4fc', cursor: 'pointer' }}
                      >
                        💻 Tech / Code
                      </button>
                      <button
                        type="button"
                        onClick={() => setFormData({ ...formData, coverImageUrl: 'https://images.unsplash.com/photo-1506466010722-395aa2bef877?w=500&auto=format&fit=crop' })}
                        style={{ fontSize: '0.72rem', padding: '4px 8px', borderRadius: 4, background: 'rgba(245, 158, 11, 0.15)', border: '1px solid rgba(245, 158, 11, 0.3)', color: '#fcd34d', cursor: 'pointer' }}
                      >
                        🪐 Sci-Fi
                      </button>
                      <button
                        type="button"
                        onClick={() => setFormData({ ...formData, coverImageUrl: 'https://images.unsplash.com/photo-1476275466078-4007374efbbe?w=500&auto=format&fit=crop' })}
                        style={{ fontSize: '0.72rem', padding: '4px 8px', borderRadius: 4, background: 'rgba(16, 185, 129, 0.15)', border: '1px solid rgba(16, 185, 129, 0.3)', color: '#6ee7b7', cursor: 'pointer' }}
                      >
                        📜 Literature
                      </button>
                      <button
                        type="button"
                        onClick={() => setFormData({ ...formData, coverImageUrl: 'https://images.unsplash.com/photo-1497633762265-9d179a990aa6?w=500&auto=format&fit=crop' })}
                        style={{ fontSize: '0.72rem', padding: '4px 8px', borderRadius: 4, background: 'rgba(6, 182, 212, 0.15)', border: '1px solid rgba(6, 182, 212, 0.3)', color: '#67e8f9', cursor: 'pointer' }}
                      >
                        🧠 Psychology
                      </button>
                      <button
                        type="button"
                        onClick={() => setFormData({ ...formData, coverImageUrl: 'https://images.unsplash.com/photo-1512820790803-83ca734da794?w=500&auto=format&fit=crop' })}
                        style={{ fontSize: '0.72rem', padding: '4px 8px', borderRadius: 4, background: 'rgba(244, 63, 94, 0.15)', border: '1px solid rgba(244, 63, 94, 0.3)', color: '#fda4af', cursor: 'pointer' }}
                      >
                        📊 Data & Systems
                      </button>
                    </div>
                  </div>
                </div>
              </div>

              <div>
                <label style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>Summary</label>
                <textarea
                  rows="3"
                  value={formData.summary}
                  onChange={(e) => setFormData({ ...formData, summary: e.target.value })}
                  style={{
                    width: '100%',
                    padding: '8px 12px',
                    borderRadius: 'var(--radius-sm)',
                    background: 'rgba(255, 255, 255, 0.05)',
                    border: '1px solid var(--border-subtle)',
                    color: '#ffffff',
                    marginTop: 4,
                    fontFamily: 'inherit'
                  }}
                />
              </div>

              <div style={{ display: 'flex', alignItems: 'center', gap: 10, marginTop: 4 }}>
                <input
                  type="checkbox"
                  id="ebookCheck"
                  checked={formData.ebook}
                  onChange={(e) => setFormData({ ...formData, ebook: e.target.checked })}
                />
                <label htmlFor="ebookCheck" style={{ fontSize: '0.85rem', cursor: 'pointer' }}>
                  Register as Digital E-Book (inherits from Book class via Multilevel Inheritance)
                </label>
              </div>

              {formData.ebook && (
                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12, padding: 12, background: 'rgba(6, 182, 212, 0.06)', borderRadius: 'var(--radius-sm)' }}>
                  <div>
                    <label style={{ fontSize: '0.78rem', color: '#67e8f9' }}>Format (PDF / EPUB)</label>
                    <input
                      type="text"
                      value={formData.downloadFormat}
                      onChange={(e) => setFormData({ ...formData, downloadFormat: e.target.value })}
                      style={{ width: '100%', padding: '6px 10px', background: '#0f172a', border: '1px solid var(--border-subtle)', color: '#fff', marginTop: 4 }}
                    />
                  </div>
                  <div>
                    <label style={{ fontSize: '0.78rem', color: '#67e8f9' }}>Size (MB)</label>
                    <input
                      type="number"
                      step="0.1"
                      value={formData.fileSizeMb}
                      onChange={(e) => setFormData({ ...formData, fileSizeMb: parseFloat(e.target.value) || 2.0 })}
                      style={{ width: '100%', padding: '6px 10px', background: '#0f172a', border: '1px solid var(--border-subtle)', color: '#fff', marginTop: 4 }}
                    />
                  </div>
                </div>
              )}

              <div style={{ display: 'flex', justifyContent: 'flex-end', gap: 10, marginTop: 14 }}>
                <button
                  type="button"
                  onClick={() => { setIsAddModalOpen(false); setEditingBook(null); }}
                  className="btn btn-secondary"
                >
                  Cancel
                </button>
                <button type="submit" className="btn btn-primary">
                  {editingBook ? 'Save Changes' : 'Add Record'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* MODAL: BORROW BOOK */}
      {borrowModalBook && (
        <div className="modal-overlay" onClick={() => setBorrowModalBook(null)}>
          <div className="modal-content" style={{ maxWidth: 460 }} onClick={(e) => e.stopPropagation()}>
            <h3 style={{ fontSize: '1.3rem', fontWeight: 700, marginBottom: 12 }}>Borrow Book Confirmation</h3>
            <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', marginBottom: 16 }}>
              Checking out: <strong style={{ color: '#ffffff' }}>{borrowModalBook.title}</strong>
            </p>

            <div style={{ marginBottom: 16 }}>
              <label style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>Reader Name / Member ID:</label>
              <input
                type="text"
                placeholder="e.g. Aarav Patel or Student ID"
                value={borrowerName}
                onChange={(e) => setBorrowerName(e.target.value)}
                style={{
                  width: '100%',
                  padding: '10px 14px',
                  borderRadius: 'var(--radius-sm)',
                  background: 'rgba(255, 255, 255, 0.05)',
                  border: '1px solid var(--border-subtle)',
                  color: '#ffffff',
                  marginTop: 6
                }}
              />
            </div>

            <div style={{ background: 'rgba(255, 255, 255, 0.03)', padding: 12, borderRadius: 'var(--radius-sm)', fontSize: '0.8rem', color: 'var(--text-dim)', marginBottom: 20 }}>
              Loan Period: 14 days • Overdue rate: $0.50/day (Physical) or $0.15/day (E-Book)
            </div>

            <div style={{ display: 'flex', justifyContent: 'flex-end', gap: 10 }}>
              <button onClick={() => setBorrowModalBook(null)} className="btn btn-secondary">
                Cancel
              </button>
              <button onClick={handleBorrowConfirm} className="btn btn-primary">
                Confirm Checkout
              </button>
            </div>
          </div>
        </div>
      )}

    </div>
  );
}
