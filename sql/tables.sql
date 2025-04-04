USE EduMark;
GO

-- Departments Table
INSERT INTO Departments (DepartmentName) VALUES
('Artificial Intelligence & Data Science'),
('Artificial Intelligence & Machine Learning Engineering'),
('Biotechnology Engineering'),
('Civil Engineering'),
('Computer & Communication Engineering'),
('Computer Science & Engineering'),
('Computer Science & Engineering (Cyber Security)'),
('Electrical & Electronics Engineering'),
('Electronics & Communication Engineering'),
('Electronics Engineering (VLSI Design & Technology)'),
('Electronics & Communication (Advanced Communication Technology)'),
('Information Science & Engineering'),
('Mechanical Engineering'),
('Robotics & Artificial Intelligence Engineering');


-- 4 Sections for CSE, ISE, and ECE
INSERT INTO Sections (SectionName, DepartmentID) VALUES
('A', 6), ('B', 6), ('C', 6), ('D', 6),
('A', 12), ('B', 12), ('C', 12), ('D', 12),
('A', 9), ('B', 9), ('C', 9), ('D', 9);

-- 1 Section for Other Departments
INSERT INTO Sections (SectionName, DepartmentID)
SELECT 'A', DepartmentID
FROM Departments
WHERE DepartmentID NOT IN (6, 9, 12);

--grades table
INSERT INTO Grades (Grade, MinMarks, MaxMarks) VALUES
('O', 90, 100),
('A+', 89, 89),
('A', 70, 88),
('B+', 60, 69),
('B', 55, 59),
('C', 50, 54),
('P', 40, 49),
('F', 0, 39);


SELECT * FROM Departments;
SELECT * FROM Sections;
SELECT * FROM Users;
SELECT * FROM Students;
SELECT * FROM Teachers;
SELECT * FROM Marks;
SELECT * FROM LoginActivity;


