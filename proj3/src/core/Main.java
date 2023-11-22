package core;

import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import javax.swing.*;
import java.awt.Color;
import java.awt.*;
import java.awt.image.BufferedImage;

import static tileengine.Tileset.fG;

public class Main {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 30;

    public static void main(String[] args) {
        //populate floorGradient array
        fG = new TETile[60][30];
        Tileset.colorGradient(fG);

        //render main menu page
        Menu menu = new Menu(WIDTH, HEIGHT);
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(menu.getTiles());

        long seed = menu.runMenu();

        //gradient floors
        Tileset.MyComponent gradient = new Tileset.MyComponent();
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        gradient.paint(g);

        //build the world, run the game
        World world = new World(seed);
        Game game = new Game();
        game.runGame(world, WIDTH, HEIGHT);

    }
}
