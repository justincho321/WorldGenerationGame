package core;
import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;
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

    private int[] aPos = new int[2];

    private long seed;
    private int leftmost;

    private String avatarName = "";

    boolean lightsOff;

    //setting custom tiles
    static final TETile WALL = Tileset.CUSTOM_WALL;
    static final TETile FLOOR = Tileset.CUSTOM_FLOOR;
    static final TETile NOTHING = Tileset.CUSTOM_NOTHING;
    private TETile avatar = Tileset.AVATAR;

    //private Tileset ts;

    //WORLD CONSTRUCTOR
    public World(long seed) {
        //set argument seed to instance variable seed
        this.seed = seed;
        //set lights on/off (initially it's false)
        lightsOff = false;
        //floor gradient intialize
        Tileset ts = new Tileset();
        RANDOM = new Random(seed);
        //make tiles object and populate with nothing initially
        tiles = new TETile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = NOTHING;
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
        int counter = 0;
        leftmost = 0;
        while (wqu.count() != 1) {
            for (int i = 0; i < roomNumbers; i++) {
                if (roomMap.get(roomNumbers) != null
                        && roomMap.get(roomNumbers).getStartX() < roomMap.get(leftmost).getStartX()) {
                    leftmost = roomNumbers;
                }
                for (int j = 0; j < roomNumbers; j++) {
                    if (counter > 50) {
                        break;
                    }
                    if (!wqu.connected(i, j)) {
                        Hall.connectRoomToAnythingByHall(this, roomMap.get(j));
                        Hall.connectRoomToAnythingByHall(this, roomMap.get(i));
                    }
                    counter++;
                }
            }
        }
    }
    //ADD FIRST AVATAR
    public void addFirstAvatar() {
        //add avatar to leftmost room
        aPos[0] = roomMap.get(this.getLeftmost()).getStartX() + 1;
        aPos[1] = roomMap.get(this.getLeftmost()).getStartY() + 1;
        avatar = new TETile('@', Color.white, Tileset.MyComponent.getColorAt(Tileset.MyComponent.getGradientPaint(),
                aPos[0], aPos[1]), "you");
        tiles[aPos[0]][aPos[1]] = avatar;
    }
    //MISCELLANEOUS GETTERS AND SETTERS
    public int getLeftmost() {
        return leftmost;
    }
    public TETile[][] returnWorld() {
        return tiles;
    }

    public TETile[][] getTiles() {
        return tiles;
    }

    public long getSeed() {
        return seed;
    }

    public TETile[][] getLitSurrounding() {
        Light light = new Light();
        return light.litSurrounding(this, this.getAPos());
    }

    public boolean getLightsOff() {
        return lightsOff;
    }

    public void setLights(boolean lightsOff1) {
        this.lightsOff = lightsOff1;
    }

    public int[] getAPos() {
        return aPos;
    }

    public void setAPos(int[] aPos2) {
        this.aPos[0] = aPos2[0];
        this.aPos[1] = aPos2[1];
    }

    public TETile getAvatar() {
        return avatar;
    }

    public void setAvatar(TETile a) {
        avatar = a;
    }
    public TETile getFloor() {
        return FLOOR;
    }

    public TETile getWall() {
        return WALL;
    }

    public TETile getNothing() {
        return NOTHING;
    }

    public String getAvatarName() {
        return avatarName;
    }

    public void setAvatarName(String name) {
        avatarName = name;
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
