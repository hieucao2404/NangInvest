create database NangInvest

use NangInvest

-- Users table
CREATE TABLE Users (
    UserID INT IDENTITY(1,1) PRIMARY KEY,
    Username VARCHAR(50) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    Password VARCHAR(100) NOT NULL,
    Role VARCHAR(20) DEFAULT 'user',
    GoogleID VARCHAR(50),
    Age INT,
    Name VARCHAR(50),
    Expertise VARCHAR(100)
);

ALTER TABLE Users
ALTER COLUMN Password VARCHAR(100) NULL;


-- Courses table (with avatar image)
CREATE TABLE Courses (
    CourseID INT IDENTITY(1,1) PRIMARY KEY,
    CourseName VARCHAR(100) NOT NULL,
    Price DECIMAL(10, 2) DEFAULT 0.00,
    Time VARCHAR(50),
    IsFree BIT DEFAULT 0,
    ImageUrl VARCHAR(255) -- Path to course avatar image
);

-- Books table (with cover image)
CREATE TABLE Books (
    BookID INT IDENTITY(1,1) PRIMARY KEY,
    BookName VARCHAR(100) NOT NULL,
    Topic VARCHAR(100),
    AffiliateLink VARCHAR(255),
    IsPreviewAvailable BIT DEFAULT 0,
    CoverImage VARCHAR(255), -- For Amazon-style cover display
    Rating DECIMAL(3, 1) DEFAULT 0.0, -- Optional Amazon-like rating
    PreviewContent VARCHAR(500) -- Single preview or teaser
);

-- Service table (with avatar image)
CREATE TABLE Service (
    ServiceID INT IDENTITY(1,1) PRIMARY KEY,
    ServiceName VARCHAR(100) NOT NULL,
    ImageUrl VARCHAR(255) -- Path to service avatar image
);

-- Blog table (with avatar and content-supporting inline images)
CREATE TABLE Blog (
    BlogID INT IDENTITY(1,1) PRIMARY KEY,
    BlogName VARCHAR(100) NOT NULL,
    Topic VARCHAR(100),
    ImageUrl VARCHAR(255), -- Path to blog avatar image
    DetailedContent TEXT -- HTML or text with inline image references
);

-- UserCourses junction table (for many-to-many relationship)
CREATE TABLE UserCourses (
    UserID INT NOT NULL,
    CourseID INT NOT NULL,
    PRIMARY KEY (UserID, CourseID),
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (CourseID) REFERENCES Courses(CourseID)
);

-- Order table
CREATE TABLE [Order] (
    OrderID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT NOT NULL,
    ProductID INT, -- Could reference a Product table if added
    PaymentStatus VARCHAR(20) DEFAULT 'Pending',
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
    -- Add Product table if needed for e-commerce
);

-- Cart table
CREATE TABLE Cart (
    CartID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT NOT NULL,
    ProductID INT, -- Could reference a Product table
    Quantity INT DEFAULT 1,
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
    -- Add Product table if needed
);

-- AffiliateClick table
CREATE TABLE AffiliateClick (
    ClickID INT IDENTITY(1,1) PRIMARY KEY,
    BookID INT NOT NULL,
    UserID INT,
    ClickTime DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (BookID) REFERENCES Books(BookID),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

CREATE TABLE user_tokens (
    id INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT NOT NULL,
    token VARCHAR(255) NOT NULL,
    expiry_date DATETIME NOT NULL,
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);
INSERT INTO Users (Username, Email, Password, Role, GoogleID, Age, Name, Expertise)
VALUES ('IronMan', 'tony@stark.com', 'arc123', 'user', NULL, 45, 'Tony Stark', 'AI');

select * from Users
