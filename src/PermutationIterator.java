/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author kiro sherif
 */
import java.util.Iterator;
import java.util.NoSuchElementException;

public class PermutationIterator implements Iterator<int[]> {

    private final int[] current;
    private boolean hasNext = true;

    public PermutationIterator(int size) {
        current = new int[size];
        for (int i = 0; i < size; i++)
            current[i] = 1;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public int[] next() {
        if (!hasNext)
            throw new NoSuchElementException();

        int[] result = current.clone();
        increment();
        return result;
    }

    private void increment() {
        for (int i = current.length - 1; i >= 0; i--) {
            if (current[i] < 9) {
                current[i]++;
                return;
            }
            current[i] = 1;
        }
        hasNext = false;
    }
}

