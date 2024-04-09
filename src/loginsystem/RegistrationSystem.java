package loginsystem;

import java.io.*;
import java.util.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;


/*

test
test
ABCDefgh!


*/


/**
 * This class handles the registration system for the login system.
 * It provides methods for registering users, encrypting passwords,
 * saving users to a file, loading users from a file, and checking
 * the uniqueness of usernames.
 * 
 * @author Xu Last Name
 */ 
public class RegistrationSystem {

    /**
     * Registers a new user by writing their username, email, authToken, id, and password to a file.
     * 
     * @param user The user to be registered.
     */
    public void register(User user) {
        try {
            // Initialize FileWriter
            FileWriter fw = new FileWriter("users.txt", true); // Append mode
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            // Write user details to file
            pw.println(user.getUsername() + "," + user.getEmail() + "," + user.getAuthToken() + "," + user.getId() + "," + user.getPassword()); 

            // Close writers
            pw.close();
            bw.close();
            fw.close();
        } catch (IOException e) {
            // Print error message if user cannot be registered
            System.out.println("User could not be registered");
        }
    }
    
    /**
     * Saves a user to a file by writing their username and password.
     * 
     * @param user The user to be saved.
     */
    public void saveUser(User user) {
        try {
            // Initialize FileWriter
            FileWriter fw = new FileWriter("users.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            
            // Write username and password to file
            pw.println(user.getUsername() + "," + user.getPassword()); 

            // Close writers
            pw.close();
            bw.close();
            fw.close();
        } catch (IOException e) {
            // Print error message if user cannot be saved
            System.out.println("User could not be saved");
        }
    }
    
    /**
     * Loads users from a file into an ArrayList.
     * 
     * @return An ArrayList containing all the loaded users.
     */
    public ArrayList<User> loadUsers() {
        ArrayList<User> users = new ArrayList<>();
        try {
            // Open file for reading
            File file = new File("users.txt");
            Scanner scanner = new Scanner(file);

            // Read each line from the file
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length == 5) {
                    // Extract user details from parts array
                    String email = parts[1];
                    String username = parts[0];
                    String password = parts[4];
                    String authToken = parts[2];
                    String id = parts[3];

                    // Create User object and add to ArrayList
                    users.add(new User(email, username, password, authToken, id));
                } else {
                    // Print error message if data format is invalid
                    System.out.println("Invalid data format in line: " + line);
                }
            }
            // Close scanner
            scanner.close();
        } catch (FileNotFoundException e) {
            // Print error message if file is not found
            System.out.println("File not found: " + e.getMessage());
        }
        return users;
    }
    
    /**
     * Checks if a username is unique by comparing it with existing usernames.
     * 
     * @param username The username to check for uniqueness.
     * @return true if the username is unique, false otherwise.
     */
    public boolean isUniqueName(String username) {
        // Load existing users
        ArrayList<User> users = loadUsers();
        
        // Iterate through each username
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false; // Not unique
            }
        }
        return true; // Unique
    }
    
    /**
     * Authenticates a user by checking if the provided email, password, and authToken match any existing user.
     * 
     * @param email The email of the user to authenticate.
     * @param password The password of the user to authenticate.
     * @param authToken The authentication token of the user to authenticate.
     * @return true if authentication is successful, false otherwise.
     */
    public boolean authenticate(String email, String password, String authToken) {
        // Load existing users
        ArrayList<User> users = loadUsers();

        // Iterate through the loaded users
        for (User user : users) {
            // Check if the email, password, and authToken match
            if (user.getEmail().equals(email) && user.getPassword().equals(password) && user.getAuthToken().equals(authToken)) {
                return true; // Authentication successful
            }
        }
        return false; // Authentication failed
    }
    
    /**
     * Checks if a password is strong.
     * 
     * @param password The password to check.
     * @return true if the password is strong, false otherwise.
     */
    public boolean strongPassword(String password) {
        // Convert password to char array
        char[] charArray = password.toCharArray();

        // Case 1: Length Check
        boolean lengthCheck = charArray.length >= 8 && charArray.length <= 32;

        // Case 2: Special Characters
        boolean specialChar = false;
        for (char c : charArray) {
            if (!Character.isLetterOrDigit(c)) {
                specialChar = true;
                break;
            }
        }

        // Case 3: Capital Letters
        boolean capitalChar = false;
        for (char c : charArray) {
            if (Character.isUpperCase(c)) {
                capitalChar = true;
                break;
            }
        }

        // Case 4: Weak Password Check
        boolean goodPass = !isWeakPassword(password);

        // Check all conditions
        return lengthCheck && specialChar && capitalChar && goodPass;
    }
    
    /**
     * Checks if a password is weak by comparing it with a list of bad passwords.
     * 
     * @param password The password to check.
     * @return true if the password is weak, false otherwise.
     */
    public boolean isWeakPassword(String password) {
        // Load the file containing bad passwords
        try (BufferedReader br = new BufferedReader(new FileReader("dictbadpass.txt"))) {
            String line;
            // Read each line from the file
            while ((line = br.readLine()) != null) {
                // Check if the password contains the bad password (case-insensitive)
                if (password.toLowerCase().contains(line.toLowerCase())) {
                    return true; // Password contains a bad password
                }
            }
        } catch (IOException e) {
            // Handle file reading errors
            System.out.println("File not read");
        }
        return false; // Password does not contain any bad passwords
    }

    /**
     * Placeholder method for encryption of passwords. 
     */
    private String encryption(String password) {
        try {
            // To be implemented
            // Java helper class to perform encryption
            MessageDigest md = MessageDigest.getInstance("MD5");
            // Give the helper function the password
            md.update(password.getBytes());
            // Perform the encryption
            byte byteData[] = md.digest();
            // To express the byte data as a hexadecimal number (the normal way)
            String encryptedPassword = "";
            for (int i = 0; i < byteData.length; ++i) {
                encryptedPassword += (Integer.toHexString((byteData[i] & 0xFF) | 0x100).substring(1,3));
            }
            return encryptedPassword;
            
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RegistrationSystem.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /**
     * Generates a random authentication token.
     * 
     * @return The generated authentication token.
     */
    public String generateAuthToken() {
        // Define characters for letters
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        // Define characters for digits
        String digits = "0123456789";

        // Create a String to store the authentication key
        String authToken = "";

        // Use Random object to generate random characters
        Random random = new Random();

        // Generate four random letters
        for (int i = 0; i < 4; i++) {
            // Append a random letter to the authentication key
            authToken += letters.charAt(random.nextInt(letters.length()));
        }

        // Generate three random digits
        for (int i = 0; i < 3; i++) {
            // Append a random digit to the authentication key
            authToken += digits.charAt(random.nextInt(digits.length()));
        }

        // Return the generated authentication key
        return authToken;
    }
    
    /**
     * Generates a random ID.
     * 
     * @return The generated ID.
     */
    public String generateId() {
        // Define characters for digits
        String digits = "0123456789";

        // Create a String to store the authentication key
        String id = "";

        // Use Random object to generate random characters
        Random random = new Random();

        // Generate ten random numbers
        for (int i = 0; i < 10; i++) {
            // Append a random letter to the authentication key
            id += digits.charAt(random.nextInt(digits.length()));
        }

        // Return the generated authentication key
        return id;
    }

    /**
     * Clears the content of the file "users.txt".
     */
    public void clearFile() {
        try {
            // Initialize FileWriter with overwrite mode
            FileWriter fw = new FileWriter("users.txt", false); // Overwrite mode
            fw.close();
            // Print message indicating successful file clearing
            System.out.println("File cleared successfully.");
        } catch (IOException e) {
            // Print error message if file clearing fails
            System.out.println("Error clearing file: " + e.getMessage());
        }
    }
}