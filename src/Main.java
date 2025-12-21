
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        // Controller
        SudokuController controller = new SudokuController();

        // GUI
        SwingUtilities.invokeLater(() -> {
            new SudokuGUI(controller);
        });
    }
}
