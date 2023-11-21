package tileengine;

import java.awt.*;
import java.awt.geom.Point2D;

import javax.swing.JComponent;
import javax.swing.JFrame;

import static tileengine.Tileset.fG;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    //floorGradient 2D array
    public static TETile[][] fG;

    int r = 100;
    int g = 150;
    int b = 200;

    public static void colorGradient(TETile[][] fG) {
        int r = 50;
        int g = 40;
        int b = 30;
        int rMult = 1;
        int gMult = -1;
        int bMult = -1;
        for (int i = 0; i < 60; i++) {
            if (r == 255 || r == 0) {
                rMult = -1 * rMult;
            }
            if (g == 255 || g == 0) {
                gMult = -1 * gMult;
            }
            if (b == 210 || b == 20) {
                bMult = -1 * bMult;
            }
            r += rMult;
            //g += gMult;
            b += bMult;
            for (int j = 0; j < 30; j++) {
                if (r == 210 || r == 20) {
                    rMult = -1 * rMult;
                }
                if (g == 210 || g == 20) {
                    gMult = -1 * gMult;
                }
                if (b == 210 || b == 20) {
                    bMult = -1 * bMult;
                }
                r += rMult;
                g += gMult;
                b += bMult;
                TETile floorTemp = new TETile('+', Color.lightGray, new Color(r, g, b),
                        "custom floor");
                fG[i][j] = floorTemp;
            }
        }
    }

//    public static void colorGradientFloor2() {
//        Graphics2D g2d = (Graphics2D) g;
//
//        Color startColor = Color.red;
//        Color endColor = Color.blue;
//
//        int startX = 0, startY = 0, endX = 60, endY = 30;
//
//        GradientPaint gradient = new GradientPaint(startX, startY, startColor, endX, endY, endColor);
//        g2d.setPaint(gradient);
//        for (int i = 0; i < 60; i++) {
//            for (int j = 0; j < 30; j++) {
//                TETile floorTemp = new TETile('+', Color.lightGray, g2d.getColor(),
//                        "custom floor");
//            }
//        }
//    }
    public class BasicDraw {
        BasicDraw() {
            JFrame frame = new JFrame();

            frame.add(new MyComponent());

            frame.setSize(300, 300);
            frame.setVisible(true);
        }

    }
    public static class MyComponent extends JComponent {
        public void paint(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;

            Color startColor = new Color(121, 5, 121);
            Color endColor = new Color(74, 127, 185);

            int startX = 0, startY = 0, endX = 60, endY = 30;

            GradientPaint gradient = new GradientPaint(startX, startY, startColor, endX, endY, endColor);
            g2d.setPaint(gradient);
            Paint p = g2d.getPaint();

            for (int i = 0; i < 60; i++) {
                for (int j = 0; j < 30; j++) {
                    Color colorAtSpecificPoint = getColorAt(gradient, i, j);
                    TETile floorTemp = new TETile('+', Color.lightGray, colorAtSpecificPoint,
                            "custom floor");
                    fG[i][j] = floorTemp;
                }
            }
        }

        private Color getColorAt(GradientPaint gradientPaint, float x, float y) {
            // Calculate the fraction of the distance between point1 and point2
            float fraction = calculateFraction(gradientPaint, x, y);

            // Interpolate the color
            Color color1 = gradientPaint.getColor1();
            Color color2 = gradientPaint.getColor2();
            float[] components1 = color1.getComponents(null);
            float[] components2 = color2.getComponents(null);

            float[] interpolatedComponents = new float[components1.length];
            for (int i = 0; i < interpolatedComponents.length; i++) {
                interpolatedComponents[i] = (1 - fraction) * components1[i] + fraction * components2[i];
            }

            return new Color(interpolatedComponents[0], interpolatedComponents[1], interpolatedComponents[2], interpolatedComponents[3]);
        }

        private float calculateFraction(GradientPaint gradientPaint, float x, float y) {
            float x1 = (float) gradientPaint.getPoint1().getX();
            float y1 = (float) gradientPaint.getPoint1().getY();
            float x2 = (float) gradientPaint.getPoint2().getX();
            float y2 = (float) gradientPaint.getPoint2().getY();

            float length = (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
            float length1 = (float) Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1));

            return length1 / length;
        }
    }

    public static final TETile AVATAR = new TETile('@', Color.white, Color.black, "you");
    public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
            "wall");

    public static final TETile CUSTOM_WALL = new TETile('#', Color.lightGray, Color.lightGray,
            "custom wall");
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor");

    public static final TETile CUSTOM_FLOOR = new TETile('+', Color.lightGray, new Color(58, 7, 57, 255),
            "custom floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");

    public static final TETile CUSTOM_NOTHING = new TETile(' ', Color.black, Color.black, "custom nothing");

    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");
}


