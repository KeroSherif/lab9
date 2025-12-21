/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author monic
 */
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class ControllerFacade implements Controllable {
    
    private Viewable controller; 
    
    public ControllerFacade(Viewable controller) {
        this.controller = controller;
    }
    
    @Override
    public boolean[] getCatalog() {
        Catalog catalog = controller.getCatalog();
        
        return new boolean[]{
            catalog.current,      
            catalog.allModesExist 
        };
    }
    
    @Override
    public int[][] getGame(char level) throws NotFoundException {
        // تحويل من char إلى DifficultyEnum
        DifficultyEnum difficulty;
        
        switch (level) {
            case 'e':
                difficulty = DifficultyEnum.EASY;
                break;
            case 'm':
                difficulty = DifficultyEnum.MEDIUM;
                break;
            case 'h':
                difficulty = DifficultyEnum.HARD;
                break;
            case 'c':
                difficulty = DifficultyEnum.INCOMPLETE;
                break;
            default:
                throw new NotFoundException("Invalid difficulty level: " + level);
        }
        
        Game game = controller.getGame(difficulty);
        
        return game.board;
    }
    
    @Override
    public void driveGames(String sourcePath) throws SolutionInvalidException {
        try {
            int[][] board = loadBoardFromFile(sourcePath);
            
            Game sourceGame = new Game(board);
            
            controller.driveGames(sourceGame);
            
        } catch (IOException e) {
            throw new SolutionInvalidException("Error reading source file: " + e.getMessage());
        }
    }
    
    @Override
    public boolean[][] verifyGame(int[][] game) {
        Game g = new Game(game);
        
        String result = controller.verifyGame(g);
        
        boolean[][] validityMatrix = new boolean[9][9];
        
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                validityMatrix[i][j] = true;
            }
        }
        
        if (result.startsWith("invalid")) {
            String[] parts = result.split(" ");
            
            for (int i = 1; i < parts.length; i++) {
                String[] coords = parts[i].split(",");
                if (coords.length == 2) {
                    int x = Integer.parseInt(coords[0]);
                    int y = Integer.parseInt(coords[1]);
                    validityMatrix[x][y] = false;
                }
            }
        }
        
        return validityMatrix;
    }
    
    @Override
    public int[][] solveGame(int[][] game) throws InvalidGameException {
       
        Game g = new Game(game);
        
        int[] solution = controller.solveGame(g);
        
        return convertSolutionToMatrix(solution, game);
    }
    
    @Override
    public void logUserAction(UserAction userAction) throws IOException {
        String actionString = userAction.toString();
        
       
        controller.logUserAction(actionString);
    }
   
    private int[][] loadBoardFromFile(String filePath) throws IOException {
        int[][] board = new int[9][9];
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            for (int i = 0; i < 9; i++) {
                String line = br.readLine();
                if (line == null) {
                    throw new IOException("Invalid file format: not enough rows");
                }
                
                String[] values = line.trim().split("\\s+");
                if (values.length != 9) {
                    throw new IOException("Invalid row format at line " + (i + 1));
                }
                
                for (int j = 0; j < 9; j++) {
                    board[i][j] = Integer.parseInt(values[j]);
                }
            }
        }
        
        return board;
    }
    
    
    private int[][] convertSolutionToMatrix(int[] solution, int[][] originalBoard) {
        
        java.util.List<int[]> emptyCells = new java.util.ArrayList<>();
        
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (originalBoard[i][j] == 0) {
                    emptyCells.add(new int[]{i, j});
                }
            }
        }
        
        
        int[][] result = new int[emptyCells.size()][3];
        
        for (int i = 0; i < emptyCells.size() && i < solution.length; i++) {
            result[i][0] = emptyCells.get(i)[0]; 
            result[i][1] = emptyCells.get(i)[1]; 
            result[i][2] = solution[i];          
        }
        
        return result;
    }
}