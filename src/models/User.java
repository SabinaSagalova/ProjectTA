package models;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private double budget;

    // Constructor with all parameters
    public User(String username, String password, double budget) {
        this.username = username;
        this.password = password;
        this.budget = budget;
    }

    // Overloaded constructor with default budget (if needed)
    public User(String username, String password) {
        this(username, password, 0.0); // Set default budget to 0.0
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    // Optional: Override toString() method to display user details
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", budget=" + budget +
                '}';
    }

    // Optional: Override equals() and hashCode() if needed
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return username != null ? username.equals(user.username) : user.username == null;
    }

    @Override
    public int hashCode() {
        return username != null ? username.hashCode() : 0;
    }
}
