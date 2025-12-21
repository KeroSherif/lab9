/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author monic
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class SudokuGUI extends JFrame {

    private JTextField[][] cells;
    private JButton verifyButton;
    private JButton solveButton;
    private JButton undoButton;
    private JLabel statusLabel;
    private Controllable controller;
    private int[][] currentBoard;
    private boolean[][] fixedCells;

    public SudokuGUI(Controllable controller) {
        this.controller = controller;
        this.cells = new JTextField[9][9];
        this.fixedCells = new boolean[9][9];

        initializeFrame();
        checkAndLoadGame();
    }

    private void initializeFrame() {
        setTitle("Sudoku Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void checkAndLoadGame() {
        try {
            boolean[] catalog = controller.getCatalog();
            boolean hasUnfinished = catalog[0];
            boolean hasAllLevels = catalog[1];

            if (hasUnfinished) {
                askContinueOrNewGame();
            } else if (hasAllLevels) {
                askDifficultyAndLoad();
            } else {
                askForSourceSolution();
            }
        } catch (Exception e) {
            showError("Error checking game catalog: " + e.getMessage());
        }
    }

    private void askContinueOrNewGame() {

        String[] options = {"Continue Game", "New Game"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "You have an unfinished game.\nWhat would you like to do?",
                "Resume Game",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            loadUnfinishedGame();
        } else if (choice == 1) {
            controller.clearIncompleteGame();
            chooseLevelThenGameFile();

        } else {
            System.exit(0);
        }

    }

  private void chooseLevelThenGameFile() {

    // -------- Choose Difficulty --------
    String[] options = {"Easy", "Medium", "Hard"};
    int choice = JOptionPane.showOptionDialog(
            this,
            "Choose difficulty:",
            "New Game",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
    );

    if (choice == -1) {
        return;
    }

    String baseDir;
    if (choice == 0) {
        baseDir = "games/easy";
    } else if (choice == 1) {
        baseDir = "games/medium";
    } else {
        baseDir = "games/hard";
    }

    // -------- Choose Game File --------
    JFileChooser chooser = new JFileChooser(new java.io.File(baseDir));
    chooser.setDialogTitle("Choose a Sudoku Game");

    int result = chooser.showOpenDialog(this);

    if (result == JFileChooser.APPROVE_OPTION) {
        try {
            currentBoard = controller.loadSelectedGame(
                    chooser.getSelectedFile()
            );
            setupGameBoard();
            setVisible(true);
        } catch (Exception e) {
            showError("Error loading selected game: " + e.getMessage());
        }
    }
}


    private void startNewRandomGame() {
        String[] options = {"Easy", "Medium", "Hard"};

        int choice = JOptionPane.showOptionDialog(
                this,
                "Choose difficulty:",
                "New Game",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == -1) {
            return;
        }

        char level = (choice == 0) ? 'e' : (choice == 1) ? 'm' : 'h';

        try {
            currentBoard = controller.getRandomGame(level);
            setupGameBoard();
            setVisible(true);
        } catch (Exception e) {
            showError("Failed to start new game: " + e.getMessage());
        }
    }

    private void loadUnfinishedGame() {
        try {
            currentBoard = controller.getGame('c');
            setupGameBoard();
            setVisible(true);
        } catch (Exception e) {
            showError("Error loading unfinished game: " + e.getMessage());
        }
    }

    private void askDifficultyAndLoad() {
        String[] options = {"Easy", "Medium", "Hard"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "Select difficulty level:",
                "Choose Difficulty",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == -1) {
            System.exit(0);
            return;
        }

        char level = 'e';
        if (choice == 1) {
            level = 'm';
        } else if (choice == 2) {
            level = 'h';
        }

        try {
            currentBoard = controller.getRandomGame(level);
            setupGameBoard();
            setVisible(true);

        } catch (Exception e) {
            showError("Error loading game: " + e.getMessage());
        }
    }

    private void askForSourceSolution() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Solved Sudoku File");

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            try {
                controller.driveGames(filePath);
                JOptionPane.showMessageDialog(
                        null,
                        "Games generated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                askDifficultyAndLoad();
            } catch (Exception e) {
                showError("Invalid solution file: " + e.getMessage());
                System.exit(0);
            }
        } else {
            System.exit(0);
        }
    }

    private void setupGameBoard() {
        getContentPane().removeAll();

        JPanel boardPanel = createBoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        statusLabel = new JLabel("Fill the empty cells to complete the puzzle");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        add(statusLabel, BorderLayout.NORTH);

        pack();
        setLocationRelativeTo(null);

        updateSolveButtonState();
    }

    private JPanel createBoardPanel() {
        JPanel mainPanel = new JPanel(new GridLayout(3, 3, 2, 2));
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        for (int blockRow = 0; blockRow < 3; blockRow++) {
            for (int blockCol = 0; blockCol < 3; blockCol++) {
                JPanel block = createBlock(blockRow, blockCol);
                mainPanel.add(block);
            }
        }

        return mainPanel;
    }

    private JPanel createBlock(int blockRow, int blockCol) {
        JPanel block = new JPanel(new GridLayout(3, 3, 1, 1));
        block.setBackground(Color.GRAY);
        block.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int row = blockRow * 3 + i;
                int col = blockCol * 3 + j;

                JTextField cell = createCell(row, col);
                cells[row][col] = cell;
                block.add(cell);
            }
        }

        return block;
    }

    private JTextField createCell(int row, int col) {
        JTextField cell = new JTextField();
        cell.setHorizontalAlignment(JTextField.CENTER);
        cell.setFont(new Font("Arial", Font.BOLD, 24));
        cell.setPreferredSize(new Dimension(60, 60));

        int value = currentBoard[row][col];

        if (value != 0) {
            cell.setText(String.valueOf(value));
            cell.setEditable(false);
            cell.setBackground(new Color(230, 230, 230));
            cell.setForeground(Color.BLACK);
            fixedCells[row][col] = true;
        } else {
            cell.setText("");
            cell.setBackground(Color.WHITE);
            cell.setForeground(new Color(0, 100, 200));
            fixedCells[row][col] = false;

            final int r = row;
            final int c = col;

            cell.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    // Block backspace and delete keys
                    if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) {
                        e.consume();
                    }
                }

                @Override
                public void keyTyped(KeyEvent e) {
                    char ch = e.getKeyChar();

                    // Only allow digits 1-9
                    if (!Character.isDigit(ch)) {
                        e.consume();
                        return;
                    }

                    if (ch < '1' || ch > '9') {
                        e.consume();
                        return;
                    }

                    if (cell.getText().length() >= 1) {
                        e.consume();
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    handleCellInput(r, c, cell.getText());
                }
            });
        }

        return cell;
    }

    private void handleCellInput(int row, int col, String text) {
        int prevValue = currentBoard[row][col];
        int newValue = text.isEmpty() ? 0 : Integer.parseInt(text);

        // Only log if value actually changed
        if (prevValue != newValue) {
            currentBoard[row][col] = newValue;

            try {
                UserAction action = new UserAction(row, col, newValue, prevValue);
                controller.logUserAction(action);
            } catch (IOException e) {
                showError("Error logging action: " + e.getMessage());
            }

        }

        updateSolveButtonState();

        if (isBoardComplete()) {
            verifyCompletedBoard();
        }
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        verifyButton = new JButton("Verify");
        verifyButton.setFont(new Font("Arial", Font.BOLD, 14));
        verifyButton.addActionListener(e -> verifyBoard());

        solveButton = new JButton("Solve");
        solveButton.setFont(new Font("Arial", Font.BOLD, 14));
        solveButton.addActionListener(e -> solveBoard());
        solveButton.setEnabled(false);

        undoButton = new JButton("Undo");
        undoButton.setFont(new Font("Arial", Font.BOLD, 14));
        undoButton.addActionListener(e -> undoAction());

        panel.add(verifyButton);
        panel.add(solveButton);
        panel.add(undoButton);

        return panel;
    }

    private void verifyBoard() {
        try {
            boolean[][] result = controller.verifyGame(currentBoard);

            boolean hasError = false;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (!fixedCells[i][j]) {
                        if (!result[i][j]) {
                            cells[i][j].setBackground(new Color(255, 200, 200));
                            hasError = true;
                        } else {
                            cells[i][j].setBackground(Color.WHITE);
                        }
                    }
                }
            }

            if (hasError) {
                statusLabel.setText("Invalid cells are highlighted in red");
            } else if (isBoardComplete()) {
                statusLabel.setText("Board is valid and complete! Congratulations!");
            } else {
                statusLabel.setText("No errors found so far. Keep going!");
            }
        } catch (Exception e) {
            showError("Error verifying board: " + e.getMessage());
        }
    }

    private void solveBoard() {
        try {
            int[][] solution = controller.solveGame(currentBoard);

            // Copy the solved board back to currentBoard
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    currentBoard[i][j] = solution[i][j];
                }
            }

            // Refresh the display and highlight solved cells
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    int value = currentBoard[i][j];
                    String displayText = value == 0 ? "" : String.valueOf(value);
                    cells[i][j].setText(displayText);

                    // Highlight cells that were solved (not originally fixed)
                    if (!fixedCells[i][j]) {
                        cells[i][j].setBackground(new Color(200, 255, 200));
                    }
                }
            }

            statusLabel.setText("Puzzle solved automatically!");
            solveButton.setEnabled(false);

            verifyCompletedBoard();
        } catch (Exception e) {
            showError("Error solving board: " + e.getMessage());
        }
    }

    private void undoAction() {
        if (currentBoard == null) {
            JOptionPane.showMessageDialog(this, "No game loaded", "Undo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Access the undoLastMove method through reflection or direct cast
            if (controller instanceof SudokuController) {
                SudokuController sudokuController = (SudokuController) controller;

                // Debug: Check if log file exists and has content
                java.io.File logFile = new java.io.File("games/incomplete/moves.log");
                if (!logFile.exists()) {
                    JOptionPane.showMessageDialog(this, "No moves have been logged yet", "Undo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (sudokuController.undoLastMove(currentBoard)) {
                    refreshBoard();
                    statusLabel.setText("Status: Last move undone");
                    JOptionPane.showMessageDialog(this, "Last move undone!", "Undo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No moves to undo", "Undo", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Controller doesn't support undo", "Undo", JOptionPane.WARNING_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error undoing: " + e.getMessage(), "Undo", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void refreshBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int value = currentBoard[i][j];
                String displayText = value == 0 ? "" : String.valueOf(value);
                // Remove listeners temporarily to avoid triggering key events
                KeyListener[] listeners = cells[i][j].getKeyListeners();
                for (KeyListener listener : listeners) {
                    cells[i][j].removeKeyListener(listener);
                }

                cells[i][j].setText(displayText);

                // Re-add listeners
                for (KeyListener listener : listeners) {
                    cells[i][j].addKeyListener(listener);
                }
            }
        }
        updateSolveButtonState();
    }

    private void updateSolveButtonState() {
        int emptyCount = countEmptyCells();
        solveButton.setEnabled(emptyCount <= 5 && emptyCount > 0);

    }

    private int countEmptyCells() {
        int count = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (currentBoard[i][j] == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean isBoardComplete() {
        return countEmptyCells() == 0;
    }

    private void verifyCompletedBoard() {
        try {
            boolean[][] result = controller.verifyGame(currentBoard);

            boolean isValid = true;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (!result[i][j]) {
                        isValid = false;
                        break;
                    }
                }
                if (!isValid) {
                    break;
                }
            }

            if (isValid) {
                JOptionPane.showMessageDialog(
                        this,
                        "Congratulations! Puzzle solved correctly!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                showError("Board is full but invalid. Please check your entries.");
            }
        } catch (Exception e) {
            showError("Error verifying completed board: " + e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
