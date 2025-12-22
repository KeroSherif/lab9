/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author kiro sherif
 */
public class SolutionBox {

    private int[] solution = null;

    public boolean isSolved() {
        return solution != null;
    }

    public void setSolution(int[] sol) {
        if (solution == null) {
            solution = sol;
        }
    }

    public int[] getSolution() {
        return solution;
    }
}

