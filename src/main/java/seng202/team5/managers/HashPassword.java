package seng202.team5.managers;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class to hash the passwords.
 */
public class HashPassword {

    /**
     * The hashed password.
     */
    private final String hashedPassword;

    /**
     * Constructor for HashPassword. Hashes the Password passed to it.
     * @param password - password inputted by user
     */
    public HashPassword(final String password) {

        hashedPassword = hashPassword(password);
    }

    /**
     * Hashes a password.
     * @param password The password the user enters
     * @return The hashed password
     */
    public String hashPassword(final String password) {

        try {
            // Specifies Hashing Algorithm
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(password.getBytes());

            // Makes a byte array of password
            byte[] resultByteArray = messageDigest.digest();

            // Convert ByteArray to a String using hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (byte b : resultByteArray) {
                sb.append(String.format("%02x", b));
            }

            // Return String format of hashed password
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Gets the hashed password.
     * @return The hashed password
     */
    public String getHashedPassword() {

        return hashedPassword;
    }
}
