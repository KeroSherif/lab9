/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab9;
import java.io.File;
/**
 *
 * @author DANAH
 */
public class CatalogManager {
   private final String EASY_DIR = "easy";
    private final String MEDIUM_DIR = "medium";
    private final String HARD_DIR = "hard";
    private final String INCOMPLETE_DIR = "incomplete";
    
public Catalog getCatalog() {
        boolean unfinished = checkIncompleteFolder();
        boolean allModesExist = checkDifficultyFolders();
       
        return new Catalog(unfinished, allModesExist);
}
private boolean checkIncompleteFolder() {
        File folder = new File(INCOMPLETE_DIR);
        if (!folder.exists() || !folder.isDirectory()) {
            folder.mkdir(); 
            return false;
        }

        File[] files = folder.listFiles();
        return (files != null && files.length == 2);
    }

}
