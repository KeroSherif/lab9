
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        // Controller (Facade + Adapter pattern)
        ControllerFacade facade = new ControllerFacade();
        ControllerAdapter controller = new ControllerAdapter(facade);

        // GUI
        SwingUtilities.invokeLater(() -> {
            new SudokuGUI(controller);
        });
    }
}
