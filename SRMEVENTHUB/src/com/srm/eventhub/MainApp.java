package com.srm.eventhub;

import javax.swing.SwingUtilities;
import com.srm.eventhub.views.WelcomeFrame;

public class MainApp {
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
            WelcomeFrame welcomeFrame = new WelcomeFrame();
            welcomeFrame.setVisible(true);
        });
    }
}
