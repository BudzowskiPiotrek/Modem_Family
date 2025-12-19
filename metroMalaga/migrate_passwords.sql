-- ============================================
-- Password Migration SQL Script
-- ============================================
-- This script prepares the database for BCrypt password hashing
-- 
-- IMPORTANT: Make a backup before running this!
--   mysqldump -u remoto -p centimetromalaga > backup_before_migration.sql
--
-- Step 1: Run this script
-- Step 2: Run the Java PasswordMigrationTool program
-- Step 3: Verify login works with the old passwords
-- ============================================

USE centimetromalaga;

-- Step 1: Modify the password column to support longer BCrypt hashes
-- BCrypt hashes are approximately 60 characters, we use 255 for safety
ALTER TABLE usuarios MODIFY COLUMN password VARCHAR(255) NOT NULL;

-- Step 2: Verify the change
DESCRIBE usuarios;

-- Step 3: Check current passwords (they should still be in plain text)
SELECT username, password, LENGTH(password) as password_length 
FROM usuarios;

-- After running the Java migration tool, verify passwords are hashed:
-- SELECT username, LEFT(password, 20) as password_start, LENGTH(password) as password_length 
-- FROM usuarios;
-- 
-- BCrypt hashes should start with $2a$ or $2b$ and be approximately 60 characters long
