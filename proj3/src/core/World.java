package core;

import org.junit.jupiter.api.ClassOrderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.util.HashMap;
import java.util.Random;

public class World {
    private int width;
    private int height;
    // build your own world!
    private TETile[][] tiles;

    private HashMap<Integer, int[]> roomMap;
    private int roomCounter = 0;
    private static final long SEED = 23758322;
    //old seed: 23758373
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
        int roomNumbers = RANDOM.nextInt(10); // there should be 10 to 20 random rom
        roomNumbers += 10;

        roomMap = new HashMap<>();

        //int myArray[] = new int[4];
        for (int x = 0; x < roomNumbers; x++) {
            roomMap.put(roomNumbers, new int[4]);
        }

        for (int i = 0; i < roomNumbers; i++) {
            makeRandomRooms();
        }
    }
    public TETile[][] getTiles() {
        return tiles;
    }
    private void makeRandomRooms() {

        int roomHeight = RANDOM.nextInt(7);
        roomHeight += 2;
        int roomWidth = RANDOM.nextInt(8);
        roomWidth += 2;
        int roomStartHeight = RANDOM.nextInt(height);
        int roomStartWidth = RANDOM.nextInt(width);

        int ceiling = roomStartHeight - roomHeight;
        int oppWall = roomStartWidth - roomWidth;
        int multiplier1 = -1;
        int multiplier2 = -1;

        //swap signs of ceiling if necessary
        if (ceiling < 0) {
            ceiling = roomStartHeight + roomHeight;
            multiplier2 = 1;

        }
        //swap signs of opposite wall if necessary
        if (oppWall < 0) {
            oppWall = roomStartWidth + roomWidth;
            multiplier1 = 1;
        }

        //makes the horizontal floor and ceiling
        for (int j = 0; j <= roomWidth; j++) {
            //if we iterate past the boundary width! Break the loop
            tiles[roomStartWidth + (j * multiplier1)][roomStartHeight] = Tileset.WALL;
            tiles[roomStartWidth + (j * multiplier1)][ceiling] = Tileset.WALL;
        }
        //makes the vertical walls
        for (int k = 0; k <= roomHeight; k++) {
            //if we iterate past the boundary height! Break the loop
            tiles[roomStartWidth][roomStartHeight + (k * multiplier2)] = Tileset.WALL;
            tiles[oppWall][roomStartHeight + (k * multiplier2)] = Tileset.WALL;
        }

        //add to room hashmap
        int[] roomInfo = new int[4];
        roomInfo[0] = roomStartWidth;
        roomInfo[1] = roomStartHeight;
        roomInfo[2] = roomWidth;
        roomInfo[3] = roomHeight;
        roomMap.put(roomCounter, roomInfo);
        roomCounter += 1;
    }

}
