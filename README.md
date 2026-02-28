# Java MVC Architecture Tutorial — Build a Student Management System

> **Tools:** Java 17+, VS Code  
> **What you'll build:** A fully working Student Management desktop application using MVC architecture with Java Swing UI

---

## Table of Contents

1. [What is MVC?](#1-what-is-mvc)
2. [Prerequisites & Setup](#2-prerequisites--setup)
3. [Project Structure](#3-project-structure)
4. [Phase 1 — The Model](#4-phase-1--the-model)
5. [Phase 2 — The View (UI)](#5-phase-2--the-view-ui)
6. [Phase 3 — The Controller](#6-phase-3--the-controller)
7. [Phase 4 — Wiring It All Together](#7-phase-4--wiring-it-all-together)
8. [Phase 5 — Run the Application](#8-phase-5--run-the-application)
9. [How MVC Works in This App](#9-how-mvc-works-in-this-app)
10. [Exercises for Students](#10-exercises-for-students)

---

## 1. What is MVC?

MVC (Model-View-Controller) is a software design pattern that separates an application into three interconnected components:

```
┌─────────────────────────────────────────────────┐
│                                                   │
│   ┌─────────┐    ┌──────────────┐    ┌─────────┐ │
│   │  MODEL   │◄───│  CONTROLLER  │◄───│  VIEW   │ │
│   │          │───►│              │───►│  (UI)   │ │
│   └─────────┘    └──────────────┘    └─────────┘ │
│                                                   │
│   Data & Logic    Handles Events     User Interface│
│   (Student.java)  (StudentController) (Swing GUI)  │
│                                                   │
└─────────────────────────────────────────────────┘
```

| Component | Responsibility | In Our App |
|-----------|---------------|------------|
| **Model** | Manages data, business logic, rules | `Student.java`, `StudentModel.java` |
| **View** | Displays data, captures user input | `StudentView.java` (Swing GUI) |
| **Controller** | Connects Model ↔ View, handles events | `StudentController.java` |

**Why MVC?**
- **Separation of Concerns** — each part has one job
- **Testability** — you can test logic without UI
- **Maintainability** — change UI without touching business logic
- **Team Collaboration** — different developers can work on different layers

---

## 2. Prerequisites & Setup

### Install Java JDK 17+

**Windows/Mac:**  
Download from [https://adoptium.net](https://adoptium.net) and install.

**Verify installation:**
```bash
java --version
javac --version
```

You should see something like:
```
openjdk 17.0.x 2024-xx-xx
```

### Install VS Code

Download from [https://code.visualstudio.com](https://code.visualstudio.com)

### Install VS Code Extensions

Open VS Code → Extensions (Ctrl+Shift+X) → Search and install:

1. **Extension Pack for Java** (by Microsoft) — This single pack installs everything you need:
   - Language Support for Java
   - Debugger for Java
   - Java Test Runner
   - Maven for Java
   - Project Manager for Java

### Create the Project

Open a terminal in VS Code (Ctrl+`) and run:

```bash
mkdir StudentMVC
cd StudentMVC
mkdir -p src/model src/view src/controller
```

Your folder structure should look like:
```
StudentMVC/
├── src/
│   ├── model/
│   ├── view/
│   ├── controller/
│   └── App.java
└── README.md
```

**Open in VS Code:**
```bash
code .
```

---

## 3. Project Structure

Here is the complete project structure we will build:

```
StudentMVC/
├── src/
│   ├── model/
│   │   ├── Student.java              ← Data class (one student)
│   │   └── StudentModel.java         ← Business logic (CRUD operations)
│   ├── view/
│   │   └── StudentView.java          ← Swing GUI (the UI)
│   ├── controller/
│   │   └── StudentController.java    ← Event handler (connects Model ↔ View)
│   └── App.java                      ← Entry point (main method)
└── README.md
```

We'll build each file step by step. **Follow the order — Model → View → Controller → App.**

---

## 4. Phase 1 — The Model

The Model handles all data and business logic. It knows nothing about the UI.

### File: `src/model/Student.java`

This class represents a single student record.

```java
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
```

**Key Points:**
- This is a Plain Old Java Object (POJO)
- No UI code, no controller references
- Just data + getters/setters

### File: `src/model/StudentModel.java`

This class manages the collection of students — the business logic layer.

```java
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
```

**Key Points:**
- Full CRUD operations (Create, Read, Update, Delete) + Search
- Uses ArrayList as an in-memory "database"
- Returns copies of lists to prevent external modification
- Pre-loaded with sample data for immediate visual feedback

---

## 5. Phase 2 — The View (UI)

The View is the user interface built with Java Swing. It displays data and captures user input but contains **no business logic**.

### File: `src/view/StudentView.java`

```java
package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import model.Student;

/**
 * StudentView - The GUI layer of the MVC pattern.
 * This class ONLY handles display and user input.
 * It does NOT contain any business logic.
 * The Controller will attach event listeners to the buttons.
 */
public class StudentView extends JFrame {

    // ──── Input Fields ────
    private JTextField nameField;
    private JTextField emailField;
    private JTextField gpaField;
    private JTextField searchField;

    // ──── Buttons ────
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton searchButton;

    // ──── Table ────
    private JTable studentTable;
    private DefaultTableModel tableModel;

    // ──── Status Bar ────
    private JLabel statusLabel;

    // ──── Currently selected student ID (for update/delete) ────
    private int selectedStudentId = -1;

    public StudentView() {
        initializeUI();
    }

    /**
     * Build the entire user interface
     */
    private void initializeUI() {
        // ── Window Settings ──
        setTitle("Student Management System — MVC Demo");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        setMinimumSize(new Dimension(750, 500));

        // ── Main Layout ──
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 245, 250));

        // ── Header ──
        JLabel headerLabel = new JLabel("Student Management System", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerLabel.setForeground(new Color(44, 62, 80));
        headerLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // ── Left Panel: Form ──
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.WEST);

        // ── Center Panel: Table ──
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // ── Bottom: Status Bar ──
        statusLabel = new JLabel(" Ready");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(100, 100, 100));
        statusLabel.setBorder(new EmptyBorder(5, 5, 0, 0));
        mainPanel.add(statusLabel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * Creates the form panel (left side) with input fields and buttons
     */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(280, 0));
        panel.setBackground(new Color(245, 245, 250));

        // ── Input Fields Section ──
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 220)),
                " Student Details ",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13),
                new Color(44, 62, 80)
            ),
            new EmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(createLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        nameField = createTextField();
        inputPanel.add(nameField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        inputPanel.add(createLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        emailField = createTextField();
        inputPanel.add(emailField, gbc);

        // GPA
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        inputPanel.add(createLabel("GPA:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        gpaField = createTextField();
        inputPanel.add(gpaField, gbc);

        panel.add(inputPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // ── Buttons Section ──
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        buttonPanel.setBackground(new Color(245, 245, 250));
        buttonPanel.setBorder(new EmptyBorder(0, 5, 0, 5));

        addButton = createButton("Add Student", new Color(46, 204, 113));
        updateButton = createButton("Update", new Color(52, 152, 219));
        deleteButton = createButton("Delete", new Color(231, 76, 60));
        clearButton = createButton("Clear", new Color(149, 165, 166));

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        panel.add(buttonPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // ── Search Section ──
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 220)),
                " Search ",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13),
                new Color(44, 62, 80)
            ),
            new EmptyBorder(10, 10, 10, 10)
        ));

        searchField = createTextField();
        searchButton = createButton("Search", new Color(155, 89, 182));

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        panel.add(searchPanel);

        return panel;
    }

    /**
     * Creates the table panel (center) that displays student records
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 220)),
                " Student Records ",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13),
                new Color(44, 62, 80)
            ),
            new EmptyBorder(5, 5, 5, 5)
        ));

        // Table Model (columns)
        String[] columns = {"ID", "Name", "Email", "GPA"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };

        // Table Setup
        studentTable = new JTable(tableModel);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        studentTable.setRowHeight(28);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.setGridColor(new Color(230, 230, 230));
        studentTable.setShowGrid(true);

        // Header styling
        studentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        studentTable.getTableHeader().setBackground(new Color(52, 73, 94));
        studentTable.getTableHeader().setForeground(Color.WHITE);
        studentTable.getTableHeader().setPreferredSize(new Dimension(0, 35));

        // Center-align the ID and GPA columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        studentTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        studentTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        // Set column widths
        studentTable.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        studentTable.getColumnModel().getColumn(1).setPreferredWidth(150);  // Name
        studentTable.getColumnModel().getColumn(2).setPreferredWidth(200);  // Email
        studentTable.getColumnModel().getColumn(3).setPreferredWidth(60);   // GPA

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ──── Helper Methods for UI Components ────

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(44, 62, 80));
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField(15);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setMargin(new Insets(5, 8, 5, 8));
        return field;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(0, 35));
        return button;
    }

    // ══════════════════════════════════════════════════════════
    // PUBLIC METHODS — Used by the Controller
    // These are the ONLY way the Controller interacts with the View
    // ══════════════════════════════════════════════════════════

    // ── Getters for input values ──
    public String getNameInput() { return nameField.getText().trim(); }
    public String getEmailInput() { return emailField.getText().trim(); }
    public String getGpaInput() { return gpaField.getText().trim(); }
    public String getSearchInput() { return searchField.getText().trim(); }
    public int getSelectedStudentId() { return selectedStudentId; }

    // ── Set selected student ID (called when table row is clicked) ──
    public void setSelectedStudentId(int id) { this.selectedStudentId = id; }

    // ── Populate input fields (when clicking a table row) ──
    public void setInputFields(String name, String email, String gpa) {
        nameField.setText(name);
        emailField.setText(email);
        gpaField.setText(gpa);
    }

    // ── Clear all input fields ──
    public void clearInputFields() {
        nameField.setText("");
        emailField.setText("");
        gpaField.setText("");
        searchField.setText("");
        selectedStudentId = -1;
    }

    // ── Update status bar message ──
    public void setStatus(String message) {
        statusLabel.setText(" " + message);
    }

    // ── Refresh the table with a list of students ──
    public void displayStudents(List<Student> students) {
        tableModel.setRowCount(0); // Clear existing rows
        for (Student s : students) {
            tableModel.addRow(new Object[]{
                s.getId(),
                s.getName(),
                s.getEmail(),
                String.format("%.2f", s.getGpa())
            });
        }
    }

    // ── Show dialog messages ──
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public boolean showConfirm(String message) {
        int result = JOptionPane.showConfirmDialog(this, message, "Confirm",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }

    // ══════════════════════════════════════════════════════════
    // LISTENER REGISTRATION — The Controller attaches its handlers here
    // This is the key MVC pattern: View doesn't know WHO is listening,
    // it just provides a way to register listeners.
    // ══════════════════════════════════════════════════════════

    public void addAddListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }

    public void addUpdateListener(ActionListener listener) {
        updateButton.addActionListener(listener);
    }

    public void addDeleteListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }

    public void addClearListener(ActionListener listener) {
        clearButton.addActionListener(listener);
    }

    public void addSearchListener(ActionListener listener) {
        searchButton.addActionListener(listener);
    }

    public void addTableSelectionListener(javax.swing.event.ListSelectionListener listener) {
        studentTable.getSelectionModel().addListSelectionListener(listener);
    }

    // ── Get selected row data from table ──
    public int getSelectedTableRow() {
        return studentTable.getSelectedRow();
    }

    public Object getTableValueAt(int row, int col) {
        return tableModel.getValueAt(row, col);
    }
}
```

**Key Points:**
- The View creates and manages all UI components
- It provides **public methods** for the Controller to read inputs and update displays
- It provides **listener registration methods** — the View doesn't know who handles the events
- **No business logic** — it doesn't know how students are stored or managed

---

## 6. Phase 3 — The Controller

The Controller is the "brain" that connects the Model and View. It listens for user actions (from the View) and updates the Model, then refreshes the View.

### File: `src/controller/StudentController.java`

```java
package controller;

import model.Student;
import model.StudentModel;
import view.StudentView;

import java.util.List;

/**
 * StudentController - The glue between Model and View.
 *
 * Responsibilities:
 * 1. Listen for user actions in the View (button clicks, table selections)
 * 2. Call appropriate methods on the Model
 * 3. Update the View with results
 *
 * The Controller is the ONLY class that knows about both Model and View.
 */
public class StudentController {
    private StudentModel model;
    private StudentView view;

    public StudentController(StudentModel model, StudentView view) {
        this.model = model;
        this.view = view;

        // ── Register event listeners ──
        // The Controller tells the View: "when this button is clicked, call MY method"
        this.view.addAddListener(e -> addStudent());
        this.view.addUpdateListener(e -> updateStudent());
        this.view.addDeleteListener(e -> deleteStudent());
        this.view.addClearListener(e -> clearForm());
        this.view.addSearchListener(e -> searchStudents());
        this.view.addTableSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectStudent();
            }
        });

        // ── Initial load: display all students ──
        refreshTable();
    }

    /**
     * ADD — Validates input, creates a student, refreshes the table
     */
    private void addStudent() {
        // Step 1: Get data from View
        String name = view.getNameInput();
        String email = view.getEmailInput();
        String gpaText = view.getGpaInput();

        // Step 2: Validate
        if (name.isEmpty() || email.isEmpty() || gpaText.isEmpty()) {
            view.showError("Please fill in all fields.");
            return;
        }

        double gpa;
        try {
            gpa = Double.parseDouble(gpaText);
            if (gpa < 0.0 || gpa > 4.0) {
                view.showError("GPA must be between 0.0 and 4.0");
                return;
            }
        } catch (NumberFormatException ex) {
            view.showError("GPA must be a valid number (e.g., 3.75)");
            return;
        }

        // Step 3: Update Model
        Student added = model.addStudent(name, email, gpa);

        // Step 4: Update View
        refreshTable();
        view.clearInputFields();
        view.setStatus("Added: " + added.getName() + " (ID: " + added.getId() + ")");
    }

    /**
     * UPDATE — Modifies the selected student record
     */
    private void updateStudent() {
        int id = view.getSelectedStudentId();
        if (id == -1) {
            view.showError("Please select a student from the table first.");
            return;
        }

        String name = view.getNameInput();
        String email = view.getEmailInput();
        String gpaText = view.getGpaInput();

        if (name.isEmpty() || email.isEmpty() || gpaText.isEmpty()) {
            view.showError("Please fill in all fields.");
            return;
        }

        double gpa;
        try {
            gpa = Double.parseDouble(gpaText);
            if (gpa < 0.0 || gpa > 4.0) {
                view.showError("GPA must be between 0.0 and 4.0");
                return;
            }
        } catch (NumberFormatException ex) {
            view.showError("GPA must be a valid number.");
            return;
        }

        if (model.updateStudent(id, name, email, gpa)) {
            refreshTable();
            view.clearInputFields();
            view.setStatus("Updated student ID: " + id);
        } else {
            view.showError("Student not found.");
        }
    }

    /**
     * DELETE — Removes the selected student after confirmation
     */
    private void deleteStudent() {
        int id = view.getSelectedStudentId();
        if (id == -1) {
            view.showError("Please select a student from the table first.");
            return;
        }

        Student student = model.getStudentById(id);
        if (student == null) {
            view.showError("Student not found.");
            return;
        }

        // Ask for confirmation before deleting
        boolean confirmed = view.showConfirm(
            "Are you sure you want to delete " + student.getName() + "?"
        );

        if (confirmed) {
            model.deleteStudent(id);
            refreshTable();
            view.clearInputFields();
            view.setStatus("Deleted: " + student.getName());
        }
    }

    /**
     * SEARCH — Filters the table based on name search
     */
    private void searchStudents() {
        String searchTerm = view.getSearchInput();
        if (searchTerm.isEmpty()) {
            refreshTable(); // Show all if search is empty
            view.setStatus("Showing all students");
        } else {
            List<Student> results = model.searchByName(searchTerm);
            view.displayStudents(results);
            view.setStatus("Found " + results.size() + " result(s) for '" + searchTerm + "'");
        }
    }

    /**
     * SELECT — When a table row is clicked, populate the form fields
     */
    private void selectStudent() {
        int selectedRow = view.getSelectedTableRow();
        if (selectedRow >= 0) {
            int id = (int) view.getTableValueAt(selectedRow, 0);
            String name = (String) view.getTableValueAt(selectedRow, 1);
            String email = (String) view.getTableValueAt(selectedRow, 2);
            String gpa = (String) view.getTableValueAt(selectedRow, 3);

            view.setSelectedStudentId(id);
            view.setInputFields(name, email, gpa);
            view.setStatus("Selected: " + name + " (ID: " + id + ")");
        }
    }

    /**
     * CLEAR — Reset the form
     */
    private void clearForm() {
        view.clearInputFields();
        refreshTable();
        view.setStatus("Ready");
    }

    /**
     * REFRESH — Reload all students from Model into View
     */
    private void refreshTable() {
        List<Student> allStudents = model.getAllStudents();
        view.displayStudents(allStudents);
        view.setStatus("Total students: " + model.getStudentCount());
    }
}
```

**Key Points:**
- The Controller holds references to **both** Model and View
- It registers event listeners in its constructor
- Each method follows the pattern: **Get from View → Validate → Update Model → Refresh View**
- Input validation happens here (not in Model or View)

---

## 7. Phase 4 — Wiring It All Together

### File: `src/App.java`

This is the entry point that creates all three components and connects them.

```java
import model.StudentModel;
import view.StudentView;
import controller.StudentController;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * App - The entry point of the application.
 *
 * This is where MVC comes together:
 * 1. Create the Model (data layer)
 * 2. Create the View (UI layer)
 * 3. Create the Controller (connects them)
 * 4. Show the View
 *
 * Notice: The Model and View don't know about each other.
 * Only the Controller knows about both.
 */
public class App {
    public static void main(String[] args) {
        // Use SwingUtilities to ensure GUI runs on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Set a modern look-and-feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Fall back to default if system L&F fails
                System.out.println("Using default look and feel.");
            }

            // ═══════════════════════════════════════
            // THIS IS MVC IN ACTION:
            // ═══════════════════════════════════════

            // 1. Create the Model (knows nothing about UI)
            StudentModel model = new StudentModel();

            // 2. Create the View (knows nothing about data logic)
            StudentView view = new StudentView();

            // 3. Create the Controller (connects Model ↔ View)
            StudentController controller = new StudentController(model, view);

            // 4. Show the application
            view.setVisible(true);

            System.out.println("Application started successfully!");
            System.out.println("MVC Components initialized:");
            System.out.println("  - Model: StudentModel (manages " + model.getStudentCount() + " students)");
            System.out.println("  - View: StudentView (Swing GUI)");
            System.out.println("  - Controller: StudentController (event handler)");
        });
    }
}
```

---

## 8. Phase 5 — Run the Application

### Option A: Compile and Run from Terminal

```bash
# Navigate to project root
cd StudentMVC

# Compile all Java files
javac -d out src/model/*.java src/view/*.java src/controller/*.java src/App.java

# Run the application
java -cp out App
```

### Option B: Run from VS Code

1. Open `src/App.java`
2. Click the **"Run"** button above the `main` method (or press `F5`)
3. VS Code will compile and run the application automatically

### What You Should See

When the application launches, you'll see:

```
┌─────────────────────────────────────────────────────────────┐
│           Student Management System — MVC Demo              │
├──────────────────────┬──────────────────────────────────────┤
│  Student Details     │  Student Records                     │
│  ┌────────────────┐  │  ┌──────┬──────────┬────────┬─────┐ │
│  │ Name: [      ] │  │  │ ID   │ Name     │ Email  │ GPA │ │
│  │ Email:[      ] │  │  ├──────┼──────────┼────────┼─────┤ │
│  │ GPA:  [      ] │  │  │ 1    │ Alice J. │ ali... │ 3.80│ │
│  └────────────────┘  │  │ 2    │ Bob S.   │ bob... │ 3.50│ │
│                      │  │ 3    │ Carol W. │ car... │ 3.90│ │
│ [Add] [Update]       │  └──────┴──────────┴────────┴─────┘ │
│ [Delete] [Clear]     │                                      │
│                      │                                      │
│  Search              │                                      │
│  [          ] [🔍]   │                                      │
├──────────────────────┴──────────────────────────────────────┤
│  Total students: 3                                          │
└─────────────────────────────────────────────────────────────┘
```

### Try These Actions

| Action | Steps | What Happens (MVC Flow) |
|--------|-------|------------------------|
| **Add** | Type name, email, GPA → click "Add Student" | View → Controller → Model → View refreshes |
| **Select** | Click a row in the table | View fires event → Controller reads row → View fills form |
| **Update** | Select a row, edit fields → click "Update" | View → Controller validates → Model updates → View refreshes |
| **Delete** | Select a row → click "Delete" → confirm | View → Controller → Model removes → View refreshes |
| **Search** | Type a name → click "Search" | View → Controller → Model filters → View shows results |
| **Clear** | Click "Clear" | View → Controller → View resets form, shows all students |

---

## 9. How MVC Works in This App

### The Data Flow

```
USER CLICKS "Add Student"
         │
         ▼
    ┌─────────┐     getNameInput()      ┌──────────────┐
    │  VIEW    │ ───────────────────────►│  CONTROLLER  │
    │  (Swing) │     getEmailInput()     │              │
    └─────────┘     getGpaInput()        │  addStudent()│
         ▲                               │  - validates │
         │                               │  - calls     │
         │           displayStudents()   │    model     │
         │◄──────────────────────────────│              │
         │           setStatus()         └──────┬───────┘
         │                                      │
         │                                      │ addStudent(name, email, gpa)
         │                                      ▼
         │                               ┌─────────┐
         │                               │  MODEL   │
         │                               │          │
         │                               │ students │
         │                               │ ArrayList│
         │                               └─────────┘
```

### What Each Component Knows

| Component | Knows About | Does NOT Know About |
|-----------|------------|-------------------|
| **Model** | Its own data (Student, ArrayList) | View, Controller, UI, buttons |
| **View** | How to display things, Swing components | Model, how data is stored, business rules |
| **Controller** | Both Model AND View | How View renders, how Model stores internally |

---

## 10. Exercises for Students

### Exercise 1: Add a "Sort by GPA" Button
- Add a button in the View
- Add a listener registration method in the View
- Implement sorting logic in the Controller (use `Collections.sort()`)
- The Model stays unchanged!

### Exercise 2: Add Input Validation
- Email should contain "@" and "."
- Name should be at least 2 characters
- Where should validation go? (Answer: Controller)

### Exercise 3: Add a "Statistics" Panel
- Show: average GPA, highest GPA student, lowest GPA student
- Add a method in the Model to compute stats
- Display results in the View via the Controller

### Exercise 4: Add Persistence (Advanced)
- Save students to a CSV file when the app closes
- Load students from CSV when the app starts
- Only the Model needs to change — this proves MVC's power!

---

## Summary

| What We Learned | Details |
|----------------|---------|
| **MVC Pattern** | Separate concerns into Model, View, Controller |
| **Java Swing** | Build desktop GUIs with JFrame, JTable, JPanel, JButton |
| **Event Handling** | ActionListener, ListSelectionListener |
| **CRUD Operations** | Create, Read, Update, Delete pattern |
| **Separation of Concerns** | Each class has one responsibility |
| **Why MVC Matters** | Easier to test, maintain, and extend |

---

## Quick Reference: Files to Create

| Order | File | Lines | Purpose |
|-------|------|-------|---------|
| 1 | `src/model/Student.java` | ~40 | Data class |
| 2 | `src/model/StudentModel.java` | ~80 | Business logic |
| 3 | `src/view/StudentView.java` | ~280 | Swing GUI |
| 4 | `src/controller/StudentController.java` | ~170 | Event handling |
| 5 | `src/App.java` | ~35 | Entry point |

**Total: ~605 lines of code across 5 files**

---

*Built as a teaching tutorial for MVC architecture with Java Swing.*
