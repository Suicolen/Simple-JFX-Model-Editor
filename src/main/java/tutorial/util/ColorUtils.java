package tutorial.util;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public final class ColorUtils {

    public static Color getAverageColor(Image image) {

        int redBucket = 0;
        int greenBucket = 0;
        int blueBucket = 0;

        int pixelCount = (int) (image.getWidth() * image.getHeight());

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color c = image.getPixelReader().getColor(x, y);
                redBucket += c.getRed() * 255D;
                greenBucket += c.getGreen() * 255D;
                blueBucket += c.getBlue() * 255D;
                // does alpha matter?
            }
        }

        return Color.rgb(redBucket / pixelCount,
                greenBucket / pixelCount,
                blueBucket / pixelCount);
    }

    public static Image darken(Image image) {
        int w = (int) image.getWidth();
        int h = (int) image.getHeight();
        WritableImage outputImage = new WritableImage(w, h);
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = outputImage.getPixelWriter();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int argb = reader.getArgb(x, y);
                int a = 65;
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;
                argb = (a << 24) | (r << 16) | (g << 8) | b;
                writer.setArgb(x, y, argb);
            }
        }
        return outputImage;
    }

    public static Image brighten(Image image) {
        int w = (int) image.getWidth();
        int h = (int) image.getHeight();
        WritableImage outputImage = new WritableImage(w, h);
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = outputImage.getPixelWriter();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int argb = reader.getArgb(x, y);
                int a = 255;
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;

                argb = (a << 24) | (r << 16) + 100000 | (g << 8) + 100000 | b + 100000;
                writer.setArgb(x, y, argb);
            }
        }
        return outputImage;
    }

    public static int colorToRgb(Color color) {
        int r = (int) (color.getRed() * 255D);
        int g = (int) (color.getGreen() * 255D);
        int b = (int) (color.getBlue() * 255D);
        return (r << 16 | g << 8 | b);
    }

    public static int colorToHSL(Color color) {
        return convertToHSL((int) (color.getRed() * 255D), (int) (color.getGreen() * 255D), (int) (color
                .getBlue() * 255D));
    }

    public static int convertToHSL(int red, int green, int blue) {
        float[] HSB = java.awt.Color.RGBtoHSB(red, green, blue, null);
        float hue = (HSB[0]);
        float saturation = (HSB[1]);
        float brightness = (HSB[2]);
        int encode_hue = (int) (hue * 63f);            //to 6-bits
        int encode_saturation = (int) (saturation * 7f);        //to 3-bits
        int encode_brightness = (int) (brightness * 127f);    //to 7-bits
        return (encode_hue << 10) + (encode_saturation << 7) + (encode_brightness);
    }

    public static javafx.scene.paint.Color RS2HSL_to_RGB(int RS2HSB) {
        int decode_hue = (RS2HSB >> 10) & 0x3f;
        int decode_saturation = (RS2HSB >> 7) & 0x07;
        int decode_brightness = (RS2HSB & 0x7f);
        java.awt.Color awtColor = new java.awt.Color(java.awt.Color.HSBtoRGB((float) decode_hue / 63, (float) decode_saturation / 7, (float) decode_brightness / 127));
        int r = awtColor.getRed();
        int g = awtColor.getGreen();
        int b = awtColor.getBlue();
        int a = awtColor.getAlpha();
        double opacity = a / 255.0;
        return javafx.scene.paint.Color.rgb(r, g, b, opacity);
    }

    public static Color getColor(int rgb) {
        return Color.color(getRed(rgb) / 255.0, getGreen(rgb) / 255.0, getBlue(rgb) / 255.0);
    }

    public static int hsbToRGB(int hsb) {
        float h = hsb >> 10 & 0x3f;
        float s = hsb >> 7 & 0x07;
        float b = hsb & 0x7f;
        return java.awt.Color.HSBtoRGB(h / 63, s / 7, b / 127);
    }

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
