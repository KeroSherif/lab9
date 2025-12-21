# MVC Architecture - Role 1: The MVC Architect
## Sudoku Application (Lab 10)

---

## 📋 Architecture Overview

This implementation demonstrates a **strict MVC architecture** using the **Adapter Pattern** to bridge two incompatible interfaces:
- **Viewable** (Controller side - uses Objects)
- **Controllable** (View side - uses Primitives)

---

## 🏗️ Component Structure

### 1. **Data Classes (Controller Side - Objects)**

#### **DifficultyEnum.java**
```java
public enum DifficultyEnum {
    EASY, MEDIUM, HARD, INCOMPLETE
}
```
- Represents game difficulty levels as an enumeration
- Used by the Controller layer

#### **Catalog.java**
```java
public class Catalog {
    private final boolean unfinished;
    private final boolean allModesExist;
    
    // Constructors and getters
}
```
- Encapsulates catalog metadata
- Properties: `unfinished` (current status), `allModesExist` (mode availability)

#### **Game.java**
```java
public class Game {
    private int[][] board;
    private DifficultyEnum level;
    
    // Constructors and getters
}
```
- Represents a Sudoku game with its board and difficulty level
- Rich domain object used by Controller

---

### 2. **Data Classes (View Side - Primitives)**

#### **UserAction.java**
```java
public class UserAction {
    private int x, y, value, prevValue;
    
    // Constructors, getters, toString(), fromString()
}
```
- Simple class holding user input details
- Can be serialized to/from String for logging

---

### 3. **Interfaces**

#### **Viewable.java** (Controller Side)
```java
public interface Viewable {
    Catalog getCatalog();
    Game getGame(DifficultyEnum level) throws NotFoundException;
    void driveGames(Game sourceGame) throws SolutionInvalidException;
    String verifyGame(Game game);
    int[] solveGame(Game game) throws InvalidGameException;
    void logUserAction(String userAction) throws IOException;
}
```
- **Works with:** Objects (Game, Catalog, DifficultyEnum)
- **Returns:** Objects and Strings
- **Purpose:** Business logic layer interface

#### **Controllable.java** (View Side)
```java
public interface Controllable {
    boolean[] getCatalog();
    int[][] getGame(char level) throws NotFoundException;
    void driveGames(String sourcePath) throws SolutionInvalidException;
    boolean[][] verifyGame(int[][] game);
    int[][] solveGame(int[][] game) throws InvalidGameException;
    void logUserAction(UserAction userAction) throws IOException;
}
```
- **Works with:** Primitives (int[][], char, boolean[])
- **Returns:** Primitive arrays
- **Purpose:** View layer interface

---

### 4. **ControllerFacade.java** (Controller Implementation)

```java
public class ControllerFacade implements Viewable {
    // All methods return dummy/stub data for now
    
    @Override
    public Catalog getCatalog() {
        return new Catalog(false, true);
    }
    
    @Override
    public Game getGame(DifficultyEnum level) {
        int[][] dummyBoard = new int[9][9];
        return new Game(dummyBoard, level);
    }
    
    // ... other stub methods
}
```

**Role:** 
- Implements the business logic layer (Viewable interface)
- Works exclusively with Objects
- Current implementation contains stubs - ready for actual logic implementation

**Methods:**
- `getCatalog()` → Returns Catalog object
- `getGame(DifficultyEnum)` → Returns Game object
- `verifyGame(Game)` → Returns String verification result
- `solveGame(Game)` → Returns flat int[] solution
- `driveGames(Game)` → Generates games from source
- `logUserAction(String)` → Logs actions to storage

---

### 5. **ControllerAdapter.java** (The Adapter Pattern) ⭐

```java
public class ControllerAdapter implements Controllable {
    private final Viewable controller;
    
    public ControllerAdapter(Viewable controller) {
        this.controller = controller;
    }
    
    // Converts primitives ↔ objects for each method
}
```

**Role:** 
- **THE CRITICAL BRIDGE** between incompatible interfaces
- Implements Controllable (View side with primitives)
- Holds reference to Viewable (Controller side with objects)
- Performs bidirectional type conversions

**Conversion Examples:**

| Method | Input Conversion | Output Conversion |
|--------|------------------|-------------------|
| `getCatalog()` | N/A | Catalog → boolean[] |
| `getGame(char)` | char 'E' → DifficultyEnum.EASY | Game → int[][] |
| `verifyGame(int[][])` | int[][] → Game | String → boolean[][] |
| `solveGame(int[][])` | int[][] → Game | int[] → int[][] |
| `logUserAction(UserAction)` | UserAction → String | N/A |

**Key Conversion Logic:**
```java
// char → DifficultyEnum
private DifficultyEnum charToDifficulty(char level) {
    switch (Character.toUpperCase(level)) {
        case 'E': return DifficultyEnum.EASY;
        case 'M': return DifficultyEnum.MEDIUM;
        case 'H': return DifficultyEnum.HARD;
        default: throw new IllegalArgumentException("Invalid level: " + level);
    }
}
```

---

### 6. **MainDemo.java** (Architecture Demonstration)

```java
public class MainDemo {
    public static void main(String[] args) {
        // 1. Create Controller (works with Objects)
        Viewable controller = new ControllerFacade();
        
        // 2. Wrap with Adapter (converts primitives ↔ objects)
        Controllable adapter = new ControllerAdapter(controller);
        
        // 3. View layer uses adapter with primitives only
        boolean[] catalog = adapter.getCatalog();
        int[][] board = adapter.getGame('E');
        boolean[][] result = adapter.verifyGame(board);
        // ... etc
    }
}
```

**Purpose:**
- Demonstrates the complete architecture wiring
- Shows how View layer uses primitives through adapter
- Validates the conversion chain works correctly

---

## 🔄 Architecture Flow Diagram

```
┌─────────────────────────────────────────────────────────┐
│                      VIEW LAYER                         │
│                 (Uses Primitives Only)                  │
│                                                         │
│  - Calls methods with: int[][], char, boolean[]        │
│  - Receives results as: int[][], boolean[][], etc      │
└────────────────────┬────────────────────────────────────┘
                     │
                     │ Controllable interface
                     ▼
┌─────────────────────────────────────────────────────────┐
│              CONTROLLER ADAPTER                         │
│           (The Bridge - Converts Types)                 │
│                                                         │
│  Converts: char → DifficultyEnum                       │
│  Converts: int[][] → Game                              │
│  Converts: Catalog → boolean[]                         │
│  Converts: String → boolean[][]                        │
│  Converts: UserAction → String                         │
└────────────────────┬────────────────────────────────────┘
                     │
                     │ Viewable interface
                     ▼
┌─────────────────────────────────────────────────────────┐
│            CONTROLLER FACADE                            │
│          (Business Logic - Uses Objects)                │
│                                                         │
│  - Works with: Game, Catalog, DifficultyEnum           │
│  - Returns: Objects and domain types                   │
│  - Contains: Business rules and game logic             │
└─────────────────────────────────────────────────────────┘
```

---

## 🎯 Key Design Principles

### 1. **Strict Separation of Concerns**
- **Controller Layer:** Only handles Objects (Game, Catalog, DifficultyEnum)
- **View Layer:** Only handles Primitives (int[][], char, boolean[])
- **No Mixing:** Each layer is completely isolated

### 2. **Adapter Pattern Implementation**
- The `ControllerAdapter` is the ONLY class that knows about both layers
- It performs all type conversions bidirectionally
- Enables incompatible interfaces to work together

### 3. **Interface Segregation**
- `Viewable`: Defines what the Controller can do (object-based)
- `Controllable`: Defines what the View can do (primitive-based)
- Both interfaces have matching methods with different signatures

### 4. **Stub Implementation**
- Current implementation focuses on **structure**, not logic
- All methods return dummy/default values
- Ready for actual game logic to be plugged in later

---

## 📁 Files Generated

### Core Implementation Files:
1. ✅ **ControllerFacade.java** - Controller layer implementation
2. ✅ **ControllerAdapter.java** - Adapter pattern implementation
3. ✅ **MainDemo.java** - Architecture demonstration

### Supporting Files (Already Existed):
4. ✅ **DifficultyEnum.java** - Difficulty enumeration
5. ✅ **Catalog.java** - Catalog metadata class
6. ✅ **Game.java** - Game domain object (enhanced with getBoard())
7. ✅ **UserAction.java** - User input class
8. ✅ **Viewable.java** - Controller interface
9. ✅ **Controllable.java** - View interface

---

## 🚀 How to Run

### Compile all files:
```bash
javac -d bin src/*.java
```

### Run the demonstration:
```bash
java -cp bin MainDemo
```

### Expected Output:
```
=== Sudoku MVC Architecture Demo ===

✓ Controller created (Viewable interface - uses Objects)
✓ Adapter created (Controllable interface - uses primitives)

--- Testing MVC Communication ---

1. View requests catalog (primitive boolean[]):
   - Has unfinished: false
   - All modes exist: true

2. View requests game with char 'E' (EASY):
   - Received board: 9x9

3. View logs user action:
LOG: (3, 5, 7, 0)

4. View verifies game board:
   - Verification result: 9x9 matrix

5. View requests solution:
   - Solution received: 9x9

=== Architecture Test Complete ===

The Adapter successfully bridges:
  • View Layer (primitives: int[][], char, boolean[])
  • Controller Layer (objects: Game, Catalog, DifficultyEnum)
```

---

## 🔧 Next Steps for Implementation

Now that the **structure** is complete, you can implement:

1. **ControllerFacade Logic:**
   - Actual catalog retrieval from storage
   - Game loading/generation algorithms
   - Sudoku solving algorithms
   - Verification logic
   - Logging to files

2. **Enhanced Adapter Logic:**
   - More sophisticated type conversions
   - Error handling and validation
   - Path-to-Game conversion for driveGames()

3. **View Layer (GUI):**
   - Create SudokuGUI that uses Controllable interface
   - Only works with primitives
   - Calls adapter methods

---

## 📊 Architecture Summary

| Component | Interface | Data Types | Purpose |
|-----------|-----------|------------|---------|
| **ControllerFacade** | Viewable | Objects | Business logic |
| **ControllerAdapter** | Controllable | Primitives → Objects | Type conversion bridge |
| **View (Future)** | Uses Controllable | Primitives | User interface |

**The Adapter is the key:** It allows the View (which only understands primitives) to communicate with the Controller (which only understands objects) without either side knowing about the other's implementation details.

---

## ✅ Checklist

- [x] DifficultyEnum defined (EASY, MEDIUM, HARD)
- [x] Catalog class created (boolean unfinished, boolean allModesExist)
- [x] Game class created (int[][] board, DifficultyEnum level)
- [x] UserAction class defined
- [x] Viewable interface defined (object-based methods)
- [x] Controllable interface defined (primitive-based methods)
- [x] ControllerFacade implements Viewable (stub methods)
- [x] ControllerAdapter implements Controllable (with type conversions)
- [x] MainDemo demonstrates architecture wiring
- [x] All code compiles without errors
- [x] Architecture follows strict MVC separation

---

**Status:** ✅ **Structural Boilerplate Complete** - Ready for logic implementation!
