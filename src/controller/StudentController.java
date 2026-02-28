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
