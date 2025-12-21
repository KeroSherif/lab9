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
    private int current; 
    private final boolean unfinished;
    private final boolean allModesExist;

    public Catalog(boolean unfinished, boolean allModesExist) {
        this.unfinished = unfinished;
        this.allModesExist = allModesExist;
        this.games = new ArrayList<>();
        this.current = 0;
    }

    public void addGame(Game game) {
        games.add(game);
    }

    public Game getCurrentGame() {
        if (games.isEmpty()) return null;
        return games.get(current);
    }

    public void nextGame() {
        if (!games.isEmpty()) {
            current = (current + 1) % games.size();
        }
    }
    public int getCurrentIndex() {
        return current;
    }
    public void showAllGames() {
        if (games.isEmpty()) {
            System.out.println("The catalog is empty.");
        } else {
            for (int i = 0; i < games.size(); i++) {
                System.out.println((i + 1) + ". " + games.get(i));
            }
        }
    }

    public boolean hasUnfinished() {
        return unfinished;
    }

    public boolean areModesReady() {
        return allModesExist;
    }
}