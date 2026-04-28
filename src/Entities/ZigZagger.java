package Entities;

import java.awt.*;
import java.util.Random;

import Definitions.Creature;
import Definitions.Territory;
import Definitions.ZigZagCitizenship;

public class ZigZagger extends Creature implements ZigZagCitizenship {

    private Random rand = new Random();

    public ZigZagger(String name, int size, Color color, int x, int y) {
        super(name, size, color, x, y);
    }

    public void move(Territory[][] map) {
        int nextX = gridX + (rand.nextBoolean() ? 1 : -1);
        int nextY = gridY + (rand.nextBoolean() ? 1 : -1);

        if (canEnter(nextX, nextY, map)) {
            gridX = nextX;
            gridY = nextY;
        } else {
            if (canEnter(gridX + (rand.nextBoolean() ? 1 : -1), gridY, map)) {
                gridX += (rand.nextBoolean() ? 1 : -1);
            } else if (canEnter(gridX, gridY + (rand.nextBoolean() ? 1 : -1), map)) {
                gridY += (rand.nextBoolean() ? 1 : -1);
            }

            gridX = Math.max(0, Math.min(map.length - 1, gridX));
            gridY = Math.max(0, Math.min(map[0].length - 1, gridY));
        }
    }
}