package core;

import tileengine.TERenderer;

public class Main {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 30;

    public static void main(String[] args) {
        long seed = 23333;
        // build your own world!
        World world = new World(seed);
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(world.getTiles());

    }
}
