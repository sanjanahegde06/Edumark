# 📊 Marklytics

**Marklytics** is a powerful **Student Marks Management System** built with Java Swing and MS SQL Server. It enables efficient tracking, insertion, and analysis of student marks while ensuring role-based access and secure data handling through stored procedures and triggers.

---

## 🚀 Features

- 🔐 **Login & Registration** for Students and Teachers  
- 🧑‍🏫 **Teacher Dashboard** to securely upload student marks  
- 🧑‍🎓 **Student Dashboard** to view detailed marks and grades  
- 🧮 **Automatic Calculation** of:
  - CIE (MSE1 + MSE2 + Task)
  - SEE (Reduced to 50)
  - Total (CIE + SEE)
  - Grade Assignment based on total  
- 🗃️ **Department and Section Allocation**
- 📚 **Subject Management** per department
- 🧠 **Stored Procedures** for data insertion and grade calculation
- 🔁 **Triggers** to log login activities
- 💾 **MSSQL Integration** via JDBC

---

## 🧱 Tech Stack

| Layer       | Technology        |
|-------------|-------------------|
| Frontend    | Java Swing        |
| Backend     | Java + JDBC       |
| Database    | MS SQL Server     |
| DB Scripts  | Stored Procedures, Triggers |

---


---

## ⚙️ How It Works

- Users login through the `LoginForm` which verifies their role and credentials.
- Role-based access:
  - Students can only view their marks.
  - Teachers can update marks only for students in their assigned section.
- Marks are inserted through a stored procedure that auto-calculates `CIE`, `SEE_50`, `Total`, and `Grade`.
- Every login is logged using a SQL trigger that monitors `LoginFlag`.

---

## 🧠 Concepts Used

- Object-Oriented Programming (OOP)
- Java Swing GUI
- JDBC (Java Database Connectivity)
- SQL Joins & Constraints
- Stored Procedures & Triggers
- Role-Based Access Control (RBAC)
- Data Validation & Exception Handling

---

## 🧪 SQL Components

- `schema.sql`: Creates tables (Users, Students, Teachers, Marks, Grades, etc.)
- `procedures.sql`: Stored procedures for inserting students, teachers, and calculating marks
- `triggers.sql`: Trigger to log login events to `LoginActivity`
- `tables.sql`: Insert default values into Departments, Sections, Grades

---

## 🖥️ Setup Instructions

1. Clone this repository  
2. Import and execute all SQL files from the `/sql` folder into MS SQL Server  
3. Update your database credentials in `DatabaseConnection.java`  
4. Compile and run `LoginForm.java` to launch the application

---

## 📸 Screenshots (Optional)

_Add UI screenshots of login page, student and teacher dashboards, etc._

---

## 👨‍💻 Developed By

**Sanjana Shrikant Hegde**  


---



