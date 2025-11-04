package com.srm.eventhub;

import javax.swing.SwingUtilities;
import com.srm.eventhub.views.WelcomeFrame;

public class MainApp {
    public static void main(String[] args) {
        // Use invokeLater to ensure all UI updates are done on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            WelcomeFrame welcomeFrame = new WelcomeFrame();
            welcomeFrame.setVisible(true);
        });
    }
}
