package core;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import java.awt.Font;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;

import static java.lang.Character.getNumericValue;

public class Menu {
    public static TETile avatar = Tileset.AVATAR;
    private TETile[][] tiles;
    private TETile nothing = Tileset.CUSTOM_NOTHING;
    private boolean keepMenu = true;
    private boolean keepAvatarScreen = true;
    private long seed;
    private String avatarName;
    public Menu(int width, int height) {
        tiles = new TETile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = nothing;
            }
        }
    }

    public TETile[][] getTiles() {
        return tiles;
    }
    public void renderMenu() {

        StdDraw.setPenColor(255, 255, 255);

        String title = "THE GAME";
        Font bigFont = new Font("Monospaced", Font.BOLD, 50);
        StdDraw.setFont(bigFont);
        StdDraw.text(30, 22, title);

        Font smallFont = new Font("Monospaced", Font.BOLD, 20);
        StdDraw.setFont(smallFont);

        String newGame = "New Game (N)";
        StdDraw.text(30, 10, newGame);
        String loadGame = "Load Game (L)";
        StdDraw.text(30, 8, loadGame);
        String avatarName = "Set Avatar Name (A)";
        StdDraw.text(30, 6, avatarName);
        String quitGame = "Quit (Q)";
        StdDraw.text(30, 4, quitGame);

        StdDraw.show();
    }

    private long prevActionTimestamp;
    private long prevFrameTimestamp;

    /**
     * Calculates the delta time with the previous frame.
     * @return the amount of time between the previous frame with the present
     */
    private long frameDeltaTime() {
        return System.currentTimeMillis() - prevFrameTimestamp;
    }
    /**
     * Resets the action timestamp to the current time in milliseconds.
     */
    private void resetActionTimer() {
        prevActionTimestamp = System.currentTimeMillis();
    }

    /**
     * Resets the frame timestamp to the current time in milliseconds.
     */
    private void resetFrameTimer() {
        prevFrameTimestamp = System.currentTimeMillis();
    }

    public boolean shouldRenderNewFrame() {
        if (frameDeltaTime() > 16) {
            resetFrameTimer();
            return true;
        }
        return false;
    }

    public void runMenu() {
        resetActionTimer();
        resetFrameTimer();
        renderMenu();

        while (keepMenu) {
            if (shouldRenderNewFrame()) {
                if (StdDraw.hasNextKeyTyped()) {
                    char key = StdDraw.nextKeyTyped();
                    //gradient floors
                    Tileset.MyComponent gradient = new Tileset.MyComponent();
                    BufferedImage image = new BufferedImage(60, 30, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g = image.createGraphics();
                    gradient.paint(g);
                    if (key == 'N') {
                        seed = setSeed();

                        //make/run world/game and ADD first avatar
                        World world = new World(seed);
                        world.addFirstAvatar();
                        if (!Objects.equals(avatarName, "")) {
                            world.setAvatarName(avatarName);
                        }
                        Game game = new Game();
                        game.runGame(world, 60, 30);
                    } else if (key == 'L') {
                        loadGame();
                    } else if (key == 'A') {
                        String avatarName = setAvatarName();
                    } else if (key == 'Q') {
                        //double check if this is the correct functionality
                        System.exit(0);
                    }
                }
            }
        }
    }

    //set the world seed!
    private long setSeed() {
        StdDraw.clear(Color.BLACK);
        String title = "Please Enter A Seed (Finish by pressing 'S'):";
        StdDraw.text(30, 20, title);
        StdDraw.show();
        long seed = 0;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = (StdDraw.nextKeyTyped());
                if (key == 'S') {
                    keepMenu = false;
                    return seed;
                } else {
                    StdDraw.clear(Color.BLACK);
                    int numKey = Character.getNumericValue(key);
                    seed = 10 * seed + numKey;
                    String seedSoFar = String.valueOf(seed);
                    StdDraw.text(30, 20, title);
                    StdDraw.text(30, 15, seedSoFar);
                    StdDraw.show();

                }
            }
        }
    }

    //change the avatar name!
    public String setAvatarName() {
        StdDraw.clear(Color.BLACK);
        String title = "Please Enter A Name (Finish by Pressing 'S'):";
        StdDraw.text(30, 20, title);
        StdDraw.show();
        StringBuilder name = new StringBuilder();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = (StdDraw.nextKeyTyped());
                if (key == 'S') {
                    StdDraw.clear(Color.BLACK);
                    keepAvatarScreen = false;
                    avatarName = name.toString();
                    runMenu();
                } else {
                    StdDraw.clear(Color.BLACK);
                    name.append(key);
                    String seedSoFar = name.toString();
                    StdDraw.text(30, 20, title);
                    StdDraw.text(30, 15, seedSoFar);
                    StdDraw.show();
                }
            }
        }
    }

    //support for loading the game from the previous game state if available, when user presses 'L'
    public void loadGame() {
        File file = new File("C:\\Programs\\CS61B\\fa23-proj3-g232\\proj3\\src\\gamelog\\savedGame.txt");
        if (file.exists()) {
            In in = new In(file);
            String strSeed = in.readLine();
            String positionStr = in.readLine();
            String strAx = positionStr.split(",")[0];
            String strAy = positionStr.split(",")[1];
            String strLightsOff = in.readLine();
            String aName = in.readLine();

            long seed = Long.parseLong(strSeed);
            int aPosX = Integer.parseInt(strAx);
            int aPosY = Integer.parseInt(strAy);
            boolean lightsOff = Boolean.parseBoolean(strLightsOff);

            World world = new World(seed);
            Game game = new Game();
            avatar = new TETile('@', Color.white, Tileset.MyComponent.getColorAt(Tileset.MyComponent.getGradientPaint(), aPosX, aPosY), "you");
            world.getTiles()[aPosX][aPosY] = avatar;
            world.setAvatarName(aName);
            world.setLights(lightsOff); //must be done after making world
            world.setAPos(new int[] {aPosX, aPosY});
            game.runGame(world, 60, 30);

        } else {
            System.exit(0);
        }
    }

}
