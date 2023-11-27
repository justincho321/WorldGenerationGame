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
    static TETile floor;
    private static TETile wall = Tileset.CUSTOM_WALL;
    private static TETile nothing = Tileset.CUSTOM_NOTHING;
    private static TETile avatar = Tileset.AVATAR;

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
        if (ceiling < 0) {  //swap signs of ceiling if necessary
            ceiling = roomStartHeight + roomHeight;
            multiplier2 = 1;
            bottomLeftY = roomStartHeight;
        }
        if (oppWall < 0) { //swap signs of opposite wall if necessary
            oppWall = roomStartWidth + roomWidth;
            multiplier1 = 1;
            bottomLeftX = roomStartWidth;
        }
        for (int j = 0; j <= roomWidth; j++) { //makes the horizontal floor and ceiling
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
                    roomNum = Room.findRoomNumber(world, x - 1, y);
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

    public static boolean checkStructure(char direction, int coord) {
        return true;
    }

    public static void makeBorderRoomY(World world, char side, int coord) {
        TETile[][] tiles = world.getTiles();
        if (side == 'n') {
            for (int i = coord - 2; i < coord + 3; i++) {
                if (i >= 0 && i < 60) {
                    tiles[i][29] = wall;
                    if (i == coord - 2 || i == coord + 2) {
                        if (tiles[i][28] == nothing) {
                            tiles[i][28] = wall;
                        }
                        if (tiles[i][27] == nothing) {
                            tiles[i][27] = wall;
                        }
                        if (tiles[i][26] == nothing) {
                            tiles[i][26] = wall;
                        }
                    } else if ((i > 0 && i < 59) && (i == coord - 1 || i == coord + 1)) {
                        tiles[i][28] = Tileset.fG[i][28];
                        tiles[i][27] = Tileset.fG[i][27];
                    }
                }
            }
            Room room = new Room(world.getRoomCounter(), coord, 27, 3, 2);
            world.getRoomMap().put(world.getRoomCounter(), room);
            world.incRoomCounter();
        } else if (side == 's') {
            for (int i = coord - 2; i < coord + 3; i++) {
                if (i >= 0 && i < 60) {
                    tiles[i][0] = wall;
                    if (i == coord - 2 || i == coord + 2) {
                        if (tiles[i][0] == nothing) {
                            tiles[i][0] = wall;
                        }
                        if (tiles[i][1] == nothing) {
                            tiles[i][1] = wall;
                        }
                        if (tiles[i][2] == nothing) {
                            tiles[i][2] = wall;
                        }
                    } else if ((i > 0 && i < 59) && (i == coord - 1 || i == coord + 1)) {
                        tiles[i][1] = Tileset.fG[i][0];
                        tiles[i][2] = Tileset.fG[i][1];
                    }
                }
            }
            Room room = new Room(world.getRoomCounter(), coord, 0, 3, 2);
            world.getRoomMap().put(world.getRoomCounter(), room);
            world.incRoomCounter();
        }
    }

    public static void makeBorderRoomX(World world, char side, int coord) {
        TETile[][] tiles = world.getTiles();
        if (side == 'e') {
            for (int i = coord - 2; i < coord + 3; i++) {
                if (i >= 0 && i < 30) {
                    tiles[59][i] = wall;
                    if (i == coord - 2 || i == coord + 2) { //the far borders--put walls
                        if (tiles[58][i] == nothing) {
                            tiles[58][i] = wall;
                        }
                        if (tiles[57][i] == nothing) {
                            tiles[57][i] = wall;
                        }
                        if (tiles[56][i] == nothing) {
                            tiles[56][i] = wall;
                        }
                    } else if ((i > 0 && i < 29) && (i == coord - 1 || i == coord + 1)) { //inside--put floor
                        tiles[58][i] = Tileset.fG[58][i];
                        tiles[57][i] = Tileset.fG[57][i];
                    }
                }
            }
            Room room = new Room(world.getRoomCounter(), 57, coord, 2, 3);
            world.getRoomMap().put(world.getRoomCounter(), room);
            world.incRoomCounter();
        } else if (side == 'w') {
            for (int i = coord - 2; i < coord + 3; i++) {
                if (i >= 0 && i < 30) {
                    tiles[0][i] = wall;
                    if (i == coord - 2 || i == coord + 2) { //the far borders--put walls
                        if (tiles[1][i] == nothing) {
                            tiles[1][i] = wall;
                        }
                        if (tiles[2][i] == nothing) {
                            tiles[2][i] = wall;
                        }
                        if (tiles[3][i] == nothing) {
                            tiles[3][i] = wall;
                        }
                    } else if ((i > 0 && i < 29) && (i == coord - 1 || i == coord + 1)) { //inside--put floor
                        tiles[1][i] = Tileset.fG[1][i];
                        tiles[2][i] = Tileset.fG[2][i];
                    }
                }
            }
            Room room = new Room(world.getRoomCounter(), 0, coord, 2, 3);
            world.getRoomMap().put(world.getRoomCounter(), room);
            world.incRoomCounter();
        }
    }
}
