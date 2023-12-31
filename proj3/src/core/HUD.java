package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;

public class HUD {

    TETile floor = World.FLOOR;
    TETile[][] floors = Tileset.fG;
    TETile wall = World.WALL;
    TETile nothing = World.NOTHING;
    //TETile avatar = World.avatar;

    //constructor
    public HUD() {
    }

    public void renderHUD(World world) {
        //display the tile the cursor is hovering over in top left corner
        String pos = "";
        TETile[][] tiles = world.getTiles();
        int hudVal = hudVal(world, tiles);
        if (hudVal == 0) {
            pos = "Nothing";
        } else if (hudVal == 1) {
            pos = "Wall";
        } else if (hudVal == 2) {
            pos = "You";
        } else if (hudVal == 3) {
            pos = "Floor";
        }

        StdDraw.setPenColor(255, 255, 255);
        Font bigFont = new Font("Monospaced", Font.BOLD, 15);
        StdDraw.setFont(bigFont);
        StdDraw.text(2, 32, "Tile:");
        StdDraw.text(5.5, 32, pos);
        //display the avatar name in top right corner
        String aName = world.getAvatarName();
        if (aName == null) {
            aName = "?";
        }
        int nameLength = aName.length();
        StdDraw.text(57 - (nameLength * 0.4), 32, "Player: " + aName);
        StdDraw.show();
    }

    public int hudVal(World world, TETile[][] tiles) {
        double dX = StdDraw.mouseX();
        //int x = (int) Math.round(dX);
        int x = ((Double) dX).intValue();
        double dY = StdDraw.mouseY();
        //int y = (int) Math.round(dY);
        int y = ((Double) dY).intValue();
        if (y >= 30 || x >= 60 || y < 0 || x < 0) {
            return -1;
        } else if (tiles[x][y] == nothing) {
            return 0;
        } else if (tiles[x][y] == wall) {
            return 1;
        } else if (tiles[x][y] == world.getAvatar()) {
            return 2;
        } else if (tiles[x][y] == floors[x][y]) {
            return 3;
        } else {
            return 99;
        }
        //if this errors then there is some tile you aren't checking, or an error above
    }

}
