package Entities;

import Definitions.Creature;
import Definitions.InhabitantCitzenship;
import Definitions.Territory;
import World.World;
import java.awt.*;
import java.util.Random;

public class Inhabitant extends Creature implements InhabitantCitzenship {
    private Random random = new Random();
    private int speed;

    public Inhabitant(int size, Color color, int x, int y) {
        super(size, color, x, y);
        this.dmg = random.nextInt(2, 4);
        this.health = random.nextInt(5, 10);
        this.speed = 1; // Ensure speed is initialized for movement logic
    }

    @Override
    public boolean canEnter(int x, int y, Territory[][] map) {
        if (x < 0 || x >= map.length || y < 0 || y >= map[0].length) {
            return false;
        }
        Territory t = map[x][y];
        // They can enter Unclaimed land or their own territory
        return t == Territory.Unclaimed || t == Territory.InhabitantTerritory;
    }

    @Override
    public void move(World world, Territory[][] map) {
        int nextX = gridX + (random.nextBoolean() ? 1 : -1);
        int nextY = gridY + (random.nextBoolean() ? 1 : -1);

        if (canEnter(nextX, nextY, map) && world.getCreature(nextX, nextY) == null) {
            gridX = nextX;
            gridY = nextY;
        } else {
            // Fallback movement if the diagonal is blocked
            int stepX = gridX + (random.nextBoolean() ? 1 : -1);
            int stepY = gridY + (random.nextBoolean() ? 1 : -1);

            if (canEnter(stepX, gridY, map) && world.getCreature(stepX, gridY) == null) {
                gridX = stepX;
            } else if (canEnter(gridX, stepY, map) && world.getCreature(gridX, stepY) == null) {
                gridY = stepY;
            }

            // Boundary safety
            gridX = Math.max(0, Math.min(map.length - 1, gridX));
            gridY = Math.max(0, Math.min(map[0].length - 1, gridY));
        }
    }

    @Override
    public boolean Attack(World world, Territory[][] map) {
        int[] axis = {-1, 1, 0};

        for (int j = 0; j < axis.length; j++) {
            for (int k = 0; k < axis.length; k++) {
                int tempX = gridX + axis[j];
                int tempY = gridY + axis[k];

                // Prevent out-of-bounds check
                if (tempX < 0 || tempX >= map.length || tempY < 0 || tempY >= map[0].length) continue;

                Object target = world.getCreature(tempX, tempY);

                if (target == null || target == this) {
                    continue;
                }

                if (!(target instanceof InhabitantCitzenship)) {
                    if (random.nextBoolean()) {
                        ((Creature) target).takeDmg(dmg);
                        if (((Creature) target).die()) {
                            reproduce(world, map, true);
                            if(map[tempX][tempY] != Territory.InhabitantTerritory){
                                map[tempX][tempY] = Territory.Unclaimed;
                            }
                            map[gridX][gridY] = Territory.InhabitantTerritory;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void reproduce(World world, Territory[][] map, boolean won) {
        int id = 4; // FIX: Inhabitant ID is 4, not 3 (3 is ZigZagger)

        int[] axis = {-1, 1, 0};
        for (int j = 0; j < axis.length; j++) {
            for (int k = 0; k < axis.length; k++) {
                int tempX = gridX + axis[j];
                int tempY = gridY + axis[k];

                if (tempX < 0 || tempX >= map.length || tempY < 0 || tempY >= map[0].length) continue;

                Object mate = world.getCreature(tempX, tempY);

                if (mate instanceof InhabitantCitzenship) {
                    if (random.nextInt(100) == 3) {
                        world.reproCreature(id);
                    }
                } else if (won) {
                    // If they won a fight, they have a chance to reproduce immediately
                    world.reproCreature(id);
                    return; // Spawn one and exit
                }
            }
        }
    }

    @Override
    public boolean die() {
        if(health <= 0){
            return true;
        }
        return false;
    }

    @Override
    public int id() {
        return 4; 
    }

    @Override
    public void move(Territory[][] map) {
        // Do nothing. The World uses the other move method instead.
    }

    public boolean reproduce() {
        // Return false. The World uses the combat-based reproduce instead.
        return false;
    }
}