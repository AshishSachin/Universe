package com.srm.eventhub.views;

import javax.swing.*;
import java.awt.*;
import java.net.URL;


public class ImagePanel extends JPanel {

    private Image backgroundImage;

    public ImagePanel(String resourcePath) {
        try {
            
            URL imageUrl = getClass().getClassLoader().getResource(resourcePath);
            if (imageUrl != null) {
                this.backgroundImage = new ImageIcon(imageUrl).getImage();
            } else {
                System.err.println("Banner image not found at: " + resourcePath);
                this.backgroundImage = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.backgroundImage = null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            
            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        } else {
            
            g.setColor(Color.RED);
            g.drawString("Image not found", 10, 20);
        }
    }
}