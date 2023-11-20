package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import java.awt.Font;
import java.awt.*;

import static java.lang.Character.getNumericValue;

public class Menu {
    private TETile[][] tiles;
    private TETile nothing = Tileset.CUSTOM_NOTHING;
    private boolean keepMenu = true;
    private long seed;
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
        String quitGame = "Quit (Q)";
        StdDraw.text(30, 6, quitGame);
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

    private long askSeed() {
        String title = "Please Enter A Seed (end with S):";
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

    public long runMenu() {
        resetActionTimer();
        resetFrameTimer();
        renderMenu();

        while (keepMenu) {
            if (shouldRenderNewFrame()) {
                if (StdDraw.hasNextKeyTyped()) {
                    char key = StdDraw.nextKeyTyped();
                    if (key == 'N') {
                        StdDraw.clear(Color.BLACK);
                        seed = askSeed();
                        return seed;
                    } else if (key == 'L') {
                        //loadGame();
                    } else if (key == 'Q') {
                        //quit();
                    }
                }
            }
        }
        return 0000;
    }

}
