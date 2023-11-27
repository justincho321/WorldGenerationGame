package core;

import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

import static tileengine.Tileset.fG;

public class AutograderBuddy {

    /**
     * Simulates a game, but doesn't render anything or call any StdDraw
     * methods. Instead, returns the world that would result if the input string
     * had been typed on the keyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quit and
     * save. To "quit" in this method, save the game to a file, then just return
     * the TETile[][]. Do not call System.exit(0) in this method.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public static TETile[][] getWorldFromInput(String input) {
        //long seed = Long.parseLong(input.substring(1, input.length() - 1));
        long seed = 0;
        boolean beforeN = true;
        String avatarName = "";
        fG = new TETile[60][30];
        Tileset.colorGradient(fG);
        TERenderer ter = new TERenderer();
        Tileset.MyComponent gradient = new Tileset.MyComponent();
        BufferedImage image = new BufferedImage(60, 30, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        gradient.paint(g);
        ter.initialize(60, 33);
        int z = 0;
        int y = 0;
        World world = null;
        Game game = null;
        HUD hud = new HUD();
        Move move = new Move();

        for (int i = 0; i < input.length(); i++) {
            if (beforeN == true) {
                if (input.charAt(i) == 'L' || input.charAt(i) == 'l') {
                    Menu menu = new Menu(60, 30);
                    menu.loadGame();
                } else if (input.charAt(i) == 'Q' || input.charAt(i) == 'q') {
                    System.exit(0);
                } else if (input.charAt(i) == 'A' || input.charAt(i) == 'a') {
                    for (int j = i; j < input.length(); j++) {
                        if (input.charAt(j) == '+') {
                            avatarName = input.substring(i + 1, j);
                            y = j;
                        }
                    }
                }
            }
        }
        for (int i = y; i < input.length(); i++) {

            if (input.charAt(i) == 'N' || input.charAt(i) == 'n') {
                beforeN = false;
                for (int j = i; j < input.length(); j++) {
                    if (input.charAt(j) == 'S' || input.charAt(j) == 's') {
                        seed = Long.parseLong(input.substring(i + 1, j));
                        world = new World(seed);
                        world.addFirstAvatar();
                        if (!Objects.equals(avatarName, "")) {
                            world.setAvatarName(avatarName);
                        }
                        game = new Game();
                        //game.runGame(world, 60, 30);
                        ter.renderFrame(world.getTiles());
                        hud.renderHUD(world);
                        z = j;
                        break;
                    }
                }
            }
        }
        double xCurr = 0;
        double yCurr = 0;
        boolean colon = false;
        boolean lightsOff = world.getLightsOff();
        //after render game
        for (int i = z; i < input.length(); i++) {
            char key = input.charAt(i);
            if (colon && (key == 'Q' || key == 'q')) {
                game.saveAndQuit(world);
                colon = false;
            } else if (key == ':') {
                colon = true;
            } else if (key == 'f' || key == 'F') {
                if (lightsOff) { //so turn on
                    ter.renderFrame(world.getTiles());
                    hud.renderHUD(world);
                    lightsOff = false;
                } else { //so turn off
                    TETile[][] lightGrid = world.getLitSurrounding();
                    ter.renderFrame(lightGrid);
                    //hud.renderHUD(world);
                    lightsOff = true;
                }
            } else {
                move.move(world, world.getAPos(), key, lightsOff);
                //if lights are off, then change the light grid accordingly
                if (lightsOff) {
                    TETile[][] lightGrid = world.getLitSurrounding();
                    ter.renderFrame(lightGrid);
                    //hud.renderHUD(world);
                } else { //otherwise rerender the full world
                    ter.renderFrame(world.getTiles());
                    hud.renderHUD(world);

                }
            }
        }
        return world.getTiles();

    }


    /**
     * Used to tell the autograder which tiles are the floor/ground (including
     * any lights/items resting on the ground). Change this
     * method if you add additional tiles.
     */
    public static boolean isGroundTile(TETile t) {
        return t.character() == Tileset.FLOOR.character()
                || t.character() == Tileset.AVATAR.character()
                || t.character() == Tileset.FLOWER.character();
    }

    /**
     * Used to tell the autograder while tiles are the walls/boundaries. Change
     * this method if you add additional tiles.
     */
    public static boolean isBoundaryTile(TETile t) {
        return t.character() == Tileset.WALL.character()
                || t.character() == Tileset.LOCKED_DOOR.character()
                || t.character() == Tileset.UNLOCKED_DOOR.character();
    }
}
