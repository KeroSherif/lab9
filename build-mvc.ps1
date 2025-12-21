# Build script for MVC Architecture Demo
# Compiles only the files needed for the MVC demonstration

Write-Host "=== Building MVC Architecture Demo ===" -ForegroundColor Cyan

$srcDir = "c:\Users\Mohamed\OneDrive\Documents\Lab 9 prog2\lab9\src"
$binDir = "c:\Users\Mohamed\OneDrive\Documents\Lab 9 prog2\lab9\bin"

# Create bin directory if it doesn't exist
if (!(Test-Path $binDir)) {
    New-Item -ItemType Directory -Path $binDir | Out-Null
    Write-Host "Created bin directory" -ForegroundColor Green
}

# Clean bin directory
Remove-Item "$binDir\*.class" -ErrorAction SilentlyContinue

cd $srcDir

Write-Host "`nStep 1: Compiling core exception classes..." -ForegroundColor Yellow
javac -d $binDir NotFoundException.java InvalidGameException.java 2>&1 | Out-String | Write-Host

Write-Host "`nStep 2: Compiling data classes..." -ForegroundColor Yellow
javac -d $binDir DifficultyEnum.java Catalog.java Game.java UserAction.java 2>&1 | Out-String | Write-Host

Write-Host "`nStep 3: Compiling interfaces..." -ForegroundColor Yellow
# Skip interfaces with SolutionInvalidException for now - we'll modify them

Write-Host "`nStep 4: Creating simplified versions for demo..." -ForegroundColor Yellow

# Copy and modify files to remove SolutionInvalidException dependency
$tempFiles = @()

# Create SimpleViewable interface
$viewableContent = @"
import java.io.IOException;

public interface SimpleViewable {
    Catalog getCatalog();
    Game getGame(DifficultyEnum level) throws NotFoundException;
    String verifyGame(Game game);
    int[] solveGame(Game game) throws InvalidGameException;
    void logUserAction(String userAction) throws IOException;
}
"@

Set-Content -Path "$srcDir\SimpleViewable.java" -Value $viewableContent
javac -d $binDir -cp $binDir SimpleViewable.java

# Create SimpleControllable interface
$controllableContent = @"
import java.io.IOException;

public interface SimpleControllable {
    boolean[] getCatalog();
    int[][] getGame(char level) throws NotFoundException;
    boolean[][] verifyGame(int[][] game);
    int[][] solveGame(int[][] game) throws InvalidGameException;
    void logUserAction(UserAction userAction) throws IOException;
}
"@

Set-Content -Path "$srcDir\SimpleControllable.java" -Value $controllableContent
javac -d $binDir -cp $binDir SimpleControllable.java

# Create SimpleFacade
$facadeContent = @"
import java.io.IOException;

public class SimpleFacade implements SimpleViewable {
    @Override
    public Catalog getCatalog() {
        return new Catalog(false, true);
    }
    
    @Override
    public Game getGame(DifficultyEnum level) throws NotFoundException {
        int[][] dummyBoard = new int[9][9];
        return new Game(dummyBoard, level);
    }
    
    @Override
    public String verifyGame(Game game) {
        return "VALID";
    }
    
    @Override
    public int[] solveGame(Game game) throws InvalidGameException {
        return new int[81];
    }
    
    @Override
    public void logUserAction(String userAction) throws IOException {
        System.out.println("LOG: " + userAction);
    }
}
"@

Set-Content -Path "$srcDir\SimpleFacade.java" -Value $facadeContent
javac -d $binDir -cp $binDir SimpleFacade.java

# Create SimpleAdapter
$adapterContent = @"
import java.io.IOException;

public class SimpleAdapter implements SimpleControllable {
    private final SimpleViewable controller;
    
    public SimpleAdapter(SimpleViewable controller) {
        this.controller = controller;
    }
    
    @Override
    public boolean[] getCatalog() {
        Catalog catalog = controller.getCatalog();
        boolean[] result = new boolean[2];
        result[0] = catalog.hasUnfinished();
        result[1] = catalog.areModesReady();
        return result;
    }
    
    @Override
    public int[][] getGame(char level) throws NotFoundException {
        DifficultyEnum difficulty = charToDifficulty(level);
        Game game = controller.getGame(difficulty);
        return game.getBoard();
    }
    
    @Override
    public boolean[][] verifyGame(int[][] game) {
        Game gameObj = new Game(game, DifficultyEnum.EASY);
        String verificationResult = controller.verifyGame(gameObj);
        boolean[][] result = new boolean[9][9];
        boolean isValid = verificationResult.equals("VALID");
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                result[i][j] = isValid;
            }
        }
        return result;
    }
    
    @Override
    public int[][] solveGame(int[][] game) throws InvalidGameException {
        Game gameObj = new Game(game, DifficultyEnum.EASY);
        int[] flatSolution = controller.solveGame(gameObj);
        int[][] solution = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                solution[i][j] = flatSolution[i * 9 + j];
            }
        }
        return solution;
    }
    
    @Override
    public void logUserAction(UserAction userAction) throws IOException {
        String actionString = userAction.toString();
        controller.logUserAction(actionString);
    }
    
    private DifficultyEnum charToDifficulty(char level) {
        switch (Character.toUpperCase(level)) {
            case 'E': return DifficultyEnum.EASY;
            case 'M': return DifficultyEnum.MEDIUM;
            case 'H': return DifficultyEnum.HARD;
            default: throw new IllegalArgumentException("Invalid difficulty level: " + level);
        }
    }
}
"@

Set-Content -Path "$srcDir\SimpleAdapter.java" -Value $adapterContent
javac -d $binDir -cp $binDir SimpleAdapter.java

# Create Demo Main
$mainContent = @"
import java.io.IOException;

public class SimpleDemo {
    public static void main(String[] args) {
        System.out.println("=== Sudoku MVC Architecture Demo ===`n");
        
        SimpleViewable controller = new SimpleFacade();
        System.out.println("√ Controller created (SimpleViewable - uses Objects)");
        
        SimpleControllable adapter = new SimpleAdapter(controller);
        System.out.println("√ Adapter created (SimpleControllable - uses primitives)`n");
        
        System.out.println("--- Testing MVC Communication ---`n");
        
        try {
            System.out.println("1. View requests catalog (primitive boolean[]):");
            boolean[] catalog = adapter.getCatalog();
            System.out.println("   - Has unfinished: " + catalog[0]);
            System.out.println("   - All modes exist: " + catalog[1]);
            
            System.out.println("`n2. View requests game with char 'E' (EASY):");
            int[][] gameBoard = adapter.getGame('E');
            System.out.println("   - Received board: " + gameBoard.length + "x" + gameBoard[0].length);
            
            System.out.println("`n3. View logs user action:");
            UserAction action = new UserAction(3, 5, 7, 0);
            adapter.logUserAction(action);
            
            System.out.println("`n4. View verifies game board:");
            boolean[][] verification = adapter.verifyGame(gameBoard);
            System.out.println("   - Verification result: " + verification.length + "x" + verification[0].length + " matrix");
            
            System.out.println("`n5. View requests solution:");
            int[][] solution = adapter.solveGame(gameBoard);
            System.out.println("   - Solution received: " + solution.length + "x" + solution[0].length);
            
            System.out.println("`n=== Architecture Test Complete ===");
            System.out.println("`nThe Adapter successfully bridges:");
            System.out.println("  • View Layer (primitives: int[][], char, boolean[])");
            System.out.println("  • Controller Layer (objects: Game, Catalog, DifficultyEnum)");
            
        } catch (NotFoundException e) {
            System.err.println("Error: Game not found - " + e.getMessage());
        } catch (InvalidGameException e) {
            System.err.println("Error: Invalid game - " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error: IO exception - " + e.getMessage());
        }
    }
}
"@

Set-Content -Path "$srcDir\SimpleDemo.java" -Value $mainContent
javac -d $binDir -cp $binDir SimpleDemo.java

if ($LASTEXITCODE -eq 0) {
    Write-Host "`n√ BUILD SUCCESSFUL!" -ForegroundColor Green
    Write-Host "`nTo run the demo:" -ForegroundColor Cyan
    Write-Host "  java -cp '$binDir' SimpleDemo" -ForegroundColor White
    Write-Host "`nRunning demo now...`n" -ForegroundColor Cyan
    java -cp $binDir SimpleDemo
} else {
    Write-Host "`nX BUILD FAILED" -ForegroundColor Red
}
