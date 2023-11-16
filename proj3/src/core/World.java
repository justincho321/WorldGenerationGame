package core;
import tileengine.TETile;
import tileengine.Tileset;
import java.util.HashMap;
import java.util.Random;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class World {
    private int width = 60;
    private int height = 30;
    private TETile[][] tiles;

    private HashMap<Integer, Room> roomMap;
    private int roomCounter = 0;
    private WeightedQuickUnionUF wqu;
    private Random RANDOM;

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

    //WORLD CONSTRUCTOR
    public World(long seed) {

        RANDOM = new Random(seed);
        //make tiles object and populate with nothing initially
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

        //make a WeightedQuickUnionUF for the number of rooms
        wqu = new WeightedQuickUnionUF(roomNumbers);

        //add random rooms
        for (int i = 0; i < roomNumbers; i++) {
            makeRandomRooms(i);
        }

        //add hallways
        for (int j = 0; j < roomNumbers; j++) {
            for (int i = 0; i < roomNumbers; i++) {
                if (makeHallwaysY(roomMap.get(j), roomMap.get(i))) {
                    wqu.union(j, i);
                }

                if (makeHallwaysX(roomMap.get(j), roomMap.get(i))) {
                    wqu.union(j, i);
                }
            }
        }
        //tiles[18][14] = Tileset.AVATAR;
        //tiles[7][6] = Tileset.AVATAR;
        while (wqu.count() != 1) {
            for (int i = 0; i < roomNumbers; i++) {
                for (int j = 0; j < roomNumbers; j++) {
                    if (!wqu.connected(i, j)) {
                        connectRoomToAnythingByHall(roomMap.get(j));
                        connectRoomToAnythingByHall(roomMap.get(i));
                    }
                }
            }
        }

        int temp = 4;
    }

    public TETile[][] returnWorld() {
        return tiles;
    }

    public TETile[][] getTiles() {
        return tiles;
    }

    //MAKE A SINGLE RANDOM ROOM IN A RANDOM LOCATION ON THE MAP.
    private void makeRandomRooms(int roomNumber) {
        int roomHeight = RANDOM.nextInt(5);
        roomHeight += 3;
        int roomWidth = RANDOM.nextInt(3);
        roomWidth += 3;
        int roomStartHeight = RANDOM.nextInt(height);
        int roomStartWidth = RANDOM.nextInt(width);
        int bottomLeftX = roomStartWidth - roomWidth;
        int bottomLeftY = roomStartHeight - roomHeight;
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
            int[] results = checkMidWallCase(firstX, firstY, "y");
            if (results[0] == 1 && results[1] != -1) {
                tiles[roomStartWidth + (j * multiplier1)][roomStartHeight] = floor;
                //check which room we are combining with and union them with our wqu
                wqu.union(roomNumber, results[1]);
            } else if (first != floor) {
                tiles[roomStartWidth + (j * multiplier1)][roomStartHeight] = wall;
            }
            results = checkMidWallCase(secondX, secondY, "y");
            if (results[0] == 1 && results[1] != -1) {
                tiles[roomStartWidth + (j * multiplier1)][ceiling] = floor;
                wqu.union(roomNumber, results[1]);
            } else if (second != floor) {
                tiles[roomStartWidth + (j * multiplier1)][ceiling] = wall;
            }
        }
        //makes the vertical walls
        for (int k = 0; k <= roomHeight; k++) {
            int thirdX = roomStartWidth;
            int thirdY = roomStartHeight + (k * multiplier2);
            TETile third = tiles[thirdX][thirdY];
            int fourthX = oppWall;
            int fourthY = roomStartHeight + (k * multiplier2);
            TETile fourth = tiles[fourthX][fourthY];
            int[] results = checkMidWallCase(thirdX, thirdY, "x");
            if (results[0] == 1 && results[1] != -1) {
                tiles[roomStartWidth][roomStartHeight + (k * multiplier2)] = floor;
                wqu.union(roomNumber, results[1]);
            } else if (third != floor) {
                tiles[roomStartWidth][roomStartHeight + (k * multiplier2)] = wall;
            }
            results = checkMidWallCase(fourthX, fourthY, "x");
            if (results[0] == 1 && results[1] != -1) {
                tiles[oppWall][roomStartHeight + (k * multiplier2)] = floor;
                wqu.union(roomNumber, results[1]);
            } else if (fourth != floor) {
                tiles[oppWall][roomStartHeight + (k * multiplier2)] = wall;
            }
        }
        Room room = new Room(roomCounter, bottomLeftX, bottomLeftY, roomWidth, roomHeight);
        roomMap.put(roomCounter, room);
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

    //first value of return array: 1 if true (you are building into another room), or 0 if false
    //second value of return array: the room number that you are building; if building into no room, this value is -1;
    private int[] checkMidWallCase(int x, int y, String direction) {
        int[] returnArray = new int[2];
        int roomNum;
        if (direction.equals("x")) {
            if ((x - 1) >= 0 && (x + 1) < width) {
                TETile temp11 = tiles[x][y];
                TETile temp12 = tiles[x + 1][y];
                TETile temp13 = tiles[x - 1][y];
                if (temp11 == wall && temp12 == floor && temp13 != nothing) {
                    roomNum = findRoomNumber(x + 1, y);
                    returnArray[0] = 1;
                    returnArray[1] = roomNum;
                    return returnArray;
                }
            }
            if ((x + 1) < width && (x - 1) >= 0) {
                TETile temp21 = tiles[x][y];
                TETile temp22 = tiles[x - 1][y];
                TETile temp23 = tiles[x + 1][y];
                if (temp21 == wall && temp22 == floor && temp23 != nothing) {
                    roomNum = findRoomNumber(x - 1, y);
                    returnArray[0] = 1;
                    returnArray[1] = roomNum;
                    return returnArray;
                }
            }
        } else if (direction.equals("y")) {
            if ((y - 1) >= 0 && (y + 1) < height) {
                TETile temp31 = tiles[x][y];
                TETile temp32 = tiles[x][y + 1];
                TETile temp33 = tiles[x][y - 1];
                if (temp31 == wall && temp32 == floor && temp33 != nothing) {
                    roomNum = findRoomNumber(x, y + 1);
                    returnArray[0] = 1;
                    returnArray[1] = roomNum;
                    return returnArray;
                }
            }
            if ((y + 1) < height && (y - 1) >= 0) {
                TETile temp41 = tiles[x][y];
                TETile temp42 = tiles[x][y - 1];
                TETile temp43 = tiles[x][y + 1];
                if (temp41 == wall && temp42 == floor && temp43 != nothing) {
                    roomNum = findRoomNumber(x, y - 1);
                    returnArray[0] = 1;
                    returnArray[1] = roomNum;
                    return returnArray;
                }
            }
        }
        returnArray[0] = 0;
        returnArray[1] = -1;
        return returnArray;
    }

    //Really useful method: given an x,y coordinate, returns the room number that that coordinate is in
    //if it's in no particular room, return -1;
    private int findRoomNumber(int x, int y) {
        for (int i = 0; i < roomCounter; i++) {
            Room tempRoom = roomMap.get(i);

            int stX = tempRoom.startX;
            int stY = tempRoom.startY;
            if (x > stX && (x) < stX + tempRoom.width && y > stY && y < stY + tempRoom.height) {
                return i;
            }
        }
        return -1;
    }

    //in the return array list, the first integer is 1 if the hallway is horizontal, or -1 if it's vertical
    //the second integer is the lower bound in the range of overlapping region
    //the third integer is the upper bound in the range of overlapping region
    private int[] boolMakeHallwaysX(Room room1, Room room2) {
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
        int smStX = smallerRoom.startX;
        int smStY = smallerRoom.startY;
        int bgStX = biggerRoom.startX;
        int bgStY = biggerRoom.startY;
        //checking if valid overlapping region of the vertical walls
        for (int i = smallerRoom.startY; i < smallerRoom.startY + smallerRoom.height; i++) {
            if (i < (biggerRoom.startY + biggerRoom.height) && i >= (biggerRoom.startY)) {
                //setting return array value indicating horizontal
                returnArray[0] = 1;

                //check relative y position of two rooms
                //FIRST CASE: bigger room LOWER than smaller room
                if (smStY > biggerRoom.startY && biggerRoom.startY + biggerRoom.height < smStY + smallerRoom.height) {
                    if (smallerRoom.startY + 1 < height) {
                        returnArray[1] = smallerRoom.startY + 1;
                    } else {
                        return null;
                    }

                    if (biggerRoom.startY + biggerRoom.height - 1 >= 0) {
                        //PREVIOUSLY SUBTRACT BY 1 in returnArray[2]
                        returnArray[2] = biggerRoom.startY + biggerRoom.height - 1;
                    } else {
                        return null;
                    }
                } else if (smStY < bgStY && bgStY + biggerRoom.height > smStY + smallerRoom.height) {
                    //SECOND CASE: bigger room HIGHER than smaller room
                    if (biggerRoom.startY + 1 < height) {
                        returnArray[1] = biggerRoom.startY + 1;
                    } else {
                        return null;
                    }
                    if (smallerRoom.startY + smallerRoom.height - 1 >= 0) {
                        //PREVIOUSLY SUBTRACT BY 1 in returnArray[2]
                        returnArray[2] = smallerRoom.startY + smallerRoom.height;
                    } else {
                        return null;
                    }
                } else {
                    //THIRD CASE: smaller room IN BETWEEN Bigger Room
                    if (smallerRoom.startY + 1 < height) {
                        returnArray[1] = smallerRoom.startY + 1;
                    } else {
                        return null;
                    }
                    if (smallerRoom.startY + smallerRoom.height - 1 >= 0) {
                        //PREVIOUSLY SUBTRACT BY 1 in returnArray[2]
                        returnArray[2] = smallerRoom.startY + smallerRoom.height - 1;
                    } else {
                        return null;
                    }
                }
                return returnArray;
            }

        }
        return null;
    }

    private int[] boolMakeHallwaysY(Room room1, Room room2) {
        int[] returnArray = new int[3];
        //HORIZONTALLY
        //find smaller of the two ranges
        Room smallerRoom;
        Room biggerRoom;
        //VERTICALLY
        //find smaller of the two ranges
        if (room1.width > room2.width) {
            smallerRoom = room2;
            biggerRoom = room1;

        } else {
            smallerRoom = room1;
            biggerRoom = room2;
        }
        int smStX = smallerRoom.startX;
        int smStY = smallerRoom.startY;
        int bgStX = biggerRoom.startX;
        int bgStY = biggerRoom.startY;
        for (int i = smallerRoom.startX; i < smallerRoom.startX + smallerRoom.width; i++) {
            if (i < (biggerRoom.startX + biggerRoom.width) && i >= (biggerRoom.startX)) {

                //setting return array value indicating horizontal
                returnArray[0] = -1;

                //check relative y position of two rooms
                //FIRST CASE: bigger room LEFTER than smaller room
                if (smStX > bgStX && bgStX + biggerRoom.width < smStX + smallerRoom.width) {
                    if (smallerRoom.startX + 1 < width) {
                        returnArray[1] = smallerRoom.startX + 1;
                    } else {
                        return null;
                    }

                    if (biggerRoom.startX + biggerRoom.width - 1 >= 0) {
                        //PREVIOUSLY SUBTRACT BY 1 in returnArray[2]
                        returnArray[2] = biggerRoom.startX + biggerRoom.width - 1;
                    } else {
                        return null;
                    }
                } else if (smStX < bgStX && bgStX + biggerRoom.width > smStX + smallerRoom.width) {
                    //SECOND CASE: bigger room RIGHTER than smaller room
                    if (biggerRoom.startX + 1 < width) {
                        returnArray[1] = biggerRoom.startX + 1;
                    } else {
                        return null;
                    }
                    if (smallerRoom.startX + smallerRoom.width - 1 >= 0) {
                        //PREVIOUSLY SUBTRACT BY 1 in returnArray[2]
                        returnArray[2] = smallerRoom.startX + smallerRoom.width;
                    } else {
                        return null;
                    }
                } else {
                    //THIRD CASE: smaller room IN BETWEEN Bigger Room
                    if (smallerRoom.startX + 1 < width) {
                        returnArray[1] = smallerRoom.startX + 1;
                    } else {
                        return null;
                    }
                    if (smallerRoom.startX + smallerRoom.width - 1 >= 0) {
                        //PREVIOUSLY SUBTRACT BY 1 in returnArray[2]
                        returnArray[2] = smallerRoom.startX + smallerRoom.width - 1;
                    } else {
                        return null;
                    }
                }
                return returnArray;
            }
        }
        return null;
    }

    private boolean makeHallwaysX(Room room1, Room room2) {

        int[] roomInfo = boolMakeHallwaysX(room1, room2);

        //if roomInfo returned nothing, there is no valid hallway between these 2 rooms, so return
        if (roomInfo == null) {
            return false;
        }

        int lowerBound = roomInfo[1];
        int upperBound = roomInfo[2];
        if (upperBound - lowerBound <= 0) {
            return false;
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
                return false;
            }

            for (int i = leftRoom.startX + leftRoom.width; i <= rightRoom.startX; i++) {

                if (tiles[i][hallStartY - 1] != floor) {
                    tiles[i][hallStartY - 1] = wall;
                }
                tiles[i][hallStartY] = floor;
                if (tiles[i][hallStartY + 1] != floor) {
                    tiles[i][hallStartY + 1] = wall;
                }
            }
        }
        return true;
    }

    private boolean makeHallwaysY(Room room1, Room room2) {

        int[] roomInfo = boolMakeHallwaysY(room1, room2);

        //if roomInfo returned nothing, there is no valid hallway between these 2 rooms, so return
        if (roomInfo == null) {
            return false;
        }

        int lowerBound = roomInfo[1];
        int upperBound = roomInfo[2];
        if (upperBound - lowerBound <= 0) {
            return false;
        }

        //VERTICAL
        if (roomInfo[0] == -1) {

            //check if room 1 is to the left of room 2, and vice versa
            Room lowerRoom;
            Room upperRoom;

            if (room1.startY < room2.startY) {
                lowerRoom = room1;
                upperRoom = room2;
            } else {
                lowerRoom = room2;
                upperRoom = room1;
            }

            //choose random starting y position for the middle of the hallway within the valid range
            int hallStartX = RANDOM.nextInt(upperBound - lowerBound);
            hallStartX += lowerBound;

            if (checkAdjHallwayX(lowerRoom, upperRoom, hallStartX)) {
                return false;
                //not make the hallway
                //shift the hallway up or down so it's not touching
            }

            for (int i = lowerRoom.startY + lowerRoom.height; i <= upperRoom.startY; i++) {

                if (tiles[hallStartX - 1][i] != floor) {
                    tiles[hallStartX - 1][i] = wall;
                }
                tiles[hallStartX][i] = floor;
                if (tiles[hallStartX + 1][i] != floor) {
                    tiles[hallStartX + 1][i] = wall;
                }
            }
        }
        return true;
    }

    private void connectRoomToAnythingByHall(Room room) {

        int finalRoomNum = 0;

        //TOP RIGHT QUADRANT
        if (room.startY > 15 && room.startX > 30) {
            connectRoomTopRightHelper(room);

        }
        //BOTTOM RIGHT QUADRANT
        if (room.startY < 16 && room.startX > 30) {
            connectRoomBottomRightHelper(room);

        }

        //TOP LEFT QUADRANT
        if (room.startY > 15 && room.startX < 31) {
            connectRoomTopLeftHelper(room);
        }

        //if bottom left quadrant
        if (room.startY < 16 && room.startX < 31) {
            connectRoomBottomLeftHelper(room);
        }
    }

    private void connectRoomTopRightHelper(Room room) {
        int finalRoomNum = 0;
        //build left
        for (int i = room.startX; i >= 0; i--) {
            if (tiles[i][room.startY + 2] != floor) {
                tiles[i][room.startY + 2] = wall;
            }
            if (tiles[i][room.startY + 1] == floor) {

                //if we reached a floor of another hallway or room, check if we're in a room.
                // if not, traverse the floor until we reach a room
                //then connect that room with our room.
                if (findRoomNumber(i, room.startY + 1) == -1) {
                    if (tiles[i][room.startY] == floor) {
                        int j = 1;

                        if (tiles[i + 1][room.startY] == floor) {
                            wqu.union(finalRoomNum, room.num);
                            return;
                        }
                        //build down until you reach a room
                        while (findRoomNumber(i, room.startY - j) == -1) {
                            j++;
                        }
                        finalRoomNum = findRoomNumber(i, room.startY - j);
                        wqu.union(finalRoomNum, room.num);
                        return;
                    }
                } else {
                    wqu.union(finalRoomNum, room.num);
                    return;
                }
            } else {
                tiles[i][room.startY + 1] = floor;
            }
            if (tiles[i][room.startY] != floor) {
                tiles[i][room.startY] = wall;
            }
        }
        //build down
        for (int i = room.startY; i >= 0; i--) {
            if (tiles[room.startX + 2][i] != floor) {
                tiles[room.startX + 2][i] = wall;
            }
            if (tiles[room.startX + 1][i] == floor) {
                //wqu.union(findRoomNumber(room.startX + 1, i), room.num);

                //if we reached a floor of another hallway or room, check if we're in a room.
                // if not, traverse the floor until we reach a room
                //then connect that room with our room.
                if (findRoomNumber(room.startX + 1, i) == -1) {
                    if (tiles[room.startX][i] == floor) {
                        int j = 1;

                        if (tiles[room.startX][i + 1] == floor) {
                            wqu.union(finalRoomNum, room.num);
                            return;
                        }
                        //build left until you reach a room
                        while (findRoomNumber(room.startX - j, i) == -1) {
                            j++;
                        }
                        finalRoomNum = findRoomNumber(room.startX - j, i);
                        wqu.union(finalRoomNum, room.num);
                        return;
                    } else {
                        wqu.union(finalRoomNum, room.num);
                        return;
                    }
                }
            } else {
                tiles[room.startX + 1][i] = floor;
            }
            if (tiles[room.startX][i] != floor) {
                tiles[room.startX][i] = wall;
            }
        }
    }

    private void connectRoomBottomRightHelper(Room room) {
        int finalRoomNum = 0;
        //build left
        for (int i = room.startX; i >= 0; i--) {
            if (tiles[i][room.startY + 2] != floor) {
                tiles[i][room.startY + 2] = wall;
            }
            if (tiles[i][room.startY + 1] == floor) {

                //if we reached a floor of another hallway or room, check if we're in a room.
                // if not, traverse the floor until we reach a room
                //then connect that room with our room.
                if (findRoomNumber(i, room.startY + 1) == -1) {
                    if (tiles[i][room.startY] == floor) {
                        int j = 1;

                        if (tiles[i + 1][room.startY] == floor) {
                            wqu.union(finalRoomNum, room.num);
                            return;
                        }
                        //build up until you reach a room
                        while (findRoomNumber(i, room.startY + j) == -1) {
                            j++;
                        }
                        finalRoomNum = findRoomNumber(i, room.startY + j);
                        wqu.union(finalRoomNum, room.num);
                        return;
                    } else {
                        wqu.union(finalRoomNum, room.num);
                        return;
                    }
                }
            } else {
                tiles[i][room.startY + 1] = floor;
            }
            if (tiles[i][room.startY] != floor) {
                tiles[i][room.startY] = wall;
            }
        }
        //build up
        for (int i = room.startY + room.height; i < height; i++) {
            if (tiles[room.startX + 2][i] != floor) {
                tiles[room.startX + 2][i] = wall;
            }
            if (tiles[room.startX + 1][i] == floor) {
                //wqu.union(findRoomNumber(room.startX + 1, i), room.num);

                //if we reached a floor of another hallway or room, check if we're in a room.
                // if not, traverse the floor until we reach a room
                //then connect that room with our room.
                if (findRoomNumber(room.startX + 1, i) == -1) {
                    if (tiles[room.startX][i] == floor) {
                        int j = 1;

                        if (tiles[room.startX][i + 1] == floor) {
                            wqu.union(finalRoomNum, room.num);
                            return;
                        }
                        //build left until you reach a room
                        while (findRoomNumber(room.startX - j, i) == -1) {
                            j++;
                        }
                        finalRoomNum = findRoomNumber(room.startX - j, i);
                        wqu.union(finalRoomNum, room.num);
                        return;
                    } else {
                        wqu.union(finalRoomNum, room.num);
                        return;
                    }
                }
            } else {
                tiles[room.startX + 1][i] = floor;
            }
            if (tiles[room.startX][i] != floor) {
                tiles[room.startX][i] = wall;
            }
        }
    }

    private void connectRoomTopLeftHelper(Room room) {
        int finalRoomNum = 0;
        //build right
        for (int i = room.startX + room.width; i < width; i++) {
            if (tiles[i][room.startY + 2] != floor) {
                tiles[i][room.startY + 2] = wall;
            }
            if (tiles[i][room.startY + 1] == floor) {

                //if we reached a floor of another hallway or room, check if we're in a room.
                // if not, traverse the floor until we reach a room
                //then connect that room with our room.
                if (findRoomNumber(i, room.startY + 1) == -1) {
                    if (tiles[i][room.startY] == floor) {
                        int j = 1;

                        if (tiles[i + 1][room.startY] == floor) {
                            wqu.union(finalRoomNum, room.num);
                            return;
                        }
                        //build down until you reach a room
                        while (findRoomNumber(i, room.startY - j) == -1) {
                            j++;
                        }
                        finalRoomNum = findRoomNumber(i, room.startY - j);
                        wqu.union(finalRoomNum, room.num);
                        return;
                    } else {

                        wqu.union(finalRoomNum, room.num);
                        return;
                    }
                }
            } else {
                tiles[i][room.startY + 1] = floor;
            }
            if (tiles[i][room.startY] != floor) {
                tiles[i][room.startY] = wall;
            }
        }
        //build down
        for (int i = room.startY; i >= 0; i--) {
            if (tiles[room.startX + 2][i] != floor) {
                tiles[room.startX + 2][i] = wall;
            }
            if (tiles[room.startX + 1][i] == floor) {
                //wqu.union(findRoomNumber(room.startX + 1, i), room.num);

                //if we reached a floor of another hallway or room, check if we're in a room.
                // if not, traverse the floor until we reach a room
                //then connect that room with our room.
                if (findRoomNumber(room.startX + 1, i) == -1) {
                    if (tiles[room.startX][i] == floor) {
                        int j = 1;

                        if (tiles[room.startX][i + 1] == floor) {
                            wqu.union(finalRoomNum, room.num);
                            return;
                        }
                        //build right until you reach a room
                        while (findRoomNumber(room.startX + j, i) == -1) {
                            j++;
                        }
                        finalRoomNum = findRoomNumber(room.startX + j, i);
                        wqu.union(finalRoomNum, room.num);
                        return;
                    } else {
                        wqu.union(finalRoomNum, room.num);
                        return;
                    }
                }
            } else {
                tiles[room.startX + 1][i] = floor;
            }
            if (tiles[room.startX][i] != floor) {
                tiles[room.startX][i] = wall;
            }
        }
    }

    private void connectRoomBottomLeftHelper(Room room) {
        int finalRoomNum = 0;
        //build right
        for (int i = room.startX + room.width; i < width; i++) {
            if (tiles[i][room.startY + 2] != floor) {
                tiles[i][room.startY + 2] = wall;
            }
            if (tiles[i][room.startY + 1] == floor) {

                //if we reached a floor of another hallway or room, check if we're in a room.
                // if not, traverse the floor until we reach a room
                //then connect that room with our room.
                if (findRoomNumber(i, room.startY + 1) == -1) {
                    if (tiles[i][room.startY + 1] == floor) {
                        int j = 1;

                        if (tiles[i + 1][room.startY + 1] == floor) {
                            wqu.union(finalRoomNum, room.num);
                            return;
                        }
                        //build up until you reach a room
                        while (findRoomNumber(i, room.startY + j) == -1) {
                            j++;
                        }
                        finalRoomNum = findRoomNumber(i, room.startY + j);
                        wqu.union(finalRoomNum, room.num);
                        return;
                    }
                } else {
                    wqu.union(finalRoomNum, room.num);
                    return;
                }

            } else {
                tiles[i][room.startY + 1] = floor;
            }
            if (tiles[i][room.startY] != floor) {
                tiles[i][room.startY] = wall;
            }
        }
        //build up
        for (int i = room.startY + room.height; i < height; i++) {
            if (tiles[room.startX + 2][i] != floor) {
                tiles[room.startX + 2][i] = wall;
            }
            if (tiles[room.startX + 1][i] == floor) {
                //wqu.union(findRoomNumber(room.startX + 1, i), room.num);

                //if we reached a floor of another hallway or room, check if we're in a room.
                // if not, traverse the floor until we reach a room
                //then connect that room with our room.
                if (findRoomNumber(room.startX + 1, i) == -1) {
                    if (tiles[room.startX][i] == floor) {
                        int j = 1;

                        if (tiles[room.startX][i + 1] == floor) {
                            wqu.union(finalRoomNum, room.num);
                            return;
                        }
                        //build right until you reach a room
                        while (findRoomNumber(room.startX + j, i) == -1) {
                            j++;
                        }
                        finalRoomNum = findRoomNumber(room.startX + j, i);
                        wqu.union(finalRoomNum, room.num);
                        return;
                    } else {
                        wqu.union(finalRoomNum, room.num);
                        return;
                    }
                }
            } else {
                tiles[room.startX + 1][i] = floor;
            }
            if (tiles[room.startX][i] != floor) {
                tiles[room.startX][i] = wall;
            }
        }
    }
    private boolean checkAdjHallwayY(Room leftRoom, Room rightRoom, int hallStartY) {
        //if hallway right above or below
        for (int i = leftRoom.startX + leftRoom.width + 1; i < rightRoom.startX; i++) {
            if (hallStartY + 1 < height && hallStartY + 2 < height) {
                //walls of hallways are touching
                if (hallStartY + 3 < height) {
                    if (tiles[i][hallStartY + 1] == wall
                            && tiles[i][hallStartY + 2] == floor && tiles[i][hallStartY + 3] == wall) {
                        return true;
                    }
                }
                //wall of hallways are overlapping above
                if (tiles[i][hallStartY] == wall && tiles[i][hallStartY + 1] == floor
                        && tiles[i][hallStartY + 2] == wall) {
                    return true;
                }
            }
            if (hallStartY - 1 >= 0 && hallStartY - 2 >= 0) {
                //walls of hallways overlapping below
                if (hallStartY - 3 >= 0) {
                    if (tiles[i][hallStartY - 1] == wall && tiles[i][hallStartY - 2] == floor
                            && tiles[i][hallStartY - 3] == wall) {
                        return true;
                    }
                }
                //overlapping below
                if (tiles[i][hallStartY] == wall && tiles[i][hallStartY - 1] == floor
                        && tiles[i][hallStartY - 2] == wall) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkAdjHallwayX(Room leftRoom, Room rightRoom, int hallStartX) {
        //if hallway right above or below
        for (int i = leftRoom.startY + leftRoom.height + 1; i < rightRoom.startY; i++) {
            if (hallStartX + 1 < width && hallStartX + 2 < width) {
                //walls of hallways are touching
                if (hallStartX + 3 < width) {
                    if (tiles[hallStartX + 1][i] == wall && tiles[hallStartX + 2][i] == floor
                            && tiles[hallStartX + 3][i] == wall) {
                        return true;
                    }
                }
                //wall of hallways are overlapping above
                if (tiles[hallStartX][i] == wall && tiles[hallStartX + 1][i] == floor
                        && tiles[hallStartX + 2][i] == wall) {
                    return true;
                }
            }
            if (hallStartX - 1 >= 0 && hallStartX - 2 >= 0) {
                //walls of hallways overlapping below
                if (hallStartX - 3 >= 0) {
                    if (tiles[hallStartX - 1][i] == wall && tiles[hallStartX - 2][i] == floor
                            && tiles[hallStartX - 3][i] == wall) {
                        return true;
                    }
                }
                //overlapping below
                if (tiles[hallStartX][i] == wall && tiles[hallStartX - 1][i] == floor
                        && tiles[hallStartX - 2][i] == wall) {
                    return true;
                }
            }
        }
        return false;
    }


}
