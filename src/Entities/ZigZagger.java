package Entities;

import java.awt.*;

import Definitions.Creature;
import Definitions.Territory;
import Definitions.ZigZagCitizenship;

public class ZigZagger extends Creature implements ZigZagCitizenship {

    private boolean movingRight = true;

    public ZigZagger(String name, int size, Color color, int x, int y) {
        super(name, size, color, x, y);
    }

    public void move(Territory[][] map) {
        int nextX = gridX + (movingRight ? 1 : -1);
        int nextY = gridY + 1;

        if (canEnterZigZag(nextX, nextY, map)) {
            gridX = nextX;
            gridY = nextY;
        } else {
            movingRight = !movingRight;

            nextX = gridX + (movingRight ? 1 : -1);
            nextY = gridY;

            if (canEnterZigZag(nextX, nextY, map)) {
                gridX = nextX;
            }
        }
    }

    private boolean canEnterZigZag(int x, int y, Territory[][] map) {
        if (x < 0 || y < 0 || x >= map.length || y >= map[0].length) {
            return false;
        }

        return map[x][y] == Territory.Unclaimed;
    }
}