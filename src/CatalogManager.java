
import java.io.File;
import javax.xml.catalog.Catalog;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


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
        return null;
}
}
