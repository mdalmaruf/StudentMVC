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
        panel.setPreferredSize(new Dimension(360, 0));
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
        studentTable.getTableHeader().setForeground(Color.BLACK);
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
        button.setPreferredSize(new Dimension(110, 35));
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
