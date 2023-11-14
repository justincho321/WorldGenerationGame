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

    //object representation of a room
    private class Room {
        int num;
        int startX;
        int startY;
        int width;
        int height;

        public Room(int num, int startX, int startY, int roomWidth, int roomHeight) {
            this.num = num;
            this.startX = startX;
            this.startY = startY;
            width = roomWidth;
            height = roomHeight;
        }
    }

    private static final long SEED = 23758378;
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

        //make hashmap of rooms
        roomMap = new HashMap<>();
        //populate room hashmap
        //MAYBE UNNECESSARY
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

        int roomHeight = RANDOM.nextInt(8);
        roomHeight += 3;
        int roomWidth = RANDOM.nextInt(10);
        roomWidth += 3;
        int roomStartHeight = RANDOM.nextInt(height);
        int roomStartWidth = RANDOM.nextInt(width);

        //bottom left point
        int bottomLeftX = roomStartWidth - roomWidth;
        int bottomLeftY = roomStartHeight - roomHeight;

        //bottom left point
        int ceiling = roomStartHeight - roomHeight;
        int oppWall = roomStartWidth - roomWidth;
        int multiplier1 = -1;
        int multiplier2 = -1;

        //swap signs of ceiling if necessary
        if (ceiling < 0) {
            ceiling = roomStartHeight + roomHeight;
            multiplier2 = 1;
            bottomLeftY = roomStartHeight;
        }
        //swap signs of opposite wall if necessary
        if (oppWall < 0) {
            oppWall = roomStartWidth + roomWidth;
            multiplier1 = 1;
            bottomLeftX = roomStartWidth;
        }

        //makes the horizontal floor and ceiling
        for (int j = 0; j <= roomWidth; j++) {
            //if we iterate past the boundary width! Break the loop
            TETile first = tiles[roomStartWidth + (j * multiplier1)][roomStartHeight];
            TETile second = tiles[roomStartWidth + (j * multiplier1)][ceiling];
            if (first != Tileset.FLOOR) {
                tiles[roomStartWidth + (j * multiplier1)][roomStartHeight] = Tileset.WALL;
            }
            if (second != Tileset.FLOOR) {
                tiles[roomStartWidth + (j * multiplier1)][ceiling] = Tileset.WALL;
            }
        }
        //makes the vertical walls
        for (int k = 0; k <= roomHeight; k++) {
            //if we iterate past the boundary height! Break the loop
            TETile third = tiles[roomStartWidth][roomStartHeight + (k * multiplier2)];
            TETile fourth = tiles[oppWall][roomStartHeight + (k * multiplier2)];
            if (third != Tileset.FLOOR) {
                tiles[roomStartWidth][roomStartHeight + (k * multiplier2)] = Tileset.WALL;
            }
            if (fourth != Tileset.FLOOR) {
                tiles[oppWall][roomStartHeight + (k * multiplier2)] = Tileset.WALL;
            }
        }

        //add to room hashmap
        int[] roomInfo = new int[4];
        roomInfo[0] = roomStartWidth;
        roomInfo[1] = roomStartHeight;
        roomInfo[2] = roomWidth;
        roomInfo[3] = roomHeight;
        roomMap.put(roomCounter, roomInfo);

        //make the room object use with setFloor method
        Room room = new Room(roomCounter, bottomLeftX, bottomLeftY, roomWidth, roomHeight);
        roomCounter += 1;
        setFloor(room);
    }

    private void setFloor(Room room) {
        for (int i = room.startX + 1; i < (room.startX + room.width); i++) {
            for (int j = room.startY + 1; j < (room.startY + room.height); j++) {
                tiles[i][j] = Tileset.FLOOR;
            }
        }
    }
    private boolean checkMidWallCase(int x, int y) {
        TETile temp1 = tiles[x + 1][y];
        TETile temp2 = tiles[x + 2][y];
        TETile temp3 = tiles[x - 1][y];
        TETile temp4 = tiles[x - 2][y];
        TETile temp5 = tiles[x][y + 1];
        TETile temp6 = tiles[x][y + 2];
        TETile temp7 = tiles[x][y - 1];
        TETile temp8 = tiles[x][y - 2];
        if (temp1 != null && temp2 != null && temp1 == Tileset.WALL && temp2 == Tileset.FLOOR) {
            return true;
        } else if (temp3 != null && temp4 != null && temp3 == Tileset.WALL && temp4 == Tileset.FLOOR) {
            return true;
        } else if (temp5 != null && temp6 != null && temp5 == Tileset.WALL && temp6 == Tileset.FLOOR) {
            return true;
        } else if (temp7 != null && temp8 != null && temp7 == Tileset.WALL && temp8 == Tileset.FLOOR) {
            return true;
        } else {
            return false;
        }
    }


}
