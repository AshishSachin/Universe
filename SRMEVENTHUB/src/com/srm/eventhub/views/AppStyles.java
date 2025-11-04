package com.srm.eventhub.views;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;


public class AppStyles {

    
    public static final Color COLOR_BACKGROUND = new Color(30, 30, 30);      
    public static final Color COLOR_PANEL = new Color(45, 45, 45);        
    public static final Color COLOR_PRIMARY = new Color(138, 43, 226);     
    public static final Color COLOR_PRIMARY_LIGHT = new Color(153, 50, 204);  
    public static final Color COLOR_TEXT = Color.WHITE;                     
    public static final Color COLOR_INPUT_BG = new Color(60, 60, 60);      
    public static final Color COLOR_ERROR = new Color(255, 69, 58);       

    
    public static final Font FONT_TITLE = new Font("Arial", Font.BOLD, 24);
    public static final Font FONT_HEADING = new Font("Arial", Font.BOLD, 20);
    public static final Font FONT_LABEL = new Font("Arial", Font.PLAIN, 14);
    public static final Font FONT_BUTTON = new Font("Arial", Font.BOLD, 14);

    
    public static final Border BORDER_BUTTON_PADDING = BorderFactory.createEmptyBorder(10, 18, 10, 18);
    public static final Border BORDER_TEXTFIELD_PADDING = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_PANEL, 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
    );

    
    public static void styleFrame(JFrame frame) {
        frame.getContentPane().setBackground(COLOR_BACKGROUND);
    }

    
    public static void styleDialog(JDialog dialog) {
        dialog.getContentPane().setBackground(COLOR_BACKGROUND);
    }

    
    public static void stylePanel(JPanel panel) {
        panel.setBackground(COLOR_PANEL);
    }
    
    
    public static void styleTransparentPanel(JPanel panel) {
        panel.setBackground(COLOR_BACKGROUND);
    }

    
    public static void styleTitle(JLabel label) {
        label.setFont(FONT_TITLE);
        label.setForeground(COLOR_PRIMARY);
    }

    
    public static void styleLabel(JLabel label) {
        label.setFont(FONT_LABEL);
        label.setForeground(COLOR_TEXT);
    }
    
    
    public static void styleTextAreaAsLabel(JTextArea textArea) {
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setFont(FONT_LABEL);
        textArea.setForeground(COLOR_TEXT);
    }

    
    public static void styleButton(JButton button) {
        button.setFont(FONT_BUTTON);
        button.setBackground(COLOR_PRIMARY);
        button.setForeground(COLOR_TEXT);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBorder(BORDER_BUTTON_PADDING);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(COLOR_PRIMARY_LIGHT);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(COLOR_PRIMARY);
            }
        });
    }

    
    public static void styleTextField(JTextField field) {
        field.setBackground(COLOR_INPUT_BG);
        field.setForeground(COLOR_TEXT);
        field.setCaretColor(COLOR_PRIMARY);
        field.setSelectionColor(COLOR_PRIMARY);
        field.setSelectedTextColor(COLOR_TEXT);
        field.setBorder(BORDER_TEXTFIELD_PADDING);
        field.setFont(FONT_LABEL);
    }

    
    public static void stylePasswordField(JPasswordField field) {
        field.setBackground(COLOR_INPUT_BG);
        field.setForeground(COLOR_TEXT);
        field.setCaretColor(COLOR_PRIMARY);
        field.setSelectionColor(COLOR_PRIMARY);
        field.setSelectedTextColor(COLOR_TEXT);
        field.setBorder(BORDER_TEXTFIELD_PADDING);
        field.setFont(FONT_LABEL);
    }
    
    
    public static void styleTable(JTable table, JScrollPane scrollPane) {
        
        table.setBackground(COLOR_PANEL);
        table.setForeground(COLOR_TEXT);
        table.setGridColor(COLOR_INPUT_BG);
        table.setFont(FONT_LABEL);
        table.setRowHeight(25);
        table.setSelectionBackground(COLOR_PRIMARY);
        table.setSelectionForeground(COLOR_TEXT);

       
        table.getTableHeader().setFont(FONT_BUTTON);
        table.getTableHeader().setBackground(COLOR_INPUT_BG);
        table.getTableHeader().setForeground(COLOR_PRIMARY);
        table.getTableHeader().setOpaque(true);
        table.getTableHeader().setBorder(BorderFactory.createLineBorder(COLOR_INPUT_BG));

        
        scrollPane.getViewport().setBackground(COLOR_PANEL);
        scrollPane.setBackground(COLOR_PANEL);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_PANEL, 1));
    }
}