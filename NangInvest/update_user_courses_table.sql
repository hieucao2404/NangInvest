-- Update UserCourses table to add progress tracking columns
-- Run this script to add the new columns for progress tracking

USE NangInvestDB;

-- Add new columns to UserCourses table if they don't exist
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'[dbo].[UserCourses]') AND name = 'Progress')
BEGIN
    ALTER TABLE UserCourses ADD Progress INT DEFAULT 0;
END

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'[dbo].[UserCourses]') AND name = 'EnrollmentDate')
BEGIN
    ALTER TABLE UserCourses ADD EnrollmentDate DATETIME2 DEFAULT GETDATE();
END

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'[dbo].[UserCourses]') AND name = 'CompletionDate')
BEGIN
    ALTER TABLE UserCourses ADD CompletionDate DATETIME2 NULL;
END

-- Update existing records to have default values
UPDATE UserCourses 
SET Progress = 0 
WHERE Progress IS NULL;

UPDATE UserCourses 
SET EnrollmentDate = GETDATE() 
WHERE EnrollmentDate IS NULL;

PRINT 'UserCourses table updated successfully with progress tracking columns.';
