# MVC ARCHITECTURE VISUAL DIAGRAM

## Complete System Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                         APPLICATION LAYER                            │
│                                                                       │
│  ┌──────────────┐                                                   │
│  │  Main.java   │  (Future: Wires everything together)             │
│  └──────┬───────┘                                                   │
│         │                                                            │
└─────────┼────────────────────────────────────────────────────────────┘
          │
          │ Creates both
          ▼
┌─────────────────────────────────────────────────────────────────────┐
│                          VIEW LAYER                                  │
│                  (Works with PRIMITIVES only)                        │
│                                                                       │
│  ┌────────────────────────────────────────────────────────────┐    │
│  │  SudokuGUI.java (Future)                                    │    │
│  │  - Display: int[][]                                         │    │
│  │  - Input: char for difficulty ('E', 'M', 'H')               │    │
│  │  - Results: boolean[][], int[][]                            │    │
│  └───────────────────────┬────────────────────────────────────┘    │
│                          │                                           │
│                          │ Uses Controllable interface              │
│                          │                                           │
└──────────────────────────┼───────────────────────────────────────────┘
                           │
                           │ Controllable methods:
                           │ - boolean[] getCatalog()
                           │ - int[][] getGame(char)
                           │ - boolean[][] verifyGame(int[][])
                           │ - int[][] solveGame(int[][])
                           ▼
┌─────────────────────────────────────────────────────────────────────┐
│                      ADAPTER LAYER (THE BRIDGE)                      │
│                                                                       │
│  ┌────────────────────────────────────────────────────────────┐    │
│  │  ControllerAdapter.java ⭐ CRITICAL COMPONENT              │    │
│  │                                                             │    │
│  │  implements Controllable {                                 │    │
│  │      private Viewable controller;  ← Holds reference       │    │
│  │                                                             │    │
│  │      // Converts char → DifficultyEnum                     │    │
│  │      int[][] getGame(char level) {                         │    │
│  │          DifficultyEnum diff = convert(level);             │    │
│  │          Game game = controller.getGame(diff);             │    │
│  │          return game.getBoard();                           │    │
│  │      }                                                      │    │
│  │                                                             │    │
│  │      // Converts Catalog → boolean[]                       │    │
│  │      // Converts int[][] → Game                            │    │
│  │      // Converts Game → int[][]                            │    │
│  │      // ... other conversions ...                          │    │
│  │  }                                                          │    │
│  └───────────────────────┬────────────────────────────────────┘    │
│                          │                                           │
└──────────────────────────┼───────────────────────────────────────────┘
                           │
                           │ Viewable methods:
                           │ - Catalog getCatalog()
                           │ - Game getGame(DifficultyEnum)
                           │ - String verifyGame(Game)
                           │ - int[] solveGame(Game)
                           ▼
┌─────────────────────────────────────────────────────────────────────┐
│                      CONTROLLER LAYER                                │
│                   (Works with OBJECTS only)                          │
│                                                                       │
│  ┌────────────────────────────────────────────────────────────┐    │
│  │  ControllerFacade.java ⭐                                   │    │
│  │                                                             │    │
│  │  implements Viewable {                                     │    │
│  │                                                             │    │
│  │      Catalog getCatalog() {                                │    │
│  │          // TODO: Get from StorageManager                  │    │
│  │          return new Catalog(false, true);  // stub         │    │
│  │      }                                                      │    │
│  │                                                             │    │
│  │      Game getGame(DifficultyEnum level) {                  │    │
│  │          // TODO: Load from CatalogManager                 │    │
│  │          return new Game(board, level);  // stub           │    │
│  │      }                                                      │    │
│  │                                                             │    │
│  │      String verifyGame(Game game) {                        │    │
│  │          // TODO: Use SudokuVerifier                       │    │
│  │          return "VALID";  // stub                          │    │
│  │      }                                                      │    │
│  │                                                             │    │
│  │      int[] solveGame(Game game) {                          │    │
│  │          // TODO: Use SudokuSolver                         │    │
│  │          return new int[81];  // stub                      │    │
│  │      }                                                      │    │
│  │  }                                                          │    │
│  └───────────────────────┬────────────────────────────────────┘    │
│                          │                                           │
│                          │ Uses domain objects                       │
│                          ▼                                           │
└─────────────────────────────────────────────────────────────────────┘
                           │
                           │ Can call existing components:
                           ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    EXISTING BUSINESS LOGIC                           │
│                                                                       │
│  ┌────────────────┐  ┌────────────────┐  ┌────────────────┐       │
│  │ SudokuSolver   │  │SudokuVerifier  │  │StorageManager  │       │
│  └────────────────┘  └────────────────┘  └────────────────┘       │
│                                                                       │
│  ┌────────────────┐  ┌────────────────┐  ┌────────────────┐       │
│  │CatalogManager  │  │  BoardPrinter  │  │  GameGenerator │       │
│  └────────────────┘  └────────────────┘  └────────────────┘       │
└─────────────────────────────────────────────────────────────────────┘


## Data Flow Example: Getting a Game

┌────────────┐
│   USER     │
│  (View)    │
└─────┬──────┘
      │
      │ 1. Click "Easy Game"
      ▼
┌────────────────────────────┐
│   SudokuGUI                │
│   adapter.getGame('E')     │  ← Uses char 'E'
└─────┬──────────────────────┘
      │
      │ 2. char 'E'
      ▼
┌────────────────────────────────────────────┐
│   ControllerAdapter                        │
│   - Convert: 'E' → DifficultyEnum.EASY     │
│   - Call: controller.getGame(EASY)         │
│   - Receive: Game object                   │
│   - Convert: Game → int[][]                │
│   - Return: int[][]                        │
└─────┬──────────────────────────────────────┘
      │
      │ 3. DifficultyEnum.EASY
      ▼
┌────────────────────────────┐
│   ControllerFacade         │
│   - Get game from storage  │
│   - Return Game object     │  ← Returns Game
└─────┬──────────────────────┘
      │
      │ 4. Game object
      ▼
┌────────────────────────────┐
│   ControllerAdapter        │
│   - Extract: game.getBoard()│
│   - Return: int[][]        │
└─────┬──────────────────────┘
      │
      │ 5. int[][] board
      ▼
┌────────────────────────────┐
│   SudokuGUI                │
│   - Display board          │
└────────────────────────────┘


## Type Conversions in the Adapter

┌─────────────────────────────────────────────────────────┐
│                  ControllerAdapter                       │
│                                                          │
│  INCOMING (from View - Primitives)                      │
│  ────────────────────────────────────────                │
│  char 'E'        →  DifficultyEnum.EASY                 │
│  char 'M'        →  DifficultyEnum.MEDIUM               │
│  char 'H'        →  DifficultyEnum.HARD                 │
│  int[][]         →  Game object                         │
│  UserAction      →  String                              │
│                                                          │
│  OUTGOING (to View - Primitives)                        │
│  ────────────────────────────────────────                │
│  Catalog         →  boolean[2]                          │
│  Game            →  int[][]                             │
│  String "VALID"  →  boolean[][]                         │
│  int[81]         →  int[9][9]                           │
│                                                          │
└─────────────────────────────────────────────────────────┘


## Layer Responsibilities

┌────────────────┬──────────────────┬─────────────────────────┐
│     LAYER      │   DATA TYPES     │    RESPONSIBILITIES     │
├────────────────┼──────────────────┼─────────────────────────┤
│  VIEW          │  int[][], char,  │  • Display UI           │
│  (SudokuGUI)   │  boolean[]       │  • Handle user input    │
│                │                  │  • Update display       │
├────────────────┼──────────────────┼─────────────────────────┤
│  ADAPTER       │  Both            │  • Convert types        │
│  (Controller   │  primitives &    │  • Bridge layers        │
│   Adapter)     │  objects         │  • No business logic    │
├────────────────┼──────────────────┼─────────────────────────┤
│  CONTROLLER    │  Game, Catalog,  │  • Business logic       │
│  (Controller   │  DifficultyEnum  │  • Coordinate services  │
│   Facade)      │                  │  • Manage workflow      │
├────────────────┼──────────────────┼─────────────────────────┤
│  BUSINESS      │  Domain objects  │  • Solving algorithms   │
│  LOGIC         │                  │  • Verification         │
│  (Existing)    │                  │  • Storage/Retrieval    │
└────────────────┴──────────────────┴─────────────────────────┘


## Key Interfaces

┌──────────────────────────────────────────────────────────┐
│                   Viewable (Controller)                   │
│  ────────────────────────────────────────────────────    │
│  + getCatalog(): Catalog                                 │
│  + getGame(DifficultyEnum): Game                         │
│  + verifyGame(Game): String                              │
│  + solveGame(Game): int[]                                │
│  + driveGames(Game): void                                │
│  + logUserAction(String): void                           │
└──────────────────────────────────────────────────────────┘
                           ▲
                           │ implements
                           │
         ┌─────────────────┴─────────────────┐
         │      ControllerFacade.java         │
         └────────────────────────────────────┘


┌──────────────────────────────────────────────────────────┐
│                 Controllable (View)                       │
│  ────────────────────────────────────────────────────    │
│  + getCatalog(): boolean[]                               │
│  + getGame(char): int[][]                                │
│  + verifyGame(int[][]): boolean[][]                      │
│  + solveGame(int[][]): int[][]                           │
│  + driveGames(String): void                              │
│  + logUserAction(UserAction): void                       │
└──────────────────────────────────────────────────────────┘
                           ▲
                           │ implements
                           │
         ┌─────────────────┴─────────────────┐
         │     ControllerAdapter.java         │
         │  (holds reference to Viewable)     │
         └────────────────────────────────────┘
```

---

**This diagram shows the complete MVC architecture with the Adapter pattern bridging the View and Controller layers.**
