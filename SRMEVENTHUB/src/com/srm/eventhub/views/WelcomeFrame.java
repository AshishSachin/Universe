package com.srm.eventhub.views;

import javax.swing.*;
import java.awt.*;

public class WelcomeFrame extends JFrame {

    public WelcomeFrame() {
        setTitle("Welcome to UniVerse"); 
        
        setSize(800, 500); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout()); 

        
        AppStyles.styleFrame(this);
        
       
        UniVerseBanner bannerPanel = new UniVerseBanner(this);
        
        
        add(bannerPanel, BorderLayout.CENTER);
    }
}