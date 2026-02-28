package model;

import java.util.ArrayList;
import java.util.List;

/**
 * StudentModel - Manages all student data and business operations.
 * This acts as our "database" using an in-memory ArrayList.
 * In a real application, this would connect to a database.
 */
public class StudentModel {
    private List<Student> students;
    private int nextId;

    public StudentModel() {
        students = new ArrayList<>();
        nextId = 1;

        // Pre-load some sample data so the app isn't empty on startup
        addStudent("Alice Johnson", "alice@university.com", 3.8);
        addStudent("Bob Smith", "bob@university.com", 3.5);
        addStudent("Carol Williams", "carol@university.com", 3.9);
    }

    /**
     * CREATE - Add a new student
     * @return the newly created Student
     */
    public Student addStudent(String name, String email, double gpa) {
        Student student = new Student(nextId++, name, email, gpa);
        students.add(student);
        return student;
    }

    /**
     * READ - Get all students
     */
    public List<Student> getAllStudents() {
        return new ArrayList<>(students); // Return a copy to protect internal list
    }

    /**
     * READ - Find a student by ID
     */
    public Student getStudentById(int id) {
        for (Student s : students) {
            if (s.getId() == id) {
                return s;
            }
        }
        return null;
    }

    /**
     * UPDATE - Modify an existing student
     * @return true if student was found and updated
     */
    public boolean updateStudent(int id, String name, String email, double gpa) {
        Student student = getStudentById(id);
        if (student != null) {
            student.setName(name);
            student.setEmail(email);
            student.setGpa(gpa);
            return true;
        }
        return false;
    }

    /**
     * DELETE - Remove a student by ID
     * @return true if student was found and removed
     */
    public boolean deleteStudent(int id) {
        return students.removeIf(s -> s.getId() == id);
    }

    /**
     * SEARCH - Find students whose name contains the search term
     */
    public List<Student> searchByName(String searchTerm) {
        List<Student> results = new ArrayList<>();
        for (Student s : students) {
            if (s.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                results.add(s);
            }
        }
        return results;
    }

    /**
     * Get total number of students
     */
    public int getStudentCount() {
        return students.size();
    }
}
