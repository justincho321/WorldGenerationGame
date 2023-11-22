package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;

public class Move {
    TETile floor = World.floor;
    TETile[][] floors = Tileset.fG;
    TETile wall = World.wall;
    TETile nothing = World.nothing;
    TETile avatar = World.avatar;

    public void move(TETile[][] tiles, int[] aPos) {
        int x = aPos[0];
        int y = aPos[1];
        char key = StdDraw.nextKeyTyped();
        if (key == 'w') {
            if (y < 30 && tiles[x][y + 1] != wall) {
                //update avatar position
                aPos[1] = y + 1;
                //change new position to avatar tile
                avatar = new TETile('@', Color.white, Tileset.MyComponent.getColorAt(Tileset.MyComponent.getGradientPaint(), aPos[0], aPos[1]), "you");
                tiles[x][y + 1] = avatar;
                //change original position back to floor
                tiles[x][y] = floors[x][y];

            }
        } else if (key == 'a') {
            if (x >= 0 && tiles[x - 1][y] != wall) {
                aPos[0] = x - 1;
                avatar = new TETile('@', Color.white, Tileset.MyComponent.getColorAt(Tileset.MyComponent.getGradientPaint(), aPos[0], aPos[1]), "you");
                tiles[x - 1][y] = avatar;
                tiles[x][y] = floors[x][y];

            }
        } else if (key == 's') {
            if (y >= 0 && tiles[x][y - 1] != wall) {
                aPos[1] = y - 1;
                avatar = new TETile('@', Color.white, Tileset.MyComponent.getColorAt(Tileset.MyComponent.getGradientPaint(), aPos[0], aPos[1]), "you");
                tiles[x][y - 1] = avatar;
                tiles[x][y] = floors[x][y];

            }

        } else if (key == 'd') {
            if (x < 60 && tiles[x + 1][y] != wall) {
                aPos[0] = x + 1;
                avatar = new TETile('@', Color.white, Tileset.MyComponent.getColorAt(Tileset.MyComponent.getGradientPaint(), aPos[0], aPos[1]), "you");
                tiles[x + 1][y] = avatar;
                tiles[x][y] = floors[x][y];

            }
        }

    }

}
