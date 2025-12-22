// ==================== CatalogManager.java - FIXED ====================
import java.io.File;

public class CatalogManager {
    private final String EASY_DIR = "games/easy";      // ✅ Fixed path
    private final String MEDIUM_DIR = "games/medium";  // ✅ Fixed path
    private final String HARD_DIR = "games/hard";      // ✅ Fixed path
    private final String INCOMPLETE_DIR = "games/incomplete";  // ✅ Fixed path
    
    public Catalog getCatalog() {
        boolean unfinished = checkIncompleteFolder();
        boolean allModesExist = checkDifficultyFolders();
        return new Catalog(unfinished, allModesExist);
    }
    
    private boolean checkIncompleteFolder() {
        File folder = new File(INCOMPLETE_DIR);
        if (!folder.exists() || !folder.isDirectory()) {
            folder.mkdirs();  // Create if doesn't exist
            return false;
        }

        File[] files = folder.listFiles();
        // Must have EXACTLY 2 files (current.txt + moves.log) or 0 files
        return (files != null && files.length == 2);
    }
    
    private boolean checkDifficultyFolders() {
        return hasFiles(EASY_DIR) && hasFiles(MEDIUM_DIR) && hasFiles(HARD_DIR);
    }

    private boolean hasFiles(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();  // Create if doesn't exist
            return false;
        }
        
        File[] files = dir.listFiles();
        return (files != null && files.length > 0);
    }
}
