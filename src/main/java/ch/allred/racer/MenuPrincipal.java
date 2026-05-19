package ch.allred.racer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.InputStream;

public class MenuPrincipal extends JPanel {
    private Racer parent;
    private int selectedOption = 0;
    private boolean cursorVisible = true;
    private Image backgroundImage;
    
    // Posiciones de los botones
    private final int BUTTON_X = 680;
    private final int[] BUTTON_Y = {250, 340, 430};
    private final int BUTTON_WIDTH = 250;
    private final int BUTTON_HEIGHT = 55;
    
    public MenuPrincipal(Racer parent) {
        this.parent = parent;
        setFocusable(true);
        setBackground(Color.BLACK);
        
        // Cargar la imagen de fondo
        loadBackgroundImage();
        
        // Timer para cursor parpadeante
        new javax.swing.Timer(400, e -> {
            cursorVisible = !cursorVisible;
            repaint();
        }).start();
        
        // Controles de teclado
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    selectedOption = Math.max(0, selectedOption - 1);
                    repaint();
                    Toolkit.getDefaultToolkit().beep();
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    selectedOption = Math.min(2, selectedOption + 1);
                    repaint();
                    Toolkit.getDefaultToolkit().beep();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (selectedOption == 0) parent.startGame();
                    else if (selectedOption == 1) parent.showCarSelector();
                    else if (selectedOption == 2) parent.showBackgroundSelector();
                }
            }
        });
        
        // Soporte para mouse
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                for (int i = 0; i < BUTTON_Y.length; i++) {
                    if (mouseX >= BUTTON_X && mouseX <= BUTTON_X + BUTTON_WIDTH &&
                        mouseY >= BUTTON_Y[i] && mouseY <= BUTTON_Y[i] + BUTTON_HEIGHT) {
                        selectedOption = i;
                        repaint();
                        break;
                    }
                }
            }
        });
        
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                for (int i = 0; i < BUTTON_Y.length; i++) {
                    if (mouseX >= BUTTON_X && mouseX <= BUTTON_X + BUTTON_WIDTH &&
                        mouseY >= BUTTON_Y[i] && mouseY <= BUTTON_Y[i] + BUTTON_HEIGHT) {
                        if (i == 0) parent.startGame();
                        else if (i == 1) parent.showCarSelector();
                        else if (i == 2) parent.showBackgroundSelector();
                        break;
                    }
                }
            }
        });
        
        setFocusable(true);
        requestFocus();
    }
    
    private void loadBackgroundImage() {
        try {
            InputStream imgStream = getClass().getResourceAsStream("/menu_background.png");
            if (imgStream != null) {
                BufferedImage original = ImageIO.read(imgStream);
                backgroundImage = original.getScaledInstance(1100, 728, Image.SCALE_SMOOTH);
                System.out.println("DEBUG: Imagen de fondo cargada");
            } else {
                System.out.println("DEBUG: No se encontró menu_background.png");
            }
        } catch (Exception e) {
            System.err.println("Error cargando imagen de fondo: " + e.getMessage());
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Dibujar imagen de fondo
        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, 1100, 728, this);
        } else {
            // Fallback: color sólido si no hay imagen
            g2d.setColor(new Color(20, 25, 35));
            g2d.fillRect(0, 0, 1100, 728);
        }
        
        // Efecto scanline pixelado (opcional, comenta si no lo quieres)
        g2d.setColor(new Color(0, 0, 0, 40));
        for (int i = 0; i < 728; i += 4) {
            g2d.fillRect(0, i, 1100, 1);
        }
        
        // Texto de los botones
        String[] buttonTexts = {"START RACE", "SELECT CAR", "CHANGE TRACK"};
        
        for (int i = 0; i < 3; i++) {
            int x = BUTTON_X;
            int y = BUTTON_Y[i];
            
            if (i == selectedOption) {
                // ===== BOTÓN SELECCIONADO =====
                // Fondo del botón
                g2d.setColor(new Color(255, 200, 50));
                g2d.fillRect(x, y, BUTTON_WIDTH, BUTTON_HEIGHT);
                
                // Borde del botón (efecto pixelado)
                g2d.setColor(new Color(255, 255, 200));
                g2d.drawRect(x, y, BUTTON_WIDTH, BUTTON_HEIGHT);
                g2d.drawRect(x + 1, y + 1, BUTTON_WIDTH - 2, BUTTON_HEIGHT - 2);
                
                // Sombra interior
                g2d.setColor(new Color(255, 220, 100, 100));
                g2d.fillRect(x + 2, y + 2, BUTTON_WIDTH - 4, BUTTON_HEIGHT - 4);
                
                // Texto del botón
                g2d.setFont(new Font("Monospaced", Font.BOLD, 20));
                g2d.setColor(Color.BLACK);
                int textX = x + (BUTTON_WIDTH - g2d.getFontMetrics().stringWidth(buttonTexts[i])) / 2;
                g2d.drawString(buttonTexts[i], textX, y + 36);
                
                // Flecha ▶ al lado del botón seleccionado
                if (cursorVisible) {
                    g2d.setFont(new Font("Monospaced", Font.BOLD, 32));
                    g2d.setColor(new Color(255, 220, 50));
                    g2d.drawString("▶", x - 40, y + 40);
                }
            } else {
                // ===== BOTÓN NO SELECCIONADO =====
                // Fondo del botón
                g2d.setColor(new Color(30, 30, 50));
                g2d.fillRect(x, y, BUTTON_WIDTH, BUTTON_HEIGHT);
                
                // Borde del botón
                g2d.setColor(new Color(100, 100, 120));
                g2d.drawRect(x, y, BUTTON_WIDTH, BUTTON_HEIGHT);
                
                // Texto del botón
                g2d.setFont(new Font("Monospaced", Font.BOLD, 20));
                g2d.setColor(new Color(180, 180, 200));
                int textX = x + (BUTTON_WIDTH - g2d.getFontMetrics().stringWidth(buttonTexts[i])) / 2;
                g2d.drawString(buttonTexts[i], textX, y + 36);
            }
        }
        
        // ===== INFORMACIÓN DEL COCHE (esquina inferior derecha) =====
        g2d.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        // Fondo semitransparente
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(850, 670, 230, 45);
        g2d.setColor(new Color(150, 150, 150));
        g2d.drawRect(850, 670, 230, 45);
        
        g2d.setColor(new Color(200, 200, 200));
        g2d.drawString("CURRENT CAR:", 860, 690);
        g2d.setColor(parent.getCurrentCarColor());
        g2d.drawString(parent.getCurrentCarName(), 860, 705);
        
        // ===== CONTROLES (esquina inferior izquierda) =====
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(20, 670, 280, 45);
        g2d.setColor(new Color(150, 150, 150));
        g2d.drawRect(20, 670, 280, 45);
        
        g2d.setColor(new Color(150, 150, 150));
        g2d.drawString("▲/▼ : NAVIGATE", 30, 690);
        g2d.drawString("ENTER : SELECT", 30, 705);
    }
}