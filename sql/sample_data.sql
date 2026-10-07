-- =====================================================================
-- BookSphere Digital Library Management and Book Discovery System
-- Sample Seed Data (DML)
-- =====================================================================

USE booksphere_db;

-- Insert Categories
INSERT INTO categories (name, description) VALUES
('Computer Science', 'Algorithms, Software Engineering, AI and Distributed Systems'),
('Science Fiction', 'Speculative fiction exploring space, time, technology, and humanity'),
('Classic Literature', 'Timeless masterpieces of world literature and philosophy'),
('Psychology & Mind', 'Human cognition, behavioral psychology, and self-mastery'),
('Data Science & AI', 'Machine Learning, Deep Learning, and Mathematical Modeling');

-- Insert Sample Books (including physical books and E-Books)
INSERT INTO books (isbn, title, author, category_id, publisher, publication_year, pages, rating, available_copies, total_copies, is_ebook, file_size_mb, download_format, summary, cover_image_url, tags) VALUES
('978-0132350884', 'Clean Code: A Handbook of Agile Software Craftsmanship', 'Robert C. Martin', 1, 'Prentice Hall', 2008, 464, 4.8, 4, 5, FALSE, 0.0, 'NONE', 'Even bad code can function. But if code isn''t clean, it can bring a development organization to its knees.', 'https://images.unsplash.com/photo-1532012164546-f432f2e3edd4?w=500&auto=format&fit=crop', 'Software,Best Practice,Clean Code'),

('978-0134685991', 'Effective Java (3rd Edition)', 'Joshua Bloch', 1, 'Addison-Wesley', 2018, 412, 4.9, 3, 3, FALSE, 0.0, 'NONE', 'The definitive guide to best practices in the Java programming language.', 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=500&auto=format&fit=crop', 'Java,Architecture,Design Patterns'),

('978-0441013593', 'Dune', 'Frank Herbert', 2, 'Ace Books', 1965, 896, 4.7, 5, 6, FALSE, 0.0, 'NONE', 'Set on the desert planet Arrakis, Dune is the story of the boy Paul Atreides, heir to a noble family.', 'https://images.unsplash.com/photo-1506466010722-395aa2bef877?w=500&auto=format&fit=crop', 'Sci-Fi,Epic,Space Opera'),

('978-0141439518', 'Pride and Prejudice', 'Jane Austen', 3, 'Penguin Classics', 1813, 279, 4.6, 2, 4, TRUE, 2.4, 'EPUB', 'A romantic novel that charts the emotional development of the protagonist Elizabeth Bennet.', 'https://images.unsplash.com/photo-1476275466078-4007374efbbe?w=500&auto=format&fit=crop', 'Classic,Romance,Literature'),

('978-0374533557', 'Thinking, Fast and Slow', 'Daniel Kahneman', 4, 'Farrar, Straus and Giroux', 2011, 499, 4.5, 3, 5, TRUE, 5.1, 'PDF', 'A tour de force on how our minds make choices and decisions by Nobel laureate Daniel Kahneman.', 'https://images.unsplash.com/photo-1497633762265-9d179a990aa6?w=500&auto=format&fit=crop', 'Psychology,Cognition,Decision Making'),

('978-1449370756', 'Designing Data-Intensive Applications', 'Martin Kleppmann', 1, 'O''Reilly Media', 2017, 616, 4.9, 2, 3, FALSE, 0.0, 'NONE', 'The big ideas behind reliable, scalable, and maintainable systems.', 'https://images.unsplash.com/photo-1512820790803-83ca734da794?w=500&auto=format&fit=crop', 'System Design,Distributed Systems,Database'),

('978-0062316097', 'Sapiens: A Brief History of Humankind', 'Yuval Noah Harari', 3, 'Harper', 2015, 464, 4.7, 6, 8, TRUE, 8.2, 'PDF', 'Explores how biology and history have defined us and enhanced our understanding of what it means to be human.', 'https://images.unsplash.com/photo-1457369804613-52c61a468e7d?w=500&auto=format&fit=crop', 'History,Anthropology,Science'),

('978-0262033848', 'Introduction to Algorithms (CLRS)', 'Thomas H. Cormen', 1, 'MIT Press', 2009, 1312, 4.6, 1, 2, FALSE, 0.0, 'NONE', 'A comprehensive textbook covering modern algorithms and data structures in depth.', 'https://images.unsplash.com/photo-1516979187457-637abb4f9353?w=500&auto=format&fit=crop', 'Algorithms,Data Structures,Academic');

-- Insert Sample Users
INSERT INTO users (username, full_name, email, phone, role, max_borrow_limit) VALUES
('aarav_patel', 'Aarav Patel', 'aarav.patel@booksphere.edu', '+91-9876543210', 'MEMBER', 5),
('priya_sharma', 'Priya Sharma', 'priya.sharma@booksphere.edu', '+91-9812345678', 'MEMBER', 5),
('librarian_admin', 'Dr. Meera Iyer', 'admin@booksphere.org', '+91-9845012345', 'LIBRARIAN', 20);

-- Insert Sample Borrow Records
INSERT INTO borrow_records (book_id, user_id, borrow_date, due_date, return_date, fine_amount, status, notes) VALUES
(1, 1, '2026-09-15', '2026-09-29', '2026-09-28', 0.00, 'RETURNED', 'Returned in pristine condition'),
(2, 2, '2026-09-25', '2026-10-09', NULL, 0.00, 'BORROWED', 'Currently on loan'),
(3, 1, '2026-09-28', '2026-10-12', NULL, 0.00, 'BORROWED', 'Renewed online');
