import javax.swing.*;
import javax.swing.Timer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Supplier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import Definitions.Creature;
import Definitions.Territory;
import Definitions.MargCitizenship;
import Definitions.SimCitizenship;

import Entities.Simonite;
import Entities.Margartian;


public class World extends JPanel{

    private final int GRID_COUNT = 50;
    private final int CELL_SIZE = 15;
    private Territory[][] map = new Territory[GRID_COUNT][GRID_COUNT];
    private List<Creature> creatures;
    private final Random random = new Random();

    public World(int n) {

        setBackground(new Color(20, 20, 30));
        generateMap();
        creatures = new ArrayList<>();
    }


    private void generateMap() {

        for(int x = 0; x < GRID_COUNT; x++) {
            for(int y = 0; y < GRID_COUNT; y++) {
                // Assign territory to corners 10x10
                // The rest of the map is unclaimed
                if(x < 10 && y < 10) map[x][y] = Territory.MargartainTerritory;
                else if(x > 39 && y > 39) map[x][y] = Territory.SimoniteTerritory;
                else map[x][y] = Territory.Unclaimed;
            }
        }
    }


    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        for(int x = 0; x < GRID_COUNT; x++) {
            for(int y = 0; y < GRID_COUNT; y++) {
                switch(map[x][y]) {
                    case MargartainTerritory -> g.setColor(new Color(100, 41, 38));
                    case SimoniteTerritory -> g.setColor(new Color(47, 87, 47));
                    case Unclaimed -> g.setColor(new Color(84, 84, 84));
                }
                g.fillRect(x*CELL_SIZE, y*CELL_SIZE, CELL_SIZE, CELL_SIZE);
                g.setColor(new Color(60, 60, 80));
                g.drawRect(x*CELL_SIZE, y*CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
        for (Creature c : creatures) c.draw(g);
    }


    public Creature createCreature() throws IOException {

        String rName = getRandomName();

        if (random.nextBoolean()) {
            return new Simonite(rName, random.nextInt(3), Color.GREEN, random.nextInt(GRID_COUNT), random.nextInt(GRID_COUNT));
        } else {
            return new Margartian(rName, random.nextInt(3), Color.RED, random.nextInt(GRID_COUNT), random.nextInt(GRID_COUNT));
        }
    }


    public String getRandomName() throws IOException {

        ArrayList<Object> names = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("./names.txt"));
            String line;
            while((line = reader.readLine()) != null) {
                names.add(line);
            }
        }
        catch (IOException e) {System.out.println(e);}
        finally {if(reader != null) reader.close();}

        Random random = new Random();
        return (String) names.get(random.nextInt(names.size()-1));
    }

    public static void main(String[] args) throws Exception {

        Random random = new Random();

        JFrame f = new JFrame("A Living World: The 4 Corners");
        World world = new World(100);
        f.add(world);
        f.pack();
        f.setSize(765, 800);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

        for (int i = 0; i < 50; i++) world.creatures.add(world.createCreature());

        // while(world.creatures.size() > 0) {
        //     if (random.nextInt(10) == 0) {
        //         world.creatures.remove(world.random.nextInt(world.creatures.size()));
        //     }
        //     if (random.nextInt(15) == 0) {
        //         world.creatures.add(world.createCreature());
        //     }
        // }
    }
}
