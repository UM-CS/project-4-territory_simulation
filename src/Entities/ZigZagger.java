package Entities;

import Definitions.Creature;
import Definitions.Territory;
import Definitions.ZigZagCitizenship;
import World.World;
import java.awt.*;
import java.util.Random;

public class ZigZagger extends Creature implements ZigZagCitizenship {

    private Random random = new Random();

    public ZigZagger(int size, Color color, int x, int y) {
        super(size, color, x, y);
        this.dmg = random.nextInt(2, 4);
        this.health = random.nextInt(5, 10);
    }

    public void move(World world, Territory[][] map) {
        int nextX = gridX + (random.nextBoolean() ? 1 : -1);
        int nextY = gridY + (random.nextBoolean() ? 1 : -1);

        if (canEnter(nextX, nextY, map) && world.getCreature(nextX, nextY) == null) {
            gridX = nextX;
            gridY = nextY;
        } else {
            if (canEnter(gridX + (random.nextBoolean() ? 1 : -1), gridY, map)) {
                gridX += (random.nextBoolean() ? 1 : -1);
            } else if (canEnter(gridX, gridY + (random.nextBoolean() ? 1 : -1), map)) {
                gridY += (random.nextBoolean() ? 1 : -1);
            }

            gridX = Math.max(0, Math.min(map.length - 1, gridX));
            gridY = Math.max(0, Math.min(map[0].length - 1, gridY));
        }
    }

    @Override
    public boolean Attack(World world, Territory[][] map) {
        int[] axis = {-1, 1, 0};

        for(int j = 0; j < axis.length; j++){
            for(int k = 0; k < axis.length; k++){
                int tempX = gridX + axis[j];
                int tempY = gridY + axis[k];

                Object target = world.getCreature(tempX, tempY);

                if(target == null){
                    continue;
                }

                if(!(target instanceof ZigZagCitizenship)){
                    if(random.nextBoolean()){
                        ((Creature) target).takeDmg(dmg);
                        if(((Creature) target).die()){
                            reproduce(world, map, true);
                            if(map[tempX][tempY] != Territory.ZigZagTerritory){
                                map[tempX][tempY] = Territory.Unclaimed;
                            }
                            map[gridX][gridY] = Territory.ZigZagTerritory;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean die() {
        if(health <= 0){
            return true;
        }
        return false;
    }

    @Override
    public void reproduce(World world, Territory[][] map, boolean won){
        int[] axis = {-1, 1, 0};
        int id = 3;

        

        for(int j = 0; j < axis.length; j++){
            for(int k = 0; k < axis.length; k++){
                int tempX = gridX + axis[j];
                int tempY = gridY + axis[k];
                
                Object mate = world.getCreature(tempX, tempY);

                if(mate == null){
                    continue;
                }

                if(mate instanceof ZigZagCitizenship){
                    int chance = random.nextInt(100);
                    if(chance == 3){
                        world.reproCreature(id);
                    }
                }else if(won){
                    boolean chance = random.nextBoolean();
                    world.reproCreature(id);
                }

            }
        }
    }

    @Override
    public int id() {
        int id = 3;
        return id;
    }

    

}