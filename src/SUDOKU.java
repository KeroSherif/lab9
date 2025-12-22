
import controller.ControllerAdapter;
import controller.ControllerFacade;
import javax.swing.SwingUtilities;
import view.SudokuGUI;

public class SUDOKU {
    public static void main(String[] args) {

        
        ControllerFacade facade = new ControllerFacade();
        ControllerAdapter controller = new ControllerAdapter(facade);

        
        SwingUtilities.invokeLater(() -> {
            new SudokuGUI(controller);
        });
    }
}
