package ch.allred.racer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BackgroundSelector extends JPanel {
    private Racer parent;
    private int selectedIndex = 0;
    private JPanel previewPanel;
    
    private Color[] trackColors = {
        new Color(80, 85, 100),   // Original
        new Color(139, 90, 43),   // Desierto
        new Color(25, 25, 45),    // Noche
        new Color(45, 75, 45),    // Bosque
        new Color(100, 45, 45),   // Volcán
        new Color(45, 75, 95)     // Ártico
    };
    
    private String[] bgNames = {
        "ORIGINAL TRACK", "DESERT TRACK", "NIGHT TRACK",
        "FOREST TRACK", "VOLCANO TRACK", "ARCTIC TRACK"
    };
    
    public BackgroundSelector(Racer parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(new Color(15, 15, 25));
        
        // Título
        JLabel title = new JLabel("CHOOSE YOUR TRACK", SwingConstants.CENTER);
        title.setFont(new Font("Monospaced", Font.BOLD, 42));
        title.setForeground(new Color(100, 200, 255));
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        add(title, BorderLayout.NORTH);
        
        // Panel de preview SIMPLE
        previewPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                // Fondo con el color seleccionado
                g.setColor(trackColors[selectedIndex]);
                g.fillRect(0, 0, getWidth(), getHeight());
                
                
                g.setColor(Color.BLACK);
                int squareSize = 100;
                int squareX = getWidth()/2 - squareSize/2;
                int squareY = getHeight()/2 - squareSize/2;
                g.fillRect(squareX, squareY, squareSize, squareSize);
                
                // Nombre del fondo
                g.setFont(new Font("Monospaced", Font.BOLD, 20));
                g.setColor(Color.WHITE);
                String name = bgNames[selectedIndex];
                int nameX = getWidth()/2 - g.getFontMetrics().stringWidth(name)/2;
                g.drawString(name, nameX, getHeight() - 50);
            }
        };
        previewPanel.setPreferredSize(new Dimension(800, 350));
        previewPanel.setBorder(BorderFactory.createLineBorder(new Color(100, 200, 255), 3));
        add(previewPanel, BorderLayout.CENTER);
        
        // Grid de opciones
        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        gridPanel.setBackground(new Color(15, 15, 25));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        for (int i = 0; i < trackColors.length; i++) {
            final int index = i;
            JPanel card = createTrackCard(trackColors[i], bgNames[i]);
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
            gridPanel.add(card);
        }
        
        add(gridPanel, BorderLayout.SOUTH);
        
        // Botones
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(15, 15, 25));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        
        JButton backBtn = createButton("◀ BACK", new Color(80, 60, 100));
        backBtn.addActionListener(e -> parent.showMenu());
        
        JButton selectBtn = createButton("SELECT ✓", new Color(50, 150, 50));
        selectBtn.addActionListener(e -> {
            parent.setTrackBackground(trackColors[selectedIndex]);
            parent.showMenu();
        });
        
        bottomPanel.add(backBtn);
        bottomPanel.add(selectBtn);
        add(bottomPanel, BorderLayout.NORTH);
    }
    
    private JPanel createTrackCard(Color color, String name) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        card.setPreferredSize(new Dimension(200, 100));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Preview del color
        JPanel colorPreview = new JPanel();
        colorPreview.setBackground(color);
        colorPreview.setPreferredSize(new Dimension(180, 50));
        
        // Cuadrado negro pequeño dentro de la tarjeta
        JPanel blackSquare = new JPanel();
        blackSquare.setBackground(Color.BLACK);
        blackSquare.setPreferredSize(new Dimension(40, 40));
        colorPreview.setLayout(new GridBagLayout());
        colorPreview.add(blackSquare);
        
        JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Monospaced", Font.BOLD, 10));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBackground(new Color(0, 0, 0, 150));
        nameLabel.setOpaque(true);
        
        card.add(colorPreview, BorderLayout.CENTER);
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