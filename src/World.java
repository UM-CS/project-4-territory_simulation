import javax.swing.*;
import javax.swing.Timer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import Definitions.Creature;
import Definitions.Territory;
import Definitions.ZigZagCitizenship;
import Definitions.MargCitizenship;
import Definitions.SimCitizenship;

import Entities.Simonite;
import Entities.Margartian;
import Entities.ZigZagger;


public class World extends JPanel{

    private final int GRID_COUNT = 50;
    private final int CELL_SIZE = 15;
    private Territory[][] map = new Territory[GRID_COUNT][GRID_COUNT];
    private List<Creature> creatures;
    private final Random random = new Random();

    public World(int n) throws IOException {

        setBackground(new Color(20, 20, 30));
        generateMap();
        creatures = new ArrayList<>();

        for (int i = 0; i < 50; i++) creatures.add(createCreature());

        new Timer(200, e -> {
            for(Creature creature : creatures) {
                if(creature instanceof MargCitizenship) ((MargCitizenship) creature).move(map);
                if(creature instanceof SimCitizenship) ((SimCitizenship) creature).move(map);
                if(creature instanceof ZigZagCitizenship) ((ZigZagCitizenship) creature).move(map);
            }
            repaint();
        }).start();
    }


    private void generateMap() {

        for(int x = 0; x < GRID_COUNT; x++) {
            for(int y = 0; y < GRID_COUNT; y++) {
                if(x < 10 && y < 10) map[x][y] = Territory.MargartianTerritory;
                else if (x > 39 && y < 10) map[x][y] = Territory.ZigZagTerritory;
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
                    case MargartianTerritory -> g.setColor(new Color(100, 41, 38));
                    case SimoniteTerritory -> g.setColor(new Color(47, 87, 47));
                    case ZigZagTerritory -> g.setColor(new Color(102, 178, 200));
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

        int choice = random.nextInt(3);

        if (choice == 0) {
            int x = 40 + random.nextInt(10);
            int y = 40 + random.nextInt(10);
            return new Simonite(rName, random.nextInt(3), Color.GREEN, x, y);
        }
        else if (choice == 1) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);
            return new Margartian(rName, random.nextInt(3), Color.RED, x, y);
        }
        else {
            int x = 40 + random.nextInt(10);
            int y = random.nextInt(10);
            return new ZigZagger(rName, random.nextInt(3), Color.CYAN, x, y);
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
    }
}