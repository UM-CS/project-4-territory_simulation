package World;
import Definitions.Creature;
import Definitions.Territory;
import Entities.Inhabitant;
import Entities.Margartian;
import Entities.Simonite;
import Entities.ZigZagger;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;


public class World extends JPanel{

    private final int GRID_COUNT = 50;
    private final int CELL_SIZE = 15;
    private Territory[][] map = new Territory[GRID_COUNT][GRID_COUNT];
    private List<Creature> creatures;
    private final Random random = new Random();
    private List<Creature> tempCreatures;

    public World(int n) throws IOException {

        setBackground(new Color(20, 20, 30));
        generateMap();
        creatures = new ArrayList<>();
        tempCreatures = new ArrayList<>();

        for (int i = 0; i < 75; i++) creatures.add(createCreature());

        new Timer(10, e -> {
            for(Creature creature : creatures) {
                if(!creature.Attack(this, map)){
                    creature.move(this, map);
                }        
            }
            
            creatures.removeIf(c -> c.health <= 0);
            creatures.addAll(tempCreatures);
            tempCreatures.removeAll(creatures);
            repaint();
        }).start();
    }


    private void generateMap() {

        for(int x = 0; x < GRID_COUNT; x++) {
            for(int y = 0; y < GRID_COUNT; y++) {
                if(x < 10 && y < 10) map[x][y] = Territory.MargartianTerritory;
                else if (x > 39 && y < 10) map[x][y] = Territory.ZigZagTerritory;
                else if(x > 39 && y > 39) map[x][y] = Territory.SimoniteTerritory;
                else if(x < 10 && y > 39) map[x][y] = Territory.InhabitantTerritory;
                else map[x][y] = Territory.Unclaimed;
            }
        }
    }

    private void getTerrainStats(Graphics g) {

        int total = GRID_COUNT * GRID_COUNT;
        int margCount = 0, simCount = 0, zigCount = 0, inCount = 0;
        int marg = 0, sim = 0, zig = 0, in = 0;

        for(int x = 0; x < GRID_COUNT; x++) {
            for(int y = 0; y < GRID_COUNT; y++) {
                switch(map[x][y]) {
                    case MargartianTerritory -> margCount++;
                    case SimoniteTerritory -> simCount++;
                    case ZigZagTerritory -> zigCount++;
                    case InhabitantTerritory -> inCount++;
                }
            }
        }
        
        for(Creature c : creatures) {
            if(c instanceof Entities.Margartian) marg++;
            else if(c instanceof Entities.Simonite) sim++;
            else if(c instanceof Entities.ZigZagger) zig++;
            else if(c instanceof Entities.Inhabitant) in++;
        }

        g.setColor(Color.WHITE);
        g.drawString("Margartian: " + String.format("%.1f%%", margCount * 100.0 / total), 30, 75);
        g.drawString("Count: " + marg, 30, 90);
        
        g.drawString("ZigZagger: " + String.format("%.1f%%", zigCount * 100.0 / total), 635, 75);
        g.drawString("Count: " + zig, 635, 90);
        
        g.drawString("Simonite: " + String.format("%.1f%%", simCount * 100.0 / total), 635, 675);
        g.drawString("Count: " + sim, 635, 690);

        g.drawString("Inhabitant: " + String.format("%.1f%%", inCount * 100.0 / total), 30, 675);
        g.drawString("Count: " + in, 30, 690);
    }


    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        for(int x = 0; x < GRID_COUNT; x++) {
            for(int y = 0; y < GRID_COUNT; y++) {
                switch(map[x][y]) {
                    case MargartianTerritory -> g.setColor(new Color(100, 41, 38));
                    case SimoniteTerritory -> g.setColor(new Color(47, 87, 47));
                    case ZigZagTerritory -> g.setColor(new Color(102, 178, 200));
                    case InhabitantTerritory -> g.setColor(new Color(218, 165, 32));
                    case Unclaimed -> g.setColor(new Color(84, 84, 84));
                }
                g.fillRect(x*CELL_SIZE, y*CELL_SIZE, CELL_SIZE, CELL_SIZE);
                g.setColor(new Color(60, 60, 80));
                g.drawRect(x*CELL_SIZE, y*CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }

        getTerrainStats(g);
        for (Creature c : creatures) c.draw(g);
    }


    public Creature createCreature(){
        int choice = random.nextInt(4);

        if (choice == 0) {
            int x = 40 + random.nextInt(10);
            int y = 40 + random.nextInt(10);
            return new Simonite(random.nextInt(3), Color.GREEN, x, y);
        }
        else if (choice == 1) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);
            return new Margartian(random.nextInt(3), Color.RED, x, y);
        }
        else if (choice == 2) {
            int x = 40 + random.nextInt(10);
            int y = random.nextInt(10);
            return new ZigZagger(random.nextInt(3), Color.CYAN, x, y);
        }
        else if (choice == 3) {
            int x = random.nextInt(10);
            int y = 40 + random.nextInt(10);
            return new Inhabitant(random.nextInt(3), Color.YELLOW, x, y);
        }
        return null;
    }

    public Creature reproCreature(int id){
            if(id == 1){
                int x = random.nextInt(10);
                int y = random.nextInt(10);
                 tempCreatures.add(new Margartian(random.nextInt(3), Color.RED, x, y));
            }
            if(id == 2){
                int x = 40 + random.nextInt(10);
                int y = 40 + random.nextInt(10);
                tempCreatures.add(new Simonite(random.nextInt(3), Color.GREEN, x, y));
            }
            if(id == 3){
                int x = 40 + random.nextInt(10);
                int y = random.nextInt(10);
                tempCreatures.add(new ZigZagger(random.nextInt(3), Color.CYAN, x, y));
            }
            if(id == 4) {
                int x = random.nextInt(10);
                int y = 40 +random.nextInt(10);
                 tempCreatures.add(new Inhabitant(random.nextInt(3), Color.YELLOW, x, y));
            }
            return null;
    }

    //public String getRandomName() throws IOException {
//
    //    ArrayList<Object> names = new ArrayList<>();
    //    BufferedReader reader = null;
    //    try {
    //        reader = new BufferedReader(new FileReader("./names.txt"));
    //        String line;
    //        while((line = reader.readLine()) != null) {
    //            names.add(line);
    //        }
    //    }
    //    catch (IOException e) {System.out.println(e);}
    //    finally {if(reader != null) reader.close();}
//
    //    Random random = new Random();
    //    return (String) names.get(random.nextInt(names.size()-1));
    //}

    public Object getCreature(int x, int y){
        for(Creature c : creatures){
            if(c.gridX == x && c.gridY == y){
                return c;
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {

        JFrame f = new JFrame("A Living World: The 4 Corners");
        World world = new World(100);
        f.add(world);
        f.pack();
        f.setSize(765, 800);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}