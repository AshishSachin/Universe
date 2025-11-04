package com.srm.eventhub.views;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class UniVerseBanner extends JPanel {
    private ArrayList<Star> stars;
    private float logoOffset = 0;
    private float glowScale = 1.0f;
    private float orbit1Angle = 0;
    private float orbit2Angle = 0;
    private float orbit3Angle = 0;
    private Random random;

    public UniVerseBanner(JFrame parentFrame) {
        random = new Random();
        stars = new ArrayList<>();
        
        setLayout(new BorderLayout());

        
        JPanel controlPanel = new JPanel(new GridBagLayout());
        AppStyles.styleTransparentPanel(controlPanel); 
        
        
        controlPanel.setOpaque(false); 
        

        GridBagConstraints gbc = new GridBagConstraints();

        
        JLabel promptLabel = new JLabel("Please select your portal:");
        AppStyles.styleLabel(promptLabel);
        promptLabel.setFont(AppStyles.FONT_HEADING);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; 
        gbc.insets = new Insets(10, 10, 15, 10); 
        gbc.anchor = GridBagConstraints.CENTER;
        controlPanel.add(promptLabel, gbc);

       
        gbc.gridy = 1; 
        gbc.gridwidth = 1; 
        gbc.insets = new Insets(10, 10, 30, 10); 
        
        JButton userPortalButton = new JButton("General User");
        AppStyles.styleButton(userPortalButton);
        
        JButton organizerPortalButton = new JButton("Event Organizer");
        AppStyles.styleButton(organizerPortalButton);
        
        gbc.gridx = 0; 
        controlPanel.add(userPortalButton, gbc); 
        
        gbc.gridx = 1; // Column 1
        controlPanel.add(organizerPortalButton, gbc);
        
        // --- Add control panel to the SOUTH ---
        add(controlPanel, BorderLayout.SOUTH);

        // --- ATTACH ACTION LISTENERS ---
        userPortalButton.addActionListener(e -> {
            new UserLoginFrame().setVisible(true);
            parentFrame.dispose(); 
        });

        organizerPortalButton.addActionListener(e -> {
            new OrganizerLoginFrame().setVisible(true);
            parentFrame.dispose(); 
        });

        
        for (int i = 0; i < 100; i++) {
            stars.add(new Star(
                random.nextFloat(), 
                random.nextFloat(), 
                random.nextFloat() * 2 + 1,
                random.nextFloat() * 0.5f + 0.3f,
                random.nextFloat() * 0.02f + 0.01f
            ));
        }

        
        Timer timer = new Timer(30, e -> {
            logoOffset = (float) (Math.sin(System.currentTimeMillis() / 500.0) * 10);
            glowScale = (float) (1.0 + Math.sin(System.currentTimeMillis() / 1000.0) * 0.2);
            orbit1Angle += 0.02f;
            orbit2Angle -= 0.015f;
            orbit3Angle += 0.01f;
            for (Star star : stars) {
                star.update();
            }
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        int centerY = height / 2;

        
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(102, 126, 234),
            width, height, new Color(240, 147, 251)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);

        
        for (Star star : stars) {
            star.draw(g2d, width, height); 
        }

        
        int glowSize = (int) (height * 1.5 * glowScale); 
        RadialGradientPaint glowGradient = new RadialGradientPaint(
            centerX, centerY, glowSize / 2,
            new float[]{0.0f, 0.7f, 1.0f},
            new Color[]{
                new Color(255, 255, 255, 30),
                new Color(255, 255, 255, 10),
                new Color(255, 255, 255, 0)
            }
        );
        g2d.setPaint(glowGradient);
        g2d.fillOval(centerX - glowSize / 2, centerY - glowSize / 2, glowSize, glowSize);

        
        drawOrbit(g2d, centerX, centerY, (int)(height * 0.4), orbit1Angle, 2);
        drawOrbit(g2d, centerX, centerY, (int)(height * 0.6), orbit2Angle, 2);
        drawOrbit(g2d, centerX, centerY, (int)(height * 0.8), orbit3Angle, 2);

        
        int logoY = (int) ((height * 0.45) + logoOffset); 
        
        int titleFontSize = Math.min(width, height) / 6;
        g2d.setFont(new Font("Arial", Font.BOLD, titleFontSize));
        
        String uni = "Uni";
        FontMetrics fm = g2d.getFontMetrics();
        int uniWidth = fm.stringWidth(uni);
        int logoX = centerX - uniWidth + 5;
        
        g2d.setColor(Color.WHITE);
        g2d.drawString(uni, logoX, logoY);

        String verse = "Verse";
        GradientPaint goldGradient = new GradientPaint(
            centerX, logoY - 50, new Color(255, 215, 0),
            centerX, logoY + 50, new Color(255, 237, 78)
        );
        g2d.setPaint(goldGradient);
        g2d.drawString(verse, logoX + uniWidth, logoY);

        
        int taglineFontSize = Math.max(12, titleFontSize / 4);
        g2d.setFont(new Font("Arial", Font.PLAIN, taglineFontSize));
        g2d.setColor(new Color(255, 255, 255, 240));
        String tagline = "E V E N T  M A N A G E M E N T";
        int taglineWidth = g2d.getFontMetrics().stringWidth(tagline);
        g2d.drawString(tagline, centerX - taglineWidth / 2, logoY + (int)(taglineFontSize * 1.5));
    }

    private void drawOrbit(Graphics2D g2d, int cx, int cy, int radius, float angle, int thickness) {
        g2d.setColor(new Color(255, 255, 255, 25));
        g2d.setStroke(new BasicStroke(thickness));
        g2d.drawOval(cx - radius, cy - radius, radius * 2, radius * 2);

        int planetX = (int) (cx + Math.cos(angle) * radius);
        int planetY = (int) (cy + Math.sin(angle) * radius);
        int planetSize = Math.max(8, (int)(radius * 0.05)); 

        RadialGradientPaint planetGlow = new RadialGradientPaint(
            planetX, planetY, planetSize,
            new float[]{0.0f, 0.5f, 1.0f},
            new Color[]{ new Color(255, 215, 0, 200), new Color(255, 215, 0, 100), new Color(255, 215, 0, 0)}
        );
        g2d.setPaint(planetGlow);
        g2d.fillOval(planetX - planetSize, planetY - planetSize, planetSize * 2, planetSize * 2);

        RadialGradientPaint planetGradient = new RadialGradientPaint(
            planetX - 3, planetY - 3, planetSize / 2,
            new float[]{0.0f, 1.0f},
            new Color[]{Color.WHITE, new Color(255, 215, 0)}
        );
        g2d.setPaint(planetGradient);
        g2d.fillOval(planetX - planetSize / 2, planetY - planetSize / 2, planetSize, planetSize);
    }

    private class Star {
        float relX, relY, size, opacity, speed; 
        boolean growing = true;

        Star(float relX, float relY, float size, float opacity, float speed) {
            this.relX = relX;
            this.relY = relY;
            this.size = size;
            this.opacity = opacity;
            this.speed = speed;
        }

        void update() {
            if (growing) {
                opacity += speed;
                if (opacity >= 1.0f) {
                    opacity = 1.0f;
                    growing = false;
                }
            } else {
                opacity -= speed;
                if (opacity <= 0.3f) {
                    opacity = 0.3f;
                    growing = true;
                }
            }
        }

        void draw(Graphics2D g2d, int panelWidth, int panelHeight) {
            g2d.setColor(new Color(255, 255, 255, (int) (opacity * 255)));
            int x = (int) (relX * panelWidth);
            int y = (int) (relY * panelHeight);
            g2d.fillOval(x, y, (int) size, (int) size);
        }
    }
}