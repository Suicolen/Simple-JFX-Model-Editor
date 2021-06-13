package tutorial.util;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public final class ColorUtils {

    public static Color rs2HSLToColor(short hsl, int alpha) {
        int transparency = alpha;
        if (transparency <= 0) {
            transparency = 255;
        }

        int hue = hsl >> 10 & 0x3f;
        int sat = hsl >> 7 & 0x07;
        int bri = hsl & 0x7f;
        java.awt.Color awtCol = java.awt.Color.getHSBColor((float) hue / 63, (float) sat / 7, (float) bri / 127);
        double r = awtCol.getRed() / 255.0;
        double g = awtCol.getGreen() / 255.0;
        double b = awtCol.getBlue() / 255.0;
        return Color.color(r, g, b, transparency / 255.0);
    }

    public static Color rs2HSLToColor(int hsl, int alpha) {
        //not sure if this is 100% correct, but i use it to build the table, so it doesn't matter
        int transparency = alpha;
        if (transparency <= 0) {
            transparency = 255;
        }

        int hue = hsl >> 10 & 0x3f;
        int sat = hsl >> 7 & 0x07;
        int bri = hsl & 0x7f;
        java.awt.Color awtCol = java.awt.Color.getHSBColor((float) hue / 63, (float) sat / 7, (float) bri / 127);
        double r = awtCol.getRed() / 255.0;
        double g = awtCol.getGreen() / 255.0;
        double b = awtCol.getBlue() / 255.0;
        return Color.color(r, g, b, transparency / 255.0);
    }

    private static final Map<Color, Integer> rgbToHSLTable = new HashMap<>();

    public static Map<Color, Integer> getRGBToHSLTable() {
        return rgbToHSLTable;
    }

    static {
        buildRGBToHSLTable();
    }

    public static void buildRGBToHSLTable() {
        for (int hsl = 0; hsl < 65536; hsl++) {
            Color color = ColorUtils.rs2HSLToColor(hsl, 0);
            rgbToHSLTable.put(color, hsl);
        }
    }


    public static int getBlue(int rgb) {
        return rgb & 0xFF;
    }

    public static int getGreen(int rgb) {
        return (rgb >> 8) & 0xFF;
    }

    public static int getRed(int rgb) {
        return (rgb >> 16) & 0xFF;
    }
}
