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
    private final String INCOMPLETE_PATH = "incomplete/";
    private final String[] MODES = {"easy", "medium", "hard"};
    
public Catalog getCatalog() {
        File incompleteDir = new File(INCOMPLETE_PATH);
        File[] files = incompleteDir.listFiles();   
        
        boolean unfinished = (files != null && files.length == 2);
        
        boolean allModesExist = true;
        for (String mode : MODES) {
            File modeDir = new File(mode + "/");
        if (modeDir.listFiles() == null || modeDir.listFiles().length == 0) {
                allModesExist = false;
                break;
            }
        }

        return new Catalog(unfinished, allModesExist);
}
}
