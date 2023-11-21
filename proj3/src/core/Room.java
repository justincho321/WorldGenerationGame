package core;

import tileengine.TETile;
import tileengine.Tileset;
import java.util.Random;

public class Room {
    int num;
    int startX;
    int startY;
    int width;
    int height;
    //public static TETile[][] fG = Tileset.floorGradient;
    static TETile floor;

    //CONSTRUCTOR
    public Room(int num, int startX, int startY, int roomWidth, int roomHeight) {
        this.num = num;
        this.startX = startX;
        this.startY = startY;
        width = roomWidth;
        height = roomHeight;
    }
    //SETTERS

    public void setNum(int n) {
        num = n;
    }

    public void setStartX(int x) {
        startX = x;
    }

    public void setStartY(int y) {
        startY = y;
    }

    public void getWidth(int w) {
        width = w;
    }

    public void setHeight(int h) {
        height = h;
    }

    //GETTERS
    public int getNum() {
        return num;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    //MAKE A SINGLE RANDOM ROOM IN A RANDOM LOCATION ON THE MAP.
    public static void makeRandomRoom(World world, int roomNumber) {
        Random random = world.getRANDOM();
        int roomHeight = random.nextInt(5);
        roomHeight += 3;
        int roomWidth = random.nextInt(3);
        roomWidth += 3;
        int roomStartHeight = random.nextInt(30);
        int roomStartWidth = random.nextInt(60);
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
            TETile first = world.getTiles()[roomStartWidth + (j * multiplier1)][roomStartHeight];
            int secondX = roomStartWidth + (j * multiplier1);
            int secondY = ceiling;
            TETile second = world.getTiles()[roomStartWidth + (j * multiplier1)][ceiling];
            int[] results = Room.checkMidWallCase(world, firstX, firstY, "y");
            if (results[0] == 1 && results[1] != -1) {
                floor = Tileset.fG[roomStartWidth + (j * multiplier1)][roomStartHeight];
                world.getTiles()[roomStartWidth + (j * multiplier1)][roomStartHeight] = floor;
                //check which room we are combining with and union them with our wqu
                world.getWQU().union(roomNumber, results[1]);
            } else if (first != Tileset.fG[roomStartWidth + (j * multiplier1)][roomStartHeight]) {
                world.getTiles()[roomStartWidth + (j * multiplier1)][roomStartHeight] = world.getWall();
            }
            results = Room.checkMidWallCase(world, secondX, secondY, "y");
            if (results[0] == 1 && results[1] != -1) {
                floor = Tileset.fG[roomStartWidth + (j * multiplier1)][ceiling];
                world.getTiles()[roomStartWidth + (j * multiplier1)][ceiling] = floor;
                world.getWQU().union(roomNumber, results[1]);
            } else if (second != Tileset.fG[roomStartWidth + (j * multiplier1)][ceiling]) {
                world.getTiles()[roomStartWidth + (j * multiplier1)][ceiling] = world.getWall();
            }
        }
        //makes the vertical walls
        for (int k = 0; k <= roomHeight; k++) {
            int thirdX = roomStartWidth;
            int thirdY = roomStartHeight + (k * multiplier2);
            TETile third = world.getTiles()[thirdX][thirdY];
            int fourthX = oppWall;
            int fourthY = roomStartHeight + (k * multiplier2);
            TETile fourth = world.getTiles()[fourthX][fourthY];
            int[] results = Room.checkMidWallCase(world, thirdX, thirdY, "x");
            if (results[0] == 1 && results[1] != -1) {
                floor = Tileset.fG[roomStartWidth][roomStartHeight + (k * multiplier2)];
                world.getTiles()[roomStartWidth][roomStartHeight + (k * multiplier2)] = floor;
                world.getWQU().union(roomNumber, results[1]);
            } else if (third != Tileset.fG[thirdX][thirdY]) {
                world.getTiles()[roomStartWidth][roomStartHeight + (k * multiplier2)] = world.getWall();
            }
            results = Room.checkMidWallCase(world, fourthX, fourthY, "x");
            if (results[0] == 1 && results[1] != -1) {
                floor = Tileset.fG[oppWall][roomStartHeight + (k * multiplier2)];
                world.getTiles()[oppWall][roomStartHeight + (k * multiplier2)] = floor;
                world.getWQU().union(roomNumber, results[1]);
            } else if (fourth != Tileset.fG[fourthX][fourthY]) {
                world.getTiles()[oppWall][roomStartHeight + (k * multiplier2)] = world.getWall();
            }
        }
        Room room = new Room(world.getRoomCounter(), bottomLeftX, bottomLeftY, roomWidth, roomHeight);
        //int rc = world.getRoomCounter();
        world.getRoomMap().put(world.getRoomCounter(), room);
        world.incRoomCounter();
        room.setFloor(world, room);
    }

    private void setFloor(World world, Room room) {
        for (int i = room.startX + 1; i < (room.startX + room.width); i++) {
            for (int j = room.startY + 1; j < (room.startY + room.height); j++) {
                world.getTiles()[i][j] = Tileset.fG[i][j];
            }
        }
    }

    //Really useful method: given an x,y coordinate, returns the room number that that coordinate is in
    //if it's in no particular room, return -1;
    public static int findRoomNumber(World world, int x, int y) {
        for (int i = 0; i < world.getRoomCounter(); i++) {
            Room tempRoom = world.getRoomMap().get(i);

            int stX = tempRoom.startX;
            int stY = tempRoom.startY;
            if (x > stX && (x) < stX + tempRoom.width && y > stY && y < stY + tempRoom.height) {
                return i;
            }
        }
        return -1;
    }

    //first value of return array: 1 if true (you are building into another room), or 0 if false
    //second value of return array: the room number that you are building; if building into no room, this value is -1;
    public static int[] checkMidWallCase(World world, int x, int y, String direction) {
        int[] returnArray = new int[2];
        int roomNum;
        if (direction.equals("x")) {
            if ((x - 1) >= 0 && (x + 1) < world.getWidth()) {
                TETile temp11 = world.getTiles()[x][y];
                TETile temp12 = world.getTiles()[x + 1][y];
                TETile temp13 = world.getTiles()[x - 1][y];
                if (temp11 == world.getWall() && temp12 == Tileset.fG[x + 1][y] && temp13 != world.getNothing()) {
                    roomNum = Room.findRoomNumber(world, x + 1, y);
                    returnArray[0] = 1;
                    returnArray[1] = roomNum;
                    return returnArray;
                }
            }
            if ((x + 1) < world.getWidth() && (x - 1) >= 0) {
                TETile temp21 = world.getTiles()[x][y];
                TETile temp22 = world.getTiles()[x - 1][y];
                TETile temp23 = world.getTiles()[x + 1][y];
                if (temp21 == world.getWall() && temp22 == Tileset.fG[x - 1][y] && temp23 != world.getNothing()) {
                    roomNum = Room.findRoomNumber(world,x - 1, y);
                    returnArray[0] = 1;
                    returnArray[1] = roomNum;
                    return returnArray;
                }
            }
        } else if (direction.equals("y")) {
            if ((y - 1) >= 0 && (y + 1) < world.getHeight()) {
                TETile temp31 = world.getTiles()[x][y];
                TETile temp32 = world.getTiles()[x][y + 1];
                TETile temp33 = world.getTiles()[x][y - 1];
                if (temp31 == world.getWall() && temp32 == Tileset.fG[x][y + 1] && temp33 != world.getNothing()) {
                    roomNum = Room.findRoomNumber(world, x, y + 1);
                    returnArray[0] = 1;
                    returnArray[1] = roomNum;
                    return returnArray;
                }
            }
            if ((y + 1) < world.getHeight() && (y - 1) >= 0) {
                TETile temp41 = world.getTiles()[x][y];
                TETile temp42 = world.getTiles()[x][y - 1];
                TETile temp43 = world.getTiles()[x][y + 1];
                if (temp41 == world.getWall() && temp42 == Tileset.fG[x][y - 1] && temp43 != world.getNothing()) {
                    roomNum = Room.findRoomNumber(world, x, y - 1);
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

}
