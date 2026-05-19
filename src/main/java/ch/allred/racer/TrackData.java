
package ch.allred.racer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrackData {

  private final static int BOUNDING_WALL_THICKNESS = 30;
  private final static int CENTRE_WALL_THICKNESS = 300;
  private final static int CENTRE_WALL_LENGTH = 700;
  private final static int CENTRE_WALL_X_POSITION = 200;
  private final static int CENTRE_WALL_Y_POSITION = 200;

  private static final int FIRST_CAR_X = 200;
  private static final int FIRST_CAR_Y = 50;
  private static final int CAR_SPACING = 50;

  private static final int IBOX_X = 300;
  private static final int IBOX_Y = 520;

  // Color del coche del jugador 1 (0=rojo, 1=azul, 2=amarillo)
  private static int playerCarColorIndex = 0;

  public static int getCarX(final int carIndex) {
    return FIRST_CAR_X;
  }

  public static int getCarY(final int carIndex) {
    return FIRST_CAR_Y + CAR_SPACING * carIndex;
  }

  public static void setPlayerCarColor(int index) {
    playerCarColorIndex = index;
    System.out.println("DEBUG: Color del coche del jugador 1 cambiado a: " + index);
  }

  public static List<Wall> createWalls() {
    Wall northWall = new Wall(0, 0, Racer.TRACK_WIDTH, BOUNDING_WALL_THICKNESS);
    Wall southWall = new Wall(0, Racer.TRACK_HEIGHT - BOUNDING_WALL_THICKNESS, Racer.TRACK_WIDTH,
        BOUNDING_WALL_THICKNESS);
    Wall westWall = new Wall(0, 0, BOUNDING_WALL_THICKNESS, Racer.TRACK_HEIGHT);
    Wall eastWall = new Wall(Racer.TRACK_WIDTH - BOUNDING_WALL_THICKNESS, 0,
        BOUNDING_WALL_THICKNESS,
        Racer.TRACK_HEIGHT);
    Wall centreWall = new Wall(CENTRE_WALL_X_POSITION, CENTRE_WALL_Y_POSITION, CENTRE_WALL_LENGTH,
        CENTRE_WALL_THICKNESS);
    List<Wall> walls = new ArrayList<>();
    Collections.addAll(walls, northWall, southWall, westWall, eastWall, centreWall);
    return walls;
  }

  public static List<Car> createCars() {
    List<Car> cars = new ArrayList<>();
    
    // Los 3 colores disponibles: 0=Rojo, 1=Azul, 2=Amarillo
    int[] coloresDisponibles = {0, 1, 2};
    
    // JUGADOR 1: el color que seleccionó el usuario
    Car playerCar = Car.fromIndex(playerCarColorIndex);
    playerCar.setAsPlayerOne(true);
    cars.add(playerCar);
    
    // Asignar los colores restantes a los otros dos jugadores
    int otrosIndex = 0;
    for (int i = 0; i < coloresDisponibles.length; i++) {
      if (coloresDisponibles[i] != playerCarColorIndex) {
        Car otherCar = Car.fromIndex(coloresDisponibles[i]);
        otherCar.setAsPlayerOne(false);
        cars.add(otherCar);
        otrosIndex++;
        if (otrosIndex >= 2) break;
      }
    }
    
    System.out.println("DEBUG: Jugador1=" + playerCarColorIndex + 
                       ", Jugador2=" + getCarColorName(cars.get(1).getCarColorIndex()) +
                       ", Jugador3=" + getCarColorName(cars.get(2).getCarColorIndex()));
    return cars;
  }
  
  private static String getCarColorName(int index) {
    switch(index) {
      case 0: return "ROJO";
      case 1: return "AZUL";
      case 2: return "AMARILLO";
      default: return "DESCONOCIDO";
    }
  }

  public static List<Box> createBoxes() {
    List<Box> boxes = new ArrayList<>();
    boxes.add(new Box(300, 520));
    boxes.add(new Box(330, 520));
    boxes.add(new Box(360, 520));
    boxes.add(new Box(500, 300));
    boxes.add(new Box(530, 300));
    boxes.add(new Box(560, 300));
    boxes.add(new Box(700, 150));
    boxes.add(new Box(730, 150));
    boxes.add(new Box(850, 500));
    boxes.add(new Box(850, 530));
    return boxes;
  }
  
  public static List<TrackPaint> createTrackPaints() {
    List<TrackPaint> paints = new ArrayList<>();
    paints.add(new TrackPaint(250, TrackData.BOUNDING_WALL_THICKNESS, 2,
        TrackData.CENTRE_WALL_Y_POSITION - TrackData.BOUNDING_WALL_THICKNESS));
    return paints;
  }
  public static String getControlsMessage() {

    switch (playerCarColorIndex) {

        case 0:
            return "CARRO ROJO\n\n"
                 + "Flecha Arriba = Acelerar\n"
                 + "Flecha Abajo = Frenar\n"
                 + "Flecha Izquierda = Izquierda\n"
                 + "Flecha Derecha = Derecha";

        case 1:
            return "CARRO AZUL\n\n"
                 + "W = Acelerar\n"
                 + "S = Frenar\n"
                 + "A = Girar izquierda\n"
                 + "D = Girar derecha";

        case 2:
            return "CARRO AMARILLO\n\n"
                 + "I = Acelerar\n"
                 + "K = Frenar\n"
                 + "J = Izquierda\n"
                 + "L = Derecha";

        default:
            return "Controles no disponibles";
    }
}
}