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

    private boolean current;
    private boolean allModesExist;

    public Catalog() {
        games = new ArrayList<>();
        currentIndex = 0;
    }

    public Catalog(boolean current, boolean allModesExist) {
        this();
        this.current = current;
        this.allModesExist = allModesExist;
    }

    // ================= FLAGS =================

    public boolean hasUnfinished() {
        return current;
    }

    public boolean areModesReady() {
        return allModesExist;
    }

    public void setUnfinished(boolean unfinished) {
        this.current = unfinished;
    }

    public void setAllModesExist(boolean allModesExist) {
        this.allModesExist = allModesExist;
    }

    // ================= GAMES =================

    public void addGame(Game game) {
        games.add(game);
    }

    public Game getCurrentGame() {
        if (games.isEmpty()) return null;
        return games.get(currentIndex);
    }

    
    public void nextGame() {
        if (!games.isEmpty()) {
            currentIndex = (currentIndex + 1) % games.size();
        }
    }

    
    public int getCurrentIndex() {
        return currentIndex;
    }

    
    public void showAllGames() {
        if (games.isEmpty()) {
            System.out.println("No games in catalog.");
            return;
        }

        for (int i = 0; i < games.size(); i++) {
            System.out.println(
                "Game " + i +
                " | Level: " + games.get(i).getLevel()
            );
        }
    }
}
