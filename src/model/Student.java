package model;

/**
 * Student - Represents a single student entity.
 * This is a simple data class (POJO) with getters and setters.
 * The Model layer knows NOTHING about the View or Controller.
 */
public class Student {
    private int id;
    private String name;
    private String email;
    private double gpa;

    // Constructor
    public Student(int id, String name, String email, double gpa) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.gpa = gpa;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public double getGpa() { return gpa; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setGpa(double gpa) { this.gpa = gpa; }

    // For display purposes
    @Override
    public String toString() {
        return "Student{id=" + id + ", name='" + name + "', email='" + email + "', gpa=" + gpa + "}";
    }
}
