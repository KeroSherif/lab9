/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 * Game class - Represents a Sudoku game with board and difficulty level
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

    // ⬅️ الكونستركتور الجديد
    public Game(int[][] board) {
        this.board = board;
        this.level = DifficultyEnum.INCOMPLETE;
    }

    public int[][] getBoard() {
        return board;
    }

    public DifficultyEnum getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "Game(Level: " + level + ", Board size: 9x9)";
    }
}


