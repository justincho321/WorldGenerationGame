package core;

import org.junit.jupiter.api.ClassOrderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.util.Random;

public class World {
    private int width;
    private int height;
    // build your own world!
    private TETile[][] tiles;
    private static final long SEED = 23758373;
    private static final Random RANDOM = new Random(SEED);
    public World(int width, int height) {
        this.width = width;
        this.height = height;
        tiles = new TETile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
        //populate and make randomized world helper method
        int roomNumbers = RANDOM.nextInt(4); // there should be 10 to 20 random rom
        //roomNumbers += 10;

        for (int i = 0; i < roomNumbers; i++) {
            makeRandomRooms();
        }
    }
    public TETile[][] getTiles() {
        return tiles;
    }
    private void makeRandomRooms() {

        int roomHeight = RANDOM.nextInt(7);
        int roomWidth = RANDOM.nextInt(8);
        int roomStartHeight = RANDOM.nextInt(30);
        int roomStartWidth = RANDOM.nextInt(60);

        for (int j = 0; j < roomWidth; j++) {
            tiles[roomStartWidth + j][roomStartHeight] = Tileset.WALL;
            tiles[roomStartWidth + j][roomStartHeight - roomHeight] = Tileset.WALL;
        }
        for (int k = 0; k < roomHeight; k++) {
            tiles[roomStartWidth][roomStartHeight - k] = Tileset.WALL;
            tiles[roomStartWidth + roomWidth][roomStartHeight - k] = Tileset.WALL;

        }

    }

}
