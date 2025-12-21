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

    public Catalog(boolean unfinished, boolean allModesExist) {
        games = new ArrayList<>();
        current = 0;
    }

    public void addGame(Game game) {
        games.add(game);
    }

    public Game getCurrentGame() {
        if (games.isEmpty()) return null;
        return games.get(current);
    }

    public void nextGame() {
        if (!games.isEmpty()) current = (current + 1) % games.size();
    }
}
