package ch.allred.racer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;

public class CarSelector extends JPanel {
    private Racer parent;
    private int selectedIndex = 0;
    
    private String[] carNames = {"RED", " BLUE", "YELLOW"};
    private String[] carImagePaths = {"/car_red.png", "/car_blue.png", "/car_yellow.png"};
    private Color[] carColors = {new Color(220, 40, 40), new Color(40, 100, 220), new Color(220, 180, 40)};
    private String[] carIcons = {"🔥", "💙", "⭐"};
    private Image[] carImages = new Image[3];
    
    public CarSelector(Racer parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(new Color(15, 15, 25));
        
        // Cargar las imágenes reales de los coches
        for (int i = 0; i < carImagePaths.length; i++) {
            try {
                InputStream imgStream = getClass().getResourceAsStream(carImagePaths[i]);
                if (imgStream != null) {
                    ImageIcon icon = new ImageIcon(ImageIO.read(imgStream));
                    // Redimensionar para el preview (manteniendo proporción)
                    Image scaledImage = icon.getImage().getScaledInstance(120, 80, Image.SCALE_SMOOTH);
                    carImages[i] = scaledImage;
                }
            } catch (IOException e) {
                System.err.println("Error cargando imagen: " + carImagePaths[i]);
                e.printStackTrace();
            }
        }
        
        // Título
        JLabel title = new JLabel("SELECT YOUR CAR", SwingConstants.CENTER);
        title.setFont(new Font("Monospaced", Font.BOLD, 42));
        title.setForeground(new Color(255, 200, 50));
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        add(title, BorderLayout.NORTH);
        
        // Panel de preview con la imagen del coche
        JPanel previewPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                // Fondo oscuro como el juego
                g2d.setColor(new Color(25, 25, 40));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Dibujar la imagen real del coche seleccionado
                if (carImages[selectedIndex] != null) {
                    int carX = getWidth()/2 - 60;
                    int carY = getHeight()/2 - 40;
                    g2d.drawImage(carImages[selectedIndex], carX, carY, 120, 80, this);
                } else {
                    // Fallback: dibujar un rectángulo si no hay imagen
                    g2d.setColor(carColors[selectedIndex]);
                    g2d.fillRect(getWidth()/2 - 50, getHeight()/2 - 30, 100, 60);
                }
                
                // Efecto de brillo alrededor
                g2d.setColor(new Color(255, 255, 100, 100));
                g2d.drawRect(getWidth()/2 - 65, getHeight()/2 - 45, 130, 90);
                
                // Nombre del coche
                g2d.setFont(new Font("Monospaced", Font.BOLD, 28));
                g2d.setColor(carColors[selectedIndex]);
                String name = carIcons[selectedIndex] + " " + carNames[selectedIndex];
                FontMetrics fm = g2d.getFontMetrics();
                int nameX = getWidth()/2 - fm.stringWidth(name)/2;
                g2d.drawString(name, nameX, getHeight() - 50);
            }
        };
        previewPanel.setPreferredSize(new Dimension(800, 350));
        add(previewPanel, BorderLayout.CENTER);
        
        // Panel de botones con miniaturas de los coches
        JPanel carsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        carsPanel.setBackground(new Color(15, 15, 25));
        carsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        for (int i = 0; i < 3; i++) {
            final int index = i;
            JPanel card = createCarCard(carNames[i], carIcons[i], carImages[i], carColors[i]);
            card.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    selectedIndex = index;
                    previewPanel.repaint();
                    Toolkit.getDefaultToolkit().beep();
                }
                public void mouseEntered(MouseEvent e) {
                    card.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
                }
                public void mouseExited(MouseEvent e) {
                    card.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
                }
            });
            carsPanel.add(card);
        }
        
        add(carsPanel, BorderLayout.SOUTH);
        
        // Botones de navegación
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(15, 15, 25));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        
        JButton backBtn = createButton("◀ BACK", new Color(80, 60, 100));
        backBtn.addActionListener(e -> parent.showMenu());
        
        JButton selectBtn = createButton("SELECT ✓", new Color(50, 150, 50));
        selectBtn.addActionListener(e -> {
            TrackData.setPlayerCarColor(selectedIndex);
            parent.setCarSelected(selectedIndex, carNames[selectedIndex], carColors[selectedIndex]);
            parent.showMenu();
        });
        
        bottomPanel.add(backBtn);
        bottomPanel.add(selectBtn);
        add(bottomPanel, BorderLayout.NORTH);
    }
    
    private JPanel createCarCard(String name, String icon, Image carImage, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(20, 20, 35));
        card.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        card.setPreferredSize(new Dimension(220, 140));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Panel con la miniatura del coche
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(new Color(20, 20, 35));
                if (carImage != null) {
                    g.drawImage(carImage, 25, 10, 80, 50, this);
                } else {
                    g.setColor(color);
                    g.fillRect(30, 15, 70, 40);
                }
            }
        };
        imagePanel.setPreferredSize(new Dimension(200, 80));
        imagePanel.setBackground(new Color(20, 20, 35));
        
        JLabel nameLabel = new JLabel(icon + " " + name, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        nameLabel.setForeground(color);
        nameLabel.setBackground(new Color(0, 0, 0, 150));
        nameLabel.setOpaque(true);
        
        card.add(imagePanel, BorderLayout.CENTER);
        card.add(nameLabel, BorderLayout.SOUTH);
        
        return card;
    }
    
    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Monospaced", Font.BOLD, 20));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}