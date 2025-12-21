/*
 * MainDemo - MVC Architecture Demonstration
 * 
 * This class demonstrates the complete MVC architecture with Adapter pattern:
 * - ControllerFacade: Implements Viewable (Controller side - uses Objects)
 * - ControllerAdapter: Implements Controllable (View side - uses primitives)
 * - The Adapter bridges the two incompatible interfaces
 */

import java.io.IOException;

public class MainDemo {
    public static void main(String[] args) {
        System.out.println("=== Sudoku MVC Architecture Demo ===\n");
        
        // Step 1: Instantiate the Controller (implements Viewable - works with Objects)
        Viewable controller = new ControllerFacade();
        System.out.println("✓ Controller created (Viewable interface - uses Objects)");
        
        // Step 2: Wrap the Controller with the Adapter (implements Controllable - works with primitives)
        Controllable adapter = new ControllerAdapter(controller);
        System.out.println("✓ Adapter created (Controllable interface - uses primitives)");
        
        // Step 3: Demonstrate the architecture in action
        System.out.println("\n--- Testing MVC Communication ---\n");
        
        try {
            // View layer calls adapter with primitives
            System.out.println("1. View requests catalog (primitive boolean[]):");
            boolean[] catalog = adapter.getCatalog();
            System.out.println("   - Has unfinished: " + catalog[0]);
            System.out.println("   - All modes exist: " + catalog[1]);
            
            System.out.println("\n2. View requests game with char 'E' (EASY):");
            int[][] gameBoard = adapter.getGame('E');
            System.out.println("   - Received board: " + gameBoard.length + "x" + gameBoard[0].length);
            
            System.out.println("\n3. View logs user action:");
            UserAction action = new UserAction(3, 5, 7, 0);
            adapter.logUserAction(action);
            
            System.out.println("\n4. View verifies game board:");
            boolean[][] verification = adapter.verifyGame(gameBoard);
            System.out.println("   - Verification result: " + verification.length + "x" + verification[0].length + " matrix");
            
            System.out.println("\n5. View requests solution:");
            int[][] solution = adapter.solveGame(gameBoard);
            System.out.println("   - Solution received: " + solution.length + "x" + solution[0].length);
            
            System.out.println("\n=== Architecture Test Complete ===");
            System.out.println("\nThe Adapter successfully bridges:");
            System.out.println("  • View Layer (primitives: int[][], char, boolean[])");
            System.out.println("  • Controller Layer (objects: Game, Catalog, DifficultyEnum)");
            
            System.out.println("\n--- Architecture Flow ---");
            System.out.println("View (primitives) → ControllerAdapter → ControllerFacade (objects)");
            System.out.println("                    ↑ Converts types ↑");
            
        } catch (NotFoundException e) {
            System.err.println("Error: Game not found - " + e.getMessage());
        } catch (InvalidGameException e) {
            System.err.println("Error: Invalid game - " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error: IO exception - " + e.getMessage());
        }
    }
}
