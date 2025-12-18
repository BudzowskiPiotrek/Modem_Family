package metroMalaga.Controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utility class for password hashing and verification using BCrypt algorithm.
 * BCrypt is a one-way hashing function, meaning passwords cannot be decrypted.
 * 
 * Usage:
 * - To hash a password: PasswordUtil.hashPassword("myPassword123")
 * - To verify a password: PasswordUtil.verifyPassword("myPassword123",
 * hashedFromDB)
 */
public class PasswordUtil {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * Hashes a plain text password using BCrypt.
     * 
     * @param plainPassword The password in plain text
     * @return The BCrypt hashed password (approximately 60 characters)
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return encoder.encode(plainPassword);
    }

    /**
     * Verifies if a plain text password matches a BCrypt hashed password.
     * 
     * @param plainPassword  The password in plain text to verify
     * @param hashedPassword The BCrypt hashed password from database
     * @return true if the password matches, false otherwise
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        try {
            return encoder.matches(plainPassword, hashedPassword);
        } catch (Exception e) {
            // If the hash is invalid or corrupted
            return false;
        }
    }

    /**
     * Checks if a given string is already a BCrypt hash.
     * BCrypt hashes start with $2a$, $2b$, or $2y$
     * 
     * @param password The string to check
     * @return true if it appears to be a BCrypt hash
     */
    public static boolean isBCryptHash(String password) {
        return password != null && password.matches("^\\$2[aby]\\$\\d{2}\\$.{53}$");
    }
}
