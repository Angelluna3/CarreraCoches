package ch.allred.racer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Track extends JPanel implements Runnable {

  private static final int DELAY = 10;
  private final List<Car> cars;
  private final List<MovingObject> movingObjects;
  private final List<Wall> walls;
  private final List<TrackPaint> trackPaints;
  private FinishLine finishLine;
  
  private Racer parent;

  public Track(Racer parent) {
    this.parent = parent;
    cars = new ArrayList<>();
    movingObjects = new ArrayList<>();
    walls = new ArrayList<>();
    trackPaints = new ArrayList<>();
    initTrack();
}

 private void initTrack() {
    addKeyListener(new TAdapter());
    setBackground(Color.GRAY);
    setFocusable(true);
    walls.addAll(TrackData.createWalls());
    
    // Cargar imagen en la pared central
    for (Wall wall : walls) {
        if (wall.getX() == 200 && wall.getY() == 200 && wall.getBounds().width == 700 && wall.getBounds().height == 300) {
            wall.loadCentreWallImage();
        }
    }
    
    trackPaints.addAll(TrackData.createTrackPaints());
    cars.addAll(TrackData.createCars());
    System.out.println("Cantidad de carros: " + cars.size());
    movingObjects.addAll(cars);
    movingObjects.addAll(TrackData.createBoxes());
    finishLine = new FinishLine(240, 30, 120, 20);
}
  private void checkFinishLine() {

    for (Car car : cars) {

        if (car.getBounds().intersects(finishLine.getBounds())) {

            if (!car.hasCrossedFinish()) {

                car.addPoint();
                car.setCrossedFinish(true);
            }
        }
        else {
            car.setCrossedFinish(false);
        }
    }
}

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    walls.forEach(wall -> wall.draw(g2d, this));
    trackPaints.forEach(paint -> paint.draw(g2d, this));
    movingObjects.forEach(obj -> obj.draw(g2d, this));
    drawStats(g2d);
    finishLine.draw(g2d, this);
    Toolkit.getDefaultToolkit().sync();
  }

  private void drawStats(Graphics2D g2d) {

    int y = 20;

    for (int i = 0; i < cars.size(); i++) {

        Car car = cars.get(i);

        g2d.drawString(
            "Jugador " + (i + 1)
            + " Puntos: " + car.getScore()
            + " Velocidad: " + (int) car.getIndicatedSpeed(),
            20,
            y
        );

        y += 20;
    }
}

  private void updateObjects(long timeDiff) {
    movingObjects.forEach(obj -> obj.update((float) timeDiff / 1000));
  }

  private class TAdapter extends KeyAdapter {

    @Override
    public void keyReleased(KeyEvent e) {
      cars.forEach(car -> car.keyReleased(e));
    }

    @Override
    public void keyPressed(KeyEvent e) {
      cars.forEach(car -> car.keyPressed(e));
    }
  }

  @Override
public void addNotify() {

    super.addNotify();

    for (Car car : cars) {
        new CarThread(car).start();
    }

    final Thread animatorThread = new Thread(this);

    animatorThread.start();
}

@Override
public void run() {

    long lastTime = System.currentTimeMillis();

    while (true) {

        final long now = System.currentTimeMillis();
        final long timeDiff = now - lastTime;

        lastTime = now;

        CollisionManager.checkAndApplyCollisions(movingObjects, walls);

        checkFinishLine();

        repaint();

        long sleep = DELAY - timeDiff;

        if (sleep < 0) {
            sleep = 2;
        }

        try {

            Thread.sleep(sleep);

        } catch (InterruptedException e) {

            String msg = String.format(
                "Thread interrupted: %s",
                e.getMessage()
            );

            JOptionPane.showMessageDialog(
                this,
                msg,
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
public void resetGame() {
    // Detener hilos existentes si es necesario
    cars.clear();
    movingObjects.clear();
    
    // Volver a crear los coches
    cars.addAll(TrackData.createCars());
    movingObjects.addAll(cars);
    movingObjects.addAll(TrackData.createBoxes());
    
    // Reiniciar hilos para los coches
    for (Car car : cars) {
        new CarThread(car).start();
    }
}
}
