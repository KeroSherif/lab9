/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab9;

/**
 *
 * @author DANAH
 */
public class CatalogManager {
    private final String EASY_PATH = "easy/";
    private final String MEDIUM_PATH = "medium/";
    private final String HARD_PATH = "hard/";
    private final String INCOMPLETE_PATH = "incomplete/";
    
public Catalog getCatalog() {
        File incompleteDir = new File(INCOMPLETE_PATH);
        File[] incompleteFiles = incompleteDir.listFiles();
        
        boolean unfinished = (incompleteFiles != null && incompleteFiles.length == 2);
        
        boolean allModesExist = checkFolderHasFiles(EASY_PATH) && checkFolderHasFiles(MEDIUM_PATH) && checkFolderHasFiles(HARD_PATH);

        return new Catalog(unfinished, allModesExist);
    }


