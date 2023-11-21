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

    //private Tileset ts;

    //WORLD CONSTRUCTOR
    public World(long seed) {
        //floor gradient intialize
        Tileset ts = new Tileset();
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
            //Room room = new Room(4, 5, 5, 5, 5);
            Room.makeRandomRoom(this, i);
        }

        //add hallways
        for (int j = 0; j < roomNumbers; j++) {
            for (int i = 0; i < roomNumbers; i++) {
                if (Hall.makeHallwaysY(this, roomMap.get(j), roomMap.get(i))) {
                    wqu.union(j, i);
                }

                if (Hall.makeHallwaysX(this, roomMap.get(j), roomMap.get(i))) {
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
                        Hall.connectRoomToAnythingByHall(this, roomMap.get(j));
                        Hall.connectRoomToAnythingByHall(this, roomMap.get(i));
                    }
                }
            }
        }
    }

    //MISCELLANEOUS GETTERS AND SETTERS
    public TETile[][] returnWorld() {
        return tiles;
    }

    public TETile[][] getTiles() {
        return tiles;
    }

    public TETile getFloor() {
        return floor;
    }

    public TETile getWall() {
        return wall;
    }

    public TETile getNothing() {
        return nothing;
    }

    public WeightedQuickUnionUF getWQU() {
        return wqu;
    }

    public int getRoomCounter() {
        return roomCounter;
    }

    public void incRoomCounter() {
        roomCounter += 1;
    }

    public HashMap<Integer, Room> getRoomMap() {
        return roomMap;
    }

    public Random getRANDOM() {
        return RANDOM;
    }

    //gets world width
    public int getWidth() {
        return width;
    }

    //gets world height
    public int getHeight() {
        return height;
    }


}
