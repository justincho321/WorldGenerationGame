package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class World {
    private static int width = 60;
    private static int height = 30;
    private TETile[][] tiles;

    private HashMap<Integer, Room> roomMap;
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

    //temporary seed until can generate
    private long SEED = 23758378;
    //old seed: 23758373
    private Random RANDOM = new Random(SEED);
    public TETile[][] returnWorld(){
        return tiles;
    }

    //WORLD CONSTRUCTOR
    public World(long SEED) { //eugenia: world should take in the seed that puts out random rooms; NO static final
        this.RANDOM = new Random(SEED);
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

        //add random rooms
        for (int i = 0; i < roomNumbers; i++) {
            makeRandomRooms();
        }

        //add hallways
        for (int i = 0; i < (roomNumbers - 2); i++){
            MakeHallways(roomMap.get(6), roomMap.get(i + 1));
        }

        //tiles[37][7] = Tileset.LOCKED_DOOR;
    }

    public TETile[][] getTiles() {
        return tiles;
    }

    //MAKE A SINGLE RANDOM ROOM IN A RANDOM LOCATION ON THE MAP.
    private void makeRandomRooms() {

        int roomHeight = RANDOM.nextInt(6);
        roomHeight += 3;
        int roomWidth = RANDOM.nextInt(3);
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

        Room room = new Room(roomCounter, bottomLeftX, bottomLeftY, roomWidth, roomHeight);
        roomMap.put(roomCounter, room);
        roomCounter += 1;
        //add to room hashmap
        //DELETE - OBSOLETE
//        int[] roomInfo = new int[4];
//        roomInfo[0] = roomStartWidth;
//        roomInfo[1] = roomStartHeight;
//        roomInfo[2] = roomWidth;
//        roomInfo[3] = roomHeight;

        //populate room with floors
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

    //in the return array list, the first integer is 1 if the hallway is horizontal, or -1 if it's vertical
    //the second integer is the lower bound in the range of overlapping region
    //the third integer is the upper bound in the range of overlapping region
    private int[] boolMakeHallways(Room room1, Room room2) {
        int[] returnArray = new int[3];
        //HORIZONTALLY
        //find smaller of the two ranges
        Room smallerRoom;
        Room biggerRoom;
        if (room1.height > room2.height) {
            smallerRoom = room2;
            biggerRoom = room1;

        } else {
            smallerRoom = room1;
            biggerRoom = room2;
        }

        //checking if valid overlapping region of the vertical walls
        for (int i = smallerRoom.startY; i < smallerRoom.startY + smallerRoom.height; i++) {
            if (i < (biggerRoom.startY + biggerRoom.height) && i >= (biggerRoom.startY)) {
                //setting return array value indicating horizontal
                returnArray[0] = 1;

                //check relative y position of two rooms
                //FIRST CASE: bigger room LOWER than smaller room
                if (smallerRoom.startY > biggerRoom.startY && biggerRoom.startY + biggerRoom.height < smallerRoom.startY + smallerRoom.height) {
                    if (smallerRoom.startY + 1 < height) {
                        returnArray[1] = smallerRoom.startY + 1;
                    } else {
                        return null;
                    }

                    if (biggerRoom.startY + biggerRoom.height - 1 >= 0) {
                        returnArray[2] = biggerRoom.startY + biggerRoom.height - 1;
                    } else {
                        return null;
                    }
                }

                //SECOND CASE: bigger room HIGHER than smaller room
                else if (smallerRoom.startY < biggerRoom.startY && biggerRoom.startY + biggerRoom.height > smallerRoom.startY + smallerRoom.height) {
                    if (biggerRoom.startY + 1 < height) {
                        returnArray[1] = biggerRoom.startY + 1;
                    } else {
                        return null;
                    }
                    if (smallerRoom.startY + smallerRoom.height - 1 >= 0) {
                        returnArray[2] = smallerRoom.startY + smallerRoom.height - 1;
                    } else {
                        return null;
                    }
                }
                //THIRD CASE: smaller room IN BETWEEN Bigger Room
                else {
                    if (smallerRoom.startY + 1 < height) {
                        returnArray[1] = smallerRoom.startY + 1;
                    } else {
                        return null;
                    }
                    if (smallerRoom.startY + smallerRoom.height - 1 >= 0) {
                        returnArray[2] = smallerRoom.startY + smallerRoom.height - 1;
                    } else {
                        return null;
                    }
                }
                return returnArray;
            }

        }

        //VERTICAL
        //find smaller of the two ranges
        if (room1.width > room2.width) {
            smallerRoom = room2;
            biggerRoom = room1;

        } else {
            smallerRoom = room1;
            biggerRoom = room2;
        }

        for (int i = smallerRoom.startX; i < smallerRoom.startX + smallerRoom.width; i++) {
            if (i < (biggerRoom.startX + biggerRoom.width) && i >= (biggerRoom.startX)) {

                //setting return array value indicating horizontal
                returnArray[0] = -1;

                //TODO: FILL LATER WITH HORIZONTAL IF STATEMENTS
            }
        }

        return null;
    }
    private void MakeHallways(Room room1, Room room2) {

        int[] roomInfo = boolMakeHallways(room1, room2);

        //if roomInfo returned nothing, there is no valid hallway between these 2 rooms, so return
        if (roomInfo == null) {
            return;
        }

        int lowerBound = roomInfo[1];
        int upperBound = roomInfo[2];
        if (upperBound - lowerBound <= 0) {
            return;
        }
        //HORIZONTAL
        if (roomInfo[0] == 1) {

            //check if room 1 is to the left of room 2, and vice versa
            Room leftRoom;
            Room rightRoom;

            if (room1.startX < room2.startX) {
                leftRoom = room1;
                rightRoom = room2;
            } else {
                leftRoom = room2;
                rightRoom = room1;
            }

            //choose random starting y position for the middle of the hallway within the valid range
            int hallStartY = RANDOM.nextInt(upperBound - lowerBound);
            hallStartY += lowerBound;

            if (checkAdjHallwayY(leftRoom, rightRoom, hallStartY)) {
                return;
                //not make the hallway
                //shift the hallway up or down so it's not touching
            }

            for (int i = leftRoom.startX + leftRoom.width; i <= rightRoom.startX; i++) {


                tiles[i][hallStartY - 1] = Tileset.WALL;
                tiles[i][hallStartY] = Tileset.FLOOR;
                tiles[i][hallStartY + 1] = Tileset.WALL;
            }
        }
//        else if (boolMakeHallways(room1, room2) == "y") {
//            for (int j = 0; j < vertDistBetweenRooms; j++) {
//                if (room1.startY + room1.height + j == height - 1) {
//                    break;
//                }
//                tiles[room1.startY][room1.startY + room1.height + j] = Tileset.WALL;
//                tiles[room2.startY][room2.startY + room2.height + j] = Tileset.WALL;
//            }
//        }
    }

    private boolean checkAdjHallwayY(Room leftRoom, Room rightRoom, int hallStartY) {
        //if hallway right above or below
        if (hallStartY + 1 < height && hallStartY + 2 < height && hallStartY + 3 < height) {
            if (tiles[leftRoom.startX + 2][hallStartY + 1] == Tileset.WALL && tiles[leftRoom.startX + 2][hallStartY + 2] == Tileset.FLOOR && tiles[leftRoom.startX + 2][hallStartY + 3] == Tileset.WALL) {
                return true;
            }
        } else if (hallStartY - 1 >= 0 && hallStartY - 2 >= 0 && hallStartY - 3 >= 0) {
            if (tiles[leftRoom.startX + 2][hallStartY - 1] == Tileset.WALL && tiles[leftRoom.startX + 2][hallStartY - 2] == Tileset.FLOOR && tiles[leftRoom.startX + 2][hallStartY - 3] == Tileset.WALL) {
                return true;
            }
        }
        return false;
    }

}
