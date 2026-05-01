package Entities;

import Definitions.Creature;
import Definitions.SimCitizenship;
import Definitions.Territory;
import World.World;
import java.awt.*;
import java.util.Random;

public class Simonite extends Creature implements SimCitizenship{
    Random random = new Random();
    int speed = 1;

    public Simonite(int size, Color color, int x, int y) {
        super(size, color, x, y);
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

    public boolean Attack(World world, Territory[][] map){
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
                        if(((Creature) target).die()){
                            reproduce(world, map, true);
                            if(map[tempX][tempY] != Territory.SimoniteTerritory){
                                map[tempX][tempY] = Territory.Unclaimed;
                            }
                            map[gridX][gridY] = Territory.SimoniteTerritory;
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
        int id = 2;

        for(int j = 0; j < axis.length; j++){
            for(int k = 0; k < axis.length; k++){
                int tempX = gridX + axis[j];
                int tempY = gridY + axis[k];
                
                Object mate = world.getCreature(tempX, tempY);

                if(mate == null){
                    continue;
                }

                if(mate instanceof SimCitizenship){
                    int chance = random.nextInt(100);
                    if(chance == 2){
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
        int id = 2;
        return id;
    }    
}
