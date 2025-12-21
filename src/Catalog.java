/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author DANAH
 */
public class Catalog {
    private final boolean unfinished;
    private final boolean allModesExist;

    public Catalog(boolean unfinished, boolean allModesExist) {
        this.unfinished = unfinished;
        this.allModesExist = allModesExist;
    }
   public boolean hasUnfinished() {
        return unfinished;
    }

    public boolean areModesReady() {
        return allModesExist;
    }
}

