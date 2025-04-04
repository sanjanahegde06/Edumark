USE EduMark;
GO

CREATE PROCEDURE InsertStudent
    @Username VARCHAR(50),
    @Password VARCHAR(255),
    @USN VARCHAR(20),
    @StudentName VARCHAR(100),
    @DepartmentID INT,
    @SectionID INT
AS
BEGIN
    DECLARE @UserID INT;

    -- Insert into Users table
    INSERT INTO Users (Username, Password, Role)
    VALUES (@Username, @Password, 'Student');
    
    SET @UserID = SCOPE_IDENTITY();

    -- Insert into Students table
    INSERT INTO Students (UserID, USN, StudentName, DepartmentID, SectionID)
    VALUES (@UserID, @USN, @StudentName, @DepartmentID, @SectionID);
END;
GO

CREATE PROCEDURE InsertTeacher
    @Username VARCHAR(50),
    @Password VARCHAR(255),
    @TeacherName VARCHAR(100),
    @DepartmentID INT,
    @SectionID INT
AS
BEGIN
    DECLARE @UserID INT;

    -- Insert into Users table
    INSERT INTO Users (Username, Password, Role)
    VALUES (@Username, @Password, 'Teacher');
    SET @UserID = SCOPE_IDENTITY();

    -- Insert into Teachers table
    INSERT INTO Teachers (UserID, TeacherName, DepartmentID, SectionID)
    VALUES (@UserID, @TeacherName, @DepartmentID, @SectionID);

    PRINT 'Teacher inserted successfully';
END;
GO

CREATE PROCEDURE InsertMarks
    @USN VARCHAR(20),
    @MSE1 INT,
    @MSE2 INT,
    @Task INT,
    @SEE INT
AS
BEGIN
    DECLARE @StudentID INT;
    DECLARE @CIE INT;
    DECLARE @SEE_50 INT;
    DECLARE @Total INT;
    DECLARE @Grade CHAR(2);

    -- Get StudentID using USN
    SELECT @StudentID = StudentID FROM Students WHERE USN = @USN;

    IF @StudentID IS NULL
    BEGIN
        PRINT 'Invalid USN. Student not found.';
        RETURN;
    END;

    -- Calculate CIE, SEE_50, and Total
    SET @CIE = @MSE1 + @MSE2 + @Task;
    SET @SEE_50 = @SEE / 2;
    SET @Total = @CIE + @SEE_50;

    -- Determine Grade
    SELECT @Grade = Grade
    FROM Grades
    WHERE @Total BETWEEN MinMarks AND MaxMarks;

    -- Insert into Marks table
    INSERT INTO Marks (StudentID, MSE1, MSE2, Task, SEE, Grade)
    VALUES (@StudentID, @MSE1, @MSE2, @Task, @SEE, @Grade);

    PRINT 'Marks inserted successfully';
END;
GO
