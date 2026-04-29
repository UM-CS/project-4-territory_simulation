package Entities;

import java.awt.*;
import java.util.Random;

import Definitions.Creature;
import Definitions.Territory;
import World.World;
import Definitions.SimCitizenship;

public class Simonite extends Creature implements SimCitizenship{
    Random random = new Random();
    int speed = 1;

    public Simonite(String name, int size, Color color, int x, int y) {
        super(name, size, color, x, y);
        this.dmg = random.nextInt(2, 4);
        this.health = random.nextInt(5, 10);
    }

    public void move(World world, Territory[][] map) {
        Random rand = new Random();
        int nextX = gridX + (rand.nextBoolean() ? 1 : -1) * rand.nextInt(speed + 1);
        int nextY = gridY + (rand.nextBoolean() ? 1 : -1) * rand.nextInt(speed + 1);

        if(canEnter(nextX, nextY, map) && world.getCreature(nextX, nextY) == null) {
            gridX = nextX;
            gridY = nextY;
        } 
        else {
            if(canEnter(gridX + (rand.nextBoolean() ? 1 : -1), gridY, map)) {
                gridX += (rand.nextBoolean() ? 1 : -1);
            }
            else if(canEnter(gridX, gridY + (rand.nextBoolean() ? 1 : -1), map)) {
                gridY += (rand.nextBoolean() ? 1 : -1);
            }
            gridX = Math.max(0, Math.min(map.length - 1, gridX));
            gridY = Math.max(0, Math.min(map[0].length - 1, gridY));
        }
    }

    public boolean Attack(World world){
        int[] axis = {-1, 1, 0};

        for(int j = 0; j < axis.length; j++){
            for(int k = 0; k < axis.length; k++){
                int tempX = gridX + axis[j];
                int tempY = gridY + axis[k];

                Object target = world.getCreature(tempX, tempY);

                if(target == null){
                    continue;
                }

                if(!(target instanceof SimCitizenship)){
                    if(random.nextBoolean()){
                        ((Creature) target).takeDmg(dmg);
                    }
                    return true;
                }
            }
        }
        return false;
    }    
}
