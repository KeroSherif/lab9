
import java.io.File;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author DANAH
 */
public class StorageManager {
    private final String INCOMPLETE_DIR = "incomplete/";
    
    public void saveIncompleteGame(int[][] board, String logContent) {
        // Implementation must ensure BOTH files are written
        // 1. Save board state to "game.txt"
        // 2. Save log content to "log.txt"
        // Result: Folder contains exactly 2 files [cite: 37, 38, 86]
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
}
