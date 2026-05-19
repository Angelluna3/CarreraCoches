package ch.allred.racer;

import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.InputStream;
import java.awt.Color;

public class Wall extends PhysicalObject {
  
  private BufferedImage centreWallImage;

  public Wall(final int x, final int y, final int width, final int height) {
    super(x, y, width, height);
  }
  
  // Método especial solo para la pared central
  public void loadCentreWallImage() {
    try {
      InputStream imgStream = getClass().getResourceAsStream("/centre_wall.png");
      if (imgStream != null) {
        centreWallImage = ImageIO.read(imgStream);
        System.out.println("DEBUG: Imagen de pared central cargada");
      }
    } catch (Exception e) {
      System.err.println("Error cargando imagen: " + e.getMessage());
    }
  }

  @Override
  public void draw(final Graphics2D g2d, final ImageObserver imageObserver) {
    // Si es la pared central y tiene imagen, dibujar la imagen
    if (centreWallImage != null && isCentreWall()) {
      g2d.drawImage(centreWallImage, (int)x, (int)y, width, height, imageObserver);
    } else {
      g2d.fill(getBounds());
    }
  }
  
  private boolean isCentreWall() {
    // La pared central está en x=200, y=200 con ancho=700, alto=300
    return (int)x == 200 && (int)y == 200 && width == 700 && height == 300;
  }
}
