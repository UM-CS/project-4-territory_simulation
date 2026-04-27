package Entities;

import java.awt.*;

import Definitions.Creature;
import Definitions.Territory;
import Definitions.ZigZagCitizenship;

public class ZigZagSimonite extends Creature implements ZigZagCitizenship {

    private boolean movingRight = true;

    public ZigZagSimonite(String name, int size, Color color, int x, int y) {
        super(name, size, color, x, y);
    }

    public void move(Territory[][] map) {
        int nextX = gridX + (movingRight ? 1 : -1);
        int nextY = gridY + 1;

        if (canEnter(nextX, nextY, map)) {
            gridX = nextX;
            gridY = nextY;
        } else {
            movingRight = !movingRight;

            nextX = gridX + (movingRight ? 1 : -1);
            nextY = gridY;

            if (canEnter(nextX, nextY, map)) {
                gridX = nextX;
            }
        }
    }
}