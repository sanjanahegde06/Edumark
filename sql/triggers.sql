use EduMark;
GO

CREATE TABLE LoginActivity (
    ActivityID INT PRIMARY KEY IDENTITY,
    UserID INT,
    Role VARCHAR(20),
    ActivityType VARCHAR(20),
    ActivityTime DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_LoginActivity_UserID FOREIGN KEY (UserID) REFERENCES Users(UserID)
);
GO

-- Trigger
CREATE TRIGGER trg_LogLogin
ON Users
AFTER UPDATE
AS
BEGIN
    IF EXISTS (SELECT 1 FROM inserted WHERE inserted.Password IS NOT NULL)
    BEGIN
        INSERT INTO LoginActivity (UserID, Role, ActivityType)
        SELECT inserted.UserID, inserted.Role, 'Login'
        FROM inserted;
    END;
END;
GO


-- Sample Test Data
INSERT INTO Users (Username, Password, Role) VALUES ('test_student', 'password123', 'Student');

-- Simulate a Login (Password Update)
UPDATE Users
SET Password = 'newpassword123'
WHERE Username = 'test_student';

-- Check if the activity is logged
SELECT * FROM LoginActivity;
