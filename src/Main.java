/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author monic
 */

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
<<<<<<< HEAD
        Catalog catalog = new Catalog(true, true);

        // إضافة ألعاب
        catalog.addGame(new Game(new int[3][3], DifficultyEnum.EASY));
        catalog.addGame(new Game(new int[4][4], DifficultyEnum.MEDIUM));

        // طباعة كل الألعاب
        catalog.showAllGames();

        // الحصول على اللعبة الحالية
        System.out.println("Current Game: " + catalog.getCurrentGame());

        // الانتقال للعبة التالية
        catalog.nextGame();
        System.out.println("Next Game: " + catalog.getCurrentGame());

        // الحصول على رقم اللعبة الحالية
        System.out.println("Current index: " + catalog.getCurrentIndex());
=======
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            Viewable realController = new SudokuController();
            Controllable adapter = new ControllerAdapter(realController);
            SudokuGUI gui = new SudokuGUI(adapter);
            
            System.out.println("Sudoku Game started successfully!");
        });
>>>>>>> 1fa8a54d1b531bfc413e1fa63c7fd29dc9f22fec
    }
}
