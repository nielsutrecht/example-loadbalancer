package com.nibado.example.loadbalance.lib;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

import javax.imageio.ImageIO;

public class RandomImage {
    private static final Color[] COLORS = new Color[] { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW };
    private final int width;
    private final int height;
    private final int step;
    private final Random random;

    public RandomImage(final int width, final int height, final int step) {
        this.width = width;
        this.height = height;
        this.step = step;
        random = new Random();
    }

    public synchronized BufferedImage generate() throws IOException {
        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = image.createGraphics();
        for (int y = 0; y < height; y += step) {
            for (int x = 0; x < width; x += step) {
                g.setColor(COLORS[random.nextInt(COLORS.length)]);
                g.fillRect(x, y, step, step);
            }
        }

        return image;
    }

    public synchronized String generateBase64(final Type imageType) throws IOException {
        final ByteArrayOutputStream outs = new ByteArrayOutputStream();
        ImageIO.write(generate(), imageType.type, outs);
        return Base64.getEncoder().encodeToString(outs.toByteArray());
    }

    public synchronized String generateDataUrl(final Type imageType) throws IOException {
        return "data:" + imageType.mimeType + ";base64," + generateBase64(imageType);
    }

    public static final void main(final String[] argv) throws IOException {
        final RandomImage ri = new RandomImage(400, 400, 4);
        System.out.println(ri.generateDataUrl(Type.PNG));
    }

    public enum Type {
        PNG("png", "image/png"), JPG("jpg", "image/jpeg"), GIF("gif", "image/gif");

        private String type;
        private String mimeType;

        private Type(final String type, final String mimeType) {
            this.type = type;
            this.mimeType = mimeType;
        }
    }
}
