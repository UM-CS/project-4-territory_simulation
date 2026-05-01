package Entities;

import Definitions.Creature;
import Definitions.MargCitizenship;
import Definitions.Territory;
import World.World;
import java.awt.*;
import java.util.Random;

public class Margartian extends Creature implements MargCitizenship {
    Random random = new Random();
    int speed = 2;


    public Margartian(int size, Color color, int x, int y) {
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

                if(!(target instanceof MargCitizenship)){
                    if(random.nextBoolean()){
                        ((Creature) target).takeDmg(dmg);
                        if(((Creature) target).die()){
                            reproduce(world, map, true);
                            if(map[tempX][tempY] != Territory.MargartianTerritory){
                                map[tempX][tempY] = Territory.Unclaimed;
                            }
                            map[gridX][gridY] = Territory.MargartianTerritory;
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
    public int id(){
        int id = 1;
        return id;
    }


    @Override
    public void reproduce(World world, Territory[][] map, boolean won){
        int[] axis = {-1, 1, 0};
        int id = 1;

        for(int j = 0; j < axis.length; j++){
            for(int k = 0; k < axis.length; k++){
                int tempX = gridX + axis[j];
                int tempY = gridY + axis[k];
                
                Object mate = world.getCreature(tempX, tempY);

                if(mate == null){
                    continue;
                }

                if(mate instanceof MargCitizenship){
                    int chance = random.nextInt(100);
                    if(chance == 1){
                        world.reproCreature(id);
                    }
                }else if(won){
                    boolean chance = random.nextBoolean();
                    world.reproCreature(id);
                }

            }
        }
    }



   
}
