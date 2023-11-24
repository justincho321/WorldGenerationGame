package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.ArrayList;

public class Light {
    public static TETile wall = Tileset.CUSTOM_WALL;
    public static TETile floor = Tileset.CUSTOM_FLOOR;
    public static TETile nothing = Tileset.CUSTOM_NOTHING;
    public static TETile avatar = Tileset.AVATAR;
    TETile[][] lightGrid = new TETile[60][30];
    public Light() {
        //set lightGrid to mask tiles
        for (int i = 0; i < 60; i++) {
            for (int j = 0; j < 30; j++) {
                lightGrid[i][j] = Tileset.MASK;
            }
        }
    }

    public TETile[][] litSurrounding(World world, int[] aPos) {
        int x = aPos[0];
        int y = aPos[1];
        TETile[][] ogGrid = world.getTiles();
        //set surrounding 5x5 square to
        for (int i = x - 2; i <= x + 2; i++) {
            for (int j = y - 2; j <= y + 2; j++) {
                if (i >= 0 && i < 60 && j >=0 && j < 30) {
                    lightGrid[i][j] = ogGrid[i][j];
                }
            }
        }
        return lightGrid;
    }
}
