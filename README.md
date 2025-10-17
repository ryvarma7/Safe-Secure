# 🛡️ SafeShare - Secure File Transfer System with MySQL Backend

**SafeShare** is a fully functional **secure file transfer application** built with a **Java Swing UI** and a **MySQL database** for persistent data storage and access control.

---

## 🚀 Overview

SafeShare enables secure management and sharing of files with a robust **two-role system** — **Administrator** and **User** — ensuring that sensitive documents are only accessed by authorized individuals.

### 👤 Users Can:
- Register for an account with a password strength checker  
- Log in securely to their personal dashboard  
- Browse a list of available files  
- Request access to specific files  
- View approved files using a unique access key  

### 🧑‍💼 Administrators Can:
- Manage the entire file repository (upload/delete)  
- View and manage all user access requests  
- Approve or reject file access requests  
- View detailed user profiles before granting access  

All data is persistently stored in a **MySQL database**, ensuring user information, files, and access rights are retained between sessions.

---

## 📁 Project Structure

```
SafeShare/
├── src/main/java/com/example/securefiletransfer/
│   ├── SecureFileTransfer.java   # Main application entry point
│   ├── DatabaseManager.java      # JDBC operations & database layer
│   ├── AdminPanel.java           # Admin dashboard UI and logic
│   ├── UserPanel.java            # User dashboard UI and logic
│   ├── LoginPanel.java           # Login screen UI
│   ├── RegisterPanel.java        # Registration screen UI
│   ├── FileHandler.java          # Logic for viewing/handling files
│   ├── PasswordStrengthDots.java # Custom UI component for password strength
│   └── ... (other UI and helper classes)
│
├── database/
│   └── database.sql              # Complete MySQL database schema
│
└── pom.xml                       # Maven project configuration and dependencies
```

---

## ⚙️ System Requirements

| Component | Version |
|------------|----------|
| Java Development Kit (JDK) | 17 or higher |
| Apache Maven | 3.6 or higher |
| MySQL Server | 5.7 or higher |
| OS | Windows / Linux / macOS |
| RAM | Minimum 512 MB |
| Disk Space | 50 MB (application + database) |

---

## 🧩 Installation Instructions

### **Step 1: Prerequisites**
- Install **JDK 17+**
- Install **Apache Maven**
- Install and run **MySQL Server**

### **Step 2: Clone the Repository**
```bash
git clone https://github.com/yourusername/SafeShare.git
cd SafeShare
```

### **Step 3: Setup MySQL Database**
Start your MySQL Server, then run:

```sql
CREATE DATABASE IF NOT EXISTS secure_file_transfer;
USE secure_file_transfer;
-- Execute the full content of database/database.sql
```

### **Step 4: Configure Database Connection**
Edit `src/main/java/com/example/securefiletransfer/DatabaseManager.java` and update these lines:

```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/secure_file_transfer";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "Sql@2112"; // Change if needed
```

### **Step 5: Compile the Application**
```bash
mvn clean install
```

### **Step 6: Run the Application**
```bash
mvn exec:java -Dexec.mainClass="com.example.securefiletransfer.SecureFileTransfer"
```

---

## 🔐 Default Login Credentials

| Role | Username | Password |
|------|-----------|-----------|
| Administrator | admin | 00000 |

---

## ✨ Features

### User & Admin Management
- Role-based access (User/Admin)  
- Secure login system  
- Registration with validation  
- Password strength indicator  

### Admin Panel
- Upload/Delete files  
- View and approve/reject requests  
- View user details before approval  

### User Panel
- Request file access  
- View approved files via access key  
- In-app file viewer for PDF, Images, and Text  

### Data Persistence & Security
- Data stored in MySQL  
- Prevents SQL injection (PreparedStatement)  
- Clear UI-database separation  

---

## 🧱 Database Schema

**Tables:**
- `users` – Stores user accounts, credentials, and roles  
- `files` – Stores file metadata and content (LONGBLOB)  
- `requests` – Tracks file access requests, status, and keys  

---

## 🧰 Troubleshooting

### Maven/Compilation Errors
- Run: `mvn clean install`
- Verify main class path

### Database Errors
- Ensure MySQL is running and accessible on port 3306
- Verify credentials in `DatabaseManager.java`
- Run `SHOW TABLES;` to confirm database setup

### Login Issues
- Ensure default admin was inserted by `database.sql`
- Verify DB connection

---

## 🔒 Security Considerations

**Current Implementation:**
- Parameterized queries to prevent SQL injection  
- Passwords stored in plain text (demo purpose)  

**For Production:**
- Use password hashing (e.g., bcrypt)  
- Encrypt files before storing  
- Implement audit logging  

---

## 🚧 Future Enhancements

- File encryption at rest  
- Email notifications for request updates  
- File download functionality  
- User group management  
- Enhanced admin controls  
- Search and filtering options  

---

**Last Updated:** October 17, 2025  
**Status:** ✅ Completed
