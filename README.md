# Lab 9 Sudoku Validator

This app validates a 9x9 Sudoku board from a CSV file in three modes:
- Mode 0: Sequential (single-threaded)
- Mode 3: Three threads (rows, columns, boxes)
- Mode 27: Thread pool with a task per check

## Usage
Compile:
```powershell
Push-Location "c:\Users\Mohamed\OneDrive\Documents\Lab 9 prog2\lab9\src"
javac *.java
Pop-Location
```
Run:
```powershell
Push-Location "c:\Users\Mohamed\OneDrive\Documents\Lab 9 prog2\lab9\src"
java main "c:\Users\Mohamed\OneDrive\Documents\Lab 9 prog2\lab9\samples\board-valid.csv" 27
java main "c:\Users\Mohamed\OneDrive\Documents\Lab 9 prog2\lab9\samples\board-invalid.csv" 27
Pop-Location
```

Output:
- Prints `VALID` when no duplicates are found.
- Prints `INVALID` then error lines like:
  - `ROW 2,#5,[1,7]`
  - `COL 4,#9,[3,9]`
  - `BOX 3,#1,[2,9]`

## CSV Format
- 9 rows, each with 9 comma-separated integers.
- Digits 1–9 represent placed values; 0 can be used for blanks.
- Example row: `5,3,0,0,7,0,0,0,0`
