/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab9;

/**
 *
 * @author DANAH
 */
public class Game {
    private int[][] board;
    private DifficultyEnum level; 

    public Game(int[][] board, DifficultyEnum level) {
        this.board = board;
        this.level = level;
    }
    public DifficultyEnum getLevel() {
        return level;
    }
}

