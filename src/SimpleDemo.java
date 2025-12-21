import java.io.IOException;

public class SimpleDemo {
    public static void main(String[] args) {
        System.out.println("=== Sudoku MVC Architecture Demo ===");
        System.out.println("");
        
        SimpleViewable controller = new SimpleFacade();
        System.out.println("Checkmark Controller created (SimpleViewable - uses Objects)");
        
        SimpleControllable adapter = new SimpleAdapter(controller);
        System.out.println("Checkmark Adapter created (SimpleControllable - uses primitives)");
        System.out.println("");
        
        System.out.println("--- Testing MVC Communication ---");
        System.out.println("");
        
        try {
            System.out.println("1. View requests catalog:");
            boolean[] catalog = adapter.getCatalog();
            System.out.println("   - Has unfinished: " + catalog[0]);
            System.out.println("   - All modes exist: " + catalog[1]);
            
            System.out.println("");
            System.out.println("2. View requests game (char E):");
            int[][] gameBoard = adapter.getGame('E');
            System.out.println("   - Board size: " + gameBoard.length + "x" + gameBoard[0].length);
            
            System.out.println("");
            System.out.println("3. View logs user action:");
            UserAction action = new UserAction(3, 5, 7, 0);
            adapter.logUserAction(action);
            
            System.out.println("");
            System.out.println("4. View verifies game:");
            boolean[][] verification = adapter.verifyGame(gameBoard);
            System.out.println("   - Result: " + verification.length + "x" + verification[0].length);
            
            System.out.println("");
            System.out.println("5. View requests solution:");
            int[][] solution = adapter.solveGame(gameBoard);
            System.out.println("   - Solution: " + solution.length + "x" + solution[0].length);
            
            System.out.println("");
            System.out.println("=== Test Complete ===");
            System.out.println("Adapter bridges View and Controller successfully!");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
