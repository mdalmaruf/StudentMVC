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
