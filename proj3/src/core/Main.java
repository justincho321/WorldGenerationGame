package core;

import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import static tileengine.Tileset.fG;

public class Main {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 30;

    public static void main(String[] args) {
        //AutograderBuddy.getWorldFromInput("Ajustine+n102S");
        //populate floorGradient array

        fG = new TETile[60][30];
        Tileset.colorGradient(fG);

        //render main menu page
        Menu menu = new Menu(WIDTH, HEIGHT);
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(menu.getTiles());

        menu.runMenu();
    }
}
