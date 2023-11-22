package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;

public class Game {
    private long lastSeed;
    private TETile[][] tiles;
    private TETile nothing = Tileset.CUSTOM_NOTHING;
    private boolean keepGame = true;
    public Game(int width, int height) {
    }
    private long prevActionTimestamp;
    private long prevFrameTimestamp;

    //Constructor
    public Game() {
        //add constructor stuff
    }

    //render the game
    public void renderGame() {
        //add
    }

    //handles when to render and update the game board
    public void runGame(World world, int WIDTH, int HEIGHT) {

        HUD hud = new HUD();
        Move move = new Move();
        TERenderer ter = new TERenderer();
        double xCurr = 0;
        double yCurr = 0;
        boolean colon = false;


        resetActionTimer();
        resetFrameTimer();

        ter.initialize(WIDTH, HEIGHT + 3);
        ter.renderFrame(world.getTiles());

        //maybe don't need if we have ter.renderFrame
        //renderGame();
        hud.renderHUD(world.getTiles());

        while (keepGame) {
            if (shouldRenderNewFrame()) {
                //add series of renders

                //only rerender frame if press key or something else changes
                if (StdDraw.hasNextKeyTyped()) {
                    char key = StdDraw.nextKeyTyped();
                    if (colon && key == 'Q') {
                        saveAndQuit(world);
                        colon = false;
                    } else if (key == ':') {
                        colon = true;
                    } else {
                        move.move(world, world.getAPos(), key);
                        ter.renderFrame(world.getTiles());
                    }
                }

                //only rerender HUD if mouse cursor moves
                if (StdDraw.mouseX() != xCurr || StdDraw.mouseY() != yCurr) {
                    ter.renderFrame(world.getTiles());
                    hud.renderHUD(world.getTiles());
                    xCurr = StdDraw.mouseX();
                    yCurr = StdDraw.mouseY();
                }
                //StdDraw.pause(100);
            }
        }
    }

    public void saveAndQuit(World world) {
        File file = new File("C:\\Programs\\CS61B\\fa23-proj3-g232\\proj3\\src\\gamelog\\savedGame.txt");
        if (file.exists()) {
            file.delete();
        }
        lastSeed = world.getSeed();

        //save avatar position
        String aPos = world.getAPos()[0] + "," + world.getAPos()[1];
        try {
            FileWriter myWriter = new FileWriter(file);
            myWriter.write(lastSeed + "\n");
            myWriter.write(aPos);
            myWriter.close();
            System.out.println("Successfully saved the game file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        System.exit(0);
    }

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
}
