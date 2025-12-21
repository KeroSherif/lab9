# 🎯 QUICK REFERENCE: MVC Architecture

## File Locations

```
lab9/
├── src/
│   ├── ControllerFacade.java      ⭐ NEW - Controller implementation
│   ├── ControllerAdapter.java     ⭐ NEW - Adapter pattern
│   ├── MainDemo.java               ⭐ NEW - Demo driver
│   ├── SimpleViewable.java         ⭐ NEW - Demo interface
│   ├── SimpleControllable.java     ⭐ NEW - Demo interface
│   ├── SimpleFacade.java           ⭐ NEW - Demo implementation
│   ├── SimpleAdapter.java          ⭐ NEW - Demo adapter
│   ├── SimpleDemo.java             ⭐ NEW - Working demo
│   ├── Game.java                   ✏️ UPDATED - Added getBoard()
│   ├── DifficultyEnum.java         ✓ EXISTS
│   ├── Catalog.java                ✓ EXISTS
│   ├── UserAction.java             ✓ EXISTS
│   ├── Viewable.java               ✓ EXISTS
│   └── Controllable.java           ✓ EXISTS
├── MVC_ARCHITECTURE_GUIDE.md      📖 Full documentation
└── IMPLEMENTATION_SUMMARY.md      📋 This summary
```

## Type Conversion Cheat Sheet

| From (View) | To (Controller) | Method |
|-------------|-----------------|--------|
| `char 'E'` | `DifficultyEnum.EASY` | `charToDifficulty('E')` |
| `char 'M'` | `DifficultyEnum.MEDIUM` | `charToDifficulty('M')` |
| `char 'H'` | `DifficultyEnum.HARD` | `charToDifficulty('H')` |
| `int[][]` | `new Game(board, level)` | Constructor |
| `UserAction` | `userAction.toString()` | toString() |

| From (Controller) | To (View) | Method |
|-------------------|-----------|--------|
| `Game` | `game.getBoard()` → `int[][]` | getBoard() |
| `Catalog` | `new boolean[] {unfinished, allModesExist}` | Manual array |
| `String "VALID"` | `boolean[][] all true` | Parse and convert |
| `int[] flat` | `int[9][9]` | Unflatten (i*9+j) |

## Architecture at a Glance

```
VIEW              ADAPTER           CONTROLLER
(Primitives)      (Converts)        (Objects)
─────────         ─────────         ─────────
int[][]     →     int[][] → Game → process
              ←     Game → int[][]

char 'E'    →     'E' → EASY → getGame()
              ←     Game → int[][]

boolean[]   ←     Catalog → bool[]
```

## Method Signatures Quick Reference

### ControllerFacade (Viewable)
```java
Catalog getCatalog()
Game getGame(DifficultyEnum level)
void driveGames(Game sourceGame)
String verifyGame(Game game)
int[] solveGame(Game game)
void logUserAction(String userAction)
```

### ControllerAdapter (Controllable)
```java
boolean[] getCatalog()
int[][] getGame(char level)
void driveGames(String sourcePath)
boolean[][] verifyGame(int[][] game)
int[][] solveGame(int[][] game)
void logUserAction(UserAction userAction)
```

## Run the Demo

```powershell
cd "c:\Users\Mohamed\OneDrive\Documents\Lab 9 prog2\lab9\src"
java SimpleDemo
```

## Next Steps Checklist

- [ ] Implement getCatalog() logic in ControllerFacade
- [ ] Implement getGame() logic to load actual boards
- [ ] Implement verifyGame() using existing verifiers
- [ ] Implement solveGame() using existing solver
- [ ] Implement driveGames() for game generation
- [ ] Implement logUserAction() with file I/O
- [ ] Create GUI that uses Controllable interface
- [ ] Wire GUI to ControllerAdapter
- [ ] Test end-to-end flow
- [ ] Add error handling

## Key Design Decisions

✅ **Why two interfaces?**  
   - Strict separation: View doesn't know about domain objects
   - Controller doesn't know about UI representations

✅ **Why the Adapter?**  
   - Makes incompatible interfaces work together
   - View thinks it's talking to primitives
   - Controller thinks it's working with objects

✅ **Why stub methods?**  
   - Focus on structure first, logic later
   - Allows testing of architecture before implementation
   - Easier to understand the flow

## Common Patterns

### Pattern 1: Get Data from Controller
```java
// View calls adapter with primitives
int[][] board = adapter.getGame('E');

// Adapter converts and calls controller
Game game = controller.getGame(DifficultyEnum.EASY);

// Adapter converts result back
return game.getBoard();
```

### Pattern 2: Send Data to Controller
```java
// View sends primitives to adapter
adapter.verifyGame(boardArray);

// Adapter converts to objects
Game game = new Game(boardArray, DifficultyEnum.EASY);

// Adapter calls controller
String result = controller.verifyGame(game);

// Adapter converts result
return convertToBoolean2D(result);
```

---

**Quick Start:** Run `SimpleDemo` to see the architecture in action!
