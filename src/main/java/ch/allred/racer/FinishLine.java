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
        g2d.setColor(Color.WHITE);
        g2d.fill(getBounds());

        g2d.setColor(Color.BLACK);

        for (int i = 0; i < width; i += 20) {
            for (int j = 0; j < height; j += 20) {
                if ((i + j) % 40 == 0) {
                    g2d.fillRect((int)x + i, (int)y + j, 20, 20);
                }
            }
        }
    }
}
