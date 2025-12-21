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
    
    public void clearIncompleteFolder() {
        File folder = new File(INCOMPLETE_DIR);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
        // Result: Folder contains 0 files [cite: 86]
}

    private void saveLog(String moveLog, String LOG_FILE) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void saveBoard(int[][] board, String GAME_FILE) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
