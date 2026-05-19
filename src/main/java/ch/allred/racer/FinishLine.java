package ch.allred.racer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

public class FinishLine extends Sprite {

    public FinishLine(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void draw(Graphics2D g2d, ImageObserver imageObserver) {
        int tileSize = 20;

        for (int i = 0; i < width; i += tileSize) {

            if ((i / tileSize) % 2 == 0) {
                g2d.setColor(Color.WHITE);
            } else {
                g2d.setColor(Color.BLACK);
            }

            g2d.fillRect((int)x + i, (int)y, tileSize, height);
        }
    }
}
