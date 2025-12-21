/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab9;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author DANAH
 */
public class StorageManager {
   private final String INCOMPLETE_DIR = "incomplete/";
    private final String GAME_FILE = "incomplete/game.txt";
    private final String LOG_FILE = "incomplete/log.txt";
    
   public void saveIncompleteGame(int[][] board, String moveLog) throws IOException {
       File dir = new File(INCOMPLETE_DIR);
        if (!dir.exists()) dir.mkdir();
        
        saveBoard(board, GAME_FILE);
        saveLog(moveLog, LOG_FILE);
        System.out.println("Rule 3 Enforced: 2 files saved in /incomplete.");
    
    }
    
    public void deleteIncompleteGame() {
        File game = new File(GAME_FILE);
        File log = new File(LOG_FILE);
        
        if (game.exists()) game.delete();
        if (log.exists()) log.delete();
        
        System.out.println("Incomplete folder wiped (0 files).");
    }
    
private void saveBoard(int[][] board, String path) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    out.print(board[r][c] + (c == 8 ? "" : " "));
                }
                out.println();
            }
        }
    }
        
