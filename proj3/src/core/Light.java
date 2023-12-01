package core;

import tileengine.TETile;
import tileengine.Tileset;
public class Light {
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
                if (i >= 0 && i < 60 && j >= 0 && j < 30) {
                    lightGrid[i][j] = ogGrid[i][j];
                }
            }
        }
        return lightGrid;
    }
}
