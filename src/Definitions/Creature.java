package Definitions;

import java.awt.*;
import java.io.IOException;

import World.World;

public abstract class Creature {

    public int gridX;
    public int gridY;

    int size;
    Color color;
    public int health, dmg;

    public Creature(int size, Color color, int x, int y) {
        this.gridX = x;
        this.gridY = y;
        this.size = size;
        this.color = color;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        int drawX = gridX * 15 + 4;
        int drawY = gridY * 15 +4;
        int drawSize = size + 8;
        g.fillOval(drawX, drawY, drawSize, drawSize);
        
    }

    protected boolean canEnter(int x, int y, Territory[][] map) {
        if(x < 0 || y < 0 || x >= map.length || y >= map[0].length) return false;

        Territory t = map[x][y];
        if(t == Territory.Unclaimed) return true;
        if(t == Territory.MargartianTerritory && (this instanceof MargCitizenship && t == Territory.MargartianTerritory)) return true;
        if(t == Territory.SimoniteTerritory && (this instanceof SimCitizenship && t == Territory.SimoniteTerritory)) return true;
        if(t == Territory.ZigZagTerritory && (this instanceof ZigZagCitizenship && t == Territory.ZigZagTerritory)) return true;
        return false;
    }

    public abstract boolean Attack(World world, Territory[][] map);
    public abstract void move(World world, Territory[][] map);
    public abstract boolean die(); 
    public abstract void reproduce(World world, Territory[][] map, boolean won); 
    public abstract int id();

    public void takeDmg(int dmg){
        health -= dmg;
    }
    
    
}

