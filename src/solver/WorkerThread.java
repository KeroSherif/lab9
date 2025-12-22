/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solver;

import model.*;
import exceptions.*;
import validation.FlyweightVerifier;

/**
 *
 * @author User
 */
public class WorkerThread extends Thread {

    private final FlyweightVerifier verifier;
    private final int[] values;
    private final SolutionBox solutionBox;

    public WorkerThread(FlyweightVerifier verifier,
                        int[] values,
                        SolutionBox solutionBox) {
        this.verifier = verifier;
        this.values = values;
        this.solutionBox = solutionBox;
    }

    @Override
    public void run() {

        if (solutionBox.isSolved())
            return;

        if (verifier.isValid(values)) {
            solutionBox.setSolution(values);
        }
    }
}

