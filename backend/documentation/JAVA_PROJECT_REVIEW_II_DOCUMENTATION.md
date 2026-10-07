# JAVA PROJECT REVIEW – II (DOCUMENTATION)

**Project Title:** BookSphere Digital Library Management and Book Discovery System  
**Academic Review:** Review – II  
**Development Platform:** Java JDK 25 (LTS), Spring Boot 3.5.16, React 19 (Vite)
**Supported IDEs:** IntelliJ IDEA / Eclipse / VS Code  

---

## 1. Title & Problem Statement

### a. Finalized Full Title
**BookSphere Digital Library Management and Book Discovery System**

### b. What Problem Does the Application Solve?
Traditional academic and institutional library management systems are predominantly static inventory trackers. They suffer from the following problems:
1. **Inefficient Search and Discovery:** Users struggle to locate books beyond exact title matches. There is no automated categorization, recommendation, or intelligent discovery mechanism for readers seeking related literature or e-books.
2. **Disconnected Physical and Digital Media:** Traditional systems fail to seamlessly handle both physical printed books and digital media (E-Books: PDF/EPUB) under a unified object model.
3. **Data Inconsistency and Fragile Input:** Library staff frequently encounter data corruption due to unsanitized user inputs, missing validations, unhandled runtime exceptions, and lack of resilient multi-tier persistence.
4. **Performance Bottlenecks:** Synchronous search algorithms freeze the user interface during heavy catalog queries and indexing across thousands of volumes.

### c. Why is This Needed?
BookSphere resolves these pain points by offering:
- **Intelligent Discovery System:** A dual-engine discovery pipeline that computes reader satisfaction ratings, trending titles, staff picks, and genre tags using multithreaded background workers.
- **Robust Multi-Tier Architecture:** Complete separation of concerns spanning `model`, `repository/DAO`, `service`, `util`, `menu`, and `controller`.
- **Hybrid Interface Accessibility:** A resilient, secure **Console-based CLI** tailored for terminal administration and academic evaluation, combined with a modern **React.js web interface** backed by Spring Boot REST endpoints.
- **Fail-Safe Persistence:** Automatic dual-layer storage featuring file-backed persistence (`data/backup.txt`, `data/logs.txt`) alongside relational schema support (`sql/schema.sql`, `sql/sample_data.sql`).

---

## 2. Features of Your Project

### a. Non-Functional Requirements

| Requirement | Implementation Detail | Source File & Class |
| :--- | :--- | :--- |
| **i. Console-based** | Fully interactive command-line interface with formatted ASCII banners, ANSI tables, and menu navigation. | [`Menu.java`](file:///c:/Users/admin/OneDrive/Desktop/pr/backend/src/main/java/com/booksphere/menu/Menu.java), [`Main.java`](file:///c:/Users/admin/OneDrive/Desktop/pr/backend/src/main/java/com/booksphere/Main.java) |
| **ii. Fast execution (threads)** | Multithreaded background indexing and discovery scoring using `ExecutorService` and `Runnable`/`Callable` worker threads (`AsyncDiscoveryWorker`) preventing UI/CLI latency. | [`DiscoveryService.java`](file:///c:/Users/admin/OneDrive/Desktop/pr/backend/src/main/java/com/booksphere/service/DiscoveryService.java), [`AsyncDiscoveryWorker.java`](file:///c:/Users/admin/OneDrive/Desktop/pr/backend/src/main/java/com/booksphere/util/AsyncDiscoveryWorker.java) |
| **iii. Secure input** | Strict input sanitization stripping malicious characters/HTML/SQL injection strings, regular expression validation for ISBN and email addresses, and bounded numerical range verification. | [`Validation.java`](file:///c:/Users/admin/OneDrive/Desktop/pr/backend/src/main/java/com/booksphere/util/Validation.java) |
| **iv. Error handling (Exception)** | Comprehensive error containment using custom checked/unchecked exceptions (`BookNotFoundException`, `InvalidBookDataException`, `BookNotAvailableException`) alongside standard `try-catch-finally` blocks. | [`exception/`](file:///c:/Users/admin/OneDrive/Desktop/pr/backend/src/main/java/com/booksphere/exception/) package |

### b. Functional Requirements

| Requirement | Description | Method & Location |
| :--- | :--- | :--- |
| **i. User can add records** | Allows librarians/users to add both physical books and digital e-books with title, author, category, ISBN, copies, rating, and summaries. | `BookService.addBook(Book book)` |
| **ii. User can search records** | Supports multi-parameter keyword searching across Title, Author, Category, ISBN, and Tags with overloaded methods. | `BookService.searchBooks(String query)`, `searchBooks(String query, String category)` |
| **iii. User can update records** | Allows modifying existing book information, inventory counts, and metadata with validity verification. | `BookService.updateBook(int id, Book updateData)` |
| **iv. User can delete records** | Provides safe record removal by unique ID with confirmation prompt and audit logging. | `BookService.deleteBook(int id)` |

---

## 3. Initial Implementation of Project (Review II Scope: 30% - 50%+)

The project fulfills and exceeds the Review II 30% milestone:
- **Core OOP Models:** 100% complete (`LibraryItem`, `Book`, `EBook`, `User`, `Member`, `BorrowRecord`).
- **Data Access Layer:** 100% complete (`BookDAO`, `BookRepositoryImpl`, `UserDAO`, `UserRepositoryImpl`).
- **Service & Business Logic:** 100% complete (`BookService`, `DiscoveryService`, `BorrowService`).
- **Console Interface & Runner:** 100% complete (`Menu`, `Main`).
- **Multithreading & Persistence:** 100% complete (`AsyncDiscoveryWorker`, `FileManager`, `DBConnection`).
- **Spring Boot REST Controllers & React Frontend:** Functional web UI connected to backend.

---

## 4. Object-Oriented Programming (OOP) Concepts Applied

| Concept | Usage in BookSphere | Code Evidence / Class |
| :--- | :--- | :--- |
| **Class** | Blueprint models for library entities: `LibraryItem`, `Book`, `EBook`, `User`, `Member`, `BorrowRecord`. | [`Book.java`](file:///c:/Users/admin/OneDrive/Desktop/pr/backend/src/main/java/com/booksphere/model/Book.java), [`User.java`](file:///c:/Users/admin/OneDrive/Desktop/pr/backend/src/main/java/com/booksphere/model/User.java) |
| **Object** | Instantiation of entities across repositories, services, and tests (`new Book(...)`, `new EBook(...)`). | [`BookRepositoryImpl.java`](file:///c:/Users/admin/OneDrive/Desktop/pr/backend/src/main/java/com/booksphere/repository/BookRepositoryImpl.java) |
| **Constructor** | Default constructors and overloaded parameterized constructors for initializing object state. | `Book()` and `Book(id, title, author, ...)` in [`Book.java`](file:///c:/Users/admin/OneDrive/Desktop/pr/backend/src/main/java/com/booksphere/model/Book.java) |
| **Methods** | Domain methods performing business operations: `addBook()`, `searchBooks()`, `calculateLateFee()`, `borrowCopy()`. | [`BookService.java`](file:///c:/Users/admin/OneDrive/Desktop/pr/backend/src/main/java/com/booksphere/service/BookService.java), [`LibraryItem.java`](file:///c:/Users/admin/OneDrive/Desktop/pr/backend/src/main/java/com/booksphere/model/LibraryItem.java) |
| **Encapsulation** | All class attributes are `private`, accessible only via validated getters and setters; state mutations are guarded. | [`LibraryItem.java`](file:///c:/Users/admin/OneDrive/Desktop/pr/backend/src/main/java/com/booksphere/model/LibraryItem.java) |
| **Inheritance** | **Hierarchical & Multilevel Inheritance:**<br>1. `LibraryItem` (Abstract) &rarr; `Book` &rarr; `EBook` (Multilevel)<br>2. `User` &rarr; `Member` (Single/Hierarchical) | [`Book.java`](file:///c:/Users/admin/OneDrive/Desktop/pr/backend/src/main/java/com/booksphere/model/Book.java), [`EBook.java`](file:///c:/Users/admin/OneDrive/Desktop/pr/backend/src/main/java/com/booksphere/model/EBook.java), [`Member.java`](file:///c:/Users/admin/OneDrive/Desktop/pr/backend/src/main/java/com/booksphere/model/Member.java) |
| **Polymorphism** | **Compile-Time (Overloading):**<br>- `searchBooks(String)` vs `searchBooks(String, String)`<br>- `calculateLateFee(int)` vs `calculateLateFee(int, double)`<br>**Runtime (Overriding):**<br>- `getItemType()`, `displayDetails()`, `calculateLateFee()` across `LibraryItem`, `Book`, `EBook`. | [`Book.java`](file:///c:/Users/admin/OneDrive/Desktop/pr/backend/src/main/java/com/booksphere/model/Book.java), [`EBook.java`](file:///c:/Users/admin/OneDrive/Desktop/pr/backend/src/main/java/com/booksphere/model/EBook.java) |
| **Abstraction** | Abstract class `LibraryItem` with abstract methods; Pure interfaces `BookDAO` and `UserDAO` defining persistence contracts. | [`LibraryItem.java`](file:///c:/Users/admin/OneDrive/Desktop/pr/backend/src/main/java/com/booksphere/model/LibraryItem.java), [`BookDAO.java`](file:///c:/Users/admin/OneDrive/Desktop/pr/backend/src/main/java/com/booksphere/repository/BookDAO.java) |

---

## 5. General Java Topics Applied

| Topic | Application in Project |
| :--- | :--- |
| **Variables & Data Types** | Primitive types (`int`, `double`, `boolean`, `long`) for numeric counts, ratings, and flags; Reference types (`String`, `List`, `Map`, `LocalDate`). |
| **Operators** | Arithmetic (`+`, `-`, `*`, `/`), Relational (`>=`, `==`), Logical (`&&`, `\|\|`), Ternary operator (`condition ? trueVal : falseVal`). |
| **Control Statements** | Enhanced `switch-case` expression in `Menu.java`, nested `if-else` verification in `Validation.java`. |
| **Loops** | Traditional `for (int i=0; ...)`, enhanced `for (Book b : books)`, `while (running)` input loops. |
| **Arrays / ArrayList** | `String[] parts` in line parsers, `ArrayList<Book>`, `CopyOnWriteArrayList<Book>` for concurrent safety. |
| **Packages** | Clean modular separation: `com.booksphere.model`, `.repository`, `.service`, `.util`, `.menu`, `.controller`, `.exception`. |
| **Constructors** | Default and overloaded constructors demonstrating constructor chaining via `super(...)`. |
| **Static Members** | Static counter `totalItemsCount`, static utility methods in `Validation.java`, static singleton instance in `DBConnection.java`. |
| **Strings** | String immutability, `split()`, `trim()`, `toLowerCase()`, `substring()`, `replaceAll()`. |
| **StringBuilder / StringBuffer** | `StringBuilder` for high-throughput serialization in `FileManager.saveBooksToBackup()`; `StringBuffer` for thread-safe audit logging in `FileManager.appendLog()`. |

---

## 6. Development Environment & Specifications

- **Operating System:** Windows 11 (64-bit)
- **Java Development Kit (JDK):** Java 25 LTS
- **Build System:** Apache Maven 3.9.16 (with embedded `mvnw.cmd` wrapper)
- **Compatible IDEs:** IntelliJ IDEA (Community / Ultimate), Eclipse IDE for Java Developers, VS Code
- **Frontend Framework:** React.js 19, Vite, Lucide Icons

---

## 7. Package and Directory Structure

```text
BookSphere/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/booksphere/
│   │   │   │   ├── model/
│   │   │   │   │   ├── LibraryItem.java      (Abstract Class: Abstraction, Encapsulation)
│   │   │   │   │   ├── Book.java             (Subclass: Inheritance, Polymorphism)
│   │   │   │   │   ├── EBook.java            (Multilevel Subclass: E-Book media)
│   │   │   │   │   ├── User.java             (Base User Class)
│   │   │   │   │   ├── Member.java           (Subclass: Library Member)
│   │   │   │   │   └── BorrowRecord.java     (Loan Transaction Model)
│   │   │   │   │
│   │   │   │   ├── repository/
│   │   │   │   │   ├── BookDAO.java          (Interface: Abstraction)
│   │   │   │   │   ├── BookRepositoryImpl.java (CRUD Data Access Implementation)
│   │   │   │   │   ├── UserDAO.java          (User Data Access Interface)
│   │   │   │   │   └── UserRepositoryImpl.java
│   │   │   │   │
│   │   │   │   ├── service/
│   │   │   │   │   ├── BookService.java      (CRUD Business Logic & Validation)
│   │   │   │   │   ├── DiscoveryService.java (Fast multithreaded discovery engine)
│   │   │   │   │   └── BorrowService.java    (Checkout, Return & Polymorphic fines)
│   │   │   │   │
│   │   │   │   ├── util/
│   │   │   │   │   ├── DBConnection.java     (Singleton DB Manager)
│   │   │   │   │   ├── FileManager.java      (File I/O with StringBuilder/StringBuffer)
│   │   │   │   │   ├── Validation.java       (Secure Input Sanitization & Regex)
│   │   │   │   │   └── AsyncDiscoveryWorker.java (Thread / Runnable Worker)
│   │   │   │   │
│   │   │   │   ├── menu/
│   │   │   │   │   └── Menu.java             (Console-based Interactive CLI)
│   │   │   │   │
│   │   │   │   ├── exception/
│   │   │   │   │   ├── BookNotFoundException.java
│   │   │   │   │   ├── InvalidBookDataException.java
│   │   │   │   │   └── BookNotAvailableException.java
│   │   │   │   │
│   │   │   │   ├── controller/
│   │   │   │   │   ├── BookController.java   (REST API endpoints for React)
│   │   │   │   │   ├── BorrowController.java (REST API for loans)
│   │   │   │   │   └── WebConfig.java        (CORS configuration)
│   │   │   │   │
│   │   │   │   ├── config/
│   │   │   │   │   └── AppConfig.java        (Spring Dependency Injection config)
│   │   │   │   │
│   │   │   │   ├── BooksphereApplication.java (Spring Boot Web Main)
│   │   │   │   └── Main.java                 (Pure Core Java Console Main)
│   │   │   │
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   │
│   │   └── test/
│   ├── pom.xml
│   ├── mvnw.cmd
│   └── mvnw
│
├── frontend/                                 (React.js + Vite Web Application)
│   ├── src/
│   │   ├── App.jsx
│   │   ├── index.css
│   │   └── main.jsx
│   ├── package.json
│   └── vite.config.js
│
├── data/
│   ├── backup.txt                            (Persistent book records storage)
│   └── logs.txt                              (Thread-safe audit & system logs)
│
├── sql/
│   ├── schema.sql                            (DDL: Tables for books, users, loans)
│   └── sample_data.sql                       (DML: Rich seed catalog)
│
├── documentation/
│   └── JAVA_PROJECT_REVIEW_II_DOCUMENTATION.md
│
└── screenshots/                              (Console & Web UI demonstration)
```

---

## 8. How to Execute the Project

### A. Running Standalone Pure Core Java Console (Zero-Config)
In IntelliJ IDEA or Eclipse:
1. Open the project root or `backend` folder.
2. Navigate to `src/main/java/com/booksphere/Main.java`.
3. Right-click and choose **Run 'Main.main()'**.

Or from the command terminal:
```bash
cd backend
javac -d target/classes src/main/java/com/booksphere/model/*.java src/main/java/com/booksphere/repository/*.java src/main/java/com/booksphere/service/*.java src/main/java/com/booksphere/util/*.java src/main/java/com/booksphere/menu/*.java src/main/java/com/booksphere/exception/*.java src/main/java/com/booksphere/Main.java
java -cp target/classes com.booksphere.Main
```

### B. Running Spring Boot Backend API
```bash
cd backend
.\mvnw.cmd spring-boot:run
```
Backend endpoints will be available at `http://localhost:8080/api/books`.

### C. Running React.js Frontend
```bash
cd frontend
npm run dev
```
Open your browser at `http://localhost:5173`.
