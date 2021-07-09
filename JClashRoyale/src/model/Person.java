package model;

/**
 * Person class, implements real player
 * @author Adibov & Armin Gh
 * @version 1.0
 */
public class Person {
    String username;
    Password password;

    /**
     * class constructor
     * @param username person username
     * @param password person password
     */
    public Person(String username, String password) {
        this.username = username;
        this.password = new Password(password);
    }
}
