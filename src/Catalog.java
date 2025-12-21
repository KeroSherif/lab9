/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author DANAH
 */


import java.util.ArrayList;

public class Catalog {

    private ArrayList<Game> games;
    private int currentIndex;

    private boolean unfinished;
    private boolean allModesExist;

    public Catalog() {
        games = new ArrayList<>();
        currentIndex = 0;
    }

    public Catalog(boolean unfinished, boolean allModesExist) {
        this();
        this.unfinished = unfinished;
        this.allModesExist = allModesExist;
    }

    // ===== flags =====
    public boolean hasUnfinished() {
        return unfinished;
    }

    public boolean areModesReady() {
        return allModesExist;
    }

    public void setUnfinished(boolean unfinished) {
        this.unfinished = unfinished;
    }

    public void setAllModesExist(boolean allModesExist) {
        this.allModesExist = allModesExist;
    }

    // ===== games =====
    public void addGame(Game game) {
        games.add(game);
    }

    public Game getCurrentGame() {


