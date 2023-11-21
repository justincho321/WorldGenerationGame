package core;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import tileengine.TETile;

public class Hall {

    //return whether two rooms can be connected by a horizontal OR vertical hallway
    public static int[] boolMakeHallwaysX(World world, Room room1, Room room2) {
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
                    if (smallerRoom.startY + 1 < world.getHeight()) {
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
                    if (biggerRoom.startY + 1 < world.getHeight()) {
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
                    if (smallerRoom.startY + 1 < world.getHeight()) {
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

    public static int[] boolMakeHallwaysY(World world, Room room1, Room room2) {
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
                    if (smallerRoom.startX + 1 < world.getWidth()) {
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
                    if (biggerRoom.startX + 1 < world.getWidth()) {
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
                    if (smallerRoom.startX + 1 < world.getWidth()) {
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

    public static boolean makeHallwaysX(World world, Room room1, Room room2) {

        int[] roomInfo = Hall.boolMakeHallwaysX(world, room1, room2);
        TETile[][] tiles = world.getTiles();
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
            int hallStartY = world.getRANDOM().nextInt(upperBound - lowerBound);
            hallStartY += lowerBound;

            if (checkAdjHallwayY(world, leftRoom, rightRoom, hallStartY)) {
                return false;
            }

            for (int i = leftRoom.startX + leftRoom.width; i <= rightRoom.startX; i++) {

                if (tiles[i][hallStartY - 1] != world.getFloor()) {
                    tiles[i][hallStartY - 1] = world.getWall();
                }
                tiles[i][hallStartY] = world.getFloor();
                if (tiles[i][hallStartY + 1] != world.getFloor()) {
                    tiles[i][hallStartY + 1] = world.getWall();
                }
            }
        }
        return true;
    }

    public static boolean makeHallwaysY(World world, Room room1, Room room2) {
        int[] roomInfo = Hall.boolMakeHallwaysY(world, room1, room2);
        TETile[][] tiles = world.getTiles();

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
            int hallStartX = world.getRANDOM().nextInt(upperBound - lowerBound);
            hallStartX += lowerBound;

            if (checkAdjHallwayX(world, lowerRoom, upperRoom, hallStartX)) {
                return false;
                //not make the hallway
                //shift the hallway up or down so it's not touching
            }

            for (int i = lowerRoom.startY + lowerRoom.height; i <= upperRoom.startY; i++) {

                if (tiles[hallStartX - 1][i] != world.getFloor()) {
                    tiles[hallStartX - 1][i] = world.getWall();
                }
                tiles[hallStartX][i] = world.getFloor();
                if (tiles[hallStartX + 1][i] != world.getFloor()) {
                    tiles[hallStartX + 1][i] = world.getWall();
                }
            }
        }
        return true;
    }

    public static boolean checkAdjHallwayY(World world, Room leftRoom, Room rightRoom, int hallStartY) {
        int height = world.getHeight();
        TETile wall = world.getWall();
        TETile floor = world.getFloor();
        TETile[][] tiles = world.getTiles();
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

    public static boolean checkAdjHallwayX(World world, Room leftRoom, Room rightRoom, int hallStartX) {
        int width = world.getWidth();
        TETile wall = world.getWall();
        TETile floor = world.getFloor();
        TETile[][] tiles = world.getTiles();
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

    public static void connectRoomToAnythingByHall(World world, Room room) {

        int finalRoomNum = 0;

        //TOP RIGHT QUADRANT
        if (room.startY > 15 && room.startX > 30) {
            connectRoomTopRightHelper(world, room);

        }
        //BOTTOM RIGHT QUADRANT
        if (room.startY < 16 && room.startX > 30) {
            connectRoomBottomRightHelper(world, room);

        }

        //TOP LEFT QUADRANT
        if (room.startY > 15 && room.startX < 31) {
            connectRoomTopLeftHelper(world, room);
        }

        //if bottom left quadrant
        if (room.startY < 16 && room.startX < 31) {
            connectRoomBottomLeftHelper(world, room);
        }
    }

    private static void connectRoomTopRightHelper(World world, Room room) {
        TETile wall = world.getWall();
        TETile floor = world.getFloor();
        TETile[][] tiles = world.getTiles();
        WeightedQuickUnionUF wqu = world.getWQU();
        int finalRoomNum = 0;
        //build left
        for (int i = room.startX; i >= 0; i--) {
            if (tiles[i][room.startY + 2] != floor) {
                tiles[i][room.startY + 2] = wall;
            }
            if (tiles[i][room.startY + 1] == floor) {
                //if we reached a floor of another hallway or room, check if we're in a room.
                // if not, traverse the floor until we reach a room then connect that room with our room.
                if (Room.findRoomNumber(world, i, room.startY + 1) == -1) {
                    if (tiles[i][room.startY] == floor) {
                        int j = 1;
                        if (tiles[i + 1][room.startY] == floor) {
                            wqu.union(finalRoomNum, room.num);
                            return;
                        }
                        //build down until you reach a room
                        while (Room.findRoomNumber(world, i, room.startY - j) == -1) {
                            j++;
                            if (room.startY - j < 0) {
                                return;
                            }
                        }
                        finalRoomNum = Room.findRoomNumber(world, i, room.startY - j);
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
                //if we reached a floor of another hallway or room, check if we're in a room...see above
                if (Room.findRoomNumber(world, room.startX + 1, i) == -1) {
                    if (tiles[room.startX][i] == floor) {
                        int j = 1;
                        if (tiles[room.startX][i + 1] == floor) {
                            wqu.union(finalRoomNum, room.num);
                            return;
                        }
                        //build left until you reach a room
                        while (Room.findRoomNumber(world, room.startX - j, i) == -1) {
                            j++;
                            if (room.startX - j < 0) {
                                return;
                            }
                        }
                        finalRoomNum = Room.findRoomNumber(world, room.startX - j, i);
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

    private static void connectRoomBottomRightHelper(World world, Room room) {
        TETile wall = world.getWall();
        TETile floor = world.getFloor();
        TETile[][] tiles = world.getTiles();
        WeightedQuickUnionUF wqu = world.getWQU();
        int finalRoomNum = 0;
        for (int i = room.startX; i >= 0; i--) { //build left
            if (tiles[i][room.startY + 2] != floor) {
                tiles[i][room.startY + 2] = wall;
            }
            if (tiles[i][room.startY + 1] == floor) {
                //if we reached a floor of another hallway or room, check if we're in a room. if not, traverse
                //the floor until we reach a room then connect that room with our room.
                if (Room.findRoomNumber(world, i, room.startY + 1) == -1) {
                    if (tiles[i][room.startY] == floor) {
                        int j = 1;

                        if (tiles[i + 1][room.startY] == floor) {
                            wqu.union(finalRoomNum, room.num);
                            return;
                        }
                        //build up until you reach a room
                        while (Room.findRoomNumber(world, i, room.startY + j) == -1) {
                            j++;
                            if (room.startY + j > 30) {
                                return;
                            }
                        }
                        finalRoomNum = Room.findRoomNumber(world, i, room.startY + j);
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
        for (int i = room.startY + room.height; i < world.getHeight(); i++) {
            if (tiles[room.startX + 2][i] != floor) {
                tiles[room.startX + 2][i] = wall;
            }
            if (tiles[room.startX + 1][i] == floor) {
                //if we reached a floor of another hallway or room, check if we're in a room. if not...
                if (Room.findRoomNumber(world, room.startX + 1, i) == -1) {
                    if (tiles[room.startX][i] == floor) {
                        int j = 1;
                        if (tiles[room.startX][i + 1] == floor) {
                            wqu.union(finalRoomNum, room.num);
                            return;
                        }
                        //build left until you reach a room
                        while (Room.findRoomNumber(world, room.startX - j, i) == -1) {
                            j++;
                            if (room.startY + j > 30) {
                                return;
                            }
                        }
                        finalRoomNum = Room.findRoomNumber(world, room.startX - j, i);
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

    private static void connectRoomTopLeftHelper(World world, Room room) {
        TETile wall = world.getWall();
        TETile floor = world.getFloor();
        TETile[][] tiles = world.getTiles();
        WeightedQuickUnionUF wqu = world.getWQU();
        int finalRoomNum = 0;
        //build right
        for (int i = room.startX + room.width; i < world.getWidth(); i++) {
            if (tiles[i][room.startY + 2] != floor) {
                tiles[i][room.startY + 2] = wall;
            }
            if (tiles[i][room.startY + 1] == floor) {
                //if we reached a floor of another hallway or room, check if we're in a room.
                // if not, traverse the floor until we reach a room then connect that room with our room.
                if (Room.findRoomNumber(world, i, room.startY + 1) == -1) {
                    if (tiles[i][room.startY] == floor) {
                        int j = 1;

                        if (tiles[i + 1][room.startY] == floor) {
                            wqu.union(finalRoomNum, room.num);
                            return;
                        }
                        while (Room.findRoomNumber(world, i, room.startY - j) == -1) { //build down until room
                            j++;
                            if (room.startY - j < 0) {
                                return;
                            }
                        }
                        finalRoomNum = Room.findRoomNumber(world, i, room.startY - j);
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
        for (int i = room.startY; i >= 0; i--) { //build down
            if (tiles[room.startX + 2][i] != floor) {
                tiles[room.startX + 2][i] = wall;
            }
            if (tiles[room.startX + 1][i] == floor) {
                //if we reached a floor of another hallway or room, check if we're in a room.
                // if not, traverse the floor until we reach a room then connect that room with our room.
                if (Room.findRoomNumber(world, room.startX + 1, i) == -1) {
                    if (tiles[room.startX][i] == floor) {
                        int j = 1;

                        if (tiles[room.startX][i + 1] == floor) {
                            wqu.union(finalRoomNum, room.num);
                            return;
                        }
                        while (Room.findRoomNumber(world, room.startX + j, i) == -1) { //build right until room
                            j++;
                            if (room.startX + j > 60) {
                                return;
                            }
                        }
                        finalRoomNum = Room.findRoomNumber(world, room.startX + j, i);
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

    private static void connectRoomBottomLeftHelper(World world, Room room) {
        TETile wall = world.getWall();
        TETile floor = world.getFloor();
        TETile[][] tiles = world.getTiles();
        WeightedQuickUnionUF wqu = world.getWQU();
        int finalRoomNum = 0;
        //build right
        for (int i = room.startX + room.width; i < world.getWidth(); i++) {
            if (tiles[i][room.startY + 2] != floor) {
                tiles[i][room.startY + 2] = wall;
            }
            if (tiles[i][room.startY + 1] == floor) {
                //if we reached a floor of another hallway or room, check if we're in a room.
                // if not, traverse the floor until we reach a room then connect that room with our room.
                if (Room.findRoomNumber(world, i, room.startY + 1) == -1) {
                    if (tiles[i][room.startY + 1] == floor) {
                        int j = 1;
                        if (tiles[i + 1][room.startY + 1] == floor) {
                            wqu.union(finalRoomNum, room.num);
                            return;
                        }
                        while (Room.findRoomNumber(world, i, room.startY + j) == -1) { //build up until room
                            j++;
                            if (room.startY + j > 30) {
                                return;
                            }
                        }
                        finalRoomNum = Room.findRoomNumber(world, i, room.startY + j);
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
        for (int i = room.startY + room.height; i < world.getHeight(); i++) { //build up
            if (tiles[room.startX + 2][i] != floor) {
                tiles[room.startX + 2][i] = wall;
            }
            if (tiles[room.startX + 1][i] == floor) {
                //if we reached a floor of another hallway or room, check if we're in a room.
                //if not, traverse the floor until we reach a room then connect that room with our room.
                if (Room.findRoomNumber(world, room.startX + 1, i) == -1) {
                    if (tiles[room.startX][i] == floor) {
                        int j = 1;

                        if (tiles[room.startX][i + 1] == floor) {
                            wqu.union(finalRoomNum, room.num);
                            return;
                        }
                        //build right until you reach a room
                        while (Room.findRoomNumber(world, room.startX + j, i) == -1) {
                            j++;
                            if (room.startX + j > 60) {
                                return;
                            }
                        }
                        finalRoomNum = Room.findRoomNumber(world, room.startX + j, i);
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
}
