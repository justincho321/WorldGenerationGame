package core;

import tileengine.TERenderer;
import tileengine.TETile;

public class Main {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 30;
    public static void main(String[] args) {

        // build your own world!
        World world = new World(WIDTH, HEIGHT);
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(world.getTiles());

    }
}
