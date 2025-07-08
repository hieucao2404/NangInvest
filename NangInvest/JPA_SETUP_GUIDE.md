# JPA Setup Guide for NangInvest

## Overview

This guide will help you set up JPA (Java Persistence API) with Hibernate for your NangInvest Maven project.

## Step 1: Dependencies ✅

Your Maven project already includes the necessary JPA dependencies:

- `jakarta.persistence-api` - JPA specification
- `hibernate-core` - Hibernate implementation
- `hibernate-hikaricp` - Connection pooling
- `mssql-jdbc` - SQL Server driver

## Step 2: Configuration Files

### 2.1 persistence.xml ✅

Located at: `src/main/resources/persistence.xml`

- Already configured with SQL Server connection
- Uses HikariCP connection pooling
- Set to `hibernate.hbm2ddl.auto=update` (safe for development)

### 2.2 Database Settings

Current configuration:

- **Database**: NangInvest
- **Server**: localhost:1433
- **User**: sa
- **Driver**: SQL Server

## Step 3: Entity Classes Setup

We'll add JPA annotations to your existing model classes:

### 3.1 User Entity (Primary)

- `@Entity` - Marks as JPA entity
- `@Table` - Maps to database table
- `@Id` - Primary key
- `@GeneratedValue` - Auto-increment
- `@Column` - Column mappings
- `@Enumerated` - For Role enum

### 3.2 Other Entities

- Book, Course, Cart, Order
- Service, Blog, Product
- AffiliateClick, GoogleAccount
- UserCourses, UserToken

## Step 4: Relationships

- User ↔ Cart (One-to-Many)
- User ↔ Order (One-to-Many)
- User ↔ UserCourses (One-to-Many)
- Course ↔ UserCourses (One-to-Many)
- And more...

## Step 5: Testing

- Create test classes
- Test entity persistence
- Test relationships
- Test queries

## Next Steps

1. Add JPA annotations to User entity
2. Add annotations to other entities
3. Create relationships
4. Test the setup
5. Create DAOs/Repositories

Let's start with Step 1!
