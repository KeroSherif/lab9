/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author monic
 */
public class UserAction {
    private int x;
    private int y;
    private int value;
    private int prevValue;
    
    public UserAction(int x, int y, int value, int prevValue) {
        this.x = x;
        this.y = y;
        this.value = value;
        this.prevValue = prevValue;
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public int getValue() { return value; }
    public int getPrevValue() { return prevValue; }
    
    @Override
    public String toString() {
        return String.format("(%d, %d, %d, %d)", x, y, value, prevValue);
    }
    
    public static UserAction fromString(String line) {
        line = line.trim().replaceAll("[()]", "");
        String[] parts = line.split(",");
        
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid UserAction format: " + line);
        }
        
        int x = Integer.parseInt(parts[0].trim());
        int y = Integer.parseInt(parts[1].trim());
        int value = Integer.parseInt(parts[2].trim());
        int prevValue = Integer.parseInt(parts[3].trim());
        
        return new UserAction(x, y, value, prevValue);
    }
}