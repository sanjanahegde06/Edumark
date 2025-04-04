CREATE DATABASE EduMark;
drop database EduMark;
USE EduMark;

-- Users Table
CREATE TABLE Users (
    UserID INT PRIMARY KEY IDENTITY,
    Username VARCHAR(50) UNIQUE NOT NULL,
    Password VARCHAR(255) NOT NULL,
    Role VARCHAR(20) CHECK (Role IN ('Student', 'Teacher')),
    CreatedAt DATETIME DEFAULT GETDATE()
);

-- Departments Table
CREATE TABLE Departments (
    DepartmentID INT PRIMARY KEY IDENTITY,
    DepartmentName VARCHAR(100) UNIQUE NOT NULL
);

-- Sections Table 
CREATE TABLE Sections (
    SectionID INT PRIMARY KEY IDENTITY,
    SectionName VARCHAR(10) NOT NULL,
    DepartmentID INT,
    FOREIGN KEY (DepartmentID) REFERENCES Departments(DepartmentID)
);

-- Teachers Table
CREATE TABLE Teachers (
    TeacherID INT PRIMARY KEY IDENTITY,
    UserID INT UNIQUE,
    TeacherName VARCHAR(100) NOT NULL,
    DepartmentID INT,
    SectionID INT,
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (DepartmentID) REFERENCES Departments(DepartmentID),
    FOREIGN KEY (SectionID) REFERENCES Sections(SectionID)
);

-- Students Table
CREATE TABLE Students (
    StudentID INT PRIMARY KEY IDENTITY,
    UserID INT UNIQUE,
    USN VARCHAR(20) UNIQUE NOT NULL,
    StudentName VARCHAR(100) NOT NULL,
    DepartmentID INT,
    SectionID INT,
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (DepartmentID) REFERENCES Departments(DepartmentID),
    FOREIGN KEY (SectionID) REFERENCES Sections(SectionID)
);

-- Marks Table
CREATE TABLE Marks (
    MarkID INT PRIMARY KEY IDENTITY,
    StudentID INT,
    MSE1 INT CHECK (MSE1 BETWEEN 0 AND 20),
    MSE2 INT CHECK (MSE2 BETWEEN 0 AND 20),
    Task INT CHECK (Task BETWEEN 0 AND 10),
    SEE INT CHECK (SEE BETWEEN 0 AND 100),
    CIE AS (MSE1 + MSE2 + Task) PERSISTED,
    SEE_50 AS (SEE / 2) PERSISTED,
    Total AS ((MSE1 + MSE2 + Task) + (SEE / 2)) PERSISTED,
    Grade CHAR(2),
    FOREIGN KEY (StudentID) REFERENCES Students(StudentID)
);

-- Grades Table
CREATE TABLE Grades (
    Grade CHAR(2) PRIMARY KEY,
    MinMarks INT,
    MaxMarks INT
);


-- Check Departments
SELECT * FROM Departments;

-- Check Sections
SELECT * FROM Sections;

-- Check Grades
SELECT * FROM Grades;
