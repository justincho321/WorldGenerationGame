package core;

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

    //setting custom tiles
    private TETile wall = Tileset.CUSTOM_WALL;
    private TETile floor = Tileset.CUSTOM_FLOOR;
    private TETile nothing = Tileset.CUSTOM_NOTHING;

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
                tiles[x][y] = nothing;
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

        //tiles[37][7] = Tileset.LOCKED_DOOR;
    }

    public TETile[][] getTiles() {
        return tiles;
    }

    private void makeRandomRooms() {

        int roomHeight = RANDOM.nextInt(8);
        roomHeight += 3;
        int roomWidth = RANDOM.nextInt(5);
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
            int firstX = roomStartWidth + (j * multiplier1);
            int firstY = roomStartHeight;
            TETile first = tiles[roomStartWidth + (j * multiplier1)][roomStartHeight];

            int secondX = roomStartWidth + (j * multiplier1);
            int secondY = ceiling;
            TETile second = tiles[roomStartWidth + (j * multiplier1)][ceiling];

            if (checkMidWallCase(firstX, firstY, "y") == true) {
                tiles[roomStartWidth + (j * multiplier1)][roomStartHeight] = floor;
            } else if (first != floor) {
                tiles[roomStartWidth + (j * multiplier1)][roomStartHeight] = wall;
            }

            if (checkMidWallCase(secondX, secondY, "y") == true) {
                tiles[roomStartWidth + (j * multiplier1)][ceiling] = floor;
            } else if (second != floor) {
                tiles[roomStartWidth + (j * multiplier1)][ceiling] = wall;
            }
        }
        //makes the vertical walls
        for (int k = 0; k <= roomHeight; k++) {
            //if we iterate past the boundary height! Break the loop
            int thirdX = roomStartWidth;
            int thirdY = roomStartHeight + (k * multiplier2);
            TETile third = tiles[thirdX][thirdY];

            int fourthX = oppWall;
            int fourthY = roomStartHeight + (k * multiplier2);
            TETile fourth = tiles[fourthX][fourthY];

            //third case
            if (checkMidWallCase(thirdX, thirdY, "x") == true) {
                tiles[roomStartWidth][roomStartHeight + (k * multiplier2)] = floor;
            } else if (third != floor) {
                tiles[roomStartWidth][roomStartHeight + (k * multiplier2)] = wall;
            }

            //fourth case
            if (checkMidWallCase(fourthX, fourthY, "x") == true) {
                tiles[oppWall][roomStartHeight + (k * multiplier2)] = floor;
            } else if (fourth != floor) {
                tiles[oppWall][roomStartHeight + (k * multiplier2)] = wall;
            }


        }

        //add to room hashmap
        //DELETE - OBSOLETE
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
                tiles[i][j] = floor;
            }
        }
    }

    private boolean checkMidWallCase(int x, int y, String direction) {
        if (direction == "x") {
            if ((x - 1) >= 0 && (x + 1) < width) {
                TETile temp11 = tiles[x][y];
                TETile temp12 = tiles[x + 1][y];
                TETile temp13 = tiles[x - 1][y];
                if (temp11 == wall && temp12 == floor && temp13 != nothing) {
                    return true;
                }
            }
            if ((x + 1) < width && (x - 1) >= 0) {
                TETile temp21 = tiles[x][y];
                TETile temp22 = tiles[x - 1][y];
                TETile temp23 = tiles[x + 1][y];
                if (temp21 == wall && temp22 == floor && temp23 != nothing) {
                    return true;
                }
            }
        }
        else if (direction == "y") {
            if ((y - 1) >= 0 && (y + 1) < height) {
                TETile temp31 = tiles[x][y];
                TETile temp32 = tiles[x][y + 1];
                TETile temp33 = tiles[x][y - 1];
                if (temp31 == wall && temp32 == floor && temp33 != nothing) {
                    return true;
                }
            }
            if ((y + 1) < height && (y - 1) >= 0) {
                TETile temp41 = tiles[x][y];
                TETile temp42 = tiles[x][y - 1];
                TETile temp43 = tiles[x][y + 1];
                if (temp41 == wall && temp42 == floor && temp43 != nothing) {
                    return true;
                }
            }
        }
        return false;
    }

}
