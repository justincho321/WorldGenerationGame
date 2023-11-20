package core;

import tileengine.TERenderer;
import tileengine.TETile;

public class Main {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 30;

    public static void main(String[] args) {
        //render main menu page
        Menu menu = new Menu(WIDTH, HEIGHT);
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(menu.getTiles());

        long seed = menu.runMenu();

        //get user input for seed


        //long seed = 3015204577362510986L;
        // build your own world!
        World world = new World(seed);
        //TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(world.getTiles());
        //test addition -- DELETE LATER
    }
}
