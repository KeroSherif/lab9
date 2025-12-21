/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author monic
 */
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SudokuController implements Viewable {
    
    private static final String GAMES_DIR = "games/";
    private static final String EASY_DIR = GAMES_DIR + "easy/";
    private static final String MEDIUM_DIR = GAMES_DIR + "medium/";
    private static final String HARD_DIR = GAMES_DIR + "hard/";
    private static final String INCOMPLETE_DIR = GAMES_DIR + "incomplete/";
    private static final String LOG_FILE = INCOMPLETE_DIR + "moves.log";
    
    public SudokuController() {
        createDirectories();
    }
    
    private void createDirectories() {
        try {
            Files.createDirectories(Paths.get(EASY_DIR));
            Files.createDirectories(Paths.get(MEDIUM_DIR));
            Files.createDirectories(Paths.get(HARD_DIR));
            Files.createDirectories(Paths.get(INCOMPLETE_DIR));
        } catch (IOException e) {
            System.err.println("Error creating directories: " + e.getMessage());
        }
    }
    
    @Override
    public Catalog getCatalog() {
        boolean hasIncomplete = hasIncompleteGame();
        boolean hasEasy = hasGameInFolder(EASY_DIR);
        boolean hasMedium = hasGameInFolder(MEDIUM_DIR);
        boolean hasHard = hasGameInFolder(HARD_DIR);
        boolean hasAllLevels = hasEasy && hasMedium && hasHard;
        
        return new Catalog(hasIncomplete, hasAllLevels);
    }
    
    private boolean hasIncompleteGame() {
        File dir = new File(INCOMPLETE_DIR);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt") && !name.equals("moves.log"));
        return files != null && files.length > 0;
    }
    
    private boolean hasGameInFolder(String folderPath) {
        File dir = new File(folderPath);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
        return files != null && files.length > 0;
    }
    
    @Override
    public Game getGame(DifficultyEnum level) throws NotFoundException {
        String folderPath;
        
        switch (level) {
            case EASY:
                folderPath = EASY_DIR;
                break;
            case MEDIUM:
                folderPath = MEDIUM_DIR;
                break;
            case HARD:
                folderPath = HARD_DIR;
                break;
            case INCOMPLETE:
                folderPath = INCOMPLETE_DIR;
                break;
            default:
                throw new NotFoundException("Invalid difficulty level");
        }
        
        File dir = new File(folderPath);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt") && !name.equals("moves.log"));
        
        if (files == null || files.length == 0) {
            throw new NotFoundException("No game found for level: " + level);
        }
        
        File selectedFile = files[new Random().nextInt(files.length)];
        
        try {
            int[][] board = loadBoardFromFile(selectedFile);
            
            if (level != DifficultyEnum.INCOMPLETE) {
                saveBoardToFile(board, INCOMPLETE_DIR + "current.txt");
                new File(LOG_FILE).delete();
            }
            
            return new Game(board);
        } catch (IOException e) {
            throw new NotFoundException("Error loading game: " + e.getMessage());
        }
    }
    
    @Override
    public void driveGames(Game sourceGame) throws SolutionInvalidException {
        String verificationResult = verifyGame(sourceGame);
        
        if (!verificationResult.equals("valid")) {
            throw new SolutionInvalidException("Source solution is not valid: " + verificationResult);
        }
        
        RandomPairs randomPairs = new RandomPairs();
        
        Game easyGame = generateGame(sourceGame, randomPairs, 10);
        saveGameToFolder(easyGame, EASY_DIR);
        
        Game mediumGame = generateGame(sourceGame, randomPairs, 20);
        saveGameToFolder(mediumGame, MEDIUM_DIR);
        
        Game hardGame = generateGame(sourceGame, randomPairs, 25);
        saveGameToFolder(hardGame, HARD_DIR);
    }
    
    private Game generateGame(Game source, RandomPairs randomPairs, int cellsToRemove) {
        int[][] newBoard = new int[9][9];
        for (int i = 0; i < 9; i++) {
            newBoard[i] = source.board[i].clone();
        }
        
        List<int[]> positions = randomPairs.generateDistinctPairs(cellsToRemove);
        for (int[] pos : positions) {
            newBoard[pos[0]][pos[1]] = 0;
        }
        
        return new Game(newBoard);
    }
    
    private void saveGameToFolder(Game game, String folderPath) {
        try {
            String filename = folderPath + "game_" + System.currentTimeMillis() + ".txt";
            saveBoardToFile(game.board, filename);
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
    }
    
    @Override
    public String verifyGame(Game game) {
        List<String> invalidCells = new ArrayList<>();
        
        for (int i = 0; i < 9; i++) {
            Set<Integer> seen = new HashSet<>();
            for (int j = 0; j < 9; j++) {
                int val = game.board[i][j];
                if (val != 0) {
                    if (!seen.add(val)) {
                        invalidCells.add(i + "," + j);
                    }
                }
            }
        }
        
        for (int j = 0; j < 9; j++) {
            Set<Integer> seen = new HashSet<>();
            for (int i = 0; i < 9; i++) {
                int val = game.board[i][j];
                if (val != 0) {
                    if (!seen.add(val)) {
                        invalidCells.add(i + "," + j);
                    }
                }
            }
        }
        
        for (int blockRow = 0; blockRow < 3; blockRow++) {
            for (int blockCol = 0; blockCol < 3; blockCol++) {
                Set<Integer> seen = new HashSet<>();
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        int row = blockRow * 3 + i;
                        int col = blockCol * 3 + j;
                        int val = game.board[row][col];
                        if (val != 0) {
                            if (!seen.add(val)) {
                                invalidCells.add(row + "," + col);
                            }
                        }
                    }
                }
            }
        }
        
        boolean isComplete = true;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (game.board[i][j] == 0) {
                    isComplete = false;
                    break;
                }
            }
        }
        
        if (!invalidCells.isEmpty()) {
            return "invalid " + String.join(" ", invalidCells);
        } else if (isComplete) {
            return "valid";
        } else {
            return "incomplete";
        }
    }
    
    @Override
    public int[] solveGame(Game game) throws InvalidGameException {
        throw new InvalidGameException("Solver not implemented yet");
    }
    
    @Override
    public void logUserAction(String userAction) throws IOException {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(userAction);
            bw.newLine();
        }
    }
    
    private int[][] loadBoardFromFile(File file) throws IOException {
        int[][] board = new int[9][9];
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            for (int i = 0; i < 9; i++) {
                String line = br.readLine();
                if (line == null) {
                    throw new IOException("Invalid file format");
                }
                
                String[] values = line.trim().split("\\s+");
                if (values.length != 9) {
                    throw new IOException("Invalid row length");
                }
                
                for (int j = 0; j < 9; j++) {
                    board[i][j] = Integer.parseInt(values[j]);
                }
            }
        }
        
        return board;
    }
    
    private void saveBoardToFile(int[][] board, String filename) throws IOException {
        try (FileWriter fw = new FileWriter(filename);
             BufferedWriter bw = new BufferedWriter(fw)) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    bw.write(board[i][j] + " ");
                }
                bw.newLine();
            }
        }
    }
}