package Entities;

import java.awt.*;

import Definitions.Creature;
import Definitions.Territory;
import Definitions.SimCitizenship;

public class Simonite extends Creature implements SimCitizenship{

    int speed = 2;

    public Simonite(String name, int size, Color color, int x, int y) {
        super(name, size, color, x, y);
    }

    @Override
    public void Sim(Territory[][] map) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Sim'");
    }
    
}
