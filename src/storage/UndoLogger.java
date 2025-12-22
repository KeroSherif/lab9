/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package storage;

import model.*;
import exceptions.*;

import java.io.*;
import java.util.*;

/**
 *
 * @author kiro sherif
 */
import java.io.*;
import java.util.*;

public class UndoLogger {

    private final File logFile;

    public UndoLogger(String logPath) {
        this.logFile = new File(logPath);
    }

    public void logMove(int x, int y, int val, int prev) throws IOException {
        try (FileWriter fw = new FileWriter(logFile, true)) {
            fw.write("(" + x + "," + y + "," + val + "," + prev + ")\n");
        }
    }

    public boolean undoLastMove(int[][] board) throws IOException {
        if (!logFile.exists()) {
            return false;
        }

        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }

        if (lines.isEmpty()) {
            return false;
        }

        String last = lines.remove(lines.size() - 1);
        last = last.replace("(", "").replace(")", "");
        String[] parts = last.split(",");

        int x = Integer.parseInt(parts[0].trim());
        int y = Integer.parseInt(parts[1].trim());
        int prev = Integer.parseInt(parts[3].trim());

        board[x][y] = prev;

        try (FileWriter fw = new FileWriter(logFile, false)) {
            for (String l : lines) {
                fw.write(l + "\n");
            }
        }

        return true;
    }
}
