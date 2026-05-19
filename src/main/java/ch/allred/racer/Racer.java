package ch.allred.racer;

import java.awt.*;
import javax.swing.*;

public class Racer extends JFrame {
    public final static int TRACK_WIDTH = 1100;
    public final static int TRACK_HEIGHT = 700;
    private final static int BOTTOM_MARGIN = 28;
    
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private Track track;
    private MenuPrincipal menu;
    private CarSelector carSelector;
    private BackgroundSelector bgSelector;
    
    private int currentCarIndex = 0;
    private String currentCarName = "FIRESTORM";
    private Color currentCarColor = new Color(220, 40, 40);
    private Color currentTrackColor = new Color(80, 85, 100);
    
    public Racer() {
        initUI();
    }
    
    private void initUI() {
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        
        menu = new MenuPrincipal(this);
        track = new Track(this);
        carSelector = new CarSelector(this);
        bgSelector = new BackgroundSelector(this);
        
        mainContainer.add(menu, "menu");
        mainContainer.add(track, "game");
        mainContainer.add(carSelector, "cars");
        mainContainer.add(bgSelector, "backgrounds");
        
        add(mainContainer);
        
        setTitle("Racer - Pixel Racing");
        setSize(TRACK_WIDTH, TRACK_HEIGHT + BOTTOM_MARGIN);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void startGame() {
        // Aplicar el color del coche seleccionado
        TrackData.setPlayerCarColor(currentCarIndex);
        cardLayout.show(mainContainer, "game");
        track.setBackground(currentTrackColor);
        track.requestFocus();
        track.resetGame();
    }
    
    public void showMenu() {
        cardLayout.show(mainContainer, "menu");
        // ELIMINA esta línea porque ya no existe:
        // menu.setBackgroundColor(currentTrackColor);
        menu.requestFocus();
    }
    
    public void showCarSelector() {
        cardLayout.show(mainContainer, "cars");
    }
    
    public void showBackgroundSelector() {
        cardLayout.show(mainContainer, "backgrounds");
    }
    
    public void setCarSelected(int index, String name, Color color) {
        this.currentCarIndex = index;
        this.currentCarName = name;
        this.currentCarColor = color;
    }
    
    public void setTrackBackground(Color color) {
        this.currentTrackColor = color;
        if (track != null) {
            track.setBackground(color);
        }
    }
    
    public Color getCurrentCarColor() {
        return currentCarColor;
    }
    
    public String getCurrentCarName() {
        return currentCarName;
    }
    
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Racer ex = new Racer();
            ex.setVisible(true);
        });
    }
}